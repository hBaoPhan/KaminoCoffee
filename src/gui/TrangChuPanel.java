package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList; 
import java.util.Objects; 

import dao.DonDatBan_dao;
import dao.HoaDon_dao;
import dao.KhachHang_dao;
import dao.ThongKe_dao;
import entity.TaiKhoan;
import entity.NhanVien;
import entity.ChucVu;
import entity.DonDatBan; // Đảm bảo đã import
import entity.KhachHang; // Cần cho logic mới
import entity.Ban; // Cần cho logic mới

public class TrangChuPanel extends JPanel {

    // ===== DAO =====
    private DonDatBan_dao donDatBan_dao;
    private HoaDon_dao hoaDon_dao;
    private KhachHang_dao khachHang_dao;
    private ThongKe_dao thongKe_dao;

    // ===== Components cho Thống kê =====
    private JLabel lblDatBanValue;
    private JLabel lblHoaDonValue;
    private JLabel lblDoanhThuValue;
    private JLabel lblKhachHangValue;
    
    // ===== Components cho Hoạt động gần đây =====
    private JPanel pnlList; // Panel chứa danh sách hoạt động
    
    // ===== Biến chung =====
	private Font customFont = new Font("Times New Roman", Font.BOLD, 20); 
    private TaiKhoan taiKhoan;
    private DecimalFormat df = new DecimalFormat("#,### đ");
	private boolean laQuanLy;

    /**
     * Constructor chính
     */
    public TrangChuPanel(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
        laQuanLy=taiKhoan.getNhanVien().getChucVu()==ChucVu.QUAN_LY;
        // ===== Khởi tạo DAO =====
        donDatBan_dao = new DonDatBan_dao();
        hoaDon_dao = new HoaDon_dao();
        khachHang_dao = new KhachHang_dao();
        thongKe_dao = new ThongKe_dao();

        setLayout(new BorderLayout());
        setBackground(new Color(250, 247, 243));

        // ======= 1. Panel chào mừng (NORTH) =======
        JPanel pnlWelcome = new JPanel(new BorderLayout());
        pnlWelcome.setBackground(new Color(245, 240, 230));
        pnlWelcome.setBorder(new EmptyBorder(15, 25, 15, 25));
        JLabel lblHello = new JLabel("Xin chào, " + taiKhoan.getNhanVien().getTenNV() + "!");
        lblHello.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel lblDesc = new JLabel("Chào mừng bạn đến với hệ thống quản lý Kamino Cafe");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlWelcome.add(lblHello, BorderLayout.NORTH);
        pnlWelcome.add(lblDesc, BorderLayout.CENTER);

        // ======= 2. Panel thống kê nhanh (CENTER) =======
        JPanel pnlStats = new JPanel(new GridLayout(1, 4, 20, 0));
        pnlStats.setBackground(getBackground());
        pnlStats.setBorder(new EmptyBorder(20, 25, 0, 25));

        lblDatBanValue = new JLabel("0");
        lblHoaDonValue = new JLabel("0");
        lblDoanhThuValue = new JLabel("0 đ");
        lblKhachHangValue = new JLabel("0");

        pnlStats.add(createStatCard("Đặt bàn hôm nay", lblDatBanValue, "", new Color(255, 115, 0)));
        pnlStats.add(createStatCard("Hóa đơn", lblHoaDonValue, "", new Color(0, 120, 215)));
        if(laQuanLy) {
        	   pnlStats.add(createStatCard("Doanh thu tạm tính", lblDoanhThuValue, "", new Color(0, 153, 51)));
        }
        pnlStats.add(createStatCard("Khách hàng", lblKhachHangValue, "", new Color(0, 102, 255)));

        // ======= 3. Panel trung tâm (SOUTH) =======
        JPanel pnlCenter = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlCenter.setBorder(new EmptyBorder(20, 25, 25, 25));
        pnlCenter.setBackground(getBackground());
        
        pnlCenter.add(createQuickActionsPanel()); 
        pnlCenter.add(createRecentActivityPanel());

        // ======= Thêm tất cả vào layout chính =======
        add(pnlWelcome, BorderLayout.NORTH);
        add(pnlStats, BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.SOUTH);

        // ======= Tải dữ liệu thực =======
        loadThongKeData();
        loadRecentActivityData();
    }

