package com.example.AniMall.Repo;

import com.example.AniMall.Entity.ShippingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SummaryDetailsRepo extends JpaRepository<ShippingDetails, Integer> {

}
