package com.woohfresh.models.api.bioadatas;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class PBiodatas{

	@SerializedName("subdistrict_id")
	private String subdistrictId;

	@SerializedName("address")
	private String address;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("last_name")
	private Object lastName;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("phone_number")
	private Object phoneNumber;

	@SerializedName("state_id")
	private String stateId;

	@SerializedName("id")
	private int id;

	@SerializedName("first_name")
	private Object firstName;

	@SerializedName("city_id")
	private String cityId;

	public void setSubdistrictId(String subdistrictId){
		this.subdistrictId = subdistrictId;
	}

	public String getSubdistrictId(){
		return subdistrictId;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setLastName(Object lastName){
		this.lastName = lastName;
	}

	public Object getLastName(){
		return lastName;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setPhoneNumber(Object phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public Object getPhoneNumber(){
		return phoneNumber;
	}

	public void setStateId(String stateId){
		this.stateId = stateId;
	}

	public String getStateId(){
		return stateId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setFirstName(Object firstName){
		this.firstName = firstName;
	}

	public Object getFirstName(){
		return firstName;
	}

	public void setCityId(String cityId){
		this.cityId = cityId;
	}

	public String getCityId(){
		return cityId;
	}

	@Override
 	public String toString(){
		return 
			"PBiodatas{" + 
			"subdistrict_id = '" + subdistrictId + '\'' + 
			",address = '" + address + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",user_id = '" + userId + '\'' + 
			",last_name = '" + lastName + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",phone_number = '" + phoneNumber + '\'' + 
			",state_id = '" + stateId + '\'' + 
			",id = '" + id + '\'' + 
			",first_name = '" + firstName + '\'' + 
			",city_id = '" + cityId + '\'' + 
			"}";
		}
}