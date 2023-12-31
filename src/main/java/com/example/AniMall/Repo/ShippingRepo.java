package com.example.AniMall.Repo;

import com.example.AniMall.Entity.ShippingDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShippingRepo extends JpaRepository<ShippingDetails, Integer> {

    @Query(value = "SELECT * FROM shipping_details where user_id=?1", nativeQuery = true)
    List<ShippingDetails> findShippingDetailsByUserId(Integer id);

    @Query(value = "DELETE from shipping_details where user_id=?1", nativeQuery = true)
    Integer deleteByUser(Integer id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE shipping_details SET status = ?2 WHERE id = ?1")
    void updateShippingStatus(Integer id, String changeStatus);

    @Query(value = "SELECT * FROM shipping_details where id=?1", nativeQuery = true)
    List<ShippingDetails> findShippingById(Integer id);
}
