package com.woohfresh.models.api.user;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class GUser{

	@SerializedName("google_id")
	private Object googleId;

	@SerializedName("is_active")
	private int isActive;

	@SerializedName("updated_at")
	private Object updatedAt;

	@SerializedName("role_id")
	private int roleId;

	@SerializedName("last_login")
	private Object lastLogin;

	@SerializedName("name")
	private String name;

	@SerializedName("created_at")
	private Object createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("avatar")
	private Object avatar;

	@SerializedName("email")
	private String email;

	@SerializedName("facebook_id")
	private Object facebookId;

	public void setGoogleId(Object googleId){
		this.googleId = googleId;
	}

	public Object getGoogleId(){
		return googleId;
	}

	public void setIsActive(int isActive){
		this.isActive = isActive;
	}

	public int getIsActive(){
		return isActive;
	}

	public void setUpdatedAt(Object updatedAt){
		this.updatedAt = updatedAt;
	}

	public Object getUpdatedAt(){
		return updatedAt;
	}

	public void setRoleId(int roleId){
		this.roleId = roleId;
	}

	public int getRoleId(){
		return roleId;
	}

	public void setLastLogin(Object lastLogin){
		this.lastLogin = lastLogin;
	}

	public Object getLastLogin(){
		return lastLogin;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setCreatedAt(Object createdAt){
		this.createdAt = createdAt;
	}

	public Object getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setAvatar(Object avatar){
		this.avatar = avatar;
	}

	public Object getAvatar(){
		return avatar;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setFacebookId(Object facebookId){
		this.facebookId = facebookId;
	}

	public Object getFacebookId(){
		return facebookId;
	}

	@Override
 	public String toString(){
		return 
			"GUser{" + 
			"google_id = '" + googleId + '\'' + 
			",is_active = '" + isActive + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",role_id = '" + roleId + '\'' + 
			",last_login = '" + lastLogin + '\'' + 
			",name = '" + name + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",avatar = '" + avatar + '\'' + 
			",email = '" + email + '\'' + 
			",facebook_id = '" + facebookId + '\'' + 
			"}";
		}
}