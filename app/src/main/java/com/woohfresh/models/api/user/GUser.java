package com.woohfresh.models.api.user;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class GUser{

	@SerializedName("recipes")
	private List<Object> recipes;

	@SerializedName("google_id")
	private Object googleId;

	@SerializedName("is_active")
	private int isActive;

	@SerializedName("last_login")
	private Object lastLogin;

	@SerializedName("biodata")
	private Biodata biodata;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("avatar")
	private Object avatar;

	@SerializedName("transactions")
	private List<Object> transactions;

	@SerializedName("facebook_id")
	private Object facebookId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("role_id")
	private int roleId;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("email")
	private String email;

	public void setRecipes(List<Object> recipes){
		this.recipes = recipes;
	}

	public List<Object> getRecipes(){
		return recipes;
	}

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

	public void setLastLogin(Object lastLogin){
		this.lastLogin = lastLogin;
	}

	public Object getLastLogin(){
		return lastLogin;
	}

	public void setBiodata(Biodata biodata){
		this.biodata = biodata;
	}

	public Biodata getBiodata(){
		return biodata;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setAvatar(Object avatar){
		this.avatar = avatar;
	}

	public Object getAvatar(){
		return avatar;
	}

	public void setTransactions(List<Object> transactions){
		this.transactions = transactions;
	}

	public List<Object> getTransactions(){
		return transactions;
	}

	public void setFacebookId(Object facebookId){
		this.facebookId = facebookId;
	}

	public Object getFacebookId(){
		return facebookId;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setRoleId(int roleId){
		this.roleId = roleId;
	}

	public int getRoleId(){
		return roleId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"GUser{" + 
			"recipes = '" + recipes + '\'' + 
			",google_id = '" + googleId + '\'' + 
			",is_active = '" + isActive + '\'' + 
			",last_login = '" + lastLogin + '\'' + 
			",biodata = '" + biodata + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",avatar = '" + avatar + '\'' + 
			",transactions = '" + transactions + '\'' + 
			",facebook_id = '" + facebookId + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",role_id = '" + roleId + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}