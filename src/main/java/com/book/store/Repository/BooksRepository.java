package com.book.store.Repository;

import com.book.store.models.domain.Books;
import com.book.store.models.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends JpaRepository<Books,Integer>{
    Books findByTitle(String title);
}
