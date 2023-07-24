package com.example.AniMall.Services;

import com.example.AniMall.Pojo.FavoritePojo;

import java.util.List;

public interface FavoriteServices {
    FavoritePojo save(FavoritePojo favoritePojo);

    FavoritePojo save(FavoritePojo favoritePojo, Integer user_id, Integer pet_id);
    List<com.example.AniMall.Entity.Favorite> findFavoriteById(Integer id);
    List<com.example.AniMall.Entity.Favorite> findAll();
    com.example.AniMall.Entity.Favorite findById(Integer id);
    void deleteById(Integer id);

    boolean existsById(Integer id);
}
