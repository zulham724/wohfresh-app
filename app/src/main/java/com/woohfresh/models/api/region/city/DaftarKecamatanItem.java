package com.woohfresh.models.api.region.city;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class DaftarKecamatanItem{

	@SerializedName("nama")
	private String nama;

	@SerializedName("id")
	private String id;

	@SerializedName("id_prov")
	private String idProv;

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setIdProv(String idProv){
		this.idProv = idProv;
	}

	public String getIdProv(){
		return idProv;
	}

	@Override
 	public String toString(){
		return 
			"DaftarKecamatanItem{" + 
			"nama = '" + nama + '\'' + 
			",id = '" + id + '\'' + 
			",id_prov = '" + idProv + '\'' + 
			"}";
		}
}