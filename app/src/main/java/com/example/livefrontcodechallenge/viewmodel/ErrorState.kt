package com.example.livefrontcodechallenge.viewmodel

sealed class ErrorState {
  data class ApiError(val msg: String) : ErrorState()
  object NetworkError : ErrorState()
  object GenericError : ErrorState()
}