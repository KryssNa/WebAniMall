package com.example.AniMall.Pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.example.AniMall.Entity.Booking;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingPojo {

    @Positive(message = "ID must be a positive number.")
    private Integer id;

    @NotNull(message = "Pet ID cannot be null.")
    @Positive(message = "Pet ID must be a positive number.")
    private int pet_id;

    @NotNull(message = "User ID cannot be null.")
    @Positive(message = "User ID must be a positive number.")
    private int user_id;

    @NotNull(message = "Shipping ID cannot be null.")
    @Positive(message = "Shipping ID must be a positive number.")
    private int shipping_id;

    private String status;


    private int quantity;

    private double price;

    public BookingPojo(Booking booking) {
        this.id= booking.getId();
        this.pet_id= booking.getPet().getId();
        this.user_id= booking.getUser().getId();
        this.shipping_id= booking.getShippingDetails().getId();
        this.quantity= booking.getQuantity();
        this.price= booking.getPrice();
        this.status= booking.getStatus();
    }
}
