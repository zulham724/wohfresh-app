package com.woohfresh.models.api;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class GSecret{

	@SerializedName("redirect")
	private String redirect;

	@SerializedName("personal_access_client")
	private int personalAccessClient;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private Object userId;

	@SerializedName("password_client")
	private int passwordClient;

	@SerializedName("name")
	private String name;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("secret")
	private String secret;

	@SerializedName("revoked")
	private int revoked;

	public void setRedirect(String redirect){
		this.redirect = redirect;
	}

	public String getRedirect(){
		return redirect;
	}

	public void setPersonalAccessClient(int personalAccessClient){
		this.personalAccessClient = personalAccessClient;
	}

	public int getPersonalAccessClient(){
		return personalAccessClient;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUserId(Object userId){
		this.userId = userId;
	}

	public Object getUserId(){
		return userId;
	}

	public void setPasswordClient(int passwordClient){
		this.passwordClient = passwordClient;
	}

	public int getPasswordClient(){
		return passwordClient;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setSecret(String secret){
		this.secret = secret;
	}

	public String getSecret(){
		return secret;
	}

	public void setRevoked(int revoked){
		this.revoked = revoked;
	}

	public int getRevoked(){
		return revoked;
	}

	@Override
 	public String toString(){
		return 
			"GSecret{" + 
			"redirect = '" + redirect + '\'' + 
			",personal_access_client = '" + personalAccessClient + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",user_id = '" + userId + '\'' + 
			",password_client = '" + passwordClient + '\'' + 
			",name = '" + name + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",secret = '" + secret + '\'' + 
			",revoked = '" + revoked + '\'' + 
			"}";
		}
}