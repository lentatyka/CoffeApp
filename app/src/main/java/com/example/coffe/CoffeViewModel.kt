package com.example.coffe

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffe.adapters.items.CartItem
import com.example.coffe.adapters.items.LocationItem
import com.example.coffe.adapters.items.MenuItem
import com.example.coffe.adapters.items.Result
import com.example.coffe.model.responces.*
import com.example.coffe.repository.CoffeRepository
import com.example.coffe.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoffeViewModel @Inject constructor(
    private val repository: CoffeRepository,
    app: Application
) : AndroidViewModel(app) {
    private lateinit var token: String //temp
    private val _state = MutableStateFlow<State<List<Result>>>(State.Loading)
    val state: StateFlow<State<List<Result>>> = _state.asStateFlow()
    private val listMenu = mutableListOf<MenuItem>()
    private val listCart = mutableListOf<CartItem>()
    /*
    Авторизацию перенести в отдельный viewmodel и "AuthActivity"!
    Данные авторизации храним в sharedPref.
     */

    fun signUp(login: String, password: String) {

        viewModelScope.launch {
            repository.signUp(
                AuthBody("lentatyka@gmail.com", "1234567")
            ).also { result ->
                if (result.isSuccessful && result.code() == 200) {
                    //
                } else {
                    //error answer
                }
            }
        }
    }

    fun signIn(login: String, password: String) {
        viewModelScope.launch {
            repository.signIn(
                AuthBody("lentatyka@gmail.com", "1234567")
            ).also { result ->
                if (result.isSuccessful && result.code() == 200) {
                    token = result.body()!!.token
                } else {
                    //error answer
                }
            }
        }
    }

    fun getLocations(location: Location) {
        listMenu.clear()    //Clear stack
        viewModelScope.launch {
            _state.value = State.Loading
            repository.getLocations(token).also { result ->
                if (result.isSuccessful && result.code() == 200) {
                    val list = mutableListOf<Result>()
                    result.body()!!.forEach {
                        list += getLocationItemEntry(location, it)
                    }
                    _state.value = State.Success(list)
                } else {
                    _state.value = State.Error(Exception(result.message()))
                }
            }
        }
    }

    fun getMenu(id: Long) {
        //When the screen orientation is changed, the data will not update
        if (listMenu.isEmpty()) {
            //From Location fragment
            _state.value = State.Loading
            listMenu.clear()
            viewModelScope.launch {
                repository.getMenu(id, token).also { result ->
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
        price = menu.price,
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


}