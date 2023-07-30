package com.example.AniMall.Services.impl;

import com.example.AniMall.Entity.Pet;
import com.example.AniMall.Pojo.PetPojo;
import com.example.AniMall.Repo.BookingRepo;
import com.example.AniMall.Repo.CartRepo;
import com.example.AniMall.Repo.FavoriteRepo;
import com.example.AniMall.Repo.PetRepo;
import com.example.AniMall.Services.PetServices;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class PetServiceImpl implements PetServices {
    private  final PetRepo petRepo;
    private final CartRepo cartRepo;
    private final BookingRepo bookingRepo;
    private final FavoriteRepo favoriteRepo;
    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/pet";
    @Override
    public PetPojo save(PetPojo petPojo) throws IOException {
        Pet pet;
        if (petPojo.getId() != null) {
            pet = petRepo.findById(petPojo.getId()).orElseThrow(() -> new RuntimeException("Not Found"));
        } else {
            pet = new Pet();
        }
        if(petPojo.getId()!=null){
            pet.setId(petPojo.getId());
        }
        pet.setDescription(petPojo.getDescription());
        pet.setPetname(petPojo.getPetname());
        pet.setPrice(petPojo.getPrice());
        pet.setQuantity(petPojo.getQuantity());
        pet.setPrice(petPojo.getPrice());
        pet.setBreed(petPojo.getBreed());
        pet.setColor(petPojo.getColor());
        pet.setCategory((petPojo.getCategory()));

        if(petPojo.getImage()!=null){
            StringBuilder fileNames = new StringBuilder();
            System.out.println(UPLOAD_DIRECTORY);
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, petPojo.getImage().getOriginalFilename());
            fileNames.append(petPojo.getImage().getOriginalFilename());
            Files.write(fileNameAndPath, petPojo.getImage().getBytes());

            pet.setImage(petPojo.getImage().getOriginalFilename());
        }

        petRepo.save(pet);
        return new PetPojo(pet);
    }
    @Override
    public void deleteById(Integer id) {
        cartRepo.deleteCartByPetId(id);
        bookingRepo.deleteBookingByPetId(id);
        favoriteRepo.deleteFavoritesByPetId(id);
        petRepo.deleteById(id);
    }


    @Override
    public List<Pet> findAll() {
        return findAllinList(petRepo.findAll());
    }


    @Override
    public Pet findById(Integer id) {
        Pet pet=petRepo.findById(id).orElseThrow(()-> new RuntimeException("not found"));
        pet=Pet.builder()
                .id(pet.getId())
                .petname(pet.getPetname())
                .price(pet.getPrice())
                .breed(pet.getBreed())
                .quantity(pet.getQuantity())
                .price(pet.getPrice())
                .category(pet.getCategory())
                .color(pet.getColor())
                .description(pet.getDescription())
                .imageBase64(getImageBase64(pet.getImage()))
                .build();
        return pet;
    }

    @Override
    public List<Pet> getLimitedPets(int page, int petsPerPage) {
        int offset=page * petsPerPage;
        return findAllinList(petRepo.getLimitedPets(offset,petsPerPage));
    }

    @Override
    public int countTotalPages(int petsPerPage) {
        int count=petRepo.countTotalPets();
        if(count%petsPerPage==0){
            return count/petsPerPage;
        }
        return (count/petsPerPage)+1;
    }

    @Override
    public List<Pet> findPetByPartialName(String partialName) {
        return findAllinList(petRepo.findPetByPartialName(partialName));
    }

    public List<Pet> findAllinList(List<Pet> list){

        Stream<Pet> allJobsWithImage = list.stream().map(pet ->
                Pet.builder()
                        .id(pet.getId())
                        .petname(pet.getPetname())
                        .breed(pet.getBreed())
                        .category(pet.getCategory())
                        .price(pet.getPrice())
                        .quantity(pet.getQuantity())
                        .price(pet.getPrice())
                        .color(pet.getColor())
                        .description(pet.getDescription())
                        .imageBase64(getImageBase64(pet.getImage()))
                        .build()
        );
        list = allJobsWithImage.toList();
        return list;
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
