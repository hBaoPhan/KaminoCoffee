package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.DefaultCategoryDataset;

import dao.ThongKe_dao;
public class ThongKePanel extends JPanel {
    private ThongKe_dao thongKeDAO = new ThongKe_dao();

    public ThongKePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("📈 THỐNG KÊ DOANH THU");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle, BorderLayout.NORTH);

        // ===== Tổng quan =====
        JPanel pnlSummary = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlSummary.setBackground(Color.WHITE);
        pnlSummary.setBorder(new EmptyBorder(20, 0, 20, 0));

        double tongDoanhThu = thongKeDAO.getTongDoanhThu();
        int tongHoaDon = thongKeDAO.getTongSoHoaDon();
        String spBanChay = thongKeDAO.getSanPhamBanChayNhat();

        pnlSummary.add(createCard("💰 Tổng doanh thu", String.format("%,.0f ₫", tongDoanhThu), new Color(46, 204, 113)));
        pnlSummary.add(createCard("📄 Tổng số hóa đơn", String.valueOf(tongHoaDon), new Color(52, 152, 219)));
        pnlSummary.add(createCard("🥇 SP bán chạy nhất", spBanChay, new Color(155, 89, 182)));

        add(pnlSummary, BorderLayout.CENTER);

        // ===== Biểu đồ doanh thu theo tháng =====
        JPanel pnlChart = new JPanel(new BorderLayout());
        pnlChart.setBackground(Color.WHITE);
        pnlChart.setBorder(new TitledBorder("Biểu đồ doanh thu theo tháng"));

        Map<Integer, Double> data = thongKeDAO.getDoanhThuTheoThang();
        JFreeChart chart = createChart(data);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(900, 400));
        pnlChart.add(chartPanel, BorderLayout.CENTER);

        add(pnlChart, BorderLayout.SOUTH);
    }

    private JPanel createCard(String title, String value, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(bgColor);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblValue.setForeground(Color.WHITE);
        lblValue.setHorizontalAlignment(SwingConstants.RIGHT);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.SOUTH);
        return card;
    }

    private JFreeChart createChart(Map<Integer, Double> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (data.isEmpty()) {
            // fallback nếu không có dữ liệu
            dataset.addValue(0, "Doanh thu", "T1");
        } else {
            for (Map.Entry<Integer, Double> e : data.entrySet()) {
                dataset.addValue(e.getValue() / 1_000_000, "Doanh thu", "T" + e.getKey());
            }
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "", "Tháng", "Doanh thu (triệu ₫)",
                dataset, PlotOrientation.VERTICAL, false, true, false
        );

        chart.setBackgroundPaint(Color.WHITE);
        chart.getPlot().setBackgroundPaint(new Color(250, 250, 250));
        return chart;
    }
}
