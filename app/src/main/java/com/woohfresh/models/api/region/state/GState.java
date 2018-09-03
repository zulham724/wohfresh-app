package com.woohfresh.models.api.region.state;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class GState{

	@SerializedName("error")
	private boolean error;

	@SerializedName("message")
	private String message;

	@SerializedName("semuaprovinsi")
	private List<SemuaprovinsiItem> semuaprovinsi;

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setSemuaprovinsi(List<SemuaprovinsiItem> semuaprovinsi){
		this.semuaprovinsi = semuaprovinsi;
	}

	public List<SemuaprovinsiItem> getSemuaprovinsi(){
		return semuaprovinsi;
	}

	@Override
 	public String toString(){
		return 
			"GState{" + 
			"error = '" + error + '\'' + 
			",message = '" + message + '\'' + 
			",semuaprovinsi = '" + semuaprovinsi + '\'' + 
			"}";
		}
}