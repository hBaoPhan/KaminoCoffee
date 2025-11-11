package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import connectDB.ConnectDB;
import entity.KhachHang;

public class KhachHang_dao {
    private Connection con;

    public KhachHang_dao() {
        con = ConnectDB.getInstance().getConnection();
    }

    // 🔹 Hàm dùng chung để chuyển ResultSet -> KhachHang
    private KhachHang mapResultSetToKhachHang(ResultSet rs) throws SQLException {
        String ma = rs.getString("maKH");
        String ten = rs.getString("tenKH");
        String sdt = rs.getString("sDT");
        int diem = rs.getInt("diemTichLuy");
        boolean laKHDK = rs.getBoolean("laKHDK");
        return new KhachHang(ma, ten, sdt, diem, laKHDK);
    }

    // ✅ Lấy danh sách tất cả khách hàng
    public ArrayList<KhachHang> getAllKhachHang() {
        ArrayList<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ds.add(mapResultSetToKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // ✅ Thêm khách hàng mới (có kiểm tra trùng SDT)
    public boolean addKhachHang(KhachHang kh) {
        // 🔍 Kiểm tra trùng SDT trước
        KhachHang tonTai = timTheoSDT(kh.getsDT());
        if (tonTai != null) {
        	if(!tonTai.getsDT().isEmpty()) {
        		 JOptionPane.showMessageDialog(null,
        	                "⚠️ Số điện thoại này đã được sử dụng bởi khách hàng: " + tonTai.getTenKhachHang());
        	            return false;
        	}
           
        }

        String sql = "INSERT INTO KhachHang (maKH, tenKH, sDT, diemTichLuy, laKHDK) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, kh.getMaKhachHang());
            stmt.setString(2, kh.getTenKhachHang());
            stmt.setString(3, kh.getsDT());
            stmt.setInt(4, kh.getDiemTichLuy());
            stmt.setBoolean(5, kh.isLaKHDK());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || e.getMessage().toLowerCase().contains("duplicate")) {
                JOptionPane.showMessageDialog(null, "⚠️  Mã khách hàng đã tồn tại, vui lòng nhập mã khác!");
                return false;
            }

            if (e.getMessage().toLowerCase().contains("check constraint") ||
                e.getMessage().toLowerCase().contains("cannot be null")) {
                JOptionPane.showMessageDialog(null, "⚠️ Dữ liệu nhập không hợp lệ (vi phạm ràng buộc)!");
                return false;
            }

            JOptionPane.showMessageDialog(null, "⚠️ Lỗi SQL khi thêm khách hàng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Sửa thông tin khách hàng (kiểm tra SDT trùng với người khác)
    public boolean suaKhachHang(KhachHang kh) {
        // 🔍 Kiểm tra trùng SDT với KH khác
    	KhachHang khTrung = timTheoSDT(kh.getsDT());
    	if (khTrung != null) {
    	    if (!khTrung.getsDT().isEmpty()) {
    	        if (khTrung.getMaKhachHang().equals(kh.getMaKhachHang())) {
    	            JOptionPane.showMessageDialog(null,
    	                "⚠️ Số điện thoại này đã được sử dụng bởi khách hàng khác (" + khTrung.getTenKhachHang() + ")");
    	            return false;
    	        }
    	    }
    	}

        String sql = "UPDATE KhachHang SET tenKH = ?, sDT = ?, diemTichLuy = ?, laKHDK = ? WHERE maKH = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, kh.getTenKhachHang());
            stmt.setString(2, kh.getsDT());
            stmt.setInt(3, kh.getDiemTichLuy());
            stmt.setBoolean(4, kh.isLaKHDK());
            stmt.setString(5, kh.getMaKhachHang());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "⚠️ Lỗi khi sửa khách hàng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean suaDiemTichLuyKhachHang(KhachHang kh) {
        // 🔍 Kiểm tra trùng SDT với KH khác
    	

        String sql = "UPDATE KhachHang SET diemTichLuy = ? WHERE maKH = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, kh.getDiemTichLuy());
 
            stmt.setString(2, kh.getMaKhachHang());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "⚠️ Lỗi khi sửa khách hàng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean coLienKetVoiHoaDonHoacDonDatBan(String maKH) {
        Connection con = ConnectDB.getInstance().getConnection();
        String sql = """
            SELECT COUNT(*) AS count FROM HoaDon WHERE maKH = ?
            UNION ALL
            SELECT COUNT(*) AS count FROM DonDatBan WHERE maKH = ?
        """;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maKH);
            stmt.setString(2, maKH);
            ResultSet rs = stmt.executeQuery();

            int tong = 0;
            while (rs.next()) {
                tong += rs.getInt("count");
            }
            return tong > 0; // true nếu có hóa đơn hoặc đơn đặt bàn
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // nếu lỗi thì tạm không cho xóa để tránh mất dữ liệu
        }
    }

    // ✅ Xóa khách hàng theo mã
    public boolean xoaKhachHang(String maKH) {
        String sql = "DELETE FROM KhachHang WHERE maKH = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maKH);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "⚠️ Lỗi khi xóa khách hàng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Tìm khách hàng theo tên (trả về danh sách)
    public ArrayList<KhachHang> timTheoTen(String ten) {
        ArrayList<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE tenKH LIKE ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, "%" + ten + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ds.add(mapResultSetToKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // ✅ Tìm khách hàng theo số điện thoại
    public KhachHang timTheoSDT(String sdt) {
        String sql = "SELECT * FROM KhachHang WHERE sDT = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, sdt);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToKhachHang(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Tìm khách hàng theo mã
    public KhachHang timTheoMa(String maKH) {
        String sql = "SELECT * FROM KhachHang WHERE maKH = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maKH);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToKhachHang(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
