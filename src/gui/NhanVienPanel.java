package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import connectDB.ConnectDB;
import dao.NhanVien_dao;
import entity.ChucVu;
import entity.NhanVien;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.ArrayList;

public class NhanVienPanel extends JPanel implements ActionListener, MouseListener {

    private JTable tableNhanVien;
    private JTextField txtMaNV, txtHoTen, txtSDT, txtTimKiem;
    private JComboBox<String> cboChucVu;
    private JCheckBox chkNu;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JLabel lblTongNV;
    private NhanVien_dao nvDAO;
    private DefaultTableModel model;

    public NhanVienPanel() {
       
    	nvDAO = new NhanVien_dao();
        // ==== THIẾT LẬP GIAO DIỆN ====
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // ======== PHẦN TRÊN: FORM NHẬP ========
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);
        add(topPanel, BorderLayout.NORTH);

        JLabel lblTitle = new JLabel("THÔNG TIN NHÂN VIÊN", SwingConstants.CENTER);
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

        // --- Mã NV ---
        Box box1 = Box.createHorizontalBox();
        JLabel lblMa = new JLabel("Mã nhân viên:");
        lblMa.setPreferredSize(labelSize);
        box1.add(lblMa);
        box1.add(txtMaNV = new JTextField(20));
        pInput.add(box1);
        pInput.add(Box.createVerticalStrut(10));

        // --- Họ tên + giới tính ---
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

        // --- Chức vụ ---
        Box box3 = Box.createHorizontalBox();
        JLabel lblChucVu = new JLabel("Chức vụ:");
        lblChucVu.setPreferredSize(labelSize);
        box3.add(lblChucVu);
        cboChucVu = new JComboBox<>(new String[]{"Nhân Viên", "Quản Lý"});
        cboChucVu.setPreferredSize(new Dimension(150, 25));
        box3.add(cboChucVu);
        pInput.add(box3);
        pInput.add(Box.createVerticalStrut(10));

        // --- SĐT ---
        Box box4 = Box.createHorizontalBox();
        JLabel lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setPreferredSize(labelSize);
        box4.add(lblSDT);
        box4.add(txtSDT = new JTextField(20));
        pInput.add(box4);

        // ======== THANH CÔNG CỤ ========
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

        // ======== BẢNG ========
        String[] columnNames = {"Mã NV", "Họ tên", "Giới tính", "Chức vụ", "SĐT"};
        model = new DefaultTableModel(columnNames, 0);
        tableNhanVien = new JTable(model);
        tableNhanVien.setRowHeight(28);
        tableNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableNhanVien.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableNhanVien.addMouseListener(this);

        JScrollPane scrollPane = new JScrollPane(tableNhanVien);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách nhân viên"));
        add(scrollPane, BorderLayout.CENTER);

        // ======== DƯỚI CÙNG ========
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(new JLabel("Tổng số nhân viên: "));
        lblTongNV = new JLabel("0");
        lblTongNV.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bottomPanel.add(lblTongNV);
        add(bottomPanel, BorderLayout.SOUTH);

