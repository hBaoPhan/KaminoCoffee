package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList; // Thêm import cho ArrayList

import dao.HoaDon_dao; // Thêm import lớp DAO

public class HoaDonPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable invoiceTable;
    
    private JLabel lblPending, lblPaid, lblTotal, lblRevenue; 
    
    // Controls
    private JTextField searchField;
    private JButton deleteBtn, searchBtn;

    // Chi tiết Hóa đơn/Khách hàng
    private JTextField txtMaHD, txtTenKH_ChiTiet, txtTenBan_ChiTiet, txtSDT_ChiTiet, txtDiemTL_ChiTiet, txtKHDangKy_ChiTiet, txtTongTien_ChiTiet;
    private JPanel pnlChiTietHD;
    
    // KHAI BÁO DAO
    private HoaDon_dao hoaDonDao; // <--- KHAI BÁO

    public HoaDonPanel() {
        // KHỞI TẠO DAO
        hoaDonDao = new HoaDon_dao(); // <--- KHỞI TẠO
        
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

    // --- (Giữ nguyên các hàm tạo giao diện) ---
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
        searchBtn.addActionListener(this::timVaHienThiChiTiet); // <--- XỬ LÝ CHÍNH
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
        String[] columnNames = {"Mã HD", "Tên KH", "Tên bàn", "Số điện thoại", "Điểm TL", "KHDK", "Tổng tiền", "Trạng thái"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
             @Override
             public boolean isCellEditable(int row, int column) {
                 return false;
             }
             // Cột Điểm TL (index 4) là Integer, Tổng tiền (index 6) là Double
             @Override
             public Class<?> getColumnClass(int column) {
                 if (column == 4) {
                     return Integer.class; 
                 }
                 if (column == 6) {
                      // Sử dụng Double để xử lý tiền tệ chính xác hơn
                     return Double.class; 
                 }
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

        CustomTableCellRenderer renderer = new CustomTableCellRenderer();
        // Áp dụng Renderer căn phải cho cột Điểm TL (index 4)
        invoiceTable.getColumnModel().getColumn(4).setCellRenderer(renderer); 
        // Áp dụng Renderer căn phải cho cột Tổng tiền (index 6)
        invoiceTable.getColumnModel().getColumn(6).setCellRenderer(renderer); 

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        for (int i = 0; i < columnNames.length; i++) {
            if (i != 4 && i != 6) {
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
    
   
    private void timVaHienThiChiTiet(ActionEvent e) {
        String tenBan = searchField.getText().trim();
        lamMoiChiTiet(); // Xóa dữ liệu cũ
        
        if (tenBan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Tên Bàn để tìm kiếm.");
            return;
        }

        // 1. GỌI DAO để lấy chi tiết Hóa đơn đang hoạt động theo Tên Bàn
        // Object[] chứa: maHD, tenKH, tenBan, sDT, diemTL, laKHDK(String), tongTien(Double)
        Object[] chiTietHD = hoaDonDao.getChiTietHoaDonDangHoatDongByTenBan(tenBan); 

        if (chiTietHD != null) {
            
            String maHD = (String) chiTietHD[0];
            String tenKH = (String) chiTietHD[1];
            String tenBanKetQua = (String) chiTietHD[2];
            String sdt = (String) chiTietHD[3];
            int diemTL = (Integer) chiTietHD[4];
            boolean laKHDK = chiTietHD[5].equals("Có"); 
            double tongTien = (Double) chiTietHD[6]; 
            
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
        txtDiemTL_ChiTiet.setText("0");
        txtKHDangKy_ChiTiet.setText("");
        txtTongTien_ChiTiet.setText("0đ");
    }
    
    /**
     * Xử lý hiển thị chi tiết khi click vào hàng trong bảng.
     */
    private void hienThiChiTietTuBang() {
        int row = invoiceTable.getSelectedRow();
        if (row >= 0) {
            String maHD = tableModel.getValueAt(row, 0).toString();
            String tenKH = tableModel.getValueAt(row, 1).toString();
            String tenBan = tableModel.getValueAt(row, 2).toString();
            String sdt = tableModel.getValueAt(row, 3).toString();
            int diem = (Integer) tableModel.getValueAt(row, 4);
            boolean khdk = tableModel.getValueAt(row, 5).toString().equals("Có");
            double tongTien = (Double) tableModel.getValueAt(row, 6); 
            
            hienThiChiTiet(maHD, tenKH, tenBan, sdt, diem, khdk, tongTien);
        }
    }

    /**
     * Phương thức được gọi từ bên ngoài (ví dụ: BanPanel) để tải lại dữ liệu.
     */
    public void taiLaiDanhSach() {
        // HÀM LOAD DỮ LIỆU LÊN BẢNG
        tableModel.setRowCount(0);
        
        // GỌI DAO để lấy danh sách Object[] (đã được JOIN)
        ArrayList<Object[]> danhSachHoaDon = hoaDonDao.getAllHoaDonChoPanel(); 
        
        // Khởi tạo các biến tính tổng (cho Summary)
        double tongDoanhThu = 0;
        int pendingCount = 0;
        int paidCount = 0;

        for (Object[] row : danhSachHoaDon) {
            // 1. Xử lý dữ liệu Tổng tiền và Trạng thái (để tính Summary)
            double tongTien = 0.0;
            if (row[6] instanceof Number) {
                 tongTien = ((Number) row[6]).doubleValue();
                 row[6] = tongTien; // Cập nhật lại giá trị đã kiểm tra
            } else {
                 row[6] = 0.0; // Đặt về 0 nếu không phải số
            }
            
            // Cột 7: Trạng thái (String)
            String trangThai = (String) row[7];
            
            // 2. Cập nhật Summary
            if (trangThai.equals("Đã thanh toán")) {
                tongDoanhThu += tongTien;
                paidCount++;
            } else if (trangThai.equals("Chờ thanh toán")) {
                pendingCount++;
            }
            
            // 3. Thêm hàng vào bảng
            tableModel.addRow(row);
        }
        
        // 4. Cập nhật Summary
        lblTotal.setText(String.valueOf(tableModel.getRowCount()));
        lblPending.setText(String.valueOf(pendingCount));
        lblPaid.setText(String.valueOf(paidCount));
        lblRevenue.setText(String.format("%,.0fđ", tongDoanhThu));
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
        
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa hóa đơn " + maHD + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
             // TODO: Thêm logic xóa thực tế và tải lại bảng
             JOptionPane.showMessageDialog(this, "Đã gửi yêu cầu xóa hóa đơn " + maHD + ".");
             lamMoiChiTiet();
             taiLaiDanhSach();
        }
    }
    
    // Phương thức main để test giao diện
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý Hóa đơn");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 850);
            
            HoaDonPanel panel = new HoaDonPanel();
            frame.add(panel);
            
            frame.setVisible(true);
        });
    }
}