package com.book.store.dao.impl;

import com.book.store.Repository.BooksRepository;
import com.book.store.dao.BooksPurchaseDao;
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
    @PersistenceContext
    private EntityManager entityManager;

    private final String queryFromTemplate = "FROM ${Object} b ";
    private final String queryWHERETemplate = "WHERE b.${id} = :id";
    private final String updateQueryTemplate = "UPDATE ${BooksPurchased} b SET b.${title} = :title, " +
            "b.${author} = :author, b.${genre} = :genre, b.${rentalFee} = :rentalFee, " +
            "b.${noOfCopies} = :noOfCopies WHERE b.${id} = :id";
    private String queryTemplate;
    private String queryString;

    private final Date purchasedDate = Date.valueOf(LocalDate.now());
    private Date rentalStartDate = Date.valueOf((LocalDate.now()));
    private final Date rentalEndDate    = Date.valueOf(LocalDate.now());
    Date today =  Date.valueOf(LocalDate.now());
    private Query queryInsert;
    private Query queryUpdate;
    private Query query;
    public BooksPurchaseDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public void setParameter(Query query, int bookId,int userId,String transactionType,int quantity,float purchasedPrice,float rentalFeeAccrued){
        boolean purchased = transactionType.equalsIgnoreCase("Purchased");
        float defaultValue = 0.0f;
        query.setParameter(1, bookId);
        query.setParameter(2, userId);
        query.setParameter(3, transactionType);
        if(purchased){
            query.setParameter(4, purchasedDate);
            query.setParameter(5, null);
            query.setParameter(6, null);
            query.setParameter(8, purchasedPrice);
            query.setParameter(9, defaultValue);
        }else{
            query.setParameter(4, null);
            query.setParameter(5, rentalStartDate);
            query.setParameter(6, null);
            query.setParameter(8, defaultValue);
            query.setParameter(9, rentalFeeAccrued);
        }
        query.setParameter(7, quantity);
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
    public Object getPurchasedBooksDetailsById(int id){
        try {
            Map<String,Object> templateValues = new HashMap<>();
            templateValues.put("Object", BooksPurchased.class.getName());
            templateValues.put("id", BooksPurchased.Fields.id);
            System.out.println("in books Purchased dao impl");
            queryString = generateQueryString(queryFromTemplate+queryWHERETemplate,templateValues);

            Map<String,Object> queryParam = new HashMap<>();
            queryParam.put("id", id);
            return this.booksPurchasedMapper.toDTO((BooksPurchased) getHQLSingleQueryResultSet(queryString,queryParam));
            //getHQLSingleQueryResultSet(queryString,queryParam);
//            BooksPurchasedDTO booksPurchasedDTO = this.booksPurchasedMapper.toDTO(booksPurchased);
//            return booksPurchasedDTO;
        }catch(Exception ex){
            return "Invalid id: "+ id;
        }
    }
    public void addBookPurchasedOrRentDetails(BooksDTO booksDTO, UserDTO userDTO, String transactionType, int quantity) throws BadRequestException {

        Map<String,Object> templateValues = new HashMap<>();
        Map<String,Object> queryParam = new HashMap<>();

    try {

    //CHECK HOW MANY BOOKS ARE LEFT : START ***************************************************

            templateValues.put("Object",Books.class.getName());
            templateValues.put("id",Books.Fields.id);
            queryString = generateQueryString(queryFromTemplate + queryWHERETemplate,templateValues);

            queryParam.put("id",booksDTO.getId());
            Books books = (Books) getHQLSingleQueryResultSet(queryString,queryParam);

            if(books.getNoOfCopies() < quantity){
                String BookTitle = books.getTitle();
                throw new BadRequestException("Unfortunately there are only "+ books.getNoOfCopies() + " '" +  BookTitle + "' book(s) is(are) left");
            }
    //CHECK HOW MANY BOOKS ARE LEFT : END ***************************************************

    //Checking whether the member has already rented 2 books : START ***************************************

            if(transactionType.equalsIgnoreCase("Rented")) {

                queryTemplate = queryFromTemplate + queryWHERETemplate;
                queryTemplate += " AND ${rentalStartDate} IS NULL AND ${transactionType} =: transactionType";

                templateValues = new HashMap<>();
                templateValues.put("Object",BooksPurchased.class.getName());
                templateValues.put("id",BooksPurchased.Fields.id);
                templateValues.put("rentalStartDate", BooksPurchased.Fields.rentalStartDate);
                templateValues.put("transactionType", BooksPurchased.Fields.transactionType);
                queryString = generateQueryString(queryTemplate,templateValues);

                queryParam = new HashMap<>();
                queryParam.put("id",userDTO.getId());
                queryParam.put("transactionType",transactionType);
                Long booksPurchasedCount = (Long) getHQLQueryCount(queryString,queryParam);

                System.out.println("booksPurchasedCount: " + booksPurchasedCount);

                if (booksPurchasedCount > 1) {
                    throw new BadRequestException( userDTO.getId() + " can only rent 2 books at a time");
                }

                Books books1 = booksMapper.toBooksFromDTO(booksDTO);
                BookUser user1 = userMapper.toUserFromUserDTO(userDTO);
                BooksPurchased booksPurchased = new BooksPurchased(0,books1,user1,purchasedDate,transactionType,rentalStartDate,rentalEndDate,quantity,0.0f,booksDTO.getRentalFee());

            }
    //Checking whether the member has already rented 2 books : END ***************************************

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
                throw new BadRequestException( user.getId()+ " is not an active Member");
            }
    //Checking whether the member has already rented 2 books : END ***************************************

//            if(booksRepository.findById(bookId).isPresent()){
//                noOfCopies = booksRepository.findById(bookId).get().getNoOfCopies();
//            }
            //int noOfCopies = booksRepository.findById(bookId).get().getNoOfCopies();




//            String insertBookRecordSql = "INSERT INTO Books_Purchased" +
//                    "(BookID,UserID,TransactionType,PurchasedDate,RentalStartDate,RentalEndDate,Quantity,PurchasedPrice,RentalFeeAccrued) " +
//                    "VALUES(?,?,?,?,?,?,?,?,?)";
            String UpdateBookRecordSql = "UPDATE Book_Record SET noOfCopies = :noOfCopies WHERE ID = :ID";

//            queryInsert = entityManager.createNativeQuery(insertBookRecordSql);
//            setParameter(queryInsert,bookId,userId,transactionType,quantity,purchasedPrice*quantity,rentalFeeAccrued*quantity);
//            queryInsert.executeUpdate();
//
//            queryUpdate = entityManager.createNativeQuery(UpdateBookRecordSql);
//            queryUpdate.setParameter("noOfCopies", noOfCopies- quantity);
//            queryUpdate.setParameter("ID", bookId);
//            queryUpdate.executeUpdate();
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
        //return List.of(new Books_Purchased());
    }


    public void UpdateBookDetailsOnReturn(int bookId, int userId) throws BadRequestException {
        try {


            String queryString;
            float extraRentalFee = 0.00f;
            float rentalFeeAccrued = 0.00f;
            int quantity = 0;
            String whereClause = "WHERE BookID = :bookId AND UserID = :userId AND TransactionType = :transactionType";
            //System.out.println("yes here");

            //UPDATE RETURNED RENTED BOOK PURCHASE RECORD ONLY AND REJECT IF THE RETURNING BOOK IS PURCHASED

            queryString = "SELECT RentalStartDate,RentalFeeAccrued,Quantity,TransactionType FROM Books_Purchased " + whereClause; //b.RentalStartDate,b.RentalFeeAccrued
            Query query = entityManager.createNativeQuery(queryString);
            query.setParameter("bookId", bookId);
            query.setParameter("userId", userId);
            query.setParameter("transactionType", "Rented");
            List<Object[]> resultSet = query.getResultList();
            for (Object[] res : resultSet) {
                //System.out.println("here " + res[3]);
                if(res[3].equals("Purchased")){
                    throw new BadRequestException("The book cannot be returned as it is purchased and not rented.");
                }
                rentalStartDate = (Date) res[0];
                rentalFeeAccrued = ((Number) res[1]).floatValue();
                quantity = (int) (res[2]);
            }
            //System.out.println("Rental Start date: " + rentalStartDate + " extraRentalFee: " + extraRentalFee);

            long datediff = ChronoUnit.DAYS.between(rentalStartDate.toLocalDate(), today.toLocalDate());
            System.out.println("Period: " + datediff);
            if (datediff > 30) {
                extraRentalFee = rentalFeeAccrued + datediff - 30;
            }

            //System.out.println("extraRentalFee: " + extraRentalFee);

            queryString = "UPDATE Books_Purchased  SET RentalEndDate = :rentalEndDate, RentalFeeAccrued = :rentalFeeAccrued " + whereClause;
            query = entityManager.createNativeQuery(queryString);
            query.setParameter("rentalEndDate", rentalEndDate);
            query.setParameter("rentalFeeAccrued", extraRentalFee);
            query.setParameter("bookId", bookId);
            query.setParameter("userId", userId);
            query.setParameter("transactionType", "Rented");
            query.executeUpdate();

            //UPDATE BOOK COPIES RECORD
            int noOfCopies = 0;
            queryString = "SELECT NoOfCopies FROM Book_Record WHERE ID = :bookId ";
            query = entityManager.createNativeQuery(queryString);
            query.setParameter("bookId", bookId);
            System.out.println("yes here: ");
            List<Object> resultSet1 = query.getResultList();
            for (Object res : resultSet1) {
                noOfCopies = (int) res;
            }
            System.out.println("no of copies: " + noOfCopies);
            queryString = "UPDATE Book_Record  SET NoOfCopies = :noOfCopies WHERE ID = :bookId";
            query = entityManager.createNativeQuery(queryString);
            query.setParameter("bookId", bookId);
            query.setParameter("noOfCopies", quantity + noOfCopies);
            query.executeUpdate();
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