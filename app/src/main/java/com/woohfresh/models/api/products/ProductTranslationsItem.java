package com.woohfresh.models.api.products;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class ProductTranslationsItem{

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("product_id")
	private int productId;

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("language")
	private Language language;

	@SerializedName("id")
	private int id;

	@SerializedName("language_id")
	private int languageId;

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setLanguage(Language language){
		this.language = language;
	}

	public Language getLanguage(){
		return language;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setLanguageId(int languageId){
		this.languageId = languageId;
	}

	public int getLanguageId(){
		return languageId;
	}

	@Override
 	public String toString(){
		return 
			"ProductTranslationsItem{" + 
			"updated_at = '" + updatedAt + '\'' + 
			",product_id = '" + productId + '\'' + 
			",name = '" + name + '\'' + 
			",description = '" + description + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",language = '" + language + '\'' + 
			",id = '" + id + '\'' + 
			",language_id = '" + languageId + '\'' + 
			"}";
		}
}