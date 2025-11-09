package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.Ban;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.TrangThaiBan;

public class HoaDon_dao {
	public ArrayList<HoaDon> getAllHoaDon() {
	    ArrayList<HoaDon> ds = new ArrayList<>();
	    ConnectDB.getInstance();
	    Connection con = ConnectDB.getConnection();
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        String sql = "SELECT * FROM HoaDon";
	        stmt = con.prepareStatement(sql);
	        rs = stmt.executeQuery();

	        while (rs.next()) {
	            String ma = rs.getString("maHD");
	            NhanVien nv = new NhanVien(rs.getString("maNV"));
	            KhachHang kh = new KhachHang(rs.getString("maKH"));
	            Ban ban = new Ban(rs.getString("maBan"));

	            // Xử lý thời gian vào
	            LocalDateTime thoiGianVao = rs.getTimestamp("thoiGianVao").toLocalDateTime();

	            // Xử lý thời gian ra có thể null
	            Timestamp tsRa = rs.getTimestamp("thoiGianRa");
	            LocalDateTime thoiGianRa = (tsRa != null) ? tsRa.toLocalDateTime() : null;

	            boolean trangThaiThanhToan = rs.getBoolean("trangThaiThanhToan");

	            HoaDon hd = new HoaDon(ma, ban, nv, trangThaiThanhToan, kh, thoiGianVao, thoiGianRa);
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

	public boolean insertHoaDon(HoaDon hoaDon) {
		ConnectDB.getInstance();
		Connection con=ConnectDB.getConnection();
		PreparedStatement stmt=null;
		ResultSet rs = null;
        try {
        	String sql = "INSERT INTO HoaDon (maHD, maNV, maKH, maBan, thoiGianVao, thoiGianRa, trangThaiThanhToan) " +"VALUES (?, ?, ?, ?, ?, ?, ?)";
        	stmt = con.prepareStatement(sql);
            stmt.setString(1, hoaDon.getMaHoaDon());
            stmt.setString(2, hoaDon.getNhanVienBan().getMaNV());
            stmt.setString(3, hoaDon.getKhachHang().getMaKhachHang());
            stmt.setString(4, hoaDon.getBan().getMaBan());
            // Chuyển LocalDateTime sang Timestamp
            stmt.setTimestamp(5, Timestamp.valueOf(hoaDon.getThoiGianVao()));

            if (hoaDon.getThoiGianRa() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(hoaDon.getThoiGianRa()));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }

            stmt.setBoolean(7, hoaDon.isTrangThaiThanhToan());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
