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
import javax.swing.table.DefaultTableModel;

public class BanPanel extends JTabbedPane implements ActionListener {

	private JLabel titleLabel;
	private JLabel lblTenBan;
	private JTextField txtTenKH;
	private JTextField txtSDT;
	private JTextField txtThoiGian;
	private DefaultTableModel modelMonAn;
	private JTable tableMonAn;
	private JLabel lblThanhTien;
	private JTextField txtTimKiemDoUong;
	Border lineBorder = BorderFactory.createLineBorder(Color.decode("#e07b39"), 5);
	Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
	private JButton btnLuu;
	private JToggleButton tggleDangSuDung;
	private JButton btnThanhToan;
	private JButton btnTimKiemDoUong;
	private JComboBox<String> cboFilterDoUong;
	private ImageIcon resizedIcon;
	private Font lblFont = new Font("Time New Roman", Font.BOLD, 20);
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

		ImageIcon LogoIcon = new ImageIcon("images/reservedTable.png");
		Image scaledImage = LogoIcon.getImage().getScaledInstance(93, 93, Image.SCALE_SMOOTH);
		resizedIcon = new ImageIcon(scaledImage);
		
		
		Box boxCacBan=Box.createHorizontalBox();
		///////////////// Danh sách bàn ///////////////////////////////////////////////////////////
		JPanel pnlCacBan = new JPanel(new GridLayout(6, 3, 5, 5));
		themBanVaoPanel(pnlCacBan);
		boxCacBan.add(pnlCacBan);
		boxCacBan.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		BoxBan_Left.add(boxCacBan);
		// === Panel giữa: Thông tin chi tiết bàn ===

		Box BoxThongTin_Center = Box.createVerticalBox();
		BoxThongTin_Center.setBackground(Color.decode("#F7F4EC"));
		BoxThongTin_Center.setOpaque(true);
		
		BoxThongTin_Center.setBorder(new CompoundBorder(lineBorder, emptyBorder));
		
		Box[] boxArray = new Box[15];
		for (int i = 0; i < 15; i++) {
			BoxThongTin_Center.add(boxArray[i] = Box.createHorizontalBox());
			boxArray[i].setAlignmentX(Component.LEFT_ALIGNMENT);
			BoxThongTin_Center.add(Box.createVerticalStrut(10));
		}

		// Tiêu đề bàn
		lblTenBan = new JLabel("Bàn 1");
		boxArray[0].add(lblTenBan);
		lblTenBan.setFont(lblFont);

		JLabel lblTenKH;
		JLabel lblSDTKH;
		JLabel lblThoiGianRa;
		JLabel lblThoiGianVao;
		// Thông tin khách hàng
		lblTenKH = new JLabel("Tên khách hàng:");
		lblTenKH.setPreferredSize(new Dimension(100, 25));
		lblTenKH.setMaximumSize(lblTenKH.getPreferredSize());

		boxArray[1].add(lblTenKH);

		boxArray[2].add(txtTenKH = new JTextField());

		boxArray[3].add(lblSDTKH = new JLabel("Số điện thoại:"));
		lblSDTKH.setPreferredSize(new Dimension(100, 25));
		lblSDTKH.setMaximumSize(lblSDTKH.getPreferredSize());

		boxArray[4].add(txtSDT = new JTextField());

//      pnlThongTin_Center.add(new JLabel("Thời gian đặt:"));
//      pnlThongTin_Center.add(Box.createVerticalStrut(5));
//      pnlThongTin_Center.add(txtThoiGian=new JTextField("10:00"));
//      pnlThongTin_Center.add(Box.createVerticalStrut(10));

		boxArray[5].add(lblThoiGianVao = new JLabel("Thời gian vào:"));
		lblThoiGianVao.setPreferredSize(new Dimension(100, 25));
		lblThoiGianVao.setMaximumSize(lblThoiGianVao.getPreferredSize());

		boxArray[6].add(new JTextField());

		boxArray[7].add(lblThoiGianRa = new JLabel("Thời gian ra:"));
		lblThoiGianRa.setPreferredSize(new Dimension(100, 25));
		lblThoiGianRa.setMaximumSize(lblThoiGianRa.getPreferredSize());
	

		boxArray[8].add(new JTextField());

		boxArray[9].add(new JLabel("Danh sách món:"));

