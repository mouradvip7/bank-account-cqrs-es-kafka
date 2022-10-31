package com.dio.account.cmd.api.controllers;

import com.dio.account.cmd.api.commands.RestoreReadDbCommand;
import com.dio.account.common.dto.BaseResponse;
import com.dio.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/restoreReadDb")
public class RestoreReadDbController {
    private final Logger logger = Logger.getLogger(RestoreReadDbController.class.getName());

    private final CommandDispatcher dispatcher;

    public RestoreReadDbController(CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @PostMapping
    public ResponseEntity<BaseResponse> restoreReadDb() {

        try {
            dispatcher.send(new RestoreReadDbCommand());
            return new ResponseEntity<>(new BaseResponse("Read Db restore request completed successfully"), HttpStatus.OK);
        } catch (IllegalStateException e){
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad Request - {0}", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            var safeErrorMessage = "Error while processing request to restore read db.";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
