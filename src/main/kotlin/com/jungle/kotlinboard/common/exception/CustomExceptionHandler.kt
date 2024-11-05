package com.jungle.kotlinboard.common.exception

import com.jungle.kotlinboard.common.BaseResponse
import com.jungle.kotlinboard.common.ResultCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun methodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage
            errors[fieldName] = errorMessage ?: "Not Exception Message"
        }

        return ResponseEntity(BaseResponse(ResultCode.ERROR.name, errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidInputException::class)
    protected fun invalidInputException(ex: InvalidInputException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))

        return ResponseEntity(BaseResponse(ResultCode.ERROR.name, errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BadCredentialsException::class)
    protected fun badCredentialsException(ex: BadCredentialsException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf("로그인 실패" to "아이디 혹은 비밀번호를 다시 확인하세요.")

        return ResponseEntity(BaseResponse(ResultCode.ERROR.name, errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MemberNotFoundException::class)
    protected fun memberNotFoundException(ex: MemberNotFoundException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf("멤버 찾기 실패" to "멤버 id를 다시 확인하세요")

        return ResponseEntity(BaseResponse(ResultCode.ERROR.name, errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NotAuthorizedException::class)
    protected fun memberNotMatchException(ex: NotAuthorizedException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf("멤버와 작성자가 일치 실패" to "멤버 id와 작성자를 다시 확인하세요")

        return ResponseEntity(BaseResponse(ResultCode.ERROR.name, errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(PostNotFoundException::class)
    protected fun postNotFoundException(ex: PostNotFoundException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf("게시글 찾기 실패" to "게시글 id를 다시 확인하세요")

        return ResponseEntity(BaseResponse(ResultCode.ERROR.name, errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    protected fun defaultException(ex: Exception): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf("미처리 에러" to (ex.message ?: "Not Exception Message"))

        return ResponseEntity(BaseResponse(ResultCode.ERROR.name, errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidTokenException::class)
    protected fun invalidTokenException(ex: InvalidTokenException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf("토큰 실패" to "토큰값을 확인하세요")

        return ResponseEntity(BaseResponse(ResultCode.ERROR.name, errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }
}
