package com.example.AniMall.Entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "summary_details")
public class SummaryDetails {
    @Id
    @SequenceGenerator(name = "summary_seq_gen", sequenceName = "summary_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "summary_seq_gen", strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking;

    @Column
    private double totalPrice;

    @Column
    private String status;

    @Column
    private int totalQuantity;

    @Column(name = "image")
    private String image;

    @Transient
    private String imageBase64;

    @Column
    private String shippingFullName;

    @Column
    private String shippingEmail;

    @Column
    private String shippingPhone;

    @Column
    private String shippingAddress;


}
