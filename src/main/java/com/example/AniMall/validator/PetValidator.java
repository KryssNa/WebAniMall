package com.example.AniMall.validator;

import com.example.AniMall.Entity.Pet;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PetValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Pet.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Pet pet = (Pet) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name","error.not_empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "error.not_empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "imageUrl", "error.not_empty");
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "error.not_empty");

        // Name must have from 2 characters to 32
        if (pet.getPetname().length() <= 1) {
            errors.rejectValue("name", "pet.error.name.less_2");
        }
        if (pet.getPetname().length() > 32) {
            errors.rejectValue("name", "pet.error.name.over_32");
        }
    }
}
