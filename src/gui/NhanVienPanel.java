package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import java.awt.*;

public class NhanVienPanel extends JPanel {

	private JTable tableNhanVien;
    private JTextField txtMaNV, txtHoTen, txtSDT, txtTimKiem;
    private JComboBox<String> cboChucVu;
    private JCheckBox chkNu;
    private JDateChooser ngayVaoLam;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JLabel lblTongNV;
    public NhanVienPanel() {
    	 // ==== THIẾT LẬP CHUNG ====
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // ======== PHẦN TRÊN: NHẬP LIỆU + CÔNG CỤ ========
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);
        add(topPanel, BorderLayout.NORTH);

        // --- Tiêu đề ---
        JLabel lblTitle = new JLabel("THÔNG TIN NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblTitle.setForeground(new Color(52, 73, 94));
        lblTitle.setBorder(new EmptyBorder(15, 10, 15, 10));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        // --- Panel nhập liệu ---
        JPanel pInput = new JPanel();
        pInput.setLayout(new BoxLayout(pInput, BoxLayout.Y_AXIS));
        pInput.setBorder(new EmptyBorder(15, 30, 15, 30));
        pInput.setBackground(new Color(245, 245, 245));
        topPanel.add(pInput, BorderLayout.CENTER);

        int labelWidth = 120;
        Dimension labelSize = new Dimension(labelWidth, 25);

        // ===== BOX 1: Mã nhân viên =====
        Box box1 = Box.createHorizontalBox();
        JLabel lblMa = new JLabel("Mã nhân viên:");
        lblMa.setPreferredSize(labelSize);
        box1.add(lblMa);
        box1.add(txtMaNV = new JTextField(20));
        pInput.add(box1);
        pInput.add(Box.createVerticalStrut(10));

        // ===== BOX 2: Tên + Giới tính =====
        Box box2 = Box.createHorizontalBox();
        JLabel lblTen = new JLabel("Họ tên:");
        lblTen.setPreferredSize(labelSize);
        box2.add(lblTen);
        box2.add(txtHoTen = new JTextField(20));
        box2.add(Box.createHorizontalStrut(20));
        JLabel lblGT = new JLabel("Giới tính:");
        box2.add(lblGT);
        chkNu = new JCheckBox("Nữ");
        chkNu.setBackground(new Color(245, 245, 245));
        box2.add(Box.createHorizontalStrut(5));
        box2.add(chkNu);
        pInput.add(box2);
        pInput.add(Box.createVerticalStrut(10));

        // ===== BOX 3: Chức vụ =====
        Box box3 = Box.createHorizontalBox();
        JLabel lblChucVu = new JLabel("Chức vụ:");
        lblChucVu.setPreferredSize(labelSize);
        box3.add(lblChucVu);
        cboChucVu = new JComboBox<>(new String[]{"Nhân viên", "Quản lý"});
        cboChucVu.setPreferredSize(new Dimension(150, 25));
        box3.add(cboChucVu);
        pInput.add(box3);
        pInput.add(Box.createVerticalStrut(10));

        // ===== BOX 4: SĐT + Ngày vào làm =====
        Box box4 = Box.createHorizontalBox();
        JLabel lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setPreferredSize(labelSize);
        box4.add(lblSDT);
        box4.add(txtSDT = new JTextField(20));
        box4.add(Box.createHorizontalStrut(20));
        JLabel lblNgay = new JLabel("Ngày vào làm:");
        box4.add(lblNgay);
        ngayVaoLam = new JDateChooser();
        ngayVaoLam.setDateFormatString("yyyy-MM-dd");
        ngayVaoLam.setPreferredSize(new Dimension(150, 25));
        box4.add(ngayVaoLam);
        pInput.add(box4);

        // ======== THANH CÔNG CỤ DƯỚI FORM ========
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);

        // --- Nút thao tác ---
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

        // --- Thanh tìm kiếm ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        txtTimKiem = new JTextField(20);
        btnTimKiem = new JButton("🔍 Tìm");
        searchPanel.add(lblTimKiem);
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTimKiem);

        buttonPanel.add(searchPanel);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ======== BẢNG DANH SÁCH NHÂN VIÊN ========
        String[] columnNames = {
            "Mã NV", "Họ tên", "Giới tính", "Chức vụ", "SĐT", "Ngày vào làm"
        };

       

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        tableNhanVien = new JTable(model);
        tableNhanVien.setFillsViewportHeight(true);
        tableNhanVien.setRowHeight(28);
        tableNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableNhanVien.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(tableNhanVien);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách nhân viên"));
        add(scrollPane, BorderLayout.CENTER);

     // ======== DÒNG DƯỚI CÙNG: THÔNG TIN PHỤ ========
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);

        // Label tĩnh
        JLabel lblTongNVText = new JLabel("Tổng số nhân viên: ");
        lblTongNVText.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        bottomPanel.add(lblTongNVText);

        // Label động hiển thị số lượng
        lblTongNV = new JLabel("0"); // mặc định ban đầu là 0
        lblTongNV.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTongNV.setForeground(new Color(41, 128, 185)); // xanh dương nhẹ
        bottomPanel.add(lblTongNV);

        add(bottomPanel, BorderLayout.SOUTH);

//        🧩 Thêm phương thức cập nhật tổng nhân viên:
//
//        	public void setTongNhanVien(int soLuong) {
//        	    lblTongNV.setText(String.valueOf(soLuong));
//        	}
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new NavBar().setVisible(true);
        });
    }

}
