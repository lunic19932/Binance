package Analysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jfree.data.time.Minute;

import com.binance.api.client.domain.market.Candlestick;

import io.PlotPoints;

public class Analyser {
	List<Candlestick> candleList;

	public Analyser(List<Candlestick> candleList) {
		this.candleList = candleList;
	}

	public List<Double> getHorizontalResistance() {
		List<Double> resistanceList = new ArrayList<Double>();
		resistanceList.add(getHigh());
		resistanceList.addAll(getHorizontalTouchpoints(0.5, 10));

		return resistanceList;

	}

	public double getHigh() {
		double high = Double.parseDouble(candleList.get(0).getHigh());
		for (int i = 0; i < candleList.size(); i++) {
			double tmp = Double.parseDouble(candleList.get(i).getHigh());
			if (tmp > high) {
				high = tmp;
			}

		}
		return high;
	}

	public List<Double> getHorizontalTouchpoints(double deviation, int minTouchpoints) {
		List<Double> resistanceList = new LinkedList<Double>();
		double[] allPrices = new double[candleList.size() * 2];
		double[] allVolumes = new double[candleList.size() * 2];
		{
			int j = 0;
			for (int i = 0; i < candleList.size(); i++) {
				allPrices[j++] = Double.parseDouble(candleList.get(i).getHigh());
				allPrices[j++] = Double.parseDouble(candleList.get(i).getLow());
			}
		}
		List<List<Double>> resistanceAreaList = new LinkedList<List<Double>>();

		for (int i = 0; i < allPrices.length; i++) {
			boolean exists = false;
			for (int j = 0; j < resistanceAreaList.size(); j++) {
				if (resistanceAreaList.get(j) != null
						&& allPrices[i] < resistanceAreaList.get(j).get(0) * ((100 + deviation) / 100)
						&& allPrices[i] > resistanceAreaList.get(j).get(0) * ((100 - deviation) / 100)) {
					exists = true;
					resistanceAreaList.get(j).add(allPrices[i]);
					break;
				}
			}

			if (!exists) {
				LinkedList<Double> newResistanceArea = new LinkedList<Double>();
				newResistanceArea.add(allPrices[i]);
				resistanceAreaList.add(newResistanceArea);
			}
		}

		for (int i = 0; i < resistanceAreaList.size(); i++) {
			if (resistanceAreaList.get(i).size() >= minTouchpoints) {
				double sum = 0;
				for (int j = 0; j < resistanceAreaList.get(i).size(); j++) {
					sum += resistanceAreaList.get(i).get(j);
				}

				resistanceList.add(sum / resistanceAreaList.get(i).size());
			}
		}
		return resistanceList;
	}

	public void getDiagonalTouchpoints() {

	}

	public PlotPoints getDifferenzGraph(int filterRange) {
		ArrayList<Double> diffList = new ArrayList<Double>();
		PlotPoints points = new PlotPoints();
		for (int i = 0; i < candleList.size(); i++) {
			int startValue = ((i - filterRange < 0) ? 0 : i - filterRange);
			int endValue = i;// ((i + filterRange < candleList.size()) ? i + filterRange :
								// candleList.size()// - 1);
			double low = candleList.get(startValue).getLowDouble();
			double high = candleList.get(startValue).getHighDouble();
			double median = high;
			median += low;
			for (int k = startValue + 1; k <= endValue; k++) {
				Candlestick candle = candleList.get(k);
				if (low > candle.getLowDouble()) {
					low = candle.getLowDouble();
				}
				if (high < candle.getHighDouble()) {
					high = candle.getHighDouble();
				}
				median += high;
				median += low;
			}
			median /= 2 * filterRange;
			double diff = ((high - low) / median);
			if (i > filterRange / 3) {
				points.addPoint(new Minute(new Date(candleList.get(i).getOpenTime())), diff);

			}
		}
		return points;
	}

}
