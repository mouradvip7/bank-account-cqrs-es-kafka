package com.dio.account.query.infrastructure.handlers;

import com.dio.account.common.events.AccountClosedEvent;
import com.dio.account.common.events.AccountOpenedEvent;
import com.dio.account.common.events.FundsDepositedEvent;
import com.dio.account.common.events.FundsWithdrawnEvent;

public interface EventHandler {
    void on(AccountOpenedEvent event);
    void on(FundsDepositedEvent event);
    void on(FundsWithdrawnEvent event);
    void on(AccountClosedEvent event);
}
