package com.example.AniMall.Services;

import com.example.AniMall.Entity.Favorite;
import com.example.AniMall.Pojo.FavoritePojo;

import java.util.List;

public interface FavoriteServices {
    FavoritePojo save(FavoritePojo favoritePojo);

    List<Favorite> findFavoriteById(Integer id);
    List<Favorite> findAll();
    Favorite findById(Integer id);
    void deleteById(Integer id);

    boolean existsById(Integer id);
}
