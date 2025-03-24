package com.book.store.models.dto;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import shaded_package.io.swagger.models.auth.In;

import java.time.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldNameConstants
@Builder
public class BooksDTO {

    private Integer id;

    private String title;

    private String author;

    private String genre;

    private float price;

    private float rentalFee;

    private int noOfCopies;
}
