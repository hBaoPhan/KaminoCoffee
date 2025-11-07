package entity;

public enum ChucVu {
    NhanVien("Nhân Viên"),
    QuanLy("Quản Lý");

    private final String tenHienThi;

    // Constructor
    private ChucVu(String tenHienThi) {
        this.tenHienThi = tenHienThi;
    }

    // Lấy tên hiển thị (để show ra UI)
    public String getTenHienThi() {
        return tenHienThi;
    }

    // Chuyển từ chuỗi trong DB sang Enum (dù có dấu hay không)
    public static ChucVu fromString(String text) {
        if (text == null) return null;
        for (ChucVu cv : ChucVu.values()) {
            if (cv.name().equalsIgnoreCase(text) || cv.tenHienThi.equalsIgnoreCase(text))
                return cv;
        }
        throw new IllegalArgumentException("Không tồn tại chức vụ: " + text);
    }
}
