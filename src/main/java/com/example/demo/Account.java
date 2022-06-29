package com.example.demo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Accessors(chain = true)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

     private String accountName;
     private Double balance;
     private LocalDateTime createdDate;
     private String city = "Atlanta";
     @OneToMany(mappedBy = "account")
     private List<Transaction> transactions;
}
