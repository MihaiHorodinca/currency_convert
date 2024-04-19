package com.awin.currencyconverter.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public interface CurrencyService {

    double convert(String source, String target, double amount) throws MalformedURLException, IOException, InterruptedException, URISyntaxException;

}
