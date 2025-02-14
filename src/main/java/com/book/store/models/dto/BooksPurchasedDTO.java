package com.book.store.models.dto;

import com.book.store.models.domain.BookUser;
import com.book.store.models.domain.Books;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.Date;



@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldNameConstants
public class BooksPurchasedDTO {

    private Integer id;
    private Integer bookId;
    private Integer userId;
    private Date purchasedDate;
    private String transactionType;
    private Date rentalStartDate;
    private Date rentalEndDate;
    private int quantity;
    private float purchasedPrice;
    private float rentalFeeAccrued;

}


