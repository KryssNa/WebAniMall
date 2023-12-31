package com.example.AniMall.Repo;

import com.example.AniMall.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    @Query(value = "select * from users where email=?1", nativeQuery = true)
    Optional<User> findByEmail(String email);

    @Query(value="update users set password=?1 where email=?2",nativeQuery = true)
    void updatePassword(String password,String email);

    Optional<User> findUserByFullname(String fullname);
}


