package com.woohfresh.models.api.products;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Supplier{

	@SerializedName("address_detail")
	private String addressDetail;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private Object description;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("contact_last_name")
	private String contactLastName;

	@SerializedName("phone_number")
	private String phoneNumber;

	@SerializedName("id")
	private int id;

	@SerializedName("contact_first_name")
	private String contactFirstName;

	@SerializedName("contact_title")
	private String contactTitle;

	@SerializedName("email")
	private String email;

	public void setAddressDetail(String addressDetail){
		this.addressDetail = addressDetail;
	}

	public String getAddressDetail(){
		return addressDetail;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setDescription(Object description){
		this.description = description;
	}

	public Object getDescription(){
		return description;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setContactLastName(String contactLastName){
		this.contactLastName = contactLastName;
	}

	public String getContactLastName(){
		return contactLastName;
	}

	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setContactFirstName(String contactFirstName){
		this.contactFirstName = contactFirstName;
	}

	public String getContactFirstName(){
		return contactFirstName;
	}

	public void setContactTitle(String contactTitle){
		this.contactTitle = contactTitle;
	}

	public String getContactTitle(){
		return contactTitle;
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
			"Supplier{" + 
			"address_detail = '" + addressDetail + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",name = '" + name + '\'' + 
			",description = '" + description + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",contact_last_name = '" + contactLastName + '\'' + 
			",phone_number = '" + phoneNumber + '\'' + 
			",id = '" + id + '\'' + 
			",contact_first_name = '" + contactFirstName + '\'' + 
			",contact_title = '" + contactTitle + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}