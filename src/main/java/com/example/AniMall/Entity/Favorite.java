package com.example.AniMall.Entity;

import jakarta.persistence.*;
import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="favorites")
public class Favorite {
    @Id
    @SequenceGenerator(name = "fav_seq_gen", sequenceName = "fav_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "fav_seq_gen", strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pet_id", referencedColumnName = "id")
    private Pet pet_id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user_id;
}

