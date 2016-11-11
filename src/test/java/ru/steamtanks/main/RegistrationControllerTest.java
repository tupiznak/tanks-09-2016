package ru.steamtanks.main;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.steamtanks.exceptions.AccountService.ASSomeDatabaseException;

import static org.junit.Assert.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.steamtanks.services.implementation.AccountService.getTableUsers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@Transactional
public class RegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate template;

    @Test
    public void addOneUser() throws Exception {
        final JSONObject request = new JSONObject();
        request.put("login", "verytest");
        request.put("password", "very@email.com");
        request.put("email", "veryvery");

        final Integer beforeCountRows = countRowsInTable(template, getTableUsers());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .header("Content-Type", "application/json")
                .content(request.toString()))
                .andExpect(status().isOk());

        assertEquals(beforeCountRows + 1, countRowsInTable(template, getTableUsers()));
    }

    @Test
    public void addUserCoockie() throws Exception {
        final JSONObject request = new JSONObject();
        request.put("login", "verytest");
        request.put("password", "very@email.com");
        request.put("email", "veryvery");

        final Integer beforeCountRows = countRowsInTable(template, getTableUsers());
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .header("Content-Type", "application/json")
                .content(request.toString()))
                .andExpect(status().isOk())
                .andReturn();

        try {
            final Integer id = template.queryForObject(
                    "select id from " + getTableUsers() + " where login=?",
                    Integer.class, request.getString("login")
            );
            assertEquals(result.getResponse().getHeader("Set-cookie"), "id=" + id);
        } catch (DataAccessException e) {
            e.getStackTrace();
            throw new ASSomeDatabaseException("Exception in add user", e);
        }

        assertEquals(beforeCountRows + 1, countRowsInTable(template, getTableUsers()));
    }

    @Test
    public void addTwoUsers() throws Exception {
        final JSONObject request = new JSONObject();
        request.put("login", "verytest");
        request.put("password", "very@email.com");
        request.put("email", "veryvery");

        final Integer beforeCountRows = countRowsInTable(template, getTableUsers());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .header("Content-Type", "application/json")
                .content(request.toString()))
                .andExpect(status().isOk());
        assertEquals(beforeCountRows + 1, countRowsInTable(template, getTableUsers()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .header("Content-Type", "application/json")
                .content(request.toString()))
                .andExpect(status().isForbidden());
        assertEquals(beforeCountRows + 1, countRowsInTable(template, getTableUsers()));
    }

/*
    @Test
    public void getUserNormal() throws Exception {
        final JSONObject request = new JSONObject();
        request.put("login", "verytest");
        request.put("password", "very@email.com");
        request.put("email", "veryvery");

        final Integer beforeCountRows = countRowsInTable(template, getTableUsers());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .header("Content-Type","application/json")
//                .session(mockHttpSession)
                .content(request.toString()))
                .andExpect(status().isOk())
                //.andDo(MockMvcResultHandlers.print())
                .andReturn();
        assertEquals(beforeCountRows+1,  countRowsInTable(template, getTableUsers()));

        Integer id;
        try {
             id = template.queryForObject(
                    "select id from "+getTableUsers()+" where login=?",
                    Integer.class,request.getString("login")
            );
            assertEquals(result.getResponse().getHeader("Set-cookie"),"id="+id);
        }
        catch (Exception e){
            throw new ASDetectUserException();
        }

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user")
                .header("Content-Type","application/json")
                .content(id.toString()))
                .andExpect(status().isForbidden());
        assertEquals(beforeCountRows+1,  countRowsInTable(template, getTableUsers()));
    }
*/
//session
/*

    @Test
    public void getSession() throws Exception {
        final JSONObject request = new JSONObject();
        request.put("login", "verytest");
        request.put("password", "very@email.com");
        request.put("email", "veryvery");

        MockHttpSession mockHttpSession = new MockHttpSession();

        final Integer beforeCountRows = countRowsInTable(template, getTableUsers());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .header("Content-Type","application/json")
                .session(mockHttpSession)
                .content(request.toString()))
                .andExpect(status().isOk())
                //.andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println(mockHttpSession.getAttribute("iduser"));
        String id = mockHttpSession.getAttribute("iduser").toString();

        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/session")
                .header("Content-Type","application/json")
                .session(mockHttpSession)
                .content(request.toString()))
                .andExpect(status().isOk())
                //.andDo(MockMvcResultHandlers.print())
                .andReturn();





        assertEquals(beforeCountRows+1,  countRowsInTable(template, getTableUsers()));
    }
*/

}