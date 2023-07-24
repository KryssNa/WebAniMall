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
import org.thymeleaf.util.StringUtils;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserServices {
    private final UserRepo userRepo;
    private final BookingRepo bookingRepo;
    private final FavoriteRepo favoriteRepo;
    @Override
    public UserPojo save(UserPojo userPojo) {

        User user;
        if (userPojo.getId() != null) {
            user = userRepo.findById(userPojo.getId()).orElseThrow(() -> new RuntimeException("Not Found"));
        } else {
            System.out.println("userPojo.getEmail() = " + userPojo.getId());
            user = new User();
        }
        user.setEmail(userPojo.getEmail());
        user.setFullname(userPojo.getFullname());
        user.setAge(userPojo.getAge());
        user.setGender(userPojo.getGender());
        user.setPhone(userPojo.getPhone());
        user.setAddress(userPojo.getAddress());
        user.setCountry(userPojo.getCountry());
        user.setAbout(userPojo.getAbout());
//        // Only update the password if it is provided in the form
//        if (!StringUtils.isEmpty(userPojo.getPassword())) {
//            user.setPassword(PasswordEncoderUtil.getInstance().encode(userPojo.getPassword()));
//        }

        // Only update the password if it is provided in the form and not empty
        if (userPojo.getPassword() != null && !userPojo.getPassword().isEmpty()) {
            user.setPassword(PasswordEncoderUtil.getInstance().encode(userPojo.getPassword()));
        }
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
                .age(user.getAge())
                .gender(user.getGender())
                .phone(user.getPhone())
                .address(user.getAddress())
                .country(user.getCountry())
                .about(user.getAbout())
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

