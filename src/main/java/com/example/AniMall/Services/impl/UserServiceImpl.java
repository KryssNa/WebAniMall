package com.example.AniMall.Services.impl;

import com.example.AniMall.Entity.User;
import com.example.AniMall.Pojo.UserPojo;
import com.example.AniMall.Repo.BookingRepo;
import com.example.AniMall.Repo.FavoriteRepo;
import com.example.AniMall.Services.UserServices;
import com.example.AniMall.config.PasswordEncoderUtil;
import com.example.AniMall.exception.AppException;
import com.example.AniMall.Repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserServices {
    private final UserRepo userRepo;
    private final BookingRepo bookingRepo;
    private final FavoriteRepo favoriteRepo;
    private final ThreadPoolTaskExecutor taskExecutor;
    @Override
    public UserPojo save(UserPojo userPojo) {
        User user;
        if (userPojo.getId() != null) {
            user = userRepo.findById(userPojo.getId()).orElseThrow(() -> new RuntimeException("Not Found"));
        } else {
            user = new User();
        }
        user.setEmail(userPojo.getEmail());
        user.setFullname(userPojo.getFullname());
        user.setAge(userPojo.getAge());
        user.setGender(userPojo.getGender());
        user.setPassword(PasswordEncoderUtil.getInstance().encode(userPojo.getPassword()));
        userRepo.save(user);
        return new UserPojo(user);
    }
    @Override
    public List<User> fetchAll() {
        return userRepo.findAll();
    }
    @Override
    public User findByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new AppException("Invalid User email", HttpStatus.BAD_REQUEST));
        return user;
    }

    @Override
    public User findBYId(Integer id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        user = User.builder()
                .id(user.getId())
                .fullname(user.getFullname())
                .email(user.getEmail())
                .build();
        return user;
    }



    @Override
    public void deleteById(Integer id) {
        bookingRepo.deleteById(id);
        favoriteRepo.deleteById(id);
        userRepo.deleteById(id);
    }



}

