package com.example.AniMall.controller;

import com.example.AniMall.Entity.Cart;
import com.example.AniMall.Pojo.BookingPojo;
import com.example.AniMall.Pojo.ShippingDetailsDto;
import com.example.AniMall.Repo.ShippingRepo;
import com.example.AniMall.Services.BookingServices;
import com.example.AniMall.Services.CartServices;
import com.example.AniMall.Services.EmailService;
import com.example.AniMall.Services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
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
    private final EmailService emailService;

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
            System.out.println("error"+bindingResult.getAllErrors());
            return "redirect:/cart";
        }
        Integer id = userService.findByEmail(principal.getName()).getId();

        List<Cart> list = cartService.fetchAvailable(id);

        for (Cart cartItem : list) {
            cartService.updatePet(cartItem.getPet().getQuantity() - cartItem.getQuantity(), cartItem.getPet().getId());
        }

        cartService.checkout(id, pojo, shippingDetailsDto, list);


        sendEmail(principal);

        return "redirect:/user/homepage";
    }

    // sending confirmation email
    @Async
    void sendEmail(Principal principal) {
        try {


        String to = principal.getName();
        System.out.println("email: "+to);
        String subject = "Order Confirmation";
        String text = "Your order has been Received. \n \n We will contact you soon. \nThank you for shopping with us. \n\n Regards, \n AniMall";
        if (emailService == null) System.out.println("emailService is null");
        else {
        emailService.sendEmail(to, subject, text);}}
        catch (Exception e) {
            System.out.println("error in sending email: "+e);
        }
    }
}





