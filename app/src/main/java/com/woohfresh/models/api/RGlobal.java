package com.woohfresh.models.api;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class RGlobal{

	@SerializedName("error")
	private String error;

	@SerializedName("message")
	private String message;

	public void setError(String error){
		this.error = error;
	}

	public String getError(){
		return error;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"RGlobal{" + 
			"error = '" + error + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}