        // Tải danh sách ban đầu
        taiLaiDanhSach();
    }

    // ================== ACTION XỬ LÝ ==================
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == btnThem) {
            themNhanVien();
        } else if (o == btnSua) {
            suaNhanVien();
        } else if (o == btnXoa) {
            xoaNhanVien();
        } else if (o == btnLamMoi) {
            xoaTrang();
        } else if (o == btnTimKiem) {
            timNhanVien();
        }
    }

    // ================== HÀM CHỨC NĂNG ==================
    private void themNhanVien() {
        try {
            String ma = txtMaNV.getText().trim();
            String ten = txtHoTen.getText().trim();
            String sdt = txtSDT.getText().trim();
            boolean gioiTinh = chkNu.isSelected();
            ChucVu cv = ChucVu.fromString(cboChucVu.getSelectedItem().toString());


            if (ma.isEmpty() || ten.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Mã và tên không được trống!");
                return;
            }

            NhanVien nv = new NhanVien(ma, ten, sdt, gioiTinh, cv);
            if (nvDAO.themNV(nv)) {
                JOptionPane.showMessageDialog(this, "✅ Thêm thành công!");
                taiLaiDanhSach();
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void suaNhanVien() {
        String ma = txtMaNV.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Chọn nhân viên cần sửa!");
            return;
        }

        // --- 1. Lấy và kiểm tra dữ liệu ---
        String ten = txtHoTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        boolean gioiTinh = chkNu.isSelected();
        
        // Lấy giá trị chuỗi từ ComboBox (ví dụ: "Nhân Viên" hoặc "Quản Lý")
        String chucVuStr = cboChucVu.getSelectedItem().toString(); 
        
        ChucVu cv;
        try {
            // --- 2. SỬ DỤNG fromString() để chuyển đổi chuỗi có dấu thành Enum ---
            cv = ChucVu.fromString(chucVuStr); 
        } catch (IllegalArgumentException e) {
            // Xử lý nếu giá trị từ ComboBox không khớp với bất kỳ Enum nào
            JOptionPane.showMessageDialog(this, "⚠️ Chức vụ không hợp lệ: " + chucVuStr);
            return;
        }

        // --- 3. Tạo và cập nhật đối tượng Nhân Viên ---
        NhanVien nv = new NhanVien(ma, ten, sdt, gioiTinh, cv);
        
        if (nvDAO.suaNV(nv)) {
            JOptionPane.showMessageDialog(this, "✅ Sửa thành công!");
            // Giả định hàm này tải lại dữ liệu bảng
            taiLaiDanhSach(); 
        } else {
            JOptionPane.showMessageDialog(this, "❌ Sửa thất bại!");
        }
    }

    private void xoaNhanVien() {
        String ma = txtMaNV.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Chọn nhân viên cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Xóa nhân viên " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (nvDAO.xoaNV(ma)) {
                JOptionPane.showMessageDialog(this, "🗑 Xóa thành công!");
                taiLaiDanhSach();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Xóa thất bại!");
            }
        }
    }

    private void xoaTrang() {
        txtMaNV.setText("");
        txtHoTen.setText("");
        chkNu.setSelected(false);
        cboChucVu.setSelectedItem("Nhân Viên");
        txtSDT.setText("");
        txtTimKiem.setText("");
        taiLaiDanhSach();
    }

    private void timNhanVien() {
        String keyword = txtTimKiem.getText().trim();
        if (keyword.isEmpty()) {
            taiLaiDanhSach();
            return;
        }

        ArrayList<NhanVien> ds = nvDAO.timNVTheoTen(keyword);
        model.setRowCount(0);
        for (NhanVien nv : ds) {
            model.addRow(new Object[]{
                    nv.getMaNV(), nv.getTenNV(), nv.isGioiTinh() ? "Nữ" : "Nam",
                    nv.getChucVu().getTenHienThi(), nv.getsDT()
            });
        }
        lblTongNV.setText(String.valueOf(ds.size()));
    }

    private void taiLaiDanhSach() {
        model.setRowCount(0);
        ArrayList<NhanVien> ds = nvDAO.getDsnv();
        for (NhanVien nv : ds) {
            model.addRow(new Object[]{
                    nv.getMaNV(), nv.getTenNV(), nv.isGioiTinh() ? "Nữ" : "Nam",
                    nv.getChucVu().getTenHienThi(), nv.getsDT()
            });
        }
        lblTongNV.setText(String.valueOf(ds.size()));
    }

    // ================== MOUSE EVENT ==================
    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tableNhanVien.getSelectedRow();
        if (row >= 0) {
            txtMaNV.setText(model.getValueAt(row, 0).toString());
            txtHoTen.setText(model.getValueAt(row, 1).toString());
            chkNu.setSelected("Nữ".equals(model.getValueAt(row, 2)));
            cboChucVu.setSelectedItem(model.getValueAt(row, 3).toString());
            txtSDT.setText(model.getValueAt(row, 4).toString());
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

   
}
