package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.Ban;
import entity.DonDatBan;
import entity.KhachHang;

public class DonDatBan_dao {
	private Connection con;
	private KhachHang_dao kh_dao;
	private Ban_dao ban_dao;

	public DonDatBan_dao() {
		con = ConnectDB.getInstance().getConnection();
		this.kh_dao = new KhachHang_dao();
        this.ban_dao = new Ban_dao();
	}

	private void checkConnection() throws SQLException {
		if (con == null || con.isClosed()) {
			con = ConnectDB.getInstance().getConnection();
		}
	}

	public ArrayList<DonDatBan> getAllDonDatBan() {
		ArrayList<DonDatBan> ds = new ArrayList<DonDatBan>();
		
		try {
			checkConnection();
			try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM DonDatBan");
				 ResultSet rs = stmt.executeQuery()) {
				
				while (rs.next()) {
					String ma = rs.getString("maDonDatBan");
					KhachHang kh = new KhachHang(rs.getString("maKH"));
					Ban ban = new Ban(rs.getString("maBan"));
					Timestamp ts = rs.getTimestamp("thoiGian");
					LocalDateTime thoiGian = (ts != null) ? ts.toLocalDateTime() : null;
					boolean daNhan=rs.getBoolean("daNhan");
					DonDatBan ddb = new DonDatBan(ma, kh, ban, thoiGian,daNhan);
					ds.add(ddb);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}

	public boolean addDonDatBan(DonDatBan ddb) {
		String sql = "INSERT INTO DonDatBan (maDonDatBan, maKH, maBan, thoiGian,daNhan) VALUES (?, ?, ?, ?,?)";
		try {
			checkConnection();
			try (PreparedStatement stmt = con.prepareStatement(sql)) {
				stmt.setString(1, ddb.getMaDonDatBan());
				stmt.setString(2, ddb.getKhachHang().getMaKhachHang());
				stmt.setString(3, ddb.getBan().getMaBan());
				stmt.setBoolean(5, ddb.isDaNhan());
				if (ddb.getThoiGian() != null) {
					stmt.setTimestamp(4, Timestamp.valueOf(ddb.getThoiGian()));
				} else {
					stmt.setTimestamp(4, null);
				}
				int rowsInserted = stmt.executeUpdate();
				return rowsInserted > 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	public boolean updateDonDatBan(DonDatBan donDatBan) {
        String sql = "UPDATE DonDatBan SET maBan= ?, thoiGian= ?,daNhan= ? WHERE maDonDatBan= ?";
        try {
            checkConnection();
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, donDatBan.getBan().getMaBan());
                stmt.setTimestamp(2, Timestamp.valueOf(donDatBan.getThoiGian()));
                stmt.setBoolean(3, donDatBan.isDaNhan()); 
                stmt.setString(4, donDatBan.getMaDonDatBan());

                int rowsUpdated = stmt.executeUpdate();
                return rowsUpdated > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	public boolean deleteDonDatBan(String maDonDatBan) {
	    String sql = "DELETE FROM DonDatBan WHERE maDonDatBan = ?";
	    try {
            checkConnection();
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
	            stmt.setString(1, maDonDatBan);
	            int rowsDeleted = stmt.executeUpdate();
	            return rowsDeleted > 0;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
    
    // ✅ Sửa: Hàm countToday()
    public int countToday() {
        String sql = "SELECT COUNT(*) FROM DonDatBan WHERE CAST(thoiGian AS DATE) = CAST(GETDATE() AS DATE);";
        try {
            checkConnection();
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public ArrayList<DonDatBan> getRecentDonDatBan(int limit) {
        ArrayList<DonDatBan> dsDDB = new ArrayList<>();
        

        String sql = "SELECT TOP " + limit + " * FROM DonDatBan ORDER BY thoiGian DESC";
        

//        String sql = "SELECT * FROM DonDatBan ORDER BY thoiGian DESC LIMIT " + limit;

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DonDatBan ddb = new DonDatBan();
                
                // === SỬA LẠI CÁCH ĐỌC DỮ LIỆU ===
                
                // 1. Lấy mã đơn đặt bàn
                // (Giả sử tên cột là 'maDonDatBan', 'maKH', 'maBan', 'thoiGian', 'daNhan')
                ddb.setMaDonDatBan(rs.getString("maDonDatBan"));
                
                // 2. Lấy Khách Hàng (cần có khachHang_dao)
                ddb.setKhachHang(kh_dao.timTheoMa(rs.getString("maKH"))); 
                
                // 3. Lấy Bàn (cần có ban_dao)
                ddb.setBan(ban_dao.timTheoMa(rs.getString("maBan")));
                
                // 4. Lấy LocalDateTime (dùng getTimestamp)
                ddb.setThoiGian(rs.getTimestamp("thoiGian").toLocalDateTime());
                
                // 5. Lấy trạng thái
                ddb.setDaNhan(rs.getBoolean("daNhan"));

                // (Các trường cũ như soLuongKhach, NhanVien, trangThai... không còn)

                dsDDB.add(ddb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsDDB;
    }

}