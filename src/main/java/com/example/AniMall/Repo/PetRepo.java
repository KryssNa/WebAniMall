package com.example.AniMall.Repo;

import com.example.AniMall.Entity.Pet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepo extends JpaRepository<Pet,Integer> {
    @Query(value = "SELECT * FROM pet ORDER BY RANDOM() LIMIT 3", nativeQuery = true)
    List<Pet> getThreeRandomData();

    @Query(value = "SELECT * FROM pet LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Pet> getLimitedPets(int offset, int limit);

    @Query(value = "SELECT COUNT(*) FROM pet", nativeQuery = true)
    int countTotalPets();

    @Query(value = "SELECT * FROM pet WHERE LOWER(pet_name) LIKE ?1", nativeQuery = true)
    List<Pet> findPetByPartialName( String partialName);

    @Query(value = "UPDATE pet SET quantity = ?1 WHERE id = ?2", nativeQuery = true)
    @Modifying // Add this annotation for update queries
    @Transactional
    int updateQuantity(int newQuantity, Integer id);

    @Query(value = "SELECT * FROM pet WHERE id = ?1", nativeQuery = true)
    Pet findPetById(Integer id);
}
