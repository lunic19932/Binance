/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Marc de Verdelhan, 2017-2019 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io;

import java.awt.Color;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;

import Analysis.Analyser;

/**
 * This class builds a traditional candlestick chart.
 */
public class CandlestickChart {

	/**
	 * Builds a JFreeChart OHLC dataset from a ta4j bar series.
	 *
	 * @param series the bar series
	 * @return an Open-High-Low-Close dataset
	 */

	XYPlot plot;

	public CandlestickChart() {

	}

	public void createCandlestickChart(List<Candlestick> candlestickList) {
		OHLCDataset ohlcDataset = createOHLCDataset(candlestickList);
		JFreeChart chart = ChartFactory.createCandlestickChart("Binance BTC price", "Time", "USD", ohlcDataset, true);
		// Candlestick rendering
		CandlestickRenderer renderer = new MyCandlestickRenderer();
		renderer.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_SMALLEST);
		plot = chart.getXYPlot();
		plot.setRenderer(renderer);
		chart.removeLegend();
		plot.setRangeGridlinePaint(Color.lightGray);
		plot.setBackgroundPaint(Color.white);
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		numberAxis.setAutoRangeIncludesZero(false);
		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		displayChart(chart);
	}

	public OHLCDataset createOHLCDataset(List<Candlestick> candlestickList) {

		final int nbBars = candlestickList.size();

		Date[] dates = new Date[nbBars];
		double[] opens = new double[nbBars];
		double[] highs = new double[nbBars];
		double[] lows = new double[nbBars];
		double[] closes = new double[nbBars];
		double[] volumes = new double[nbBars];

		for (int i = 0; i < nbBars; i++) {
			Candlestick cStick = candlestickList.get(i);
			dates[i] = new Date(cStick.getOpenTime());
			opens[i] = Double.parseDouble(cStick.getOpen());
			highs[i] = Double.parseDouble(cStick.getHigh());
			lows[i] = Double.parseDouble(cStick.getLow());
			closes[i] = Double.parseDouble(cStick.getClose());
			volumes[i] = Double.parseDouble(cStick.getVolume());

		}

		return new DefaultHighLowDataset("btc", dates, highs, lows, opens, closes, volumes);
	}

	/**
	 * Builds an additional JFreeChart dataset from a ta4j bar series.
	 *
	 * @param series the bar series
	 * @return an additional dataset
	 */
	public TimeSeriesCollection createHorizontalLine(List<Candlestick> candlestickList, double x) {
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		org.jfree.data.time.TimeSeries chartTimeSeries = new org.jfree.data.time.TimeSeries("Btc price");
		for (int i = 0; i < candlestickList.size(); i++) {
			Candlestick cStick = candlestickList.get(i);
			chartTimeSeries.add(new Minute(new Date(cStick.getOpenTime())), x);
		}
		dataset.addSeries(chartTimeSeries);
		int index = plot.getDatasetCount();
		plot.setDataset(index, dataset);
		plot.mapDatasetToRangeAxis(index, 0);
		XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
		renderer2.setSeriesPaint(index, Color.blue);
		plot.setRenderer(index, renderer2);
		return dataset;
	}

	public void createLineFromPoints(PlotPoints points) {
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		org.jfree.data.time.TimeSeries chartTimeSeries = new org.jfree.data.time.TimeSeries("Btc price");
		for (int i = 0; i < points.pointList.size(); i++) {
			chartTimeSeries.add(points.getTimePeriodList().get(i), points.getPointList().get(i));
			System.out.println(points.getPointList().get(i));
		}

		dataset.addSeries(chartTimeSeries);
		int index = plot.getDatasetCount();
		plot.setDataset(index, dataset);
		plot.mapDatasetToRangeAxis(index, 0);
		XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
		renderer2.setSeriesPaint(index, Color.cyan);

		plot.setRenderer(index, renderer2);
		try {
			Thread.sleep(80);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Displays a chart in a frame.
	 *
	 * @param chart the chart to be displayed
	 */
	private void displayChart(JFreeChart chart) {
		// Chart panel
		ChartPanel panel = new ChartPanel(chart);
		panel.setFillZoomRectangle(true);
		panel.setMouseWheelEnabled(true);
		panel.setPreferredSize(new java.awt.Dimension(740, 300));
		// Application frame
		ApplicationFrame frame = new ApplicationFrame("Ta4j example - Candlestick chart");
		frame.setContentPane(panel);
		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setVisible(true);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		/*
		 * Getting bar series
		 */
		BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
		BinanceApiRestClient client = factory.newRestClient();
		List<Candlestick> candlestickList = client.getCandlestickBars("BTCUSDT".toUpperCase(),
				CandlestickInterval.FOUR_HOURLY);
		candlestickList = candlestickList.subList(0, 499);
		CandlestickChart charts = new CandlestickChart();
		charts.createCandlestickChart(candlestickList);

		/*
		 * Displaying the chart
		 */

		Analyser anl = new Analyser(candlestickList);
		List<Double> res = anl.getHorizontalResistance();
		double drawRadius = 20;
//		for (int i = 0; i < res.size(); i++) {
//			double low = Double.parseDouble(candlestickList.get(candlestickList.size() - 1).getLow())
//					* (100 - drawRadius) / 100;
//			double high = Double.parseDouble(candlestickList.get(candlestickList.size() - 1).getHigh())
//					* (100 + drawRadius) / 100;
//			if (res.get(i) > low && res.get(i) < high) {
//				Thread.sleep(180);
//				charts.createHorizontalLine(candlestickList, res.get(i));
//
//			}
//
//		}
		charts.createLineFromPoints(anl.getDifferenzGraph(20));

	}

}
