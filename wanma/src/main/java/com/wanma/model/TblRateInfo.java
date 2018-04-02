package com.wanma.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.bluemobi.product.model.common.BasicListAndMutiFile;

public class TblRateInfo extends BasicListAndMutiFile  implements Serializable {

	private static final long serialVersionUID = 3055687264484769401L;
	private int pkId; //历史表主键
	private int pk_RateInformation;//费率表主键
	private String rateInformation;//费率表主键
	private Date raIn_EffectiveDates;//费率表主键
	private Date raIn_ExpiryDate;//费率表主键
	private BigDecimal raIn_FreezingMoney; //预冻结金额
	private BigDecimal raIn_MinFreezingMoney; //最小冻结金额
	private String raIn_QuantumDate; //json格式的费率
	private BigDecimal raIn_ReservationRate; //预约单价
	private BigDecimal raIn_ServiceCharge; //服务费
	private BigDecimal raIn_WarnMoney; //告警余额
	private String userId; //关联用户id，p_m_user表的id
	private String updateUserId; //修改人
	private String raIn_ProvinceId;
	private String raIn_CityId;
	private String raIn_AreaId; 
	private BigDecimal raIn_TipTimeTariff;
	private BigDecimal raIn_PeakElectricityPrice;
	private BigDecimal raIn_UsualPrice;
	private BigDecimal raIn_ValleyTimePrice;
	private String raInRemarks;
	private String raInType; //1:费率两位，2：费率四位
	//	以下字段不予数据库对应 
	private String parentLoveLoginId;
	private int user_level; //用户级别
	
	
	
	
	public String getRateInformation() {
		return rateInformation;
	}
	public void setRateInformation(String rateInformation) {
		this.rateInformation = rateInformation;
	}
	public String getRaInType() {
		return raInType;
	}
	public void setRaInType(String raInType) {
		this.raInType = raInType;
	}
	public int getUser_level() {
		return user_level;
	}
	public void setUser_level(int user_level) {
		this.user_level = user_level;
	}
	
