package com.example.AniMall.Pojo;

import com.example.AniMall.Entity.ShippingDetails;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDetailsDto {

    private Integer id;

    private Integer user_id;

    private double totalPrice;

    private String status;

    private int totalQuantity;

    //@jakarta.validation.constraints.NotNull(message = "Image can't be null")
    private MultipartFile image;

    private String shippingFullName;

    private String shippingEmail;

    private String shippingPhone;

    private String shippingAddress;

    public ShippingDetailsDto(ShippingDetails shippingDetails) {
        this.id = shippingDetails.getId();
        this.user_id = shippingDetails.getUser().getId();
        this.totalPrice = shippingDetails.getTotalPrice();
        this.status = shippingDetails.getStatus();
        this.totalQuantity = shippingDetails.getTotalQuantity();
        this.shippingFullName = shippingDetails.getShippingFullName();
        this.shippingEmail = shippingDetails.getShippingEmail();
        this.shippingPhone = shippingDetails.getShippingPhone();
        this.shippingAddress = shippingDetails.getShippingAddress();

    }

}
