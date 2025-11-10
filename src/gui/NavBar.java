package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import entity.ChucVu;
import entity.TaiKhoan;

public class NavBar extends JFrame implements MouseListener, ActionListener {

	private JPanel contentPanel;
	private CardLayout cardLayout;

	private JLabel lblTenTaiKhoan;
	private JLabel lblChucVu;

	private Color defaultColor = new Color(230, 230, 230);
	private Color hoverColor = new Color(200, 200, 255); // màu nền khi hover
	private Color textHoverColor = Color.decode("#e07b39"); // màu chữ khi hover
	private Color textDefaultColor = Color.BLACK;
	private Font customFont = new Font("Time New Romans", Font.BOLD, 20);
	private JButton btnDangXuat;

	public NavBar(TaiKhoan taiKhoan) {
		setTitle("Kamino Coffee");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel sidebar = new JPanel();
		sidebar.setBackground(Color.WHITE);
		sidebar.setOpaque(true);
		sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
		sidebar.setAlignmentX(Component.CENTER_ALIGNMENT);
		sidebar.setPreferredSize(new Dimension(150, getHeight()));
		add(sidebar, BorderLayout.WEST);

		ImageIcon LogoIcon = new ImageIcon("data/images/logo.png");
		Image scaledImage = LogoIcon.getImage().getScaledInstance(146, 146, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(scaledImage);
		JLabel lblLogo = new JLabel(resizedIcon);
		sidebar.add(lblLogo);

		lblTenTaiKhoan = new JLabel(taiKhoan.getNhanVien().getTenNV());
		lblChucVu = new JLabel(taiKhoan.getNhanVien().getChucVu() == ChucVu.QUAN_LY ? "Quản Lý" : "Nhân Viên");
		
		lblTenTaiKhoan.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
		lblChucVu.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		sidebar.add(Box.createVerticalStrut(10));
		sidebar.add(lblTenTaiKhoan);
		sidebar.add(lblChucVu);
		sidebar.add(Box.createVerticalStrut(10));
		btnDangXuat=new JButton("Đăng xuất");
		btnDangXuat.setBackground(Color.RED);
		btnDangXuat.setForeground(Color.WHITE);
		sidebar.add(btnDangXuat);
		sidebar.add(Box.createVerticalStrut(20));

		String[] tabs;
		if (taiKhoan.getNhanVien().getChucVu() == ChucVu.QUAN_LY) {
			tabs = new String[]{ "Trang chủ", "Bàn",  "Hóa Đơn", "Khách hàng","Thực đơn", "Nhân viên", "Thống Kê" };
		} else {
			tabs = new String[]{ "Trang chủ", "Bàn",  "Hóa Đơn", "Khách hàng","Thực đơn" };
		}

		JLabel[] labels = new JLabel[tabs.length];

		for (int i = 0; i < tabs.length; i++) {
			final String tab = tabs[i];
			labels[i] = new JLabel(tab);
			labels[i].setFont(customFont);
			labels[i].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			labels[i].addMouseListener(this);
			labels[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
			sidebar.add(labels[i]);
		}
		cardLayout = new CardLayout();
		
		contentPanel = new JPanel(cardLayout);

		contentPanel.add(new TrangChuPanel(), "Trang chủ");
		contentPanel.add(new BanPanel(), "Bàn");
		contentPanel.add(new ThucDonPanel(), "Thực đơn");
		contentPanel.add(new HoaDonPanel(), "Hóa Đơn");
		contentPanel.add(new KhachHangPanel(), "Khách hàng");
		
		if (taiKhoan.getNhanVien().getChucVu() == ChucVu.QUAN_LY) {
			contentPanel.add(new NhanVienPanel(), "Nhân viên");
			contentPanel.add(new ThongKePanel(), "Thống Kê");
		}

		add(contentPanel, BorderLayout.CENTER);
		btnDangXuat.addActionListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		JLabel clickedLabel = (JLabel) e.getSource();
		String tabName = clickedLabel.getText();
		cardLayout.show(contentPanel, tabName);
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

	@Override
	public void actionPerformed(ActionEvent e) {
Object o = e.getSource();
		
		if (o.equals(btnDangXuat)) {
			int xacNhan = JOptionPane.showConfirmDialog(this, 
				"Bạn có chắc chắn muốn đăng xuất?", 
				"Xác nhận đăng xuất", 
				JOptionPane.YES_NO_OPTION);
			
			if (xacNhan == JOptionPane.YES_OPTION) {
				dispose();
				new Login().setVisible(true);
			}
		}
		
	}
}