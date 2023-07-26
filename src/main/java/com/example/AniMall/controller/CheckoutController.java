package com.example.AniMall.controller;

import com.example.AniMall.Entity.Cart;
import com.example.AniMall.Pojo.BookingPojo;
import com.example.AniMall.Pojo.ShippingDetailsDto;
import com.example.AniMall.Repo.SummaryDetailsRepo;
import com.example.AniMall.Services.BookingServices;
import com.example.AniMall.Services.CartServices;
import com.example.AniMall.Services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class CheckoutController {
    private final UserServices userService;
    private final CartServices cartService;
    private final BookingServices bookingServices;
    final SummaryDetailsRepo summaryDetailsRepo ;

    @PostMapping("/checkout")
    public String checkkout(Principal principal, Model model){
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

    @PostMapping("/checkout/confirm")
    public String checkoutProcess(Principal principal, @Valid BookingPojo pojo, @Valid ShippingDetailsDto shippingDetailsDto, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            // If there are validation errors, redirect back to the checkout page to display the errors
            System.out.println("error"+bindingResult.getAllErrors());
            return "redirect:/cart";
        }
        Integer id = userService.findByEmail(principal.getName()).getId();

        List<Cart> list = cartService.fetchAvailable(id);

        for (Cart cartItem : list) {
            cartService.updatePet(cartItem.getPet().getQuantity() - cartItem.getQuantity(), cartItem.getPet().getId());
        }

        System.out.println("checkout process");
        cartService.checkout(id, pojo, shippingDetailsDto, list);

        System.out.println("summary details");
        // Clear the user's cart after successful checkout
//        cartService.clearCart(id);

        return "redirect:/user/homepage";
    }
}

