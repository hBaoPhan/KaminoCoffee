package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.KhachHang;

public class KhachHang_dao {

    // ✅ Lấy danh sách tất cả khách hàng
    public ArrayList<KhachHang> getAllKhachHang() {
        ArrayList<KhachHang> ds = new ArrayList<>();
        Connection con = ConnectDB.getInstance().getConnection();

        String sql = "SELECT * FROM KhachHang";
        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String ma = rs.getString("maKH");
                String ten = rs.getString("tenKH");
                String sdt = rs.getString("sDT");
                int diem = rs.getInt("diemTichLuy");
                boolean laKHDK = rs.getBoolean("laKHDK");

                ds.add(new KhachHang(ma, ten, sdt, diem, laKHDK));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // ✅ Thêm khách hàng mới
    public boolean addKhachHang(KhachHang kh) {
        Connection con = ConnectDB.getInstance().getConnection();
        String sql = "INSERT INTO KhachHang (maKH, tenKH, sDT, diemTichLuy, laKHDK) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, kh.getMaKhachHang());
            stmt.setString(2, kh.getTenKhachHang());
            stmt.setString(3, kh.getsDT());
            stmt.setInt(4, kh.getDiemTichLuy());
            stmt.setBoolean(5, kh.isLaKHDK());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Sửa thông tin khách hàng
    public boolean suaKhachHang(KhachHang kh) {
        Connection con = ConnectDB.getInstance().getConnection();
        String sql = "UPDATE KhachHang SET tenKH = ?, sDT = ?, diemTichLuy = ?, laKHDK = ? WHERE maKH = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, kh.getTenKhachHang());
            stmt.setString(2, kh.getsDT());
            stmt.setInt(3, kh.getDiemTichLuy());
            stmt.setBoolean(4, kh.isLaKHDK());
            stmt.setString(5, kh.getMaKhachHang());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Xóa khách hàng theo mã
    public boolean xoaKhachHang(String maKH) {
        Connection con = ConnectDB.getInstance().getConnection();
        String sql = "DELETE FROM KhachHang WHERE maKH = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maKH);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Tìm khách hàng theo tên (trả về nhiều kết quả)
    public ArrayList<KhachHang> timTheoTen(String ten) {
        ArrayList<KhachHang> ds = new ArrayList<>();
        Connection con = ConnectDB.getInstance().getConnection();
        String sql = "SELECT * FROM KhachHang WHERE tenKH LIKE ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, "%" + ten + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String ma = rs.getString("maKH");
                String tenKH = rs.getString("tenKH");
                String sdt = rs.getString("sDT");
                int diem = rs.getInt("diemTichLuy");
                boolean laKHDK = rs.getBoolean("laKHDK");

                ds.add(new KhachHang(ma, tenKH, sdt, diem, laKHDK));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // ✅ Tìm khách hàng theo số điện thoại (chính xác 1 hoặc 0)
    public KhachHang timTheoSDT(String sdt) {
        Connection con = ConnectDB.getInstance().getConnection();
        String sql = "SELECT * FROM KhachHang WHERE sDT = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, sdt);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String ma = rs.getString("maKH");
                String ten = rs.getString("tenKH");
                int diem = rs.getInt("diemTichLuy");
                boolean laKHDK = rs.getBoolean("laKHDK");

                return new KhachHang(ma, ten, sdt, diem, laKHDK);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Tìm khách hàng theo mã (1 hoặc null)
    public KhachHang timTheoMa(String maKH) {
        Connection con = ConnectDB.getInstance().getConnection();
        String sql = "SELECT * FROM KhachHang WHERE maKH = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maKH);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String ten = rs.getString("tenKH");
                String sdt = rs.getString("sDT");
                int diem = rs.getInt("diemTichLuy");
                boolean laKHDK = rs.getBoolean("laKHDK");

                return new KhachHang(maKH, ten, sdt, diem, laKHDK);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
