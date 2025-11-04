package entity;

import java.util.Objects;

public class Ban {
	private String maBan;
	private int soGhe;
	private TrangThaiBan trangThai;
	public Ban(String maBan, int soGhe, TrangThaiBan trangThai) {
		super();
		this.maBan = maBan;
		this.soGhe = soGhe;
		this.trangThai = trangThai;
	}
	public int getSoGhe() {
		return soGhe;
	}
	public void setSoGhe(int soGhe) {
		this.soGhe = soGhe;
	}
	public TrangThaiBan getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(TrangThaiBan trangThai) {
		this.trangThai = trangThai;
	}
	public String getMaBan() {
		return maBan;
	}
	@Override
	public int hashCode() {
		return Objects.hash(maBan);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ban other = (Ban) obj;
		return Objects.equals(maBan, other.maBan);
	}
	@Override
	public String toString() {
		return "Ban [maBan=" + maBan + ", soGhe=" + soGhe + ", trangThai=" + trangThai + "]";
	}
	
}
