package com.book.store.dao.impl;

import com.book.store.Repository.BooksRepository;
import com.book.store.dao.BooksManagementDao;
import com.book.store.dao.BooksPurchaseDao;
import com.book.store.dao.UserDao;
import com.book.store.models.domain.BookUser;
import com.book.store.models.domain.BooksPurchased;
import com.book.store.models.domain.Books;
import com.book.store.models.dto.BooksDTO;
import com.book.store.models.dto.BooksPurchasedDTO;
import com.book.store.models.dto.UserDTO;
import com.book.store.models.mappers.BooksMapper;
import com.book.store.models.mappers.BooksPurchasedMapper;
import com.book.store.models.mappers.UserMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Repository
//@Transactional
public class BooksPurchaseDaoImpl extends GenericDaoImpl<BooksPurchased> implements BooksPurchaseDao {

    @Autowired
    BooksRepository booksRepository;
    @Autowired
    BooksPurchasedMapper booksPurchasedMapper;
    @Autowired
    BooksMapper booksMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    BooksManagementDao booksManagementDao;
    @Autowired
    UserDao userDao;
    @PersistenceContext
    private EntityManager entityManager;

    private final String queryFromTemplate = "FROM ${Object} b";
    private final String queryWHERETemplate = " WHERE b.${id} = :id";
    private final String updateQueryTemplate = "UPDATE ${BooksPurchased} b SET b.${title} = :title, " +
            "b.${author} = :author, b.${genre} = :genre, b.${rentalFee} = :rentalFee, " +
            "b.${noOfCopies} = :noOfCopies WHERE b.${id} = :id";

    private final String UpdateBookNoOfCopiesTemplate = "UPDATE ${BookRecord} b SET b.${noOfCopies} = :noOfCopies" + queryWHERETemplate;
    private String queryTemplate;
    private String queryString;

    private final Date purchasedDate = Date.valueOf(LocalDate.now());
    private final Date rentalStartDate = Date.valueOf((LocalDate.now()));
    private final Date rentalEndDate    = Date.valueOf(LocalDate.now());
    Date today =  Date.valueOf(LocalDate.now());
    private Query queryInsert;
    private Query queryUpdate;
    private Query query;
    public BooksPurchaseDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Object getAllPurchasedBooksDetails(){
        try {
            Map<String,Object> templateValues = new HashMap<>();
            templateValues.put("Object", BooksPurchased.class.getName());
            System.out.println("in books Purchased dao impl");
            queryString = generateQueryString(queryFromTemplate,templateValues);
            return getHQLQueryResultSet(queryString);
//            BooksPurchasedDTO booksPurchasedDTO = this.booksPurchasedMapper.toDTO(booksPurchased);
//            return booksPurchasedDTO;
        }catch(Exception ex){
            return ex.getMessage();
        }
    }
    public BooksPurchased getPurchasedBooksDetailsById(int id) throws BadRequestException{
        //try {
            Map<String,Object> templateValues = new HashMap<>();
            templateValues.put("Object", BooksPurchased.class.getName());
            templateValues.put("id", BooksPurchased.Fields.id);
            System.out.println("in books Purchased dao impl");
            queryString = generateQueryString(queryFromTemplate+queryWHERETemplate,templateValues);

            Map<String,Object> queryParam = new HashMap<>();
            queryParam.put("id", id);
            return (BooksPurchased) getHQLSingleQueryResultSet(queryString,queryParam);
            //getHQLSingleQueryResultSet(queryString,queryParam);
//            BooksPurchasedDTO booksPurchasedDTO = this.booksPurchasedMapper.toDTO(booksPurchased);
//            return booksPurchasedDTO;
//        }catch(Exception ex){
//            throw new BadRequestException("Invalid id: "+ id);
//        }
    }

