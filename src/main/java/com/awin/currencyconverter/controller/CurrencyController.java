package com.awin.currencyconverter.controller;

import com.awin.currencyconverter.service.CurrencyService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(final CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/")
    public String home() {

        return "Welcome to the Currency Converter!\n";
    }

    @GetMapping(value = "currencies/convert", produces = MediaType.APPLICATION_JSON_VALUE)
    public double convert(
            @RequestParam("source") String source,
            @RequestParam("target") String target,
            @RequestParam("amount") double amount) throws URISyntaxException, MalformedURLException, IOException, InterruptedException {

        return currencyService.convert(source, target, amount);
    }

}
