package com.book.store.Repository;

import com.book.store.models.domain.BooksPurchased;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BooksPurchaseRepository extends JpaRepository<BooksPurchased, Integer> {

}
