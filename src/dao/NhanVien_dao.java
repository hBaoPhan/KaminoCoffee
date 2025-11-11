package dao;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import connectDB.ConnectDB;
import entity.NhanVien;
import entity.ChucVu;

public class NhanVien_dao {
    private Connection con;

    public NhanVien_dao() {
        con = ConnectDB.getInstance().getConnection();
    }
    // LẤY DANH SÁCH NHÂN VIÊN
    public ArrayList<NhanVien> getDsnv() {
        ArrayList<NhanVien> dsnv = new ArrayList<>();

        try {
            String sql = "SELECT * FROM NhanVien";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String maNV = rs.getString("MaNV");
                String tenNV = rs.getString("TenNV");
                boolean gioiTinh = rs.getBoolean("GioiTinh");
                String sdt = rs.getString("SDT");
                String chucVuStr = rs.getString("ChucVu");
                ChucVu chucVu;
                try {
                    chucVu = ChucVu.fromString(chucVuStr);
                } catch (IllegalArgumentException e) {
                    System.err.println("⚠️ Giá trị chức vụ không hợp lệ trong DB: " + chucVuStr + ". Đã gán mặc định.");
                    chucVu = ChucVu.NHAN_VIEN;
                }
                NhanVien nv = new NhanVien(maNV, tenNV, sdt, gioiTinh, chucVu);
                dsnv.add(nv);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsnv;
    }
 // THÊM NHÂN VIÊN
    public boolean themNV(NhanVien nv) {
        Connection con = ConnectDB.getInstance().getConnection();
        String sql = "INSERT INTO NhanVien (maNV, tenNV, sDT, gioiTinh, chucVu) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nv.getMaNV());
            stmt.setString(2, nv.getTenNV());
            stmt.setString(3, nv.getsDT());
            stmt.setBoolean(4, nv.isGioiTinh());
            stmt.setString(5, nv.getChucVu().name());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Lỗi trùng khóa chính
            if (e.getErrorCode() == 1062 || e.getMessage().toLowerCase().contains("duplicate")) {
                JOptionPane.showMessageDialog(null, "⚠️ Mã nhân viên đã tồn tại, vui lòng nhập mã khác!");
                return false;
            }
            // Lỗi vi phạm CHECK constraint
            if (e.getMessage().toLowerCase().contains("check constraint")) {
                JOptionPane.showMessageDialog(null, "⚠️ Dữ liệu nhập không hợp lệ (vi phạm ràng buộc)!");
                return false;
            }
            // Các lỗi SQL khác
            JOptionPane.showMessageDialog(null, "⚠️ Lỗi SQL: " + e.getMessage());
            return false;
        }
    }
 // SỬA NHÂN VIÊN
 public boolean suaNV(NhanVien nv) {
     try {
         String sql = "UPDATE NhanVien SET TenNV = ?, GioiTinh = ?, ChucVu = ?, SDT = ? WHERE MaNV = ?";
         PreparedStatement pst = con.prepareStatement(sql);
         pst.setString(1, nv.getTenNV());
         pst.setBoolean(2, nv.isGioiTinh());
         pst.setString(3, nv.getChucVu().toDatabaseValue());
         pst.setString(4, nv.getsDT());
         pst.setString(5, nv.getMaNV());
         int rows = pst.executeUpdate();
         pst.close();
         return rows > 0;
     } catch (SQLException e) {
         e.printStackTrace();
         return false;
     }
 }
    // XÓA NHÂN VIÊN
    public boolean xoaNV(String maNV) {
        try {
            String sql = "DELETE FROM NhanVien WHERE MaNV = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, maNV);
            int rows = pst.executeUpdate();
            pst.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // TÌM NHÂN VIÊN THEO MÃ
    public NhanVien timNV(String maNV) {
        NhanVien nv = null;
        try {
            String sql = "SELECT * FROM NhanVien WHERE MaNV = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, maNV);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                nv = mapNhanVien(rs);
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nv;
    }
    // TÌM NHÂN VIÊN THEO TÊN
    public ArrayList<NhanVien> timNVTheoTen(String tenNV) {
        ArrayList<NhanVien> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM NhanVien WHERE TenNV LIKE ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, "%" + tenNV + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(mapNhanVien(rs));
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    // TÌM NHÂN VIÊN THEO SDT
    public NhanVien timNVTheoSDT(String sdt) {
        NhanVien nv = null;
        try {
            String sql = "SELECT * FROM NhanVien WHERE SDT = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, sdt);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                nv = mapNhanVien(rs);
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nv;
    }
    // TÌM NHÂN VIÊN THEO CHỨC VỤ
    public ArrayList<NhanVien> timNVTheoChucVu(ChucVu chucVu) {
        ArrayList<NhanVien> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM NhanVien WHERE ChucVu = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, chucVu.name());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(mapNhanVien(rs));
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    // HÀM ÁNH XẠ RESULTSET -> NHÂN VIÊN
    private NhanVien mapNhanVien(ResultSet rs) throws SQLException {
        String maNV = rs.getString("MaNV");
        String tenNV = rs.getString("TenNV");
        boolean gioiTinh = rs.getBoolean("GioiTinh");
        String sdt = rs.getString("SDT");
        String chucVuStr = rs.getString("ChucVu");
        ChucVu chucVu = ChucVu.fromString(chucVuStr);

        return new NhanVien(maNV, tenNV, sdt, gioiTinh, chucVu);
    }
}