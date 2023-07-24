package com.example.AniMall.Services;

import com.example.AniMall.Entity.Booking;
import com.example.AniMall.Entity.ShippingDetails;
import com.example.AniMall.Pojo.BookingPojo;
import com.example.AniMall.Pojo.ShippingDetailsDto;

import java.io.IOException;
import java.util.List;

public interface BookingServices {
    BookingPojo save(BookingPojo bookingPojo);
    List<Booking> findBookingById(Integer id);

    List<ShippingDetails> findShippingById(Integer id);
    List<Booking> findAll();

    List<ShippingDetails> findAllShipping();

    Booking findById(Integer id);
    void deleteById(Integer id);

    boolean existsById(Integer id);

    public String summaryCheckout(Integer id, BookingPojo pojo, ShippingDetailsDto shippingDetailsDto) throws IOException;

    //checkout
    BookingPojo saveCheckout(BookingPojo bookingPojo);
}