		String header[] = { "Tên món", "Số lượng", "Đơn giá", "Thành tiền" };
		modelMonAn = new DefaultTableModel(header, 0);
		tableMonAn = new JTable(modelMonAn);
		JScrollPane scrDanhSachMon = new JScrollPane(tableMonAn);
		boxArray[10].add(scrDanhSachMon);
		//      Tong Tien
		JLabel lblTongTien = new JLabel("Tổng tiền: ");
		lblTongTien.setPreferredSize(new Dimension(100, 25));
		lblTongTien.setMaximumSize(lblTongTien.getPreferredSize());
	
		lblThanhTien = new JLabel("0");
		lblThanhTien.setMaximumSize(lblThanhTien.getPreferredSize());
		
		Box tongTienBox = Box.createHorizontalBox();
		tongTienBox.add(lblTongTien);
		tongTienBox.add(Box.createHorizontalStrut(740));
		tongTienBox.add(lblThanhTien);
		boxArray[11].add(tongTienBox);
		
		tggleDangSuDung=new JToggleButton("Đang sử dụng");
		tggleDangSuDung.setBorder(BorderFactory.createLineBorder(Color.decode("#e07b39"), 5, true)); /////// Màu cam///
		tggleDangSuDung.setForeground(Color.WHITE);
		tggleDangSuDung.setPreferredSize(new Dimension(410, 40));
		tggleDangSuDung.setMaximumSize(tggleDangSuDung.getPreferredSize());
		tggleDangSuDung.setBackground(Color.decode("#e07b39"));
		btnLuu=new JButton("Lưu");
		btnLuu.setBorder(BorderFactory.createLineBorder(Color.decode("#e07b39"), 5, true)); /////// Màu cam///
		btnLuu.setForeground(Color.WHITE);
		btnLuu.setPreferredSize(new Dimension(410, 40));
		btnLuu.setMaximumSize(btnLuu.getPreferredSize());
		btnLuu.setBackground(Color.decode("#e07b39"));
		
		Box buttonBox=Box.createHorizontalBox();
		
		buttonBox.add(tggleDangSuDung);
		buttonBox.add(Box.createHorizontalStrut(30));
		buttonBox.add(btnLuu);
		boxArray[12].add(buttonBox);
		btnThanhToan=new JButton("Thanh Toán");
		btnThanhToan.setBorder(BorderFactory.createLineBorder(Color.decode("#00A651"), 5, true)); /////// Màu cam///
		btnThanhToan.setForeground(Color.WHITE);
		btnThanhToan.setPreferredSize(new Dimension(850, 50));
		btnThanhToan.setMaximumSize(btnThanhToan.getPreferredSize());
		btnThanhToan.setBackground(Color.decode("#00A651"));
		boxArray[13].add(Box.createVerticalStrut(15));
		boxArray[14].add(btnThanhToan);
		
		
		
		// === Panel phải: Thực đơn đồ uống ===
		Box BoxThucDon_Right = Box.createVerticalBox();
		
		BoxThucDon_Right.setBackground(Color.decode("#F7F4EC"));
		BoxThucDon_Right.setOpaque(true);
		BoxThucDon_Right.setBorder(new CompoundBorder(lineBorder, emptyBorder));
		
		Box[] arrayBoxThucDon=new Box[15];
		for (int i = 0; i < arrayBoxThucDon.length; i++) {
			BoxThucDon_Right.add(arrayBoxThucDon[i]=Box.createHorizontalBox());
			BoxThucDon_Right.add(Box.createVerticalStrut(10));
			arrayBoxThucDon[i].setAlignmentX(Component.LEFT_ALIGNMENT);
		}
		
		JLabel lblMenuDoUong=new  JLabel("Menu đồ uống");
		lblMenuDoUong.setFont(lblFont);
		txtTimKiemDoUong = new JTextField(15);
	
		txtTimKiemDoUong.setPreferredSize(new Dimension(175,30));
		txtTimKiemDoUong.setMaximumSize(txtTimKiemDoUong.getPreferredSize());
		
		btnTimKiemDoUong = new JButton("Tìm kiếm");
		btnTimKiemDoUong.setPreferredSize(new Dimension(175,30));
		btnTimKiemDoUong.setMaximumSize(txtTimKiemDoUong.getPreferredSize());
		btnTimKiemDoUong.setBackground(Color.decode("#00A651"));
		btnTimKiemDoUong.setBorder(BorderFactory.createLineBorder(Color.decode("#00A651"), 5, true)); /////// Màu cam///
		btnTimKiemDoUong.setForeground(Color.WHITE);
		
