package com.example.AniMall.Services.impl;


import com.example.AniMall.Entity.Otp;
import com.example.AniMall.Entity.User;
import com.example.AniMall.Pojo.UserPojo;
import com.example.AniMall.Repo.BookingRepo;
import com.example.AniMall.Repo.FavoriteRepo;
import com.example.AniMall.Repo.OtpRepo;
import com.example.AniMall.Services.UserServices;
import com.example.AniMall.config.PasswordEncoderUtil;
import com.example.AniMall.exception.AppException;
import com.example.AniMall.Repo.UserRepo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserServices {
    private final UserRepo userRepo;
    private final BookingRepo bookingRepo;
    private final FavoriteRepo favoriteRepo;
    private final OtpRepo otpRepo;

    private final ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    @Qualifier("emailConfigBean")
    private Configuration emailConfig;

    private final JavaMailSender getJavaMailSender;
    @Override
    public UserPojo save(UserPojo userPojo) {

        User user;
        if (userPojo.getId() != null) {
            user = userRepo.findById(userPojo.getId()).orElseThrow(() -> new RuntimeException("Not Found"));
            userPojo.setPassword(user.getPassword());
        } else {
            user = new User();
            user.setPassword(PasswordEncoderUtil.getInstance().encode(userPojo.getPassword()));
        }

        user.setEmail(userPojo.getEmail());
        user.setFullname(userPojo.getFullname());
        user.setAge(userPojo.getAge());
        user.setGender(userPojo.getGender());
        user.setPhone(userPojo.getPhone());
        user.setAddress(userPojo.getAddress());
        user.setCountry(userPojo.getCountry());
        user.setAbout(userPojo.getAbout());
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

    @Override
    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return PasswordEncoderUtil.getInstance().matches(oldPassword, user.getPassword());
    }

    @Override
    public void changeUserPassword(User user, String newPassword) {
        userRepo.updatePassword(PasswordEncoderUtil.getInstance().encode(newPassword),user.getEmail());
        System.out.println("newPassword = " + newPassword);
    }

    @Override
    public void update(User user) {
        userRepo.save(user);
    }

    public void sendResetEmail(String email) {
        try {
            User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

            Map<String, String> model = new HashMap<>();
            model.put("name", user.getFullname());
            String otpCode= generateRandomNumber();
            model.put("otp", otpCode);
            Otp otp = otpRepo.findByEmail(email).orElse(new Otp());
            otp.setEmail(email);
            otp.setOtp(otpCode);
            otpRepo.save(otp);

            MimeMessage message = getJavaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Template template = emailConfig.getTemplate("Email/resetPassTemp.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setText(html, true);
            mimeMessageHelper.setSubject("Registration");
            mimeMessageHelper.setFrom("krishna.kryss@gmail.com");;


            taskExecutor.execute(new Thread() {
                public void run() {
                    getJavaMailSender.send(message);
                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    public static String generateRandomNumber() {
        Random random = new Random();

        int otp=100000 + random.nextInt(900000);
        return String.valueOf(otp);
        // Generates a random number between 100000 and 999999 (inclusive)
    }

    @Override
    public void resetPass(String email, String password, String Otp) throws IOException {
        Otp otp1 = otpRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Email not found"));
        if (otp1.getOtp().equals(Otp) && otp1.getDate().isAfter(LocalDateTime.now())) {
            User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
            user.setPassword(PasswordEncoderUtil.getInstance().encode(password));
            userRepo.save(user);
        } else {
            throw new RuntimeException("Invalid OTP");
        }
    }
}

