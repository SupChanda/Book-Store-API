package com.book.store.models.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "BooksPurchased")
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldNameConstants
public class BooksPurchased {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID")
        private int id;

        @ManyToOne
        @JoinColumn(name = "BookID", referencedColumnName = "ID", nullable = false)
        private Books books;

        @ManyToOne
        @JoinColumn(name = "UserID", referencedColumnName = "ID", nullable = false)
        private BookUser user;

        @Column(name = "PurchasedDate",nullable = true)
        private Date purchasedDate;

        @Column(name = "TransactionType")
        private String transactionType;

        @Column(name = "RentalStartDate",nullable = true)
        private Date rentalStartDate;

        @Column(name = "RentalEndDate", nullable = true) // ADD return date
        private Date rentalEndDate;

        @Column(name = "Quantity")
        private int quantity;

        @Column(name = "PurchasedPrice")
        private float purchasedPrice;


        @Column(name = "RentalFeeAccrued")
        private float rentalFeeAccrued;

        }


