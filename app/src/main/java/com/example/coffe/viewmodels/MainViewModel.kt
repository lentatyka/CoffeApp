package com.example.coffe.viewmodels

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffe.adapters.items.CartItem
import com.example.coffe.adapters.items.LocationItem
import com.example.coffe.adapters.items.MenuItem
import com.example.coffe.adapters.items.Result
import com.example.coffe.model.responces.LocationResponse
import com.example.coffe.model.responces.MenuResponse
import com.example.coffe.repository.CoffeRepository
import com.example.coffe.util.SessionManager
import com.example.coffe.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CoffeRepository,
    private val userManager: SessionManager,
    app: Application
) : AndroidViewModel(app) {
    private val _state = MutableStateFlow<State<List<Result>>>(State.Loading)
    val state: StateFlow<State<List<Result>>> = _state.asStateFlow()
    private val listMenu = mutableListOf<MenuItem>()
    private val listCart = mutableListOf<CartItem>()
    private val listLocation = mutableListOf<LocationItem>()

    fun updateLocation(location: Location) {
        viewModelScope.launch {
            _state.value = State.Loading
            repository.getLocations(userManager.getToken().token!!).also { result ->
                if (result.isSuccessful && result.code() == 200) {
                    listLocation.clear()
                    result.body()!!.forEach {
                        listLocation += getLocationItemEntry(location, it)
                    }
                    _state.value = State.Success(listLocation)
                } else {
                    _state.value = State.Error(Exception(result.message()))
                }
            }
        }
    }

    fun setLocation() {
        listMenu.clear()    //Clear stack
        _state.value = State.Success(listLocation)
    }

    /*
    Стоит ли всякий раз делать запрос возвращаясь из корзины или только обновлять количество?
    Выбрал вариант два. Вместо передачи, например, отрицательного id проверяем был ли список заполнен
     */
    fun getMenu(id: Long) {
        if (listMenu.isEmpty()) {
            //From Location fragment
            _state.value = State.Loading
            listMenu.clear()
            viewModelScope.launch {
                repository.getMenu(id, userManager.getToken().token!!).also { result ->
                    if (result.isSuccessful && result.code() == 200) {
                        result.body()!!.forEach {
                            listMenu += getMenuItemEntry(it)
                        }
                        _state.value = State.Success(listMenu)
                    } else {
                        _state.value = State.Error(Exception())
                    }
                }
            }
        } else {
            //Returned from Cart fragment
            listCart.forEach { cartItem ->
                listMenu.find { it.id == cartItem.id }?.let {
                    it.amount = cartItem.amount
                }
            }
            _state.value = State.Success(listMenu)
        }
    }

    fun getCart(): List<Result> {
        listCart.clear()
        listMenu.filter { menuItem ->
            menuItem.amount > 0
        }.onEach {
            listCart += getCartItemEntry(it)
        }
        return listCart
    }

    private fun getCartItemEntry(menuItem: MenuItem) = CartItem(
        id = menuItem.id,
        name = menuItem.name,
        price = menuItem.price,
        _amount = menuItem.amount
    )

    private fun getMenuItemEntry(menu: MenuResponse) = MenuItem(
        id = menu.id,
        name = menu.name,
        imageURL = menu.imageURL,
        price = menu.price.toInt(),
    )

    private fun getLocationItemEntry(
        myLocation: Location,
        objectLocation: LocationResponse
    ): LocationItem {
        val distance = myLocation.distanceTo(Location("").apply {
            latitude = objectLocation.point.latitude
            longitude = objectLocation.point.longitude
        })
        return LocationItem(
            id = objectLocation.id,
            name = objectLocation.name,
            latitude = objectLocation.point.latitude,
            longitude = objectLocation.point.longitude,
            distance = distance.toInt()
        )
    }

    fun setMenuAmount(id: Double, amount: Int) {
        listMenu.find { it.id == id }?.let {
            it.amount = amount
        }
    }

    fun setCartAmount(id: Double, amount: Int) {
        listCart.find { it.id == id }?.let {
            it.amount = amount
        }
    }

    fun getTotalPrice() = listCart.filter { it.amount > 0 }.sumOf { it.price * it.amount }

    fun cartNotEmpty(): Boolean {
        return listMenu.find { it.amount > 0 } != null
    }

    fun getLocationCollection() = listLocation

}