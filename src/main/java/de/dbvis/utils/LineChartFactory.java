package de.dbvis.utils;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Wolfgang Jentner on 11/3/2017.
 */
public class LineChartFactory {

    public static void createZipfsLaw(String title, double[] frequencies) throws IOException {
        double[] xValues = ZipfsLaw.getXValues(frequencies.length);
        double[] zipfYValues = ZipfsLaw.getValues(frequencies.length);

        XYChart chart = QuickChart.getChart(title, "Rank", "Frequency", "Zipf's Law", xValues, zipfYValues);
        chart.getStyler().setXAxisLogarithmic(true);
        chart.getStyler().setYAxisLogarithmic(true);

        XYSeries series = chart.addSeries(title, xValues, frequencies);
        series.setMarker(SeriesMarkers.NONE);

        // Show it
        //new SwingWrapper<>(chart).displayChart();

        // Save it
        BitmapEncoder.saveBitmap(chart, "./output/"+title, BitmapEncoder.BitmapFormat.PNG);
    }

    public static void createTaggerPerformance(final List<Double> xValues, final TreeMap<String, List<Double>> ySeries) throws IOException {
        createLineChart("Precision Chart", "Corpus Size", "Precision", xValues, ySeries);
    }

    public static void createTaggerRuntime(final List<Double> xValues, final TreeMap<String, List<Double>> ySeries) throws IOException {
        createLineChart("Runtime Chart", "Corpus Size", "Runtime (ms)", xValues, ySeries);
    }

    private static void createLineChart(final String title, final String xAxis, final String yAxis, final List<Double> xValues, final TreeMap<String, List<Double>> ySeries) throws IOException {
        Map.Entry<String, List<Double>> pair = ySeries.pollFirstEntry();

        final XYChart chart = QuickChart.getChart(title, xAxis, yAxis, pair.getKey(), xValues, pair.getValue());

        ySeries.forEach((key, value) -> chart.addSeries(key, xValues, value).setMarker(SeriesMarkers.NONE));

//        SwingWrapper<XYChart> wrapper = new SwingWrapper<>(chart);
//        wrapper.displayChart();

        BitmapEncoder.saveBitmap(chart, "./output/"+ title, BitmapEncoder.BitmapFormat.PNG);
    }

    public static void createSentimentLineChart(final String chartTitle, final List<Double> sentimentOwn, final List<Double> sentimentStanford, final List<Double> sentimentOpenNlp) throws IOException {
        if(sentimentOwn.size() != sentimentStanford.size() || sentimentOwn.size() != sentimentOpenNlp.size() || sentimentStanford.size() != sentimentOpenNlp.size()) {
            throw new IllegalArgumentException("All three sentiment lists should be of the same size");
        }

        List<Double> xValues = IntStream.range(0, sentimentOwn.size()).asDoubleStream().boxed().collect(Collectors.toList());
        TreeMap<String, List<Double>> ySeries = new TreeMap<>();
        ySeries.put("Own", sentimentOwn);
        ySeries.put("Stanford", sentimentStanford);
        ySeries.put("OpenNLP", sentimentOpenNlp);

        createLineChart(chartTitle, "Utterance Nr", "Avg Sentiment", xValues, ySeries);
    }
}
