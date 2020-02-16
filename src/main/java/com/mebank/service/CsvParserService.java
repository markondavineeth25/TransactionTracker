package com.mebank.service;

import com.google.common.collect.Lists;
import com.mebank.bo.Transaction;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Service
public class CsvParserService {

    public CsvParserService() {
    }

    /**
     * Convert to List of Transactions from CSV Parser
     * CSV Parser is the one that holds all the records that are read from csv
     *
     * @param csvParser
     * @param inputsMap
     * @return
     * @throws ParseException
     */
    public ArrayList<Transaction> parseTransactions(CSVParser csvParser, HashMap<String, Object> inputsMap) throws ParseException {
        ArrayList<Transaction> transactions = Lists.newArrayList();
        Transaction transaction;

        for (CSVRecord csvRecord : csvParser) {

            transaction = new Transaction();

            // Accessing values by Header names
            String transactionId = csvRecord.get("transactionId");
            String fromAccountId = csvRecord.get("fromAccountId");
            String toAccountId = csvRecord.get("toAccountId");
            String createdAt = csvRecord.get("createdAt");
            String amount = csvRecord.get("amount");
            String transactionType = csvRecord.get("transactionType");
            String relatedTransaction = csvRecord.get("relatedTransaction");

            Date createdAtDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                    .parse(createdAt);
            double amountDouble = Double.parseDouble(amount);

            System.out.println("Record No - " + csvRecord.getRecordNumber());
            System.out.println("---------------");
            System.out.println("transactionId : " + transactionId);
            System.out.println("fromAccountId : " + fromAccountId);
            System.out.println("---------------\n\n");

            transaction.setTransactionId(transactionId);
            transaction.setFromAccountId(fromAccountId);
            transaction.setToAccountId(toAccountId);
            transaction.setCreatedAt(createdAtDate);
            transaction.setAmount(amountDouble);
            transaction.setRelatedTransaction(relatedTransaction);
            transaction.setTransactionType(transactionType);

                transactions.add(transaction);
        }

        return transactions;
    }
}
