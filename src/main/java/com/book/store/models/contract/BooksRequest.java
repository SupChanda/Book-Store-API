package com.book.store.models.contract;


import lombok.*;
import lombok.experimental.FieldNameConstants;


@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldNameConstants
@Builder
public class BooksRequest {

    private Integer id;


}
