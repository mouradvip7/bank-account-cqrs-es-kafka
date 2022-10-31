package com.dio.account.cmd.api.controllers;

import com.dio.account.cmd.api.commands.DepositFundsCommand;
import com.dio.account.common.dto.BaseResponse;
import com.dio.cqrs.core.exceptions.AggregateNotFoundException;
import com.dio.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/depositFunds")
public class DepositFundsController {
    private final Logger logger = Logger.getLogger(DepositFundsController.class.getName());
    private final CommandDispatcher dispatcher;


    public DepositFundsController(CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
    @PutMapping(path = "/{id}")
    public ResponseEntity<BaseResponse> depositFunds(@PathVariable(value = "id") String id, @RequestBody DepositFundsCommand command){
        try{
            command.setId(id);
            dispatcher.send(command);
            return new ResponseEntity<>(new BaseResponse("Deposit Funds request completed successfully"), HttpStatus.OK);
        } catch (IllegalStateException | AggregateNotFoundException e){
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad Request - {0}", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            var safeErrorMessage = MessageFormat.format("Error while processing request to deposit Funds to bank account with id - {0}.", id);
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
