package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects; 

import dao.HoaDon_dao; 

public class HoaDonPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable invoiceTable;
    
    private JLabel lblPending, lblPaid, lblTotal, lblRevenue; 
    
    // Controls
    private JTextField searchField;
    private JButton deleteBtn, searchBtn;

    // Chi tiết Hóa đơn/Khách hàng
    // Ghi chú: txtDiemTL_ChiTiet vẫn được giữ để hiển thị khi tìm kiếm theo Bàn, 
    // dù nó đã bị xóa khỏi bảng danh sách chính.
    private JTextField txtMaHD, txtTenKH_ChiTiet, txtTenBan_ChiTiet, txtSDT_ChiTiet, txtDiemTL_ChiTiet, txtKHDangKy_ChiTiet, txtTongTien_ChiTiet;
    private JPanel pnlChiTietHD;
    
    // KHAI BÁO DAO
    private HoaDon_dao hoaDonDao; 

    public HoaDonPanel() {
        // KHỞI TẠO DAO
        hoaDonDao = new HoaDon_dao(); 
        
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(247, 242, 236));

        // 1. HEADER (Summary + Controls)
        mainPanel.add(createHeaderAndSummaryAndControlsPanel(), BorderLayout.NORTH);
        
        // 2. BẢNG (Danh sách hóa đơn)
        mainPanel.add(createInvoiceTablePanel(), BorderLayout.CENTER); 
        
        // 3. CHI TIẾT HÓA ĐƠN/KHÁCH HÀNG (Ở dưới cùng)
        pnlChiTietHD = createChiTietHoaDonPanel();
        mainPanel.add(pnlChiTietHD, BorderLayout.SOUTH);

        add(mainPanel);
        
        // Tải dữ liệu lần đầu
        taiLaiDanhSach();
    }

    // --- CÁC PHƯƠNG THỨC TẠO GIAO DIỆN ---
    private JPanel createHeaderAndSummaryAndControlsPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.setBackground(new Color(235, 225, 210)); 

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(160, 140, 120));
        JLabel titleLabel = new JLabel("Quản lý hóa đơn");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        Color paleCream = new Color(255, 245, 238);
        titleLabel.setForeground(paleCream);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.EAST); 

        topPanel.add(headerPanel);
        topPanel.add(Box.createVerticalStrut(20));

        // Summary 
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        summaryPanel.setBackground(new Color(249, 224, 220));

        lblPending = createSummaryLabel("0");
        lblPaid = createSummaryLabel("0");
        lblTotal = createSummaryLabel("0");
        lblRevenue = createSummaryLabel("0đ");

        summaryPanel.add(createSummaryCard(lblPending, "Chờ thanh toán", new Color(77, 63, 55)));
        summaryPanel.add(createSummaryCard(lblPaid, "Đã thanh toán", new Color(77, 63, 55)));
        summaryPanel.add(createSummaryCard(lblTotal, "Tổng hóa đơn", new Color(77, 63, 55)));
        summaryPanel.add(createSummaryCard(lblRevenue, "Doanh thu", new Color(77, 63, 55)));

        topPanel.add(summaryPanel);
        topPanel.add(Box.createVerticalStrut(20));

        // Controls (Tìm kiếm và Xóa)
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controlsPanel.setBackground(new Color(235, 225, 210));
        
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 35));
        searchField.setToolTipText("Nhập Tên bàn để tìm kiếm thông tin hóa đơn");
        
        controlsPanel.add(new JLabel("Tìm theo Tên Bàn:")); 
        controlsPanel.add(searchField);
        
        searchBtn = new JButton("Tìm hóa đơn");
        searchBtn.setBackground(new Color(77, 63, 55));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setPreferredSize(new Dimension(150, 35));
        searchBtn.addActionListener(this::timVaHienThiChiTiet); 
        controlsPanel.add(searchBtn);
        
        deleteBtn = new JButton("Xóa hóa đơn");
        deleteBtn.setBackground(new Color(220, 50, 50)); 
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setPreferredSize(new Dimension(150, 35));
        deleteBtn.addActionListener(this::xoaHoaDon); 
        controlsPanel.add(deleteBtn);
        
        topPanel.add(controlsPanel);

        return topPanel;
    }
    
    
    private JPanel createChiTietHoaDonPanel() {
        JPanel pInput = new JPanel();
        pInput.setLayout(new BoxLayout(pInput, BoxLayout.Y_AXIS));
        pInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Thông tin chi tiết Hóa đơn/Khách hàng"),
                BorderFactory.createEmptyBorder(10, 30, 10, 30)));
        pInput.setBackground(Color.decode("#F7F4EC"));

        Dimension labelSize = new Dimension(150, 25);
        int verticalStrut = 5; 

        // Hàng 1: Mã HD và Tên KH
        Box box1 = Box.createHorizontalBox();
        box1.add(createLabeledField("Mã Hóa đơn:", txtMaHD = new JTextField(15), labelSize, false));
        box1.add(Box.createHorizontalStrut(20));
        box1.add(createLabeledField("Tên khách hàng:", txtTenKH_ChiTiet = new JTextField(15), labelSize, false));
        pInput.add(box1);
        pInput.add(Box.createVerticalStrut(verticalStrut));

        // Hàng 2: Tên Bàn và SĐT
        Box box2 = Box.createHorizontalBox();
        box2.add(createLabeledField("Tên Bàn:", txtTenBan_ChiTiet = new JTextField(15), labelSize, false));
        box2.add(Box.createHorizontalStrut(20));
        box2.add(createLabeledField("Số điện thoại:", txtSDT_ChiTiet = new JTextField(15), labelSize, false));
        pInput.add(box2);
        pInput.add(Box.createVerticalStrut(verticalStrut));

        // Hàng 3: Điểm TL và KHD K
        Box box3 = Box.createHorizontalBox();
        // Giữ lại Diem TL ở đây, vì nó được lấy khi tìm kiếm theo bàn
        box3.add(createLabeledField("Điểm tích lũy:", txtDiemTL_ChiTiet = new JTextField("0", 15), labelSize, false)); 
        box3.add(Box.createHorizontalStrut(20));
        box3.add(createLabeledField("Khách hàng ĐK:", txtKHDangKy_ChiTiet = new JTextField(15), labelSize, false));
        pInput.add(box3);
        pInput.add(Box.createVerticalStrut(verticalStrut));
        
        // Hàng 4: Tổng tiền
        Box box4 = Box.createHorizontalBox();
        box4.add(createLabeledField("Tổng tiền HD:", txtTongTien_ChiTiet = new JTextField("0đ", 15), labelSize, false));
        pInput.add(box4);

        return pInput;
    }
    
    private Box createLabeledField(String labelText, JTextField textField, Dimension labelSize, boolean isEditable) {
        Box box = Box.createHorizontalBox();
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(labelSize);
        label.setMaximumSize(labelSize);
        
        textField.setEditable(isEditable);
        textField.setBackground(new Color(240, 240, 240)); 
        
        box.add(label);
        box.add(textField);
        return box;
    }


    private JLabel createSummaryLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        return label;
    }

    private JPanel createSummaryCard(JLabel valueLabel, String title, Color borderColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, borderColor),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        card.add(valueLabel);
        card.add(titleLabel);
        return card;
    }

    private JScrollPane createInvoiceTablePanel() {
        // ✅ ĐÃ SỬA CẤU TRÚC CỘT: Xóa "Điểm TL", thêm "Ngày"
        String[] columnNames = {"Mã HD", "Tên KH", "Tên bàn", "Số điện thoại", "Ngày", "KHDK", "Tổng tiền", "Trạng thái"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
             @Override
             public boolean isCellEditable(int row, int column) {
                 return false;
             }
             @Override
             public Class<?> getColumnClass(int column) {
                 // Cột 6: Tổng tiền (Double)
                 if (column == 6) { 
                      return Double.class; 
                 }
                 // Các cột còn lại (bao gồm Ngày) là String
                 return String.class;
             }
        };
        invoiceTable = new JTable(tableModel);
        invoiceTable.setFont(new Font("Arial", Font.PLAIN, 14));
        invoiceTable.setRowHeight(30);
        invoiceTable.getTableHeader().setReorderingAllowed(false);
        invoiceTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        invoiceTable.getTableHeader().setBackground(new Color(190, 175, 160));
        invoiceTable.getTableHeader().setForeground(Color.BLACK);
        
        invoiceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hienThiChiTietTuBang();
            }
        });

        // Setup Renderer
        CustomTableCellRenderer renderer = new CustomTableCellRenderer();
        // Căn phải cho cột Tổng tiền (Index 6)
        invoiceTable.getColumnModel().getColumn(6).setCellRenderer(renderer); 

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        for (int i = 0; i < columnNames.length; i++) {
            // Căn trái cho tất cả trừ cột Tổng tiền (Index 6)
            if (i != 6) { 
                invoiceTable.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(invoiceTable); 
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 
        scrollPane.setBackground(new Color(247, 242, 236));
        return scrollPane;
    }
    
    // Custom Renderer
    class CustomTableCellRenderer extends DefaultTableCellRenderer {
        public CustomTableCellRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                                                      boolean isSelected, boolean hasFocus, 
                                                      int row, int column) {
            
            JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? new Color(255, 255, 255) : new Color(240, 240, 240));
            } else {
                c.setBackground(table.getSelectionBackground());
            }

            return c;
        }
    }
    
    // ================== CÁC PHƯƠNG THỨC XỬ LÝ DỮ LIỆU (TÍCH HỢP DAO) ==================
    
    /**
     * Xử lý tìm kiếm hóa đơn đang hoạt động theo Tên Bàn và hiển thị chi tiết.
     * DAO trả về 7 cột: maHD(0), tenKH(1), tenBan(2), sDT(3), diemTL(4), laKHDK(5), tongTien(6)
     */
    private void timVaHienThiChiTiet(ActionEvent e) {
        String tenBan = searchField.getText().trim();
        lamMoiChiTiet(); 
        
        if (tenBan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Tên Bàn để tìm kiếm.");
            return;
        }

        // GỌI DAO
        Object[] chiTietHD = hoaDonDao.getChiTietHoaDonDangHoatDongByTenBan(tenBan); 

        if (chiTietHD != null) {
            
            // Lấy và ép kiểu dữ liệu từ Object[] (7 phần tử)
            String maHD = Objects.toString(chiTietHD[0], "");
            String tenKH = Objects.toString(chiTietHD[1], "");
            String tenBanKetQua = Objects.toString(chiTietHD[2], "");
            String sdt = Objects.toString(chiTietHD[3], "N/A");
            
            // Lấy Điểm TL (Index 4)
            int diemTL = (chiTietHD[4] instanceof Number) ? ((Number) chiTietHD[4]).intValue() : 0;
            
            String laKHDK_Str = Objects.toString(chiTietHD[5], "Không"); 
            boolean laKHDK = "Có".equals(laKHDK_Str);
            
            // Lấy Tổng tiền (Index 6)
            double tongTien = (chiTietHD[6] instanceof Number) ? ((Number) chiTietHD[6]).doubleValue() : 0.0;
            
            hienThiChiTiet(maHD, tenKH, tenBanKetQua, sdt, diemTL, laKHDK, tongTien);
            JOptionPane.showMessageDialog(this, "Đã tìm thấy hóa đơn đang hoạt động cho bàn " + tenBanKetQua + ".");
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn đang hoạt động cho bàn: " + tenBan + ".");
        }
    }
    
    /**
     * Phương thức dùng để hiển thị chi tiết Hóa đơn lên khu vực dưới cùng.
     */
    public void hienThiChiTiet(String maHD, String tenKH, String tenBan, String sdt, int diemTL, boolean laKHDK, double tongTien) {
        txtMaHD.setText(maHD);
        txtTenKH_ChiTiet.setText(tenKH);
        txtTenBan_ChiTiet.setText(tenBan);
        txtSDT_ChiTiet.setText(sdt);
        txtDiemTL_ChiTiet.setText(String.valueOf(diemTL));
        txtKHDangKy_ChiTiet.setText(laKHDK ? "Có" : "Không");
        txtTongTien_ChiTiet.setText(String.format("%,.0fđ", tongTien));
    }
    
    /**
     * Xóa dữ liệu trên khu vực chi tiết.
     */
    private void lamMoiChiTiet() {
        txtMaHD.setText("");
        txtTenKH_ChiTiet.setText("");
        txtTenBan_ChiTiet.setText("");
        txtSDT_ChiTiet.setText("");
        txtDiemTL_ChiTiet.setText("0"); // Giữ lại giá trị mặc định cho Diem TL
        txtKHDangKy_ChiTiet.setText("");
        txtTongTien_ChiTiet.setText("0đ");
    }
    
    /**
     * Xử lý hiển thị chi tiết khi click vào hàng trong bảng.
     * Cấu trúc bảng hiện tại: [maHD(0), tenKH(1), tenBan(2), sDT(3), Ngay(4), KHDK(5), TongTien(6), TrangThai(7)]
     */
    private void hienThiChiTietTuBang() {
        int row = invoiceTable.getSelectedRow();
        if (row >= 0) {
            
            String maHD = Objects.toString(tableModel.getValueAt(row, 0), "");
            String tenKH = Objects.toString(tableModel.getValueAt(row, 1), "");
            String tenBan = Objects.toString(tableModel.getValueAt(row, 2), "");
            String sdt = Objects.toString(tableModel.getValueAt(row, 3), "N/A");
            
            // ⚠️ Diem TL đã bị xóa khỏi bảng chính, nên không thể lấy từ bảng. Đặt là 0.
            int diem = 0; 
            
            // Lấy KHDK ở Index 5
            String khdkStr = Objects.toString(tableModel.getValueAt(row, 5), "Không");
            boolean khdk = "Có".equals(khdkStr);
            
            // Lấy Tổng tiền ở Index 6
            Object tongTienObj = tableModel.getValueAt(row, 6);
            double tongTien = (tongTienObj instanceof Number) ? ((Number) tongTienObj).doubleValue() : 0.0;
            
            if (!maHD.isEmpty()) {
                // Vẫn truyền diem = 0 vào hàm hiển thị chi tiết
                hienThiChiTiet(maHD, tenKH, tenBan, sdt, diem, khdk, tongTien); 
            } else {
                lamMoiChiTiet();
            }
        }
    }

    /**
     * Phương thức được gọi từ bên ngoài để tải lại dữ liệu và cập nhật Summary.
     * DAO trả về 8 cột: [maHD, tenKH, tenBan, sDT, Ngay, laKHDK, TongTien, TrangThai]
     */
    public void taiLaiDanhSach() {
        tableModel.setRowCount(0);
        
        // GỌI DAO
        ArrayList<Object[]> danhSachHoaDon = hoaDonDao.getAllHoaDonChoPanel(); 
        
        double tongDoanhThu = 0;
        int pendingCount = 0;
        int paidCount = 0;

        for (Object[] row : danhSachHoaDon) {
            // 1. Xử lý dữ liệu Tổng tiền và Trạng thái 
            // Cột 6 (Index 6): Tổng tiền
            double tongTien = 0.0;
            if (row[6] instanceof Number) {
                 tongTien = ((Number) row[6]).doubleValue();
            } 
            
            // Cột 7 (Index 7): Trạng thái (String)
            String trangThai = (String) row[7];
            
            // 2. Cập nhật Summary
            if (trangThai.equals("Đã thanh toán")) {
                tongDoanhThu += tongTien;
                paidCount++;
            } else if (trangThai.equals("Chờ thanh toán")) {
                pendingCount++;
            }
            
            // 3. Thêm hàng vào bảng (Cấu trúc mới khớp với DAO)
            tableModel.addRow(row);
        }
        
        // 4. Cập nhật Summary
        lblTotal.setText(String.valueOf(tableModel.getRowCount()));
        lblPending.setText(String.valueOf(pendingCount));
        lblPaid.setText(String.valueOf(paidCount));
        lblRevenue.setText(String.format("%,.0fđ", tongDoanhThu));
        
        lamMoiChiTiet(); 
    }
    
    /**
     * Xử lý xóa hóa đơn.
     */
    private void xoaHoaDon(ActionEvent e) {
        String maHD = txtMaHD.getText().trim();
        if (maHD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng tìm kiếm hoặc chọn hóa đơn cần xóa.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa hóa đơn " + maHD + "?\n(Thao tác này sẽ xóa vĩnh viễn dữ liệu liên quan)", 
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
             // GỌI DAO để xóa
             if (hoaDonDao.xoaHoaDon(maHD)) {
                 JOptionPane.showMessageDialog(this, "✅ Xóa hóa đơn " + maHD + " thành công!");
                 lamMoiChiTiet();
                 taiLaiDanhSach(); // Tải lại bảng sau khi xóa
             } else {
                 JOptionPane.showMessageDialog(this, "❌ Xóa hóa đơn thất bại! Vui lòng kiểm tra lại.", "Lỗi xóa", JOptionPane.ERROR_MESSAGE);
             }
        }
    }
}