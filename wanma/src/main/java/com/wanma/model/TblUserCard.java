package com.wanma.model;

import java.util.Date;

import com.bluemobi.product.model.common.BasicListAndMutiFile;

public class TblUserCard extends BasicListAndMutiFile {
	private static final long serialVersionUID = -2344081692000738049L;
	private Long pkUserCard;// 主键
	private String ucInternalCardNumber;// 内卡号
	private String ucExternalCardNumber;// 外卡号
	private Double ucBalance;// 金额
	private Integer ucCompanyNumber;// 公司标识
	private String ucUserId;// 用户ID
	private Integer ucStatus;// 状态 0：正常，1：挂失
	private Integer ucPayMode; //卡类型
	private Date ucUpdateDate;//卡绑定时间
	
	//表单填充
	private String userAccount;
	private String normRealName;
	private Long pkCardapplicationform;
	private String cpyCompanyname;
	private Integer bindFlag;//绑定标识位  1绑定，2未绑定
	
	private String authCode;
	private String actionUrl;
	
	public Long getPkUserCard() {
		return pkUserCard;
	}

	public void setPkUserCard(Long pkUserCard) {
		this.pkUserCard = pkUserCard;
	}

	public String getUcInternalCardNumber() {
		return ucInternalCardNumber;
	}

	public void setUcInternalCardNumber(String ucInternalCardNumber) {
		this.ucInternalCardNumber = ucInternalCardNumber;
	}

	public String getUcExternalCardNumber() {
		return ucExternalCardNumber;
	}

	public void setUcExternalCardNumber(String ucExternalCardNumber) {
		this.ucExternalCardNumber = ucExternalCardNumber;
	}

	public Double getUcBalance() {
		return ucBalance;
	}

	public void setUcBalance(Double ucBalance) {
		this.ucBalance = ucBalance;
	}

	public Integer getUcCompanyNumber() {
		return ucCompanyNumber;
	}

	public void setUcCompanyNumber(Integer ucCompanyNumber) {
		this.ucCompanyNumber = ucCompanyNumber;
	}

	public String getUcUserId() {
		return ucUserId;
	}

	public void setUcUserId(String ucUserId) {
		this.ucUserId = ucUserId;
	}

	public Integer getUcStatus() {
		return ucStatus;
	}

	public void setUcStatus(Integer ucStatus) {
		this.ucStatus = ucStatus;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public Long getPkCardapplicationform() {
		return pkCardapplicationform;
	}

	public void setPkCardapplicationform(Long pkCardapplicationform) {
		this.pkCardapplicationform = pkCardapplicationform;
	}

	public String getNormRealName() {
		return normRealName;
	}

	public void setNormRealName(String normRealName) {
		this.normRealName = normRealName;
	}

	public String getCpyCompanyname() {
		return cpyCompanyname;
	}

	public void setCpyCompanyname(String cpyCompanyname) {
		this.cpyCompanyname = cpyCompanyname;
	}

	public Integer getBindFlag() {
		return bindFlag;
	}

	public void setBindFlag(Integer bindFlag) {
		this.bindFlag = bindFlag;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public Integer getUcPayMode() {
		return ucPayMode;
	}

	public void setUcPayMode(Integer ucPayMode) {
		this.ucPayMode = ucPayMode;
	}

	public Date getUcUpdateDate() {
		return ucUpdateDate;
	}

	public void setUcUpdateDate(Date ucUpdateDate) {
		this.ucUpdateDate = ucUpdateDate;
	}

	

	
	
}
