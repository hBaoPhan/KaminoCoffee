package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;

import connectDB.ConnectDB;
import dao.Ban_dao;
import dao.DonDatBan_dao;
import dao.HoaDon_dao;
import dao.KhachHang_dao;
import dao.SanPham_dao;
import entity.Ban;
import entity.DonDatBan;
import entity.KhachHang;
import entity.SanPham;
import entity.TrangThaiBan;

public class BanPanel extends JTabbedPane implements ActionListener, ChangeListener, ItemListener {

	private JLabel titleLabel;
	private JLabel lblTenBan;
	private JTextField txtTenKH;
	private JTextField txtSDT;
	private JTextField txtThoiGian;
	private DefaultTableModel modelMonAn;
	private JTable tableMonAn;
	private JLabel lblThanhTien;
	private JTextField txtTimKiemDoUong;
	private TimePicker txtThoiGianVao;
	private TimePicker txtThoiGianRa;
	private JButton btnLuu;
	private JToggleButton tggleDangSuDung;
	private JButton btnThanhToan;
	private JButton btnTimKiemDoUong;
	private JComboBox<String> cboFilterDoUong;

	private JLabel lblTenBan_DatBan;
	private JTextField txtTenKH_DatBan;
	private JLabel lblSDT_DatBan;
	private JTextField txtSDT_DatBan;
	private JLabel lblThoiGian_DatBan;
	private DatePicker txtNgay_DatBan;
	private JButton btnDatBan;
	private DefaultTableModel modelDatBan;
	private JTable tableDatBan;
	private Ban_dao banDao;

	private ImageIcon imgBanDangSuDung;
	private ImageIcon imgBanDaDuocDat;
	private ImageIcon imgBanTrong;

	private Font lblFont = new Font("Time New Roman", Font.BOLD, 20);
	Border lineBorder = BorderFactory.createLineBorder(Color.decode("#e07b39"), 5);
	Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
	private SanPham_dao sanPhamDao;
	private Dimension textFieldSize = new Dimension(600, 40);

	private DonDatBan_dao donDatBanDao;
	private JComboBox<String> cboFilterThoiGianDatBan;
	private TimePicker txtGio_DatBan;
	private JButton btnLocTheoThoiGian;
	private DatePicker txtFilterNgayDatBan;
	private TimePicker txtFilterGioDatBan;
	private JScrollPane jscrCacMon;
	private HoaDon_dao hoaDonDao;
	private KhachHang_dao khachHangDao;
	private JPanel pnlCacBan_DatBan;
	private JPanel pnlCacBan;

