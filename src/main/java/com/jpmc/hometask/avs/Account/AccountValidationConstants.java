package com.jpmc.hometask.avs.Account;

import org.springframework.stereotype.Component;

@Component
public class AccountValidationConstants {
  final public static String PROPERTY_DOMAIN = "account.domain.";
  final public static String PROPERTY_RESOURCE_TYPE = "account.resource";
  final public static String PROPERTY_ENV_INSTANCE = "env.instance";
  final public static String SERVICE_ACCOUNT_NUMBER ="accountNumber";
  final public static String VALID_ACCT = "12345678";
  final public static String ACCT_IS_VALID = "isValid";
}
