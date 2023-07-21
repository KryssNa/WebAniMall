package com.example.AniMall.Services;

import com.example.AniMall.Entity.Booking;
import com.example.AniMall.Pojo.BookingPojo;

import java.util.List;

public interface BookingServices {
    BookingPojo save(BookingPojo bookingPojo);
    List<Booking> findBookingById(Integer id);
    List<Booking> findAll();
    Booking findById(Integer id);
    void deleteById(Integer id);

    boolean existsById(Integer id);

    //checkout
    BookingPojo saveCheckout(BookingPojo bookingPojo);
}
