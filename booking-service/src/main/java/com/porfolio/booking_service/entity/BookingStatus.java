package com.porfolio.booking_service.entity;

public enum BookingStatus {
    PENDING, //<-- pendiente de confirmacion
    CONFIRMED, //<-- confirmada
    CANCELLED, //<-- candelada por el usuario
    REJECTED, //<-- rechazada por disponibilidad
    COMPLETED //<-- completada despues del checkout
}
