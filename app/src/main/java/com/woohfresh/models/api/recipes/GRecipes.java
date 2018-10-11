package com.woohfresh.models.api.recipes;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class GRecipes{

	@SerializedName("difficulty_level")
	private int difficultyLevel;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("name")
	private String name;

	@SerializedName("portion_per_serve")
	private int portionPerServe;

	@SerializedName("description")
	private String description;

	@SerializedName("preparation_time")
	private int preparationTime;

	@SerializedName("created_at")
	private String createdAt;

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

	public void setPreparationTime(int preparationTime){
		this.preparationTime = preparationTime;
	}

	public int getPreparationTime(){
		return preparationTime;
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
			",updated_at = '" + updatedAt + '\'' + 
			",user_id = '" + userId + '\'' + 
			",name = '" + name + '\'' + 
			",portion_per_serve = '" + portionPerServe + '\'' + 
			",description = '" + description + '\'' + 
			",preparation_time = '" + preparationTime + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",tutorial = '" + tutorial + '\'' + 
			"}";
		}
}