package com.woohfresh.models.api.products;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class ProductSalesItem{

	@SerializedName("subdistrict_id")
	private Object subdistrictId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("price")
	private int price;

	@SerializedName("product_id")
	private int productId;

	@SerializedName("discount")
	private int discount;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("state_id")
	private int stateId;

	@SerializedName("stock")
	private int stock;

	@SerializedName("city_id")
	private Object cityId;

	@SerializedName("is_available")
	private int isAvailable;

	public void setSubdistrictId(Object subdistrictId){
		this.subdistrictId = subdistrictId;
	}

	public Object getSubdistrictId(){
		return subdistrictId;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setPrice(int price){
		this.price = price;
	}

	public int getPrice(){
		return price;
	}

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
	}

	public void setDiscount(int discount){
		this.discount = discount;
	}

	public int getDiscount(){
		return discount;
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

	public void setStateId(int stateId){
		this.stateId = stateId;
	}

	public int getStateId(){
		return stateId;
	}

	public void setStock(int stock){
		this.stock = stock;
	}

	public int getStock(){
		return stock;
	}

	public void setCityId(Object cityId){
		this.cityId = cityId;
	}

	public Object getCityId(){
		return cityId;
	}

	public void setIsAvailable(int isAvailable){
		this.isAvailable = isAvailable;
	}

	public int getIsAvailable(){
		return isAvailable;
	}

	@Override
 	public String toString(){
		return 
			"ProductSalesItem{" + 
			"subdistrict_id = '" + subdistrictId + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",price = '" + price + '\'' + 
			",product_id = '" + productId + '\'' + 
			",discount = '" + discount + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",state_id = '" + stateId + '\'' + 
			",stock = '" + stock + '\'' + 
			",city_id = '" + cityId + '\'' + 
			",is_available = '" + isAvailable + '\'' + 
			"}";
		}
}