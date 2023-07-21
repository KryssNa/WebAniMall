package com.example.AniMall.Pojo;

import com.example.AniMall.Entity.User;
import jakarta.validation.constraints.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPojo {

    private Integer id;

    @NotEmpty(message = "Email can't be empty")
//    @Pattern(regexp = "^\\S+@\\S+\\.\\S+$", message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Fullname can't be empty")
    private String fullname;

    @NotEmpty(message = "Password can't be empty")
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "Password must be at least 8 characters long, contain at least one letter and one number")
    private String password;

//    @NotEmpty(message = "Gender can't be empty")
    private String gender;

//    @NotNull(message = "Age can't be null")
    @Min(value = 10, message = "Age must be at least 10")
    @Max(value = 99, message = "Age must be at most 99")
    private Integer age;



    public UserPojo(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.fullname = user.getFullname();
        this.gender = user.getGender();
        this.age = user.getAge();
        this.password = user.getPassword();
    }
}
