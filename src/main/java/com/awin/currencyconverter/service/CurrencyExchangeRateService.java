package com.awin.currencyconverter.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.json.JSONObject;

import org.springframework.stereotype.Service;

/**
 * TODO: Implementation of this class has to be backed by https://api.exchangerate.host/latest?base=EUR&symbols=AUD,CAD,CHF,CNY,GBP,JPY,USD
 */
@Service
public class CurrencyExchangeRateService implements CurrencyService {

    private static final String BASE_URL = "http://api.exchangerate.host/live";
    private static final String ACCESS_KEY = "e18cd2ec127a6b6417888c5f7d00d9f8";

    @Override
    public double convert(String source, String target, double amount) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String finalUrl = String.format("%s?access_key=%s&from=%s&to=%s&amount=%f", BASE_URL, ACCESS_KEY, source, target, amount);

        HttpRequest request = HttpRequest.newBuilder().uri(new URI(finalUrl)).build();
        HttpResponse<String> response = client.send(request,BodyHandlers.ofString());

        JSONObject obj = new JSONObject(response.body());
        JSONObject quotes = obj.getJSONObject("quotes");
        double rate = quotes.getDouble(source + target);


        return rate * amount;
    }
}
