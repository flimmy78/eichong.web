package com.wanma.model;

import java.io.Serializable;
import java.util.Date;

import com.bluemobi.product.model.common.BasicListAndMutiFile;

/**
 * 首页消息管理
 */
public class TblMessageInfo extends BasicListAndMutiFile implements
		Serializable {
		private java.lang.Integer pkMessageInfoId;//主键
		private java.lang.String messageInfoContent;//首页消息内容
		private java.lang.String messageInfoName;//首页消息标题
		private java.lang.Integer messageInfoStatus;//状态默认0: 1 有效 2无效 3 删除    
		private java.lang.Integer messageInfoType;//状态默认0: 1故障 2 新建
		private java.util.Date messageInfoBegintime;//开始时间
		private java.util.Date messageInfoEndtime;//结束时间
		private java.util.Date messageInfoCreatedate;//创建时间
		private java.util.Date messageInfoUpdatedate;//更新时间
		private java.lang.String messageInfoProvinceCode;//所属省份代码
		private java.lang.String messageInfoCityCode;//所属城市代码
		private java.lang.String messageInfoCountyCode;//所属区县代码
		private java.lang.Integer pkPowerstation;//首页消息关系的电站Id
		private java.lang.String mprName;//首页消息关系的电站名称
		//非数据库字段
		private java.lang.String messageInfoRegion;// 地区
		private java.lang.String messageInfoPageStatus;// 页面展示的状态
		private java.lang.String messageInfoBegintimeStartTime;
		private java.lang.String messageInfoBegintimeEndTime;
		private java.lang.String messageInfoEndtimeStartTime;
		private java.lang.String messageInfoEndtimeEndTime;
		private java.lang.String messageInfoUserId;
		public java.lang.Integer getPkMessageInfoId() {
			return pkMessageInfoId;
		}
		public void setPkMessageInfoId(java.lang.Integer pkMessageInfoId) {
			this.pkMessageInfoId = pkMessageInfoId;
		}
		public java.lang.String getMessageInfoContent() {
			return messageInfoContent;
		}
		public void setMessageInfoContent(java.lang.String messageInfoContent) {
			this.messageInfoContent = messageInfoContent;
		}
		public java.lang.String getMessageInfoName() {
			return messageInfoName;
		}
		public void setMessageInfoName(java.lang.String messageInfoName) {
			this.messageInfoName = messageInfoName;
		}
		public java.lang.Integer getMessageInfoStatus() {
			return messageInfoStatus;
		}
		public void setMessageInfoStatus(java.lang.Integer messageInfoStatus) {
			this.messageInfoStatus = messageInfoStatus;
		}
		public java.lang.Integer getMessageInfoType() {
			return messageInfoType;
		}
		public void setMessageInfoType(java.lang.Integer messageInfoType) {
			this.messageInfoType = messageInfoType;
		}
		public java.util.Date getMessageInfoBegintime() {
			return messageInfoBegintime;
		}
		public void setMessageInfoBegintime(java.util.Date messageInfoBegintime) {
			this.messageInfoBegintime = messageInfoBegintime;
		}
		public java.util.Date getMessageInfoEndtime() {
			return messageInfoEndtime;
		}
		public void setMessageInfoEndtime(java.util.Date messageInfoEndtime) {
			this.messageInfoEndtime = messageInfoEndtime;
		}
		public java.util.Date getMessageInfoCreatedate() {
			return messageInfoCreatedate;
		}
		public void setMessageInfoCreatedate(java.util.Date messageInfoCreatedate) {
			this.messageInfoCreatedate = messageInfoCreatedate;
		}
		public java.util.Date getMessageInfoUpdatedate() {
			return messageInfoUpdatedate;
		}
		public void setMessageInfoUpdatedate(java.util.Date messageInfoUpdatedate) {
			this.messageInfoUpdatedate = messageInfoUpdatedate;
		}
		public java.lang.String getMessageInfoProvinceCode() {
			return messageInfoProvinceCode;
		}
		public void setMessageInfoProvinceCode(java.lang.String messageInfoProvinceCode) {
			this.messageInfoProvinceCode = messageInfoProvinceCode;
		}
		public java.lang.String getMessageInfoCityCode() {
			return messageInfoCityCode;
		}
		public void setMessageInfoCityCode(java.lang.String messageInfoCityCode) {
			this.messageInfoCityCode = messageInfoCityCode;
		}
		public java.lang.String getMessageInfoCountyCode() {
			return messageInfoCountyCode;
		}
		public void setMessageInfoCountyCode(java.lang.String messageInfoCountyCode) {
			this.messageInfoCountyCode = messageInfoCountyCode;
		}
		public java.lang.String getMessageInfoRegion() {
			return messageInfoRegion;
		}
		public void setMessageInfoRegion(java.lang.String messageInfoRegion) {
			this.messageInfoRegion = messageInfoRegion;
		}
		public java.lang.String getMessageInfoPageStatus() {
			return messageInfoPageStatus;
		}
		public void setMessageInfoPageStatus(java.lang.String messageInfoPageStatus) {
			this.messageInfoPageStatus = messageInfoPageStatus;
		}
		public java.lang.String getMessageInfoBegintimeStartTime() {
			return messageInfoBegintimeStartTime;
		}
		public void setMessageInfoBegintimeStartTime(
				java.lang.String messageInfoBegintimeStartTime) {
			this.messageInfoBegintimeStartTime = messageInfoBegintimeStartTime;
		}
		public java.lang.String getMessageInfoBegintimeEndTime() {
			return messageInfoBegintimeEndTime;
		}
		public void setMessageInfoBegintimeEndTime(
				java.lang.String messageInfoBegintimeEndTime) {
			this.messageInfoBegintimeEndTime = messageInfoBegintimeEndTime;
		}
		public java.lang.String getMessageInfoEndtimeStartTime() {
			return messageInfoEndtimeStartTime;
		}
		public void setMessageInfoEndtimeStartTime(
				java.lang.String messageInfoEndtimeStartTime) {
			this.messageInfoEndtimeStartTime = messageInfoEndtimeStartTime;
		}
		public java.lang.String getMessageInfoEndtimeEndTime() {
			return messageInfoEndtimeEndTime;
		}
		public void setMessageInfoEndtimeEndTime(
				java.lang.String messageInfoEndtimeEndTime) {
			this.messageInfoEndtimeEndTime = messageInfoEndtimeEndTime;
		}
		public java.lang.String getMessageInfoUserId() {
			return messageInfoUserId;
		}
		public void setMessageInfoUserId(java.lang.String messageInfoUserId) {
			this.messageInfoUserId = messageInfoUserId;
		}
		public java.lang.Integer getPkPowerstation() {
			return pkPowerstation;
		}
		public void setPkPowerstation(java.lang.Integer pkPowerstation) {
			this.pkPowerstation = pkPowerstation;
		}
		public java.lang.String getMprName() {
			return mprName;
		}
		public void setMprName(java.lang.String mprName) {
			this.mprName = mprName;
		}
		@Override
		public String toString() {
			return "TblMessageInfo [pkMessageInfoId=" + pkMessageInfoId
					+ ", messageInfoContent=" + messageInfoContent
					+ ", messageInfoName=" + messageInfoName
					+ ", messageInfoStatus=" + messageInfoStatus
					+ ", messageInfoType=" + messageInfoType
					+ ", messageInfoBegintime=" + messageInfoBegintime
					+ ", messageInfoEndtime=" + messageInfoEndtime
					+ ", messageInfoCreatedate=" + messageInfoCreatedate
					+ ", messageInfoUpdatedate=" + messageInfoUpdatedate
					+ ", messageInfoProvinceCode=" + messageInfoProvinceCode
					+ ", messageInfoCityCode=" + messageInfoCityCode
					+ ", messageInfoCountyCode=" + messageInfoCountyCode
					+ ", pkPowerstation=" + pkPowerstation + ", mprName="
					+ mprName + ", messageInfoRegion=" + messageInfoRegion
					+ ", messageInfoPageStatus=" + messageInfoPageStatus
					+ ", messageInfoBegintimeStartTime="
					+ messageInfoBegintimeStartTime
					+ ", messageInfoBegintimeEndTime="
					+ messageInfoBegintimeEndTime
					+ ", messageInfoEndtimeStartTime="
					+ messageInfoEndtimeStartTime
					+ ", messageInfoEndtimeEndTime="
					+ messageInfoEndtimeEndTime + ", messageInfoUserId="
					+ messageInfoUserId + "]";
		}
		
		
		
}
