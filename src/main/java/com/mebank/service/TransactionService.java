package com.mebank.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import com.google.common.collect.Maps;
import com.mebank.bo.Transaction;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private CsvParserService csvParserService;

    @Autowired
    public TransactionService(CsvParserService csvParserService) {
        this.csvParserService = csvParserService;
    }

    public HashMap<String, Object> processTransactions(CSVParser csvParser) throws ParseException {
        HashMap<String, Object> inputsMap = initializeInputs();

        ArrayList<Transaction> transactions = csvParserService.parseTransactions(csvParser, inputsMap);

        HashMap<String, Object> outputMap = calculateRelativeBalance(transactions, inputsMap);

        return outputMap;
    }

    private HashMap<String, Object> initializeInputs() throws ParseException {

        HashMap<String, Object> inputsMap = Maps.newHashMap();

        // Inputs entered
        String accountId = "ACC334455";
        Date inputStartDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .parse("20/10/2018 12:00:00");
        Date inputEndDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .parse("20/10/2018 19:00:00");

        inputsMap.put("accountId", accountId);
        inputsMap.put("inputStartDate", inputStartDate);
        inputsMap.put("inputEndDate", inputEndDate);

        return inputsMap;
    }

    public HashMap<String, Object> calculateRelativeBalance(List<Transaction> transactions, HashMap<String, Object> inputsMap) throws ParseException {

        HashMap<String, Object> outputMap = Maps.newHashMap();

        // Payment transactions after filtering
        List<Transaction> transactions_filtered = filterTransactionsBasedOnInputs(transactions, inputsMap);

        Double payment_amount = getSumOfTransactions(transactions_filtered);

        List<String> transaction_ids_filtered = getTransactionIds(transactions_filtered);

        // Reversal Transactions
        List<Transaction> transactions_reversal = getReversalTransactions(transactions, transaction_ids_filtered);

        Double reversal_amount = getSumOfTransactions(transactions_reversal);

        int total_transactions = transactions_filtered.size() - transactions_reversal.size();

        // Output
        System.out.println("Relative balance for the period is: : " + (reversal_amount - payment_amount));
        System.out.println("Number of transactions included is: " + total_transactions);

        outputMap.put("relativeBalance", (reversal_amount - payment_amount) );
        outputMap.put("totalTransactions", total_transactions);

        return outputMap;
    }

    public List<Transaction> getReversalTransactions(List<Transaction> transactions, List<String> transaction_ids_filtered) {
        return transactions.stream()
                    .filter(t-> t.getTransactionType().equals("REVERSAL"))
                    .filter(t -> transaction_ids_filtered.contains(t.getRelatedTransaction()))
                    .collect(Collectors.toList());
    }

    public List<String> getTransactionIds(List<Transaction> transactions_filtered) {
        return transactions_filtered.stream()
                .map(t -> t.getTransactionId())
                .collect(Collectors.toList());
    }

    /**
     * For a given transaction,
     * @param transactions_filtered
     * @return
     */
    public Double getSumOfTransactions(List<Transaction> transactions_filtered) {
        return transactions_filtered.stream()
                .collect(Collectors.summingDouble(t -> t.getAmount()));
    }

    /**
     * Filter and get only those payment transactions based on inputs given
     * @param transactions
     * @param inputsMap
     * @return
     */
    public List<Transaction> filterTransactionsBasedOnInputs(List<Transaction> transactions, HashMap<String, Object> inputsMap) {
        return transactions.stream()
                    .filter(t -> t.getFromAccountId().equals(inputsMap.get("accountId")))
                    .filter(t -> t.getTransactionType().equals("PAYMENT"))
                    .filter(t -> t.getCreatedAt().compareTo((Date) inputsMap.get("inputStartDate")) >= 0
                            && t.getCreatedAt().compareTo((Date) inputsMap.get("inputEndDate")) <= 0)
                    .collect(Collectors.toList());
    }
}
