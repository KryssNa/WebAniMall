package com.example.AniMall.Repo;

import com.example.AniMall.Entity.Booking;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking,Integer> {
    @Query(value = "SELECT * FROM bookings where user_id=?1", nativeQuery = true)
    List<Booking> findBookingByUserId(Integer id);

    @Query(value = "SELECT * FROM bookings where id=?1", nativeQuery = true)
    List<Booking> findBookingById(Integer id);

    @Query(value = "DELETE from bookings where user_id=?1", nativeQuery = true)
    Integer deleteByUser(Integer id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM bookings WHERE pet_id = ?1")
    void deleteBookingByPetId(Integer pet_id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE bookings SET status = ?2 WHERE id = ?1")
    void updateStatus(Integer id, String status);
}
