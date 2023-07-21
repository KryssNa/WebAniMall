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

    private String status;

    private String shippingEmail;

    private String shippingFullName;

    private String shippingPhone;

    private String shippingAddress;

    private double total;

    private int quantity;

//    @jakarta.validation.constraints.NotNull(message = "Image can't be null")
    private MultipartFile image;

    public BookingPojo(Booking booking) {
        this.id= booking.getId();
        this.pet_id= booking.getPet().getId();
        this.user_id= booking.getUser().getId();
        this.status= booking.getStatus();
        this.total= booking.getTotal();
        this.quantity= booking.getQuantity();
        this.shippingEmail= booking.getShippingEmail();
        this.shippingFullName= booking.getShippingFullName();
        this.shippingPhone= booking.getShippingPhone();
        this.shippingAddress= booking.getShippingAddress();
    }
}
