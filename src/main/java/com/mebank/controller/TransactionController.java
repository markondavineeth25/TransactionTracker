package com.mebank.controller;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.HashMap;
import com.mebank.service.TransactionService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TransactionController {

    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public HashMap<String, Object> listTransactions() throws IOException, ParseException {
        System.out.println( "Controller: list transactions" );

        Reader reader = Files.newBufferedReader(Paths.get("transactions.csv"));
        CSVParser csvParser = new CSVParser(reader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

        HashMap<String, Object> outputMap = transactionService.processTransactions(csvParser);

        return outputMap;
    }
}
