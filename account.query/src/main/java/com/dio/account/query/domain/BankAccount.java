package com.dio.account.query.domain;

import com.dio.account.common.dto.AccountType;
import com.dio.cqrs.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount extends BaseEntity {
    @Id
    private String id;

    private String accountHolder;
    private Date creationDate;
    private AccountType accountType;
    private double balance;

}
