package com.example.AniMall.Services.impl;

import com.example.AniMall.Entity.*;
import com.example.AniMall.Pojo.BookingPojo;
import com.example.AniMall.Pojo.ShippingDetailsDto;
import com.example.AniMall.Repo.BookingRepo;
import com.example.AniMall.Repo.PetRepo;
import com.example.AniMall.Repo.SummaryDetailsRepo;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingServices {
    private  final BookingRepo bookingRepo;
    private  final SummaryDetailsRepo shippingRepo;
    private  final UserRepo userRepo;
    private  final PetRepo petRepo;
    private final SummaryDetailsRepo summaryDetailsRepo;

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/ShippingDetails";

    @Override
    public String summaryCheckout(Integer id, BookingPojo pojo, ShippingDetailsDto shippingDetailsDto) throws IOException {
        ShippingDetails shippingDetails = new ShippingDetails();
        Booking booking = new Booking();
        User user = userRepo.findById(id).orElseThrow();
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
    public List<ShippingDetails> findShippingById(Integer id) {
        return findShppinginList(shippingRepo.findShippingDetailsById(id));
    }
//    @Override
//    public ShippingDetails findOrderById(Integer id) {
//        ShippingDetails shippingDetails=summaryDetailsRepo.findById(id).orElseThrow(()-> new RuntimeException("not found"));
//        shippingDetails=shippingDetails.builder()
//                    .id(shippingDetails.getId())
////                    .quantity(order.getQuantity())
//                    .user_id(shippingDetails.getUser_id())
//                    .product_id(.getProduct_id())
//                    .address(order.getAddress())
//                    .build();
//            return order;
//    }


    @Override
    public List<Booking> findAll() {
        return findAllinList(bookingRepo.findAll());
    }

    @Override
    public List<ShippingDetails> findAllShipping() {
        return findShppinginList(shippingRepo.findAll());
    }
//    public List<Booking> findAllinList(List<Booking> list){
//
//        Stream<Booking> allJobsWithImage = list.stream().map(shipping ->
//                Booking.builder()
//                        .id(shipping.getId())
//                        .user(shipping.getUser())
//                        .pet(shipping.getPet())
//                        .shippingDetails(shipping.getShippingDetails())
//                        .quantity(shipping.getQuantity())
//                        .price(shipping.getPrice())
//                        .status(shipping.getStatus())
//                        .build()
//        );
//        list = allJobsWithImage.toList();
//        return list;
//    }

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
//                        .pet(pet.getPet())
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
        booking.setShippingDetails(shippingRepo.findById(bookingPojo.getShipping_id()).orElseThrow());
        booking.setPrice(bookingPojo.getPrice());
        booking.setQuantity(bookingPojo.getQuantity());
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
