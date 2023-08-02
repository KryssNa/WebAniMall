package com.example.AniMall.Services;

import com.example.AniMall.Entity.Booking;
import com.example.AniMall.Entity.ShippingDetails;
import com.example.AniMall.Pojo.BookingPojo;

import java.util.List;

public interface BookingServices {

    BookingPojo save(BookingPojo bookingPojo);

    List<Booking> findBookingByUserId(Integer id);

    List<Booking> findBookingById(Integer id);

    List<Booking> findAll();

    void updateStatus(Integer id, String status);

    List<ShippingDetails> findAllShipping();

    Booking findById(Integer id);

    void deleteById(Integer id);





    List<ShippingDetails> findShippingByUserId(Integer id);

    List<ShippingDetails> findShippingId(Integer id);
}
