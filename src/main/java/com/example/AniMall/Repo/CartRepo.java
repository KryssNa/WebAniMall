package com.example.AniMall.Repo;


import com.example.AniMall.Entity.Cart;
import com.example.AniMall.Entity.Pet;
import com.example.AniMall.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Integer> {
    @Query(value = "SELECT * FROM cart WHERE user_id = ?1 AND status='Added'", nativeQuery = true)
    Optional<List<Cart>> fetchAll(Integer userId);

    @Query(value = "SELECT user_id, COUNT(*) as count FROM Cart GROUP BY user_id", nativeQuery = true)
    List<Object[]> fetchAllCustomer();

    @Query(nativeQuery = true, value = "SELECT * from Cart")
    List<Cart> findallCart();

    @Query(value = "UPDATE cart SET quantity = ?1 WHERE id = ?2", nativeQuery = true)
    @Modifying // Add this annotation for update queries
    @Transactional
    int updateQuantity(int newQuantity, Integer id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE Cart SET status = :status WHERE id = :id")
    void updateTaskStatus(@Param("id") Integer id, @Param("status") String status);


    @Query(nativeQuery = true, value = "SELECT * FROM Cart WHERE status = :status")
    List<Cart> findCartByStatus(@Param("status") String status);

//    delete form cart using pet id
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM Cart WHERE pet_id = ?1")
    void deleteCartByPetId(Integer pet_id);
}

