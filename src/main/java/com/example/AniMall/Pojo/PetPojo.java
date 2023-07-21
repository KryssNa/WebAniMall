package com.example.AniMall.Pojo;

import com.example.AniMall.Entity.Pet;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PetPojo {
    private Integer id;

    @NotEmpty(message = "Petname can't be empty")
    private String petname;

    @NotEmpty(message = "Description can't be empty")
    private String description;

    @NotEmpty(message = "Breed can't be empty")
    private String breed;

    @NotNull(message = "Price can't be null")
    private Integer price;

    @NotEmpty(message = "Category can't be empty")
    private String category;

    @NotEmpty(message = "Color can't be empty")
    private String color;

    @NotNull(message = "Quantity can't be null")
    private Integer quantity;

    @NotNull(message = "Image can't be null")
    private MultipartFile image;

    public PetPojo(Pet pet){
        this.id = pet.getId();
        this.description = pet.getDescription();
        this.petname = pet.getPetname();
        this.breed = pet.getBreed();
        this.price = pet.getPrice();
        this.category = pet.getCategory();
        this.color = pet.getColor();
    }
}
