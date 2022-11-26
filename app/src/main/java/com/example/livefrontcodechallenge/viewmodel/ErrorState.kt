package com.example.livefrontcodechallenge.viewmodel

sealed class ErrorState {
  data class ApiError(val msg: String, val code: Int) : ErrorState()
  object NetworkError : ErrorState()
  object GenericError : ErrorState()
}