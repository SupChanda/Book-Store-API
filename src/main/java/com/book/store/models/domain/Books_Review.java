package com.book.store.models.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import javax.naming.Name;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name="Books_Review")
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldNameConstants
public class Books_Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "BookID", referencedColumnName = "ID", nullable = false)
    private Books books;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "ID", nullable = false)
    private User user;

    @Column(name = "DateReviewed")
    private Date dateReviewed;

    @Column(name = "Comments",columnDefinition = "nvarchar(max)")
    private String comments;

}
