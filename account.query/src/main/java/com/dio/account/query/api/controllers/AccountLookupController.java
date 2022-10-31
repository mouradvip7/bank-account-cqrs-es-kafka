package com.dio.account.query.api.controllers;

import com.dio.account.query.api.dto.AccountLookupResponse;
import com.dio.account.query.api.dto.EqualityType;
import com.dio.account.query.api.queries.FindAccountByHolderQuery;
import com.dio.account.query.api.queries.FindAccountByIdQuery;
import com.dio.account.query.api.queries.FindAccountWithBalanceQuery;
import com.dio.account.query.api.queries.FindAllAccountsQuery;
import com.dio.account.query.domain.BankAccount;
import com.dio.cqrs.core.infrastructure.QueryDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/bankAccountLookup")
public class AccountLookupController {
    private final Logger logger = Logger.getLogger(AccountLookupController.class.getName());

    private final QueryDispatcher dispatcher;

    public AccountLookupController(QueryDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @GetMapping(path = "/")
    public ResponseEntity<AccountLookupResponse> getAllAccounts() {
        try {
            List<BankAccount> accounts = dispatcher.send(new FindAllAccountsQuery());
            if (accounts == null || accounts.size() == 0)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message(MessageFormat.format("Successfully returned {0} bank account(s)", accounts.size()))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            var safeErrorMessage = "failed to complete get all accounts request";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(path = "/byId/{id}")
    public ResponseEntity<AccountLookupResponse> getAccountById(@PathVariable(value = "id" ) String id) {
        try {
            List<BankAccount> account = dispatcher.send(new FindAccountByIdQuery(id));
            if (account == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            var response = AccountLookupResponse.builder()
                    .accounts(account)
                    .message("Successfully returned bank account")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            var safeErrorMessage = "failed to complete get account By ID request";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/byHolder/{holder}")
    public ResponseEntity<AccountLookupResponse> getAccountByHolder(@PathVariable(value = "holder" ) String holder) {
        try {
            List<BankAccount> account = dispatcher.send(new FindAccountByHolderQuery(holder));
            if (account == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            var response = AccountLookupResponse.builder()
                    .accounts(account)
                    .message("Successfully returned bank account")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            var safeErrorMessage = "failed to complete get accounts by holder request";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/withBalance/{equalityType}/{balance}")
    public ResponseEntity<AccountLookupResponse> getAccountWithBalance(@PathVariable(value = "balance" ) double balance, @PathVariable(value = "equalityType") EqualityType equalityType) {
        try {
            List<BankAccount> accounts = dispatcher.send(new FindAccountWithBalanceQuery( equalityType, balance));
            if (accounts == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message(MessageFormat.format("Successfully returned {0} bank account", accounts.size()))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            var safeErrorMessage = "failed to complete get accounts by equality type request";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
