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
    private final SummaryDetailsRepo summaryDetailsRepo;

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/pet/";

//    method to save to cart
    @Override
    public String saveToCart(Integer id, Principal principal) {
        Cart cart = new Cart();
        cart.setUser(userRepo.findByEmail(principal.getName()).orElseThrow());
        cart.setPet(petRepo.findById(id).orElseThrow());
        cart.setPrice(petRepo.findById(id).orElseThrow().getPrice());
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


//    will be implemented in future some error is coming
    @Override
    public void clearCart(Integer id) {
        cartRepo.deleteById(id);
    }


    @Override
    public Cart fetchOne(Integer id) {
        return cartRepo.findById(id).orElseThrow();
    }


    // main checkout method
    @Override
    public String checkout(Integer id, BookingPojo pojo, ShippingDetailsDto shippingDetailsDto, List<Cart> itemsToPurchase) throws IOException {
        ShippingDetails shippingDetails = new ShippingDetails();
        Booking booking = new Booking();
        User user = userRepo.findById(id).orElseThrow();
        System.out.println("user email: "+user.getEmail());
        for (Cart value : itemsToPurchase) {
            booking.setId(value.getId());
            booking.setQuantity(value.getQuantity());
            booking.setPrice(value.getPet().getPrice());
            value.setStatus("Ordered");
            booking.setUser(value.getUser());
            booking.setPet(value.getPet());
            booking.setStatus("Ordered");
        }
        System.out.println("shipping details");
        user.setId(id);
        shippingDetails.setUser(user);
        shippingDetails.setShippingFullName(shippingDetailsDto.getShippingFullName());
        shippingDetails.setShippingAddress(shippingDetailsDto.getShippingAddress());
        shippingDetails.setShippingEmail(itemsToPurchase.get(0).getUser().getEmail());
        shippingDetails.setShippingPhone(shippingDetailsDto.getShippingPhone());
        shippingDetails.setStatus("Ordered");
        shippingDetails.setTotalPrice(shippingDetailsDto.getTotalPrice());
        shippingDetails.setTotalQuantity(shippingDetailsDto.getTotalQuantity());

        if(shippingDetailsDto.getImage()!=null){
            System.out.println("Image is not null");
            StringBuilder fileNames = new StringBuilder();
            System.out.println(UPLOAD_DIRECTORY);
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, shippingDetailsDto.getImage().getOriginalFilename());
            fileNames.append(shippingDetailsDto.getImage().getOriginalFilename());
            Files.write(fileNameAndPath, shippingDetailsDto.getImage().getBytes());

            shippingDetails.setImage(shippingDetailsDto.getImage().getOriginalFilename());
        }
        bookingRepo.save(booking);
        shippingDetails.setUser(user);
        summaryDetailsRepo.save(shippingDetails);

        System.out.println("shipping details inserted");
        return "Saved Purchase";
    }

    @Override
    public String
    checkout(Integer id, BookingPojo pojo, List<Cart> itemsToPurchase) {
        for (Cart value : itemsToPurchase) {
            Booking booking = new Booking();
            booking.setId(value.getId());
            booking.setQuantity(value.getQuantity());
            booking.setPrice(value.getPet().getPrice());
            value.setStatus("Ordered");
            booking.setUser(value.getUser());
            booking.setPet(value.getPet());
            booking.setStatus("Ordered");

            bookingRepo.save(booking);
        }
//

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

