package com.example.AniMall.Services.impl;

import com.example.AniMall.Entity.*;
import com.example.AniMall.Pojo.BookingPojo;
import com.example.AniMall.Repo.BookingRepo;
import com.example.AniMall.Repo.PetRepo;
import com.example.AniMall.Repo.ShippingRepo;
import com.example.AniMall.Repo.UserRepo;
import com.example.AniMall.Services.BookingServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingServices {
    private  final BookingRepo bookingRepo;
    private  final ShippingRepo shippingRepo;
    private  final UserRepo userRepo;
    private  final PetRepo petRepo;

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/ShippingDetails";

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
    public List<Booking> findBookingByUserId(Integer id) {
        return findAllinList(bookingRepo.findBookingByUserId(id));
    }

    @Override
    public List<Booking> findBookingById(Integer id) {
        return findAllinList(bookingRepo.findBookingById(id));
    }

    public List<ShippingDetails> findShippingByUserId(Integer id) {
        return findShppinginList(shippingRepo.findShippingDetailsByUserId(id));
    }

    @Override
    public List<ShippingDetails> findShippingId(Integer id) {
        return findShppinginList(shippingRepo.findShippingById(id));
    }
    @Override
    public List<Booking> findAll() {
        return findAllinList(bookingRepo.findAll());
    }

    @Override
    public List<ShippingDetails> findAllShipping() {
        return findShppinginList(shippingRepo.findAll());
    }

    public List<Booking> findAllinList(List<Booking> list){
        Stream<Booking> allBookingsWithImage = list.stream().map(booking -> {
            Pet pet = Pet.builder()
                    .id(booking.getPet().getId())
                    .imageBase64(getImageBase64(booking.getPet().getImage()))
                    .petname(booking.getPet().getPetname())
                    .build();

            return Booking.builder()
                    .id(booking.getId())
                    .user(booking.getUser())
                    .pet(pet)
                    .shippingDetails(booking.getShippingDetails())
                    .quantity(booking.getQuantity())
                    .price(booking.getPrice())
                    .status(booking.getStatus())
                    .build();
        });

        return allBookingsWithImage.collect(Collectors.toList());
    }

    public List<ShippingDetails> findShppinginList(List<ShippingDetails> list){

        Stream<ShippingDetails> allJobsWithImage = list.stream().map(shippingDetails ->
                ShippingDetails.builder()
                        .id(shippingDetails.getId())
                        .user(shippingDetails.getUser())
                        .shippingFullName(shippingDetails.getShippingFullName())
                        .shippingAddress(shippingDetails.getShippingAddress())
                        .shippingEmail(shippingDetails.getShippingEmail())
                        .shippingPhone(shippingDetails.getShippingPhone())
                        .totalPrice(shippingDetails.getTotalPrice())
                        .totalQuantity(shippingDetails.getTotalQuantity())
                        .image(shippingDetails.getImage())
                        .imageBase64(getImageBase64(shippingDetails.getImage()))
                        .status(shippingDetails.getStatus())
                        .build()
        );
        list = allJobsWithImage.toList();
        return list;
    }


    @Override
    public Booking findById(Integer id) {
        Booking booking= bookingRepo.findById(id).orElseThrow(()-> new RuntimeException("not found"));
        booking= Booking.builder()
                .id(booking.getId())
                .user(booking.getUser())
                .pet(booking.getPet())
                .shippingDetails(booking.getShippingDetails())
                .quantity(booking.getQuantity())
                .price(booking.getPrice())
                .status(booking.getStatus())
                .build();
        return booking;
    }

    @Override
    public void deleteById(Integer id) {
        bookingRepo.deleteById(id);
    }

    @Override
    public void updateStatus(Integer id, String status) {
        bookingRepo.updateStatus(id,status);
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
