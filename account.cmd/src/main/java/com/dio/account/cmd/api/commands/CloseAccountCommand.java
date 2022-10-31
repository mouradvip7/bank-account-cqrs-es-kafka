package com.dio.account.cmd.api.commands;

import com.dio.cqrs.core.commands.BaseCommand;
import lombok.Data;

public class CloseAccountCommand extends BaseCommand {
    public CloseAccountCommand(String id){
        super(id);
    }
}
