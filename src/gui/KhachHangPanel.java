package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.KhachHang_dao;
import entity.KhachHang;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class KhachHangPanel extends JPanel implements ActionListener, MouseListener {
    private JTable tableKhachHang;
    private JTextField txtMaKH, txtTenKH, txtSDT, txtDiem, txtTimKiem;
    private JCheckBox chkLaKHDK;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JLabel lblTongKH;
    private DefaultTableModel model;
    private KhachHang_dao khDAO;

    public KhachHangPanel() {
        khDAO = new KhachHang_dao();

        // ==== GIAO DIỆN CHÍNH ====
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // ==== PHẦN TRÊN (THÔNG TIN) ====
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);
        add(topPanel, BorderLayout.NORTH);

        JLabel lblTitle = new JLabel("THÔNG TIN KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblTitle.setForeground(new Color(52, 73, 94));
        lblTitle.setBorder(new EmptyBorder(15, 10, 15, 10));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel pInput = new JPanel();
        pInput.setLayout(new BoxLayout(pInput, BoxLayout.Y_AXIS));
        pInput.setBorder(new EmptyBorder(15, 30, 15, 30));
        pInput.setBackground(new Color(245, 245, 245));
        topPanel.add(pInput, BorderLayout.CENTER);

        Dimension labelSize = new Dimension(120, 25);

        // --- Mã KH ---
        Box box1 = Box.createHorizontalBox();
        JLabel lblMa = new JLabel("Mã khách hàng:");
        lblMa.setPreferredSize(labelSize);
        box1.add(lblMa);
        box1.add(txtMaKH = new JTextField(20));
        pInput.add(box1);
        pInput.add(Box.createVerticalStrut(10));

        // --- Tên KH ---
        Box box2 = Box.createHorizontalBox();
        JLabel lblTen = new JLabel("Tên khách hàng:");
        lblTen.setPreferredSize(labelSize);
        box2.add(lblTen);
        box2.add(txtTenKH = new JTextField(20));
        pInput.add(box2);
        pInput.add(Box.createVerticalStrut(10));

        // --- SĐT ---
        Box box3 = Box.createHorizontalBox();
        JLabel lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setPreferredSize(labelSize);
        box3.add(lblSDT);
        box3.add(txtSDT = new JTextField(20));
        pInput.add(box3);
        pInput.add(Box.createVerticalStrut(10));

        // --- Điểm tích lũy ---
        Box box4 = Box.createHorizontalBox();
        JLabel lblDiem = new JLabel("Điểm tích lũy:");
        lblDiem.setPreferredSize(labelSize);
        box4.add(lblDiem);
        box4.add(txtDiem = new JTextField("0", 20));
        pInput.add(box4);
        pInput.add(Box.createVerticalStrut(10));

        // --- Khách hàng đăng ký ---
        Box box5 = Box.createHorizontalBox();
        JLabel lblKHDK = new JLabel("Là khách hàng đăng ký:");
        lblKHDK.setPreferredSize(labelSize);
        box5.add(lblKHDK);
        chkLaKHDK = new JCheckBox("Có đăng ký");
        chkLaKHDK.setBackground(new Color(245, 245, 245));
        box5.add(chkLaKHDK);
        pInput.add(box5);

        // ==== THANH CÔNG CỤ ====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);

        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");
        btnTimKiem = new JButton("🔍 Tìm");

        Font btnFont = new Font("Segoe UI", Font.BOLD, 14);
        JButton[] allButtons = {btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem};
        for (JButton b : allButtons) {
            b.setFont(btnFont);
            b.setBackground(new Color(70, 130, 180));
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            b.addActionListener(this);
            buttonPanel.add(b);
        }

        // --- ô tìm kiếm ---
        txtTimKiem = new JTextField(20);
        buttonPanel.add(new JLabel("Tìm theo tên: "));
        buttonPanel.add(txtTimKiem);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ==== BẢNG KHÁCH HÀNG ====
        String[] columnNames = {"Mã KH", "Tên KH", "Số điện thoại", "Điểm TL", "KHDK"};
        model = new DefaultTableModel(columnNames, 0);
        tableKhachHang = new JTable(model);
        tableKhachHang.setRowHeight(28);
        tableKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableKhachHang.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableKhachHang.addMouseListener(this);

        JScrollPane scrollPane = new JScrollPane(tableKhachHang);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng"));
        add(scrollPane, BorderLayout.CENTER);

        // ==== DƯỚI CÙNG ====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(new JLabel("Tổng số khách hàng: "));
        lblTongKH = new JLabel("0");
        lblTongKH.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bottomPanel.add(lblTongKH);
        add(bottomPanel, BorderLayout.SOUTH);

        // Tải dữ liệu ban đầu
        taiLaiDanhSach();
    }

    // ================== ACTION HANDLING ==================
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == btnThem) {
            themKhachHang();
        } else if (o == btnSua) {
            suaKhachHang();
        } else if (o == btnXoa) {
            xoaKhachHang();
        } else if (o == btnLamMoi) {
            lamMoi();
        } else if (o == btnTimKiem) {
            timKhachHang();
        }
    }

    // ================== HÀM CHỨC NĂNG ==================
    private void themKhachHang() {
        try {
            String ma = txtMaKH.getText().trim();
            String ten = txtTenKH.getText().trim();
            String sdt = txtSDT.getText().trim();
            int diem = Integer.parseInt(txtDiem.getText().trim());
            boolean laKHDK = chkLaKHDK.isSelected();

            if (ma.isEmpty() || ten.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            KhachHang kh = new KhachHang(ma, ten, sdt, diem, laKHDK);
            if (khDAO.addKhachHang(kh)) {
                JOptionPane.showMessageDialog(this, "✅ Thêm thành công!");
                taiLaiDanhSach();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Thêm thất bại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "⚠️ Lỗi dữ liệu!");
        }
    }

    private void suaKhachHang() {
        String ma = txtMaKH.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Vui lòng chọn khách hàng cần sửa!");
            return;
        }

        String ten = txtTenKH.getText().trim();
        String sdt = txtSDT.getText().trim();
        int diem = Integer.parseInt(txtDiem.getText().trim());
        boolean laKHDK = chkLaKHDK.isSelected();

        KhachHang kh = new KhachHang(ma, ten, sdt, diem, laKHDK);
        if (khDAO.suaKhachHang(kh)) {
            JOptionPane.showMessageDialog(this, "✅ Sửa thành công!");
            taiLaiDanhSach();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Sửa thất bại!");
        }
    }

    private void xoaKhachHang() {
        String ma = txtMaKH.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Chọn khách hàng cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Xóa khách hàng " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (khDAO.xoaKhachHang(ma)) {
                JOptionPane.showMessageDialog(this, "🗑 Xóa thành công!");
                taiLaiDanhSach();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Xóa thất bại!");
            }
        }
    }

    private void timKhachHang() {
        String keyword = txtTimKiem.getText().trim();
        if (keyword.isEmpty()) {
            taiLaiDanhSach();
            return;
        }

        ArrayList<KhachHang> ds = khDAO.timTheoTen(keyword);
        model.setRowCount(0);
        for (KhachHang kh : ds) {
            model.addRow(new Object[]{
                    kh.getMaKhachHang(), kh.getTenKhachHang(), kh.getsDT(),
                    kh.getDiemTichLuy(), kh.isLaKHDK() ? "Có" : "Không"
            });
        }
        lblTongKH.setText(String.valueOf(ds.size()));
    }

    private void lamMoi() {
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtSDT.setText("");
        txtDiem.setText("0");
        chkLaKHDK.setSelected(false);
        txtTimKiem.setText("");
        taiLaiDanhSach();
    }

    private void taiLaiDanhSach() {
        model.setRowCount(0);
        ArrayList<KhachHang> ds = khDAO.getAllKhachHang();
        for (KhachHang kh : ds) {
            model.addRow(new Object[]{
                    kh.getMaKhachHang(), kh.getTenKhachHang(), kh.getsDT(),
                    kh.getDiemTichLuy(), kh.isLaKHDK() ? "Có" : "Không"
            });
        }
        lblTongKH.setText(String.valueOf(ds.size()));
    }

    // ================== MOUSE EVENT ==================
    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tableKhachHang.getSelectedRow();
        if (row >= 0) {
            txtMaKH.setText(model.getValueAt(row, 0).toString());
            txtTenKH.setText(model.getValueAt(row, 1).toString());
            txtSDT.setText(model.getValueAt(row, 2).toString());
            txtDiem.setText(model.getValueAt(row, 3).toString());
            chkLaKHDK.setSelected("Có".equals(model.getValueAt(row, 4)));
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
