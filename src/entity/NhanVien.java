package entity;

import java.util.Objects;

public class NhanVien {
    private String maNV;
    private String tenNV;
    private String sDT;
    private boolean gioiTinh; // true = Nữ, false = Nam
    private ChucVu chucVu;

    public NhanVien(String maNV, String tenNV, String sDT, boolean gioiTinh, ChucVu chucVu) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.sDT = sDT;
        this.gioiTinh = gioiTinh;
        this.chucVu = chucVu;
    }

    public NhanVien(String maNV) {
        this.maNV = maNV;
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

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public ChucVu getChucVu() {
        return chucVu;
    }

    public void setChucVu(ChucVu chucVu) {
        this.chucVu = chucVu;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maNV);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        NhanVien other = (NhanVien) obj;
        return Objects.equals(maNV, other.maNV);
    }

    @Override
    public String toString() {
        return String.format(
            "NhanVien[maNV=%s, tenNV=%s, sDT=%s, gioiTinh=%s, chucVu=%s]",
            maNV, tenNV, sDT, gioiTinh ? "Nữ" : "Nam", chucVu
        );
    }
}
