package com.book.store.models.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.web.bind.annotation.RequestHeader;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class BooksPurchasedRequest {
    private int  bookId;
    private String transactionType;
    private int quantity;
    private int currentUserId;
}
