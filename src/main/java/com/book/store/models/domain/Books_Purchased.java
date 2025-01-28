package com.book.store.models.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "Books_Purchased")
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldNameConstants
public class Books_Purchased {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID")
        private int id;

        @ManyToOne
        @JoinColumn(name = "BookID", referencedColumnName = "ID", nullable = false)
        private Books books;

        @ManyToOne
        @JoinColumn(name = "UserID", referencedColumnName = "ID", nullable = false)
        private User user;

        @Column(name = "PurchasedDate")
        private Date purchasedDate;

        @Column(name = "TransactionType")
        private String transactionType;

        @Column(name = "RentalStartDate")
        private Date rentalStartDate;

        @Column(name = "RentalEndDate") // ADD return date
        private Date rentalEndDate;

        @Column(name = "Quantity")
        private int quantity;

        @Column(name = "PurchasedPrice")
        private float purchasedPrice;


        @Column(name = "RentalFeeAccrued")
        private float rentalFeeAccrued;

        }