    public Object getPurchasedBooksDetailsByUserIdAndBookId(int userId,int bookId){
        try {
            String queryWHEREBookIdTemplate = " AND b.${bookId} = :bookId";
            Map<String,Object> templateValues = new HashMap<>();
            templateValues.put("Object", BooksPurchased.class.getName());
            templateValues.put("id", BooksPurchased.Fields.user + ".id");
            templateValues.put("bookId", BooksPurchased.Fields.books + ".id");
            queryString = generateQueryString(queryFromTemplate + queryWHERETemplate + queryWHEREBookIdTemplate,templateValues);

            Map<String,Object> queryParam = new HashMap<>();
            queryParam.put("id", userId);
            queryParam.put("bookId", bookId);
            return getHQLSingleQueryResultSet(queryString,queryParam);
        }catch(Exception ex){
            return "Invalid user id: "+ userId + " or book id: " + bookId;
        }
    }
    @Transactional
    public void addBookPurchasedOrRentDetails(BooksDTO booksDTO, UserDTO userDTO, String transactionType, int quantity) throws BadRequestException {

        Map<String,Object> templateValues = new HashMap<>();
        Map<String,Object> queryParam = new HashMap<>();

    try {

    //CHECK HOW MANY BOOKS ARE LEFT : START ***************************************************

        templateValues.put("Object",Books.class.getName());
        templateValues.put("id",Books.Fields.id);
        System.out.println("inside CHECK HOW MANY BOOKS ARE LEFT");
        queryString = generateQueryString(queryFromTemplate + queryWHERETemplate,templateValues);

        queryParam.put("id",booksDTO.getId());
        Books books = (Books) getHQLSingleQueryResultSet(queryString,queryParam);

        if(books.getNoOfCopies() < quantity){
            String BookTitle = books.getTitle();
            System.out.println("books.getNoOfCopies() < quantity");
            throw new BadRequestException("Unfortunately there are only "+ books.getNoOfCopies() + " '" +  BookTitle + "' book(s) is(are) left");
        }
    //CHECK HOW MANY BOOKS ARE LEFT : END ***************************************************
    //Checking whether the user has active membership : START *********************************************

        templateValues = new HashMap<>();
        templateValues.put("Object", BookUser.class.getName());
        templateValues.put("id",BookUser.Fields.id);
        queryString = generateQueryString(queryFromTemplate + queryWHERETemplate,templateValues);

        queryParam = new HashMap<>();
        queryParam.put("id",userDTO.getId());
        BookUser user = (BookUser) getHQLSingleQueryResultSet(queryString,queryParam);

        Boolean isActiveMember = user.getIsActiveMember();

        System.out.println("Is Active Member?: " + isActiveMember);
        if ( !isActiveMember) {
            throw new BadRequestException( "User id: " + user.getId()+ " is not an active Member");
        }
    //Checking whether the user has active membership : END ***************************************

        Books books1 = (Books) booksManagementDao.getBooksByIdOrName(booksDTO.getId());
        BookUser user1 = (BookUser) userDao.getUsrByUserId(userDTO.getId());

    //Checking whether the member has already rented 2 books : START ***************************************


        if(transactionType.equalsIgnoreCase("Rented")) {
            System.out.println("Inside checking whether the member has already rented 2 books: ");
            queryTemplate = queryFromTemplate + queryWHERETemplate;
            queryTemplate += " AND b.${rentalStartDate} IS NULL AND b.${transactionType} =: transactionType";

            templateValues = new HashMap<>();
            templateValues.put("Object",BooksPurchased.class.getName());
            templateValues.put("id",BooksPurchased.Fields.id);
            templateValues.put("rentalStartDate", BooksPurchased.Fields.rentalStartDate);
            templateValues.put("transactionType", BooksPurchased.Fields.transactionType);
            queryString = generateQueryString(queryTemplate,templateValues);

            queryParam = new HashMap<>();
            queryParam.put("id",userDTO.getId());
            queryParam.put("transactionType",transactionType.toLowerCase());
            Long booksRentCount = (Long) getHQLQueryCount(queryString,queryParam);

            System.out.println("booksRentCount: " + booksRentCount);

            if (booksRentCount > 1) {
                System.out.println("booksRentCount > 1");
                throw new BadRequestException( userDTO.getId() + " can only rent 2 books at a time");
            }



            BooksPurchased booksRent = new BooksPurchased();
            booksRent.setBooks(books1);
            booksRent.setUser(user1);
            booksRent.setPurchasedDate(null);
            booksRent.setTransactionType(transactionType);
            booksRent.setRentalStartDate(rentalStartDate);
            booksRent.setRentalEndDate(null);
            booksRent.setQuantity(quantity);
            booksRent.setPurchasedPrice(0.0f);
            booksRent.setRentalFeeAccrued(booksDTO.getRentalFee());

            //Adding Book Rent Record
            saveOrUpdate(booksRent);
            booksRentCount += quantity;

            //Updating Book Record with No Of Copies
            templateValues = new HashMap<>();
            templateValues.put("BookRecord",Books.class.getName());
            templateValues.put("noOfCopies",Books.Fields.noOfCopies);
            templateValues.put("id",Books.Fields.id);

            //queryString = generateQueryString(updateQueryTemplate,templateValues);
            queryParam = new HashMap<>();//noOfCopies
            queryParam.put("noOfCopies",booksDTO.getNoOfCopies()-booksRentCount);
            queryParam.put("id",booksDTO.getId());

            updateOrDeleteObject(generateQueryString(UpdateBookNoOfCopiesTemplate,templateValues),queryParam);
            //String UpdateBookRecordSql = "UPDATE Book_Record SET noOfCopies = :noOfCopies WHERE ID = :ID";

    // Checking whether the member has already rented 2 books : END ***************************************

    // Purchased Book Logic  : START ***************************************
        }else{
            System.out.println("Inside checking how many times the book is purchased: ");
            queryTemplate = queryFromTemplate + queryWHERETemplate;
            queryTemplate += " AND b.${transactionType} = :transactionType";

            templateValues = new HashMap<>();
            templateValues.put("Object",BooksPurchased.class.getName());
            templateValues.put("id",BooksPurchased.Fields.id);
            templateValues.put("transactionType",BooksPurchased.Fields.transactionType);
            queryString = generateQueryString(queryTemplate,templateValues);

            queryParam = new HashMap<>();
            queryParam.put("id",booksDTO.getId());
            queryParam.put("transactionType",transactionType.toLowerCase());
            Long booksPurchasedCount = (Long) getHQLQueryCount(queryString,queryParam);

            BooksPurchased booksPurchased = new BooksPurchased();
            booksPurchased.setBooks(books1);
            booksPurchased.setUser(user1);
            booksPurchased.setPurchasedDate(purchasedDate);
            booksPurchased.setTransactionType(transactionType);
            booksPurchased.setRentalStartDate(null);
            booksPurchased.setRentalEndDate(null);
            booksPurchased.setQuantity(quantity);
            booksPurchased.setPurchasedPrice(booksDTO.getPrice());
            booksPurchased.setRentalFeeAccrued(0.0f);

            //Adding Book Purchase Record
            saveOrUpdate(booksPurchased);

            booksPurchasedCount+=quantity;

            //Updating Book Record with No Of Copies
            templateValues = new HashMap<>();
            templateValues.put("BookRecord",Books.class.getName());
            templateValues.put("noOfCopies",Books.Fields.noOfCopies);
            templateValues.put("id",Books.Fields.id);

            //queryString = generateQueryString(updateQueryTemplate,templateValues);
            queryParam = new HashMap<>();//noOfCopies
            queryParam.put("noOfCopies",booksDTO.getNoOfCopies()-booksPurchasedCount);
            queryParam.put("id",booksDTO.getId());

            updateOrDeleteObject(generateQueryString(UpdateBookNoOfCopiesTemplate,templateValues),queryParam);
        }
    // Purchased Book Logic  : END ***************************************
    }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }


