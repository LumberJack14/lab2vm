import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.TextAnchor;

import javax.swing.*;
import java.awt.*;

class SystemPlotPanel extends JPanel {
    private int selectedSystem = 2;
    private ChartPanel chartPanel;

    public SystemPlotPanel() {
        setLayout(new BorderLayout());
    }

    public void setSystem(int system) {
        this.selectedSystem = system;
        updateChart();
    }

    public int getSelectedSystem() {
        return selectedSystem;
    }

    private void updateChart() {
        XYSeries series1Positive = new XYSeries("Equation 1 (Positive)");
        XYSeries series1Negative = new XYSeries("Equation 1 (Negative)");
        XYSeries series2Positive = new XYSeries("Equation 2 (Positive)");
        XYSeries series2Negative = new XYSeries("Equation 2 (Negative)");

        if (selectedSystem == 1) {

            for (double x = -10; x <= 10; x += 0.01) {
                double y = Math.sqrt(4 - x * x);
                series1Positive.add(x, y);
                series1Negative.add(x, -y);
            }

            for (double x = -10; x <= 10; x += 0.01) {
                double y = 3 * x * x;
                series2Positive.add(x, y);
            }
        } else if (selectedSystem == 2) {

            for (double x = -10; x <= 10; x += 0.01) {
                double y = (3 - x * x) / 2;
                series1Positive.add(x, y);
            }

            for (double x = -10; x <= 10; x += 0.01) {
                double y1 = Math.sqrt(5 - 3 * x);
                double y2 = -Math.sqrt(5 - 3 * x);
                series2Positive.add(x, y1);
                series2Negative.add(x, y2);
            }
        }


        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1Positive);
        dataset.addSeries(series1Negative);
        dataset.addSeries(series2Positive);
        dataset.addSeries(series2Negative);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "График системы уравнений",
                "x",
                "y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        );

        XYPlot plot = chart.getXYPlot();

        plot.getRenderer().setSeriesPaint(0, Color.RED);
        plot.getRenderer().setSeriesPaint(1, Color.RED);
        plot.getRenderer().setSeriesPaint(2, Color.BLUE);
        plot.getRenderer().setSeriesPaint(3, Color.BLUE);

        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setRange(-10, 10); // x range

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(-5, 5); // y range

        ValueMarker oxMarker = new ValueMarker(0);
        oxMarker.setPaint(Color.BLACK);
        oxMarker.setStroke(new BasicStroke(2));
        oxMarker.setLabel("OX");
        oxMarker.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
        oxMarker.setLabelTextAnchor(TextAnchor.BOTTOM_RIGHT);
        plot.addRangeMarker(oxMarker);

        ValueMarker oyMarker = new ValueMarker(0);
        oyMarker.setPaint(Color.BLACK);
        oyMarker.setStroke(new BasicStroke(2));
        oyMarker.setLabel("OY");
        oyMarker.setLabelAnchor(RectangleAnchor.BOTTOM_LEFT);
        oyMarker.setLabelTextAnchor(TextAnchor.TOP_LEFT);
        plot.addDomainMarker(oyMarker);

        if (chartPanel != null) {
            remove(chartPanel);
        }

        chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
}
