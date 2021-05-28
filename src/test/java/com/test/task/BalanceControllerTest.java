package com.test.task;

import com.test.task.entity.Balance;
import com.test.task.repository.BalanceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class BalanceControllerTest {

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    MockMvc mockMvc;

    @Test
    void whenGetExistingBalance_noErrors() throws Exception {
        createTestBalance(1, 5.0);

        mockMvc.perform(get("/v1/balance/get-balance/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.clientId", is(1)))
                .andExpect(jsonPath("$.balance", is(5.0)));

        balanceRepository.deleteAll();
    }

    @Test
    void whenGetNonExistingBalance_notFound() throws Exception {

        mockMvc.perform(get("/v1/balance/get-balance/5"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenAddingBalanceToNonExistingClient_noErrors() throws Exception {

        mockMvc.perform(put("/v1/balance/add-balance/1/5"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.clientId", is(1)))
                .andExpect(jsonPath("$.balance", is(5.0)));

        balanceRepository.deleteAll();
    }

    @Test
    void whenAddingBalanceToExistingClient_noErrors() throws Exception {
        createTestBalance(1, 5.0);

        mockMvc.perform(put("/v1/balance/add-balance/1/5"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.clientId", is(1)))
                .andExpect(jsonPath("$.balance", is(10.0)));

        balanceRepository.deleteAll();
    }

    @Test
    void whenChargingExistingClient_noErrors() throws Exception {
        createTestBalance(1, 5.0);

        mockMvc.perform(patch("/v1/balance/charge-balance/1/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.clientId", is(1)))
                .andExpect(jsonPath("$.balance", is(3.0)));

        balanceRepository.deleteAll();
    }

    @Test
    void whenChargingExistingClientWithLowBalance_444expected() throws Exception {
        createTestBalance(1, 5.0);

        mockMvc.perform(patch("/v1/balance/charge-balance/1/10"))
                .andExpect(status().is(444));

        balanceRepository.deleteAll();
    }

    @Test
    void whenChargingNonExistingClientWith_notFound() throws Exception {

        mockMvc.perform(patch("/v1/balance/charge-balance/1/10"))
                .andExpect(status().isNotFound());
    }

    private void createTestBalance(Integer clientId, Double amount) {
        Balance balance = new Balance();
        balance.setBalance(amount);
        balance.setClientId(clientId);
        balanceRepository.save(balance);
    }

}
