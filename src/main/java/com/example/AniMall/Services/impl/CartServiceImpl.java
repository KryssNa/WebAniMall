package com.example.AniMall.Services.impl;

import com.example.AniMall.Entity.*;
import com.example.AniMall.Pojo.BookingPojo;
import com.example.AniMall.Pojo.ShippingDetailsDto;
import com.example.AniMall.Repo.*;
import com.example.AniMall.Services.CartServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartServices {


    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final PetRepo petRepo;
    private final BookingRepo bookingRepo;
    private final ShippingRepo shippingRepo;

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/pet/";

@Override
public String saveToCart(Integer id, Principal principal) {
    User user = userRepo.findByEmail(principal.getName()).orElseThrow();

    Pet pet = petRepo.findById(id).orElseThrow();
    List<Cart> userCartItems = fetchAll(user.getId());
    boolean petExistsInCart = false;

    for (Cart cartItem : userCartItems) {
        if (cartItem.getPet().getId().equals(id)) {
            // If the Cart already contains the Pet, update the quantity and price
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItem.setPrice(pet.getPrice() * cartItem.getQuantity());
            cartRepo.save(cartItem);
            petExistsInCart = true;
            break;
        }
    }

    if (!petExistsInCart) {
        // If the Cart does not contain the Pet, create a new Cart entry
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setPet(pet);
        cart.setPrice(pet.getPrice());
        cart.setQuantity(1);
        cartRepo.save(cart);
    }

    return "Saved";
}


    @Override
    public String deleteFromCart(Integer id) {
        cartRepo.deleteById(id);
        return "Deleted";
    }

    @Override
    public String updateCartDetails(Cart cart) {
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
                    .imageBase64(getImageBase64(cart.getPet().getImage()))
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


    // main checkout method
    @Override
    public String checkout(Integer id, BookingPojo pojo, ShippingDetailsDto shippingDetailsDto, List<Cart> itemsToPurchase) throws IOException {
        User user = userRepo.findById(id).orElseThrow();
        System.out.println("user email: " + user.getEmail());

        ShippingDetails shippingDetails = new ShippingDetails();
        shippingDetails.setUser(user);
        shippingDetails.setShippingFullName(shippingDetailsDto.getShippingFullName());
        shippingDetails.setShippingAddress(shippingDetailsDto.getShippingAddress());
        shippingDetails.setShippingEmail(itemsToPurchase.get(0).getUser().getEmail());
        shippingDetails.setShippingPhone(shippingDetailsDto.getShippingPhone());
        shippingDetails.setStatus("Pending");
        shippingDetails.setTotalPrice(shippingDetailsDto.getTotalPrice());
        shippingDetails.setTotalQuantity(shippingDetailsDto.getTotalQuantity());

        if (shippingDetailsDto.getImage() != null) {
            System.out.println("Image is not null");
            StringBuilder fileNames = new StringBuilder();
            System.out.println(UPLOAD_DIRECTORY);
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, shippingDetailsDto.getImage().getOriginalFilename());
            fileNames.append(shippingDetailsDto.getImage().getOriginalFilename());
            Files.write(fileNameAndPath, shippingDetailsDto.getImage().getBytes());

            shippingDetails.setImage(shippingDetailsDto.getImage().getOriginalFilename());
        }

        // Save the shipping details first
        ShippingDetails savedShippingDetails = shippingRepo.save(shippingDetails);
        System.out.println("Shipping details inserted");

        // Save the bookings related to the shipping details
        for (Cart value : itemsToPurchase) {
            Booking booking = new Booking();
            booking.setId(value.getId());
            booking.setQuantity(value.getQuantity());
            booking.setPrice(value.getPet().getPrice());
            value.setStatus("Ordered");
            booking.setUser(value.getUser());
            booking.setPet(value.getPet());
            booking.setStatus("Pending");

            // Set the shipping details for each booking
            booking.setShippingDetails(savedShippingDetails);

            // Save each booking
            bookingRepo.save(booking);
        }

        return "Saved Purchase";
    }

    @Override
    public int updatePet(int quantity, Integer id) {
        petRepo.updateQuantity(quantity, id);
        return quantity;
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

