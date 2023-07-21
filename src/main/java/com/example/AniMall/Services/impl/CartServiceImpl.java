package com.example.AniMall.Services.impl;

import com.example.AniMall.Entity.Booking;
import com.example.AniMall.Entity.Cart;
import com.example.AniMall.Entity.Pet;
import com.example.AniMall.Pojo.BookingPojo;
import com.example.AniMall.Repo.BookingRepo;
import com.example.AniMall.Repo.CartRepo;
import com.example.AniMall.Repo.PetRepo;
import com.example.AniMall.Repo.UserRepo;
import com.example.AniMall.Services.CartServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartServices {


    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final PetRepo petRepo;
    private final BookingRepo bookingRepo;
    @Override
    public String saveToCart(Integer id, Principal principal) {
        Cart cart = new Cart();
        cart.setUser(userRepo.findByEmail(principal.getName()).orElseThrow());
        cart.setPet(petRepo.findById(id).orElseThrow());
        cart.setQuantity(1);
        cartRepo.save(cart);

        return "Saved";
    }

    @Override
    public String deleteFromCart(Integer id) {
        cartRepo.deleteById(id);
        return "Deleted";
    }

    @Override
    public String updateQuantity(Cart cart) {
        cartRepo.save(cart);
        return "Updated";
    }


    @Override
    public List<Cart> fetchAll(Integer id) {
        List<Cart> allItems = cartRepo.fetchAll(id).orElseThrow();
        for (Cart cart : allItems){
            cart.setPet(Pet.builder()
                    .id(cart.getPet().getId())
                    .category(cart.getPet().getCategory())
                    .image(cart.getPet().getImage())
                    .color(cart.getPet().getColor())
                    .description(cart.getPet().getDescription())
                    .price((cart.getPet().getPrice()))
                    .petname(cart.getPet().getPetname())
                    .breed(cart.getPet().getBreed())
                    .build());
            }

        return allItems;
    }

    @Override
    public List<Cart> fetchAllCart() {
        return null;
    }

    @Override
    public List<Cart> fetchAllCartByStatus(String status) {
        return null;
    }

    @Override
    public List<Object[]> fetchstatusCount() {
        return null;
    }

    @Override
    public List<String> distinctstatus() {
        return null;
    }

    @Override
    public List<Cart> cartdetails() {
        return null;
    }

    @Override
    public void updatecartstatus(Integer id, String status) {

    }

    @Override
    public List<Cart> fetchAvailable(Integer id) {
        List<Cart> allItems = cartRepo.fetchAll(id).orElseThrow();
        for (Cart cart : allItems){
            if (cart.getQuantity()<cart.getPet().getQuantity()) {
                cart.setPet(Pet.builder()
                        .id(cart.getPet().getId())
                        .category(cart.getPet().getCategory())
                        .image(cart.getPet().getImage())
                        .color(cart.getPet().getColor())
                        .description(cart.getPet().getDescription())
                        .price((cart.getPet().getPrice()))
                        .petname(cart.getPet().getPetname())
                        .breed(cart.getPet().getBreed())
                        .quantity(cart.getPet().getQuantity())
                        .build());
            } else {
                allItems.remove(cart);
            }
        }
        return allItems;
    }


    @Override
    public Cart fetchOne(Integer id) {
        return cartRepo.findById(id).orElseThrow();
    }

    @Override
    public String checkout(Integer id, BookingPojo pojo, List<Cart> itemsToPurchase) {
        for (Cart value : itemsToPurchase) {
            Booking booking = new Booking();
            booking.setId(value.getId());
            booking.setUser(value.getUser());
            booking.setPet(value.getPet());
            booking.setQuantity(value.getQuantity());

            booking.setShippingFullName(pojo.getShippingFullName());
            booking.setShippingAddress(pojo.getShippingAddress());
            booking.setShippingEmail(pojo.getShippingEmail());
            booking.setShippingPhone(pojo.getShippingPhone());
            booking.setImageBase64(getImageBase64(value.getPet().getImage()));

            booking.setStatus("Ordered");

            bookingRepo.save(booking);
        }
        return "Saved Purchase";
    }

    @Override
    public String updatePet(double quantity, Integer id) {
        petRepo.updateQuantity(quantity, id);
        return "Updated Quantity";
    }

    public String getImageBase64(String fileName) {
        if (fileName!=null) {
            String filePath = System.getProperty("user.dir")+"/pet/";
            File file = new File(filePath + fileName);
            byte[] bytes;
            try {
                bytes = Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return Base64.getEncoder().encodeToString(bytes);
        }
        return null;
    }
}