		cboFilterDoUong = new JComboBox<String>();
		cboFilterDoUong.setPreferredSize(new Dimension(175,30));
		cboFilterDoUong.setMaximumSize(txtTimKiemDoUong.getPreferredSize());
		
		
		
		
		JPanel pnlCacMon=new JPanel(new GridLayout(5,4,5,5));
		Box boxCacMon=Box.createHorizontalBox();
		///////////////// Danh sách món ///////////////////////////////////////////////////////////
		boxCacMon.add(pnlCacMon);
		for (int i = 0; i < 10; i++) {
			pnlCacMon.add(new JButton("Món"+i));
			
		}

		arrayBoxThucDon[0].add(lblMenuDoUong);
		arrayBoxThucDon[1].add(txtTimKiemDoUong);
		arrayBoxThucDon[2].add(btnTimKiemDoUong);
		arrayBoxThucDon[3].add(cboFilterDoUong);
		arrayBoxThucDon[5].add(boxCacMon);
		
		// === Panel đặt bàn ===
		JPanel pnlDatBan = new JPanel(new BorderLayout());
		
		Box BoxDanhSachBan_DatBan=Box.createVerticalBox();
		BoxDanhSachBan_DatBan.setBorder(new CompoundBorder(lineBorder, emptyBorder));
		
		Box BoxThongTin_DatBan=Box.createVerticalBox();
		BoxThongTin_DatBan.setBorder(new CompoundBorder(lineBorder, emptyBorder));
		
		Box DanhSachDonDatBan=Box.createVerticalBox();
		DanhSachDonDatBan.setBorder(new CompoundBorder(lineBorder, emptyBorder));
		
		
		
		pnlDatBan.setBackground(Color.decode("#F7F4EC"));
		
		JLabel lblDatBanTitle=new JLabel("Đặt Bàn");
		lblDatBanTitle.setFont(lblFont);
		Box boxTitleDatBan=Box.createHorizontalBox();
		boxTitleDatBan.setAlignmentX(Component.LEFT_ALIGNMENT);
		boxTitleDatBan.add(lblDatBanTitle);
		BoxDanhSachBan_DatBan.add(boxTitleDatBan);
		BoxDanhSachBan_DatBan.add(Box.createVerticalStrut(10));
		
		Box BoxCacBan_DatBan=Box.createHorizontalBox();
		JPanel pnlCacBan_DatBan = new JPanel(new GridLayout(6, 3, 5, 5));
		themBanVaoPanel(pnlCacBan_DatBan);
		BoxCacBan_DatBan.add(pnlCacBan_DatBan);
		BoxCacBan_DatBan.setAlignmentX(Component.LEFT_ALIGNMENT);
		BoxDanhSachBan_DatBan.add(BoxCacBan_DatBan);
		
		
		pnlDatBan.add(BoxDanhSachBan_DatBan,BorderLayout.WEST);
		pnlDatBan.add(BoxThongTin_DatBan,BorderLayout.CENTER);
		pnlDatBan.add(DanhSachDonDatBan,BorderLayout.EAST);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		// Gắn các panel vào mainPanel
		pnlDanhSachBan.add(BoxBan_Left, BorderLayout.WEST);
		pnlDanhSachBan.add(BoxThongTin_Center, BorderLayout.CENTER);
		pnlDanhSachBan.add(BoxThucDon_Right, BorderLayout.EAST);


		// Thêm tab đầu tiên
		this.addTab("Danh Sách Bàn", pnlDanhSachBan);
		this.addTab("Đặt Bàn", pnlDatBan);

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	
	}
	public void themBanVaoPanel(JPanel pnl) {
		ButtonGroup tableGroup = new ButtonGroup();
		for (int i = 1; i <= 10; i++) {
			JRadioButton tableBtn = new JRadioButton("Bàn "+i,resizedIcon);
			tableBtn.setVerticalTextPosition(SwingConstants.TOP);
			tableBtn.setHorizontalTextPosition(SwingConstants.CENTER);
			tableBtn.setHorizontalAlignment(SwingConstants.CENTER);

			// Gán màu trạng thái
//            if (i == 1 || i == 2) {
//                tableBtn.setBackground(Color.RED); // Đang sử dụng
//            } else if (i == 3 || i == 6) {
//                tableBtn.setBackground(Color.GREEN); // Trống
//            } else {
//                tableBtn.setBackground(Color.YELLOW); // Đặt trước
//            }

			tableBtn.setOpaque(true);
			tableGroup.add(tableBtn);
			pnl.add(tableBtn);
		}
		
	}

}
