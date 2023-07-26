package com.example.AniMall.Services.impl;

import com.example.AniMall.Repo.ShippingRepo;
import com.example.AniMall.Services.ShippingServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ShippingServiceImpl implements ShippingServices {
    private final ShippingRepo shippingRepo;
    @Override
    public void updateShippingStatus(Integer id, String changeStatus) {
        shippingRepo.updateShippingStatus(id, changeStatus);
    }
}
