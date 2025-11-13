package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import dao.ThongKe_dao;

@SuppressWarnings("serial")
public class ThongKePanel extends JPanel {
    private ThongKe_dao thongKeDAO = new ThongKe_dao();

    private JLabel lblDoanhThu, lblHoaDon, lblSPBanChay, lblKhachHang;
    private ChartPanel chartPanel, piePanel;

    public ThongKePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#F7F4EC"));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("THỐNG KÊ DOANH THU");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle, BorderLayout.NORTH);

        // ===== Tổng quan =====
        JPanel pnlSummary = new JPanel(new GridLayout(1, 4, 20, 0));
        pnlSummary.setBackground(Color.decode("#F7F4EC"));
        pnlSummary.setBorder(new EmptyBorder(20, 0, 20, 0));

        lblDoanhThu = new JLabel();
        lblHoaDon = new JLabel();
        lblSPBanChay = new JLabel();
        lblKhachHang = new JLabel();

        pnlSummary.add(createCard("Tổng doanh thu", lblDoanhThu, new Color(46, 204, 113)));
        pnlSummary.add(createCard("Tổng số hóa đơn", lblHoaDon, new Color(52, 152, 219)));
        pnlSummary.add(createCard("SP bán chạy nhất", lblSPBanChay, new Color(155, 89, 182)));
        pnlSummary.add(createCard("Số lượng khách hàng", lblKhachHang, new Color(241, 196, 15)));

        add(pnlSummary, BorderLayout.NORTH);

        // ===== Biểu đồ =====
        chartPanel = new ChartPanel(null);
        chartPanel.setPreferredSize(new Dimension(300, 250));

        piePanel = new ChartPanel(null);
        piePanel.setPreferredSize(new Dimension(300, 250));

        JPanel pnlCharts = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlCharts.setBackground(Color.decode("#F7F4EC"));
        pnlCharts.setBorder(new EmptyBorder(10, 0, 0, 0));
        pnlCharts.add(chartPanel);
        pnlCharts.add(piePanel);

        add(pnlCharts, BorderLayout.CENTER);
        loadDuLieuThongKe();
    }

    public void loadDuLieuThongKe() {
        double tongDoanhThu = thongKeDAO.getTongDoanhThu();
        int tongHoaDon = thongKeDAO.getTongSoHoaDon();
        String spBanChay = thongKeDAO.getSanPhamBanChayNhat();
        int tongKhachHang = thongKeDAO.getTongSoKhachHang();

        lblDoanhThu.setText(String.format("%,.0f ₫", tongDoanhThu));
        lblHoaDon.setText(String.valueOf(tongHoaDon));
        lblSPBanChay.setText(spBanChay);
        lblKhachHang.setText(String.valueOf(tongKhachHang));

        Map<Integer, Double> data = thongKeDAO.getDoanhThuTheoThang();
        chartPanel.setChart(createBarChart(data));

        Map<String, Double> pieData = thongKeDAO.getTiLeLoaiSanPham();
        piePanel.setChart(createPieChart(pieData));
    }

    private JPanel createCard(String title, JLabel valueLabel, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(bgColor);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(Color.WHITE);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.SOUTH);
        return card;
    }

    private JFreeChart createBarChart(Map<Integer, Double> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<Integer, Double> e : data.entrySet()) {
            dataset.addValue(e.getValue(), "Doanh thu", "Tháng " + e.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
            "Biểu đồ doanh thu theo tháng", "Tháng", "Doanh thu (Nghìn đồng)",
            dataset, PlotOrientation.VERTICAL, true, true, false
        );

        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(new Font("Segoe UI", Font.PLAIN, 14));

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.decode("#F7F4EC"));
        plot.getDomainAxis().setLabelFont(new Font("Segoe UI", Font.PLAIN, 13));
        plot.getDomainAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 11));
        plot.getRangeAxis().setLabelFont(new Font("Segoe UI", Font.PLAIN, 13));
        plot.getRangeAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 11));

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setItemMargin(0.01);
        renderer.setMaximumBarWidth(0.1);
        renderer.setSeriesPaint(0, Color.decode("#00A651"));
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardBarPainter());

        return chart;
    }

    private JFreeChart createPieChart(Map<String, Double> data) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
            "Tỷ lệ sử dụng loại sản phẩm", dataset, true, true, false
        );
       

     // Màu nền bên trong vùng vẽ (plot)
	    PiePlot plot = (PiePlot) chart.getPlot();
	    plot.setBackgroundPaint(Color.decode("#F7F4EC")); // hoặc Color.WHITE
	    plot.setOutlineVisible(false); 
        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chart.getLegend().setItemFont(new Font("Segoe UI", Font.PLAIN, 12));
        return chart;
    }
}
