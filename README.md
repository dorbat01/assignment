## Assignment

### How to run the assignment
1. bring up a command line and navigate to the codebase folder where the pom.xml is
2. mvn clean install
3. java -cp target/assignment-1.0.0-SNAPSHOT.jar au.com.livewirelabs.assignment.Assignment −exchange {either one of the stock codes NAB/CBA/QAN/CXA/ASX}

````
   java −cp target/assignment-1.0.0-SNAPSHOT.jar au.com.livewirelabs.assignment.Assignment −exchange ASX

   java −cp target/assignment-1.0.0-SNAPSHOT.jar au.com.livewirelabs.assignment.Assignment −exchange CXA
````

### Requirement
The trade() method should buy and sell a random number of units of the stock codes
(NAB,CBA,QAN), a random number of times on the exchange specified by the command
line arguments:
````
    java −cp target/assignment-1.0.0-SNAPSHOT.jar au.com.livewirelabs.assignment.Assignment −exchange ASX

    java −cp target/assignment-1.0.0-SNAPSHOT.jar au.com.livewirelabs.assignment.Assignment −exchange CXA
````

The CXA exchange charges 5 cents per trade. 
The ASX exchange charges 7 cents per trade.

### Features

- When you buy stock, the total available volume should go down
- When you sell stock, the total volume should go up.
- Attempts to buy stock when insufficient volume is available should result in throwing InsufficientUnitsException warning and will skip the current transaction and continue to process the rest of transactions..
- When the stock code is not pre-defined ie NAB, CBA, QAN, CXA or ASX, InvalidCodeException will be thrown and stop to process the transactions for that particular stock code.
- After the trading activity is complete, report the remaining volume and the income of the exchange

### Tech Debt/Outstanding
- the valid/pre-defined stock codes should be implemented as stockMaster DAO
- The rates currently is hard-coded.  It should be implemented as the DAO
- The application should maintain its state across multiple executions (i.e. the volume remaining after each run should be available at the start of the next run. (running out of time to implement)
- All trading activity (buys & sells) is recorded in the history DAO