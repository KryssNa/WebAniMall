package com.example.AniMall.Pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.example.AniMall.Entity.Favorite;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoritePojo {

    @Positive(message = "ID must be a positive number.")
    private Integer id;

    @NotNull(message = "Pet ID cannot be null.")
    @Positive(message = "Pet ID must be a positive number.")
    private int pet_id;

    @NotNull(message = "User ID cannot be null.")
    @Positive(message = "User ID must be a positive number.")
    private int user_id;

    public FavoritePojo(Favorite favorite) {
        this.id=favorite.getId();
        this.pet_id=favorite.getPet_id().getId();
        this.user_id=favorite.getUser_id().getId();
    }
}
