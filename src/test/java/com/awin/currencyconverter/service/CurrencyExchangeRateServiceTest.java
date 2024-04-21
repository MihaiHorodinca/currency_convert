package com.awin.currencyconverter.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.awin.currencyconverter.exceptions.CurrencyConversionException;
import com.awin.currencyconverter.exceptions.ExternalServiceException;
import com.awin.currencyconverter.exceptions.InvalidRequestException;

@SpringBootTest
@TestConfiguration
@ExtendWith(SpringExtension.class)
class CurrencyExchangeRateServiceTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private CurrencyExchangeRateService service;

    @Test
    void GIVEN_validEURtoUSDConversion_WHEN_conversionRequested_THEN_returnCorrectRate() {
        String response = "{\"success\":true, \"result\":80}";
        when(restTemplate.getForObject(any(String.class), eq(String.class))).thenReturn(response);

        double result = service.convert("EUR", "USD", 100);
        assertEquals(80, result);
    }

    @Test
    void GIVEN_invalidApiKey_WHEN_conversionRequested_THEN_throwInvalidRequestException() {
        String errorResponse = "{\"success\":false, \"error\":{\"info\":\"You have entered an invalid \\\"to\\\" property. [Example: to=GBP]\"}}";
        when(restTemplate.getForObject(any(String.class), eq(String.class))).thenReturn(errorResponse);

        Exception exception = assertThrows(InvalidRequestException.class, () -> {
            service.convert("EUR", "XXX", 100);
        });

        assertEquals("Failed to convert currency: You have entered an invalid \"to\" property. [Example: to=GBP]",
            exception.getMessage());
    }

    @Test
    void GIVEN_clientError_WHEN_conversionRequested_THEN_throwExternalServiceException() {
        when(restTemplate.getForObject(any(String.class), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));

        Exception exception = assertThrows(ExternalServiceException.class, () -> {
            service.convert("EUR", "USD", 100);
        });

        assertEquals("Service is currently unavailable. Please try again later.",
            exception.getMessage());
    }

    @Test
    void GIVEN_serverError_WHEN_conversionRequested_THEN_throwExternalServiceException() {
        when(restTemplate.getForObject(any(String.class), eq(String.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"));

        Exception exception = assertThrows(ExternalServiceException.class, () -> {
            service.convert("EUR", "USD", 100);
        });

        assertEquals("Service is currently unavailable. Please try again later.",
            exception.getMessage());    
    }

    @Test
    void GIVEN_invalidResponse_WHEN_conversionRequested_THEN_throwJSONException() {
        String response = "{invalidJSON";
        when(restTemplate.getForObject(any(String.class), eq(String.class))).thenReturn(response);

        Exception exception = assertThrows(CurrencyConversionException.class, () -> {
            service.convert("EUR", "USD", 100);
        });
        
        assertEquals("Failed to parse the server response.",
            exception.getMessage());
    }
}
