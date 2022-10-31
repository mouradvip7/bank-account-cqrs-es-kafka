package com.dio.account.cmd.api.commands;

import com.dio.account.common.dto.AccountType;
import com.dio.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class OpenAccountCommand extends BaseCommand {
    private String accountHolder;
    private AccountType accountType;
    private double openingBalance;
}
