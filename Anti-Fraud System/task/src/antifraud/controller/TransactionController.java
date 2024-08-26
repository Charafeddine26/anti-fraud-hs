package antifraud.controller;


import antifraud.domain.dto.ResultDTO;
import antifraud.domain.dto.TransactionDTO;
import antifraud.domain.enums.TransactionStatus;
import antifraud.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/antifraud/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, "application/json;charset=UTF-8"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultDTO> createTransaction(@Valid @RequestBody TransactionDTO transaction) {
            try {
                ResultDTO result = transactionService.createTransaction(transaction);
                return ResponseEntity.ok(result);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            } catch (RuntimeException e) {
                return ResponseEntity.status(409).build();
            }



    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
