package com.example.AniMall.Services;

import com.example.AniMall.Entity.Cart;
import com.example.AniMall.Entity.Pet;
import com.example.AniMall.Pojo.CartPojo;
import com.example.AniMall.Pojo.PetPojo;

import java.util.*;
import java.io.IOException;

public interface PetServices {
    PetPojo save(PetPojo petPojo) throws IOException;
    List<Pet> findAll();
    List<Pet> getThreeRandomData();
    Pet findById(Integer id);

    List<Pet> getLimitedPets(int page,int petsPerPage);

    int countTotalPages(int petsPerPage);
    void deleteById(Integer id);

    List<Pet> findPetByPartialName(String partialName);
}

