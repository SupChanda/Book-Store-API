package com.book.store.models.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name="BooksReview")
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldNameConstants
public class BooksReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "BookID", referencedColumnName = "ID", nullable = false)
    private Books books;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "ID", nullable = false)
    private BookUser user;

    @Column(name = "DateReviewed")
    private Date dateReviewed;

    @Column(name = "Comments",columnDefinition = "nvarchar(max)")
    private String comments;

}
