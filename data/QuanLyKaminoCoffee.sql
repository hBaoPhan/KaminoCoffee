USE [master]
GO 
DROP DATABASE IF EXISTS QUANLYKAMINOCOFFEE
GO
CREATE DATABASE QUANLYKAMINOCOFFEE;
GO
USE QUANLYKAMINOCOFFEE;
GO

CREATE TABLE NhanVien (
    maNV VARCHAR(20) PRIMARY KEY,
    tenNV NVARCHAR(100) NOT NULL,
    chucVu NVARCHAR(10) CHECK (chucVu IN (N'NV', N'QL')) NOT NULL, 
    sDT VARCHAR(15),
    gioiTinh Bit not null
);
GO

CREATE TABLE TaiKhoan (
    tenDangNhap VARCHAR(50) PRIMARY KEY,
    matKhau VARCHAR(255) NOT NULL,
    maNV VARCHAR(20) NOT NULL,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    UNIQUE(maNV) 
);
GO

CREATE TABLE KhachHang (
    maKH VARCHAR(20) PRIMARY KEY,
    tenKH NVARCHAR(100),
    sDT VARCHAR(15),
    diemTichLuy int,
    laKHDK bit not null
);
GO

CREATE TABLE Ban (
    maBan VARCHAR(20) PRIMARY KEY,
    tenBan NVARCHAR(100),
    soGhe INT CHECK (soGhe >= 0),
    trangThai NVARCHAR(20) 
        CHECK (trangThai IN (N'Trống', N'Đang được sử dụng', N'Đã được đặt')) 
        DEFAULT N'Trống'
);
GO

CREATE TABLE SanPham (
    maSP VARCHAR(20) PRIMARY KEY,
    tenSP NVARCHAR(100),
    gia FLOAT CHECK (gia >= 0),
    loaiSanPham NVARCHAR(100) 
        CHECK (loaiSanPham IN(N'Trà',N'Cafe',N'Bánh',N'Trà Sữa',N'Sinh tố',N'YOGURT',N'Nước uống đóng chai'))
   
);
GO

CREATE TABLE HoaDon (
    maHD VARCHAR(20) PRIMARY KEY,
    maNV VARCHAR(20),
    maKH VARCHAR(20),
    maBan VARCHAR(20),
    thoiGianVao DATETIME DEFAULT (GETDATE()),
    thoiGianRa DATETIME NULL, 
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
('NV001', N'Phan Hoài Bảo', N'QL', '0335806335', 1),
('NV002', N'Trần Thiên Bảo', N'QL', '0912345678', 1),
('NV003', N'Chìu Kim Thi', N'QL', '0923456789', 0),
('NV004', N'Trần Tấn Tài', N'QL', '0934567890', 1),
('NV005', N'Hoàng Nhân Viên', N'NV', '0945678901', 1);
INSERT INTO TaiKhoan (tenDangNhap, matKhau, maNV) VALUES
('baoph', 'adpass', 'NV001'),
('baotr', 'adpass', 'NV002'),
('thich', 'adpass', 'NV003'),
('taitr', 'adpass', 'NV004'),
('user', 'pass', 'NV005');
INSERT INTO KhachHang (maKH, tenKH, sDT, diemTichLuy, laKHDK) VALUES
('KH001', N'Nguyễn Thanh Tú', '0981111111', 71, 1),
('KH002', N'Nguyễn Thành Long', '0982222222', 86, 1),
('KH003', N'Nguyễn Xuân Trường', '0983333333', 48, 1),
('KH004', N'Nguyễn Minh Nhật', '0984444444', 59, 0),
('KH005', N'Nguyên Chồn', '0985555555', 47, 1);
INSERT INTO Ban VALUES 
('BA001','Bàn 1',4,N'Trống'),
('BA002','Bàn 2',2,N'Trống'),
('BA003','Bàn 3',6,N'Trống'),
('BA004','Bàn 4', 4, N'Trống'),
('BA005','Bàn 5', 6, N'Trống'),
('BA006','Bàn 6', 2, N'Trống'),
('BA007','Bàn 7', 4, N'Trống'),
('BA008','Bàn 8', 6, N'Trống'),
('BA009','Bàn 9', 4, N'Trống'),
('BA010','Bàn 10', 6, N'Trống');
INSERT INTO DonDatBan (maDonDatBan, maKH, maBan, thoiGian, daNhan) VALUES
('DB001', 'KH001', 'BA002', '2025-11-01 09:00:00',1),
('DB002', 'KH002', 'BA003', '2025-11-03 13:30:00',1),
('DB003', 'KH003', 'BA004', '2025-11-05 17:45:00',0),
('DB004', 'KH004', 'BA005', '2025-11-08 10:15:00',1),
('DB005', 'KH005', 'BA006', '2025-11-10 15:20:00',0);

INSERT INTO HoaDon (maHD, maNV, maKH, maBan, thoiGianVao, thoiGianRa, trangThaiThanhToan) VALUES
('HD001', 'NV001', 'KH002', 'BA006', '2025-10-05 09:30:00', '2025-10-05 10:15:00', 1),
('HD002', 'NV002', 'KH003', 'BA007', '2025-10-10 14:00:00', '2025-10-10 15:00:00', 1),
('HD003', 'NV003', 'KH004', 'BA008', '2025-10-15 18:20:00', '2025-10-15 19:10:00', 1),
('HD004', 'NV004', 'KH005', 'BA009', '2025-10-20 11:45:00', '2025-10-20 12:30:00', 1),
('HD005', 'NV005', 'KH001', 'BA001', '2025-10-25 16:10:00', '2025-10-25 17:00:00', 1),
('HD006', 'NV001', 'KH001', 'BA001', '2025-11-01 09:30:00', '2025-11-01 10:15:00', 1),
('HD007', 'NV002', 'KH002', 'BA002', '2025-11-03 14:00:00', '2025-11-03 15:00:00', 1),
('HD008', 'NV003', 'KH003', 'BA003', '2025-11-05 18:20:00', '2025-11-05 19:10:00', 1),
('HD009', 'NV004', 'KH004', 'BA004', '2025-11-08 11:45:00', '2025-11-08 12:30:00', 1),
('HD010', 'NV005', 'KH005', 'BA005', '2025-11-10 16:10:00', '2025-11-10 17:00:00', 1);
INSERT INTO ChiTietHoaDon (maHD, maSP, soLuong, tongTien) VALUES
('HD001', 'SP001', 2, 46000),   
('HD001', 'SP002', 1, 25000),   
('HD002', 'SP004', 3, 105000),   
('HD003', 'SP007', 5, 150000),
('HD003', 'SP005', 2, 80000),   
('HD004', 'SP007', 5, 150000),  
('HD005', 'SP005', 2, 80000),
('HD006', 'SP001', 2, 46000),  
('HD007', 'SP002', 1, 25000),   
('HD008', 'SP003', 2, 70000),   
('HD009', 'SP004', 3, 105000),  
('HD010', 'SP005', 1, 40000);   
  







