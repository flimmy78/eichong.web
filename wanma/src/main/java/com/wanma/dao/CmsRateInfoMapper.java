package com.wanma.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wanma.model.TblRateInfo;

public interface CmsRateInfoMapper {
	
	/**
	 * 根据用户id获取费率信息
	 * @return
	 */
	public Map<String, String> getRateInfoByUserId();
	
	/**
	 * 根据用户id获取(超级管理员、管理员、纯商家)费率列表信息
	 * @return
	 */
	public List<Map<String, Object>> getRateInfoListByUserId(TblRateInfo tblRateInfo);
	/**
	 * 根据用户id获取同一个公司(该公司纯商家、子商家)的费率列表信息
	 * @param tblRateInfo
	 * @return
	 */
	public List<Map<String, Object>> getRateInfoListByUserId2(TblRateInfo tblRateInfo);
	
	/**
	 * 根据用户id，获取超级管理员、管理员、纯商家设置的费率总数
	 * @param tblRateinfo
	 * @return
	 */
	public int getRateInfoNumByUserId(TblRateInfo tblRateinfo);
	
	/**
	 * 根据用户id，获取公司设置的费率总数
	 * @param userId
	 * @return
	 */
	public int getRateInfoNumByUserId2(@Param("userId") String userId);
	
	
	
	public int insertRateInfo(TblRateInfo tblRateInfo);
	
	public void insertRateHistoryInfo(TblRateInfo tblRateInfo);
	
	public void delRateInfo(int id);
	
	/**
	 * 根据主键获取费率信息
	 * @param id
	 * @return
	 */
	public Map<String, Object> getRateInfoById(int id);
	
	public void updateRateInfo(TblRateInfo tblRateInfo);
	

	/**
	 * 省级字段数目，用在尖峰平谷电费模块中
	 * @return
	 */
	public int getElectricChargeCount();
	
	public void updateElectricCharge(Map<String,Object> params);
	/**
	 * 省市区三级查询
	 * @param params
	 * @return
	 */
	public List<HashMap<String,Object>> searchProvinceList(Map<String,Object> params);
	
	public List<HashMap<String,Object>>  searchCityList(Map<String,Object> params);
	
	public List<HashMap<String,Object>>  searchAreaList(Map<String,Object> params);
	
	/**
	 * 个体商家 根据登陆用户id和地区获取费率信息 
	 * @param rateInfo
	 * @return
	 */
	public List<TblRateInfo> getRateInfoByUser(TblRateInfo rateInfo);
	
	/**
	 * 纯商家 根据登陆用户id所属公司和地区获取费率信息
	 * @param rateInfo
	 * @return
	 */
	public List<TblRateInfo> getRateInfoByCompany(TblRateInfo rateInfo);
	
	/**
	 * 万马账户 根据地区获取万马新增费率 
	 * @param rateInfo
	 * @return
	 */
	public List<TblRateInfo> getRateInfoByWM(TblRateInfo rateInfo); 
	
	/**
	 * 判断传入ID的费率是否已经绑定了桩
	 * @param id
	 * @return
	 */
	public List<HashMap<String,Object>> getRateAndElectricSend(int id);

	/**
	 * 根据省份记录修改费率信息
	 * @param params
	 */
	public void updateRateInfoByProvince(Map<String, Object> params);

	public Double selectMinPriceByPsId(Integer pkPowerstation);
	
	/**
	 * 当前用户下的费率
	 * @param rateInfo
	 * @return
	 */
	public int getRateInfoList(TblRateInfo rateInfo);
	 	
}
