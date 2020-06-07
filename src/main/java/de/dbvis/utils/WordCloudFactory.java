package de.dbvis.utils;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.FontWeight;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.image.AngleGenerator;
import com.kennycason.kumo.palette.ColorPalette;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Wolfgang Jentner.
 */
public class WordCloudFactory {

    /**
     * A convinience wrapper for the word counts.
     * @param frequencies the word frequencies as a Map
     * @param output the output file as png: e.g. "example.png"
     */
    public static void create(Map<String, Integer> frequencies, String output) {
        List<WordFrequency> list = frequencies.entrySet().stream().map(e -> new WordFrequency(e.getKey(), e.getValue())).collect(Collectors.toList());
        WordCloudFactory.create(list, output);
    }

    /**
     * This wrapper will create a circular word cloud.
     * @param frequencies the word frequencies
     * @param output the output file as png: e.g. "example.png"
     */
    public static void create(List<WordFrequency> frequencies, String output) {
        final Dimension dimension = new Dimension(600, 600);
        final com.kennycason.kumo.WordCloud wordCloud = new com.kennycason.kumo.WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setAngleGenerator(new AngleGenerator(0));
        wordCloud.setKumoFont(new KumoFont("LICENSE PLATE", FontWeight.BOLD));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
        wordCloud.build(frequencies);
        wordCloud.writeToFile("output/"+output);
    }
}
