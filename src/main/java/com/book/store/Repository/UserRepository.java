package com.book.store.Repository;

import com.book.store.models.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;

@Repository
public interface UserRepository extends JpaRepository<BookUser,Integer> {
    BookUser findByUserName(String uName); // TO DO : User repo implementation custom query. DAO implementation for reference
}
