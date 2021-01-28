package Analysis;

import java.util.ArrayList;
import java.util.List;

import com.binance.api.client.domain.market.Candlestick;

public class Triangle {
	ArrayList<Candlestick> candleList;
	Point headerPoint = null;
	int minPoints = 1;
	ArrayList<Point> savePoints;
	ArrayList<ArrayList<Candlestick>> highPoints;

	public Triangle(List<Candlestick> candleList) {
		this.candleList = candleList;
		this.headerPoint = new Point(null);
		savePoints = new ArrayList<Point>();
		highPoints = new ArrayList<ArrayList<Candlestick>>();
	}

	public Point calculateHighPoints() {
		for (int i = 0; i < candleList.size(); i++) {
			Candlestick candle = candleList.get(i);
			Point point = headerPoint;
			do {
				if (point.getNextPoint() == null) {
					point.setNextPoint(new Point(candle));
					break;
				} else if (point.getNextPoint().getCandle().getHighDouble() < candle.getHighDouble()) {
					if (point.getNextPoint().getChildrenCount() >= minPoints) {
						savePoints.add(point.getNextPoint());
					}
					point.setNextPoint(new Point(candle));
					break;
				}
				point = point.getNextPoint();
			} while (point != null);
		}
		savePoints.add(headerPoint.getNextPoint());
		return headerPoint;
	}

	public ArrayList<ArrayList<Candlestick>> calcFallingHighs() {
		highPoints.add(new ArrayList<Candlestick>());
		highPoints.get(0).add(candleList.get(0));
		int numberOfLines = 0;
		for (int i = 0; i < candleList.size(); i++) {
			candleList.get(i);

			for (int j = 0; j < highPoints.size(); j++) {
			}
		}
		return null;
	}

	public ArrayList<Point> getSavePoints() {
		return savePoints;
	}

	public void setSavePoints(ArrayList<Point> savePoints) {
		this.savePoints = savePoints;
	}

}
