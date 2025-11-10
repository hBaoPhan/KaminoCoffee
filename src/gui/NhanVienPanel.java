package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import dao.NhanVien_dao;
import entity.ChucVu;
import entity.NhanVien;
import java.awt.*;
import java.awt.event.*;
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
        
        // ================== THIẾT LẬP GIAO DIỆN ==================
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(235, 225, 210)); 

        // ======== PHẦN TRÊN: FORM NHẬP ========
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(new Color(235, 225, 210)); 
        add(topPanel, BorderLayout.NORTH);

        JLabel lblTitle = new JLabel("THÔNG TIN NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblTitle.setForeground(new Color(52, 73, 94)); 
        lblTitle.setBorder(new EmptyBorder(15, 10, 15, 10));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel pInput = new JPanel();
        pInput.setLayout(new BoxLayout(pInput, BoxLayout.Y_AXIS));
        pInput.setBorder(new EmptyBorder(15, 30, 15, 30));
        pInput.setBackground(Color.decode("#F7F4EC")); 
        topPanel.add(pInput, BorderLayout.CENTER);

        Dimension labelSize = new Dimension(120, 25);

        // --- Mã NV ---
        Box box1 = Box.createHorizontalBox();
        JLabel lblMa = new JLabel("Mã nhân viên:");
        lblMa.setPreferredSize(labelSize);
        box1.add(lblMa);
        box1.add(txtMaNV = new JTextField(20));
        txtMaNV.setEditable(false); // 💡 KHÔNG CHO CHỈNH SỬA MÃ NV
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
        chkNu.setBackground(Color.decode("#F7F4EC")); 
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
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        buttonPanel.setBackground(new Color(235, 225, 210)); 

        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");
        btnTimKiem = new JButton("Tìm");

        Font btnFont = new Font("Segoe UI", Font.BOLD, 14);
        Color primaryColor = new Color(52, 152, 219); // xanh dương cho nút Tìm
        Color shadowColor = new Color(150, 150, 150); 

        JButton[] allButtons = {btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem};

        for (JButton b : allButtons) {
            b.setFont(btnFont);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setContentAreaFilled(true); 
            b.setOpaque(true); 

            Border paddingBorder = BorderFactory.createEmptyBorder(8, 20, 8, 20);
            Border bevelBorder = BorderFactory.createSoftBevelBorder(
                javax.swing.border.BevelBorder.RAISED, 
                new Color(173, 216, 230),              
                new Color(0, 51, 102)                  
            );
            b.setBorder(BorderFactory.createCompoundBorder(bevelBorder, paddingBorder)); 
            b.addActionListener(this);
            buttonPanel.add(b);
        }

        // --- đặt màu riêng cho từng nút ---
        btnThem.setBackground(new Color(46, 204, 113));   // xanh lá
        btnSua.setBackground(new Color(243, 156, 18));    // cam
        btnXoa.setBackground(new Color(231, 76, 60));     // đỏ
        btnLamMoi.setBackground(new Color(127, 140, 141)); // xám
        btnTimKiem.setBackground(primaryColor);           // xanh dương

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
        bottomPanel.setBackground(new Color(235, 225, 210)); 
        bottomPanel.add(new JLabel("Tổng số nhân viên: "));
        lblTongNV = new JLabel("0");
        lblTongNV.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bottomPanel.add(lblTongNV);
        add(bottomPanel, BorderLayout.SOUTH);

        // Tải danh sách ban đầu
        taiLaiDanhSach();
        
        // 🚀 Tự động gán mã nhân viên khi khởi tạo
        txtMaNV.setText(generateNextCodeForNhanVien());
    }
    
    // ----------------------------------------------------
    // ========== HÀM SINH MÃ NHÂN VIÊN TỰ ĐỘNG ==========
    // ----------------------------------------------------
    private String generateNextCodeForNhanVien() {
        ArrayList<NhanVien> dsNV = nvDAO.getDsnv();

        if (dsNV.isEmpty()) {
            return "NV001";
        }

        int max = 0;
        for (NhanVien nv : dsNV) {
            String ma = nv.getMaNV();
            if (ma != null && ma.startsWith("NV")) {
                try {
                    // Lấy phần số sau "NV"
                    int so = Integer.parseInt(ma.substring(2));
                    if (so > max) max = so;
                } catch (NumberFormatException ignored) {
                    // Bỏ qua nếu mã không đúng định dạng số
                }
            }
        }

        // Trả về mã tiếp theo với định dạng NVxxx
        return String.format("NV%03d", max + 1);
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
            // Giả định ChucVu.fromString() là an toàn
            ChucVu cv = ChucVu.fromString(cboChucVu.getSelectedItem().toString()); 
            
            // 🔹 Kiểm tra rỗng (Mã NV đã có tự động, chỉ cần kiểm tra Tên và SĐT)
            if (ten.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Vui lòng nhập đầy đủ Họ tên và Số điện thoại!");
                return;
            }

            // 🔹 Ràng buộc tên nhân viên (chỉ chữ và khoảng trắng)
            if (!ten.matches("^[\\p{L}\\s]+$")) {
                JOptionPane.showMessageDialog(this, "⚠️ Tên nhân viên chỉ được chứa chữ cái và khoảng trắng!");
                return;
            }

            // 🔹 Ràng buộc số điện thoại (10 chữ số, đầu số VN)
            if (!sdt.matches("^(0[3|5|7|8|9])[0-9]{8}$")) {
                JOptionPane.showMessageDialog(this, "⚠️ Số điện thoại phải gồm đúng 10 chữ số!");
                return;
            }

            // 🔹 Kiểm tra trùng số điện thoại
            // Giả định nvDAO có hàm timNVTheoSDT
            NhanVien nvTonTai = nvDAO.timNVTheoSDT(sdt); 
            if (nvTonTai != null) {
                JOptionPane.showMessageDialog(this, "⚠️ Số điện thoại này đã tồn tại cho nhân viên khác!");
                return;
            }

            // 🔹 Tạo đối tượng và thêm vào DB
            NhanVien nv = new NhanVien(ma, ten, sdt, gioiTinh, cv);
            if (nvDAO.themNV(nv)) {
                JOptionPane.showMessageDialog(this, "✅ Thêm thành công!");
                taiLaiDanhSach();
                xoaTrang(); 
            } else {
                // Lỗi DB (ví dụ: trùng mã NV, dù đã sinh tự động nhưng vẫn nên giữ)
                JOptionPane.showMessageDialog(this, "❌ Thêm thất bại! Mã nhân viên đã tồn tại.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "⚠️ Đã xảy ra lỗi khi thêm nhân viên!");
        }
    }

    private void suaNhanVien() {
        String ma = txtMaNV.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Chọn nhân viên cần sửa!");
            return;
        }

        try {
            String ten = txtHoTen.getText().trim();
            String sdt = txtSDT.getText().trim();
            boolean gioiTinh = chkNu.isSelected();
            String chucVuStr = cboChucVu.getSelectedItem().toString();

            // 🔹 Kiểm tra rỗng
            if (ten.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Vui lòng nhập đầy đủ Họ tên và Số điện thoại!");
                return;
            }
            
            // 🔹 Ràng buộc tên nhân viên
            if (!ten.matches("^[\\p{L}\\s]+$")) {
                JOptionPane.showMessageDialog(this, "⚠️ Tên nhân viên chỉ được chứa chữ cái và khoảng trắng!");
                return;
            }

            // 🔹 Ràng buộc số điện thoại
            if (!sdt.matches("^(0[3|5|7|8|9])[0-9]{8}$")) {
                JOptionPane.showMessageDialog(this, "⚠️ Số điện thoại phải gồm đúng 10 chữ số!");
                return;
            }

            // 🔹 Kiểm tra trùng số điện thoại (trừ chính nhân viên đang sửa)
            // Giả định nvDAO có hàm timNVTheoSDT
            NhanVien nvTonTai = nvDAO.timNVTheoSDT(sdt); 
            if (nvTonTai != null && !nvTonTai.getMaNV().equals(ma)) {
                JOptionPane.showMessageDialog(this, "⚠️ Số điện thoại này đã tồn tại cho nhân viên khác!");
                return;
            }

            // 🔹 Lấy Chức vụ
            ChucVu cv;
            try {
                cv = ChucVu.fromString(chucVuStr);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "⚠️ Chức vụ không hợp lệ: " + chucVuStr);
                return;
            }

            // 🔹 Cập nhật
            NhanVien nv = new NhanVien(ma, ten, sdt, gioiTinh, cv);
            
            if (nvDAO.suaNV(nv)) {
                JOptionPane.showMessageDialog(this, "✅ Sửa thành công!");
                taiLaiDanhSach();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Sửa thất bại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "⚠️ Đã xảy ra lỗi khi sửa nhân viên!");
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
                xoaTrang();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Xóa thất bại!");
            }
        }
    }

    private void xoaTrang() {
        txtHoTen.setText("");
        chkNu.setSelected(false);
        cboChucVu.setSelectedItem("Nhân Viên");
        txtSDT.setText("");
        txtTimKiem.setText("");
        
        // 🚀 Sinh mã mới sau khi xóa trắng
        txtMaNV.setText(generateNextCodeForNhanVien()); 
        
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