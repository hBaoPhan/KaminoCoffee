package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class DonDatBan {
	private String maDonDatHang;
	private KhachHang khachHang;
	private Ban ban;
	private LocalDateTime thoiGian;
	public DonDatBan(String maDonDatHang, KhachHang khachHang, Ban ban, LocalDateTime thoiGian) {
		super();
		this.maDonDatHang = maDonDatHang;
		this.khachHang = khachHang;
		this.ban = ban;
		this.thoiGian = thoiGian;
	}
	public KhachHang getKhachHang() {
		return khachHang;
	}
	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}
	public Ban getBan() {
		return ban;
	}
	public void setBan(Ban ban) {
		this.ban = ban;
	}
	public LocalDateTime getThoiGian() {
		return thoiGian;
	}
	public void setThoiGian(LocalDateTime thoiGian) {
		this.thoiGian = thoiGian;
	}
	public String getMaDonDatHang() {
		return maDonDatHang;
	}
	@Override
	public int hashCode() {
		return Objects.hash(maDonDatHang);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DonDatBan other = (DonDatBan) obj;
		return Objects.equals(maDonDatHang, other.maDonDatHang);
	}
	@Override
	public String toString() {
		return "DonDatBan [maDonDatHang=" + maDonDatHang + ", khachHang=" + khachHang + ", ban=" + ban + ", thoiGian="
				+ thoiGian + "]";
	}
	
}
