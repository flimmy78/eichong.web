/**
 * FileName:AppFeedbackMapper.java
 * Author: Administrator
 * Create: 2014年6月26日
 * Last Modified: 2014年6月26日
 * Version: V1.0 
 */
package com.wanma.service;

import com.wanma.model.PowerStationDetail;
import com.wanma.model.TblPowerstation;

/**
 * 反馈信息业务处理接口
 * 
 * @version V1.0
 * @author Administrator
 * @date 2014年6月26日
 */
public interface CmsPowerStationDetailService {

	/**
	 * 获取电桩列表
	 */
	public PowerStationDetail getPowerStationDetail(TblPowerstation tblPowerstation);

}
