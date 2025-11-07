package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HoaDonPanel extends JPanel {

    private JPanel invoiceListPanel;
    private List<click1> allInvoices = new ArrayList<>(); // Danh sách tất cả hóa đơn
    private int nextInvoiceId = 1; // Biến quản lý ID tuần tự

    private JLabel lblPending, lblPaid, lblTotal, lblRevenue;
    private JComboBox<String> statusDropdown;

    public HoaDonPanel() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(247, 242, 236));

        mainPanel.add(createHeaderAndSummaryPanel(), BorderLayout.NORTH);
        mainPanel.add(createInvoiceListPanel(), BorderLayout.CENTER);

        add(mainPanel);
        updateInvoiceList(0); // Hiển thị mặc định (Tất cả trạng thái = Chờ thanh toán)
    }

    private JPanel createHeaderAndSummaryPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.setBackground(new Color(160, 140, 120));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(160, 140, 120));
        JLabel titleLabel = new JLabel("Quản lý hóa đơn");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        Color paleCream = new Color(255, 245, 238);
        titleLabel.setForeground(paleCream);

        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton createInvoiceBtn = new JButton("+ Tạo hóa đơn mới");
        createInvoiceBtn.setBackground(new Color(77, 63, 55));
        createInvoiceBtn.setForeground(Color.WHITE);
        createInvoiceBtn.setFocusPainted(false);
        createInvoiceBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        createInvoiceBtn.addActionListener(this::showCreateInvoiceDialog);
        headerPanel.add(createInvoiceBtn, BorderLayout.EAST);

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

        // Controls
        JPanel controlsPanel = new JPanel(new BorderLayout(25, 0));
        controlsPanel.setBackground(new Color(160, 140, 120));
        JTextField searchField = new JTextField("🔍 Tìm kiếm hóa đơn...");
        searchField.setPreferredSize(new Dimension(800, 35));
        searchField.setForeground(Color.GRAY);
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("🔍 Tìm kiếm hóa đơn...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("🔍 Tìm kiếm hóa đơn...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        String[] statuses = {"Tất cả trạng thái", "Chờ thanh toán", "Đã thanh toán", "Đã hủy"};
        statusDropdown = new JComboBox<>(statuses);
        statusDropdown.setPreferredSize(new Dimension(200, 35));
        
        // Thêm Listener để lọc hóa đơn khi trạng thái thay đổi
        statusDropdown.addActionListener(e -> updateInvoiceList(statusDropdown.getSelectedIndex()));
        
        controlsPanel.add(searchField, BorderLayout.CENTER);
        controlsPanel.add(statusDropdown, BorderLayout.EAST);

        topPanel.add(controlsPanel);

        return topPanel;
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

    private JScrollPane createInvoiceListPanel() {
        invoiceListPanel = new JPanel();
        invoiceListPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        invoiceListPanel.setBackground(new Color(160, 140, 120));

        JScrollPane scrollPane = new JScrollPane(invoiceListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBackground(new Color(249, 247, 244));
        return scrollPane;
    }

    // Phương thức tạo ID tuần tự
    private String getNextInvoiceId() {
        return String.format("HD%02d", nextInvoiceId++);
    }
    
    // Tạo hóa đơn mới (CẬP NHẬT để nhận danh sách món ăn)
    public void addInvoice(String table, String customer, int amount, List<MenuItem> items) {
        String id = getNextInvoiceId();
        // TRUYỀN DANH SÁCH MÓN VÀO click1
        click1 invoice = new click1(this, id, table, customer, "Chờ thanh toán", amount, items);
        allInvoices.add(invoice);
        updateSummary();
        updateInvoiceList(statusDropdown.getSelectedIndex());
    }
    
    // Thanh toán hóa đơn
    public void markAsPaid(click1 invoice) {
        invoice.setAsPaid();
        updateSummary();
        updateInvoiceList(statusDropdown.getSelectedIndex());
    }
    
    // Hủy hóa đơn
    public void markAsCancelled(click1 invoice) {
        invoice.setAsCancelled();
        updateSummary();
        updateInvoiceList(statusDropdown.getSelectedIndex());
    }

    // Cập nhật tổng hợp
    private void updateSummary() {
        long pendingCount = allInvoices.stream().filter(i -> i.getStatus().equals("Chờ thanh toán")).count();
        long paidCount = allInvoices.stream().filter(i -> i.getStatus().equals("Đã thanh toán")).count();
        int total = allInvoices.size();

        // Tổng hóa đơn chỉ tính các hóa đơn ĐÃ THANH TOÁN
        int revenue = allInvoices.stream()
                .filter(i -> i.getStatus().equals("Đã thanh toán"))
                .mapToInt(click1::getAmount)
                .sum();

        lblPending.setText(String.valueOf(pendingCount));
        lblPaid.setText(String.valueOf(paidCount));
        lblTotal.setText(String.valueOf(total));
        lblRevenue.setText(String.format("%,dđ", revenue));
    }
    
    // Lọc và hiển thị hóa đơn
    private void updateInvoiceList(int selectedIndex) {
        invoiceListPanel.removeAll();
        
        List<click1> filteredList;
        String statusFilter; 

        if (selectedIndex == 1) {
            statusFilter = "Chờ thanh toán";
        } else if (selectedIndex == 2) {
            statusFilter = "Đã thanh toán";
        } else if (selectedIndex == 3) {
            statusFilter = "Đã hủy";
        } else {
            statusFilter = null; 
        }

        if (selectedIndex == 0) { // Tất cả trạng thái (Chỉ hiển thị các hóa đơn CHƯA HỦY)
             filteredList = allInvoices.stream()
                     .filter(i -> !i.getStatus().equals("Đã hủy"))
                     .collect(Collectors.toList());
        } else {
            filteredList = allInvoices.stream()
                    .filter(i -> i.getStatus().equals(statusFilter))
                    .collect(Collectors.toList());
        }

        for (click1 invoice : filteredList) {
            invoiceListPanel.add(invoice);
        }

        invoiceListPanel.revalidate();
        invoiceListPanel.repaint();
    }

    private void showCreateInvoiceDialog(ActionEvent e) {
        // Lấy JFrame chứa panel hiện tại để làm parent cho JDialog
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        click2 dialog = new click2(parentFrame, this); 
        dialog.setVisible(true);
    }

    //Lớp click1 (Card Hóa đơn)
    class click1 extends JPanel {
        private String id;
        private String status;
        private int amount; 
        private HoaDonPanel parent;
        private List<MenuItem> items; // Danh sách món đã chọn

        private JLabel statusLabel;
        private JLabel priceLabel;
        private JButton payBtn; 
        private JButton cancelBtn; 

        // CẬP NHẬT hàm tạo để nhận List<MenuItem>
        public click1(HoaDonPanel parent, String invoiceId, String table, String customer, String status, int amount, List<MenuItem> items) {
            this.parent = parent;
            this.id = invoiceId;
            this.status = status;
            this.amount = amount; 
            this.items = items; // Lưu trữ danh sách món

            // Điều chỉnh màu sắc ban đầu dựa trên trạng thái
            Color borderColor = status.equals("Chờ thanh toán") ? new Color(252, 232, 131) : 
                                status.equals("Đã hủy") ? new Color(200, 200, 200) : new Color(106, 153, 78);
            Color statusBg = borderColor;
            Color statusFg = status.equals("Đã hủy") ? Color.BLACK : status.equals("Chờ thanh toán") ? Color.BLACK : Color.WHITE;
            
            // KÍCH THƯỚC CARD ĐÃ ĐƯỢC CHỈNH LẠI
            setPreferredSize(new Dimension(350, 500)); 
            setBackground(Color.WHITE);
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(5, 0, 0, 0, borderColor),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(Color.WHITE);
            JLabel idLabel = new JLabel(invoiceId);
            header.add(idLabel, BorderLayout.WEST);

            statusLabel = new JLabel(status);
            statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
            statusLabel.setOpaque(true);
            statusLabel.setBackground(statusBg);
            statusLabel.setForeground(statusFg);
            statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            header.add(statusLabel, BorderLayout.EAST);
            add(header, BorderLayout.NORTH);

            // PHẦN BODY: HIỂN THỊ CHI TIẾT MÓN ĂN
            JPanel body = new JPanel();
            body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
            body.setBackground(Color.WHITE);
            
            body.add(new JLabel("Tên bàn: " + table));
            body.add(new JLabel("Khách: " + (customer.isEmpty() ? "Không tên" : customer)));
            body.add(Box.createVerticalStrut(5));
            body.add(new JLabel("--- Chi tiết món ---"));
            body.add(Box.createVerticalStrut(5));
            
            // HIỂN THỊ DANH SÁCH MÓN ĐÃ CHỌN
            for (MenuItem item : items) {
                String detail = String.format("• %s (x%d): %,dđ", 
                                            item.name, 
                                            item.quantity, 
                                            item.price * item.quantity);
                JLabel itemLabel = new JLabel(detail);
                itemLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                body.add(itemLabel);
            }
            
            body.add(Box.createVerticalStrut(10));
            
            priceLabel = new JLabel("Tổng tiền: " + String.format("%,dđ", amount));
            priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
            body.add(priceLabel);
            
            add(body, BorderLayout.CENTER);

            // Phần Nút Bấm
            payBtn = new JButton("Thanh toán");
            payBtn.setBackground(new Color(247, 168, 61));
            payBtn.setForeground(Color.WHITE);
            payBtn.setFocusPainted(false);
            payBtn.setPreferredSize(new Dimension(100, 35));
            payBtn.addActionListener(e -> parent.markAsPaid(this));

            // Nút Hủy
            cancelBtn = new JButton("Hủy");
            cancelBtn.setBackground(new Color(180, 180, 180));
            cancelBtn.setForeground(Color.BLACK);
            cancelBtn.setFocusPainted(false);
            cancelBtn.setPreferredSize(new Dimension(100, 35));
            cancelBtn.addActionListener(e -> parent.markAsCancelled(this));


            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT)); 
            bottom.setBackground(Color.WHITE);
            bottom.add(cancelBtn); // Thêm nút Hủy
            bottom.add(payBtn);
            add(bottom, BorderLayout.SOUTH);
            
            updateButtonStatus();
        }

        public String getStatus() {
            return status;
        }

        public int getAmount() {
            return amount;
        }
        
        private void updateButtonStatus() {
            if (status.equals("Đã thanh toán") || status.equals("Đã hủy")) {
                payBtn.setEnabled(false);
                cancelBtn.setEnabled(false);
            } else {
                payBtn.setEnabled(true);
                cancelBtn.setEnabled(true);
            }
        }

        public void setAsPaid() {
            this.status = "Đã thanh toán";
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(5, 0, 0, 0, new Color(106, 153, 78)),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
            statusLabel.setText(status);
            statusLabel.setBackground(new Color(106, 153, 78));
            statusLabel.setForeground(Color.WHITE);
            updateButtonStatus();
        }
        
        // Phương thức Hủy hóa đơn
        public void setAsCancelled() {
            this.status = "Đã hủy";
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(5, 0, 0, 0, new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
            statusLabel.setText(status);
            statusLabel.setBackground(new Color(200, 200, 200));
            statusLabel.setForeground(Color.BLACK);
            updateButtonStatus();
        }
    }
    
    //Lớp MenuItem (Quản lý món ăn)
    class MenuItem {
        String name;
        int price;
        int quantity = 0; // Số lượng

        public MenuItem(String name, int price) {
            this.name = name;
            this.price = price;
        }
    }

    // Lớp click2 (Form tạo hóa đơn)
    class click2 extends JDialog {
        private List<MenuItem> menuItems; 
        private JLabel totalAmountLabel;
        private int totalAmount = 0;
        private HoaDonPanel invoicePanel;
        
        // CẬP NHẬT: Nhận JFrame parent và HoaDonPanel
        public click2(JFrame parentFrame, HoaDonPanel invoicePanel) { 
            super(parentFrame, "Tạo hóa đơn mới", true);
            this.invoicePanel = invoicePanel;
            
            // Khởi tạo Menu
            menuItems = new ArrayList<>();
            menuItems.add(new MenuItem("Croissant", 45000));
            menuItems.add(new MenuItem("Pain au Chocolat", 60000));
            menuItems.add(new MenuItem("Baguette", 35000));
            menuItems.add(new MenuItem("Tarte", 15000));
            menuItems.add(new MenuItem("Đào xinh", 500000));

            setLayout(new BorderLayout());
            setSize(450, 600);
            setLocationRelativeTo(parentFrame);

            JPanel headerPanel = new JPanel();
            headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

            JLabel titleLabel = new JLabel("Tạo hóa đơn mới");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            JLabel subtitleLabel = new JLabel("Chọn món ăn và tạo hóa đơn cho bàn");
            subtitleLabel.setForeground(Color.GRAY);

            headerPanel.add(titleLabel);
            headerPanel.add(subtitleLabel);
            add(headerPanel, BorderLayout.NORTH);

            JPanel formPanel = new JPanel();
            formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
            formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

            formPanel.add(new JLabel("Tên bàn"));
            JTextField tableField = new JTextField("VD: T1-05, VIP-02");
            tableField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 15)); 
            tableField.setForeground(Color.GRAY);
            tableField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (tableField.getText().equals("VD: T1-05, VIP-02")) {
                        tableField.setText("");
                        tableField.setForeground(Color.BLACK);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (tableField.getText().isEmpty()) {
                        tableField.setText("VD: T1-05, VIP-02");
                        tableField.setForeground(Color.GRAY);
                    }
                }
            });
            formPanel.add(tableField);
            formPanel.add(Box.createVerticalStrut(10));

            formPanel.add(new JLabel("Tên khách hàng (tùy chọn)"));
            JTextField customerField = new JTextField("Nhập tên khách hàng");
            customerField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 15));
            customerField.setForeground(Color.GRAY);
            customerField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (customerField.getText().equals("Nhập tên khách hàng")) {
                        customerField.setText("");
                        customerField.setForeground(Color.BLACK);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (customerField.getText().isEmpty()) {
                        customerField.setText("Nhập tên khách hàng");
                        customerField.setForeground(Color.GRAY);
                    }
                }
            });
            formPanel.add(customerField);
            formPanel.add(Box.createVerticalStrut(20));


            formPanel.add(new JLabel("Chọn món ăn"));
            formPanel.add(Box.createVerticalStrut(5));
            
            // Danh sách các món ăn thực tế
            JPanel menuList = new JPanel();
            menuList.setLayout(new BoxLayout(menuList, BoxLayout.Y_AXIS));
            menuList.setBackground(Color.WHITE);
            
            for(MenuItem item : menuItems) {
                menuList.add(createMenuItem(item));
            }

            JScrollPane scrollPane = new JScrollPane(menuList);
            scrollPane.setPreferredSize(new Dimension(360, 200));
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            formPanel.add(scrollPane);
            
            // Thêm phần hiển thị Tổng tiền
            totalAmountLabel = new JLabel("Tổng tiền: 0đ");
            totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 16));
            formPanel.add(Box.createVerticalStrut(10));
            formPanel.add(totalAmountLabel);

            add(formPanel, BorderLayout.CENTER);

            JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

            JButton createBtn = new JButton("Tạo hóa đơn");
            createBtn.setBackground(new Color(247, 168, 61));
            createBtn.setForeground(Color.WHITE);
            createBtn.setPreferredSize(new Dimension(170, 35));
            
            // Gửi totalAmount thực tế khi tạo hóa đơn
            createBtn.addActionListener(e -> {
                // Lấy giá trị thực tế của Số bàn
                String table = tableField.getText().trim();
                
                // 1. Kiểm tra BẮT BUỘC nhập Số bàn
                if (table.isEmpty() || table.equals("VD: T1-05, VIP-02")) {
                    JOptionPane.showMessageDialog(this, 
                                                  "Vui lòng nhập Số bàn hợp lệ để tạo hóa đơn.", 
                                                  "Lỗi nhập liệu", 
                                                  JOptionPane.ERROR_MESSAGE);
                    return; // Ngừng thực thi nếu Số bàn trống
                }

                // Lấy các món đã chọn (số lượng > 0)
                List<MenuItem> selectedItems = menuItems.stream()
                        .filter(item -> item.quantity > 0)
                        .collect(Collectors.toList());
                
                // 2. (TÙY CHỌN) Kiểm tra nếu chưa chọn món nào
                if (selectedItems.isEmpty()) {
                     JOptionPane.showMessageDialog(this, 
                                                  "Vui lòng chọn ít nhất một món ăn.", 
                                                  "Lỗi tạo hóa đơn", 
                                                  JOptionPane.ERROR_MESSAGE);
                    return; 
                }
                
                // 3. Tên khách hàng (customerField) không bắt buộc
                String customer = customerField.getText().trim();
                if (customer.equals("Nhập tên khách hàng") || customer.isEmpty()) {
                    customer = ""; 
                }
                
                // Nếu hợp lệ, tạo hóa đơn VÀ TRUYỀN DANH SÁCH MÓN ĐÃ CHỌN
                invoicePanel.addInvoice(table, customer, totalAmount, selectedItems); // Dùng invoicePanel
                dispose();
            });

            JButton cancelBtn = new JButton("Hủy");
            cancelBtn.setBackground(Color.WHITE);
            cancelBtn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            cancelBtn.setPreferredSize(new Dimension(170, 35));
            cancelBtn.addActionListener(e -> dispose());

            actionPanel.add(createBtn);
            actionPanel.add(cancelBtn);

            add(actionPanel, BorderLayout.SOUTH);
        }
        
        // Cập nhật hàm tạo item để xử lý số lượng và tính tổng
        private JPanel createMenuItem(MenuItem item) {
            JPanel itemPanel = new JPanel(new BorderLayout(10, 0));
            itemPanel.setBackground(Color.WHITE);
            itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JLabel nameLabel = new JLabel(item.name);
            JLabel priceLabel = new JLabel(String.format("%,d VND", item.price));
            priceLabel.setFont(new Font("Arial", Font.BOLD, 14));

            JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
            quantitySpinner.setPreferredSize(new Dimension(60, 25));
            
            // Thêm Listener cho Spinner để tính tổng
            quantitySpinner.addChangeListener(e -> {
                int newQuantity = (int) quantitySpinner.getValue();
                int oldQuantity = item.quantity;
                item.quantity = newQuantity;
                
                totalAmount = totalAmount - (oldQuantity * item.price) + (newQuantity * item.price);
                totalAmountLabel.setText("Tổng tiền: " + String.format("%,dđ", totalAmount));
            });

            itemPanel.add(nameLabel, BorderLayout.WEST);
            itemPanel.add(priceLabel, BorderLayout.CENTER);
            itemPanel.add(quantitySpinner, BorderLayout.EAST); 

            itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            return itemPanel;
        }
    }
}