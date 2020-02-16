package com.mebank.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mebank.bo.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    private List<Transaction> transactions;

    @Mock
    private CsvParserService csvParserServiceMock;

    @InjectMocks
    private TransactionService transactionService;

    @Before
    public void setUp() throws ParseException {
        System.out.println("set up method");

        formTransactions();

    }

    private List<Transaction> formTransactions() throws ParseException {

        this.transactions = Lists.newArrayList();
        Transaction t1 = new Transaction();
        t1.setTransactionId("TX10001");
        t1.setFromAccountId("ACC334455");
        t1.setToAccountId("ACC778899");
        Date createdAt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .parse("20/10/2018 12:47:55");
        t1.setCreatedAt(createdAt);
        t1.setAmount(25.0);
        t1.setTransactionType("PAYMENT");

        Transaction t2 = new Transaction();
        t2.setTransactionId("TX10002");
        t2.setFromAccountId("ACC334455");
        t2.setToAccountId("ACC998877");
        Date createdAt2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .parse("20/10/2018 17:33:43");
        t2.setCreatedAt(createdAt2);
        t2.setAmount(10.5);
        t2.setTransactionType("PAYMENT");

        Transaction t3 = new Transaction();
        t3.setTransactionId("TX10003");
        t3.setFromAccountId("ACC998877");
        t3.setToAccountId("ACC778899");
        Date createdAt3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .parse("20/10/2018 18:00:00");
        t3.setCreatedAt(createdAt3);
        t3.setAmount(5.0);
        t3.setTransactionType("PAYMENT");

        this.transactions.add(t1);
        this.transactions.add(t2);
        this.transactions.add(t3);

        return this.transactions;
    }

    @Test
    public void calculateRelativeBalance() throws ParseException {
        HashMap<String, Object> inputsMap = getInputs();
        transactionService.calculateRelativeBalance(transactions, inputsMap);
    }

    @Test
    public void getReversalTransactions() {
        transactionService.getReversalTransactions(this.transactions, Lists.newArrayList());
    }

    @Test
    public void getTransactionIds() {
        List<String> transactionIds = transactionService.getTransactionIds(this.transactions);
        assertEquals(3, transactionIds.size());
    }

    @Test
    public void getSumOfTransactions() {
        System.out.println("sum of transactions");
        Double sumOfTransactions = transactionService.getSumOfTransactions(transactions);
        assertEquals("40.5", String.valueOf(sumOfTransactions));
    }

    @Test
    public void filterTransactionsBasedOnInputs() throws ParseException {

        HashMap<String, Object> inputsMap = getInputs();

        List<Transaction> transactionsFiltered = transactionService
                .filterTransactionsBasedOnInputs(this.transactions, inputsMap);

        assertEquals(2, transactionsFiltered.size());
    }

    private HashMap<String, Object> getInputs() throws ParseException {
        HashMap<String, Object> inputsMap = Maps.newHashMap();
        inputsMap.put("accountId","ACC334455");
        Date inputStartDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .parse("20/10/2018 12:00:00");
        inputsMap.put("inputStartDate",inputStartDate);

        Date inputEndDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .parse("20/10/2018 17:34:43");
        inputsMap.put("inputEndDate",inputEndDate);
        return inputsMap;
    }

}