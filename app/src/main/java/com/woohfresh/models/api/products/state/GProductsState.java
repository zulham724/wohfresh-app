package com.woohfresh.models.api.products.state;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class GProductsState{

	@SerializedName("quantity")
	private int quantity;

	@SerializedName("product_sales")
	private List<ProductSalesItem> productSales;

	@SerializedName("weight")
	private int weight;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("product_images")
	private List<ProductImagesItem> productImages;

	@SerializedName("badge")
	private String badge;

	@SerializedName("unit")
	private String unit;

	@SerializedName("category_id")
	private int categoryId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("group_id")
	private int groupId;

	@SerializedName("sub_category_id")
	private int subCategoryId;

	@SerializedName("supplier")
	private Supplier supplier;

	@SerializedName("id")
	private int id;

	@SerializedName("supplier_id")
	private int supplierId;

	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public int getQuantity(){
		return quantity;
	}

	public void setProductSales(List<ProductSalesItem> productSales){
		this.productSales = productSales;
	}

	public List<ProductSalesItem> getProductSales(){
		return productSales;
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

	public void setProductImages(List<ProductImagesItem> productImages){
		this.productImages = productImages;
	}

	public List<ProductImagesItem> getProductImages(){
		return productImages;
	}

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

	public void setSupplier(Supplier supplier){
		this.supplier = supplier;
	}

	public Supplier getSupplier(){
		return supplier;
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
			"GProductsState{" + 
			"quantity = '" + quantity + '\'' + 
			",product_sales = '" + productSales + '\'' + 
			",weight = '" + weight + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",product_images = '" + productImages + '\'' + 
			",badge = '" + badge + '\'' + 
			",unit = '" + unit + '\'' + 
			",category_id = '" + categoryId + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",group_id = '" + groupId + '\'' + 
			",sub_category_id = '" + subCategoryId + '\'' + 
			",supplier = '" + supplier + '\'' + 
			",id = '" + id + '\'' + 
			",supplier_id = '" + supplierId + '\'' + 
			"}";
		}
}