package com.woohfresh.models.api.region.city;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class GCity{

	@SerializedName("daftar_kecamatan")
	private List<DaftarKecamatanItem> daftarKecamatan;

	@SerializedName("error")
	private boolean error;

	@SerializedName("message")
	private String message;

	public void setDaftarKecamatan(List<DaftarKecamatanItem> daftarKecamatan){
		this.daftarKecamatan = daftarKecamatan;
	}

	public List<DaftarKecamatanItem> getDaftarKecamatan(){
		return daftarKecamatan;
	}

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"GCity{" + 
			"daftar_kecamatan = '" + daftarKecamatan + '\'' + 
			",error = '" + error + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}