package io;

import java.util.ArrayList;

import org.jfree.data.time.RegularTimePeriod;

public class PlotPoints {
	ArrayList<RegularTimePeriod> timePeriodList;
	ArrayList<Double> pointList;

	public PlotPoints() {
		pointList = new ArrayList<Double>();
		timePeriodList = new ArrayList<RegularTimePeriod>();
	}

	public void addPoint(RegularTimePeriod tPeriod, Double point) {
		pointList.add(point);
		timePeriodList.add(tPeriod);
	}

	public ArrayList<RegularTimePeriod> getTimePeriodList() {
		return timePeriodList;
	}

	public void setTimePeriodList(ArrayList<RegularTimePeriod> timePeriodList) {
		this.timePeriodList = timePeriodList;
	}

	public ArrayList<Double> getPointList() {
		return pointList;
	}

	public void setPointList(ArrayList<Double> pointList) {
		this.pointList = pointList;
	}

}
