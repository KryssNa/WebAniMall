package com.example.AniMall.Services.impl;

import com.example.AniMall.Entity.Favorite;
import com.example.AniMall.Entity.Pet;
import com.example.AniMall.Pojo.FavoritePojo;
import com.example.AniMall.Repo.FavoriteRepo;
import com.example.AniMall.Repo.PetRepo;
import com.example.AniMall.Repo.UserRepo;
import com.example.AniMall.Services.FavoriteServices;
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
public class FavoriteServiceImpl implements FavoriteServices {
    private  final FavoriteRepo favoriteRepo;
    private  final UserRepo userRepo;
    private  final PetRepo petRepo;

    @Override
    public FavoritePojo save(FavoritePojo favoritePojo) {
        Favorite favorite=new Favorite();
        if(favoritePojo.getId()!=null){
            favorite.setId(favorite.getId());
        }
        favorite.setUser_id(userRepo.findById(favoritePojo.getUser_id()).orElseThrow());
        favorite.setPet_id(petRepo.findById(favoritePojo.getPet_id()).orElseThrow());

        favoriteRepo.save(favorite);
        return new FavoritePojo(favorite);
    }


    @Override
    public List<Favorite> findFavoriteById(Integer id) {
        return findAllinList(id);
    }
    @Override
    public List<Favorite> findAll() {
        return findAllinList(favoriteRepo.findAll());
    }
    public List<Favorite> findAllinList(List<Favorite> list){

        Stream<Favorite> allJobsWithImage = list.stream().map(pet ->
                Favorite.builder()
                        .id(pet.getId())
                        .user_id(pet.getUser_id())
                        .pet_id(pet.getPet_id())
                        .build()
        );
        list = allJobsWithImage.toList();
        return list;
    }

    public List<Favorite> findAllinList(Integer id){
        List<Favorite> list=favoriteRepo.findFavoriteById(id);
        for (Favorite favorite:list){
            favorite.setPet_id(Pet.builder()
                    .id(favorite.getPet_id().getId())
                    .imageBase64(getImageBase64(favorite.getPet_id().getImage()))
                    .price(favorite.getPet_id().getPrice())
                    .petname(favorite.getPet_id().getPetname())
                    .description(favorite.getPet_id().getDescription())
                    .category(favorite.getPet_id().getCategory())
                    .build()
            );
        }
        return list;

    }


    @Override
    public Favorite findById(Integer id) {
        Favorite pet=favoriteRepo.findById(id).orElseThrow(()-> new RuntimeException("not found"));
        pet= Favorite.builder()
                .id(pet.getId())
                .user_id(pet.getUser_id())
                .pet_id(pet.getPet_id())
                .build();
        return pet;
    }

    @Override
    public void deleteById(Integer id) {
        favoriteRepo.deleteById(id);
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

