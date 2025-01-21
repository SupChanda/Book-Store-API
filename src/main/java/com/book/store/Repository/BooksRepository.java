package com.book.store.Repository;

import com.book.store.models.domain.Books;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends JpaRepository<Books,Integer>{
    Books findByTitle(String title);

//    @Modifying
//    @Query(value ="UPDATE Book_Record SET author =:author WHERE u.title = :title")
//    //,genre =:genre" +
//    //        ",price =:price, rentalFee =:rentalFee, createdBy =: createdBy, noOfCopies =:noOfCopies" +
//     //       "
//    void updateBookDetails(String title,String author);

}
