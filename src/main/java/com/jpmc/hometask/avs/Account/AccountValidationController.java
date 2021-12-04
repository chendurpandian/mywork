package com.jpmc.hometask.avs.Account;


import java.net.URISyntaxException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class AccountValidationController {
	
	@Autowired
	private AccountValidationService accountValidationService;
	
	@PostMapping(value = "/account/v1/validate",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> validateAccountByAccNum(@Valid @RequestBody AccountReq accountReq, BindingResult bindingResult) throws JsonProcessingException, URISyntaxException{
		if(bindingResult.hasErrors()) {
			AccountReqErrorResp acctErr = new AccountReqErrorResp();
			acctErr.setFieldName(AccountValidationConstants.SERVICE_ACCOUNT_NUMBER);
			acctErr.setErrorMsg(bindingResult.getFieldError(AccountValidationConstants.SERVICE_ACCOUNT_NUMBER).getDefaultMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(acctErr);
		}
		return ResponseEntity.ok(accountValidationService.validateAccountByAccNum(accountReq));
	}
	
	@ExceptionHandler({JsonProcessingException.class,URISyntaxException.class})
	public ResponseEntity<Object> handleException() {
		return new ResponseEntity<>("Exception occured when processing the api request", HttpStatus.EXPECTATION_FAILED);
	}
}
