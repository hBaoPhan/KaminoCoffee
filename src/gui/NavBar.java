package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
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
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import entity.ChucVu;
import entity.TaiKhoan;

@SuppressWarnings("serial")
public class NavBar extends JFrame implements MouseListener, ActionListener {

	private JPanel contentPanel;
	private CardLayout cardLayout;
	private JLabel lblTenTaiKhoan;
	private JLabel lblChucVu;
	private Color textHoverColor = Color.decode("#e07b39"); // màu chữ khi hover
	private Color textDefaultColor = Color.BLACK;
	private Font customFont = new Font("Time New Romans", Font.BOLD, 20);
	private JButton btnDangXuat;
	private BanPanel pnlBan;
	private KhachHangPanel pnlKhachHang;
	private NhanVienPanel pnlNhanVien;
	private Color sidebarColor;
	private HoaDonPanel pnlHoaDon;
	private TrangChuPanel pnlTrangChu;
	private ThongKePanel pnlThongKe;
	private boolean isQuanLy;
	private JLabel[] labels;
	
	
	private Border emptyBorder=BorderFactory.createEmptyBorder(5, 10, 5, 10);
	private Border lineBorder=BorderFactory.createLineBorder(Color.decode("#F7F4EC"),5,true);
	private Border selectedBorder=BorderFactory.createCompoundBorder(lineBorder, emptyBorder);

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
		lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT); 
		lblLogo.setBorder(BorderFactory.createEmptyBorder(20,20,0,20));
		sidebar.add(lblLogo);
		
		
		JLabel lblKAMINOCOFFEE=new JLabel("Kamino Coffee");
		lblKAMINOCOFFEE.setFont(new Font(lblKAMINOCOFFEE.getFont().getFontName(), Font.BOLD, 17));
		
		JLabel lblXinChao =new JLabel("Xin chào,");
		lblTenTaiKhoan = new JLabel(taiKhoan.getNhanVien().getTenNV());
		lblChucVu = new JLabel(taiKhoan.getNhanVien().getChucVu() == ChucVu.QUAN_LY ? "Quản Lý" : "Nhân Viên");
		lblChucVu.setForeground(Color.decode("#00A651"));
		
		lblKAMINOCOFFEE.setBorder(BorderFactory.createEmptyBorder(10, 10, 7, 10));
		lblXinChao.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 10));
		lblTenTaiKhoan.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
		lblChucVu.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		lblKAMINOCOFFEE.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblXinChao.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTenTaiKhoan.setAlignmentX(Component.CENTER_ALIGNMENT);
	    lblChucVu.setAlignmentX(Component.CENTER_ALIGNMENT); 
	    
	    sidebar.add(lblKAMINOCOFFEE);
		sidebar.add(Box.createVerticalStrut(10));
		sidebar.add(lblXinChao);
		sidebar.add(lblTenTaiKhoan);
		sidebar.add(lblChucVu);
		sidebar.add(Box.createVerticalStrut(10));
		sidebar.add(Box.createVerticalStrut(20));

		String[] tabs;
		if (taiKhoan.getNhanVien().getChucVu() == ChucVu.QUAN_LY) {
			tabs = new String[]{ "Trang chủ", "Bàn", "Thực đơn", "Hóa Đơn", "Khách hàng", "Nhân viên", "Thống Kê" };
		} else {
			tabs = new String[]{ "Trang chủ", "Bàn", "Hóa Đơn", "Khách hàng"};
		}

		labels = new JLabel[tabs.length];

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
		
		cardLayout = new CardLayout();
	    
	    contentPanel = new JPanel(cardLayout);
		contentPanel.add(pnlTrangChu = new TrangChuPanel(taiKhoan), "Trang chủ");
		contentPanel.add(pnlBan=new BanPanel(taiKhoan), "Bàn");
		contentPanel.add(pnlHoaDon = new HoaDonPanel(), "Hóa Đơn");
		contentPanel.add(pnlKhachHang=new KhachHangPanel(), "Khách hàng");
		isQuanLy=taiKhoan.getNhanVien().getChucVu()==ChucVu.QUAN_LY;
		if(isQuanLy) {
			contentPanel.add(new ThucDonPanel(), "Thực đơn");//
			
			contentPanel.add(pnlNhanVien=new NhanVienPanel(), "Nhân viên");
			contentPanel.add(pnlThongKe=new ThongKePanel(), "Thống Kê");
		}

		
		
		sidebar.add(Box.createVerticalGlue());
	    // 2. Định nghĩa nút Đăng xuất và đặt style
	    btnDangXuat = new JButton("Đăng xuất");
	    btnDangXuat.setBackground(Color.decode("#DC3545"));
	    btnDangXuat.setForeground(Color.WHITE);
	    btnDangXuat.setAlignmentX(Component.CENTER_ALIGNMENT);
	    btnDangXuat.setBorder(BorderFactory.createLineBorder(Color.red,5,true));
	    
	    // 3. Đặt nút vào Sidebar
	    // Dùng JPanel để kiểm soát padding và màu nền xung quanh nút
	    JPanel pLogout = new JPanel();
	    pLogout.setBackground(sidebarColor);
	    pLogout.add(btnDangXuat);
	    
	    sidebar.add(pLogout);
	    sidebar.add(Box.createVerticalStrut(10)); // Khoảng đệm dưới cùng 10px


	    // --- CardLayout (CENTER) (GIỮ NGUYÊN) ---
	   

	    btnDangXuat.addActionListener(this);
	    add(contentPanel, BorderLayout.CENTER);
	    btnDangXuat.addActionListener(this);
	
	}
	
	private void onCardChanged() {
		pnlBan.loadDataBanPanel();
		pnlKhachHang.taiLaiDanhSach();
		pnlHoaDon.taiLaiDanhSach();
		pnlTrangChu.loadThongKeData();
		pnlTrangChu.loadRecentActivityData();
		if(isQuanLy) {
			pnlNhanVien.taiLaiDanhSach();
			pnlThongKe.loadDuLieuThongKe();
		}
		////////////////////// sửa tên biến phía trên rồi bỏ hàm qua đây
	    
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		JLabel clickedLabel = (JLabel) e.getSource();
		String tabName = clickedLabel.getText();
		
		for (JLabel label : labels) {
	        if (label == clickedLabel) {
	            label.setBorder(selectedBorder);
	            label.setOpaque(true); 
	            label.setBackground(Color.decode("#F7F4EC")); 
	            
	        } else {
	        	 label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        	 label.setOpaque(true); 
	             label.setBackground(Color.WHITE);
	            
	        }
	    }
		
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

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnDangXuat)) {
			int xacNhan = JOptionPane.showConfirmDialog(null, 
				"Bạn có chắc chắn muốn đăng xuất?", 
				"Xác nhận đăng xuất", 
				JOptionPane.YES_NO_OPTION);
			
			if (xacNhan == JOptionPane.YES_OPTION) {
				
				
				EventQueue.invokeLater(() -> {
			        new Login().setVisible(true);
			    });
				this.dispose();
				
			}
		}
		
	}
	public void switchTo(String action) {
	    CardLayout layout = (CardLayout) contentPanel.getLayout();
	    onCardChanged();
	    switch (action) {
	        case "Đặt bàn mới" -> layout.show(contentPanel, "Bàn");
	        case "Quản lý hóa đơn" -> layout.show(contentPanel, "Hóa Đơn");
	        case "Quản lý khách hàng" -> layout.show(contentPanel, "Khách hàng");
	        case "Xem thống kê" -> layout.show(contentPanel, "Thống Kê");
	    }
	}

}