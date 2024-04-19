<h3>Currency Converter (Coding Test - Software Engineer)</h3>

This exercise focuses on functionality, "clear code" and how well the application is tested.

Imagine you are part of the engineering team at AWIN which is responsible for Finance domain
(e.g. invoicing advertisers and paying publishers. For the sake of this exercise, let's assume that AWIN only operates
with Euro as a currency within the EU, we don't - but let's assume).

There are plans to expand into additional markets, not only in the EUR zone. For this, your team has received a request to
build a small helper app to perform currency exchange rate conversions. This is the starting point for adding currency
conversion functionality to the entire platform later on and allowing the business to expand into new regions.

Your task is to complete a concept for a currency conversion solution and to implement a small but key part of the
application including some tests.

For getting actual the conversion rates, assume you would use this service: https://exchangerate.host/.

To be able to use this service you need to [signup for a free account](https://exchangerate.host/signup/free)
and get your own api key from your [profile dashboard](https://exchangerate.host/dashboard).
A sample request would be: `http://api.exchangerate.host/live?access_key=<your_key>&source=EUR&symbols=AUD,CAD,CHF,CNY,GBP,JPY,USD`)

Tasks:
 * Implement 'CurrencyExchangeRateService' (please use this https://exchangerate.host as back-end service)
 * Write at least on test case (or as many as you think necessary)
 * Please introduce any change to any existing class if you think it improves the solution


Good luck!