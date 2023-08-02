package com.example.AniMall.controller;

import com.example.AniMall.Entity.*;
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

import java.io.IOException;
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

    @GetMapping("/homepage")
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
        if (principal==null){
            return "redirect:/login";
        }

        List<Pet> petList=petServices.getLimitedPets(page,petPerPage);
        model.addAttribute("page",page);
        model.addAttribute("favourite", new FavoritePojo());
        model.addAttribute("userdata",userService.findByEmail(principal.getName()));
        model.addAttribute("pageCounts",petServices.countTotalPages(petPerPage));
        model.addAttribute("petList", petList);
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

    @GetMapping("findAniMall")
    public String getAniMallPage(Model model) {
        List<Pet> pets = petServices.findAll();
        model.addAttribute("add", pets);
        return petServices.findAll().toString();
    }

    @GetMapping("/viewUserDetails/{id}")
    public String getUserDetails(@PathVariable("id") Integer id, Model model, Principal principal) {
        User user = userService.findBYId(id);
        model.addAttribute("signup", new UserPojo(user));
        model.addAttribute("signups", user);
        return "userProfile";
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

    @GetMapping("/viewMyBooking/{id}")
    public String getBooking(@PathVariable("id") Integer id, Model model, Principal principal) {
        List<Booking> booking =bookingServices.findBookingByUserId(id);
        List<ShippingDetails> shipping = bookingServices.findShippingByUserId(id);
        model.addAttribute("bookingList", booking);
        model.addAttribute("shippingLsit", shipping);
        model.addAttribute("userdata",userService.findByEmail(principal.getName()));

        return "bookings";
    }

    @GetMapping("/viewAllMyBookings/{id}")
    public String getBookinginList(@PathVariable("id") Integer id, Model model, Principal principal) {
        List<Booking> booking =bookingServices.findBookingByUserId(id);
        model.addAttribute("bookingList", booking);
        model.addAttribute("userdata",userService.findByEmail(principal.getName()));

        User user = userService.findBYId(id);
        model.addAttribute("signup", new UserPojo(user));
        model.addAttribute("signups", user);
        return "userProfile";
    }

    @GetMapping("/deleteFav/{id}")
    public String deleteFavorite(@PathVariable("id") Integer id) {
        favoriteServices.deleteById(id);
        return "redirect:/user/homepage";
    }

    @GetMapping("reset")
    public String reset(){
        return "resetPass";
    }

    @GetMapping("/sendResetEmail/{email}")
    public String sendResetEmail(@PathVariable("email") String email,Model model) throws IOException {
        System.out.println("Email: "+email);
        this.userService.sendResetEmail(email);
        model.addAttribute("email",email);
        return "resetPass";
    }

    @PostMapping("/resetPass")
    public String resetPassword(@RequestParam("email") String email,@RequestParam("password") String password,@RequestParam("otp") String otp,@RequestParam("cPassword") String cPass) throws IOException  {
        if(!password.equals(cPass)){
            return "redirect:/user/sendResetEmail/"+email+"?error=Password and Confirm Password must be same";
        }

        userService.resetPass(email,password,otp);
        return "redirect:/user/login";
    }


    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute("userPojo") UserPojo userPojo, Model model) {
        User user = userService.findByEmail(userPojo.getEmail());

        if (user != null) {
            // Update the non-password fields
            user.setFullname(userPojo.getFullname());
            user.setAge(userPojo.getAge());
            user.setGender(userPojo.getGender());
            user.setPhone(userPojo.getPhone());
            user.setAddress(userPojo.getAddress());
            user.setCountry(userPojo.getCountry());
            user.setAbout(userPojo.getAbout());

            // Only update the password if a new password is provided
            if (userPojo.getPassword() != null && !userPojo.getPassword().isEmpty()) {
                userService.changeUserPassword(user, userPojo.getPassword());
            }
            // Save the updated user using the appropriate method in UserServices
            userService.update(user);

            model.addAttribute("successMsg", "User details updated successfully");
        } else {
            model.addAttribute("errorMsg", "User not found");
        }
        return "redirect:/user/homepage";
    }
    @PostMapping("/updatePassword")
    public String updatePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName());
        if (userService.checkIfValidOldPassword(user, oldPassword)) {
            if (newPassword.equals(confirmPassword)) {
                userService.changeUserPassword(user, newPassword);
                model.addAttribute("successMsg", "Password changed successfully");
            } else {
                model.addAttribute("errorMsg", "New password and confirm password do not match");
            }
        } else {
            model.addAttribute("errorMsg", "Old password is not correct");
        }
        return "redirect:/user/homepage";
    }


//    @GetMapping("/pets/{category}")
//    public List<Pet> getPetsByCategory(@PathVariable String category){
//        return petServices.getPetsByCategory(category);
//    }


    @GetMapping("/AboutUs")
    public String getAboutUsPage(Model model) {
        return "aboutUs";
    }

    @GetMapping("/searchPet")
    public String searchPet(@RequestParam("search") String searchText, Model model) {
        List<Pet> searchResults = petServices.findPetByPartialName(searchText);
        System.out.println("searchResults"+searchResults.size());
        model.addAttribute("petsResult", searchResults);
        if(!searchResults.isEmpty())
            return "searchResults";
        else
            return "redirect:/user/homepage";
    }
}