package com.example.AniMall.Services;

import com.example.AniMall.Entity.User;
import com.example.AniMall.Pojo.UserPojo;

import java.io.IOException;
import java.util.List;

public interface UserServices {
    UserPojo save (UserPojo userPojo);
    User findByEmail(String email);
    User findBYId(Integer id);
    List<User> fetchAll();
    void deleteById(Integer id);

    boolean checkIfValidOldPassword(User user, String oldPassword);

    void changeUserPassword(User user, String newPassword);

    void update(User user);
    void sendResetEmail(String email) throws IOException;

    void resetPass(String email, String password,String Otp) throws IOException;
}
