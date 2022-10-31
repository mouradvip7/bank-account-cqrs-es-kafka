package com.dio.account.query;

import com.dio.account.query.api.queries.*;
import com.dio.cqrs.core.infrastructure.QueryDispatcher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class AccountQueryApplication {
	private final QueryHandler handler;

	private final QueryDispatcher dispatcher;

	public AccountQueryApplication(AccountQueryHandler handler, QueryDispatcher dispatcher) {
		this.handler = handler;
		this.dispatcher = dispatcher;
	}

	public static void main(String[] args) {
		SpringApplication.run(AccountQueryApplication.class, args);
	}

	@PostConstruct
	public void registerHandlers(){
		dispatcher.registerHandler(FindAllAccountsQuery.class, handler::handle);
		dispatcher.registerHandler(FindAccountByIdQuery.class, handler::handle);
		dispatcher.registerHandler(FindAccountByHolderQuery.class, handler::handle);
		dispatcher.registerHandler(FindAccountWithBalanceQuery.class, handler::handle);
	}
}
