package com.book.store.models.mappers;

import com.book.store.models.contract.BooksReviewRequest;
import com.book.store.models.domain.BooksReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "Spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BooksReviewMapper {

    @Mapping(target = "id",source = "booksReview.id")
    @Mapping(target = "bookId",source = "booksReview.books.id")
    @Mapping(target = "userId",source = "booksReview.user.id")
    @Mapping(target = "dateReviewed",source = "booksReview.dateReviewed")
    @Mapping(target = "comments",source = "booksReview.comments")
    BooksReviewRequest toRequest(BooksReview booksReview);
    BooksReview toBooksReview(BooksReviewRequest booksReviewRequest);
    List<BooksReviewRequest> toBooksReviewRequestListFromRequest(List<BooksReview> booksReviewList);
}
