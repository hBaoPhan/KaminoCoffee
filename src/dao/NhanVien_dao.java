package dao;

import java.sql.*;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.NhanVien;
import entity.ChucVu;

public class NhanVien_dao {
    private Connection con;

    public NhanVien_dao() {
        con = ConnectDB.getInstance().getConnection();
    }

    // ======================================================
    // LẤY DANH SÁCH NHÂN VIÊN
    // ======================================================
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
                String chucVuStr = rs.getString("ChucVu"); // Lấy mã chức vụ (ví dụ: "NV", "QL") từ DB

                ChucVu chucVu;
                try {
                    // Chuyển mã từ DB thành Enum ChucVu (ví dụ: "NV" -> ChucVu.NHAN_VIEN)
                    chucVu = ChucVu.fromString(chucVuStr);
                } catch (IllegalArgumentException e) {
                    // Xử lý trường hợp giá trị trong DB bị sai/lỗi
                    System.err.println("⚠️ Giá trị chức vụ không hợp lệ trong DB: " + chucVuStr + ". Đã gán mặc định.");
                    chucVu = ChucVu.NHAN_VIEN; // Gán mặc định là NHAN_VIEN nếu giá trị DB sai
                }

                // Đối tượng NhanVien lưu trữ đối tượng Enum ChucVu
                NhanVien nv = new NhanVien(maNV, tenNV, sdt, gioiTinh, chucVu);
                dsnv.add(nv);

                // Ghi chú: Khi bạn hiển thị nhân viên này lên UI/Bảng, 
                // bạn chỉ cần gọi nv.getChucVu().getTenHienThi() hoặc nv.getChucVu().toString() 
                // để lấy tên tiếng Việt (Nhân Viên / Quản Lý).
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsnv;
    }




    // ======================================================
    // THÊM NHÂN VIÊN
    // ======================================================
 // ======================================================
 // THÊM NHÂN VIÊN
 // ======================================================
 public boolean themNV(NhanVien nv) {
     try {
         String sql = "INSERT INTO NhanVien (MaNV, TenNV, GioiTinh, ChucVu, SDT) VALUES (?, ?, ?, ?, ?)";
         PreparedStatement pst = con.prepareStatement(sql);
         pst.setString(1, nv.getMaNV());
         pst.setString(2, nv.getTenNV());
         pst.setBoolean(3, nv.isGioiTinh());
         pst.setString(4, nv.getChucVu().toDatabaseValue());
         pst.setString(5, nv.getsDT());

         int rows = pst.executeUpdate();
         pst.close();
         return rows > 0;
     } catch (SQLException e) {
         e.printStackTrace();
         return false;
     }
 }

 // ======================================================
 // SỬA NHÂN VIÊN
 // ======================================================
 public boolean suaNV(NhanVien nv) {
     try {
         String sql = "UPDATE NhanVien SET TenNV = ?, GioiTinh = ?, ChucVu = ?, SDT = ? WHERE MaNV = ?";
         PreparedStatement pst = con.prepareStatement(sql);
         pst.setString(1, nv.getTenNV());
         pst.setBoolean(2, nv.isGioiTinh());
         // Lưu tên hiển thị luôn
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

    // ======================================================
    // XÓA NHÂN VIÊN
    // ======================================================
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

    // ======================================================
    // TÌM NHÂN VIÊN THEO MÃ
    // ======================================================
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

    // ======================================================
    // TÌM NHÂN VIÊN THEO TÊN
    // ======================================================
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

    // ======================================================
    // TÌM NHÂN VIÊN THEO SDT
    // ======================================================
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

    // ======================================================
    // TÌM NHÂN VIÊN THEO CHỨC VỤ
    // ======================================================
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

    // ======================================================
    // HÀM ÁNH XẠ RESULTSET -> NHÂN VIÊN
    // ======================================================
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