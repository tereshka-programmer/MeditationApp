package com.example.tranyapp.utils

class Event<T>(
    value: T
) {

    private var _value: T? = value

    fun get() = _value.also { _value = null }

}