package com.mebank.service;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.google.common.collect.Maps;
import com.mebank.bo.Transaction;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CsvParserServiceTest {

    private CsvParserService csvParserService;

    @Before
    public void setUp() throws ParseException {
        csvParserService = new CsvParserService();

    }

    @Test
    public void parseTransactions() throws IOException, ParseException {

        Reader reader = Files.newBufferedReader(Paths.get("transactions.csv"));
        CSVParser csvParser = new CSVParser(reader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

        HashMap<String, Object> inputsMap = getInputs();

        List<Transaction> transactions = csvParserService.parseTransactions(csvParser, inputsMap);
        assertEquals(5, transactions.size());

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
