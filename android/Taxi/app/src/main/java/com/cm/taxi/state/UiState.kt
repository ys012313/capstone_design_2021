package com.cm.taxi.state

sealed class UiState<T> {
    class Loading<T>(val isLoading : Boolean) : UiState<T>()
    class Success<T>(val data: T) : UiState<T>()
    class Failed<T>(val message: String) : UiState<T>()

    companion object {
        fun <T> loading(isLoading : Boolean) = Loading<T>(isLoading)
        fun <T> success(data: T) = Success(data)
        fun <T> failed(message: String) = Failed<T>(message)
    }
}
