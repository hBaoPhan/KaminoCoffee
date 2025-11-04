package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class KhachHangPanel extends JPanel {

    private JTable tableKhachHang;
    private JTextField txtTimKiem;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JLabel lblTongKhach;

    public KhachHangPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // ======== PHẦN TRÊN: THANH CÔNG CỤ ========
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);

        // --- Thanh tìm kiếm ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        txtTimKiem = new JTextField(20);
        btnTimKiem = new JButton("🔍 Tìm");
        searchPanel.add(lblTimKiem);
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTimKiem);
        topPanel.add(searchPanel, BorderLayout.WEST);

        // --- Nút thao tác ---
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
            b.setBackground(new Color(70, 130, 180));
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            buttonPanel.add(b);
        }

        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // ======== PHẦN GIỮA: BẢNG DANH SÁCH KHÁCH HÀNG ========
        String[] columnNames = {"Mã KH", "Họ tên", "Giới tính", "SĐT", "Điểm tích lũy", "Ngày tham gia"};
        Object[][] data = {
            {"KH001", "Nguyễn Văn A", "Nam", "0905123456", 120, "2024-02-20"},
            {"KH002", "Trần Thị B", "Nữ", "0987654321", 80, "2024-03-15"},
            {"KH003", "Lê Minh C", "Nam", "0912345678", 150, "2024-01-10"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        tableKhachHang = new JTable(model);
        tableKhachHang.setFillsViewportHeight(true);
        tableKhachHang.setRowHeight(28);
        tableKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableKhachHang.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(tableKhachHang);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng"));
        add(scrollPane, BorderLayout.CENTER);

        // ======== PHẦN DƯỚI: THÔNG TIN PHỤ ========
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        lblTongKhach = new JLabel("Tổng số khách hàng: 3");
        lblTongKhach.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        bottomPanel.add(lblTongKhach);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NavBar app = new NavBar();
            app.setVisible(true);
        });
    }


}

