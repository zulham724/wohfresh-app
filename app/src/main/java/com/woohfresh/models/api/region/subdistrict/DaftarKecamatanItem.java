package com.woohfresh.models.api.region.subdistrict;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class DaftarKecamatanItem{

	@SerializedName("nama")
	private String nama;

	@SerializedName("id")
	private String id;

	@SerializedName("id_kabupaten")
	private String idKabupaten;

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

	public void setIdKabupaten(String idKabupaten){
		this.idKabupaten = idKabupaten;
	}

	public String getIdKabupaten(){
		return idKabupaten;
	}

	@Override
 	public String toString(){
		return 
			"DaftarKecamatanItem{" + 
			"nama = '" + nama + '\'' + 
			",id = '" + id + '\'' + 
			",id_kabupaten = '" + idKabupaten + '\'' + 
			"}";
		}
}