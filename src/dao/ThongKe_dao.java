package dao;

import java.sql.*;
import java.util.*;
import connectDB.ConnectDB;

public class ThongKe_dao {

	/**
	 * Lấy doanh thu theo từng tháng trong năm hiện tại (SQL Server version).
	 */
	public Map<Integer, Double> getDoanhThuTheoThang() {
		Map<Integer, Double> map = new LinkedHashMap<>();

		String sql = """
				    SELECT MONTH(hd.thoiGianVao) AS thang,
				           SUM(ct.tongTien) AS doanhThu
				    FROM HoaDon hd
				    JOIN ChiTietHoaDon ct ON hd.maHD = ct.maHD
				    WHERE YEAR(hd.thoiGianVao) = YEAR(GETDATE())
				    GROUP BY MONTH(hd.thoiGianVao)
				    ORDER BY thang;
				""";

		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int thang = rs.getInt("thang");
				double doanhThu = rs.getDouble("doanhThu");
				map.put(thang, doanhThu);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * Tổng doanh thu toàn bộ.
	 */
	public double getTongDoanhThu() {
		String sql = "SELECT ISNULL(SUM(tongTien), 0) FROM ChiTietHoaDon";
		ConnectDB.getInstance();
		PreparedStatement stmt = null;
		Connection con = ConnectDB.getConnection();
		try {
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			if (rs.next())
				return rs.getDouble(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Tổng số hóa đơn.
	 */
	public int getTongSoHoaDon() {
		String sql = "SELECT COUNT(*) FROM HoaDon";
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			if (rs.next())
				return rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Sản phẩm bán chạy nhất (theo số lượng) — SQL Server version.
	 */
	public String getSanPhamBanChayNhat() {
		String sql = """
				    SELECT TOP 1 sp.tenSP, SUM(ct.soLuong) AS tongSL
				    FROM ChiTietHoaDon ct
				    JOIN SanPham sp ON sp.maSP = ct.maSP
				    GROUP BY sp.tenSP
				    ORDER BY tongSL DESC;
				""";

		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			if (rs.next())
				return rs.getString("tenSP");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Chưa có dữ liệu";
	}

	public int getTongSoKhachHang() {
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		PreparedStatement stmt = null;
		String sql = "SELECT COUNT(*) FROM KhachHang";
		try {
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				return rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Map<String, Double> getTiLeLoaiSanPham() {
		Map<String, Double> map = new LinkedHashMap<>();
		String sql = "SELECT LoaiSanPham, COUNT(*) AS SoLuong FROM SanPham GROUP BY LoaiSanPham";
		double total = 0;
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			// Tạm lưu số lượng từng loại để tính tổng
			Map<String, Integer> temp = new LinkedHashMap<>();

			while (rs.next()) {
				String loai = rs.getString("LoaiSanPham");
				int soLuong = rs.getInt("SoLuong");
				temp.put(loai, soLuong);
				total += soLuong;
			}

			// Tính phần trăm từng loại
			for (Map.Entry<String, Integer> entry : temp.entrySet()) {
				double tiLe = (entry.getValue() * 100.0) / total;
				map.put(entry.getKey(), tiLe);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}
	public double getDoanhThuHomNay() {
        String sql = """
           SELECT ISNULL(SUM(ct.tongTien), 0)
        	FROM ChiTietHoaDon ct
        	JOIN HoaDon hd ON hd.maHD = ct.maHD
        	WHERE CAST(hd.thoiGianVao AS DATE) = CAST(GETDATE() AS DATE);

        """;
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement ps=null;
		try {
              ps= con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
