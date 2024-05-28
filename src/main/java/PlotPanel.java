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

class PlotPanel extends JPanel {
    private int selectedFunction = 1;
    private int selectedMethod = 1;
    private ChartPanel chartPanel;

    public PlotPanel() {
        setLayout(new BorderLayout());
    }

    public void setFunction(int function) {
        this.selectedFunction = function;
        updateChart();
    }

    public void setMethod(int method) {
        this.selectedMethod = method;
        // Method logic can be added here
        System.out.println("Selected Method: " + method);
    }

    private void updateChart() {
        XYSeries series = new XYSeries("Function");

        // Generate data for the selected function
        for (int x = -100; x <= 100; x++) { // Adjust number of points
            double xx = x / 10.0; // Scale x values for better plotting
            double y;
            switch (selectedFunction) {
                case 1:
                    y = 2.3 * Math.pow(xx, 3) + 5.75 * Math.pow(xx, 2) - 7.41 * xx - 10.6;
                    break;
                case 2:
                    y = Math.sin(xx) + 0.25 * xx - 1;
                    break;
                case 3:
                    y = 1.8 * Math.pow(xx, 3) - Math.pow(xx, 2) - 5 * xx + 1.539;
                    break;
                default:
                    y = 0;
            }
            // Only add points within the specified y-range
            if (y >= -8 && y <= 8) {
                series.add(xx, y);
            }
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "График функции",
                "x",
                "y",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );

        // Customize plot
        XYPlot plot = chart.getXYPlot();

        // Set a specific range for the x-axis and y-axis
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setRange(-10, 10); // x range

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(-5, 5); // y range

        // OX axis (y=0) marker
        ValueMarker oxMarker = new ValueMarker(0);
        oxMarker.setPaint(Color.BLACK);
        oxMarker.setStroke(new BasicStroke(2));
        oxMarker.setLabel("OX");
        oxMarker.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
        oxMarker.setLabelTextAnchor(TextAnchor.BOTTOM_RIGHT);
        plot.addRangeMarker(oxMarker);

        // OY axis (x=0) marker
        ValueMarker oyMarker = new ValueMarker(0);
        oyMarker.setPaint(Color.BLACK);
        oyMarker.setStroke(new BasicStroke(2));
        oyMarker.setLabel("OY");
        oyMarker.setLabelAnchor(RectangleAnchor.BOTTOM_LEFT);
        oyMarker.setLabelTextAnchor(TextAnchor.TOP_LEFT);
        plot.addDomainMarker(oyMarker);

        // Remove the old chart panel if it exists
        if (chartPanel != null) {
            remove(chartPanel);
        }

        chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);

        // Refresh the panel
        revalidate();
        repaint();
    }

    public int getSelectedFunction() {
        return selectedFunction;
    }

    public int getSelectedMethod() {
        return selectedMethod;
    }
}