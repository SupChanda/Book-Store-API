package com.book.store.models.contract;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.stereotype.Service;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldNameConstants
@Builder
public class BooksReviewRequest {

    private Integer id;
    private Integer bookId;
    private Integer userId;
    private Date dateReviewed;
    private String comments;

}
