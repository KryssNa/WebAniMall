package com.example.AniMall.Repo;

import com.example.AniMall.Entity.Booking;
import com.example.AniMall.Entity.ShippingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SummaryDetailsRepo extends JpaRepository<ShippingDetails, Integer> {

    @Query(value = "SELECT * FROM shipping_details where user_id=?1", nativeQuery = true)
    List<ShippingDetails> findShippingDetailsById(Integer id);

    @Query(value = "DELETE from shipping_details where user_id=?1", nativeQuery = true)
    Integer deleteByUser(Integer id);
}
