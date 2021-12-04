package com.jpmc.hometask.avs.Account;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "accountReq")
public class AccountReq
{
	@Valid
	@NotBlank(message="Account Number missing in the request")
    private String accountNumber;

    private List<String> providers;

    public void setAccountNumber(String accountNumber){
    	
        this.accountNumber = accountNumber;
    }
    public String getAccountNumber(){
        return this.accountNumber;
    }
    public void setProviders(List<String> providers){
        this.providers = providers;
    }
    public List<String> getProviders(){
        return this.providers;
    }
}

