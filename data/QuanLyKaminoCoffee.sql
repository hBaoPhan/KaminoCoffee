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
    chucVu NVARCHAR(10) CHECK (chucVu IN ('NV', 'QL')) NOT NULL,
    sDT VARCHAR(15)
);
GO

-- ========================================================
-- B?NG TÀI KHO?N
-- ========================================================
CREATE TABLE TaiKhoan (
    tenDangNhap VARCHAR(50) PRIMARY KEY,
    matKhau VARCHAR(255) NOT NULL,
    chucVu NVARCHAR(10) CHECK (chucVu IN ('NV', 'QL')) NOT NULL,
    maNV VARCHAR(20) NULL,
    tenNV NVARCHAR(100),
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
        ON DELETE SET NULL
        ON UPDATE CASCADE
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
-- B?NG BÀN
-- ========================================================
CREATE TABLE Ban (
    maBan VARCHAR(20) PRIMARY KEY,
    soGhe INT CHECK (soGhe >= 0),
    trangThai BIT DEFAULT 0  -- 0: Tr?ng, 1: Ðang s? d?ng/Ðã d?t
   
);
GO

-- ========================================================
-- B?NG S?N PH?M
-- ========================================================
CREATE TABLE SanPham (
    maSP VARCHAR(20) PRIMARY KEY,
    tenSP NVARCHAR(100),
    gia FLOAT CHECK (gia >= 0),
    soLuong INT CHECK (soLuong >= 0)
);
GO
CREATE TABLE KhuyenMai (
    maKM VARCHAR(20) PRIMARY KEY,
    phanTram FLOAT CHECK (phanTram > 0),
    ngayBatDau Date,
    ngayKetThuc Date
  
);
-- ========================================================
-- B?NG HÓA ÐON
-- ========================================================
CREATE TABLE HoaDon (
    maHD VARCHAR(20) PRIMARY KEY,
    maNV VARCHAR(20),
    maKH VARCHAR(20),
    maBan VARCHAR(20),
    maKM VARCHAR(20),
    ngayLap DATE DEFAULT (GETDATE()),
    trangThaiThanhToan Bit,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    FOREIGN KEY (maBan) REFERENCES Ban(maBan)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM)
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
    tongTien FLOAT CHECK (tongTien >= 0),  -- Có th? tính t? gia * soLuong, nhung luu tr?c ti?p d? don gi?n
    PRIMARY KEY (maHD, maSP),
    FOREIGN KEY (maHD) REFERENCES HoaDon(maHD)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (maSP) REFERENCES SanPham(maSP)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
GO

/*
-- Dữ liệu cho bảng NhanVien
INSERT INTO NhanVien VALUES 
('NV001', N'Nguyễn Văn A', 'NV', '0901234567'),
('NV002', N'Trần Thị B', 'NV', '0902345678'),
('NV003', N'Lê Văn C', 'QL', '0903456789'),
('NV004', N'Phạm Thị D', 'NV', '0904567890'),
('NV005', N'Hoàng Văn E', 'QL', '0905678901');

-- Dữ liệu cho bảng TaiKhoan
INSERT INTO TaiKhoan VALUES 
('nguyena', 'pass123', 'NV', 'NV001', N'Nguyễn Văn A'),
('tranb', 'pass234', 'NV', 'NV002', N'Trần Thị B'),
('lec', 'pass345', 'QL', 'NV003', N'Lê Văn C'),
('phamd', 'pass456', 'NV', 'NV004', N'Phạm Thị D'),
('hoange', 'pass567', 'QL', 'NV005', N'Hoàng Văn E');

-- Dữ liệu cho bảng KhachHang
INSERT INTO KhachHang VALUES 
('KH001', N'Nguyễn Thị X', '0911111111', 120, 1),
('KH002', N'Trần Văn Y', '0912222222', 80, 0),
('KH003', N'Lê Thị Z', '0913333333', 150, 1),
('KH004', N'Phạm Văn W', '0914444444', 60, 0),
('KH005', N'Hoàng Thị V', '0915555555', 200, 1);

-- Dữ liệu cho bảng Ban
INSERT INTO Ban VALUES 
('B01', 4, 0),
('B02', 2, 1),
('B03', 6, 0),
('B04', 4, 1),
('B05', 2, 0);

-- Dữ liệu cho bảng SanPham
INSERT INTO SanPham VALUES 
('SP001', N'Cà phê sữa', 25000, 100),
('SP002', N'Trà đào', 30000, 80),
('SP003', N'Sinh tố bơ', 35000, 50),
('SP004', N'Nước suối', 10000, 200),
('SP005', N'Bánh ngọt', 20000, 60);

-- Dữ liệu cho bảng HoaDon
INSERT INTO HoaDon VALUES 
('HD001', 'NV001', 'KH001', 'B01', '2025-10-25', 1),
('HD002', 'NV002', 'KH002', 'B02', '2025-10-26', 0),
('HD003', 'NV003', 'KH003', 'B03', '2025-10-26', 1),
('HD004', 'NV004', 'KH004', 'B04', '2025-10-27', 0),
('HD005', 'NV005', 'KH005', 'B05', '2025-10-27', 1);

-- Dữ liệu cho bảng ChiTietHoaDon
--INSERT INTO ChiTietHoaDon VALUES 
--('HD001', 'SP001', 2, 50000),
--('HD001', 'SP005', 1, 20000),
--('HD002', 'SP002', 1, 30000),
--('HD003', 'SP003', 2, 70000),
--('HD004', 'SP004', 3, 30000);

*/
