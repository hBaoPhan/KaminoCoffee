package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import connectDB.ConnectDB;
import entity.Ban;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.SanPham;

public class ChiTietHoaDon_dao {
	public ArrayList<ChiTietHoaDon> getAllChiTietHoaDon() {
	    ArrayList<ChiTietHoaDon> ds = new ArrayList<>();
	    ConnectDB.getInstance();
	    Connection con = ConnectDB.getConnection();
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        String sql = "SELECT * FROM ChiTietHoaDon";
	        stmt = con.prepareStatement(sql);
	        rs = stmt.executeQuery();

	        while (rs.next()) {
	            HoaDon hd=new HoaDon(rs.getString("maHD"));
	            SanPham sp=new SanPham(rs.getString("maSP"));
	            int soLuong=rs.getInt("soLuong");
	            double tongTien=rs.getDouble("tongTien");
	            ChiTietHoaDon cthd=new ChiTietHoaDon(hd, sp, soLuong);
	            ds.add(cthd);
	            
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
	public boolean insertChiTietHoaDon(ChiTietHoaDon cthd) {
		ConnectDB.getInstance();
		Connection con=ConnectDB.getConnection();
		PreparedStatement stmt=null;
        try {
        	String sql="MERGE INTO ChiTietHoaDon AS target "
        			+ "USING (SELECT ? AS maHD, ? AS maSP, ? AS soLuong, ? AS tongTien) AS source " 
        			+ "ON (target.maHD = source.maHD AND target.maSP = source.maSP) "
        			+ "WHEN MATCHED THEN "
        			+ "UPDATE SET target.soLuong = source.soLuong, "
        			+ "target.tongTien = source.tongTien "
        			+ "WHEN NOT MATCHED THEN "
        			+ "INSERT (maHD, maSP, soLuong, tongTien) "
        			+ "VALUES (source.maHD, source.maSP, source.soLuong, source.tongTien);";
        	stmt = con.prepareStatement(sql);
            stmt.setString(1, cthd.getHoaDon().getMaHoaDon());
            stmt.setString(2, cthd.getSanPham().getMaSanPham());
            stmt.setInt(3, cthd.getSoLuong());
            stmt.setDouble(4, cthd.getTongTien());
           
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	public ArrayList<ChiTietHoaDon> getChiTietHoaDonByMaHoaDon(String maHD) {
	    ArrayList<ChiTietHoaDon> ds = new ArrayList<>();
	    ConnectDB.getInstance();
	    Connection con = ConnectDB.getConnection();
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        String sql = "SELECT * FROM ChiTietHoaDon Where maHD = ?";
	        stmt = con.prepareStatement(sql);
	        stmt.setString(1, maHD);
	        rs = stmt.executeQuery();

	        while (rs.next()) {
	            HoaDon hd=new HoaDon(rs.getString("maHD"));
	            SanPham sp=new SanPham(rs.getString("maSP"));
	            int soLuong=rs.getInt("soLuong");
	            
	            ChiTietHoaDon cthd=new ChiTietHoaDon(hd, sp, soLuong);
	            ds.add(cthd);
	            
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
	public boolean xoaChiTietHoaDonThua(String maHD, ArrayList<String> danhSachMaSPMoi) {
	    ConnectDB.getInstance();
	    Connection con = ConnectDB.getConnection();
	    PreparedStatement stmt = null;
	    try {
	        // Tạo chuỗi dấu ? tương ứng với số lượng maSP
	        String placeholders = String.join(",", Collections.nCopies(danhSachMaSPMoi.size(), "?"));
	        String sql = "DELETE FROM ChiTietHoaDon WHERE maHD = ? AND maSP NOT IN (" + placeholders + ")";
	        stmt = con.prepareStatement(sql);
	        stmt.setString(1, maHD);
	        for (int i = 0; i < danhSachMaSPMoi.size(); i++) {
	            stmt.setString(i + 2, danhSachMaSPMoi.get(i));
	        }
	        int rows = stmt.executeUpdate();
	        return rows >= 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}



}
