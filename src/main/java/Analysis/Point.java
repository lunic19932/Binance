package Analysis;

import com.binance.api.client.domain.market.Candlestick;

public class Point {

	private Point prevPoint;
	private Point nextPoint;
	private Candlestick candle;
	private int childrenCount;

	public Point(Candlestick candleStick) {
		this.candle = candleStick;
		childrenCount = 0;
	}

	public Point getPrevPoint() {
		return prevPoint;
	}

	public void setPrevPoint(Point prevPoint) {
		this.prevPoint = prevPoint;

	}

	public Point getNextPoint() {
		return nextPoint;
	}

	public void setNextPoint(Point nextPoint) {
		this.nextPoint = nextPoint;
		while() {
			
		}
		childrenCount = nextPoint.getChildrenCount() + 1;
		System.out.println(getChildrenCount());
	}

	public int getChildrenCount() {
		return childrenCount;
	}

	public void setChildrenCount(int childrenCount) {
		this.childrenCount = childrenCount;
	}

	public Candlestick getCandle() {
		return candle;
	}

	public void setCandle(Candlestick candle) {
		this.candle = candle;
	}

}
