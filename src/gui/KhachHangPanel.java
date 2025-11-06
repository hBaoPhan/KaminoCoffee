package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import java.awt.*;

public class KhachHangPanel extends JPanel {

    private JTable tableKhachHang;
    private JTextField txtTimKiem;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JLabel lblTongKhach;
	private JTextField txtMaKH;
	private JTextField txtTenKH;
	private JComboBox cmbGT;
	private JTextField txtSDT;
	private JDateChooser ngayDK;
	private JCheckBox chkNu;

    public KhachHangPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // ======== PHẦN TRÊN: THANH CÔNG CỤ ========
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel("THÔNG TIN KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblTitle.setForeground(new Color(52, 73, 94));
        lblTitle.setBorder(new EmptyBorder(15, 10, 15, 10));
        topPanel.add(lblTitle, BorderLayout.NORTH);
        
		 // ===== Panel nhập liệu =====
        JPanel pInput = new JPanel();
        pInput.setLayout(new BoxLayout(pInput, BoxLayout.Y_AXIS));
        pInput.setBorder(BorderFactory.createTitledBorder("THÔNG TIN NHÂN VIÊN"));
        pInput.setBackground(Color.WHITE);
        topPanel.add(pInput);
        pInput.setBorder(new EmptyBorder(15, 30, 15, 30));
        pInput.setBackground(new Color(245, 245, 245));
        topPanel.add(pInput, BorderLayout.CENTER);

        int labelWidth = 120; // chiều rộng chuẩn cho tất cả label
        Dimension labelSize = new Dimension(labelWidth, 25);

        // ===== BOX 1: Mã KH =====
        Box box1 = Box.createHorizontalBox();
        JLabel lblMa = new JLabel("Mã KH:");
        lblMa.setPreferredSize(labelSize);
        box1.add(lblMa);
        box1.add(txtMaKH = new JTextField(20));
        pInput.add(box1);
        pInput.add(Box.createVerticalStrut(10));

        // ===== BOX 2: Tên KH + Giới tính =====
        Box box2 = Box.createHorizontalBox();
        JLabel lblTen = new JLabel("Tên KH:");
        lblTen.setPreferredSize(labelSize);
        box2.add(lblTen);
        box2.add(txtTenKH = new JTextField(20));
        box2.add(Box.createHorizontalStrut(20));
        JLabel lblGT = new JLabel("Giới tính:");
        box2.add(lblGT);
        chkNu = new JCheckBox("Nữ");
        chkNu.setBackground(new Color(245, 245, 245));
        box2.add(Box.createHorizontalStrut(5));
        box2.add(chkNu);
        pInput.add(box2);
        pInput.add(Box.createVerticalStrut(10));

        // ===== BOX 3: Số điện thoại =====
        Box box3 = Box.createHorizontalBox();
        JLabel lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setPreferredSize(labelSize);
        box3.add(lblSDT);
        box3.add(txtSDT = new JTextField(20));
        pInput.add(box3);
        pInput.add(Box.createVerticalStrut(10));

        // ===== BOX 4: Ngày đăng ký =====
        Box box4 = Box.createHorizontalBox();
        JLabel lblNgay = new JLabel("Ngày đăng ký:");
        lblNgay.setPreferredSize(labelSize);
        box4.add(lblNgay);
        ngayDK = new JDateChooser();
        ngayDK.setDateFormatString("yyyy-MM-dd");
        ngayDK.setPreferredSize(new Dimension(150, 25));
        box4.add(ngayDK);
        pInput.add(box4);
       
        // --- Thanh tìm kiếm ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        txtTimKiem = new JTextField(20);
        btnTimKiem = new JButton("🔍 Tìm");
        searchPanel.add(lblTimKiem);
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTimKiem);

        // --- Nút thao tác ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");

        Font btnFont = new Font("Segoe UI", Font.BOLD, 14);
        JButton[] allButtons = {btnThem, btnSua, btnXoa, btnLamMoi};
        for (JButton b : allButtons) {
            b.setFont(btnFont);
            b.setBackground(new Color(70, 130, 180));
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            buttonPanel.add(b);
        }
        buttonPanel.add(searchPanel);

        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // ======== PHẦN GIỮA: BẢNG DANH SÁCH KHÁCH HÀNG ========
        String[] columnNames = {"Mã KH", "Họ tên", "Giới tính", "SĐT", "Ngày đăng ký", "Điểm tích lũy"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        tableKhachHang = new JTable(model);
        tableKhachHang.setFillsViewportHeight(true);
        tableKhachHang.setRowHeight(28);
        tableKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableKhachHang.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(tableKhachHang);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng"));
        add(scrollPane, BorderLayout.CENTER);

        // ======== PHẦN DƯỚI: THÔNG TIN PHỤ ========
     // ======== BOTTOM PANEL (hiển thị tổng số khách hàng) ========
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);

        JLabel lblTongKhachText = new JLabel("Tổng số khách hàng: ");
        lblTongKhachText.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        bottomPanel.add(lblTongKhachText);

        // Label động hiển thị số lượng
        lblTongKhach = new JLabel("0"); // mặc định ban đầu là 0
        lblTongKhach.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTongKhach.setForeground(new Color(41, 128, 185)); // màu xanh dương nhẹ
        bottomPanel.add(lblTongKhach);

        add(bottomPanel, BorderLayout.SOUTH);
//        public void setTongKhach(int soLuong) { 🧩 Thêm phương thức cập nhật động:
//            lblTongKhach.setText(String.valueOf(soLuong));
//        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NavBar app = new NavBar();
            app.setVisible(true);
        });
    }


}

