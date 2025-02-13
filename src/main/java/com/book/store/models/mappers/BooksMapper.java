package com.book.store.models.mappers;

import ch.qos.logback.core.model.ComponentModel;
import com.book.store.models.domain.Books;
import com.book.store.models.dto.BooksDTO;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BooksMapper {
    @Mapping(target = "id",source = "id")
    @Mapping(target = "title",source = "title")
    @Mapping(target = "author",source = "author")
    @Mapping(target = "genre",source = "genre")
    @Mapping(target = "price",source = "price")
    @Mapping(target = "rentalFee",source = "rentalFee")
    @Mapping(target = "noOfCopies",source = "noOfCopies")
    BooksDTO toDTO(Books books);

    List<BooksDTO> toDTOList(List<Books> booksList);
    Books toBooksFromDTO(BooksDTO booksDTO);

}
