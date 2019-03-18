import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;


import java.io.File;
import java.io.IOException;
import java.util.List;


public class ChartSeries extends ApplicationFrame {

    private List<Result> results;
    private double[] coefficents;

    public ChartSeries(final String title, List<Result> results) {
        super(title);
        this.results = results;
        this.coefficents = getCoefficents();

        XYDataset dataset = getDataset(results);

        // createXYLineChart
        JFreeChart chart = ChartFactory.createScatterPlot(
                title,
                "Total Trainings",
                "Percentage",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);


        File imageFile = new File(title + ".png");
        int width = 1000;
        int height = 700;
        try {
            ChartUtilities.saveChartAsPNG(imageFile, chart, width, height);
        } catch (IOException e) {
            System.err.println(e);
        }

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1000, 700));
        setContentPane(chartPanel);
    }

    private double[] getCoefficents() {
        final WeightedObservedPoints obs = new WeightedObservedPoints();

        for(Result result : this.results) {
            obs.add(result.getTotal(), result.getPercent());
        }

        final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2);

        return fitter.fit(obs.toList());
    }

    private XYDataset getDataset(List<Result> results) {
        XYSeries series1 = new XYSeries("Values");
        for(Result result : results) {
            series1.add(result.getTotal(), result.getPercent());
        }

        double y = 0;
        XYSeries series2 = new XYSeries("Regression");
        for(Result result : results) {
            y = this.coefficents[0] + (this.coefficents[1]*result.getTotal()) + (this.coefficents[2]*Math.pow(result.getTotal(), 2));
            series2.add(result.getTotal(), y);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        return dataset;
    }

    private double calcYRegression(double[] consts, double x) {
        return consts[0] + (consts[1] * x);
    }

    private double[][] listToMatrix(List<Result> results) {
        double[][] returnValue = new double[results.size()][2];

        for(int i = 0; i < results.size(); i++) {
            returnValue[i][0] = results.get(i).getTotal();
            returnValue[i][1] = results.get(i).getPercent();
        }

        return returnValue;
    }

}
