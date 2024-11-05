package com.jungle.kotlinboard.common.exception

class InvalidInputException(
    val fieldName: String = "",
    message: String = "Invalid Input",
) : RuntimeException(message)
