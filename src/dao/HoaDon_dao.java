package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.Ban;
import entity.HoaDon;
import entity.KhachHang;
import entity.LoaiSanPham;
import entity.NhanVien;
import entity.SanPham;
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
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return ds;
	}
	 public ArrayList<Object[]> getAllHoaDonChoPanel() {
        ArrayList<Object[]> danhSach = new ArrayList<>();
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        
        // SỬA LỖI SQL: Dùng KH.maKH cho JOIN và KH.sDT, KH.diemTichLuy, KH.laKHDK
        String sql = "SELECT HD.maHD, KH.tenKhachHang, B.tenBan, KH.sDT, KH.diemTichLuy, KH.laKHDK, " +
                     "       ISNULL(SUM(CT.soLuong * SP.gia), 0) AS TongTien, " +
                     "       HD.trangThaiThanhToan " + 
                     "FROM HoaDon HD " +
                     "JOIN KhachHang KH ON HD.maKH = KH.maKH " + // <--- ĐÃ SỬA: KH.maKH
                     "JOIN Ban B ON HD.maBan = B.maBan " +
                     "LEFT JOIN ChiTietHoaDon CT ON HD.maHD = CT.maHD " +
                     "LEFT JOIN SanPham SP ON CT.maSanPham = SP.maSanPham " +
                     "GROUP BY HD.maHD, KH.tenKhachHang, B.tenBan, KH.sDT, KH.diemTichLuy, KH.laKHDK, HD.trangThaiThanhToan";


        try (Statement stmt = con.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                
                // 1. Ánh xạ trạng thái số (boolean) sang chữ
                String trangThaiStr;
                boolean isPaid = rs.getBoolean("trangThaiThanhToan");
                if (isPaid) {
                    trangThaiStr = "Đã thanh toán";
                } else {
                    trangThaiStr = "Chờ thanh toán"; 
                }
                
                // 2. Ánh xạ KHDK
                String khdkStr = rs.getBoolean("laKHDK") ? "Có" : "Không";

                // 3. Lấy Tổng tiền (TongTien)
                double tongTienDouble = rs.getDouble("TongTien");
                int diemTichLuy = rs.getInt("diemTichLuy");

                // Thêm vào danh sách (Object[] khớp với 8 cột trong JTable của bạn)
                danhSach.add(new Object[]{
                    rs.getString("maHD"),
                    rs.getString("tenKhachHang"),
                    rs.getString("tenBan"),
                    rs.getString("sDT"),
                    diemTichLuy, // Cột 4: Diem TL (Integer)
                    khdkStr, // Cột 5: KHDK (String)
                    tongTienDouble, // Cột 6: Tổng tiền (Double)
                    trangThaiStr // Cột 7: Trạng thái (String)
                });
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn tất cả hóa đơn cho Panel: " + e.getMessage());
        }
        return danhSach;
    }
	public Object[] getChiTietHoaDonDangHoatDongByTenBan(String tenBan) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        
        // SỬA LỖI SQL: Dùng KH.maKH cho JOIN và tên cột chính xác
        String sql = "SELECT TOP 1 HD.maHD, KH.tenKhachHang, B.tenBan, KH.sDT, KH.diemTichLuy, KH.laKHDK, " +
                     "       ISNULL(SUM(CT.soLuong * SP.gia), 0) AS TongTien " + 
                     "FROM HoaDon HD " +
                     "JOIN KhachHang KH ON HD.maKH = KH.maKH " + // <--- ĐÃ SỬA: KH.maKH
                     "JOIN Ban B ON HD.maBan = B.maBan " +
                     "LEFT JOIN ChiTietHoaDon CT ON HD.maHD = CT.maHD " +
                     "LEFT JOIN SanPham SP ON CT.maSanPham = SP.maSanPham " +
                     "WHERE B.tenBan = ? AND HD.trangThaiThanhToan = 0 " + // 0: Chờ TT
                     "GROUP BY HD.maHD, KH.tenKhachHang, B.tenBan, KH.sDT, KH.diemTichLuy, KH.laKHDK";


        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tenBan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    
                    String khdkStr = rs.getBoolean("laKHDK") ? "Có" : "Không";
                    double tongTienDouble = rs.getDouble("TongTien");

                    // Trả về Object[] (7 phần tử) khớp với hàm hienThiChiTiet(...)
                    return new Object[]{
                        rs.getString("maHD"),
                        rs.getString("tenKhachHang"),
                        rs.getString("tenBan"),
                        rs.getString("sDT"),
                        rs.getInt("diemTichLuy"), // Integer
                        khdkStr, // laKHDK (String)
                        tongTienDouble // tongTien (Double)
                    };
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn hóa đơn theo Tên Bàn: " + e.getMessage());
        }
        return null; // Không tìm thấy
    }

	public boolean insertHoaDon(HoaDon hoaDon) {
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "INSERT INTO HoaDon (maHD, maNV, maKH, maBan, thoiGianVao, thoiGianRa, trangThaiThanhToan) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
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
	

	public HoaDon getHoaDonByMa(String maHD) {
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		PreparedStatement stmt = null;
		String sql = "SELECT * FROM HoaDon WHERE maHD = ?";
		try {
			stmt = con.prepareStatement(sql);
			stmt.setString(1, maHD);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
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

				return new HoaDon(ma, ban, nv, trangThaiThanhToan, kh, thoiGianVao, thoiGianRa);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public boolean updateHoaDon(HoaDon hoaDon) {
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "update HoaDon  "
					+ "set maNV = ?, maKH = ?, maBan = ?, ThoiGianVao = ?, thoiGianRa = ?, trangThaiThanhToan =?  " 
					+ "WHERE maHD = ? ";
			stmt = con.prepareStatement(sql);
			stmt.setString(7, hoaDon.getMaHoaDon());
			stmt.setString(1, hoaDon.getNhanVienBan().getMaNV());
			stmt.setString(2, hoaDon.getKhachHang().getMaKhachHang());
			stmt.setString(3, hoaDon.getBan().getMaBan());
			// Chuyển LocalDateTime sang Timestamp
			stmt.setTimestamp(4, Timestamp.valueOf(hoaDon.getThoiGianVao()));

			if (hoaDon.getThoiGianRa() != null) {
				stmt.setTimestamp(5, Timestamp.valueOf(hoaDon.getThoiGianRa()));
			} else {
				stmt.setNull(5, Types.TIMESTAMP);
			}

			stmt.setBoolean(6, hoaDon.isTrangThaiThanhToan());

			int rows = stmt.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	 public boolean xoaHoaDon(String maHoaDon) {
        Connection con = ConnectDB.getConnection();
        String sqlDeleteChiTiet = "DELETE FROM ChiTietHoaDon WHERE maHD = ?";
        String sqlDeleteHoaDon = "DELETE FROM HoaDon WHERE maHD = ?";

        try (PreparedStatement psChiTiet = con.prepareStatement(sqlDeleteChiTiet);
             PreparedStatement psHoaDon = con.prepareStatement(sqlDeleteHoaDon)) {
            
            con.setAutoCommit(false); 

            psChiTiet.setString(1, maHoaDon);
            psChiTiet.executeUpdate();

            psHoaDon.setString(1, maHoaDon);
            int rowsAffected = psHoaDon.executeUpdate();
            
            con.commit(); 
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Lỗi xóa hóa đơn: " + e.getMessage());
            try {
                con.rollback(); 
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
             try {
                 if (con != null) con.setAutoCommit(true);
             } catch (SQLException ex) {
                 ex.printStackTrace();
             }
        }
}
	 }
