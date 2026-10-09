package com.Booking_Serivce.Constant;

public enum BookingStatus {
    PENDING,
    LOCKED,
    CONFIRMED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    NO_SHOW,
    CHECKED_IN,
    PAYMENT_FAILED // add this — payment failed, slot must be released
}

// Add Later after the kafka setuo
//@KafkaListener(topics = "paymentBookingEvent", groupId = "booking-service")
//public void handlePaymentEvent(PaymentBookingEvent event) {
//    Booking booking = bookingRepository.findById(event.getBookingId())
//            .orElseThrow(...);
//
//    if (event.getStatus() == PaymentStatus.SUCCESS) {
//        booking.setBookingStatus(BookingStatus.CONFIRMED);
//    } else {
//        booking.setBookingStatus(BookingStatus.PAYMENT_FAILED); // frees capacity
//    }
//    bookingRepository.save(booking);
//}