	public BanPanel() {

		// Tạo panel chính cho tab đầu tiên
		JPanel pnlDanhSachBan = new JPanel(new BorderLayout());

		// === Panel trái: Danh sách bàn ===
		Box BoxBan_Left = Box.createVerticalBox();
		BoxBan_Left.setAlignmentX(Component.LEFT_ALIGNMENT);
		BoxBan_Left.setBackground(Color.decode("#F7F4EC"));
		BoxBan_Left.setOpaque(true);

		BoxBan_Left.setBorder(new CompoundBorder(lineBorder, emptyBorder));

		Box BoxTitle = Box.createHorizontalBox();
		BoxTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel lblDanhSachBan = new JLabel("Danh Sách Bàn");
		lblDanhSachBan.setFont(lblFont);
		BoxTitle.add(lblDanhSachBan);
		lblDanhSachBan.setPreferredSize(new Dimension(200, 25));
		lblDanhSachBan.setMaximumSize(lblDanhSachBan.getPreferredSize());

//		lblDanhSachBan.setFont(new Font("Arial", Font.BOLD, 16));
		BoxBan_Left.add(BoxTitle);
		BoxBan_Left.add(Box.createVerticalStrut(10));

		Box boxCacBan = Box.createHorizontalBox();
		///////////////// Danh sách bàn
		///////////////// ///////////////////////////////////////////////////////////
		pnlCacBan = new JPanel(new GridLayout(0, 3, 5, 5));
		pnlCacBan.setMaximumSize(new Dimension(400, 500));
		pnlCacBan.setBackground(Color.decode("#F7F4EC"));
		boxCacBan.add(pnlCacBan);
		boxCacBan.setAlignmentX(Component.LEFT_ALIGNMENT);

		BoxBan_Left.add(boxCacBan);
		// === Panel giữa: Thông tin chi tiết bàn ===

		Box BoxThongTin_Center = Box.createVerticalBox();
		BoxThongTin_Center.setBackground(Color.decode("#F7F4EC"));
		BoxThongTin_Center.setOpaque(true);

		BoxThongTin_Center.setBorder(new CompoundBorder(lineBorder, emptyBorder));

		Box[] boxArrayThongTinBan = new Box[15];
		for (int i = 0; i < 15; i++) {
			BoxThongTin_Center.add(boxArrayThongTinBan[i] = Box.createHorizontalBox());
			boxArrayThongTinBan[i].setAlignmentX(Component.LEFT_ALIGNMENT);
			BoxThongTin_Center.add(Box.createVerticalStrut(10));
		}

		// Tiêu đề bàn
		lblTenBan = new JLabel("Bàn 1");
		boxArrayThongTinBan[0].add(lblTenBan);
		lblTenBan.setFont(lblFont);

		JLabel lblTenKH;
		JLabel lblSDTKH;
		JLabel lblThoiGianRa;
		JLabel lblThoiGianVao;
		// Thông tin khách hàng
		lblTenKH = new JLabel("Tên khách hàng:");
		lblTenKH.setPreferredSize(new Dimension(100, 25));
		lblTenKH.setMaximumSize(lblTenKH.getPreferredSize());

		boxArrayThongTinBan[1].add(lblTenKH);

		boxArrayThongTinBan[2].add(txtTenKH = new JTextField());
		txtTenKH.setPreferredSize(textFieldSize);

		boxArrayThongTinBan[3].add(lblSDTKH = new JLabel("Số điện thoại:"));
		lblSDTKH.setPreferredSize(new Dimension(100, 25));
		lblSDTKH.setMaximumSize(lblSDTKH.getPreferredSize());

		boxArrayThongTinBan[4].add(txtSDT = new JTextField());
		txtSDT.setPreferredSize(textFieldSize);

//      pnlThongTin_Center.add(new JLabel("Thời gian đặt:"));
//      pnlThongTin_Center.add(Box.createVerticalStrut(5));
//      pnlThongTin_Center.add(txtThoiGian=new JTextField("10:00"));
//      pnlThongTin_Center.add(Box.createVerticalStrut(10));

		boxArrayThongTinBan[5].add(lblThoiGianVao = new JLabel("Thời gian vào:"));
		lblThoiGianVao.setPreferredSize(new Dimension(100, 25));
		lblThoiGianVao.setMaximumSize(lblThoiGianVao.getPreferredSize());

		boxArrayThongTinBan[6].add(txtThoiGianVao = new TimePicker());
		txtThoiGianVao.setPreferredSize(textFieldSize);

		boxArrayThongTinBan[7].add(lblThoiGianRa = new JLabel("Thời gian ra:"));
		lblThoiGianRa.setPreferredSize(new Dimension(100, 25));
		lblThoiGianRa.setMaximumSize(lblThoiGianRa.getPreferredSize());

		boxArrayThongTinBan[8].add(txtThoiGianRa = new TimePicker());
		txtThoiGianRa.setPreferredSize(textFieldSize);
//		txtThoiGianRa.setTimeToNow();

		boxArrayThongTinBan[9].add(new JLabel("Danh sách món:"));

		String header[] = { "Tên món", "Số lượng", "Đơn giá", "Thành tiền" };
		modelMonAn = new DefaultTableModel(header, 0);
		tableMonAn = new JTable(modelMonAn);
		tableMonAn.setBackground(Color.WHITE);
		JScrollPane scrDanhSachMon = new JScrollPane(tableMonAn);
		boxArrayThongTinBan[10].add(scrDanhSachMon);
		// Tong Tien
		JLabel lblTongTien = new JLabel("Tổng tiền: ");
		lblTongTien.setPreferredSize(new Dimension(100, 25));
		lblTongTien.setMaximumSize(lblTongTien.getPreferredSize());

		lblThanhTien = new JLabel("0");
		lblThanhTien.setMaximumSize(lblThanhTien.getPreferredSize());

		Box tongTienBox = Box.createHorizontalBox();
		tongTienBox.add(lblTongTien);
		tongTienBox.add(Box.createHorizontalStrut(560));
		tongTienBox.add(lblThanhTien);
		boxArrayThongTinBan[11].add(tongTienBox);

		tggleDangSuDung = new JToggleButton("Đang sử dụng");
		tggleDangSuDung.setBorder(BorderFactory.createLineBorder(Color.decode("#e07b39"), 5, true)); /////// Màu cam///
		tggleDangSuDung.setForeground(Color.WHITE);
		tggleDangSuDung.setPreferredSize(new Dimension(410, 40));
		tggleDangSuDung.setMaximumSize(tggleDangSuDung.getPreferredSize());
		tggleDangSuDung.setBackground(Color.decode("#e07b39"));
		btnLuu = new JButton("Lưu");
		btnLuu.setBorder(BorderFactory.createLineBorder(Color.decode("#e07b39"), 5, true)); /////// Màu cam///
		btnLuu.setForeground(Color.WHITE);
		btnLuu.setPreferredSize(new Dimension(410, 40));
		btnLuu.setMaximumSize(btnLuu.getPreferredSize());
		btnLuu.setBackground(Color.decode("#e07b39"));

		Box buttonBox = Box.createHorizontalBox();

		buttonBox.add(tggleDangSuDung);
		buttonBox.add(Box.createHorizontalStrut(30));
		buttonBox.add(btnLuu);
		boxArrayThongTinBan[12].add(buttonBox);
		btnThanhToan = new JButton("Thanh Toán");
		btnThanhToan.setBorder(BorderFactory.createLineBorder(Color.decode("#00A651"), 5, true)); /////// Màu cam///
		btnThanhToan.setForeground(Color.WHITE);
		btnThanhToan.setPreferredSize(new Dimension(850, 50));
		btnThanhToan.setMaximumSize(btnThanhToan.getPreferredSize());
		btnThanhToan.setBackground(Color.decode("#00A651"));
		boxArrayThongTinBan[13].add(Box.createVerticalStrut(15));
		boxArrayThongTinBan[14].add(btnThanhToan);

		// === Panel phải: Thực đơn đồ uống ===
		Box BoxThucDon_Right = Box.createVerticalBox();

		BoxThucDon_Right.setBackground(Color.decode("#F7F4EC"));
		BoxThucDon_Right.setOpaque(true);
		BoxThucDon_Right.setBorder(new CompoundBorder(lineBorder, emptyBorder));

		Box[] boxArrayThucDon = new Box[15];
		for (int i = 0; i < boxArrayThucDon.length; i++) {
			BoxThucDon_Right.add(boxArrayThucDon[i] = Box.createHorizontalBox());
			BoxThucDon_Right.add(Box.createVerticalStrut(10));
			boxArrayThucDon[i].setAlignmentX(Component.LEFT_ALIGNMENT);
		}

		JLabel lblMenuDoUong = new JLabel("Menu đồ uống");
		lblMenuDoUong.setFont(lblFont);
		txtTimKiemDoUong = new JTextField(15);

		txtTimKiemDoUong.setPreferredSize(new Dimension(150, 30));
		txtTimKiemDoUong.setMaximumSize(txtTimKiemDoUong.getPreferredSize());

		btnTimKiemDoUong = new JButton("Tìm kiếm");
		btnTimKiemDoUong.setPreferredSize(new Dimension(60, 30));
		btnTimKiemDoUong.setMaximumSize(btnTimKiemDoUong.getPreferredSize());
		btnTimKiemDoUong.setBackground(Color.decode("#00A651"));
		btnTimKiemDoUong.setBorder(BorderFactory.createLineBorder(Color.decode("#00A651"), 5, true));
		btnTimKiemDoUong.setForeground(Color.WHITE);

		cboFilterDoUong = new JComboBox<String>();
		cboFilterDoUong.setPreferredSize(new Dimension(300, 30));
		cboFilterDoUong.setMaximumSize(cboFilterDoUong.getPreferredSize());

		JPanel pnlCacMon = new JPanel(new GridLayout(0, 3, 5, 5));
		Box boxCacMon = Box.createHorizontalBox();
		jscrCacMon = new JScrollPane(pnlCacMon);
		//////////////////////////////// Danh sách món
		//////////////////////////////// ///////////////////////////////////////////////////////////
		boxCacMon.add(pnlCacMon);

		boxArrayThucDon[0].add(lblMenuDoUong);
		boxArrayThucDon[1].add(txtTimKiemDoUong);
		boxArrayThucDon[1].add(Box.createHorizontalStrut(5));
		boxArrayThucDon[1].add(btnTimKiemDoUong);
		boxArrayThucDon[1].setMaximumSize(new Dimension(300, 30));
		boxArrayThucDon[2].add(cboFilterDoUong);
		boxArrayThucDon[3].add(boxCacMon);

		//////////////////////////// === Panel đặt bàn
		//////////////////////////// ===///////////////////////////////////////////
		JPanel pnlDatBan = new JPanel(new BorderLayout());

		Box BoxDanhSachBan_DatBan = Box.createVerticalBox();
		BoxDanhSachBan_DatBan.setBorder(new CompoundBorder(lineBorder, emptyBorder));

		pnlDatBan.setBackground(Color.decode("#F7F4EC"));

////////////////////////////////////////////////////Thêm vào Panel bên đặt bàn
		JLabel lblDanhSachBan_DatBan = new JLabel("Danh Sách Bàn");
		lblDanhSachBan_DatBan.setFont(lblFont);
		Box boxTitleDatBan = Box.createHorizontalBox();
		boxTitleDatBan.setAlignmentX(Component.LEFT_ALIGNMENT);
		boxTitleDatBan.add(lblDanhSachBan_DatBan);

		txtFilterNgayDatBan = new DatePicker();
		txtFilterNgayDatBan.setPreferredSize(new Dimension(60, 30));

		txtFilterGioDatBan = new TimePicker();
		txtFilterGioDatBan.setPreferredSize(new Dimension(50, 30));

		Box boxFilterThoiGianDatBan = Box.createHorizontalBox();
		boxFilterThoiGianDatBan.setMaximumSize(new Dimension(300, 30));
		boxFilterThoiGianDatBan.setAlignmentX(Component.LEFT_ALIGNMENT);

		boxFilterThoiGianDatBan.add(txtFilterNgayDatBan);
		boxFilterThoiGianDatBan.add(Box.createHorizontalStrut(5));
		boxFilterThoiGianDatBan.add(txtFilterGioDatBan);
		boxFilterThoiGianDatBan.add(Box.createHorizontalStrut(5));

		Box boxBtnLoc = Box.createHorizontalBox();
		boxBtnLoc.setAlignmentX(Component.LEFT_ALIGNMENT);
		boxBtnLoc.setMaximumSize(new Dimension(300, 30));
		btnLocTheoThoiGian = new JButton("Lọc");
		btnLocTheoThoiGian.setBackground(Color.decode("#00A651"));
		btnLocTheoThoiGian.setForeground(Color.white);
		btnLocTheoThoiGian.setPreferredSize(new Dimension(150, 30));
		btnLocTheoThoiGian.setMaximumSize(btnLocTheoThoiGian.getPreferredSize());
		boxBtnLoc.add(btnLocTheoThoiGian);
		Box BoxCacBan_DatBan = Box.createHorizontalBox();
		pnlCacBan_DatBan = new JPanel(new GridLayout(0, 3, 5, 5));
		pnlCacBan_DatBan.setMaximumSize(new Dimension(400, 500));
		pnlCacBan_DatBan.setBackground(Color.decode("#F7F4EC"));

		BoxDanhSachBan_DatBan.add(boxTitleDatBan);
		BoxDanhSachBan_DatBan.add(Box.createVerticalStrut(10));
		BoxDanhSachBan_DatBan.add(boxFilterThoiGianDatBan);
		BoxDanhSachBan_DatBan.add(Box.createVerticalStrut(10));
		BoxDanhSachBan_DatBan.add(boxBtnLoc);
		BoxDanhSachBan_DatBan.add(Box.createVerticalStrut(10));
		BoxCacBan_DatBan.add(pnlCacBan_DatBan);
		BoxCacBan_DatBan.setAlignmentX(Component.LEFT_ALIGNMENT);
		BoxDanhSachBan_DatBan.add(BoxCacBan_DatBan);

		Box BoxThongTin_DatBan = Box.createVerticalBox();
		BoxThongTin_DatBan.setBorder(new CompoundBorder(lineBorder, emptyBorder));

		Box[] boxArrayThongTinDatBan = new Box[10];
		for (int i = 0; i < boxArrayThongTinDatBan.length; i++) {
			BoxThongTin_DatBan.add(boxArrayThongTinDatBan[i] = Box.createHorizontalBox());
			BoxThongTin_DatBan.add(Box.createVerticalStrut(10));
			boxArrayThongTinDatBan[i].setAlignmentX(Component.LEFT_ALIGNMENT);
		}
		JLabel lblThongTinDatBan = new JLabel("Thông Tin Đặt Bàn");
		lblThongTinDatBan.setFont(lblFont);
		lblTenBan_DatBan = new JLabel("Bàn 1");
		lblTenBan_DatBan.setFont(lblFont);
		txtTenKH_DatBan = new JTextField();
		txtTenKH_DatBan.setPreferredSize(new Dimension(800, 30));
		txtTenKH_DatBan.setMaximumSize(txtTenKH_DatBan.getPreferredSize());
		lblSDT_DatBan = new JLabel("Số điện thoại:");
		txtSDT_DatBan = new JTextField();
		txtSDT_DatBan.setPreferredSize(new Dimension(800, 30));
		txtSDT_DatBan.setMaximumSize(txtSDT_DatBan.getPreferredSize());
		lblThoiGian_DatBan = new JLabel("Thời gian đặt:");
		txtNgay_DatBan = new DatePicker();
		txtNgay_DatBan.setPreferredSize(new Dimension(275, 30));
		txtNgay_DatBan.setMaximumSize(txtNgay_DatBan.getPreferredSize());

		txtGio_DatBan = new TimePicker();
		txtGio_DatBan.setPreferredSize(new Dimension(275, 30));
		txtGio_DatBan.setMaximumSize(txtNgay_DatBan.getPreferredSize());

		btnDatBan = new JButton("Đặt Bàn");
		btnDatBan.setBorder(BorderFactory.createLineBorder(Color.decode("#e07b39"), 5, true)); /////// Màu cam///
		btnDatBan.setForeground(Color.WHITE);
		btnDatBan.setPreferredSize(new Dimension(800, 40));
		btnDatBan.setMaximumSize(btnDatBan.getPreferredSize());
		btnDatBan.setBackground(Color.decode("#e07b39"));

		boxArrayThongTinDatBan[0].add(lblThongTinDatBan);
		boxArrayThongTinDatBan[1].add(lblTenBan_DatBan);
		boxArrayThongTinDatBan[2].add(new JLabel("Tên khách hàng:"));
		boxArrayThongTinDatBan[3].add(txtTenKH_DatBan);
		boxArrayThongTinDatBan[4].add(lblSDT_DatBan);
		boxArrayThongTinDatBan[5].add(txtSDT_DatBan);
		boxArrayThongTinDatBan[6].add(lblThoiGian_DatBan);
		boxArrayThongTinDatBan[7].add(txtNgay_DatBan);
		boxArrayThongTinDatBan[7].add(Box.createHorizontalStrut(10));
		boxArrayThongTinDatBan[7].add(txtGio_DatBan);
		boxArrayThongTinDatBan[7].setMaximumSize(new Dimension(600, 30));
		boxArrayThongTinDatBan[8].add(Box.createVerticalStrut(15));
		boxArrayThongTinDatBan[9].add(btnDatBan);

		Box DanhSachDonDatBan = Box.createVerticalBox();
		DanhSachDonDatBan.setBorder(new CompoundBorder(lineBorder, emptyBorder));

		Box[] boxArrayDanhSachDonDatBan = new Box[10];
		for (int i = 0; i < boxArrayDanhSachDonDatBan.length; i++) {
			DanhSachDonDatBan.add(boxArrayDanhSachDonDatBan[i] = Box.createHorizontalBox());
			DanhSachDonDatBan.add(Box.createVerticalStrut(10));
			boxArrayDanhSachDonDatBan[i].setAlignmentX(Component.LEFT_ALIGNMENT);
		}
		JLabel lblDanhSachDonDatBan = new JLabel("Danh Sách Đơn Đặt Bàn");
		lblDanhSachDonDatBan.setFont(lblFont);

		String headerDatBan[] = { "Mã Đơn đặt bàn", "Tên Khách Hàng", "Mã khách hàng", "Bàn", "Thời gian" };
		modelDatBan = new DefaultTableModel(headerDatBan, 0);
		tableDatBan = new JTable(modelDatBan);
		TableColumnModel columnModel = tableDatBan.getColumnModel();

		// Đặt kích thước cho cột thứ 0 (cột đầu tiên)
		columnModel.getColumn(4).setPreferredWidth(110);
		columnModel.getColumn(0).setPreferredWidth(100);
		columnModel.getColumn(1).setPreferredWidth(130);
		columnModel.getColumn(3).setPreferredWidth(30);
		columnModel.getColumn(4).setMinWidth(100); // chiều rộng tối thiểu
		columnModel.getColumn(4).setMaxWidth(200);
		tableDatBan.setBackground(Color.WHITE);
		tableDatBan.setOpaque(true);
		tableMonAn.setOpaque(true);
		JScrollPane scrPaneDanhSachDonDatBan = new JScrollPane(tableDatBan);

		boxArrayDanhSachDonDatBan[0].add(lblDanhSachDonDatBan);
		boxArrayDanhSachDonDatBan[1].add(scrPaneDanhSachDonDatBan);

		pnlDatBan.add(BoxDanhSachBan_DatBan, BorderLayout.WEST);
		pnlDatBan.add(BoxThongTin_DatBan, BorderLayout.CENTER);
		pnlDatBan.add(DanhSachDonDatBan, BorderLayout.EAST);

		// Gắn các panel vào mainPanel
		pnlDanhSachBan.add(BoxBan_Left, BorderLayout.WEST);
		pnlDanhSachBan.add(BoxThongTin_Center, BorderLayout.CENTER);
		pnlDanhSachBan.add(BoxThucDon_Right, BorderLayout.EAST);

		// Thêm tab đầu tiên
		this.addTab("Danh Sách Bàn", pnlDanhSachBan);
		this.addTab("Đặt Bàn", pnlDatBan);

		banDao = new Ban_dao();
		sanPhamDao = new SanPham_dao();
		donDatBanDao = new DonDatBan_dao();
		hoaDonDao = new HoaDon_dao();
		khachHangDao = new KhachHang_dao();
		//////////////////////////////
		initIcons();

		///////////////////////
		// Bàn bên ds Bàn
		updateTrangThaiDatBan();
		themBanVaoPanel(pnlCacBan, banDao.getAllBan());
		//////////////////////////////////////////////////////////////////////////
		themBanVaoPanel(pnlCacBan_DatBan, banDao.getAllBan());/// Bàn bên đặt bàn

		////////////////////
		loadSanPhamVaoPanel(pnlCacMon, sanPhamDao.getAllSanPham()); // Danh sách món

		/////////////////////////////////
		updateTableDonDatBanTuDao(donDatBanDao.getAllDonDatBan()); // Danh Sách Đơn đặt bàn
		///////////////////////////////////

		this.addChangeListener(this);
		btnDatBan.addActionListener(this);
		btnLocTheoThoiGian.addActionListener(this);
		btnTimKiemDoUong.addActionListener(this);
		btnThanhToan.addActionListener(this);
		btnLuu.addActionListener(this);

	}

