package com.example.AniMall.controller;

import com.example.AniMall.Entity.Booking;
import com.example.AniMall.Entity.Pet;
import com.example.AniMall.Entity.ShippingDetails;
import com.example.AniMall.Entity.User;
import com.example.AniMall.Pojo.PetPojo;
import com.example.AniMall.Services.BookingServices;
import com.example.AniMall.Services.PetServices;
import com.example.AniMall.Services.UserServices;
import com.example.AniMall.exception.AppException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private  final PetServices petServices;
    private final UserServices userServices;
    private  final BookingServices bookingServices;

    @GetMapping("/addpet")
    public String getAddPetForm(Model model) {
        model.addAttribute("add",new PetPojo());
        return "Admin/AddPet";
    }
    @PostMapping("/savepet")
    public String createPet(@Valid PetPojo petPojo, BindingResult bindingResult, RedirectAttributes redirectAttributes
    ) throws IOException {

        Map<String, String> requestError = validateRequest(bindingResult);
        if (requestError != null) {
            redirectAttributes.addFlashAttribute("requestError", requestError);
            return "redirect:/admin/addpet";
        }
        petServices.save(petPojo);
        redirectAttributes.addFlashAttribute("successMsg", "Pet saved successfully");
        return "redirect:/admin/viewallpet/0/5";
    }

    @GetMapping("/viewallpet/{page}/{petPerPage}")
    public String getPetinList(Model model,@PathVariable int page,@PathVariable int petPerPage) {
        List<Pet> petList=petServices.getLimitedPets(page,petPerPage);
        model.addAttribute("limitedPets", petList);
        model.addAttribute("page",page);
        model.addAttribute("totalPages",petServices.countTotalPages(petPerPage));
        System.out.println("pageCounts"+petServices.countTotalPages(petPerPage));
        return "Admin/allPetList";
    }

    @GetMapping("/next/{page}/{petPerPage}")
    public String getNextPage(Model model, @PathVariable int page, @PathVariable int petPerPage) {
        return "redirect:/admin/viewallpet/" + (page + 1) + "/" + petPerPage;
    }


    @GetMapping("/prev/{page}/{petPerPage}")
    public String getPreviousPage(Model model,@PathVariable int page,@PathVariable int petPerPage) {
        return "redirect:/admin/viewallpet/"+(page-1)+"/"+petPerPage;
    }

    @GetMapping("/editpet/{id}")
    public String editMembers(@PathVariable("id") Integer id, Model model) {
        Pet pet = petServices.findById(id);
        model.addAttribute("add", new PetPojo(pet));
        return "Admin/AddPet";
    }

    @GetMapping("/deletepet/{id}")
    public String deleteMembers(@PathVariable("id") Integer id) {
        petServices.deleteById(id);
        return "redirect:/admin/viewallpet/0/4";
    }

    @GetMapping("/viewallbooking")
    public String getBookingList(Model model) {
        try {
            List<Booking> bookings = bookingServices.findAll();
            model.addAttribute("alllist", bookings);
            return "Admin/ViewBookings";
        } catch (Exception ex) {
            // Handle the exception and throw an appropriate AppException with the corresponding status code
            throw new AppException("Error retrieving booking list", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/viewAllShippings")
    public String getShippingList(Model model) {
        List<ShippingDetails> shipping =bookingServices.findAllShipping();
        model.addAttribute("allListShipping", shipping);
        return "Admin/Shippings";
    }
    public Map<String, String> validateRequest(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return null;
        }
        Map<String, String> errors = new HashMap<>();
        bindingResult.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return errors;
    }

    @GetMapping("/list")
    public String showUserList(Model model) {
        List<User> users = userServices.fetchAll();
        model.addAttribute("userList", users);
        System.out.println(users);
        return "Admin/user_list";
    }

    @GetMapping("/deleteuser/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userServices.deleteById(id);
        return "redirect:/admin/list";
    }

    @GetMapping("/searchPet")
    public String searchPet(@RequestParam("search") String searchText, Model model) {
        List<Pet> searchResults = petServices.findPetByPartialName(searchText);
        model.addAttribute("limitedPets", searchResults);

        return "Admin/allPetList";
    }
}

