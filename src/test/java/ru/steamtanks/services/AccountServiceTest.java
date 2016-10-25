package ru.steamtanks.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@Transactional
public class AccountServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate template;
    @Test
    public void addUser() throws Exception {
        //UserProfile userProfile = new UserProfile("verytest","very@email.com","veryvery");

        final Integer beforeCountRows = countRowsInTable(template, "users");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .header("Content-Type","application/json")/*
                .param("email", "lala@la.la")
                .param("password", "lolo")*/
                .content("{\"login\": \"hehe\"}"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        assertEquals(beforeCountRows+1,  countRowsInTable(template, "users"));
    }

}