package com.bluemobi.product.model.echarts;

import java.io.Serializable;

public class PeopleCountEchartsModel implements Serializable,EchartsParamModel {
	/**
	 * 用户注册图表model
	 */
	private static final long serialVersionUID = 1L;
	private String month;
	private String zcfrom;
	private double count;
	private String begin_date;
	private String end_date;
	private String type;
	private String onlyData;
	private String firstLoadFlag;
	private String userId;
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	public String getZcfrom() {
		return zcfrom;
	}
	public void setZcfrom(String zcfrom) {
		this.zcfrom = zcfrom;
	}
	public String getBegin_date() {
		return begin_date;
	}
	public void setBegin_date(String begin_date) {
		this.begin_date = begin_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOnlyData() {
		return onlyData;
	}
	public void setOnlyData(String onlyData) {
		this.onlyData = onlyData;
	}
	public String getFirstLoadFlag() {
		return firstLoadFlag;
	}
	public void setFirstLoadFlag(String firstLoadFlag) {
		this.firstLoadFlag = firstLoadFlag;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserId() {
		return userId;
	}
	@Override
	public String getUserIdForShow() {
		// TODO Auto-generated method stub
		return null;
	}
}
