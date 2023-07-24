package com.example.AniMall.Services.impl;

import com.example.AniMall.Entity.Favorite;
import com.example.AniMall.Pojo.FavoritePojo;
import com.example.AniMall.Repo.FavoriteRepo;
import com.example.AniMall.Repo.PetRepo;
import com.example.AniMall.Repo.UserRepo;
import com.example.AniMall.Services.FavoriteServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public FavoritePojo save(FavoritePojo favoritePojo, Integer user_id, Integer pet_id) {
        Favorite favorite=new Favorite();
        if(favoritePojo.getId()!=null){
            favorite.setId(favorite.getId());
        }
        favorite.setUser_id(userRepo.findById(user_id).orElseThrow());
        favorite.setPet_id(petRepo.findById(pet_id).orElseThrow());

        favoriteRepo.save(favorite);
        return new FavoritePojo(favorite);
    }

    @Override
    public List<Favorite> findFavoriteById(Integer id) {
        return findAllinList(favoriteRepo.findFavoriteById(id));
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


    @Override
    public Favorite findById(Integer id) {
        com.example.AniMall.Entity.Favorite pet=favoriteRepo.findById(id).orElseThrow(()-> new RuntimeException("not found"));
        pet= com.example.AniMall.Entity.Favorite.builder()
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

    @Override
    public boolean existsById(Integer id) {
        return false;
    }
}
