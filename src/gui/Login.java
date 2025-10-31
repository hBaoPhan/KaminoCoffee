package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame implements ActionListener {

	private JTextField txtUserName;
	private JPasswordField txtPassword;
	private JButton btnLogin;
	private JButton btnDangKyNhanVienMoi;
	private JLabel lblQuenMatKhau;


	public Login() {
		setBackground(Color.WHITE);
		setTitle("Login");
		setSize(475, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
//		setExtendedState(JFrame.MAXIMIZED_BOTH);

		JPanel pnlMain = new JPanel();
		add(pnlMain, BorderLayout.CENTER);

		ImageIcon LogoIcon = new ImageIcon("images/logo.png");
		Image scaledImage = LogoIcon.getImage().getScaledInstance(205, 205, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(scaledImage);
		JLabel lblLogo = new JLabel(resizedIcon);

		add(lblLogo, BorderLayout.NORTH);

		JLabel lblUserName = new JLabel("Tên đăng nhập: ");
		lblUserName.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblUserName.setMaximumSize(lblUserName.getPreferredSize());

		JLabel lblPassword = new JLabel("Mật khẩu: ");
		lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblPassword.setMaximumSize(lblPassword.getPreferredSize());
		lblPassword.setPreferredSize(lblUserName.getPreferredSize());

		txtUserName = new JTextField(20);
		txtUserName.setBorder(BorderFactory.createLineBorder(Color.white, 5, true));

		txtPassword = new JPasswordField(20);
		txtPassword.setBorder(BorderFactory.createLineBorder(Color.white, 5, true));

		btnLogin = new JButton("Đăng nhập");
		btnLogin.setBorder(BorderFactory.createLineBorder(Color.decode("#e07b39"), 5, true)); /////// Màu cam///
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setPreferredSize(new Dimension(400, 30));
		btnLogin.setMaximumSize(btnLogin.getPreferredSize());
		btnLogin.setBackground(Color.decode("#e07b39"));

		btnDangKyNhanVienMoi = new JButton("Đăng ký nhân viên mới");
		btnDangKyNhanVienMoi.setBorder(BorderFactory.createLineBorder(Color.white, 5, true));
		btnDangKyNhanVienMoi.setPreferredSize(new Dimension(400, 30));
		btnDangKyNhanVienMoi.setMaximumSize(btnDangKyNhanVienMoi.getPreferredSize());
		btnDangKyNhanVienMoi.setBackground(Color.WHITE);

		lblQuenMatKhau = new JLabel("Quên mật khẩu?");

		Box box = Box.createVerticalBox();
		Box box1, box2, box3, box4, box5, box6, box7, box8;
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
		box.add(Box.createVerticalStrut(15));
		box.add(box7 = Box.createHorizontalBox());

		box1.setAlignmentX(Component.LEFT_ALIGNMENT);
		box2.setAlignmentX(Component.LEFT_ALIGNMENT);
		box3.setAlignmentX(Component.LEFT_ALIGNMENT);
		box4.setAlignmentX(Component.LEFT_ALIGNMENT);
		box5.setAlignmentX(Component.LEFT_ALIGNMENT);
		box6.setAlignmentX(Component.LEFT_ALIGNMENT);
		box7.setAlignmentX(Component.LEFT_ALIGNMENT);

		box1.add(lblUserName);
		box2.add(txtUserName);
		box3.add(lblPassword);
		box4.add(txtPassword);
		box5.add(btnLogin);
//		box6.add(btnDangKyNhanVienMoi);
		box7.add(lblQuenMatKhau);
		
		btnLogin.addActionListener(this);

	}

	public static void main(String[] args) {
		new Login().setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == btnLogin) {
			new NavBar().setVisible(true);
			dispose();
		}
		
	}

}