	public boolean checkValidDataDatBan() {
		String tenKH = txtTenKH_DatBan.getText().trim();
		String sdtKH = txtSDT_DatBan.getText().trim();
		LocalDateTime thoiGianDatBan = null;
		try {
			thoiGianDatBan = LocalDateTime.of(txtNgay_DatBan.getDate(), txtGio_DatBan.getTime());

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày và giờ đặt bàn!");
		}
		if (!tenKH.matches(
				"([A-ZĐ][a-zàầáảãạăâđèéẻẽễẹêìíỉĩịòốóỏõọôơùúủũụưỳýỷỹỵ]+ )+[A-ZĐ][a-zàầáảãạăâđèéẻẽễẹêìíỉĩịòốóỏõọôơùúủũụưỳýỷỹỵ]+")) {
			JOptionPane.showMessageDialog(this, "Tên khách hàng không hợp lệ! Vui lòng nhập lại.");
			txtTenKH_DatBan.requestFocus();
			return false;
		}
		if (!sdtKH.matches("0\\d{9}")) {
			JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ! Vui lòng nhập lại.");
			txtSDT_DatBan.requestFocus();
			return false;
		}

//		if(txtNgay_DatBan.getDate() == null || txtGio_DatBan.getTime() == null) {
//			JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày và giờ đặt bàn!");
//			txtNgay_DatBan.requestFocus();
//			return false;
//		}
		if (thoiGianDatBan.isBefore(LocalDateTime.now())) {
			JOptionPane.showMessageDialog(this, "Thời gian đặt bàn phải sau thời gian hiện tại! Vui lòng chọn lại.");
			txtGio_DatBan.requestFocus();
			return false;
		}
		return true;
	}