    @Transactional
    public void UpdateBookDetailsOnReturn(BooksPurchasedDTO booksPurchasedDTO) throws BadRequestException {
        try {
            //UpdateBookRecordTemplate = "UPDATE ${BookRecord} b SET b.${noOfCopies} = :noOfCopies" + queryWHERETemplate;
            if(booksPurchasedDTO.getTransactionType().equalsIgnoreCase("purchased")){
                throw new BadRequestException("User id: "+ booksPurchasedDTO.getUserId() + " cannot return because he/she has purchased the book id: " + booksPurchasedDTO.getBookId());
            }

            Map<String,Object> templateValues ;
            Map<String,Object> queryParam;
            templateValues = new HashMap<>();
            templateValues.put("Object",Books.class.getName());
            templateValues.put("id",Books.Fields.id);

            queryString = generateQueryString(queryFromTemplate + queryWHERETemplate,templateValues);

            queryParam = new HashMap<>();//noOfCopies
            queryParam.put("id",booksPurchasedDTO.getBookId());
            System.out.println("yyyyyyyyyyyyyyyy");
            int noOfCopies =  ((Books) getHQLSingleQueryResultSet(queryString,queryParam)).getNoOfCopies();
            System.out.println("no of copies " + noOfCopies);

            //We got number of copies

            // update books copies
            templateValues = new HashMap<>();
            templateValues.put("BookRecord",Books.class.getName());
            templateValues.put("noOfCopies",Books.Fields.noOfCopies);
            templateValues.put("id",Books.Fields.id);

            queryString = generateQueryString(UpdateBookNoOfCopiesTemplate,templateValues);


            queryParam = new HashMap<>();//noOfCopies
            queryParam.put("noOfCopies",booksPurchasedDTO.getQuantity() + noOfCopies);
            queryParam.put("id",booksPurchasedDTO.getBookId());

            updateOrDeleteObject(queryString,queryParam);

            // update books rented date and fee

            templateValues = new HashMap<>();
            templateValues.put("Object",BooksPurchased.class.getName());
            templateValues.put("id",BooksPurchased.Fields.id);

            queryString = generateQueryString(queryFromTemplate+queryWHERETemplate,templateValues);

            queryParam = new HashMap<>();
            queryParam.put("id",booksPurchasedDTO.getId());

            if(getHQLSingleQueryResultSet(queryString,queryParam)== null){
                throw new BadRequestException("books purchased id " + booksPurchasedDTO.getId() + " is invalid");
            }


            String UpdateBookPurchasedRentalDateAndFeeTemplate = "UPDATE ${BookPurchased} b SET b.${rentalEndDate} = :rentalEndDate, " +
                    "b.${rentalFeeAccrued} = :rentalFeeAccrued " + queryWHERETemplate;

            templateValues = new HashMap<>();
            templateValues.put("BookPurchased",BooksPurchased.class.getName());
            templateValues.put("rentalEndDate",BooksPurchased.Fields.rentalEndDate);
            templateValues.put("rentalFeeAccrued",BooksPurchased.Fields.rentalFeeAccrued);
            templateValues.put("id",BooksPurchased.Fields.id);

            queryString = generateQueryString(UpdateBookPurchasedRentalDateAndFeeTemplate,templateValues);


            queryParam = new HashMap<>();
            queryParam.put("rentalEndDate",rentalEndDate);
            queryParam.put("rentalFeeAccrued",booksPurchasedDTO.getRentalFeeAccrued());
            queryParam.put("id",booksPurchasedDTO.getId());
            System.out.println("rentalEndDate: " +rentalEndDate);
            updateOrDeleteObject(queryString,queryParam);



//            String queryString;
//            float extraRentalFee = 0.00f;
//            float rentalFeeAccrued = 0.00f;
//            int quantity = 0;
//
//            //System.out.println("yes here");
//
//            //UPDATE RETURNED RENTED BOOK PURCHASE RECORD ONLY AND REJECT IF THE RETURNING BOOK IS PURCHASED
//
//            queryString = "SELECT RentalStartDate,RentalFeeAccrued,Quantity,TransactionType FROM Books_Purchased " + whereClause; //b.RentalStartDate,b.RentalFeeAccrued
//            Query query = entityManager.createNativeQuery(queryString);
//            query.setParameter("bookId", bookId);
//            query.setParameter("userId", userId);
//            query.setParameter("transactionType", "Rented");
//            List<Object[]> resultSet = query.getResultList();
//            for (Object[] res : resultSet) {
//                //System.out.println("here " + res[3]);
//                if(res[3].equals("Purchased")){
//                    throw new BadRequestException("The book cannot be returned as it is purchased and not rented.");
//                }
//                rentalStartDate = (Date) res[0];
//                rentalFeeAccrued = ((Number) res[1]).floatValue();
//                quantity = (int) (res[2]);
//            }
//            //System.out.println("Rental Start date: " + rentalStartDate + " extraRentalFee: " + extraRentalFee);
//
//            long datediff = ChronoUnit.DAYS.between(rentalStartDate.toLocalDate(), today.toLocalDate());
//            System.out.println("Period: " + datediff);
//            if (datediff > 30) {
//                extraRentalFee = rentalFeeAccrued + datediff - 30;
//            }
//
//            //System.out.println("extraRentalFee: " + extraRentalFee);
//
//            queryString = "UPDATE Books_Purchased  SET RentalEndDate = :rentalEndDate, RentalFeeAccrued = :rentalFeeAccrued " + whereClause;
//            query = entityManager.createNativeQuery(queryString);
//            query.setParameter("rentalEndDate", rentalEndDate);
//            query.setParameter("rentalFeeAccrued", extraRentalFee);
//            query.setParameter("bookId", bookId);
//            query.setParameter("userId", userId);
//            query.setParameter("transactionType", "Rented");
//            query.executeUpdate();
//
//            //UPDATE BOOK COPIES RECORD
//            int noOfCopies = 0;
//            queryString = "SELECT NoOfCopies FROM Book_Record WHERE ID = :bookId ";
//            query = entityManager.createNativeQuery(queryString);
//            query.setParameter("bookId", bookId);
//            System.out.println("yes here: ");
//            List<Object> resultSet1 = query.getResultList();
//            for (Object res : resultSet1) {
//                noOfCopies = (int) res;
//            }
//            System.out.println("no of copies: " + noOfCopies);
//            queryString = "UPDATE Book_Record  SET NoOfCopies = :noOfCopies WHERE ID = :bookId";
//            query = entityManager.createNativeQuery(queryString);
//            query.setParameter("bookId", bookId);
//            query.setParameter("noOfCopies", quantity + noOfCopies);
//            query.executeUpdate();
        }
        catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }
}

//                String quantityCheckQuery = "SELECT COUNT(*) FROM Books_Purchased WHERE UserId = :userId AND RentalEndDate IS NULL AND TransactionType = :transactionType"; //totalBookRentedQueryString
//                query = entityManager.createNativeQuery(quantityCheckQuery);
//                query.setParameter("userId", userId);
//                query.setParameter("transactionType", "Rented");
//                int quantityCheck = (int) query.getSingleResult();
//                if (query.getSingleResult() == null) { // get single result in one query
//                    quantityCheck = 0;
//                } else {
//                    quantityCheck = (int) query.getSingleResult();
//                }