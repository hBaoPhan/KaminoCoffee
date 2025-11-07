package dao;


public class KhachHang_dao {

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.Ban;
import entity.DonDatBan;
import entity.KhachHang;

public class KhachHang_dao {
	public ArrayList<KhachHang> getAllKhachHang() {
		ArrayList<KhachHang> ds = new ArrayList<KhachHang>();
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		PreparedStatement stmt = null;

		try {
			String sql = "SELECT * FROM KhachHang";
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String ma = rs.getString("maKH");
				String ten = rs.getString("tenKH");
				String sdt = rs.getString("sDT");
				int diemTichLuy = rs.getInt("diemTichLuy");
				boolean laKHDK = rs.getBoolean("laKHDK");
				KhachHang kh = new KhachHang(ma, ten, sdt, diemTichLuy, laKHDK);
				ds.add(kh);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}

	public boolean addKhachHang(KhachHang khachHang) {
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		
		String sql = "INSERT INTO KhachHang (maKH, tenKH, sDT, diemTichLuy, laKHDK) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, khachHang.getMaKhachHang());
			stmt.setString(2, khachHang.getTenKhachHang());
			stmt.setString(3, khachHang.getsDT());
			stmt.setInt(4, khachHang.getDiemTichLuy());
			stmt.setBoolean(5, khachHang.isLaKHDK());
			
			int rowsInserted = stmt.executeUpdate();
			return rowsInserted > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
			return false;
		
		
		
	}

}
