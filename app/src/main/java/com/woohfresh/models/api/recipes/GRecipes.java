package com.woohfresh.models.api.recipes;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class GRecipes{

	@SerializedName("difficulty_level")
	private int difficultyLevel;

	@SerializedName("recipe_comments")
	private List<Object> recipeComments;

	@SerializedName("portion_per_serve")
	private int portionPerServe;

	@SerializedName("description")
	private String description;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("name")
	private String name;

	@SerializedName("recipe_images")
	private List<RecipeImagesItem> recipeImages;

	@SerializedName("recipe_tutorials")
	private List<Object> recipeTutorials;

	@SerializedName("preparation_time")
	private int preparationTime;

	@SerializedName("ingredients")
	private List<Object> ingredients;

	@SerializedName("id")
	private int id;

	@SerializedName("tutorial")
	private String tutorial;

	public void setDifficultyLevel(int difficultyLevel){
		this.difficultyLevel = difficultyLevel;
	}

	public int getDifficultyLevel(){
		return difficultyLevel;
	}

	public void setRecipeComments(List<Object> recipeComments){
		this.recipeComments = recipeComments;
	}

	public List<Object> getRecipeComments(){
		return recipeComments;
	}

	public void setPortionPerServe(int portionPerServe){
		this.portionPerServe = portionPerServe;
	}

	public int getPortionPerServe(){
		return portionPerServe;
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

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setRecipeImages(List<RecipeImagesItem> recipeImages){
		this.recipeImages = recipeImages;
	}

	public List<RecipeImagesItem> getRecipeImages(){
		return recipeImages;
	}

	public void setRecipeTutorials(List<Object> recipeTutorials){
		this.recipeTutorials = recipeTutorials;
	}

	public List<Object> getRecipeTutorials(){
		return recipeTutorials;
	}

	public void setPreparationTime(int preparationTime){
		this.preparationTime = preparationTime;
	}

	public int getPreparationTime(){
		return preparationTime;
	}

	public void setIngredients(List<Object> ingredients){
		this.ingredients = ingredients;
	}

	public List<Object> getIngredients(){
		return ingredients;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setTutorial(String tutorial){
		this.tutorial = tutorial;
	}

	public String getTutorial(){
		return tutorial;
	}

	@Override
 	public String toString(){
		return 
			"GRecipes{" + 
			"difficulty_level = '" + difficultyLevel + '\'' + 
			",recipe_comments = '" + recipeComments + '\'' + 
			",portion_per_serve = '" + portionPerServe + '\'' + 
			",description = '" + description + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",user_id = '" + userId + '\'' + 
			",name = '" + name + '\'' + 
			",recipe_images = '" + recipeImages + '\'' + 
			",recipe_tutorials = '" + recipeTutorials + '\'' + 
			",preparation_time = '" + preparationTime + '\'' + 
			",ingredients = '" + ingredients + '\'' + 
			",id = '" + id + '\'' + 
			",tutorial = '" + tutorial + '\'' + 
			"}";
		}
}