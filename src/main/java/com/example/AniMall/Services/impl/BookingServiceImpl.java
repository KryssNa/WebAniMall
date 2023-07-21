package com.example.AniMall.Services.impl;

import com.example.AniMall.Entity.Booking;
import com.example.AniMall.Pojo.BookingPojo;
import com.example.AniMall.Repo.BookingRepo;
import com.example.AniMall.Repo.PetRepo;
import com.example.AniMall.Repo.UserRepo;
import com.example.AniMall.Services.BookingServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingServices {
    private  final BookingRepo bookingRepo;
    private  final UserRepo userRepo;
    private  final PetRepo petRepo;
    @Override
    public BookingPojo save(BookingPojo bookingPojo) {
        Booking booking =new Booking();
        if(bookingPojo.getId()!=null){
            booking.setId(booking.getId());
        }
        booking.setUser(userRepo.findById(bookingPojo.getUser_id()).orElseThrow());
        booking.setPet(petRepo.findById(bookingPojo.getPet_id()).orElseThrow());
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
