package com.woohfresh.models.api.subcategories;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class SubCategoryTranslationsItem{

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("sub_category_id")
	private int subCategoryId;

	@SerializedName("name")
	private String name;

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

	public void setSubCategoryId(int subCategoryId){
		this.subCategoryId = subCategoryId;
	}

	public int getSubCategoryId(){
		return subCategoryId;
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
			"SubCategoryTranslationsItem{" + 
			"updated_at = '" + updatedAt + '\'' + 
			",sub_category_id = '" + subCategoryId + '\'' + 
			",name = '" + name + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",language = '" + language + '\'' + 
			",id = '" + id + '\'' + 
			",language_id = '" + languageId + '\'' + 
			"}";
		}
}