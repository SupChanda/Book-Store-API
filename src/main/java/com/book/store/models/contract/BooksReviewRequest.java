package com.book.store.models.contract;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldNameConstants
public class BooksReviewRequest {

    private int id;
    private int bookId;
    private int userId;
    private Date dateReviewed;
    private String comments;

}