    /**
     * Tạo một thẻ thống kê
     */
    private JPanel createStatCard(String title, JLabel lblValue, String change, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230)), new EmptyBorder(15, 20, 15, 20)));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblValue.setForeground(color);

        JLabel lblChange = new JLabel(change);
        lblChange.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblChange.setForeground(new Color(120, 120, 120));

        JPanel pnlText = new JPanel(new BorderLayout());
        pnlText.setOpaque(false);
        pnlText.add(lblTitle, BorderLayout.NORTH);
        pnlText.add(lblValue, BorderLayout.CENTER);
        pnlText.add(lblChange, BorderLayout.SOUTH);

        card.add(pnlText, BorderLayout.CENTER);
        return card;
    }

    /**
     * Tạo panel Thao tác nhanh (dùng MouseAdapter để điều hướng)
     */
    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
                new LineBorder(new Color(230, 230, 230)),
                new EmptyBorder(15, 20, 15, 20)));

        JLabel lblTitle = new JLabel("Thao tác nhanh");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel lblDesc = new JLabel("Các chức năng thường dùng");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        pnlHeader.add(lblTitle, BorderLayout.NORTH);
        pnlHeader.add(lblDesc, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel(new GridLayout(2, 2, 15, 15));
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(new EmptyBorder(20, 0, 0, 0));

        String[] tabs = laQuanLy
                ? new String[]{"Đặt bàn mới", "Quản lý hóa đơn", "Quản lý khách hàng", "Xem thống kê"}
                : new String[]{"Đặt bàn mới", "Quản lý hóa đơn", "Quản lý khách hàng"};

        Color[] colors = {
                new Color(0, 102, 255),
                new Color(0, 180, 0),
                new Color(255, 115, 0),
                new Color(102, 51, 255)
        };

        for (int i = 0; i < tabs.length; i++) {
            JLabel label = new JLabel(tabs[i], SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 22));
            label.setForeground(Color.WHITE);
            label.setOpaque(true);
            label.setBackground(i < colors.length ? colors[i] : Color.GRAY);
            label.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
            label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            

            Color base = label.getBackground();
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    label.setBackground(base.darker());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    label.setBackground(base);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    handleNavigation(label.getText());
                }
            });

            pnlButtons.add(label);
        }

        panel.add(pnlHeader, BorderLayout.NORTH);
        panel.add(pnlButtons, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Hàm điều hướng (gọi đến Frame cha)
     */
    private void handleNavigation(String action) {
        Container parent = getParent();
        while (parent != null && !(parent instanceof NavBar)) {
            parent = parent.getParent();
        }

        if (parent instanceof NavBar navBar) {
            
            navBar.switchTo(action); // Gọi hàm của NavBar
            
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi điều hướng: Không tìm thấy 'NavBar' parent", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Tạo panel Hoạt động gần đây (chỉ tạo khung)
     */
    private JPanel createRecentActivityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230)), new EmptyBorder(15, 20, 15, 20)));

        JLabel lblTitle = new JLabel("Hoạt động gần đây");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel lblDesc = new JLabel("Các lượt đặt bàn mới nhất trong hệ thống");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        pnlHeader.add(lblTitle, BorderLayout.NORTH);
        pnlHeader.add(lblDesc, BorderLayout.CENTER);

        pnlList = new JPanel(new GridLayout(4, 1, 10, 10));
        pnlList.setBackground(Color.WHITE);
        pnlList.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        panel.add(pnlHeader, BorderLayout.NORTH);
        panel.add(pnlList, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Tạo một mục hoạt động (đã sửa để nhận LocalTime)
     */
    private JPanel createActivityItem(LocalTime time, String title, String desc, String status, Color statusColor) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(new Color(250, 250, 250));
        item.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblTime = new JLabel(time != null ? time.withNano(0).toString() : "HH:MM:SS");
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTime.setForeground(new Color(120, 120, 120));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(100, 100, 100));

        JPanel pnlText = new JPanel(new GridLayout(2, 1));
        pnlText.setOpaque(false);
        pnlText.add(lblTitle);
        pnlText.add(lblDesc);

        JButton btnStatus = new JButton(status);
        btnStatus.setBackground(statusColor);
        btnStatus.setForeground(Color.WHITE);
        btnStatus.setFocusPainted(false);
        btnStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnStatus.setBorder(new EmptyBorder(5, 10, 5, 10));

        JPanel pnlLeft = new JPanel(new BorderLayout());
        pnlLeft.setOpaque(false);
        pnlLeft.add(lblTime, BorderLayout.WEST);
        pnlLeft.add(pnlText, BorderLayout.CENTER);

        item.add(pnlLeft, BorderLayout.CENTER);
        item.add(btnStatus, BorderLayout.EAST);
        return item;
    }

    /**
     * Tải dữ liệu cho 4 thẻ thống kê
     */
    public void loadThongKeData() {
        try {
            int datBan = donDatBan_dao.countToday();
            int hoaDon = hoaDon_dao.countToday();
            double doanhThu = thongKe_dao.getDoanhThuHomNay();
            int khachHang = khachHang_dao.countAll();

            lblDatBanValue.setText(String.valueOf(datBan));
            lblHoaDonValue.setText(String.valueOf(hoaDon));
            lblDoanhThuValue.setText(df.format(doanhThu));
            lblKhachHangValue.setText(String.valueOf(khachHang));
        } catch (Exception e) {
            e.printStackTrace();
            lblDatBanValue.setText("Lỗi");
            lblHoaDonValue.setText("Lỗi");
            lblDoanhThuValue.setText("Lỗi");
            lblKhachHangValue.setText("Lỗi");
        }
    }
    
    /**
     * (ĐÃ SỬA) Tải dữ liệu cho panel Hoạt động gần đây
     * Logic được cập nhật để dùng 'isDaNhan' và 'thoiGian'
     */
    public void loadRecentActivityData() {
        pnlList.removeAll();
        
        try {
            ArrayList<DonDatBan> recentBookings = donDatBan_dao.getRecentDonDatBan(4);

            for (DonDatBan ddb : recentBookings) {
                String title = "Đặt bàn"; 
                String desc = "";
                String status = "";
                Color statusColor;

                String tenBan = (ddb.getBan() != null && ddb.getBan().getMaBan() != null) 
                                ? ddb.getBan().getMaBan() : "Không rõ";
                
                String maKH = "N/A";
                if (ddb.getKhachHang() != null) {
                    maKH = ddb.getKhachHang().getMaKhachHang(); 
                }
                
                desc = "Bàn " + tenBan + ", KH: " + maKH;

                if (ddb.isDaNhan()) {
                    status = "Đã nhận";
                    statusColor = new Color(0, 153, 51);
                } else {
                    status = "Chưa nhận";
                    statusColor = new Color(255, 153, 0);
                }
                
                LocalTime time = (ddb.getThoiGian() != null) ? ddb.getThoiGian().toLocalTime() : LocalTime.now();
                
                pnlList.add(createActivityItem(time, title, desc, status, statusColor));
            }

            // 3. Lấp đầy các ô trống để giữ layout 4 hàng
            for (int i = recentBookings.size(); i < 4; i++) {
                JPanel emptyPanel = new JPanel();
                emptyPanel.setBackground(Color.WHITE); // Giữ nền trắng
                pnlList.add(emptyPanel);
            }

        } catch (Exception e) {
            e.printStackTrace();
            pnlList.add(new JLabel("Lỗi khi tải hoạt động gần đây..."));
        }

        // Cập nhật lại UI
        pnlList.revalidate();
        pnlList.repaint();
    }
}