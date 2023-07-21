package com.example.AniMall;


import com.example.AniMall.Entity.Pet;
import com.example.AniMall.Repo.PetRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PetRepositoryTest {
    @Autowired
    private PetRepo petRepo;
    @Test
    @Order(1)
    @Rollback(value=false)
    public void savePetTest(){
        Pet pet= Pet.builder()
                .petname("Golu")
                .breed("idk")
                .category("dog")
                .price(1000)
                .quantity(5)
                .color("Black")
                .description("this is a dog")
                .image("xyz")
                .build();

        petRepo.save(pet);

        Assertions.assertThat(pet.getId()).isGreaterThan(0);
    }
    @Test
    @Order(2)
    public void getPetTest(){
        Pet petCreated=petRepo.findById(1).get();
        Assertions.assertThat(petCreated.getId()).isEqualTo(1);
    }
//    @Test
//    @Order(3)
//    public void getListOfPetTest(){
//        List<Pet> Pets=petRepo.findAll();
//        Assertions.assertThat(Pets.size()).isGreaterThan(0);
//    }
    @Test
    @Order(4)
    @Rollback(value=false)
    public void updatePetTest(){
        Pet pet=petRepo.findById(1).get();
        pet.setPetname("golden");
        Pet petUpdated=petRepo.save(pet);
        Assertions.assertThat(petUpdated.getPetname()).isEqualTo("golden");
    }
    @Test
    @Order(5)
    @Rollback(value=false)
    public void deletePetTest(){
        Pet pet=petRepo.findById(1).get();
        petRepo.delete(pet);
        Pet pet1=null;
        Optional<Pet> optionalPet=petRepo.findById(1);
        if(optionalPet.isPresent()){
            pet1=optionalPet.get();
        }
        Assertions.assertThat(pet1).isNull();
    }
}