	public void themBanVaoPanel(JPanel pnl, ArrayList<Ban> danhSachBan) {
		pnl.removeAll();
		ButtonGroup tableGroup = new ButtonGroup();
		for (Ban ban : danhSachBan) {
			String ten = ban.getTenBan();
			if (ban.getTrangThai() == TrangThaiBan.DaDuocDat) {
				donDatBanDao.getAllDonDatBan();
				for (DonDatBan ddb : donDatBanDao.getAllDonDatBan()) {
					if (ddb.getBan().getMaBan().equals(ban.getMaBan())
							&& ddb.getThoiGian().isAfter(LocalDateTime.now())) {
						ten += " (" + ddb.getThoiGian().getHour() + ":"
								+ String.format("%02d", ddb.getThoiGian().getMinute()) + ")";
						;
						break;
					}
				}

			}

			ImageIcon icon = getIconForTrangThai(ban.getTrangThai());
			JRadioButton radBan = new JRadioButton(ten, icon);
			radBan.setVerticalTextPosition(SwingConstants.TOP);
			radBan.setHorizontalTextPosition(SwingConstants.CENTER);
			radBan.setHorizontalAlignment(SwingConstants.CENTER);
			radBan.setPreferredSize(new Dimension(100, 80));
			radBan.setMaximumSize(radBan.getPreferredSize());
			radBan.setBackground(Color.decode("#F7F4EC"));
			radBan.setOpaque(true);
			radBan.addActionListener(this);
			radBan.addItemListener(this);
			tableGroup.add(radBan);
			pnl.add(radBan);
		}

		pnl.revalidate();
		pnl.repaint();

	}

