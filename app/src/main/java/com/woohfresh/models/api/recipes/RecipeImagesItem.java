package com.woohfresh.models.api.recipes;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class RecipeImagesItem{

	@SerializedName("image")
	private String image;

	@SerializedName("recipe_id")
	private int recipeId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("description")
	private Object description;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setRecipeId(int recipeId){
		this.recipeId = recipeId;
	}

	public int getRecipeId(){
		return recipeId;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
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

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"RecipeImagesItem{" + 
			"image = '" + image + '\'' + 
			",recipe_id = '" + recipeId + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",description = '" + description + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}