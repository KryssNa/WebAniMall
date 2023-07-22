package com.example.AniMall.controller;

import com.example.AniMall.Entity.Booking;
import com.example.AniMall.Entity.Favorite;
import com.example.AniMall.Entity.Pet;
import com.example.AniMall.Entity.User;
import com.example.AniMall.Pojo.FavoritePojo;
import com.example.AniMall.Pojo.UserPojo;
import com.example.AniMall.Services.BookingServices;
import com.example.AniMall.Services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserServices userService;
    private final PetServices petServices;
    private final BookingServices bookingServices;
    private final FavoriteServices favoriteServices;

    @GetMapping(value = {"/homepage"})
    public String getHomepage(Model model ,Principal principal, Authentication authentication) {

        if (authentication!=null){
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority grantedAuthority : authorities) {
                if (grantedAuthority.getAuthority().equals("Admin")) {
                    return "redirect:/admin/list";
                }
            }
        }
        return "redirect:/user/dashboard/0/8";
    }

    @GetMapping(value = {"/dashboard/{page}/{petPerPage}"})
    public String getSetting(Model model,Principal principal,@PathVariable int page ,@PathVariable int petPerPage) {

        List<Pet> petList=petServices.getLimitedPets(page,petPerPage);
        model.addAttribute("page",page);
        model.addAttribute("petList", petList);
        List<Pet> pets = petServices.getThreeRandomData();
        model.addAttribute("petfetch", pets);
        model.addAttribute("userdata",userService.findByEmail(principal.getName()));
        model.addAttribute("pageCounts",petServices.countTotalPages(petPerPage));
        return "homepage";

    }

    @GetMapping("/next/{page}/{petPerPage}")
    public String getNextPage(Model model, @PathVariable int page, @PathVariable int petPerPage) {
        return "redirect:/user/dashboard/" + (page + 1) + "/" + petPerPage;
    }


    @GetMapping("/prev/{page}/{petPerPage}")
    public String getPreviousPage(Model model,@PathVariable int page,@PathVariable int petPerPage) {
        return "redirect:/user/dashboard/"+(page-1)+"/"+petPerPage;
    }

    @GetMapping("/signup")
    public String getSignupPage(Model model) {
        model.addAttribute("create", new UserPojo());
        return "register";
    }

    @PostMapping("/saveuser")
    public String saveUser(@Valid UserPojo userPojo) {
        userService.save(userPojo);
        return "redirect:/signup";
    }

    @GetMapping("/user/shop")
    public String getShopPage(@RequestParam(name = "page", defaultValue = "1") int page, Model model) {
        int pageSize = 1; // Number of items per page
        List<Pet> pets = petServices.findAll(); // Fetch all pets from the database
        int totalItems = pets.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);
        List<Pet> paginatedPets = pets.subList(startIndex, endIndex);

        model.addAttribute("add", paginatedPets);
        model.addAttribute("totalPages", totalPages);

        return "shop-page";
    }

    @GetMapping("findAniMall")
    public String getAniMallPage(Model model) {
        List<Pet> pets = petServices.findAll();
        model.addAttribute("add", pets);
        return petServices.findAll().toString();
    }


    @GetMapping("/viewAllMyBookings/{id}")
    public String getBookinginList(@PathVariable("id") Integer id, Model model, Principal principal) {
        List<Booking> booking =bookingServices.findBookingById(id);
        model.addAttribute("bookingList", booking);
        model.addAttribute("userdata",userService.findByEmail(principal.getName()));

        User user = userService.findBYId(id);
        model.addAttribute("signup", new UserPojo(user));
        model.addAttribute("signups", user);
        return "profile";
    }


    @GetMapping("/delete/{id}")
    public String deleteBooking(@PathVariable("id") Integer id) {
        bookingServices.deleteById(id);
        return "redirect:/user/homepage";
    }


    @PostMapping("/savefavorite")
    public String getFav(@Valid FavoritePojo favoritePojo){
        favoriteServices.save(favoritePojo);
        return "redirect:/user/homepage";
    }

    @GetMapping("/viewAllMyFavorites/{id}")
    public String getFavoriteinList(@PathVariable("id") Integer id, Model model, Principal principal) {
        List<Favorite> favorite=favoriteServices.findFavoriteById(id);
        model.addAttribute("favoriteList", favorite);
        model.addAttribute("userdata",userService.findByEmail(principal.getName()));

        return "fav";
    }

    @GetMapping("/deleteFav/{id}")
    public String deleteFavorite(@PathVariable("id") Integer id) {
        favoriteServices.deleteById(id);
        return "redirect:/user/homepage";
    }


    @PostMapping("/updateprofile")
    public String updateRegister(@Valid UserPojo userPojo){
        userService.save(userPojo);
        return "redirect:/user/homepage";}


//    @GetMapping("/sendEmail")
//    public String sendRegistrationEmail() {
//        this.userService.sendEmail();
//        return "emailsuccess";
//    }

//    @GetMapping("/pets/{category}")
//    public List<Pet> getPetsByCategory(@PathVariable String category){
//        return petServices.getPetsByCategory(category);
//    }



}

