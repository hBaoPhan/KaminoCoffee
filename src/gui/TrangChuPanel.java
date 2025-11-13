package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;

import dao.DonDatBan_dao;
import dao.HoaDon_dao;
import dao.KhachHang_dao;
import dao.ThongKe_dao;

public class TrangChuPanel extends JPanel {

    private DonDatBan_dao donDatBan_dao;
    private HoaDon_dao hoaDon_dao;
    private KhachHang_dao khachHang_dao;
    private ThongKe_dao thongKe_dao;

    private JLabel lblDatBanValue;
    private JLabel lblHoaDonValue;
    private JLabel lblDoanhThuValue;
    private JLabel lblKhachHangValue;

    private DecimalFormat df = new DecimalFormat("#,### đ");

    public TrangChuPanel() {
        // ===== DAO =====
        donDatBan_dao = new DonDatBan_dao();
        hoaDon_dao = new HoaDon_dao();
        khachHang_dao = new KhachHang_dao();
        thongKe_dao = new ThongKe_dao();

        setLayout(new BorderLayout());
        setBackground(new Color(250, 247, 243));

        // ======= Panel chào mừng =======
        JPanel pnlWelcome = new JPanel(new BorderLayout());
        pnlWelcome.setBackground(new Color(245, 240, 230));
        pnlWelcome.setBorder(new EmptyBorder(15, 25, 15, 25));
        JLabel lblHello = new JLabel("Xin chào, " + LocalDate.now().getDayOfWeek() + "!");
        lblHello.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel lblDesc = new JLabel("Chào mừng bạn đến với hệ thống quản lý Kamino Cafe");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlWelcome.add(lblHello, BorderLayout.NORTH);
        pnlWelcome.add(lblDesc, BorderLayout.CENTER);

        // ======= Panel thống kê nhanh =======
        JPanel pnlStats = new JPanel(new GridLayout(1, 4, 20, 0));
        pnlStats.setBackground(getBackground());
        pnlStats.setBorder(new EmptyBorder(20, 25, 0, 25));

        lblDatBanValue = new JLabel("0");
        lblHoaDonValue = new JLabel("0");
        lblDoanhThuValue = new JLabel("0 đ");
        lblKhachHangValue = new JLabel("0");

        pnlStats.add(createStatCard("Đặt bàn hôm nay", lblDatBanValue, "", new Color(255, 115, 0)));
        pnlStats.add(createStatCard("Hóa đơn hôm nay", lblHoaDonValue, "", new Color(0, 120, 215)));
        pnlStats.add(createStatCard("Doanh thu tạm tính", lblDoanhThuValue, "", new Color(0, 153, 51)));
        pnlStats.add(createStatCard("Khách hàng", lblKhachHangValue, "", new Color(0, 102, 255)));

        // ======= Panel trung tâm =======
        JPanel pnlCenter = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlCenter.setBorder(new EmptyBorder(20, 25, 25, 25));
        pnlCenter.setBackground(getBackground());
        pnlCenter.add(createQuickActionsPanel());
        pnlCenter.add(createRecentActivityPanel());

        // ======= Thêm tất cả vào layout =======
        add(pnlWelcome, BorderLayout.NORTH);
        add(pnlStats, BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.SOUTH);

        // ======= Load dữ liệu thực =======
        loadThongKeData();
    }

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

    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230)), new EmptyBorder(15, 20, 15, 20)));

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

        pnlButtons.add(createActionButton("Đặt bàn mới", new Color(0, 102, 255)));
        pnlButtons.add(createActionButton("Tạo hóa đơn", new Color(0, 180, 0)));
        pnlButtons.add(createActionButton("Quản lý khách hàng", new Color(255, 115, 0)));
        pnlButtons.add(createActionButton("Xem thống kê", new Color(102, 51, 255)));

        panel.add(pnlHeader, BorderLayout.NORTH);
        panel.add(pnlButtons, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRecentActivityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230)), new EmptyBorder(15, 20, 15, 20)));

        JLabel lblTitle = new JLabel("Hoạt động gần đây");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel lblDesc = new JLabel("Các thao tác mới nhất trong hệ thống");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        pnlHeader.add(lblTitle, BorderLayout.NORTH);
        pnlHeader.add(lblDesc, BorderLayout.CENTER);

        JPanel pnlList = new JPanel(new GridLayout(4, 1, 10, 10));
        pnlList.setBackground(Color.WHITE);
        pnlList.setBorder(new EmptyBorder(15, 0, 0, 0));

        pnlList.add(createActivityItem("Đặt bàn mới", "Bàn VIP-02, 4 khách", "Thành công", new Color(0, 153, 51)));
        pnlList.add(createActivityItem("Thanh toán", "Hóa đơn #HD001234 - 850,000đ", "Thành công", new Color(0, 153, 51)));
        pnlList.add(createActivityItem("Hủy đặt bàn", "Bàn T1-05, khách hủy", "Cảnh báo", new Color(255, 153, 0)));
        pnlList.add(createActivityItem("Đặt bàn mới", "Bàn SV-08, 6 khách", "Thành công", new Color(0, 153, 51)));

        panel.add(pnlHeader, BorderLayout.NORTH);
        panel.add(pnlList, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createActivityItem(String title, String desc, String status, Color statusColor) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(new Color(250, 250, 250));
        item.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblTime = new JLabel(LocalTime.now().withNano(0).toString());
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

    private JPanel createActionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(new EmptyBorder(10, 10, 10, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(Color.WHITE);
        wrap.add(btn, BorderLayout.CENTER);
        return wrap;
    }

    public void loadThongKeData() {
        int datBan = donDatBan_dao.countToday();
		int hoaDon = hoaDon_dao.countToday();
		double doanhThu = thongKe_dao.getDoanhThuHomNay();
		int khachHang = khachHang_dao.countAll();

		lblDatBanValue.setText(String.valueOf(datBan));
		lblHoaDonValue.setText(String.valueOf(hoaDon));
		lblDoanhThuValue.setText(df.format(doanhThu));
		lblKhachHangValue.setText(String.valueOf(khachHang));
    }

}
