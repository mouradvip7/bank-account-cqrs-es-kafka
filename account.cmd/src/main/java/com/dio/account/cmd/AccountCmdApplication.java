package com.dio.account.cmd;

import com.dio.account.cmd.api.commands.*;
import com.dio.account.cmd.api.commands.handler.CommandHandler;
import com.dio.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class AccountCmdApplication {
	private final CommandDispatcher dispatcher;

	private final CommandHandler handler;

	public AccountCmdApplication(CommandDispatcher dispatcher, CommandHandler handler) {
		this.dispatcher = dispatcher;
		this.handler = handler;
	}

	public static void main(String[] args) {
		SpringApplication.run(AccountCmdApplication.class, args);
	}

	@PostConstruct
	public void registerHandlers(){
		dispatcher.registerHandler(OpenAccountCommand.class, handler::handle);
		dispatcher.registerHandler(DepositFundsCommand.class, handler::handle);
		dispatcher.registerHandler(WithdrawFundsCommand.class, handler::handle);
		dispatcher.registerHandler(CloseAccountCommand.class, handler::handle);
		dispatcher.registerHandler(RestoreReadDbCommand.class, handler::handle);

	}
}
