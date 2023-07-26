package com.example.AniMall.controller;

import com.example.AniMall.Entity.Cart;
import com.example.AniMall.Pojo.CartPojo;
import com.example.AniMall.Pojo.FavoritePojo;
import com.example.AniMall.Services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final UserServices userService;
    private final CartServices cartService;
    private final FavoriteServices favoriteServices;

    @GetMapping()
    public String displayCart(Principal principal, Model model, @ModelAttribute("cartPojo") CartPojo cartPojo) {
        Integer id = userService.findByEmail(principal.getName()).getId();
        List<Cart> cartItems = cartService.fetchAll(id);

        double total = 0.0;
        for (Cart cart : cartItems) {
            total += cart.getQuantity() * cart.getPet().getPrice();
        }

        model.addAttribute("total", total);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("favorite", new FavoritePojo());
        model.addAttribute("userdata", userService.findBYId(id));
        model.addAttribute("info", userService.findByEmail(principal.getName()));

        return "addToCart";
    }

    @GetMapping("/add/{id}")
    public String saveToCart(@PathVariable Integer id, Principal principal){
        cartService.saveToCart(id, principal);
        return "redirect:/user/homepage";
    }

//    @PostMapping("/updateQuantity/{id}")
//    public String updateQuantity(@Valid CartPojo cartPojo){
//        Cart cart = cartService.fetchOne(cartPojo.getId());
//        cart.setQuantity(cartPojo.getQuantity());
//        cartService.updateQuantity(cart);
//        return "redirect:/cart";
//    }

    @PostMapping("/updateQuantity/{id}")
    public String updateQuantity(@PathVariable int id){
        Cart cart = cartService.fetchOne(id);
        cart.setQuantity(cart.getQuantity()-1);
        cartService.updateCartDetails(cart);
        cart.setPrice(cart.getPet().getPrice()*cart.getQuantity());
        cartService.updateCartDetails(cart);
        return "redirect:/cart";
    }
    @PostMapping("/updatedQuantity/{id}")
    public String updatedQuantity(@PathVariable int id){
        Cart cart = cartService.fetchOne(id);
        cart.setQuantity(cart.getQuantity()+1);
        cartService.updateCartDetails(cart);
        cart.setPrice(cart.getPet().getPrice()*cart.getQuantity());
        cartService.updateCartDetails(cart);
        return "redirect:/cart";
    }

    @PostMapping("/remove/{id}")
    public String deleteCartItem(@PathVariable("id") Integer id){
        cartService.deleteFromCart(id);
        return "redirect:/cart";
    }
    @PostMapping("/savefavorite")
    public String getFav(@Valid FavoritePojo favoritePojo){
        favoriteServices.save(favoritePojo);
        return "redirect:/user/homepage";
    }

}
