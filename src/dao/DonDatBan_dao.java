package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.Ban;
import entity.DonDatBan;
import entity.KhachHang;

public class DonDatBan_dao {
	public ArrayList<DonDatBan> getAllDonDatBan() {
		ArrayList<DonDatBan> ds = new ArrayList<DonDatBan>();
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		PreparedStatement stmt = null;

		try {
			String sql = "SELECT * FROM DonDatBan";
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String ma = rs.getString("maDonDatBan");
				KhachHang kh = new KhachHang(rs.getString("maKH"));
				Ban ban = new Ban(rs.getString("maBan"));
				// read timestamp and convert to LocalDateTime (handle nulls)
				Timestamp ts = rs.getTimestamp("thoiGian");
				LocalDateTime thoiGian = (ts != null) ? ts.toLocalDateTime() : null;
				DonDatBan ddb = new DonDatBan(ma, kh, ban, thoiGian);
				ds.add(ddb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}

}