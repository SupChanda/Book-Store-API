package com.book.store.models.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.time.*;

@Entity
@Getter
@Setter
@Table(name = "Books")
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldNameConstants
@Builder
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "Title",unique = true)
    private String title;

    @Column(name = "Author")
    private String author;

    @Column(name = "Genre")
    private String genre;

    @Column(name = "Price")
    private float price;

    @Column(name = "RentalFee")
    private float rentalFee;
//
//    @Column(name = "CreatedBy")
//    private String createdBy;

    @Column(name = "NoOfCopies")
    private int noOfCopies;
}
