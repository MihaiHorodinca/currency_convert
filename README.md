# Currency Exchange Service

Tasks:
 * Implement 'CurrencyExchangeRateService' (please use this https://exchangerate.host as back-end service)
 * Write at least on test case (or as many as you think necessary)
 * Please introduce any change to any existing class if you think it improves the solution

## Running the service
You can start the service with `mvn spring-boot:run`.

Example request: `curl -G "localhost:8080/currencies/convert?source=USD&target=GBP&amount=1"`

The service will return the result of the conversion, or an exception in readable format if any arise.

**Example Response**:
```json
{
    "value": 0.75
}
```

You can also `curl -G "localhost:8080"` for a simple ping.

## Testing
Tests can be run with `mvn test`. They cover success and failure tests for the `CurrencyController` and the `CurrencyExchangeRateService`.