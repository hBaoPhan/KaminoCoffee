package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.Ban;
import entity.KhachHang;
import entity.TrangThaiBan;

public class Ban_dao {
	public ArrayList<Ban> getAllBan() {
		ArrayList<Ban> ds=new ArrayList<Ban>();
		ConnectDB.getInstance();
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
	public boolean updateBan(Ban ban) {
		ConnectDB.getInstance();
		Connection con=ConnectDB.getConnection();
        String sql = "UPDATE Ban SET tenBan = ?, soGhe = ?, trangThai = ? WHERE maBan = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, ban.getTenBan());
            stmt.setInt(2, ban.getSoGhe());
            stmt.setString(3, ban.getTrangThai().getMoTa()); 
            stmt.setString(4, ban.getMaBan());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	 public Ban timTheoMa(String maBan) {
		 ConnectDB.getInstance();
		 Connection con=ConnectDB.getConnection();
	      try {
	        	String sql = "SELECT * FROM Ban WHERE maBan = ?";
	        	PreparedStatement stmt = con.prepareStatement(sql);
	            stmt.setString(1, maBan);
	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	            	String ma=rs.getString("maBan");
					String ten=rs.getString("tenBan");
					int soGhe=rs.getInt("soGhe");
					String trangThaiStr = rs.getString("trangThai");
					TrangThaiBan trangThai = null;
					
					trangThai = TrangThaiBan.fromString(trangThaiStr);
					
					Ban ban= new Ban(ma, ten, soGhe, trangThai);
					return ban;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	      return null;
	       
	    }


}