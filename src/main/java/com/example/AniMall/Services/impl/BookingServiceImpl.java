package com.example.AniMall.Services.impl;

import com.example.AniMall.Entity.Booking;
import com.example.AniMall.Entity.ShippingDetails;
import com.example.AniMall.Entity.User;
import com.example.AniMall.Pojo.BookingPojo;
import com.example.AniMall.Pojo.ShippingDetailsDto;
import com.example.AniMall.Repo.BookingRepo;
import com.example.AniMall.Repo.PetRepo;
import com.example.AniMall.Repo.UserRepo;
import com.example.AniMall.Services.BookingServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
public class BookingServiceImpl implements BookingServices {
    private  final BookingRepo bookingRepo;
    private  final UserRepo userRepo;
    private  final PetRepo petRepo;

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/ShippingDetails";

    @Override
    public String summaryCheckout(Integer id, BookingPojo pojo, ShippingDetailsDto shippingDetailsDto) throws IOException {
        ShippingDetails shippingDetails = new ShippingDetails();
        Booking booking = new Booking();
        User user = userRepo.findById(id).orElseThrow();
//        for (Cart value : itemsToPurchase) {
//            booking.setId(value.getId());
//            booking.setQuantity(value.getQuantity());
//            booking.setPrice(value.getPet().getPrice());
//            value.setStatus("Ordered");
//            booking.setUser(value.getUser());
//            booking.setPet(value.getPet());
//
//
//
//
////            booking.setQuantity(value.getQuantity());
////
////            booking.setShippingFullName(pojo.getShippingFullName());
////            booking.setShippingAddress(pojo.getShippingAddress());
////            booking.setShippingEmail(pojo.getShippingEmail());
////            booking.setShippingPhone(pojo.getShippingPhone());
////            booking.setImageBase64(getImageBase64(value.getPet().getImage()));
//
//            booking.setStatus("Ordered");
//
//            bookingRepo.save(booking);
//        }

        shippingDetails.setShippingFullName(shippingDetailsDto.getShippingFullName());
        shippingDetails.setShippingAddress(shippingDetailsDto.getShippingAddress());
        shippingDetails.setShippingEmail(shippingDetailsDto.getShippingEmail());
        shippingDetails.setShippingPhone(shippingDetailsDto.getShippingPhone());
        shippingDetails.setStatus("Ordered");
        booking.setPrice(shippingDetailsDto.getTotalPrice());
        booking.setQuantity(shippingDetailsDto.getTotalQuantity());
        if(shippingDetailsDto.getImage()!=null){
            System.out.println("Image is not null");
            StringBuilder fileNames = new StringBuilder();
            System.out.println(UPLOAD_DIRECTORY);
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, shippingDetailsDto.getImage().getOriginalFilename());
            fileNames.append(shippingDetailsDto.getImage().getOriginalFilename());
            Files.write(fileNameAndPath, shippingDetailsDto.getImage().getBytes());

            shippingDetails.setImage(shippingDetailsDto.getImage().getOriginalFilename());
        }

        shippingDetails.setUser(user);
//        bookingRepo.save(shippingDetails.getUser());
        return "Saved Purchase";
    }

    @Override
    public BookingPojo save(BookingPojo bookingPojo) {
        Booking booking =new Booking();
        if(bookingPojo.getId()!=null){
            booking.setId(booking.getId());
        }
        booking.setUser(userRepo.findById(bookingPojo.getUser_id()).orElseThrow());
        booking.setPet(petRepo.findById(bookingPojo.getPet_id()).orElseThrow());
        booking.setStatus("pending");
        booking.setQuantity(bookingPojo.getQuantity());
        bookingRepo.save(booking);
        return new BookingPojo(booking);
    }
    @Override
    public List<Booking> findBookingById(Integer id) {
        return findAllinList(bookingRepo.findBookingById(id));
    }
//    @Override
//    public Order findOrderById(Integer id) {
//            Order order=orderRepo.findById(id).orElseThrow(()-> new RuntimeException("not found"));
//            order=Order.builder()
//                    .id(order.getId())
//                    .quantity(order.getQuantity())
//                    .user_id(order.getUser_id())
//                    .product_id(order.getProduct_id())
//                    .address(order.getAddress())
//                    .build();
//            return order;
//    }


    @Override
    public List<Booking> findAll() {
        return findAllinList(bookingRepo.findAll());
    }
    public List<Booking> findAllinList(List<Booking> list){

        Stream<Booking> allJobsWithImage = list.stream().map(pet ->
                Booking.builder()
                        .id(pet.getId())
                        .user(pet.getUser())
                        .pet(pet.getPet())
                        .status(pet.getStatus())
                        .build()
        );
        list = allJobsWithImage.toList();
        return list;
    }




    @Override
    public Booking findById(Integer id) {
        Booking pet= bookingRepo.findById(id).orElseThrow(()-> new RuntimeException("not found"));
        pet= Booking.builder()
                .id(pet.getId())
                .user(pet.getUser())
                .pet(pet.getPet())
                .status(pet.getStatus())
                .build();
        return pet;
    }

    @Override
    public void deleteById(Integer id) {
        bookingRepo.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return false;
    }

    //checkout
    @Override
    public BookingPojo saveCheckout(BookingPojo bookingPojo) {
        Booking booking =new Booking();
        if(bookingPojo.getId()!=null){
            booking.setId(booking.getId());
        }
        booking.setUser(userRepo.findById(bookingPojo.getUser_id()).orElseThrow());
        booking.setPet(petRepo.findById(bookingPojo.getPet_id()).orElseThrow());
        booking.setStatus("pending");
        bookingRepo.save(booking);
        return new BookingPojo(booking);
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
