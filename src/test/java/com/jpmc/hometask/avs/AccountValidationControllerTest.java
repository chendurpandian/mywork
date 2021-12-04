package com.jpmc.hometask.avs;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.hometask.avs.Account.AccountReq;
import com.jpmc.hometask.avs.Account.AccountValidationController;
import com.jpmc.hometask.avs.Account.AccountValidationService;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountValidationControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AccountValidationService accountValidationService;
	
	@Before
    public void setup() {

        //notice here I'm setting the mocked dao here
        // if you didn't use @RunWith(MockitoJUnitRunner.class)
        // you can do: simpleCategoryDAO = Mockito.mock(SimpleCategoryDAO.class);
        mockMvc = MockMvcBuilders.standaloneSetup(AccountValidationController.class).build();
    }
	
	@Test
	public void validateAcct() throws Exception{
		
		 
		AccountReq accReq = new AccountReq();
		accReq.setAccountNumber("12345678");
		List<String> providersList = new ArrayList<String>(); 
		providersList.add("provider1");
		providersList.add("provider2");
		accReq.setProviders(providersList);
		
		String json = new ObjectMapper().writeValueAsString(accReq);
		//Mockito.doReturn(accReq).when(accountValidationService.validateAccountByAccNum(accReq));
		this.mockMvc.perform(MockMvcRequestBuilders.post("/account/v1/validate")
		            .contentType(MediaType.APPLICATION_JSON)
		            .content(json)
		            .accept(MediaType.APPLICATION_JSON))
		            .andExpect(status().isOk());
		
	}
}
