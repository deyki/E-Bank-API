package com.EBank.EBankApplication.controller;

import com.EBank.EBankApplication.model.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public ResponseEntity<ResponseMessage> home() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseMessage("Authenticated!"));
    }
}
