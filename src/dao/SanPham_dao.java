package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.LoaiSanPham;
import entity.SanPham;

public class SanPham_dao {
	public ArrayList<SanPham> getAllSanPham() {
		ArrayList<SanPham> ds=new ArrayList<SanPham>();
		ConnectDB.getInstance();
		Connection con=ConnectDB.getConnection();
		PreparedStatement stmt=null;
		try {
			String sql="SELECT * FROM SanPham";
			stmt=con.prepareStatement(sql);
			ResultSet rs=stmt.executeQuery();
			
			while (rs.next()) {
				String ma=rs.getString("maSP");
				String ten=rs.getString("tenSP");
				double gia=rs.getDouble("gia");
				LoaiSanPham loai=LoaiSanPham.fromString(rs.getString("loaiSanPham"));
				SanPham sp=new SanPham(ma, ten, gia, loai);
				ds.add(sp);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ds;
		
	}

}
