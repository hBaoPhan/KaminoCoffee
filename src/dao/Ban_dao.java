package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.Ban;
import entity.TrangThaiBan;

public class Ban_dao {
	public ArrayList<Ban> getAllBan() {
		ArrayList<Ban> ds=new ArrayList<Ban>();
		try {
			ConnectDB.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Connection con=ConnectDB.getConnection();
		PreparedStatement stmt=null;
		ResultSet rs = null;
		try {
			if (con == null) {
				System.err.println("Database connection is null in Ban_dao.getAllBan()");
				return ds;
			}
			String sql="SELECT * FROM Ban";
			stmt=con.prepareStatement(sql);
			rs=stmt.executeQuery();
			while (rs.next()) {
				
				String ma=rs.getString("maBan");
				String ten=rs.getString("tenBan");
				int soGhe=rs.getInt("soGhe");
				String trangThaiStr = rs.getString("trangThai");
				TrangThaiBan trangThai = null;
				
				trangThai = TrangThaiBan.fromString(trangThaiStr);
				
				Ban ban=new Ban(ma, ten, soGhe, trangThai);
				ds.add(ban);
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