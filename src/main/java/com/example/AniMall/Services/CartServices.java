package com.example.AniMall.Services;

import com.example.AniMall.Entity.Cart;
import com.example.AniMall.Pojo.BookingPojo;
import com.example.AniMall.Pojo.CartPojo;

import java.security.Principal;
import java.util.List;

public interface CartServices {

    String saveToCart(Integer id, Principal principal);

    String deleteFromCart(Integer id);

    String updateQuantity(Cart cart);

    List<Cart> fetchAll(Integer id);

    Cart fetchOne(Integer id);

    String checkout(Integer id, BookingPojo pojo, List<Cart> itemsToPurchase);

    List<Cart> fetchAllCart();

    List<Cart> fetchAllCartByStatus(String status);

    List<Object[]> fetchstatusCount();

    List<String> distinctstatus();

    List<Cart> cartdetails();

    String updatePet(double quantity, Integer id);

    void updatecartstatus(Integer id,String status);

    List<Cart> fetchAvailable(Integer id);

}
