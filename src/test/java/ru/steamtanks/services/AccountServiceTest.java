package ru.steamtanks.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.steamtanks.models.UserProfile;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.steamtanks.services.AccountService.getTableUsers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@Transactional
public class AccountServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private  JdbcTemplate template;
    @Test
    public void addOneUser() throws Exception {
        UserProfile userProfile = new UserProfile("verytest","very@email.com","veryvery");
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(userProfile);

        final Integer beforeCountRows = countRowsInTable(template, getTableUsers());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .header("Content-Type","application/json")
                .content(jsonInString))
                .andExpect(status().isOk());

        assertEquals(beforeCountRows+1,  countRowsInTable(template, getTableUsers()));
    }
    @Test
    public void addUserCoockie() throws Exception {
        UserProfile userProfile = new UserProfile("verytest","very@email.com","veryvery");
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(userProfile);

        //MockHttpSession mockHttpSession = new MockHttpSession();

        final Integer beforeCountRows = countRowsInTable(template, getTableUsers());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .header("Content-Type","application/json")
//                .session(mockHttpSession)
                .content(jsonInString))
                .andExpect(status().isOk())
                //.andDo(MockMvcResultHandlers.print())
                .andReturn();

        //System.out.println(mockHttpSession.getAttribute("iduser"));

        try {
            Integer id = template.queryForObject(
                    "select id from "+getTableUsers()+" where login=?",
                    Integer.class,userProfile.getLogin()
            );
            assertEquals(result.getResponse().getHeader("Set-cookie"),"id="+id);
        }
        catch (Exception e){
            System.out.println("SQLerror");
        }

        assertEquals(beforeCountRows+1,  countRowsInTable(template, getTableUsers()));
    }

    @Test
    public void addTwoUsers() throws Exception {
        UserProfile userProfile = new UserProfile("verytest","very@email.com","veryvery");
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(userProfile);

        final Integer beforeCountRows = countRowsInTable(template, getTableUsers());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .header("Content-Type","application/json")
                .content(jsonInString))
                .andExpect(status().isOk());
        assertEquals(beforeCountRows+1,  countRowsInTable(template, getTableUsers()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .header("Content-Type","application/json")
                .content(jsonInString))
                .andExpect(status().isForbidden());
        assertEquals(beforeCountRows+1,  countRowsInTable(template, getTableUsers()));
    }
}