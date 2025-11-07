package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container; // Import thêm java.awt.Container
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
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

public class ThucDonPanel extends JPanel implements ActionListener, MouseListener{

	private SanPham_dao sanPhanDao;
	
	private Font lblFont = new Font("Time New Roman", Font.BOLD, 20);
	Border lineBorder = BorderFactory.createLineBorder(Color.decode("#e07b39"), 5);
	Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);

	private JTextField txtTimKiemDoUong;
	private JButton btnTimKiemDoUong;
	private JComboBox<String> cboFilterDoUong;
	private JPanel pnlCacMon;

    private JTextField txtMaSP, txtTenSP, txtGiaSP;
    private JComboBox<String> cboLoaiSP;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnChonAnh;
    private JLabel lblAnhSanPham;

	private String currentImagePath;
	private SanPham selectedSanPham; 

	public ThucDonPanel() {
		sanPhanDao = new SanPham_dao();
		
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
		
        Box BoxThucDon_Left = Box.createVerticalBox();
		BoxThucDon_Left.setPreferredSize(new Dimension(950, 800));
		BoxThucDon_Left.setBackground(Color.decode("#F7F4EC"));
		BoxThucDon_Left.setOpaque(true);
		BoxThucDon_Left.setBorder(new CompoundBorder(lineBorder, emptyBorder));
		
		Box box1 = Box.createHorizontalBox();
		box1.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel lblMenuDoUong = new JLabel("Menu Sản Phẩm");
		lblMenuDoUong.setFont(lblFont);
		box1.add(lblMenuDoUong);
		BoxThucDon_Left.add(box1);
		BoxThucDon_Left.add(Box.createVerticalStrut(10));

		Box box2 = Box.createHorizontalBox();
		box2.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtTimKiemDoUong = new JTextField(15);
		txtTimKiemDoUong.setPreferredSize(new Dimension(150, 30));
		txtTimKiemDoUong.setMaximumSize(txtTimKiemDoUong.getPreferredSize());
		
		btnTimKiemDoUong = new JButton("Tìm kiếm");
		btnTimKiemDoUong.setPreferredSize(new Dimension(100, 30));
		btnTimKiemDoUong.setMaximumSize(btnTimKiemDoUong.getPreferredSize());
		btnTimKiemDoUong.setBackground(Color.decode("#00A651"));
		btnTimKiemDoUong.setBorder(BorderFactory.createLineBorder(Color.decode("#00A651"), 3, true));
		btnTimKiemDoUong.setForeground(Color.WHITE);
		
		box2.add(txtTimKiemDoUong);
		box2.add(Box.createHorizontalStrut(5));
		box2.add(btnTimKiemDoUong);

		box2.add(Box.createHorizontalStrut(20));
		JLabel lblLoc = new JLabel("Lọc theo loại:");
		box2.add(lblLoc);
		box2.add(Box.createHorizontalStrut(5));
		
		cboFilterDoUong = new JComboBox<String>();
		cboFilterDoUong.setPreferredSize(new Dimension(255, 30));
		cboFilterDoUong.setMaximumSize(cboFilterDoUong.getPreferredSize());
		box2.add(cboFilterDoUong);
		BoxThucDon_Left.add(box2);

		pnlCacMon = new JPanel(new GridLayout(0, 4, 10, 10)); 
		pnlCacMon.setBackground(Color.decode("#F7F4EC"));
		
		JScrollPane scrollMon = new JScrollPane(pnlCacMon);
		scrollMon.setBorder(null);
		scrollMon.setBackground(Color.decode("#F7F4EC"));
		scrollMon.setPreferredSize(new Dimension(400, 600)); 
		
		BoxThucDon_Left.add(scrollMon);
		
		
		JPanel pnlCenter = new JPanel(new BorderLayout());
		pnlCenter.add(BoxThucDon_Left, BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        
        
        Box BoxThongTin_Right = Box.createVerticalBox();
        BoxThongTin_Right.setBackground(Color.WHITE);
        BoxThongTin_Right.setOpaque(true);
        BoxThongTin_Right.setBorder(new CompoundBorder(lineBorder, emptyBorder));
        
        JLabel lblTitle = new JLabel("Thông Tin Sản Phẩm");
        lblTitle.setFont(lblFont);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        BoxThongTin_Right.add(lblTitle);
        BoxThongTin_Right.add(Box.createVerticalStrut(20));

        Box boxAnh = Box.createVerticalBox();
        boxAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAnhSanPham = new JLabel();
        lblAnhSanPham.setPreferredSize(new Dimension(200, 200));
        lblAnhSanPham.setMaximumSize(new Dimension(200, 200));
        lblAnhSanPham.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblAnhSanPham.setIcon(loadSanPhamIcon("data/images/default", false)); 
        
        btnChonAnh = new JButton("Chọn Ảnh");
        btnChonAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        boxAnh.add(lblAnhSanPham);
        boxAnh.add(Box.createVerticalStrut(10));
        boxAnh.add(btnChonAnh);
        BoxThongTin_Right.add(boxAnh);
        BoxThongTin_Right.add(Box.createVerticalStrut(20));

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBackground(Color.WHITE);
        
        Dimension labelSize = new Dimension(100, 30);
        Dimension fieldSize = new Dimension(300, 30);

        Box boxMa = Box.createHorizontalBox();
        JLabel lblMa = new JLabel("Mã sản phẩm:");
        lblMa.setPreferredSize(labelSize);
        txtMaSP = new JTextField();
        txtMaSP.setPreferredSize(fieldSize);
        txtMaSP.setMaximumSize(fieldSize);
        txtMaSP.setEditable(false);
        txtMaSP.setBackground(Color.LIGHT_GRAY);
        boxMa.add(lblMa);
        boxMa.add(txtMaSP);
        pnlForm.add(boxMa);
        pnlForm.add(Box.createVerticalStrut(10));

        Box boxTen = Box.createHorizontalBox();
        JLabel lblTen = new JLabel("Tên sản phẩm:");
        lblTen.setPreferredSize(labelSize);
        txtTenSP = new JTextField();
        txtTenSP.setPreferredSize(fieldSize);
        txtTenSP.setMaximumSize(fieldSize);
        boxTen.add(lblTen);
        boxTen.add(txtTenSP);
        pnlForm.add(boxTen);
        pnlForm.add(Box.createVerticalStrut(10));

        Box boxGia = Box.createHorizontalBox();
        JLabel lblGia = new JLabel("Đơn giá:");
        lblGia.setPreferredSize(labelSize);
        txtGiaSP = new JTextField();
        txtGiaSP.setPreferredSize(fieldSize);
        txtGiaSP.setMaximumSize(fieldSize);
        boxGia.add(lblGia);
        boxGia.add(txtGiaSP);
        pnlForm.add(boxGia);
        pnlForm.add(Box.createVerticalStrut(10));

        Box boxLoai = Box.createHorizontalBox();
        JLabel lblLoai = new JLabel("Loại sản phẩm:");
        lblLoai.setPreferredSize(labelSize);
        cboLoaiSP = new JComboBox<>();
        cboLoaiSP.setPreferredSize(fieldSize);
        cboLoaiSP.setMaximumSize(fieldSize);
        boxLoai.add(lblLoai);
        boxLoai.add(cboLoaiSP);
        pnlForm.add(boxLoai);
        
        BoxThongTin_Right.add(pnlForm);
        BoxThongTin_Right.add(Box.createVerticalStrut(30));

        JPanel pnlButtons = new JPanel(new GridLayout(2, 2, 10, 10)); // 2x2 grid
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setMaximumSize(new Dimension(400, 100));
        
        btnThem = createCrudButton("Thêm", Color.decode("#00A651"));
        btnSua = createCrudButton("Sửa", Color.decode("#e07b39"));
        btnXoa = createCrudButton("Xóa", Color.decode("#DC3545"));
        btnLamMoi = createCrudButton("Làm Mới", Color.decode("#6C757D"));

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);
        
        BoxThongTin_Right.add(pnlButtons);
        BoxThongTin_Right.add(Box.createVerticalGlue()); 
        
        JPanel pnlLeft = new JPanel(new BorderLayout());
        pnlLeft.add(BoxThongTin_Right, BorderLayout.CENTER);
        add(pnlLeft, BorderLayout.EAST);
        
        cboFilterDoUong.addItem("Tất cả");
        for (LoaiSanPham loai : LoaiSanPham.values()) {
            cboLoaiSP.addItem(loai.getMoTa());
            cboFilterDoUong.addItem(loai.getMoTa());
        }
        
        loadSanPhamVaoPanel(pnlCacMon, sanPhanDao.getAllSanPham());
        
        btnChonAnh.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnSua.addActionListener(this);
        btnThem.addActionListener(this);
        btnTimKiemDoUong.addActionListener(this);
        btnXoa.addActionListener(this);
        cboFilterDoUong.addActionListener(this);
	}
	
	
	
	private JButton createCrudButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 40));
        return btn;
    }
	
	public void loadSanPhamVaoPanel(JPanel panel, ArrayList<SanPham> danhSachSanPham) {
	    panel.removeAll();
	    
	    for (SanPham sp : danhSachSanPham) {
	    	JPanel cardPanel = new JPanel(new BorderLayout(15, 0)); 
	        cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 15));
	        cardPanel.setPreferredSize(new Dimension(150, 110)); 
	        cardPanel.setBackground(Color.WHITE); 
	        
	        String tenSP = sp.getTenSanPham();
	        ImageIcon icon = loadSanPhamIcon("data/images/" + tenSP, true); 
	        
	        JLabel lblImage = new JLabel(icon);
	        lblImage.setPreferredSize(new Dimension(90, 90));
	        lblImage.setOpaque(false);
	        cardPanel.add(lblImage, BorderLayout.WEST);

	        JPanel infoPanel = new JPanel();
	        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
	        infoPanel.setOpaque(false); 

	        JLabel lblTen = new JLabel(tenSP);
	        lblTen.setName("lblTenSP"); 
	        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 16));
	        lblTen.setForeground(new Color(60, 60, 60));

	        double gia = sp.getGia();
	        String giaFormatted = String.format("%,.0f₫", gia);
	        JLabel lblGia = new JLabel(giaFormatted);
	        lblGia.setFont(new Font("Segoe UI", Font.BOLD, 14));
	        lblGia.setForeground(new Color(220, 0, 0));

	        String loai = sp.getLoaiSanPham() != null ? sp.getLoaiSanPham().getMoTa() : "-";
	        JLabel lblLoaiMon = new JLabel("Loại: " + loai);
	        lblLoaiMon.setFont(new Font("Segoe UI", Font.PLAIN, 12));
	        lblLoaiMon.setForeground(new Color(100, 100, 100));

	        boolean isAvailable = true; 
	        JLabel lblTrangThai = new JLabel(isAvailable ? "Có sẵn" : "Hết món");
	        lblTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 12));
	        lblTrangThai.setForeground(isAvailable ? new Color(0, 128, 0) : Color.RED);
	        
	        infoPanel.add(lblTen);
	        infoPanel.add(Box.createVerticalGlue()); 
	        infoPanel.add(lblGia);
	        infoPanel.add(Box.createVerticalStrut(5));
	        infoPanel.add(lblLoaiMon); 
	        infoPanel.add(lblTrangThai);

	        cardPanel.add(infoPanel, BorderLayout.CENTER);
	        
	        panel.add(cardPanel);
	        cardPanel.addMouseListener(this);
	    }

	    panel.revalidate();
	    panel.repaint();
	}
	
	public ImageIcon loadSanPhamIcon(String tenSP, boolean isSmall) {
	    String[] extensions = { ".png", ".jpg", ".jpeg" };
	    File file = null;
	    int size = isSmall ? 85 : 200; 
	    
	    if (currentImagePath != null && !isSmall) {
	        file = new File(currentImagePath);
	        if (file.exists()) {
	            return new ImageIcon(new ImageIcon(file.getAbsolutePath()).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
	        }
	    }

	    for (String ext : extensions) {
	        file = new File(tenSP + ext);
	        if (file.exists()) {
	        	 return new ImageIcon(new ImageIcon(file.getAbsolutePath()).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));	        
	        }
	    }
	    
   	 	file = new File("data/images/default.png");
   	 	return new ImageIcon(new ImageIcon(file.getAbsolutePath()).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));	        
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if (o == btnChonAnh) {
			chonAnh();
		} else if (o == btnLamMoi) {
			lamMoi();
		} else if (o == btnTimKiemDoUong || o == cboFilterDoUong) {
			timKiem();
		} else if (o == btnThem) {
			themSanPham();
		} else if (o == btnSua) {
			suaSanPham();
		} else if (o == btnXoa) {
			xoaSanPham();
		}
	}


	private void suaSanPham() {
		if (selectedSanPham == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
		
		String maSP = txtMaSP.getText().trim(); 
		SanPham spCapNhat = taoSanPhamTuForm(maSP);
		
		if (spCapNhat != null) {
			if (sanPhanDao.suaSanPham(spCapNhat)) {
				JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!");
				loadSanPhamVaoPanel(pnlCacMon, sanPhanDao.getAllSanPham());
				lamMoi();
			} else {
				JOptionPane.showMessageDialog(this, "Lỗi: Không thể cập nhật sản phẩm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void xoaSanPham() {
		if (selectedSanPham == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
		
		int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sản phẩm '" + selectedSanPham.getTenSanPham() + "'?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			if (sanPhanDao.xoaSanPham(selectedSanPham.getMaSanPham())) {
				JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
				loadSanPhamVaoPanel(pnlCacMon, sanPhanDao.getAllSanPham());
				lamMoi();
			} else {
				JOptionPane.showMessageDialog(this, "Lỗi: Không thể xóa sản phẩm. (Có thể do sản phẩm đang có trong hóa đơn).", "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		}
	}



	private void chonAnh() {
		JFileChooser fileChooser = new JFileChooser("data/images/");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Hình ảnh", "jpg", "jpeg", "png");
		fileChooser.setFileFilter(filter);
		
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    currentImagePath = selectedFile.getAbsolutePath();
		    lblAnhSanPham.setIcon(loadSanPhamIcon(currentImagePath, false));
		}
	}

	private void themSanPham() {
        int nextId = sanPhanDao.getAllSanPham().stream()
            .map(SanPham::getMaSanPham)
            .filter(id -> id.matches("SP\\d+"))
            .mapToInt(id -> Integer.parseInt(id.substring(2)))
            .max().orElse(0) + 1;
        String newID = "SP" + String.format("%03d", nextId);

		SanPham spMoi = taoSanPhamTuForm(newID); 
		
		if (spMoi != null) {
			if (sanPhanDao.getSanPhamByTen(spMoi.getTenSanPham()) != null) {
				JOptionPane.showMessageDialog(this, "Lỗi: Tên sản phẩm đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (sanPhanDao.themSanPham(spMoi)) {
				JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
				loadSanPhamVaoPanel(pnlCacMon, sanPhanDao.getAllSanPham());
				lamMoi();
			} else {
				JOptionPane.showMessageDialog(this, "Lỗi: Không thể thêm sản phẩm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	
	private SanPham taoSanPhamTuForm(String maSP) {
        String tenSP = txtTenSP.getText().trim();
        double gia = 0.0;
        
        if (tenSP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            txtTenSP.requestFocus();
            return null;
        }

        try {
            gia = Double.parseDouble(txtGiaSP.getText().trim().replace(",", "").replace("₫", "")); 
            if (gia < 0) {
                JOptionPane.showMessageDialog(this, "Giá phải là số không âm.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                txtGiaSP.requestFocus();
                return null;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá phải là một số hợp lệ (ví dụ: 50000).", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            txtGiaSP.requestFocus();
            return null;
        }

        String loaiStr = Objects.toString(cboLoaiSP.getSelectedItem(), "");
        LoaiSanPham loaiSP;
        try {
            loaiSP = LoaiSanPham.fromString(loaiStr);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Loại sản phẩm không hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return new SanPham(maSP, tenSP, gia, loaiSP);
	}


	
	private void timKiem() {
		String tuKhoa = txtTimKiemDoUong.getText().trim();
        String loai = Objects.toString(cboFilterDoUong.getSelectedItem(), "Tất cả");
        
        ArrayList<SanPham> danhSachLoc = sanPhanDao.getSanPhamByLoc(tuKhoa, loai);
        loadSanPhamVaoPanel(pnlCacMon, danhSachLoc);
	}



	private void lamMoi() {
		txtMaSP.setText("");
		txtTenSP.setText("");
		txtGiaSP.setText("");
		cboLoaiSP.setSelectedIndex(0); 
		lblAnhSanPham.setIcon(loadSanPhamIcon("data/images/default", false));
		currentImagePath = null; 
		selectedSanPham = null;
		
		txtTimKiemDoUong.setText("");
		cboFilterDoUong.setSelectedIndex(0);
		loadSanPhamVaoPanel(pnlCacMon, sanPhanDao.getAllSanPham());
	}



	@Override
	public void mouseClicked(MouseEvent e) {
		Component component = e.getComponent();
	    JPanel clickedPanel = null;
	    
	    while (component != null) {
	        if (component instanceof Container && ((Container) component).getLayout() instanceof BorderLayout) {
	            if (component instanceof JPanel) {
	                clickedPanel = (JPanel) component;
	            }
	            break; 
	        }
	        component = component.getParent(); 
	    }
	    
	    if (clickedPanel == null) {
	        return; 
	    }
	    
	    Component infoComponent = clickedPanel.getComponent(1); 
	    if (!(infoComponent instanceof JPanel)) return;
	    
	    Component[] components = ((JPanel) infoComponent).getComponents();
	    String tenSP = "";
	    for (Component comp : components) {
	        if (comp instanceof JLabel && "lblTenSP".equals(comp.getName())) {
	            tenSP = ((JLabel) comp).getText();
	            break;
	        }
	    }

	    if (!tenSP.isEmpty()) {
	        selectedSanPham = sanPhanDao.getSanPhamByTen(tenSP); 
	        if (selectedSanPham != null) {
	            txtMaSP.setText(selectedSanPham.getMaSanPham());
	            txtTenSP.setText(selectedSanPham.getTenSanPham());
	            txtGiaSP.setText(String.format("%,.0f", selectedSanPham.getGia()));
	            cboLoaiSP.setSelectedItem(selectedSanPham.getLoaiSanPham().getMoTa());
	            
	            currentImagePath = null; 
	            lblAnhSanPham.setIcon(loadSanPhamIcon("data/images/" + selectedSanPham.getTenSanPham(), false)); 
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
	    	JPanel panel = (JPanel) e.getSource();
	    	if(panel.getLayout() instanceof BorderLayout) { 
	    		panel.setBackground(new Color(230, 245, 255));
	    	}
	    }
	}



	@Override
	public void mouseExited(MouseEvent e) {
	    if (e.getSource() instanceof JPanel) {
	    	JPanel panel = (JPanel) e.getSource();
	    	if(panel.getLayout() instanceof BorderLayout) { 
	    		panel.setBackground(Color.WHITE);
	    	}
	    }
	}
}