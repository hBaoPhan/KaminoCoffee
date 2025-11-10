-- ========================================================
-- DATABASE: QUANLYQUANCOFFEE
-- ========================================================
USE [master]
GO 
DROP DATABASE IF EXISTS QUANLYKAMINOCOFFEE
GO
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
    sDT VARCHAR(15),
    gioiTinh Bit not null
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
        CHECK (loaiSanPham IN(N'Trà',N'Cafe',N'Bánh',N'Trà Sữa',N'Sinh tố',N'YOGURT',N'Nước uống đóng chai'))
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
    daNhan Bit,
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    FOREIGN KEY (maBan) REFERENCES Ban(maBan)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);
GO


DELETE FROM ChiTietHoaDon;
DELETE FROM HoaDon;
DELETE FROM DonDatBan;
DELETE FROM TaiKhoan;
DELETE FROM NhanVien;
DELETE FROM KhachHang;
DELETE FROM Ban;
DELETE FROM SanPham;
GO


INSERT INTO SanPham VALUES 
('SP001', N'Cà phê đen', 23000, N'Cafe'),
('SP002', N'Cà phê sữa', 25000, N'Cafe'),
('SP003', N'Trà đào', 35000, N'Trà'),
('SP004', N'Trà ổi', 35000, N'Trà'),
('SP005', N'Sinh tố bơ', 40000, N'Sinh tố'),
('SP006', N'Sinh tố dừa', 40000, N'Sinh tố'),
('SP007', N'Sữa chua', 30000, N'YOGURT'),
('SP008', N'Sữa chua việt quất', 35000, N'YOGURT'),
('SP009', N'Trà sữa matcha', 38000, N'Trà Sữa'),
('SP0010', N'Trà sữa trân châu đường đen', 38000, N'Trà Sữa'),
('SP0011', N'Trà sữa kem trứng nướng', 40000, N'Trà Sữa'),
('SP0012', N'Sting', 25000, N'Nước uống đóng chai'),
('SP0013', N'Nước suối', 25000, N'Nước uống đóng chai'),
('SP0014', N'Bò húc', 25000, N'Nước uống đóng chai'),
('SP0015', N'Tiramisu', 35000, N'Bánh'),
('SP0016', N'Bánh chuối', 35000, N'Bánh'),
('SP0017', N'Mousse', 35000, N'Bánh');
INSERT INTO NhanVien (maNV, tenNV, chucVu, sDT, gioiTinh) VALUES
('NV001', N'Nguyễn Văn A', N'NV', '0901234567', 1),
('NV002', N'Trần Thị B', N'NV', '0912345678', 0),
('NV003', N'Lê Văn C', N'QL', '0923456789', 1),
('NV004', N'Phạm Thị D', N'NV', '0934567890', 0),
('NV005', N'Hoàng Văn E', N'QL', '0945678901', 1);
INSERT INTO TaiKhoan (tenDangNhap, matKhau, maNV) VALUES
('userA', 'passA', 'NV001'),
('userB', 'passB', 'NV002'),
('userC', 'passC', 'NV003'),
('userD', 'passD', 'NV004'),
('userE', 'passE', 'NV005');
INSERT INTO KhachHang (maKH, tenKH, sDT, diemTichLuy, laKHDK) VALUES
('KH001', N'Nguyễn Khách 1', '0981111111', 50, 1),
('KH002', N'Nguyễn Khách 2', '0982222222', 0, 0),
('KH003', N'Nguyễn Khách 3', '0983333333', 100, 1),
('KH004', N'Nguyễn Khách 4', '0984444444', 0, 0),
('KH005', N'Nguyễn Khách 5', '0985555555', 70, 1);

INSERT INTO Ban VALUES 
('BA001','Bàn 1',4,N'Trống'),
('BA002','Bàn 2',2,N'Đang được sử dụng'),
('BA003','Bàn 3',6,N'Trống'),
('BA004','Bàn 4', 4, N'Trống'),
('BA005','Bàn 5', 6, N'Trống'),
('BA006','Bàn 6', 2, N'Trống'),
('BA007','Bàn 7', 4, N'Trống'),
('BA008','Bàn 8', 6, N'Trống'),
('BA009','Bàn 9', 4, N'Trống'),
('BA010','Bàn 10', 6, N'Trống');
INSERT INTO DonDatBan (maDonDatBan, maKH, maBan, thoiGian, daNhan) VALUES
('DB001', 'KH001', 'BA002', GETDATE(), 1),
('DB002', 'KH002', 'BA003', GETDATE(), 1),
('DB003', 'KH003', 'BA004', GETDATE(), 1),
('DB004', 'KH004', 'BA005', GETDATE(), 1),
('DB005', 'KH005', 'BA006', GETDATE(), 1);
INSERT INTO HoaDon (maHD, maNV, maKH, maBan, thoiGianVao, thoiGianRa, trangThaiThanhToan) VALUES
('HD001', 'NV001', 'KH001', 'BA001', GETDATE(), NULL, 1),
('HD002', 'NV002', 'KH002', 'BA002', GETDATE(), NULL, 1),
('HD003', 'NV003', 'KH003', 'BA003', GETDATE(), NULL, 1),
('HD004', 'NV004', 'KH004', 'BA004', GETDATE(), NULL, 1),
('HD005', 'NV005', 'KH005', 'BA005', GETDATE(), NULL, 1);
INSERT INTO ChiTietHoaDon (maHD, maSP, soLuong, tongTien) VALUES
('HD001', 'SP001', 2, 40000),
('HD002', 'SP002', 1, 25000),
('HD003', 'SP004', 3, 105000),
('HD004', 'SP005', 2, 80000),
('HD005', 'SP007', 5, 50000);






