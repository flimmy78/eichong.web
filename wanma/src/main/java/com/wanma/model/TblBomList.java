package com.wanma.model;

import java.io.Serializable;

import com.bluemobi.product.model.common.BasicListAndMutiFile;

public class TblBomList extends BasicListAndMutiFile implements Serializable {
	private static final long serialVersionUID = 1L;
	private java.lang.Integer pkBomListId; // 主键
	private java.lang.Integer pkTypeSpanId; // 产品ID
	private java.lang.String blHardwareNumber; // 硬件编号
	private java.lang.String blHardwareVersion; // 硬件版本号
	private java.lang.String blFirmwareNumber; // 固件编号
	private java.lang.String blFirmwareVersion; // 固件版本号
	private java.lang.Integer blForceUpdate; // 强制更新标识
	private java.lang.String blFileMd5; // 版本文件的md5值
	private java.util.Date blCreatedate; // 创建时间
	private java.util.Date blUpdatedate;// 修改时间
	public java.lang.Integer getPkBomListId() {
		return pkBomListId;
	}
	public void setPkBomListId(java.lang.Integer pkBomListId) {
		this.pkBomListId = pkBomListId;
	}
	public java.lang.Integer getPkTypeSpanId() {
		return pkTypeSpanId;
	}
	public void setPkTypeSpanId(java.lang.Integer pkTypeSpanId) {
		this.pkTypeSpanId = pkTypeSpanId;
	}
	public java.lang.String getBlHardwareNumber() {
		return blHardwareNumber;
	}
	public void setBlHardwareNumber(java.lang.String blHardwareNumber) {
		this.blHardwareNumber = blHardwareNumber;
	}
	public java.lang.String getBlHardwareVersion() {
		return blHardwareVersion;
	}
	public void setBlHardwareVersion(java.lang.String blHardwareVersion) {
		this.blHardwareVersion = blHardwareVersion;
	}
	public java.lang.String getBlFirmwareNumber() {
		return blFirmwareNumber;
	}
	public void setBlFirmwareNumber(java.lang.String blFirmwareNumber) {
		this.blFirmwareNumber = blFirmwareNumber;
	}
	public java.lang.String getBlFirmwareVersion() {
		return blFirmwareVersion;
	}
	public void setBlFirmwareVersion(java.lang.String blFirmwareVersion) {
		this.blFirmwareVersion = blFirmwareVersion;
	}
	public java.lang.Integer getBlForceUpdate() {
		return blForceUpdate;
	}
	public void setBlForceUpdate(java.lang.Integer blForceUpdate) {
		this.blForceUpdate = blForceUpdate;
	}
	public java.lang.String getBlFileMd5() {
		return blFileMd5;
	}
	public void setBlFileMd5(java.lang.String blFileMd5) {
		this.blFileMd5 = blFileMd5;
	}
	public java.util.Date getBlCreatedate() {
		return blCreatedate;
	}
	public void setBlCreatedate(java.util.Date blCreatedate) {
		this.blCreatedate = blCreatedate;
	}
	public java.util.Date getBlUpdatedate() {
		return blUpdatedate;
	}
	public void setBlUpdatedate(java.util.Date blUpdatedate) {
		this.blUpdatedate = blUpdatedate;
	}

}
