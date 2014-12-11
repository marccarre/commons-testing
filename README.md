[![Build Status](https://travis-ci.org/marccarre/commons-testing.png?branch=master)](https://travis-ci.org/marccarre/commons-testing) [![Coverage Status](https://coveralls.io/repos/marccarre/commons-testing/badge.png)](https://coveralls.io/r/marccarre/commons-testing)

Utilities to assist with testing.

Features:
  - JUnit rule for log capture via SLF4J (with bindings for log4j, log4j2 and logback).
  - Hamcrest matcher for throwables.
  - Java 6 compatible.

Latest version (from either [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ccom.carmatechnologies.commons) or [OSS Sonatype](https://oss.sonatype.org/#nexus-search;quick~com.carmatechnologies.commons)):

    
    <dependency>
      <groupId>com.carmatechnologies.commons</groupId>
      <artifactId>commons-testing</artifactId>
      <version>1.0.1</version>
    </dependency>


*******************************************************************************
Example:


    import com.carmatechnologies.commons.testing.logging.ExpectedLogs;
    import com.carmatechnologies.commons.testing.logging.api.LogLevel;
    import org.junit.Rule;
    import org.junit.Test;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import static org.hamcrest.CoreMatchers.is;
    import static org.junit.Assert.assertThat;

    public class ATMTest {
        @Rule
        public final ExpectedLogs logs = new ExpectedLogs() {{
            captureFor(ATM.class, LogLevel.WARN);
        }};

        @Test
        public void atmShouldLogWarningIfRequestedAmountIsGreaterThanCurrentBalance() {
            // Nothing captured yet:
            assertThat(logs.isEmpty(), is(true));
            assertThat(logs.size(), is(0));

            // Logic using the class you are capturing logs for:
            ATM model = new ATM(90);
            assertThat(model.withdraw(100), is(90));

            // Assert content of the logs:
            assertThat(logs.isEmpty(), is(false));
            assertThat(logs.size(), is(1));
            assertThat(logs.contains("Only withdrew 90. Remaining balance: 0."), is(true));
        }

        private static class ATM {
            private static Logger logger = LoggerFactory.getLogger(ATM.class);
            private int balance;

            public ATM(final int balance) {
                this.balance = balance;
            }

            public int withdraw(final int amount) {
                logger.debug("Amount to withdraw: " + amount);

                if (amount <= 0) {
                    logger.error("Cannot withdraw zero or negative amount: " + amount);
                    return 0;
                }

                if (amount > balance) {
                    int initialBalance = balance;
                    balance = 0;
                    logger.warn("Only withdrew " + initialBalance + ". Remaining balance: " + balance + ".");
                    return initialBalance;
                }

                balance -= amount;
                logger.info("Withdrew " + amount + ". Remaining balance: " + balance + ".");
                return amount;
            }
        }
    }
