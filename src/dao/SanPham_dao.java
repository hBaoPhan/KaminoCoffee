package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
			e.printStackTrace();
		}
		return ds;
		
	}

	public boolean themSanPham(SanPham spMoi) {
		ConnectDB.getInstance();
	    Connection con = ConnectDB.getConnection();
	    PreparedStatement stmt = null;
	    String sql = "INSERT INTO SanPham (maSP, tenSP, gia, loaiSanPham) VALUES (?, ?, ?, ?)";
	    try {
	        stmt = con.prepareStatement(sql);
	        stmt.setString(1, spMoi.getMaSanPham());
	        stmt.setString(2, spMoi.getTenSanPham());
	        stmt.setDouble(3, spMoi.getGia());
	        stmt.setString(4, spMoi.getLoaiSanPham().getMoTa());
	        
	        int n = stmt.executeUpdate();
	        return n > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (stmt != null) stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return false;
	}

	public boolean suaSanPham(SanPham spCapNhat) {
		ConnectDB.getInstance();
	    Connection con = ConnectDB.getConnection();
	    PreparedStatement stmt = null;
	    String sql = "UPDATE SanPham SET tenSP = ?, gia = ?, loaiSanPham = ? WHERE maSP = ?";
	    try {
	        stmt = con.prepareStatement(sql);
	        stmt.setString(1, spCapNhat.getTenSanPham());
	        stmt.setDouble(2, spCapNhat.getGia());
	        stmt.setString(3, spCapNhat.getLoaiSanPham().getMoTa());
	        stmt.setString(4, spCapNhat.getMaSanPham());
	        
	        int n = stmt.executeUpdate();
	        return n > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (stmt != null) stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return false;
	}

	public ArrayList<SanPham> getSanPhamByLoc(String tuKhoa, String loai) {
		ArrayList<SanPham> ds = new ArrayList<>();
	    ConnectDB.getInstance();
	    Connection con = ConnectDB.getConnection();
	    PreparedStatement stmt = null;
	    
	    String sql = "SELECT * FROM SanPham WHERE tenSP LIKE ?";
	    
	    if (!"Tất cả".equalsIgnoreCase(loai)) {
	        sql += " AND loaiSanPham = ?";
	    }

	    try {
	        stmt = con.prepareStatement(sql);
	        stmt.setString(1, "%" + tuKhoa + "%");
	        
	        if (!"Tất cả".equalsIgnoreCase(loai)) {
	            stmt.setString(2, loai);
	        }

	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            String ma = rs.getString("maSP");
	            String ten = rs.getString("tenSP");
	            double gia = rs.getDouble("gia");
	            LoaiSanPham loaiSP = LoaiSanPham.fromString(rs.getString("loaiSanPham"));
	            SanPham sp = new SanPham(ma, ten, gia, loaiSP);
	            ds.add(sp);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (stmt != null) stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return ds;
	}

	public boolean xoaSanPham(String maSanPham) {
		ConnectDB.getInstance();
	    Connection con = ConnectDB.getConnection();
	    PreparedStatement stmt = null;
	    String sql = "DELETE FROM SanPham WHERE maSP = ?";
	    try {
	        stmt = con.prepareStatement(sql);
	        stmt.setString(1, maSanPham);
	        
	        int n = stmt.executeUpdate();
	        return n > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (stmt != null) stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return false;
	}
	
	public SanPham getSanPhamByTen(String tenSP) {
	    ConnectDB.getInstance();
	    Connection con = ConnectDB.getConnection();
	    PreparedStatement stmt = null;
	    String sql = "SELECT * FROM SanPham WHERE tenSP = ?";
	    try {
	        stmt = con.prepareStatement(sql);
	        stmt.setString(1, tenSP);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            String ma = rs.getString("maSP");
	            double gia = rs.getDouble("gia");
	            LoaiSanPham loai = LoaiSanPham.fromString(rs.getString("loaiSanPham"));
	            return new SanPham(ma, tenSP, gia, loai);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (stmt != null) stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return null;
	}
	public SanPham getSanPhamByMa(String maSP) {
	    ConnectDB.getInstance();
	    Connection con = ConnectDB.getConnection();
	    PreparedStatement stmt = null;
	    String sql = "SELECT * FROM SanPham WHERE maSP = ?";
	    try {
	        stmt = con.prepareStatement(sql);
	        stmt.setString(1, maSP);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            String ma = rs.getString("maSP");
	            String ten=rs.getString("tenSp");
	            double gia = rs.getDouble("gia");
	            LoaiSanPham loai = LoaiSanPham.fromString(rs.getString("loaiSanPham"));
	            return new SanPham(ma, ten, gia, loai);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (stmt != null) stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return null;
	}

}