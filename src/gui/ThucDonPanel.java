package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout; 
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import dao.SanPham_dao;
import entity.LoaiSanPham;
import entity.SanPham;

public class ThucDonPanel extends JPanel implements ActionListener, MouseListener {

	private SanPham_dao daoSanPham;

	private Font fontTieuDe = new Font("Time New Roman", Font.BOLD, 20);
	private Border vienVienNgoai = BorderFactory.createLineBorder(Color.decode("#e07b39"), 5);
	private Border vienTrong = BorderFactory.createEmptyBorder(10, 10, 10, 10);

	private JTextField txtThanhTimKiem;
	private JButton btnTimKiem;
	private JComboBox<String> cboLocTheoLoai;
	private JPanel pnlDanhSachMon;

	private JTextField txtMaSanPham, txtTenSanPham, txtGiaSanPham;
	private JComboBox<String> cboLoaiSanPham;
	private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnChonAnh;
	private JLabel lblHienThiAnh;

	private String duongDanAnhHienTai;
	private SanPham sanPhamDaChon;

	public ThucDonPanel() {
		daoSanPham = new SanPham_dao();

		setLayout(new BorderLayout(10, 10));
		setBackground(Color.WHITE);

		Box hopThucDonBenTrai = Box.createVerticalBox();
		hopThucDonBenTrai.setPreferredSize(new Dimension(950, 800));
		hopThucDonBenTrai.setBackground(Color.decode("#F7F4EC"));
		hopThucDonBenTrai.setOpaque(true);
		hopThucDonBenTrai.setBorder(new CompoundBorder(vienVienNgoai, vienTrong));

		Box hopHang1 = Box.createHorizontalBox();
		hopHang1.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel lblTieuDeMenu = new JLabel("Menu Sản Phẩm");
		lblTieuDeMenu.setFont(fontTieuDe);
		hopHang1.add(lblTieuDeMenu);
		hopThucDonBenTrai.add(hopHang1);
		hopThucDonBenTrai.add(Box.createVerticalStrut(10));

		Box hopHang2 = Box.createHorizontalBox();
		hopHang2.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtThanhTimKiem = new JTextField(15);
		txtThanhTimKiem.setPreferredSize(new Dimension(150, 30));
		txtThanhTimKiem.setMaximumSize(txtThanhTimKiem.getPreferredSize());

		btnTimKiem = new JButton("Tìm kiếm");
		btnTimKiem.setPreferredSize(new Dimension(100, 30));
		btnTimKiem.setMaximumSize(btnTimKiem.getPreferredSize());
		btnTimKiem.setBackground(Color.decode("#00A651"));
		btnTimKiem.setBorder(BorderFactory.createLineBorder(Color.decode("#00A651"), 3, true));
		btnTimKiem.setForeground(Color.WHITE);

		hopHang2.add(txtThanhTimKiem);
		hopHang2.add(Box.createHorizontalStrut(5));
		hopHang2.add(btnTimKiem);

		hopHang2.add(Box.createHorizontalStrut(20));
		JLabel lblTieuDeLoc = new JLabel("Lọc theo loại:");
		hopHang2.add(lblTieuDeLoc);
		hopHang2.add(Box.createHorizontalStrut(5));

		cboLocTheoLoai = new JComboBox<String>();
		cboLocTheoLoai.setPreferredSize(new Dimension(255, 30));
		cboLocTheoLoai.setMaximumSize(cboLocTheoLoai.getPreferredSize());
		hopHang2.add(cboLocTheoLoai);
		hopThucDonBenTrai.add(hopHang2);

		pnlDanhSachMon = new JPanel(new GridLayout(0, 4, 10, 10));
		pnlDanhSachMon.setBackground(Color.decode("#F7F4EC"));

		JScrollPane thanhCuonDanhSachMon = new JScrollPane(pnlDanhSachMon);
		thanhCuonDanhSachMon.setBorder(null);
		thanhCuonDanhSachMon.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		thanhCuonDanhSachMon.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		thanhCuonDanhSachMon.setBackground(Color.decode("#F7F4EC"));
		thanhCuonDanhSachMon.setPreferredSize(new Dimension(400, 600));

		hopThucDonBenTrai.add(thanhCuonDanhSachMon);

		JPanel pnlChinhGiua = new JPanel(new BorderLayout());
		pnlChinhGiua.add(hopThucDonBenTrai, BorderLayout.CENTER);
		add(pnlChinhGiua, BorderLayout.CENTER);

		
		Box hopThongTinBenPhai = Box.createVerticalBox();
		hopThongTinBenPhai.setBackground(Color.WHITE);
		hopThongTinBenPhai.setOpaque(true);
		hopThongTinBenPhai.setBorder(new CompoundBorder(vienVienNgoai, vienTrong));

		JLabel lblTieuDeThongTin = new JLabel("Thông Tin Sản Phẩm");
		lblTieuDeThongTin.setFont(fontTieuDe);
		lblTieuDeThongTin.setAlignmentX(Component.CENTER_ALIGNMENT);
		hopThongTinBenPhai.add(lblTieuDeThongTin);
		hopThongTinBenPhai.add(Box.createVerticalStrut(20));

		Box hopChuaAnh = Box.createVerticalBox();
		hopChuaAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblHienThiAnh = new JLabel();
		lblHienThiAnh.setPreferredSize(new Dimension(200, 200));
		lblHienThiAnh.setMaximumSize(new Dimension(200, 200));
		lblHienThiAnh.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		lblHienThiAnh.setIcon(taiHinhAnhSanPham("data/images/default", false));

		btnChonAnh = new JButton("Chọn Ảnh");
		btnChonAnh.setAlignmentX(Component.CENTER_ALIGNMENT);

		hopChuaAnh.add(lblHienThiAnh);
		hopChuaAnh.add(Box.createVerticalStrut(10));
		hopChuaAnh.add(btnChonAnh);
		hopThongTinBenPhai.add(hopChuaAnh);
		hopThongTinBenPhai.add(Box.createVerticalStrut(20));

		JPanel pnlNhapLieu = new JPanel(new GridBagLayout());
		pnlNhapLieu.setBackground(Color.WHITE);
		pnlNhapLieu.setAlignmentX(Component.CENTER_ALIGNMENT);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = 0.0;
		JLabel lblMaSanPham = new JLabel("Mã sản phẩm:");
		pnlNhapLieu.add(lblMaSanPham, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 1.0;
		txtMaSanPham = new JTextField();
		txtMaSanPham.setEditable(false);
		txtMaSanPham.setBackground(Color.LIGHT_GRAY);
		pnlNhapLieu.add(txtMaSanPham, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = 0.0;
		JLabel lblTenSanPham = new JLabel("Tên sản phẩm:");
		pnlNhapLieu.add(lblTenSanPham, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 1.0;
		txtTenSanPham = new JTextField();
		pnlNhapLieu.add(txtTenSanPham, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = 0.0;
		JLabel lblGiaSanPham = new JLabel("Đơn giá:");
		pnlNhapLieu.add(lblGiaSanPham, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 1.0;
		txtGiaSanPham = new JTextField();
		pnlNhapLieu.add(txtGiaSanPham, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = 0.0;
		JLabel lblLoaiSanPham = new JLabel("Loại sản phẩm:");
		pnlNhapLieu.add(lblLoaiSanPham, gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 1.0;
		cboLoaiSanPham = new JComboBox<>();
		pnlNhapLieu.add(cboLoaiSanPham, gbc);

		hopThongTinBenPhai.add(pnlNhapLieu);
		hopThongTinBenPhai.add(Box.createVerticalStrut(30));

		JPanel pnlCacNut = new JPanel(new GridLayout(2, 2, 10, 10));
		pnlCacNut.setBackground(Color.WHITE);
		pnlCacNut.setMaximumSize(new Dimension(400, 100));
		pnlCacNut.setAlignmentX(Component.CENTER_ALIGNMENT);

		btnThem = taoNutChucNang("Thêm", Color.decode("#00A651"));
		btnSua = taoNutChucNang("Sửa", Color.decode("#e07b39"));
		btnXoa = taoNutChucNang("Xóa", Color.decode("#DC3545"));
		btnLamMoi = taoNutChucNang("Làm Mới", Color.decode("#6C757D"));

		pnlCacNut.add(btnThem);
		pnlCacNut.add(btnSua);
		pnlCacNut.add(btnXoa);
		pnlCacNut.add(btnLamMoi);

		hopThongTinBenPhai.add(pnlCacNut);
		hopThongTinBenPhai.add(Box.createVerticalGlue());

		JPanel pnlBaoBenPhai = new JPanel(new BorderLayout());
		pnlBaoBenPhai.add(hopThongTinBenPhai, BorderLayout.CENTER);
		add(pnlBaoBenPhai, BorderLayout.EAST);

		
		cboLocTheoLoai.addItem("Tất cả");
		for (LoaiSanPham loai : LoaiSanPham.values()) {
			cboLoaiSanPham.addItem(loai.getMoTa());
			cboLocTheoLoai.addItem(loai.getMoTa());
		}
		
		taiDanhSachSanPhamVaoPanel(pnlDanhSachMon, daoSanPham.getAllSanPham());

		btnChonAnh.addActionListener(this);
		btnLamMoi.addActionListener(this);
		btnSua.addActionListener(this);
		btnThem.addActionListener(this);
		btnTimKiem.addActionListener(this);
		btnXoa.addActionListener(this);
		cboLocTheoLoai.addActionListener(this);
	}

	private JButton taoNutChucNang(String text, Color color) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btn.setBackground(color);
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setPreferredSize(new Dimension(150, 40));
		return btn;
	}

	public void taiDanhSachSanPhamVaoPanel(JPanel panelHienThi, ArrayList<SanPham> dsSanPham) {
		panelHienThi.removeAll();

		for (SanPham sp : dsSanPham) {
			JPanel pnlTheSanPham = new JPanel(new BorderLayout(15, 0));
			pnlTheSanPham.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 15));
			pnlTheSanPham.setPreferredSize(new Dimension(225, 110)); 
			pnlTheSanPham.setBackground(Color.WHITE);

			String tenSanPham = sp.getTenSanPham();
			ImageIcon hinhAnh = taiHinhAnhSanPham("data/images/" + tenSanPham, true);

			JLabel lblAnh = new JLabel(hinhAnh);
			lblAnh.setPreferredSize(new Dimension(90, 90));
			lblAnh.setOpaque(false);
			pnlTheSanPham.add(lblAnh, BorderLayout.WEST);

			JPanel pnlThongTin = new JPanel();
			pnlThongTin.setLayout(new BoxLayout(pnlThongTin, BoxLayout.Y_AXIS));
			pnlThongTin.setOpaque(false);

			JLabel lblTenSP = new JLabel(tenSanPham);
			lblTenSP.setName("lblTenSP"); 
			lblTenSP.setFont(new Font("Segoe UI", Font.BOLD, 16));
			lblTenSP.setForeground(new Color(60, 60, 60));

			double giaBan = sp.getGia();
			String giaDinhDang = String.format("%,.0f₫", giaBan);
			JLabel lblGiaSP = new JLabel(giaDinhDang);
			lblGiaSP.setFont(new Font("Segoe UI", Font.BOLD, 14));
			lblGiaSP.setForeground(new Color(220, 0, 0));

			String tenLoai = sp.getLoaiSanPham() != null ? sp.getLoaiSanPham().getMoTa() : "-";
			JLabel lblLoaiSP = new JLabel("Loại: " + tenLoai);
			lblLoaiSP.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			lblLoaiSP.setForeground(new Color(100, 100, 100));

			boolean conHang = true; 
			JLabel lblTrangThai = new JLabel(conHang ? "Có sẵn" : "Hết món");
			lblTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			lblTrangThai.setForeground(conHang ? new Color(0, 128, 0) : Color.RED);

			pnlThongTin.add(lblTenSP);
			pnlThongTin.add(Box.createVerticalGlue());
			pnlThongTin.add(lblGiaSP);
			pnlThongTin.add(Box.createVerticalStrut(5));
			pnlThongTin.add(lblLoaiSP);
			pnlThongTin.add(lblTrangThai);

			pnlTheSanPham.add(pnlThongTin, BorderLayout.CENTER);

			JPanel pnlBaoBoc = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			pnlBaoBoc.setBackground(Color.decode("#F7F4EC"));
			pnlBaoBoc.add(pnlTheSanPham);
			
			panelHienThi.add(pnlBaoBoc);
			
			pnlTheSanPham.addMouseListener(this);
		}

		panelHienThi.revalidate();
		panelHienThi.repaint();
	}

	public ImageIcon taiHinhAnhSanPham(String tenFileAnh, boolean laHinhNho) {
		String[] danhSachPhanMoRong = { ".png", ".jpg", ".jpeg" };
		File tepAnh = null;
		int kichThuoc = laHinhNho ? 85 : 200;

		if (duongDanAnhHienTai != null && !laHinhNho) {
			tepAnh = new File(duongDanAnhHienTai);
			if (tepAnh.exists()) {
				return new ImageIcon(
						new ImageIcon(tepAnh.getAbsolutePath()).getImage().getScaledInstance(kichThuoc, kichThuoc, Image.SCALE_SMOOTH));
			}
		}

		for (String phanMoRong : danhSachPhanMoRong) {
			tepAnh = new File(tenFileAnh + phanMoRong);
			if (tepAnh.exists()) {
				return new ImageIcon(
						new ImageIcon(tepAnh.getAbsolutePath()).getImage().getScaledInstance(kichThuoc, kichThuoc, Image.SCALE_SMOOTH));
			}
		}

		tepAnh = new File("data/images/default.png");
		return new ImageIcon(
				new ImageIcon(tepAnh.getAbsolutePath()).getImage().getScaledInstance(kichThuoc, kichThuoc, Image.SCALE_SMOOTH));
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object nguonSuKien = e.getSource();

		if (nguonSuKien == btnChonAnh) {
			chonAnh();
		} else if (nguonSuKien == btnLamMoi) {
			lamMoi();
		} else if (nguonSuKien == btnTimKiem || nguonSuKien == cboLocTheoLoai) {
			timKiem();
		} else if (nguonSuKien == btnThem) {
			themSanPham();
		} else if (nguonSuKien == btnSua) {
			suaSanPham();
		} else if (nguonSuKien == btnXoa) {
			xoaSanPham();
		}
	}

	private void suaSanPham() {
		if (sanPhamDaChon == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa.", "Lỗi", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String maSanPham = txtMaSanPham.getText().trim();
		SanPham sanPhamCapNhat = taoSanPhamTuForm(maSanPham);

		if (sanPhamCapNhat != null) {
			if (daoSanPham.suaSanPham(sanPhamCapNhat)) {
				JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!");
				taiDanhSachSanPhamVaoPanel(pnlDanhSachMon, daoSanPham.getAllSanPham());
				lamMoi();
			} else {
				JOptionPane.showMessageDialog(this, "Lỗi: Không thể cập nhật sản phẩm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void xoaSanPham() {
		if (sanPhamDaChon == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa.", "Lỗi", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int xacNhan = JOptionPane.showConfirmDialog(this,
				"Bạn có chắc chắn muốn xóa sản phẩm '" + sanPhamDaChon.getTenSanPham() + "'?", "Xác nhận xóa",
				JOptionPane.YES_NO_OPTION);
		
		if (xacNhan == JOptionPane.YES_OPTION) {
			if (daoSanPham.xoaSanPham(sanPhamDaChon.getMaSanPham())) {
				JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
				taiDanhSachSanPhamVaoPanel(pnlDanhSachMon, daoSanPham.getAllSanPham());
				lamMoi();
			} else {
				JOptionPane.showMessageDialog(this,
						"Lỗi: Không thể xóa sản phẩm. (Có thể do sản phẩm đang có trong hóa đơn).", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void chonAnh() {
		JFileChooser hopThoaiChonTep = new JFileChooser("data/images/");
		FileNameExtensionFilter boLocTep = new FileNameExtensionFilter("Hình ảnh", "jpg", "jpeg", "png");
		hopThoaiChonTep.setFileFilter(boLocTep);

		int ketQua = hopThoaiChonTep.showOpenDialog(this);
		if (ketQua == JFileChooser.APPROVE_OPTION) {
			File tepHinhDaChon = hopThoaiChonTep.getSelectedFile();
			duongDanAnhHienTai = tepHinhDaChon.getAbsolutePath();
			lblHienThiAnh.setIcon(taiHinhAnhSanPham(duongDanAnhHienTai, false));
		}
	}

	private void themSanPham() {
		int idTiepTheo = daoSanPham.getAllSanPham().stream()
				.map(SanPham::getMaSanPham)
				.filter(id -> id.matches("SP\\d+"))
				.mapToInt(id -> Integer.parseInt(id.substring(2)))
				.max().orElse(0) + 1;
		String maSanPhamMoi = "SP" + String.format("%03d", idTiepTheo);

		SanPham sanPhamMoi = taoSanPhamTuForm(maSanPhamMoi);

		if (sanPhamMoi != null) {
			if (daoSanPham.getSanPhamByTen(sanPhamMoi.getTenSanPham()) != null) {
				JOptionPane.showMessageDialog(this, "Lỗi: Tên sản phẩm đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (daoSanPham.themSanPham(sanPhamMoi)) {
				JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
				taiDanhSachSanPhamVaoPanel(pnlDanhSachMon, daoSanPham.getAllSanPham());
				lamMoi();
			} else {
				JOptionPane.showMessageDialog(this, "Lỗi: Không thể thêm sản phẩm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private SanPham taoSanPhamTuForm(String maSanPham) {
		String tenSanPham = txtTenSanPham.getText().trim();
		double giaBan = 0.0;

		if (tenSanPham.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống.", "Lỗi nhập liệu",
					JOptionPane.ERROR_MESSAGE);
			txtTenSanPham.requestFocus();
			return null;
		}

		try {
			giaBan = Double.parseDouble(txtGiaSanPham.getText().trim().replace(",", "").replace("₫", ""));
			if (giaBan < 0) {
				JOptionPane.showMessageDialog(this, "Giá phải là số không âm.", "Lỗi nhập liệu",
						JOptionPane.ERROR_MESSAGE);
				txtGiaSanPham.requestFocus();
				return null;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Giá phải là một số hợp lệ (ví dụ: 50000).", "Lỗi nhập liệu",
					JOptionPane.ERROR_MESSAGE);
			txtGiaSanPham.requestFocus();
			return null;
		}

		String chuoiLoaiSanPham = Objects.toString(cboLoaiSanPham.getSelectedItem(), "");
		LoaiSanPham loaiSanPham;
		try {
			loaiSanPham = LoaiSanPham.fromString(chuoiLoaiSanPham);
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, "Loại sản phẩm không hợp lệ.", "Lỗi nhập liệu",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return new SanPham(maSanPham, tenSanPham, giaBan, loaiSanPham);
	}

	private void timKiem() {
		String tuKhoaTimKiem = txtThanhTimKiem.getText().trim();
		String loaiDaChon = Objects.toString(cboLocTheoLoai.getSelectedItem(), "Tất cả");

		ArrayList<SanPham> dsKetQuaLoc = daoSanPham.getSanPhamByLoc(tuKhoaTimKiem, loaiDaChon);
		taiDanhSachSanPhamVaoPanel(pnlDanhSachMon, dsKetQuaLoc);
	}

	private void lamMoi() {
		txtMaSanPham.setText("");
		txtTenSanPham.setText("");
		txtGiaSanPham.setText("");
		cboLoaiSanPham.setSelectedIndex(0);
		lblHienThiAnh.setIcon(taiHinhAnhSanPham("data/images/default", false));
		duongDanAnhHienTai = null;
		sanPhamDaChon = null;

		txtThanhTimKiem.setText("");
		cboLocTheoLoai.setSelectedIndex(0);
		taiDanhSachSanPhamVaoPanel(pnlDanhSachMon, daoSanPham.getAllSanPham());
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Component thanhPhan = e.getComponent();
		JPanel pnlDuocNhan = null;

		while (thanhPhan != null) {
			if (thanhPhan instanceof Container && ((Container) thanhPhan).getLayout() instanceof BorderLayout) {
				if (thanhPhan instanceof JPanel) {
					pnlDuocNhan = (JPanel) thanhPhan;
				}
				break;
			}
			thanhPhan = thanhPhan.getParent();
		}

		if (pnlDuocNhan == null) {
			return;
		}

		Component thanhPhanThongTin = pnlDuocNhan.getComponent(1); 
		if (!(thanhPhanThongTin instanceof JPanel))
			return;

		Component[] dsThanhPhanCon = ((JPanel) thanhPhanThongTin).getComponents();
		String tenSanPham = "";
		for (Component comp : dsThanhPhanCon) {
			if (comp instanceof JLabel && "lblTenSP".equals(comp.getName())) {
				tenSanPham = ((JLabel) comp).getText();
				break;
			}
		}

		if (!tenSanPham.isEmpty()) {
			sanPhamDaChon = daoSanPham.getSanPhamByTen(tenSanPham);
			if (sanPhamDaChon != null) {
				txtMaSanPham.setText(sanPhamDaChon.getMaSanPham());
				txtTenSanPham.setText(sanPhamDaChon.getTenSanPham());
				txtGiaSanPham.setText(String.format("%,.0f", sanPhamDaChon.getGia()));
				cboLoaiSanPham.setSelectedItem(sanPhamDaChon.getLoaiSanPham().getMoTa());

				duongDanAnhHienTai = null; 
				lblHienThiAnh.setIcon(taiHinhAnhSanPham("data/images/" + sanPhamDaChon.getTenSanPham(), false));
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() instanceof JPanel) {
			JPanel pnlHover = (JPanel) e.getSource();
			if (pnlHover.getLayout() instanceof BorderLayout) { 
				pnlHover.setBackground(new Color(230, 245, 255));
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() instanceof JPanel) {
			JPanel pnlHover = (JPanel) e.getSource();
			if (pnlHover.getLayout() instanceof BorderLayout) { 
				pnlHover.setBackground(Color.WHITE);
			}
		}
	}
}