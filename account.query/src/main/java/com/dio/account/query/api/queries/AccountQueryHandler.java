package com.dio.account.query.api.queries;

import com.dio.account.query.api.dto.EqualityType;
import com.dio.account.query.domain.AccountRepository;
import com.dio.account.query.domain.BankAccount;
import com.dio.cqrs.core.domain.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountQueryHandler implements QueryHandler{
    public final AccountRepository repository;

    public AccountQueryHandler(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<BaseEntity> handle(FindAllAccountsQuery query) {
        Iterable<BankAccount> bankAccounts = repository.findAll();
        List<BaseEntity> baseEntityList = new ArrayList<>();
        bankAccounts.forEach(baseEntityList::add);
        return baseEntityList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByIdQuery query) {
        var bankAccount = repository.findById(query.getId());
        if (bankAccount.isEmpty())
            return null;
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccount.get());
        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByHolderQuery query) {
        var bankAccount = repository.findByAccountHolder(query.accountHolder);
        if(bankAccount.isEmpty()){
            return null;
        }
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccount.get());
        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountWithBalanceQuery query) {
        return query.getEqualityType() == EqualityType.GREATER_THAN ? repository.findByBalanceGreaterThan(query.getBalance()) : repository.findByBalanceLessThan(query.getBalance());
    }
}
