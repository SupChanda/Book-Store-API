package com.book.store.models.mappers;

import com.book.store.models.domain.BooksPurchased;
import com.book.store.models.dto.BooksPurchasedDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "Spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BooksPurchasedMapper {
    @Mapping(target = "id",source = "booksPurchased.id")
    @Mapping(target = "bookId",source = "booksPurchased.books.id")
    @Mapping(target = "userId",source = "booksPurchased.user.id")
    @Mapping(target = "purchasedDate",source = "booksPurchased.purchasedDate")
    @Mapping(target = "transactionType",source = "booksPurchased.transactionType")
    @Mapping(target = "rentalStartDate",source = "booksPurchased.rentalStartDate")
    @Mapping(target = "rentalEndDate",source = "booksPurchased.rentalEndDate")
    @Mapping(target = "quantity",source = "booksPurchased.quantity")
    @Mapping(target = "purchasedPrice",source = "booksPurchased.purchasedPrice")
    @Mapping(target = "rentalFeeAccrued",source = "booksPurchased.rentalFeeAccrued")
    BooksPurchasedDTO toDTO(BooksPurchased booksPurchased);

    BooksPurchased toBooksPurchasedFromDTO(BooksPurchasedDTO booksPurchasedDTO);
}
