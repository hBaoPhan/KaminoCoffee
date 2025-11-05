-- ========================================================
-- DATABASE: QUANLYQUANCOFFEE
-- ========================================================
CREATE DATABASE QUANLYKAMINOCOFFEE;
GO
USE QUANLYKAMINOCOFFEE;
GO

-- ========================================================
-- B?NG NHÂN VIÊN
-- ========================================================
CREATE TABLE NhanVien (
    maNV VARCHAR(20) PRIMARY KEY,
    tenNV NVARCHAR(100) NOT NULL,
    chucVu NVARCHAR(10) CHECK (chucVu IN (N'NV', N'QL')) NOT NULL, -- Gi? nguyên 'NV', 'QL' t? enum ChucVu
    sDT VARCHAR(15)
);
GO

-- ========================================================
-- B?NG TÀI KHO?N (ÐÃ S?A)
-- S?a: B? c?t chucVu và tenNV (đã có ? b?ng NhanVien)
-- S?a: Thêm UNIQUE(maNV) đ? đ?m b?o quan h? 1-1 nhu UML
-- ========================================================
CREATE TABLE TaiKhoan (
    tenDangNhap VARCHAR(50) PRIMARY KEY,
    matKhau VARCHAR(255) NOT NULL,
    maNV VARCHAR(20) NOT NULL, -- Ð? là NOT NULL cho quan h? 1-1
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
        ON DELETE CASCADE -- N?u xóa NV thì xóa luôn TK
        ON UPDATE CASCADE,
    UNIQUE(maNV) -- Đ?m b?o m?i nhân viên ch? có 1 tài kho?n (quan h? 1-1)
);
GO

-- ========================================================
-- B?NG KHÁCH HÀNG
-- ========================================================
CREATE TABLE KhachHang (
    maKH VARCHAR(20) PRIMARY KEY,
    tenKH NVARCHAR(100),
    sDT VARCHAR(15),
    diemTichLuy int,
    laKHDK bit not null
);
GO

-- ========================================================
-- B?NG BÀN (ÐÃ S?A)
-- S?a: Đ?i trangThai t? BIT sang NVARCHAR đ? kh?p v?i enum TrangThaiBan (3 tr?ng thái)
-- ========================================================
CREATE TABLE Ban (
    maBan VARCHAR(20) PRIMARY KEY,
    tenBan NVARCHAR(100),
    soGhe INT CHECK (soGhe >= 0),
    trangThai NVARCHAR(20) 
        CHECK (trangThai IN (N'Trống', N'Đang được sử dụng', N'Đã được đặt')) 
        DEFAULT N'Trống'
);
GO

-- ========================================================
-- B?NG S?N PH?M (ÐÃ S?A)
-- S?a: B? c?t soLuong và thêm c?t loaiSanPham nhu trong UML
-- ========================================================
CREATE TABLE SanPham (
    maSP VARCHAR(20) PRIMARY KEY,
    tenSP NVARCHAR(100),
    gia FLOAT CHECK (gia >= 0),
    loaiSanPham NVARCHAR(100) -- Thêm c?t này t? UML
        CHECK (loaiSanPham IN('Trà','Cafe','Bánh','Trà Sữa'))
    -- B? c?t soLuong (không có trong UML)
);
GO

-- ========================================================
-- B?NG HÓA ÐON (ÐÃ S?A)
-- S?a: Đ?i ngayLap -> thoiGianVao (DATETIME) và thêm thoiGianRa (DATETIME)
-- ========================================================
CREATE TABLE HoaDon (
    maHD VARCHAR(20) PRIMARY KEY,
    maNV VARCHAR(20),
    maKH VARCHAR(20),
    maBan VARCHAR(20),
    thoiGianVao DATETIME DEFAULT (GETDATE()), -- S?a t? ngayLap (DATE)
    thoiGianRa DATETIME NULL, -- Thêm c?t này t? UML
    trangThaiThanhToan Bit,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    FOREIGN KEY (maBan) REFERENCES Ban(maBan)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);
GO

