# TransactionTracker
========================================

The objective of this application is to track all the transactions in a given period of time and then calculate the relative balance of those transactions. 

It takes a csv file as an input, which has transactions.  

## System Requirements
To use this project you will require the following technology installed on your machine:
- Apache Maven
- JDK8

## Third party libraries  
- Apache commons csv: This is used to read csv file and process it, so as to convert records from csv to java objects.  
- Google guava: This is a general purpose library, designed to make day to day coding easier. It has quite an extensive handy features. 
- Mockito: Testing framework, used to mock objects. 

## Inputs 
The application takes 3 inputs to filter out transactions in csv file.
- Account Number
- Transaction Start date
- Transaction End date 

## Configuration and Running the application

This application is designed to run as a stand alone application. 
This means import the project into an IDE and build and run the main method. 

To run the application, run the App.java  
This implements ApplicationRunner, hence making it easy to run on its own. 

# Important Technical Components
This is a spring boot application.  
Here spring boot is used only for start up so as to invoke the Controller class.  
From controller class the actual flow starts. 

TransactionController
----------------------
It reads the csv file. 
It wraps the file inside a csvParser object, which is passed onto the TransactionService.

Service classes
----------------
**TransactionService** - Parses the cvs file with the help of CsvParserService.  
Initializes the inputs.  
Filters transactions based on these inputs.  
Calculate all payment transactions.   
Then ignore reversal transactions.   
Calculate relative balance.   

**CsvParserService** - Service which takes csv parser object then converts them to list of Transaction objects.
