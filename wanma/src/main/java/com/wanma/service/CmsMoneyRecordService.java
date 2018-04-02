package com.wanma.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wanma.model.Feedback;
import com.wanma.model.TblInstalldetail;

public interface CmsMoneyRecordService {
	/**
	 * 查出充值总额
	 * 
	 * @return
	 */
	public HashMap<String, Object> getTotalRecharge();

	/**
	 * 查出消费总额
	 * 
	 * @return
	 */
	public HashMap<String, Object> getTotalPurchase();

	/**
	 * 查出用户消费充值情况
	 * 
	 * @param parems
	 * @return
	 */
	public List<HashMap<String, Object>> getUserMoneyRecordList(
			Map<String, Object> params);

	public long getUserMoneyRecordCount(Map<String, Object> params);

	/**
	 * 查出单个用户充值总额
	 * 
	 * @return
	 */
	public HashMap<String, Object> getUserTotalRecharge(
			Map<String, Object> params);

	/**
	 * 查出单个用户消费总额
	 * 
	 * @return
	 */
	public HashMap<String, Object> getUserTotalPurchase(
			Map<String, Object> params);

	/**
	 * 查出单个用户消费充值详情
	 * 
	 * @param parems
	 * @return
	 */

	public List<HashMap<String, Object>> getMoneyRecordListByUserId(
			Map<String, Object> params);

	public long getMoneyRecordCountByUserId(Map<String, Object> params);

	public HashMap<String, Object> getTotalAccount();
	/**
	 * 查出合作商用户消费充值情况
	 * 
	 * @param parems
	 * @return
	 */
	public long getPartnerMoneyRecordCount(Map<String, Object> params);

	public List<HashMap<String, Object>> getPartnerMoneyRecordList(
			Map<String, Object> params);

	public long getMoneyRecordCountByCompanyNameNumber(
			Map<String, Object> params);

	public List<HashMap<String, Object>> getMoneyRecordListByCompanyNameNumber(
			Map<String, Object> params);


	public int updateNormAccount(Map<String, Object> map);

	public String getPuHiUserId(String userAccount);

	public int insertPurhistory(Map<String, Object> hiMap);

	public int refund(String userAccount, String refundAccount,String puHiUserId)throws Exception;

	public Map<String, Object> getRefundInfo(String userId);

}
