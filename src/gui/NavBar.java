package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
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

	private Color defaultColor = new Color(230, 230, 230);
	private Color hoverColor = new Color(200, 200, 255); // màu nền khi hover
	private Color textHoverColor = Color.BLUE; // màu chữ khi hover
	private Color textDefaultColor = Color.BLACK;
	private Font customFont = new Font("Time New Romans", Font.BOLD, 20);

	public NavBar() {
		setTitle("Main Frame");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel sidebar = new JPanel();
//		sidebar.setBackground(Color.black);
//		sidebar.setOpaque(true);
		sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
		sidebar.setAlignmentX(Component.CENTER_ALIGNMENT);
		sidebar.setBackground(new Color(230, 230, 230));
		sidebar.setPreferredSize(new Dimension(150, getHeight()));
		add(sidebar, BorderLayout.WEST);

		ImageIcon LogoIcon = new ImageIcon("images/logo.png");
		Image scaledImage = LogoIcon.getImage().getScaledInstance(146, 146, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(scaledImage);
		JLabel lblLogo = new JLabel(resizedIcon);
		sidebar.add(lblLogo);

		lblTenTaiKhoan = new JLabel("Tên tài khoản");
		lblChucVu = new JLabel("Chức vụ");
		lblTenTaiKhoan.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
		lblChucVu.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		sidebar.add(Box.createVerticalStrut(10));
		sidebar.add(lblTenTaiKhoan);
		sidebar.add(lblChucVu);

		sidebar.add(Box.createVerticalStrut(20));

		String[] tabs = { "Trang chủ", "Bàn", "Thực đơn", "Hóa Đơn", "Khách hàng", "Nhân viên", "Thống Kê" };
		JLabel[] labels = new JLabel[tabs.length];

		for (int i = 0; i < tabs.length; i++) {
			final String tab = tabs[i];
			labels[i] = new JLabel(tab);
			labels[i].setFont(customFont);
			labels[i].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			labels[i].addMouseListener(this);
			labels[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
			sidebar.add(labels[i]);
//            sidebar.add(Box.createVerticalStrut(10));
		}
		// CardLayout lồng vào panel chính
		cardLayout = new CardLayout();
		contentPanel = new JPanel(cardLayout);

		// Thêm các panel vào CardLayout
		contentPanel.add(new TrangChuPanel(), "Trang chủ");
		contentPanel.add(new BanPanel(), "Bàn");
		contentPanel.add(new ThucDonPanel(), "Thực đơn");
		contentPanel.add(new HoaDonPanel(), "Hóa Đơn");
		contentPanel.add(new KhachHangPanel(), "Khách hàng");
		contentPanel.add(new NhanVienPanel(), "Nhân viên");
		contentPanel.add(new ThongKePanel(), "Thống Kê");

		add(contentPanel, BorderLayout.CENTER);

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
//	        label.setOpaque(true); // cần thiết để màu nền có hiệu lực
//	        label.setBackground(hoverColor);
			label.setForeground(textHoverColor);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		Component c = e.getComponent();
		if (c instanceof JLabel) {
			JLabel label = (JLabel) c;
//	        label.setBackground(defaultColor);
			label.setForeground(textDefaultColor);
		}
	}

}
