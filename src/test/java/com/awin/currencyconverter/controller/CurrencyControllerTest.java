package com.awin.currencyconverter.controller;

import org.assertj.core.matcher.AssertionMatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static java.lang.Double.parseDouble;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void GIVEN_rootPath_WHEN_requestIsMade_THEN_returnWelcomeMessage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to the Currency Converter!"));
    }

    @Test
    public void GIVEN_validEURtoUSDConversion_WHEN_requestIsMade_THEN_rateIsGreaterThanOne() throws Exception {
        mockMvc.perform(get("/currencies/convert?source=EUR&target=USD&amount=1"))
                .andExpect(status().isOk())
                .andExpect(content().string(new AssertionMatcher<>() {
                    @Override
                    public void assertion(String value) throws AssertionError {
                        assertThat(parseDouble(value)).isGreaterThan(1.0);
                    }
                }));
    }

    @Test
    public void GIVEN_validUSDToEURConversion_WHEN_requestIsMade_THEN_rateIsLessThanOne() throws Exception {
        mockMvc.perform(get("/currencies/convert?source=USD&target=EUR&amount=1"))
                .andExpect(status().isOk())
                .andExpect(content().string(new AssertionMatcher<>() {
                    @Override
                    public void assertion(String value) throws AssertionError {
                        assertThat(parseDouble(value)).isLessThan(1.0);
                    }
                }));
    }

    @Test
    public void GIVEN_missingSourceParameter_WHEN_requestIsMade_THEN_returnBadRequest() throws Exception {
        mockMvc.perform(get("/currencies/convert?target=USD&amount=1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing request parameter: source"));
    }

    @Test
    public void GIVEN_missingTargetParameter_WHEN_requestIsMade_THEN_returnBadRequest() throws Exception {
        mockMvc.perform(get("/currencies/convert?source=USD&amount=1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing request parameter: target"));
    }

    @Test
    public void GIVEN_missingAmountParameter_WHEN_requestIsMade_THEN_returnBadRequest() throws Exception {
        mockMvc.perform(get("/currencies/convert?source=USD&target=EUR"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing request parameter: amount"));
    }

    @Test
    public void GIVEN_invalidSource_WHEN_requestIsMade_THEN_returnBadRequestWithInvalidSourceMessage() throws Exception {
        mockMvc.perform(get("/currencies/convert?source=INVALID&target=USD&amount=1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to convert currency: You have entered an invalid \"from\" property. [Example: from=EUR]"));
    }

    @Test
    public void GIVEN_invalidTarget_WHEN_requestIsMade_THEN_returnBadRequestWithInvalidTargetMessage() throws Exception {
        mockMvc.perform(get("/currencies/convert?source=USD&target=INVALID&amount=1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to convert currency: You have entered an invalid \"to\" property. [Example: to=GBP]"));
    }

    @Test
    public void GIVEN_invalidAmount_WHEN_requestIsMade_THEN_returnBadRequestWithInvalidAmountMessage() throws Exception {
        mockMvc.perform(get("/currencies/convert?source=USD&target=EUR&amount=-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to convert currency: You have not specified an amount to be converted. [Example: amount=5]"));
    }

}
