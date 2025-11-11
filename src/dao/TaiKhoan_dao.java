package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connectDB.ConnectDB;
import entity.ChucVu;
import entity.NhanVien;
import entity.TaiKhoan;

public class TaiKhoan_dao {
	public TaiKhoan timTaiKhoan(String tenDangNhap, String matKhau) {
		TaiKhoan taiKhoan = null;
		Connection con = ConnectDB.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			if (con == null) {
				ConnectDB.getInstance().connect();
				con = ConnectDB.getConnection();
				if (con == null) {
					System.err.println("Lỗi kết nối CSDL trong TaiKhoan_dao.");
					return null;
				}
			}
			String sql = "SELECT tk.*, nv.tenNV, nv.sDT, nv.gioiTinh, nv.chucVu " +
						 "FROM TaiKhoan tk " +
						 "JOIN NhanVien nv ON tk.maNV = nv.maNV " +
						 "WHERE tk.tenDangNhap = ? AND tk.matKhau = ?";
			
			stmt = con.prepareStatement(sql);
			stmt.setString(1, tenDangNhap);
			stmt.setString(2, matKhau); 
			
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				String maNV = rs.getString("maNV");
				String tenNV = rs.getString("tenNV");
				String sDT = rs.getString("sDT");
				boolean gioiTinh = rs.getBoolean("gioiTinh");
				ChucVu chucVu = rs.getString("chucVu").equals("QL") ? ChucVu.QUAN_LY : ChucVu.NHAN_VIEN;
				
				NhanVien nv = new NhanVien(maNV, tenNV, sDT, gioiTinh, chucVu);
				
				String tenDN = rs.getString("tenDangNhap");
				String mk = rs.getString("matKhau");
				
				taiKhoan = new TaiKhoan(tenDN, mk, nv);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return taiKhoan;
	}
}