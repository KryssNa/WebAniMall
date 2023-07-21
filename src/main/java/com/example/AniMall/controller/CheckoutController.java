package com.example.AniMall.controller;

import com.example.AniMall.Entity.Cart;
import com.example.AniMall.Pojo.BookingPojo;
import com.example.AniMall.Services.CartServices;
import com.example.AniMall.Services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class CheckoutController {
    private final UserServices userService;
    private final CartServices cartService;

    @PostMapping("/checkout")
    public String displayCart(Principal principal, Model model){
        Integer id = userService.findByEmail(principal.getName()).getId();
        List<Cart> list = cartService.fetchAll(id);

        double total = 0.0;
        for(Cart totalCalc:list){
            if (totalCalc.getPet() != null && totalCalc.getPet().getQuantity() != null){
                total += totalCalc.getQuantity()*totalCalc.getPet().getPrice();
            }
//            if (totalCalc.getPet().getQuantity()>0) total += totalCalc.getQuantity()*totalCalc.getPet().getPrice();
        }
        model.addAttribute("total", total);
        model.addAttribute("cartItems", list);
        model.addAttribute("checkout", new BookingPojo());

        return "redirect:/checkout/confirm";
    }

    @GetMapping("/checkout/confirm")
    public String checkoutProcess(Principal principal, @Valid BookingPojo pojo){
        Integer id = userService.findByEmail(principal.getName()).getId();
        List<Cart> list = cartService.fetchAvailable(id);

        for (Cart cartItem: list){
            cartService.updatePet(cartItem.getPet().getQuantity()-cartItem.getQuantity(),cartItem.getPet().getId());
        }

        cartService.checkout(id, pojo, list);
        return "redirect:/user/homepage";
    }
}

