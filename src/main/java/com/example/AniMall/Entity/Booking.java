package com.example.AniMall.Entity;
import jakarta.persistence.*;
import lombok.*;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="bookings")
public class Booking {
    @Id
    @SequenceGenerator(name = "book_seq_gen", sequenceName = "book_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "book_seq_gen", strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pet_id", referencedColumnName = "id")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(columnDefinition = "varchar(255) default 'Pending'")
    private String status;

    @Column
    private String shippingFullName;

    @Column
    private String shippingEmail;

    @Column
    private String shippingPhone;

    @Column
    private String shippingAddress;

    @Column
    private double total;

    @Column
    private int quantity;

    @Column(name = "image")
    private String image;

    @Transient
    private String imageBase64;

}

