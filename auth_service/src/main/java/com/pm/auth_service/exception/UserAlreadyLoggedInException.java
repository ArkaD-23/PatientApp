package com.pm.auth_service.exception;

import lombok.experimental.StandardException;

@StandardException
public class UserAlreadyLoggedInException extends RuntimeException {
}