	public void initIcons() {
		imgBanTrong = loadIconBan("data/images/emptyTable.png");
		imgBanDaDuocDat = loadIconBan("data/images/reservedTable.png");
		imgBanDangSuDung = loadIconBan("data/images/InUseTable.png");
	}

	public ImageIcon loadIconBan(String relativePath) {
		File file = new File(relativePath);
		if (!file.exists()) {
			System.err.println("Không tìm thấy ảnh: " + relativePath);
			return null;
		}
		return new ImageIcon(
				new ImageIcon(file.getAbsolutePath()).getImage().getScaledInstance(93, 93, Image.SCALE_SMOOTH));
	}

	public ImageIcon getIconForTrangThai(TrangThaiBan trangThai) {
		switch (trangThai) {
		case Trong:
			return imgBanTrong;
		case DaDuocDat:
			return imgBanDaDuocDat;
		case DangDuocSuDung:
			return imgBanDangSuDung;
		default:
			return imgBanTrong;
		}
	}

	public void loadSanPhamVaoPanel(JPanel panel, ArrayList<SanPham> danhSachSanPham) {

		for (SanPham sp : danhSachSanPham) {
			String tenSP = sp.getTenSanPham();
			ImageIcon icon = loadSanPhamIcon("data/images/" + tenSP);
			JButton btn = new JButton(tenSP, icon);
			btn.setPreferredSize(new Dimension(100, 100));
			btn.setMaximumSize(btn.getPreferredSize());
			btn.setBackground(Color.WHITE);
			btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
			btn.setToolTipText(tenSP + " - " + String.format("%.0f", sp.getGia()) + " VND");
			btn.setVerticalTextPosition(SwingConstants.TOP);
			btn.setHorizontalTextPosition(SwingConstants.CENTER);
			btn.setHorizontalAlignment(SwingConstants.CENTER);
			panel.add(btn);
		}

		panel.revalidate();
		panel.repaint();
	}

