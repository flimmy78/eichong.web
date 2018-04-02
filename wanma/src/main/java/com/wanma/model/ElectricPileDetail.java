package com.wanma.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * 电桩详情
 * 
 * @Description:
 * @author bruce cheng(http://blog.csdn.net/brucehome)
 * @createTime：2015-3-16 下午06:34:23
 * @updator：
 * @updateTime：
 * @version：V1.0
 */
public class ElectricPileDetail {
	private String pk_ElectricPile;
	private String electricPileImage;// 电桩图片
	private String electricPileUserName;// 电桩用途
	private String electricPileState;// 电桩状态
	private String electricPileNo;// 电桩编号
	private String electricPileChargingMode;// 充电类型
	private String electricPileParam;// 参数属性
	private String electricPileAdress;// 电桩地址
	private String electricPileTell;// 电桩电话
	private String electricPileRemark;//电桩备注
	private String electricPileCommentSum;// 电桩评论总数
	private String electricPileCommentUser;// 电桩评论用户名
	private String electricPileCommentStar;// 电桩评论星级
	private String electricPileCommentContent;// 电桩评论内容
	private String onlineTime; //在线时间
	private List<PowerElectricPileHead> pileHeadList;// 枪头详情

	private String isCollect;// 是否收藏 0未收藏 1已收藏

	private String electricPileName;// 电桩名称
	private String electricPowerInterface;//电桩接口方式
	private String electricPowerSize;//电桩额定功率
	private String electricMaxElectricity;//最大电流
	private String elpiLongitude; // 经度
	private String elpiLatitude; // 纬度
	private List<TblProductcomment> commentList;// 电桩评价列表
	private BigDecimal raIn_ReservationRate;//预约单价
	private BigDecimal raIn_ServiceCharge;//服务费
	private String raInReservationRate;
	private String raInServiceCharge;
	private int powerUser;//电桩用途
	private String distance;
	private int comm_status;//连接状态 0断开1连接
	private String totalChargeDl;//累计充电度量
	private String totalChargeTime;//累计充电时间，单位秒
	private String totalChargeAmt;//累计充电费用 
	private String faultAmount;//故障次数
	private Integer commStatus;//连接状态 0断开1连接
	
	
	public String getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(String onlineTime) {
		this.onlineTime = onlineTime;
	}

	public int getComm_status() {
		return comm_status;
	}

	public void setComm_status(int comm_status) {
		this.comm_status = comm_status;
	}

	public int getPowerUser() {
		return powerUser;
	}

	public void setPowerUser(int powerUser) {
		this.powerUser = powerUser;
	}

	public BigDecimal getRaIn_ServiceCharge() {
		return raIn_ServiceCharge;
	}

	public void setRaIn_ServiceCharge(BigDecimal raIn_ServiceCharge) {
		this.raIn_ServiceCharge = raIn_ServiceCharge;
	}

	public String getPk_ElectricPile() {
		return pk_ElectricPile;
	}

	public void setPk_ElectricPile(String pk_ElectricPile) {
		this.pk_ElectricPile = pk_ElectricPile;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public BigDecimal getRaIn_ReservationRate() {
		return raIn_ReservationRate;
	}

	public void setRaIn_ReservationRate(BigDecimal raIn_ReservationRate) {
		this.raIn_ReservationRate = raIn_ReservationRate;
	}

	public String getElectricPileRemark() {
		return electricPileRemark;
	}

	public void setElectricPileRemark(String electricPileRemark) {
		this.electricPileRemark = electricPileRemark;
	}

	public String getElectricPileImage() {
		return electricPileImage;
	}

	public void setElectricPileImage(String electricPileImage) {
		this.electricPileImage = electricPileImage;
	}

	public String getElectricPileState() {
		return electricPileState;
	}

	public void setElectricPileState(String electricPileState) {
		this.electricPileState = electricPileState;
	}

	public String getElectricPileNo() {
		return electricPileNo;
	}

	public void setElectricPileNo(String electricPileNo) {
		this.electricPileNo = electricPileNo;
	}

	public String getElectricPileChargingMode() {
		return electricPileChargingMode;
	}

	public void setElectricPileChargingMode(String electricPileChargingMode) {
		this.electricPileChargingMode = electricPileChargingMode;
	}

	public String getElectricPileParam() {
		return electricPileParam;
	}

	public void setElectricPileParam(String electricPileParam) {
		this.electricPileParam = electricPileParam;
	}

	public String getElectricPileAdress() {
		return electricPileAdress;
	}

	public void setElectricPileAdress(String electricPileAdress) {
		this.electricPileAdress = electricPileAdress;
	}

	public String getElectricPileTell() {
		return electricPileTell;
	}

	public void setElectricPileTell(String electricPileTell) {
		this.electricPileTell = electricPileTell;
	}

	public String getElectricPileCommentSum() {
		return electricPileCommentSum;
	}

	public void setElectricPileCommentSum(String electricPileCommentSum) {
		this.electricPileCommentSum = electricPileCommentSum;
	}

	public String getElectricPileCommentUser() {
		return electricPileCommentUser;
	}

	public void setElectricPileCommentUser(String electricPileCommentUser) {
		this.electricPileCommentUser = electricPileCommentUser;
	}

	public String getElectricPileCommentStar() {
		return electricPileCommentStar;
	}

	public void setElectricPileCommentStar(String electricPileCommentStar) {
		this.electricPileCommentStar = electricPileCommentStar;
	}

	public String getElectricPileCommentContent() {
		return electricPileCommentContent;
	}

	public void setElectricPileCommentContent(String electricPileCommentContent) {
		this.electricPileCommentContent = electricPileCommentContent;
	}

	public List<PowerElectricPileHead> getPileHeadList() {
		return pileHeadList;
	}

	public void setPileHeadList(List<PowerElectricPileHead> pileHeadList) {
		this.pileHeadList = pileHeadList;
	}

	public String getElectricPileUserName() {
		return electricPileUserName;
	}

	public void setElectricPileUserName(String electricPileUserName) {
		this.electricPileUserName = electricPileUserName;
	}

	public String getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}

	public List<TblProductcomment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<TblProductcomment> commentList) {
		this.commentList = commentList;
	}

	public String getElectricPowerInterface() {
		return electricPowerInterface;
	}

	public void setElectricPowerInterface(String electricPowerInterface) {
		this.electricPowerInterface = electricPowerInterface;
	}

	public String getElectricPowerSize() {
		return electricPowerSize;
	}

	public void setElectricPowerSize(String electricPowerSize) {
		this.electricPowerSize = electricPowerSize;
	}

	public String getElectricMaxElectricity() {
		return electricMaxElectricity;
	}

	public void setElectricMaxElectricity(String electricMaxElectricity) {
		this.electricMaxElectricity = electricMaxElectricity;
	}

	public String getElpiLongitude() {
		return elpiLongitude;
	}

	public void setElpiLongitude(String elpiLongitude) {
		this.elpiLongitude = elpiLongitude;
	}

	public String getElpiLatitude() {
		return elpiLatitude;
	}

	public void setElpiLatitude(String elpiLatitude) {
		this.elpiLatitude = elpiLatitude;
	}

	public String getElectricPileName() {
		return electricPileName;
	}

	public void setElectricPileName(String electricPileName) {
		this.electricPileName = electricPileName;
	}

	public String getRaInReservationRate() {
		return raInReservationRate;
	}

	public void setRaInReservationRate(String raInReservationRate) {
		this.raInReservationRate = raInReservationRate;
	}

	public String getRaInServiceCharge() {
		return raInServiceCharge;
	}

	public void setRaInServiceCharge(String raInServiceCharge) {
		this.raInServiceCharge = raInServiceCharge;
	}

	public String getTotalChargeDl() {
		return totalChargeDl;
	}

	public void setTotalChargeDl(String totalChargeDl) {
		this.totalChargeDl = totalChargeDl;
	}

	public String getTotalChargeTime() {
		return totalChargeTime;
	}

	public void setTotalChargeTime(String totalChargeTime) {
		this.totalChargeTime = totalChargeTime;
	}

	public String getTotalChargeAmt() {
		return totalChargeAmt;
	}

	public void setTotalChargeAmt(String totalChargeAmt) {
		this.totalChargeAmt = totalChargeAmt;
	}

	public String getFaultAmount() {
		return faultAmount;
	}

	public void setFaultAmount(String faultAmount) {
		this.faultAmount = faultAmount;
	}

	public Integer getCommStatus() {
		return commStatus;
	}

	public void setCommStatus(Integer commStatus) {
		this.commStatus = commStatus;
	}

	

}
