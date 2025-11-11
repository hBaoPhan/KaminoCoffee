package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class NavBar extends JFrame implements MouseListener {

	private JPanel contentPanel;
	private CardLayout cardLayout;

	private JLabel lblTenTaiKhoan;
	private JLabel lblChucVu;

	private Color textHoverColor = Color.decode("#e07b39");
	private Color textDefaultColor = Color.BLACK;
	private Font customFont = new Font("Time New Romans", Font.BOLD, 20);
	private JButton btnDangXuat;
	private BanPanel pnlBan;
	private KhachHangPanel pnlKhachHang;
	private NhanVienPanel pnlNhanVien;

	public NavBar() {
	    setTitle("Kamino Coffee");
	    setExtendedState(JFrame.MAXIMIZED_BOTH);
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);

	    Color sidebarColor = new Color(235, 225, 210); 

	    JPanel sidebar = new JPanel();
	    sidebar.setOpaque(true);
	    sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
	    sidebar.setAlignmentX(Component.CENTER_ALIGNMENT);
	    sidebar.setPreferredSize(new Dimension(150, getHeight()));
	    add(sidebar, BorderLayout.WEST);

	    // --- LOGO VÀ THÔNG TIN TÀI KHOẢN---
	    ImageIcon LogoIcon = new ImageIcon("data/images/logo.png");
	    Image scaledImage = LogoIcon.getImage().getScaledInstance(146, 146, Image.SCALE_SMOOTH);
	    ImageIcon resizedIcon = new ImageIcon(scaledImage);
	    JLabel lblLogo = new JLabel(resizedIcon);
	    lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT); 
	    sidebar.add(lblLogo);

	    lblTenTaiKhoan = new JLabel("Tên tài khoản");
	    lblChucVu = new JLabel("Chức vụ");
	    lblTenTaiKhoan.setAlignmentX(Component.CENTER_ALIGNMENT);
	    lblChucVu.setAlignmentX(Component.CENTER_ALIGNMENT); 
	    
	    lblTenTaiKhoan.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
	    lblChucVu.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

	    sidebar.add(Box.createVerticalStrut(10));
	    sidebar.add(lblTenTaiKhoan);
	    sidebar.add(lblChucVu);
	    sidebar.add(Box.createVerticalStrut(10));
	    

	    // --- MENU ITEMS ---
	    String[] tabs = { "Trang chủ", "Bàn","Thực đơn",  "Hóa Đơn", "Khách hàng", "Nhân viên", "Thống Kê" };
	    JLabel[] labels = new JLabel[tabs.length];


	    for (int i = 0; i < tabs.length; i++) {
	        final String tab = tabs[i];
	        labels[i] = new JLabel(tab);
	        labels[i].setFont(customFont);
	        labels[i].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        labels[i].addMouseListener(this);
	        labels[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
	        labels[i].setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa
	        sidebar.add(labels[i]);
	    }
	    
	    sidebar.add(Box.createVerticalGlue());

		cardLayout = new CardLayout();
	    
	    contentPanel = new JPanel(cardLayout);
		contentPanel.add(new TrangChuPanel(), "Trang chủ");
		contentPanel.add(pnlBan=new BanPanel(), "Bàn");
		contentPanel.add(new ThucDonPanel(), "Thực đơn");
		contentPanel.add(new HoaDonPanel(), "Hóa Đơn");
		contentPanel.add(pnlKhachHang=new KhachHangPanel(), "Khách hàng");
		contentPanel.add(pnlNhanVien=new NhanVienPanel(), "Nhân viên");
		contentPanel.add(new ThongKePanel(), "Thống Kê");

	    btnDangXuat = new JButton("Đăng xuất");
	    btnDangXuat.setBackground(Color.RED);
	    btnDangXuat.setForeground(Color.WHITE);
	    btnDangXuat.setAlignmentX(Component.CENTER_ALIGNMENT); 
	    
	    JPanel pLogout = new JPanel();
	    pLogout.add(btnDangXuat);
	    
	    sidebar.add(pLogout);
	    sidebar.add(Box.createVerticalStrut(10));	   
	    
	    add(contentPanel, BorderLayout.CENTER);
	    
	}
	
	private void onCardChanged() {
		pnlBan.loadDataBanPanel();
		pnlKhachHang.taiLaiDanhSach();
		pnlNhanVien.taiLaiDanhSach();
		////////////////////// sửa tên biến phía trên rồi bỏ hàm qua đây
	    
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		JLabel clickedLabel = (JLabel) e.getSource();
		String tabName = clickedLabel.getText();
		cardLayout.show(contentPanel, tabName);
		onCardChanged();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		Component c = e.getComponent();
		if (c instanceof JLabel) {
			JLabel label = (JLabel) c;
			label.setForeground(textHoverColor);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		Component c = e.getComponent();
		if (c instanceof JLabel) {
			JLabel label = (JLabel) c;
			label.setForeground(textDefaultColor);
		}
	}

}
