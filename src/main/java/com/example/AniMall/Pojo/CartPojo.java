package com.example.AniMall.Pojo;


import com.example.AniMall.Entity.Pet;
import com.example.AniMall.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartPojo {
    private Integer id;
    private Pet pet;
    private User user;
    private Integer quantity;
    private String status;
    private Date createdDate;
}
