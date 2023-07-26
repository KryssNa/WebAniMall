package com.example.AniMall.Services;

import com.example.AniMall.Entity.Cart;
import com.example.AniMall.Pojo.BookingPojo;
import com.example.AniMall.Pojo.ShippingDetailsDto;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface CartServices {

    String saveToCart(Integer id, Principal principal);

    String deleteFromCart(Integer id);

    String updateCartDetails(Cart cart);

    List<Cart> fetchAll(Integer id);

    Cart fetchOne(Integer id);

    String checkout(Integer id, BookingPojo pojo, ShippingDetailsDto shippingDetailsDto, List<Cart> itemsToPurchase) throws IOException;

    List<Cart> fetchAllCart();

    List<Cart> fetchAllCartByStatus(String status);
    int updatePet(int quantity, Integer id);


    List<Cart> fetchAvailable(Integer id);

    void clearCart(Integer id);
}
