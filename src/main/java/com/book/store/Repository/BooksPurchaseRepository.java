package com.book.store.Repository;

import com.book.store.models.domain.Books_Purchased;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BooksPurchaseRepository extends JpaRepository<Books_Purchased, Integer> {

}
