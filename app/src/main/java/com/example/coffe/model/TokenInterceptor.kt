package com.example.coffe.model

import com.example.coffe.util.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(
    private val sessionManager: SessionManager
    ):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if(request.url().encodedPath().contains("/login") &&
                request.method().equals("post") ||
            request.url().encodedPath().contains("/register") &&
            request.method().equals("post"))
                return chain.proceed(request)

        val requestBuilder = request.newBuilder()
            .addHeader("Authorization","Bearer ${sessionManager.getToken().token}")
            .url(request.url())

        return chain.proceed(requestBuilder.build())
    }

}