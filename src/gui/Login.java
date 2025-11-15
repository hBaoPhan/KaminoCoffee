package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane; 
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import connectDB.ConnectDB;
import dao.TaiKhoan_dao;
import entity.TaiKhoan;

@SuppressWarnings("serial")
public class Login extends JFrame implements ActionListener {

	private JTextField txtUserName;
	private JPasswordField txtPassword;
	private JButton btnLogin;
	
	private TaiKhoan_dao taiKhoanDao;

	public Login() {
		
		taiKhoanDao = new TaiKhoan_dao();
		
		try {
			ConnectDB.getInstance().connect();
			System.out.println("Connected");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, 
                "Không thể kết nối đến SQL Server.\n"
                + "Hãy đảm bảo SQL Server đang chạy và thông tin file ConnectDB.java là chính xác.\n"
                + "Lỗi: " + e.getMessage(), 
                "Lỗi Kết Nối CSDL", 
                JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(1); 
		}
		
		setBackground(Color.WHITE);
		setTitle("Login");
		setSize(475, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel pnlMain = new JPanel();
		pnlMain.setBackground(Color.WHITE);
		pnlMain.setOpaque(true);
		add(pnlMain, BorderLayout.CENTER);

		ImageIcon LogoIcon = new ImageIcon("data/images/logo.png");
		Image scaledImage = LogoIcon.getImage().getScaledInstance(205, 205, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(scaledImage);
		JLabel lblLogo = new JLabel(resizedIcon);
		lblLogo.setBackground(Color.WHITE);
		lblLogo.setOpaque(true);

		add(lblLogo, BorderLayout.NORTH);

		JLabel lblUserName = new JLabel("Tên đăng nhập: ");
		lblUserName.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblUserName.setMaximumSize(lblUserName.getPreferredSize());

		JLabel lblPassword = new JLabel("Mật khẩu: ");
		lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblPassword.setMaximumSize(lblPassword.getPreferredSize());
		lblPassword.setPreferredSize(lblUserName.getPreferredSize());

		txtUserName = new JTextField(20);
		txtUserName.setBorder(BorderFactory.createLineBorder(Color.decode("#F7F4EC"), 5, true));
		txtUserName.setBackground(Color.decode("#F7F4EC"));

		txtPassword = new JPasswordField(20);
		txtPassword.setBorder(BorderFactory.createLineBorder(Color.decode("#F7F4EC"), 5, true));
		txtPassword.setBackground(Color.decode("#F7F4EC"));

		btnLogin = new JButton("Đăng nhập");
		btnLogin.setBorder(BorderFactory.createLineBorder(Color.decode("#e07b39"), 5, true));
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setPreferredSize(new Dimension(400, 30));
		btnLogin.setMaximumSize(btnLogin.getPreferredSize());
		btnLogin.setBackground(Color.decode("#e07b39"));


		Box box = Box.createVerticalBox();
		Box box1, box2, box3, box4, box5, box6;
		pnlMain.add(box, BorderLayout.CENTER);
		box.add(Box.createVerticalStrut(15));
		box.add(box1 = Box.createHorizontalBox());
		box.add(Box.createVerticalStrut(5));
		box.add(box2 = Box.createHorizontalBox());
		box.add(Box.createVerticalStrut(15));
		box.add(box3 = Box.createHorizontalBox());
		box.add(Box.createVerticalStrut(5));
		box.add(box4 = Box.createHorizontalBox());
		box.add(Box.createVerticalStrut(15));
		box.add(box5 = Box.createHorizontalBox());
		box.add(Box.createVerticalStrut(15));
		box.add(box6 = Box.createHorizontalBox());

		box1.setAlignmentX(Component.LEFT_ALIGNMENT);
		box2.setAlignmentX(Component.LEFT_ALIGNMENT);
		box3.setAlignmentX(Component.LEFT_ALIGNMENT);
		box4.setAlignmentX(Component.LEFT_ALIGNMENT);
		box5.setAlignmentX(Component.LEFT_ALIGNMENT);
		box6.setAlignmentX(Component.LEFT_ALIGNMENT);

		box1.add(lblUserName);
		box2.add(txtUserName);
		box3.add(lblPassword);
		box4.add(txtPassword);
		box5.add(btnLogin);
		
		btnLogin.addActionListener(this);
		txtUserName.addActionListener(this);
		txtPassword.addActionListener(this);
	}

	public static void main(String[] args) {
        new Login().setVisible(true);
    }
		
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == btnLogin|| o == txtUserName || o == txtPassword) {
			xuLyDangNhap();
		}
	}

	private void xuLyDangNhap() {
		String tenDangNhap = txtUserName.getText().trim();
		String matKhau = new String(txtPassword.getPassword());
		
		if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		TaiKhoan taiKhoan = taiKhoanDao.timTaiKhoan(tenDangNhap, matKhau);
		
		if (taiKhoan != null) {
			
			new NavBar(taiKhoan).setVisible(true);
			dispose();
			
		} else {
			JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!", "Lỗi Đăng Nhập", JOptionPane.ERROR_MESSAGE);
			txtPassword.requestFocus();
		}
	}
}