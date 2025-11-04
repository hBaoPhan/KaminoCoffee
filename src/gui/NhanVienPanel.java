package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class NhanVienPanel extends JPanel {

    private JTable tableNhanVien;
    private JTextField txtTimKiem;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JLabel lblTongNV;

    public NhanVienPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // ======== THANH CÔNG CỤ (TRÊN CÙNG) ========
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);

        // --- Ô tìm kiếm ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        txtTimKiem = new JTextField(20);
        btnTimKiem = new JButton("🔍 Tìm");
        searchPanel.add(lblTimKiem);
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTimKiem);
        topPanel.add(searchPanel, BorderLayout.WEST);

        // --- Các nút thao tác ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        btnThem = new JButton("➕ Thêm");
        btnSua = new JButton("✏️ Sửa");
        btnXoa = new JButton("🗑️ Xóa");
        btnLamMoi = new JButton("🔄 Làm mới");

        Font btnFont = new Font("Segoe UI", Font.BOLD, 14);
        JButton[] allButtons = {btnThem, btnSua, btnXoa, btnLamMoi};
        for (JButton b : allButtons) {
            b.setFont(btnFont);
            b.setBackground(new Color(46, 139, 87)); // xanh ngọc dịu
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            buttonPanel.add(b);
        }

        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // ======== BẢNG DANH SÁCH NHÂN VIÊN ========
        String[] columnNames = {
            "Mã NV", "Họ tên", "Giới tính", "Chức vụ", "SĐT", "Ngày vào làm", "Lương"
        };

        Object[][] data = {
            {"NV001", "Nguyễn Văn Minh", "Nam", "Quản lý", "0909123456", "2022-05-10", "15,000,000"},
            {"NV002", "Trần Thị Lan", "Nữ", "Thu ngân", "0987345123", "2023-01-20", "9,000,000"},
            {"NV003", "Lê Văn Bình", "Nam", "Phục vụ", "0912456789", "2023-07-01", "7,500,000"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
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
        lblTongNV = new JLabel("Tổng số nhân viên: 3");
        lblTongNV.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        bottomPanel.add(lblTongNV);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new NavBar().setVisible(true);
        });
    }

}
