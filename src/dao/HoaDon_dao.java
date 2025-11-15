package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
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

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return ds;
	}
	// Trong lớp HoaDon_dao

	public ArrayList<Object[]> getAllHoaDonChoPanel() {
		ArrayList<Object[]> danhSach = new ArrayList<>();

		String sql = "SELECT HD.maHD, KH.tenKH, B.tenBan, KH.sDT, "
	            + " FORMAT(HD.thoiGianVao, 'dd-MM-yyyy') AS Ngay, " // Đổi DATE_FORMAT thành FORMAT
	            + " (CASE WHEN KH.laKHDK = 1 THEN 'Có' ELSE 'Không' END) AS laKHDK, "
	            + " ISNULL(SUM(CT.soLuong * SP.gia), 0) AS TongTien, " // Đổi IFNULL thành ISNULL
	            + " HD.trangThaiThanhToan "
	            + " FROM HoaDon HD "
	            + " LEFT JOIN KhachHang KH ON HD.maKH = KH.maKH "
	            + " JOIN Ban B ON HD.maBan = B.maBan "
	            + " LEFT JOIN ChiTietHoaDon CT ON HD.maHD = CT.maHD "
	            + " LEFT JOIN SanPham SP ON CT.maSP = SP.maSP "
	            + " GROUP BY HD.maHD, KH.tenKH, B.tenBan, KH.sDT, "
	            + "     FORMAT(HD.thoiGianVao, 'dd-MM-yyyy'), " // Bắt buộc lặp lại, không dùng alias 'Ngay'
	            + "     (CASE WHEN KH.laKHDK = 1 THEN 'Có' ELSE 'Không' END), " // Bắt buộc lặp lại
	            + "     HD.trangThaiThanhToan "
	            + " ORDER BY HD.maHD DESC";

		Connection con = ConnectDB.getConnection();

		try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {

				String trangThaiStr;
				boolean isPaid = rs.getBoolean("trangThaiThanhToan");
				trangThaiStr = isPaid ? "Đã thanh toán" : "Chờ thanh toán";

				String khdkStr = rs.getString("laKHDK");

				double tongTienDouble = rs.getDouble("TongTien");

				// Thêm vào danh sách (8 cột)
				danhSach.add(new Object[] { rs.getString("maHD"), rs.getString("tenKH"), rs.getString("tenBan"),
						rs.getString("sDT"), rs.getString("Ngay"), // Cột 4: Ngày
						khdkStr, // Cột 5: KHDK
						tongTienDouble, // Cột 6: Tổng tiền
						trangThaiStr // Cột 7: Trạng thái
				});
			}
		} catch (SQLException e) {
			System.err.println("Lỗi truy vấn tất cả hóa đơn cho Panel: " + e.getMessage());
		}
		return danhSach;
	}public ArrayList<Object[]> getHoaDonByNgay(LocalDate ngay) {
        ArrayList<Object[]> dsHoaDon = new ArrayList<>();
        
        LocalDateTime startOfDay = ngay.atStartOfDay(); 
        LocalDateTime startOfNextDay = ngay.plusDays(1).atStartOfDay(); 
        ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		PreparedStatement ps=null;
		String sql="SELECT HD.maHD, KH.tenKH, B.tenBan, KH.sDT, "
	            + " FORMAT(HD.thoiGianVao, 'dd-MM-yyyy') AS Ngay, "
	            + " (CASE WHEN KH.laKHDK = 1 THEN 'Có' ELSE 'Không' END) AS laKHDK, "
	            + " ISNULL(SUM(CT.soLuong * SP.gia), 0) AS TongTien, "
	            + " HD.trangThaiThanhToan "
	            + " FROM HoaDon HD "
	            + " LEFT JOIN KhachHang KH ON HD.maKH = KH.maKH "
	            + " JOIN Ban B ON HD.maBan = B.maBan "
	            + " LEFT JOIN ChiTietHoaDon CT ON HD.maHD = CT.maHD "
	            + " LEFT JOIN SanPham SP ON CT.maSP = SP.maSP "
	            // Điều kiện WHERE (so sánh Date Range) vẫn giữ nguyên
	            + " WHERE HD.thoiGianVao >= ? AND HD.thoiGianVao < ? " 
	            + " GROUP BY HD.maHD, KH.tenKH, B.tenBan, KH.sDT, "
	            + "     FORMAT(HD.thoiGianVao, 'dd-MM-yyyy'), " // Bắt buộc lặp lại
	            + "     (CASE WHEN KH.laKHDK = 1 THEN 'Có' ELSE 'Không' END), " // Bắt buộc lặp lại
	            + "     HD.trangThaiThanhToan "
	            + " ORDER BY HD.maHD DESC";
        try {
             ps= con.prepareStatement(sql) ;

            ps.setTimestamp(1, java.sql.Timestamp.valueOf(startOfDay));
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(startOfNextDay));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                	boolean isPaid = rs.getBoolean("trangThaiThanhToan");
    				String trangThaiStr = isPaid ? "Đã thanh toán" : "Chờ thanh toán";
                    dsHoaDon.add(new Object[]{
                        rs.getString("maHD"),
                        rs.getString("tenKH"),
                        rs.getString("tenBan"),
                        rs.getString("sDT"),
                        rs.getString("Ngay"), 
                        rs.getString("laKHDK"), 
                        rs.getDouble("TongTien"),
                       trangThaiStr
                    });
                }
            }
        } catch (SQLException e) {
            // Rất quan trọng: Nếu có lỗi SQL, nó sẽ in ra ở đây
            e.printStackTrace(); 
        }
        return dsHoaDon;
    }


	public boolean insertHoaDon(HoaDon hoaDon) {
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		PreparedStatement stmt = null;
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
		}
		return null;
	}

	public boolean updateHoaDon(HoaDon hoaDon) {
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		PreparedStatement stmt = null;
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
				if (con != null)
					con.setAutoCommit(true);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public int countToday() {
		String sql = "SELECT COUNT(*)" + "	FROM HoaDon"
				+ " WHERE CAST(thoiGianVao AS DATE) = CAST(GETDATE() AS DATE);";
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			if (rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
}
