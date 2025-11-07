package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.Ban;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.TrangThaiBan;

public class HoaDon_dao {
	public ArrayList<HoaDon> getAllHoaHon() {
		ArrayList<HoaDon> ds=new ArrayList<HoaDon>();
		ConnectDB.getInstance();
		Connection con=ConnectDB.getConnection();
		PreparedStatement stmt=null;
		ResultSet rs = null;
		try {
			String sql="SELECT * FROM HoaDon";
			stmt=con.prepareStatement(sql);
			rs=stmt.executeQuery();
			while (rs.next()) {
				String ma=rs.getString("maHD");
				NhanVien nv=new NhanVien(rs.getString("maNV"));
				KhachHang kh=new KhachHang(rs.getString("maKH"));
				Ban ban=new Ban(rs.getString("maBan"));
				LocalDateTime thoiGianVao=rs.getTimestamp("thoiGianVao").toLocalDateTime();
				LocalDateTime thoiGianRa=rs.getTimestamp("thoiGianRa").toLocalDateTime();
				boolean trangThaiThanhToan=rs.getBoolean("trangThaiThanhToan");
				
				
				HoaDon hd=new HoaDon(ma, ban, nv, trangThaiThanhToan, kh, thoiGianVao, thoiGianRa);
				ds.add(hd);
				
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ds;
	}
}
