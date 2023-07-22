package com.example.AniMall.Repo;

import com.example.AniMall.Entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepo extends JpaRepository<Favorite,Integer> {
    @Query(value = "SELECT * FROM favorites where user_id=?1", nativeQuery = true)
    List<Favorite> findFavoriteById(Integer id);

    @Query(value = "DELETE from favorites where user_id=?1", nativeQuery = true)
    Integer deleteByUser(Integer id);

    @Query(value = "SELECT * FROM favorites where user_id=?1", nativeQuery = true)
    List<Favorite> findByUser_Id(Integer userId);
}
