package com.jpmc.hometask.avs.Account;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AccountValidationService {
	
	@Value(AccountValidationConstants.PROPERTY_ENV_INSTANCE)
	private String envInstance;
	
	@Autowired
	private Environment env;
	
	@Value("${"+AccountValidationConstants.PROPERTY_RESOURCE_TYPE+"}")
	private String accoutResource;
	
	public AccountResp validateAccountByAccNum(AccountReq accReq) throws URISyntaxException, JsonProcessingException {
		AccountResp accResp = new AccountResp();
		String apiResp ="";
		Map<String , String> providerUrls = new HashMap<String , String>();
		Map<String, String> prvdrDomains = new HashMap<String , String>();
		ObjectMapper objMapper;
		//Provider Api Call
			if(null != accReq.getProviders() && !accReq.getProviders().isEmpty()) {
				//Request Body has Providers
				for(Object provider : accReq.getProviders()) {
					String appendUrl = AccountValidationConstants.PROPERTY_DOMAIN.concat(provider.toString());
					String finalUrl = env.getProperty(appendUrl);
					providerUrls.put(provider.toString(),finalUrl.concat(accoutResource));
				}
			}else {
				//Request Body has no Providers
				prvdrDomains = getAllProviderUrls(AccountValidationConstants.PROPERTY_DOMAIN);
				for(Map.Entry<String, String> prvdDmn : prvdrDomains.entrySet()) {
					providerUrls.put(getHostName(prvdDmn.getValue()), prvdDmn.getValue().concat(accoutResource));
				}
			}
			    
			//Calling the external api 
			Result res;
			List<Result> appendRes = new ArrayList<Result>();
			for(Map.Entry<String,String> apiUrl : providerUrls.entrySet()) {
				apiResp = isAccountValid(accReq.getAccountNumber(),apiUrl.getValue());
				objMapper = new ObjectMapper();
				JsonNode actualObj = objMapper.readTree(apiResp);
			    res = new Result();
				res.setProvider(apiUrl.getKey());
				res.setIsValid(actualObj.get(AccountValidationConstants.ACCT_IS_VALID).asBoolean());
				appendRes.add(res);
			}
			accResp.setResult(appendRes); 
		return accResp;
	}
	private String isAccountValid(String accountNum, String providerApiUrl) throws JsonProcessingException {
		
		String responseBody = "";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		//Set the account number as json object 
		String requestJson = new JSONObject()
				             .put(AccountValidationConstants.SERVICE_ACCOUNT_NUMBER, accountNum)
				             .toString();
	
		
		HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);
		// Commenting the rest call and sending the valid response
		/*
		 * ResponseEntity<String> result = restTemplate.postForEntity(providerApiUrl,
		 * requestEntity, String.class); if(result.getStatusCode().is2xxSuccessful()) {
		 * responseBody = result.getBody(); //Return the actual response }
		 */	
			if(accountNum.equals(AccountValidationConstants.VALID_ACCT))
				responseBody = "{ \"isValid\": true}";
			else
				responseBody = "{ \"isValid\": false}";
			
		return responseBody;
	}
	private Map<String, String> getAllProviderUrls(String prefix) {
		
		Map<String, String> properties = new HashMap<>();
		if (env instanceof ConfigurableEnvironment) {
		    for (PropertySource<?> propertySource : ((ConfigurableEnvironment) env).getPropertySources()) {
		        if (propertySource instanceof EnumerablePropertySource) {
		            for (String key : ((EnumerablePropertySource) propertySource).getPropertyNames()) {
		                if (key.startsWith(prefix)) {
		                    properties.put(key, propertySource.getProperty(key).toString());
		                }
		            }
		        }
		    }
		}
	 return properties;
	}
	public String getHostName(String url) throws URISyntaxException {
	    URI uri = new URI(url);
	    String hostname = uri.getHost();
	    if (hostname != null) {
	        return (hostname.startsWith("www.") ? hostname.substring(4) : hostname).split(".com")[0];
	    }
	    return hostname;
	}
}
