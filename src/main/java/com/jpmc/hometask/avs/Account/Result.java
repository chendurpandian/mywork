package com.jpmc.hometask.avs.Account;

public class Result {
	    private String provider;

	    private boolean isValid;

	    public void setProvider(String provider){
	        this.provider = provider;
	    }
	    public String getProvider(){
	        return this.provider;
	    }
	    public void setIsValid(boolean isValid){
	        this.isValid = isValid;
	    }
	    public boolean getIsValid(){
	        return this.isValid;
	    }
}
