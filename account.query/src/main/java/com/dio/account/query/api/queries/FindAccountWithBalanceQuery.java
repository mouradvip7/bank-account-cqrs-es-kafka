package com.dio.account.query.api.queries;

import com.dio.account.query.api.dto.EqualityType;
import com.dio.cqrs.core.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindAccountWithBalanceQuery  extends BaseQuery {
    private EqualityType equalityType;
    private double balance;
}
