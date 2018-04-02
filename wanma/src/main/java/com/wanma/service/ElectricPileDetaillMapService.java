/**
 * FileName:AppFeedbackMapper.java
 * Author: Administrator
 * Create: 2014年6月26日
 * Last Modified: 2014年6月26日
 * Version: V1.0 
 */
package com.wanma.service;

import java.util.Map;

import com.wanma.model.ElectricPileDetail;
import com.wanma.model.TblElectricpile;

/**
 * 反馈信息业务处理接口
 * 
 * @version V1.0
 * @author Administrator
 * @date 2014年6月26日
 */
public interface ElectricPileDetaillMapService {

	/**
	 * 获取电桩列表
	 */
	public ElectricPileDetail getElectricPileDetail(TblElectricpile tblElectricpile);
	/**
	 * @Title: getSharePowerstation
	 * @Description: 查询电桩分享信息
	 * @param TblPowerstation
	 * @return
	 */
	public TblElectricpile getShareElectricpile(TblElectricpile tblElectricpile);

	/**
	 * @Title: selectPileInfo
	 * @Description: 手机端扫描二维码显示电桩和枪口信息
	 * @param params
	 * @return
	 */
	public Map<String, Object> selectPileInfo(Map<String, Object> params);

	/**
	 * @Title:getElectricPileByCode
	 * @Description: 根据电桩编号获取电桩信息
	 * @param params
	 * @return
	 */
	public TblElectricpile getElectricPileByCode(String elpiElectricpilecode);

}
