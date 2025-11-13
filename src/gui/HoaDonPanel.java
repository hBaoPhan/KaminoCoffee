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
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// ✅ THÊM IMPORT ĐỂ ĐỊNH DẠNG TIỀN
import java.text.DecimalFormat; 

import dao.HoaDon_dao;

public class HoaDonPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable invoiceTable;
    
    private JLabel lblPending, lblPaid, lblTotal, lblRevenue; 
    
    private DatePicker datePicker; 
    private JButton deleteBtn, searchBtn, refreshBtn;

    private JTextField txtMaHD, txtTenKH_ChiTiet, txtTenBan_ChiTiet, txtSDT_ChiTiet, txtDiemTL_ChiTiet, txtKHDangKy_ChiTiet, txtTongTien_ChiTiet;
    private JPanel pnlChiTietHD;
    
    private HoaDon_dao hoaDonDao; 
    
    private final Color primaryLight = Color.decode("#F7F4EC");
    private final Color primaryAccent = Color.decode("#e07b39");
    private final Color textOnAccent = Color.WHITE;
    private final Color textOnLight = Color.BLACK;

    // ✅ THÊM BIẾN ĐỊNH DẠNG TIỀN TỆ
    private final DecimalFormat vndFormatter = new DecimalFormat("#,##0 VND");

    public HoaDonPanel() {
        hoaDonDao = new HoaDon_dao(); 
        
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(primaryLight); 

        mainPanel.add(createHeaderAndSummaryAndControlsPanel(), BorderLayout.NORTH);
        mainPanel.add(createInvoiceTablePanel(), BorderLayout.CENTER); 
        pnlChiTietHD = createChiTietHoaDonPanel();
        mainPanel.add(pnlChiTietHD, BorderLayout.SOUTH);

        add(mainPanel);
        
        taiLaiDanhSach();
    }

    // ... (createHeaderAndSummaryAndControlsPanel không đổi) ...
    private JPanel createHeaderAndSummaryAndControlsPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.setBackground(primaryLight); 

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryAccent); 
        JLabel titleLabel = new JLabel("Quản lý hóa đơn");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(textOnAccent); 
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        topPanel.add(headerPanel);
        topPanel.add(Box.createVerticalStrut(20));

        // Summary 
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        summaryPanel.setBackground(primaryLight); 

        lblPending = createSummaryLabel("0");
        lblPaid = createSummaryLabel("0");
        lblTotal = createSummaryLabel("0");
        lblRevenue = createSummaryLabel("0đ");

        summaryPanel.add(createSummaryCard(lblPending, "Chờ thanh toán", primaryAccent));
        summaryPanel.add(createSummaryCard(lblPaid, "Đã thanh toán", primaryAccent));
        summaryPanel.add(createSummaryCard(lblTotal, "Tổng hóa đơn", primaryAccent));
        summaryPanel.add(createSummaryCard(lblRevenue, "Doanh thu", primaryAccent));

        topPanel.add(summaryPanel);
        topPanel.add(Box.createVerticalStrut(20));

        // Controls (Tìm kiếm và Xóa)
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controlsPanel.setBackground(primaryLight);
        
        controlsPanel.add(new JLabel("Tìm theo Ngày:")); 
        
        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setFormatForDatesCommonEra("dd-MM-yyyy"); 
        dateSettings.setAllowKeyboardEditing(false);

        datePicker = new DatePicker(dateSettings);
        datePicker.setPreferredSize(new Dimension(200, 35));
        datePicker.setToolTipText("Chọn ngày cần tìm kiếm");
        controlsPanel.add(datePicker);
        
        searchBtn = new JButton("Tìm theo Ngày");
        searchBtn.setBackground(primaryAccent);
        searchBtn.setForeground(textOnAccent);
        searchBtn.setFocusPainted(false);
        searchBtn.setPreferredSize(new Dimension(150, 35));
        searchBtn.addActionListener(this::timKiemTheoNgay); 
        controlsPanel.add(searchBtn);
        
        refreshBtn = new JButton("Làm mới");
        refreshBtn.setBackground(new Color(60, 179, 113)); 
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setPreferredSize(new Dimension(120, 35));
        refreshBtn.addActionListener(e -> taiLaiDanhSach()); 
        controlsPanel.add(refreshBtn);
        
        deleteBtn = new JButton("Xóa hóa đơn");
        deleteBtn.setBackground(new Color(220, 50, 50)); 
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setPreferredSize(new Dimension(150, 35));
        deleteBtn.addActionListener(this::xoaHoaDon); 
        
        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        deletePanel.setBackground(primaryLight);
        deletePanel.add(deleteBtn);
        
        JPanel bottomControls = new JPanel(new BorderLayout());
        bottomControls.setBackground(primaryLight);
        bottomControls.add(controlsPanel, BorderLayout.WEST);
        bottomControls.add(deletePanel, BorderLayout.EAST);
        
        topPanel.add(bottomControls);

        return topPanel;
    }

    // ... (createChiTietHoaDonPanel không đổi) ...
    private JPanel createChiTietHoaDonPanel() {
        JPanel pInput = new JPanel();
        pInput.setLayout(new BoxLayout(pInput, BoxLayout.Y_AXIS));
        pInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Thông tin chi tiết Hóa đơn/Khách hàng"),
                BorderFactory.createEmptyBorder(10, 30, 10, 30)));
        pInput.setBackground(primaryLight); 

        Dimension labelSize = new Dimension(150, 25);
        int verticalStrut = 5; 

        // Hàng 1
        Box box1 = Box.createHorizontalBox();
        box1.add(createLabeledField("Mã Hóa đơn:", txtMaHD = new JTextField(15), labelSize, false));
        box1.add(Box.createHorizontalStrut(20));
        box1.add(createLabeledField("Tên khách hàng:", txtTenKH_ChiTiet = new JTextField(15), labelSize, false));
        pInput.add(box1);
        pInput.add(Box.createVerticalStrut(verticalStrut));

        // Hàng 2
        Box box2 = Box.createHorizontalBox();
        box2.add(createLabeledField("Tên Bàn:", txtTenBan_ChiTiet = new JTextField(15), labelSize, false));
        box2.add(Box.createHorizontalStrut(20));
        box2.add(createLabeledField("Số điện thoại:", txtSDT_ChiTiet = new JTextField(15), labelSize, false));
        pInput.add(box2);
        pInput.add(Box.createVerticalStrut(verticalStrut));

        // Hàng 3
        Box box3 = Box.createHorizontalBox();
        box3.add(createLabeledField("Điểm tích lũy:", txtDiemTL_ChiTiet = new JTextField("0", 15), labelSize, false)); 
        box3.add(Box.createHorizontalStrut(20));
        box3.add(createLabeledField("Khách hàng ĐK:", txtKHDangKy_ChiTiet = new JTextField(15), labelSize, false));
        pInput.add(box3);
        pInput.add(Box.createVerticalStrut(verticalStrut));
        
        // Hàng 4
        Box box4 = Box.createHorizontalBox();
        box4.add(createLabeledField("Tổng tiền HD:", txtTongTien_ChiTiet = new JTextField("0đ", 15), labelSize, false));
        pInput.add(box4);

        return pInput;
    }
    
    // ... (createLabeledField, createSummaryLabel, createSummaryCard không đổi) ...
    private Box createLabeledField(String labelText, JTextField textField, Dimension labelSize, boolean isEditable) {
        Box box = Box.createHorizontalBox();
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(labelSize);
        label.setMaximumSize(labelSize);
        label.setForeground(textOnLight);
        
        textField.setEditable(isEditable);
        textField.setBackground(Color.WHITE); 
        textField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        box.add(label);
        box.add(textField);
        return box;
    }

    private JLabel createSummaryLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(textOnLight);
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
        titleLabel.setForeground(textOnLight);

        card.add(valueLabel);
        card.add(titleLabel);
        return card;
    }


    /**
     * ✅ SỬA ĐỔI: Đơn giản hóa việc cài đặt Renderer
     */
    private JScrollPane createInvoiceTablePanel() {
        String[] columnNames = {"Mã HD", "Tên KH", "Tên bàn", "Số điện thoại", "Ngày", "KHDK", "Tổng tiền", "Trạng thái"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
             @Override
             public boolean isCellEditable(int row, int column) {
                 return false;
             }
             @Override
             public Class<?> getColumnClass(int column) {
                 if (column == 6) { 
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
        invoiceTable.getTableHeader().setBackground(primaryAccent); 
        invoiceTable.getTableHeader().setForeground(textOnAccent); 
        
        invoiceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hienThiChiTietTuBang();
            }
        });

        // ✅ SỬA ĐỔI: Đặt CustomTableCellRenderer làm trình kết xuất mặc định
        // Nó sẽ xử lý cả định dạng tiền, căn lề và tô màu hàng
        CustomTableCellRenderer renderer = new CustomTableCellRenderer();
        invoiceTable.setDefaultRenderer(Object.class, renderer);
        invoiceTable.setDefaultRenderer(Double.class, renderer);
        
        // ❌ BỎ CODE CŨ: Toàn bộ khối code bên dưới không còn cần thiết
        /*
        renderer.setHorizontalAlignment(SwingConstants.RIGHT);
        invoiceTable.getColumnModel().getColumn(6).setCellRenderer(renderer); 

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        for (int i = 0; i < columnNames.length; i++) {
            if (i != 6) { 
                invoiceTable.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
            }
        }
        */
        
        JScrollPane scrollPane = new JScrollPane(invoiceTable); 
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 
        scrollPane.setBackground(primaryLight); 
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }
    
    /**
     * ✅ SỬA ĐỔI: Cập nhật Renderer để xử lý định dạng tiền, căn lề VÀ màu hàng
     */
    class CustomTableCellRenderer extends DefaultTableCellRenderer {
        
        public CustomTableCellRenderer() {
            super(); // Gọi hàm khởi tạo của lớp cha
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                                                       boolean isSelected, boolean hasFocus, 
                                                       int row, int column) {
            
            // 1. Xử lý giá trị và căn lề
            if (column == 6 && value instanceof Number) {
                // Nếu là cột Tổng tiền (index 6), định dạng nó
                value = vndFormatter.format(value);
                setHorizontalAlignment(SwingConstants.RIGHT);
            } else {
                // Các cột khác, căn lề trái
                setHorizontalAlignment(SwingConstants.LEFT);
            }

            // 2. Gọi hàm super với giá trị (đã có thể bị thay đổi) và căn lề
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // 3. Xử lý màu nền (row striping)
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : primaryLight);
                c.setForeground(textOnLight);
            } else {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            }

            return c;
        }
    }
    
    // ================== CÁC PHƯƠNG THỨC XỬ LÝ DỮ LIỆU (TÍCH HỢP DAO) ==================
    
    // ... (timKiemTheoNgay không đổi) ...
    private void timKiemTheoNgay(ActionEvent e) {
        LocalDate selectedDate = datePicker.getDate();
        lamMoiChiTiet(); 
        
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một ngày để tìm kiếm.");
            return;
        }

        ArrayList<Object[]> danhSachLoc = hoaDonDao.getHoaDonByNgay(selectedDate); 

        if (danhSachLoc != null && !danhSachLoc.isEmpty()) {
            hienThiDanhSachLoc(danhSachLoc);
           
        } else {
            tableModel.setRowCount(0);
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn nào cho ngày: " 
                + selectedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".");
        }
    }
    
    // ... (hienThiChiTiet không đổi) ...
    public void hienThiChiTiet(String maHD, String tenKH, String tenBan, String sdt, int diemTL, boolean laKHDK, double tongTien) {
        txtMaHD.setText(maHD);
        txtTenKH_ChiTiet.setText(tenKH);
        txtTenBan_ChiTiet.setText(tenBan);
        txtSDT_ChiTiet.setText(sdt);
        txtDiemTL_ChiTiet.setText(String.valueOf(diemTL));
        txtKHDangKy_ChiTiet.setText(laKHDK ? "Có" : "Không");
        txtTongTien_ChiTiet.setText(String.format("%,.0fđ", tongTien));
    }
    
    // ... (lamMoiChiTiet không đổi) ...
    private void lamMoiChiTiet() {
        txtMaHD.setText("");
        txtTenKH_ChiTiet.setText("");
        txtTenBan_ChiTiet.setText("");
        txtSDT_ChiTiet.setText("");
        txtDiemTL_ChiTiet.setText("0"); 
        txtKHDangKy_ChiTiet.setText("");
        txtTongTien_ChiTiet.setText("0đ");
        
        datePicker.clear(); 
        
        invoiceTable.clearSelection();
    }
    
    // ... (hienThiChiTietTuBang không đổi) ...
    private void hienThiChiTietTuBang() {
        int row = invoiceTable.getSelectedRow();
        if (row >= 0) {
            
            String maHD = Objects.toString(tableModel.getValueAt(row, 0), "");
            String tenKH = Objects.toString(tableModel.getValueAt(row, 1), "");
            String tenBan = Objects.toString(tableModel.getValueAt(row, 2), "");
            String sdt = Objects.toString(tableModel.getValueAt(row, 3), "N/A");
            int diem = 0; 
            String khdkStr = Objects.toString(tableModel.getValueAt(row, 5), "Không");
            boolean khdk = "Có".equals(khdkStr);
            Object tongTienObj = tableModel.getValueAt(row, 6);
            double tongTien = (tongTienObj instanceof Number) ? ((Number) tongTienObj).doubleValue() : 0.0;
            
            if (!maHD.isEmpty()) {
                hienThiChiTiet(maHD, tenKH, tenBan, sdt, diem, khdk, tongTien); 
            } else {
                lamMoiChiTiet();
            }
        }
    }
    
    // ... (hienThiDanhSachLoc không đổi) ...
    private void hienThiDanhSachLoc(ArrayList<Object[]> danhSach) {
        tableModel.setRowCount(0);
        if (danhSach == null) return;
        
        for (Object[] row : danhSach) {
            tableModel.addRow(row);
        }
        lamMoiChiTiet();
    }

    // ... (taiLaiDanhSach không đổi, nó đã xử lý String "Đã thanh toán"...) ...
    public void taiLaiDanhSach() {
        ArrayList<Object[]> danhSachHoaDon = hoaDonDao.getAllHoaDonChoPanel(); 
        
        double tongDoanhThu = 0;
        int pendingCount = 0;
        int paidCount = 0;

        if (danhSachHoaDon != null) {
            for (Object[] row : danhSachHoaDon) {
                double tongTien = 0.0;
                if (row[6] instanceof Number) {
                    tongTien = ((Number) row[6]).doubleValue();
                } 
                
                // Hàm này đã mong đợi chuỗi, nên sửa SQL là đúng
                String trangThai = (String) row[7]; 
                
                if (trangThai.equals("Đã thanh toán")) {
                    tongDoanhThu += tongTien;
                    paidCount++;
                } else if (trangThai.equals("Chờ thanh toán")) {
                    pendingCount++;
                }
            }
            
            lblTotal.setText(String.valueOf(danhSachHoaDon.size()));
            lblPending.setText(String.valueOf(pendingCount));
            lblPaid.setText(String.valueOf(paidCount));
            lblRevenue.setText(String.format("%,.0fđ", tongDoanhThu));
        } else {
            lblTotal.setText("0");
            lblPending.setText("0");
            lblPaid.setText("0");
            lblRevenue.setText("0đ");
        }
        
        hienThiDanhSachLoc(danhSachHoaDon); 
    }
    
    // ... (xoaHoaDon không đổi) ...
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
             if (hoaDonDao.xoaHoaDon(maHD)) {
                 JOptionPane.showMessageDialog(this, "✅ Xóa hóa đơn " + maHD + " thành công!");
                 lamMoiChiTiet();
                 taiLaiDanhSach();
             } else {
                 JOptionPane.showMessageDialog(this, "❌ Xóa hóa đơn thất bại! Vui lòng kiểm tra lại.", "Lỗi xóa", JOptionPane.ERROR_MESSAGE);
             }
        }
    }
}