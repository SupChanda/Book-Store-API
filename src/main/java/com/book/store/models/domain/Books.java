package com.book.store.models.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Getter
@Setter
@Table(name = "BookRecord")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "UserID")
    private int userID;

    @Column(name = "Title")
    private String title;

    @Column(name = "Author")
    private String author;

    @Column(name = "Genre")
    private String genre;

    @Column(name = "TransactionType")
    private String transactionType;

    @Column(name = "ReturnDate")
    private LocalDate returnDate;

    @Column(name = "Price")
    private float price;

    @Column(name = "RentalFee")
    private float rentalFee;





}
