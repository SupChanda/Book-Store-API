package com.book.store.Repository;

import com.book.store.models.domain.Books;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface BooksRepository extends JpaRepository<Books,Integer>{
    Books findByTitle(String title);

}
