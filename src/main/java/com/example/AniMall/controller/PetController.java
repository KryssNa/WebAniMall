package com.example.AniMall.controller;

import com.example.AniMall.Entity.Pet;
import com.example.AniMall.Pojo.BookingPojo;
import com.example.AniMall.Pojo.FavoritePojo;
import com.example.AniMall.Pojo.PetPojo;
import com.example.AniMall.Services.BookingServices;
import com.example.AniMall.Services.PetServices;
import com.example.AniMall.Services.UserServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pet")
public class PetController {

    private final UserServices userService;
    private final PetServices petServices;
    private final BookingServices bookingServices;

    @PostMapping("/savebooking")
    public String getHire(@Valid BookingPojo bookingPojo){
        bookingServices.save(bookingPojo);
        return "redirect:/user/homepage";
    }

    @GetMapping("/petinfo/{id}")
    public String GetmoreInfo(@PathVariable("id") Integer id , Model model, Principal principal){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/user/signup";
        }
        Pet pet= petServices.findById(id);

        model.addAttribute("booking", new BookingPojo());
        model.addAttribute("favorite", new FavoritePojo());
        model.addAttribute("petinfo",new PetPojo(pet));
        model.addAttribute("petdata",pet);
        model.addAttribute("info",userService.findByEmail(principal.getName()));
        return "petDesc";
    }

}
