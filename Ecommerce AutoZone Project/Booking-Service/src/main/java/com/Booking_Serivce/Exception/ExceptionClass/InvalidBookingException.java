package com.Booking_Serivce.Exception.ExceptionClass;

public class InvalidBookingException extends RuntimeException {
    public InvalidBookingException(String message) {
        super(message);
    }
}
