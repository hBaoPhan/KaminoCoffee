package entity;

import enums.ChucVu;

public class NhanVien {
	private String maNV;
	private String tenNV;
	private String sDT;
	private ChucVu chucVu;
	public NhanVien(String maNV, String tenNV, String sDT, ChucVu chucVu) {
		super();
		this.maNV = maNV;
		this.tenNV = tenNV;
		this.sDT = sDT;
		this.chucVu = chucVu;
	}
	public String getMaNV() {
		return maNV;
	}
	public void setMaNV(String maNV) {
		this.maNV = maNV;
	}
	public String getTenNV() {
		return tenNV;
	}
	public void setTenNV(String tenNV) {
		this.tenNV = tenNV;
	}
	public String getsDT() {
		return sDT;
	}
	public void setsDT(String sDT) {
		this.sDT = sDT;
	}
	public ChucVu getChucVu() {
		return chucVu;
	}
	public void setChucVu(ChucVu chucVu) {
		this.chucVu = chucVu;
	}
	
	
	
}