	public ImageIcon loadSanPhamIcon(String tenSP) {
		String[] extensions = { ".png", ".jpg", ".jpeg" };
		for (String ext : extensions) {
			File file = new File(tenSP + ext);
			if (file.exists()) {
				return new ImageIcon(
						new ImageIcon(file.getAbsolutePath()).getImage().getScaledInstance(85, 85, Image.SCALE_SMOOTH));
			} else {
//	        	System.out.println("Không tìm thấy ảnh: " + file.getAbsolutePath());
			}
		}
		return new ImageIcon(new ImageIcon(new File("data/images/default.png").getAbsolutePath()).getImage()
				.getScaledInstance(85, 85, Image.SCALE_SMOOTH));

	}

	public void updateTableDonDatBanTuDao(ArrayList<DonDatBan> ds) {
		modelDatBan.getDataVector().removeAllElements();
		ArrayList<KhachHang> dsKH = khachHangDao.getAllKhachHang();

		for (DonDatBan ddb : ds) {
			for (KhachHang kh : dsKH) {
				if (ddb.getKhachHang().getMaKhachHang().equals(kh.getMaKhachHang())) {
					modelDatBan.addRow(new Object[] { ddb.getMaDonDatBan(), kh.getTenKhachHang(),
							ddb.getKhachHang().getMaKhachHang(), ddb.getBan().getMaBan(), ddb.getThoiGian().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")) });
					break;
				}
			}

		}
		modelDatBan.fireTableDataChanged();
	}

	public void updateTrangThaiDatBan() {
		ArrayList<DonDatBan> ds = donDatBanDao.getAllDonDatBan();
		for (DonDatBan ddb : ds) {
			Ban ban = ddb.getBan();
			for (Ban b : banDao.getAllBan()) {
				if (b.getMaBan().equals(ban.getMaBan())) {
					if (b.getTrangThai() != TrangThaiBan.DangDuocSuDung) {
						if (Duration.between(LocalDateTime.now(), ddb.getThoiGian()).toMinutes() <= 60
								&& Duration.between(LocalDateTime.now(), ddb.getThoiGian()).toMinutes() >= 0) {
//							System.out.println(Duration.between(LocalDateTime.now(),ddb.getThoiGian()).toMinutes());
							b.setTrangThai(TrangThaiBan.DaDuocDat);
							banDao.updateBan(b);
						}
					}
				}
			}

		}

	}

	public String generateNextCodeForDDB(String lastCode) {
		ArrayList<DonDatBan> dsDDB = donDatBanDao.getAllDonDatBan();
		String prefix = lastCode.substring(0, 3); // "HD"
		int number = Integer.parseInt(lastCode.substring(3)); // 001 → 1
		String newMa = String.format("%s%03d", prefix, number + 1);
		for (DonDatBan ddb : dsDDB) {
			if (ddb.getMaDonDatBan().equals(newMa)) {
				return generateNextCodeForDDB(newMa);
			}
		}
		return newMa;

	}

	public String generateNextCodeForKhachHang(String lastCode) {
		ArrayList<KhachHang> dsKH = khachHangDao.getAllKhachHang();
		String prefix = lastCode.substring(0, 2); // "HD"
		int number = Integer.parseInt(lastCode.substring(2)); // 001 → 1
		String newMa = String.format("%s%03d", prefix, number + 1);
		for (KhachHang kh : dsKH) {
			if (kh.getMaKhachHang().equals(newMa)) {
				return generateNextCodeForKhachHang(newMa);
			}
		}
		return newMa;
	}

	public boolean kiemTraKhongTrungThoiGian(DonDatBan ddbMoi) {
		for (DonDatBan ddb : donDatBanDao.getAllDonDatBan()) {
			if (ddb.getBan().getMaBan().equals(ddbMoi.getBan().getMaBan())) {
				long khoangCach = Math.abs(Duration.between(ddb.getThoiGian(), ddbMoi.getThoiGian()).toMinutes());
//				JOptionPane.showMessageDialog(this, "Khoảng cách: " + khoangCach);
				if (khoangCach < 60 && khoangCach > -60) {
					return false;
				}
			}
		}
		return true;
	}
	public ArrayList<Ban> setTrangThaiBanVeTrongChoViecFilterBan(ArrayList<Ban> danhSachBan) {
		for (Ban ban : danhSachBan) {
			ban.setTrangThai(TrangThaiBan.Trong);
		}
		return danhSachBan;
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnDatBan)) {
			if (checkValidDataDatBan()) {
				String tenKH = txtTenKH_DatBan.getText().trim();
				String sdtKH = txtSDT_DatBan.getText().trim();
				LocalDateTime thoiGianDatBan = LocalDateTime.of(txtNgay_DatBan.getDate(), txtGio_DatBan.getTime());

				// Lấy thông tin bàn từ lblTenBan_DatBan
				String tenBan = lblTenBan_DatBan.getText();
				Ban banDat = null;
				for (Ban ban : banDao.getAllBan()) {
					if (ban.getTenBan().equals(tenBan)) {
						banDat = ban;
						break;
					}
				}
				String lastMaDonDatBan = donDatBanDao.getAllDonDatBan().getLast().getMaDonDatBan();
				String maDonDatBan = generateNextCodeForDDB(lastMaDonDatBan);
				String maKhachHang = generateNextCodeForKhachHang(
						donDatBanDao.getAllDonDatBan().getLast().getKhachHang().getMaKhachHang());
				DonDatBan ddb = new DonDatBan(maDonDatBan, new KhachHang(maKhachHang, tenKH, sdtKH, 0, false), banDat,
						thoiGianDatBan);

				if (kiemTraKhongTrungThoiGian(ddb) == true) {
					int confirm = JOptionPane.showConfirmDialog(this,
							"Bạn có muốn đăng ký thành viên cho khách hàng  " + tenKH + " không?", "Xác nhận",
							JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						if (khachHangDao.addKhachHang(new KhachHang(maKhachHang, tenKH, sdtKH, true))
								&& donDatBanDao.addDonDatBan(ddb)) {
							JOptionPane.showMessageDialog(this,
									"Đặt bàn thành công và đăng ký thành viên cho khách hàng " + tenKH);
							updateTrangThaiDatBan();
							updateTableDonDatBanTuDao(donDatBanDao.getAllDonDatBan());
							themBanVaoPanel(pnlCacBan_DatBan, banDao.getAllBan());/// Bàn bên đặt bàn
							themBanVaoPanel(pnlCacBan, banDao.getAllBan());
						}
					} else if (confirm == JOptionPane.NO_OPTION) {
						if (khachHangDao.addKhachHang(new KhachHang(maKhachHang, tenKH, sdtKH, false))
								&& donDatBanDao.addDonDatBan(ddb)) {
							JOptionPane.showMessageDialog(this, "Đặt bàn thành công cho khách hàng " + tenKH);
							updateTrangThaiDatBan();
							updateTableDonDatBanTuDao(donDatBanDao.getAllDonDatBan());

						} else {
							JOptionPane.showMessageDialog(this, "Đặt bàn không thành công! Vui lòng thử lại.");
						}
					}

				} else {
					JOptionPane.showMessageDialog(this,
							"Đặt bàn không thành công!\nDo bàn đã được đặt vào khoảng thời gian này!\nVui lòng đặt bàn trước hoặc sau đó 60p gần với thời gian mà bạn mong muốn");
					return;

				}

			}
			}
			if(o.equals(btnLocTheoThoiGian)) {
				LocalDate date = txtFilterNgayDatBan.getDate();
				LocalTime time = txtFilterGioDatBan.getTime();
				if(date == null || time == null) {
					JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày và giờ để lọc!");
					themBanVaoPanel(pnlCacBan_DatBan, banDao.getAllBan());
					return;
				}
				LocalDateTime filterDateTime = LocalDateTime.of(date, time);
				ArrayList<Ban> filteredBan = new ArrayList<>();
				for(Ban ban : banDao.getAllBan()) {
					boolean isAvailable = true;
					for(DonDatBan ddb : donDatBanDao.getAllDonDatBan()) {
						if(ddb.getBan().getMaBan().equals(ban.getMaBan()) && ddb.getThoiGian().isAfter(LocalDateTime.now())) {
							long khoangCach = Math.abs(Duration.between(ddb.getThoiGian(), filterDateTime).toMinutes());
							if(khoangCach < 60 && khoangCach > -60) {
								isAvailable = false;
								break;
							}
						}
					}
					if(isAvailable) {
						filteredBan.add(ban);
					}
				}
				
				themBanVaoPanel(pnlCacBan_DatBan, setTrangThaiBanVeTrongChoViecFilterBan(filteredBan));
				
				lblTenBan_DatBan.setText(filteredBan.get(0).getTenBan());
			}
		

	if(o instanceof JRadioButton)

	{

		JRadioButton btn = (JRadioButton) o;
		lblTenBan.setText(btn.getText());
		lblTenBan_DatBan.setText(btn.getText());

		for (Ban ban : banDao.getAllBan()) {
			if (ban.getTenBan().equals(btn.getText())) {

			}
		}
	}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		updateTrangThaiDatBan();
		updateTableDonDatBanTuDao(donDatBanDao.getAllDonDatBan());

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		JRadioButton source = (JRadioButton) e.getItem();
        if (source.isSelected()) {
        	 source.setForeground(Color.decode("#00A651"));
        } else {
            source.setForeground(Color.BLACK);
        }
	}

}
