package com.aroman.mimwallet.common

sealed class ViewState<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(val successData: T) : ViewState<T>(successData)
    class Error<T>(message: String, data: T? = null) : ViewState<T>(data, message)
    class Loading<T>(data: T? = null) : ViewState<T>(data)
}