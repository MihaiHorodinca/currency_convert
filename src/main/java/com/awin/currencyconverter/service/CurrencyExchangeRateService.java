package com.awin.currencyconverter.service;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.awin.currencyconverter.exceptions.CurrencyConversionException;
import com.awin.currencyconverter.exceptions.ExternalServiceException;
import com.awin.currencyconverter.exceptions.InvalidRequestException;

import org.springframework.web.client.HttpServerErrorException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CurrencyExchangeRateService implements CurrencyService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyExchangeRateService.class);
    
    @Value("${currency.api.baseurl}")
    private String BASE_URL;

    @Value("${currency.api.accesskey}")
    private String ACCESS_KEY;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public double convert(String source, String target, double amount) {
        String finalUrl = String.format("%s?access_key=%s&from=%s&to=%s&amount=%f", BASE_URL, ACCESS_KEY, source, target, amount);
        logger.info("Requesting currency conversion from " + source + " to " + target + " for amount " + amount);
        try {
            String response = restTemplate.getForObject(finalUrl, String.class);
            JSONObject obj = new JSONObject(response);

            if (!obj.getBoolean("success")) {
                logger.error("Error occurred while requesting currency conversion: " + obj.getJSONObject("error").toString());
                throw new InvalidRequestException("Failed to convert currency: " + obj.getJSONObject("error").getString("info"));
            }
            logger.info("Successfully converted currency.");
            return obj.getDouble("result");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("HTTP error occurred while requesting currency conversion: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new ExternalServiceException("Service is currently unavailable. Please try again later.", e);
        } catch (JSONException e) {
            logger.error("Error parsing the JSON response: ", e);
            throw new CurrencyConversionException("Failed to parse the server response.");
        } catch (InvalidRequestException e) {
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred: ", e);
            throw new RuntimeException("An unexpected error occurred. Please contact support.");
        }
    }
}