-- ========================================================
-- B?NG CHI TI?T HÓA ÐON
-- ========================================================
CREATE TABLE ChiTietHoaDon (
    maHD VARCHAR(20),
    maSP VARCHAR(20),
    soLuong INT CHECK (soLuong > 0),
    tongTien FLOAT CHECK (tongTien >= 0),
    PRIMARY KEY (maHD, maSP),
    FOREIGN KEY (maHD) REFERENCES HoaDon(maHD)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (maSP) REFERENCES SanPham(maSP)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
GO

-- ========================================================
-- B?NG ĐON Đ?T BÀN (B?NG M?I)
-- Thêm b?ng này vì có trong UML nhung không có trong SQL
-- ========================================================
CREATE TABLE DonDatBan (
    maDonDatBan VARCHAR(20) PRIMARY KEY,
    maKH VARCHAR(20),
    maBan VARCHAR(20),
    thoiGian DATETIME NOT NULL,
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    FOREIGN KEY (maBan) REFERENCES Ban(maBan)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);
GO

-- ========================================================
-- D? LI?U M?U (ĐÃ C?P NH?T THEO C?U TRÚC M?I)
-- ========================================================

-- Dữ liệu cho bảng NhanVien (Không đ?i)
INSERT INTO NhanVien VALUES 
('NV001', N'Nguyễn Văn A', 'NV', '0901234567'),
('NV002', N'Trần Thị B', 'NV', '0902345678'),
('NV003', N'Lê Văn C', 'QL', '0903456789'),
('NV004', N'Phạm Thị D', 'NV', '0904567890'),
('NV005', N'Hoàng Văn E', 'QL', '0905678901');

-- Dữ liệu cho bảng TaiKhoan (S?a: B? chucVu, tenNV)
INSERT INTO TaiKhoan VALUES 
('nguyena', 'pass123', 'NV001'),
('tranb', 'pass234', 'NV002'),
('lec', 'pass345', 'NV003'),
('phamd', 'pass456', 'NV004'),
('hoange', 'pass567', 'NV005');

-- Dữ liệu cho bảng KhachHang (Không đ?i)
INSERT INTO KhachHang VALUES 
('KH001', N'Nguyễn Thị X', '0911111111', 120, 1),
('KH002', N'Trần Văn Y', '0912222222', 80, 0),
('KH003', N'Lê Thị Z', '0913333333', 150, 1),
('KH004', N'Phạm Văn W', '0914444444', 60, 0),
('KH005', N'Hoàng Thị V', '0915555555', 200, 1);

-- Dữ liệu cho bảng Ban (S?a: Đ?i 0/1 -> Tr?ng thái ch?)
INSERT INTO Ban VALUES 
('B01','Bàn 1', 4, N'Trống'),
('B02','Bàn 2', 2, N'Đang được sử dụng'),
('B03','Bàn 3', 6, N'Trống'),
('B04','Bàn 4', 4, N'Đang được sử dụng'),
('B05','Bàn 5', 2, N'Trống');

-- Dữ liệu cho bảng SanPham (S?a: B? soLuong, thêm loaiSanPham)
INSERT INTO SanPham VALUES 
('SP001', N'Cà phê sữa', 20000, N'Cafe'),
('SP002', N'Trà đào', 30000, N'Trà'),
('SP003', N'Tiramisu', 35000, N'Bánh'),
('SP004', N'Trà Ổi', 30000, N'Trà'),
('SP005', N'Croissant', 20000, N'Bánh');


-- Dữ liệu cho bảng HoaDon (S?a: Thêm th?i gian vào/ra)
-- Gi? s? hóa đơn đã thanh toán (1) thì có thoiGianRa
-- Hóa đơn chua thanh toán (0) thì thoiGianRa là NULL
INSERT INTO HoaDon VALUES 
('HD001', 'NV001', 'KH001', 'B01', '2025-10-25 10:30:00', '2025-10-25 11:15:00', 1),
('HD002', 'NV002', 'KH002', 'B02', '2025-10-26 14:00:00', NULL, 0),
('HD003', 'NV003', 'KH003', 'B03', '2025-10-26 18:45:00', '2025-10-26 19:30:00', 1),
('HD004', 'NV004', 'KH004', 'B04', '2025-10-27 08:15:00', NULL, 0),
('HD005', 'NV005', 'KH005', 'B05', '2025-10-27 09:00:00', '2025-10-27 09:45:00', 1);

-- Dữ liệu cho bảng ChiTietHoaDon (Không đ?i)
INSERT INTO ChiTietHoaDon VALUES 
('HD001', 'SP001', 2, 50000),
('HD001', 'SP005', 1, 20000),
('HD002', 'SP002', 1, 30000),
('HD003', 'SP003', 2, 70000),
('HD004', 'SP004', 3, 30000);

-- Dữ liệu cho bảng DonDatBan (B?ng m?i)
INSERT INTO DonDatBan VALUES
('DDB001', 'KH001', 'B03', '2025-11-05 19:00:00'),
('DDB002', 'KH003', 'B05', '2025-11-06 12:00:00');
GO