	public int getPkId() {
		return pkId;
	}
	public void setPkId(int pkId) {
		this.pkId = pkId;
	}
	public int getPk_RateInformation() {
		return pk_RateInformation;
	}
	public void setPk_RateInformation(int pk_RateInformation) {
		this.pk_RateInformation = pk_RateInformation;
	}
	public Date getRaIn_EffectiveDates() {
		return raIn_EffectiveDates;
	}
	public void setRaIn_EffectiveDates(Date raIn_EffectiveDates) {
		this.raIn_EffectiveDates = raIn_EffectiveDates;
	}
	public Date getRaIn_ExpiryDate() {
		return raIn_ExpiryDate;
	}
	public void setRaIn_ExpiryDate(Date raIn_ExpiryDate) {
		this.raIn_ExpiryDate = raIn_ExpiryDate;
	}
	public BigDecimal getRaIn_FreezingMoney() {
		return raIn_FreezingMoney;
	}
	public void setRaIn_FreezingMoney(BigDecimal raIn_FreezingMoney) {
		this.raIn_FreezingMoney = raIn_FreezingMoney;
	}
	public BigDecimal getRaIn_MinFreezingMoney() {
		return raIn_MinFreezingMoney;
	}
	public void setRaIn_MinFreezingMoney(BigDecimal raIn_MinFreezingMoney) {
		this.raIn_MinFreezingMoney = raIn_MinFreezingMoney;
	}
	public String getRaIn_QuantumDate() {
		return raIn_QuantumDate;
	}
	public void setRaIn_QuantumDate(String raIn_QuantumDate) {
		this.raIn_QuantumDate = raIn_QuantumDate;
	}
	public BigDecimal getRaIn_ReservationRate() {
		return raIn_ReservationRate;
	}
	public void setRaIn_ReservationRate(BigDecimal raIn_ReservationRate) {
		this.raIn_ReservationRate = raIn_ReservationRate;
	}
	public BigDecimal getRaIn_ServiceCharge() {
		return raIn_ServiceCharge;
	}
	public void setRaIn_ServiceCharge(BigDecimal raIn_ServiceCharge) {
		this.raIn_ServiceCharge = raIn_ServiceCharge;
	}
	public BigDecimal getRaIn_WarnMoney() {
		return raIn_WarnMoney;
	}
	public void setRaIn_WarnMoney(BigDecimal raIn_WarnMoney) {
		this.raIn_WarnMoney = raIn_WarnMoney;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getRaIn_AreaId() {
		return raIn_AreaId;
	}
	public void setRaIn_AreaId(String raIn_AreaId) {
		this.raIn_AreaId = raIn_AreaId;
	}
	public String getParentLoveLoginId() {
		return parentLoveLoginId;
	}
	public void setParentLoveLoginId(String parentLoveLoginId) {
		this.parentLoveLoginId = parentLoveLoginId;
	}
	
	

	public String getRaIn_ProvinceId() {
		return raIn_ProvinceId;
	}
	public void setRaIn_ProvinceId(String raIn_ProvinceId) {
		this.raIn_ProvinceId = raIn_ProvinceId;
	}
	public String getRaIn_CityId() {
		return raIn_CityId;
	}
	public void setRaIn_CityId(String raIn_CityId) {
		this.raIn_CityId = raIn_CityId;
	}
	
	public BigDecimal getRaIn_TipTimeTariff() {
		return raIn_TipTimeTariff;
	}
	public void setRaIn_TipTimeTariff(BigDecimal raIn_TipTimeTariff) {
		this.raIn_TipTimeTariff = raIn_TipTimeTariff;
	}
	public BigDecimal getRaIn_PeakElectricityPrice() {
		return raIn_PeakElectricityPrice;
	}
	public void setRaIn_PeakElectricityPrice(BigDecimal raIn_PeakElectricityPrice) {
		this.raIn_PeakElectricityPrice = raIn_PeakElectricityPrice;
	}
	public BigDecimal getRaIn_UsualPrice() {
		return raIn_UsualPrice;
	}
	public void setRaIn_UsualPrice(BigDecimal raIn_UsualPrice) {
		this.raIn_UsualPrice = raIn_UsualPrice;
	}
	public BigDecimal getRaIn_ValleyTimePrice() {
		return raIn_ValleyTimePrice;
	}
	public void setRaIn_ValleyTimePrice(BigDecimal raIn_ValleyTimePrice) {
		this.raIn_ValleyTimePrice = raIn_ValleyTimePrice;
	}
	
	public String getRaInRemarks() {
		return raInRemarks;
	}
	public void setRaInRemarks(String raInRemarks) {
		this.raInRemarks = raInRemarks;
	}
	@Override
	public String toString() {
		return "TblRateInfo [pkId=" + pkId + ", pk_RateInformation="
				+ pk_RateInformation + ", rateInformation=" + rateInformation
				+ ", raIn_EffectiveDates=" + raIn_EffectiveDates
				+ ", raIn_ExpiryDate=" + raIn_ExpiryDate
				+ ", raIn_FreezingMoney=" + raIn_FreezingMoney
				+ ", raIn_MinFreezingMoney=" + raIn_MinFreezingMoney
				+ ", raIn_QuantumDate=" + raIn_QuantumDate
				+ ", raIn_ReservationRate=" + raIn_ReservationRate
				+ ", raIn_ServiceCharge=" + raIn_ServiceCharge
				+ ", raIn_WarnMoney=" + raIn_WarnMoney + ", userId=" + userId
				+ ", updateUserId=" + updateUserId + ", raIn_ProvinceId="
				+ raIn_ProvinceId + ", raIn_CityId=" + raIn_CityId
				+ ", raIn_AreaId=" + raIn_AreaId + ", raIn_TipTimeTariff="
				+ raIn_TipTimeTariff + ", raIn_PeakElectricityPrice="
				+ raIn_PeakElectricityPrice + ", raIn_UsualPrice="
				+ raIn_UsualPrice + ", raIn_ValleyTimePrice="
				+ raIn_ValleyTimePrice + ", raInRemarks=" + raInRemarks
				+ ", raInType=" + raInType + ", parentLoveLoginId="
				+ parentLoveLoginId + ", user_level=" + user_level + "]";
	}
	
	
}