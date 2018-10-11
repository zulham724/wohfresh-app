package com.woohfresh.models.api.search.products;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class GSearch{

	@SerializedName("badge")
	private String badge;

	@SerializedName("unit")
	private String unit;

	@SerializedName("product_translations")
	private List<ProductTranslationsItem> productTranslations;

	@SerializedName("quantity")
	private int quantity;

	@SerializedName("category_id")
	private int categoryId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("group_id")
	private int groupId;

	@SerializedName("sub_category_id")
	private int subCategoryId;

	@SerializedName("weight")
	private int weight;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("supplier_id")
	private int supplierId;

	public void setBadge(String badge){
		this.badge = badge;
	}

	public String getBadge(){
		return badge;
	}

	public void setUnit(String unit){
		this.unit = unit;
	}

	public String getUnit(){
		return unit;
	}

	public void setProductTranslations(List<ProductTranslationsItem> productTranslations){
		this.productTranslations = productTranslations;
	}

	public List<ProductTranslationsItem> getProductTranslations(){
		return productTranslations;
	}

	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public int getQuantity(){
		return quantity;
	}

	public void setCategoryId(int categoryId){
		this.categoryId = categoryId;
	}

	public int getCategoryId(){
		return categoryId;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setGroupId(int groupId){
		this.groupId = groupId;
	}

	public int getGroupId(){
		return groupId;
	}

	public void setSubCategoryId(int subCategoryId){
		this.subCategoryId = subCategoryId;
	}

	public int getSubCategoryId(){
		return subCategoryId;
	}

	public void setWeight(int weight){
		this.weight = weight;
	}

	public int getWeight(){
		return weight;
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

	public void setSupplierId(int supplierId){
		this.supplierId = supplierId;
	}

	public int getSupplierId(){
		return supplierId;
	}

	@Override
 	public String toString(){
		return 
			"GSearch{" + 
			"badge = '" + badge + '\'' + 
			",unit = '" + unit + '\'' + 
			",product_translations = '" + productTranslations + '\'' + 
			",quantity = '" + quantity + '\'' + 
			",category_id = '" + categoryId + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",group_id = '" + groupId + '\'' + 
			",sub_category_id = '" + subCategoryId + '\'' + 
			",weight = '" + weight + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",supplier_id = '" + supplierId + '\'' + 
			"}";
		}
}