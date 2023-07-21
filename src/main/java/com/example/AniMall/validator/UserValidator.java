package com.example.AniMall.validator;

import com.example.AniMall.Entity.User;
import com.example.AniMall.Services.UserServices;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final UserServices userService;

    public UserValidator(UserServices userService) {
        this.userService = userService;
    }

//    @Autowired
//    public UserValidator(UserServices userService) {
//        this.userService = userService;
//    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        //Username and password can't be empty or contain whitespace
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "error.not_empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.not_empty");

        // Username must have from 4 characters to 10
        if (user.getUsername().length() < 4) {
            errors.rejectValue("username", "register.error.username.less_4");
        }
        if(user.getUsername().length() > 10){
            errors.rejectValue("username","register.error.username.over_32");
        }
//        //Username can't be duplicated
//        if (userService.findByUsername(user.getUsername()) != null) {
//            errors.rejectValue("username", "register.error.duplicated.username");
//        }
        //Email can't be duplicated
        if (userService.findByEmail(user.getEmail()) != null){
            errors.rejectValue("email", "register.error.duplicated.email");
        }
        //Password must have at least 8 characters and max 32
        if (user.getPassword().length() < 8) {
            errors.rejectValue("password", "register.error.password.less_8");
        }
        if (user.getPassword().length() > 32){
            errors.rejectValue("password", "register.error.password.over_32");
        }
        //Age needs to be higher than 13
        if (user.getAge() <= 13){
            errors.rejectValue("age", "register.error.age_size");
        }
        if (user.getAge() >= 100){
            errors.rejectValue("age", "register.error.age_size_greater");
        }
    }
}
