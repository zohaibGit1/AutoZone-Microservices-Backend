package com.Booking_Serivce.Exception.ExceptionClass;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
