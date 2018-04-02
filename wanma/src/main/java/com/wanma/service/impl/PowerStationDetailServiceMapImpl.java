/**
 * FileName:AppFeedbackMapper.java
 * Author: Administrator
 * Create: 2014年6月26日
 * Last Modified: 2014年6月26日
 * Version: V1.0 
 */
package com.wanma.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.wanma.common.JsonUtil;
import com.wanma.common.JudgeNullUtils;
import com.wanma.common.WanmaConstants;
import com.wanma.dao.CmsElectricPileMapper;
import com.wanma.dao.CmsElectricpileheadMapper;
import com.wanma.dao.CmsPowerstationMapper;
import com.wanma.dao.CmsProductcommentMapper;
import com.wanma.model.PowerElectricPileHead;
import com.wanma.model.PowerStationDetail;
import com.wanma.model.PowerStationElictric;
import com.wanma.model.TblElectricpilehead;
import com.wanma.model.TblPowerstation;
import com.wanma.service.PowerStationDetailMapService;

/***
 *
 * 充电点详情
 * 
 * @Description:
 * @author bruce cheng(http://blog.csdn.net/brucehome)
 * @createTime：2015-3-13 下午04:51:34
 * @updator：
 * @updateTime：
 * @version：V1.0
 */
@Service
public class PowerStationDetailServiceMapImpl implements
		PowerStationDetailMapService {

	@Autowired
	CmsElectricPileMapper tblElectricpileMapper;
	@Autowired
	CmsPowerstationMapper tblPowerstationMapper;
	@Autowired
	CmsElectricpileheadMapper tblElectricpileheadMapper;
	@Autowired
	CmsProductcommentMapper productcommentMapper;

	@Override
	public PowerStationDetail getPowerStationDetail(
			TblPowerstation tblPowerstation) {
		// TODO Auto-generated method stub
		PowerStationDetail powerStationDetail = null;

		// 01：获取充电点信息
		Map<String, Object> powerStationInfo = tblPowerstationMapper
				.getPowerstationById(tblPowerstation);

		if (powerStationInfo != null) {
			powerStationDetail = new PowerStationDetail();
			powerStationDetail.setPowerStationId(JudgeNullUtils
					.nvlStr(powerStationInfo.get("pkPowerstation")));
			powerStationDetail.setPowerStationName(JudgeNullUtils
					.nvlStr(powerStationInfo.get("postName")));
			powerStationDetail.setPowerStationImage(JudgeNullUtils
					.nvlStr(powerStationInfo.get("postPic")));
			powerStationDetail.setPowerStationState(JudgeNullUtils
					.nvlStr(powerStationInfo.get("postStatus")));
			powerStationDetail.setPowerElectricpileSum(JudgeNullUtils
					.nvlStr(powerStationInfo.get("pileSum")));
			powerStationDetail.setPowerStationAddress(JudgeNullUtils
					.nvlStr(powerStationInfo.get("postAddress")));
			powerStationDetail.setPostLongitude(JudgeNullUtils
					.nvlStr0(powerStationInfo.get("postLongitude")));
			powerStationDetail.setPostLatitude(JudgeNullUtils
					.nvlStr0(powerStationInfo.get("postLatitude")));
			powerStationDetail.setIsCollect(JudgeNullUtils
					.nvlStr(powerStationInfo.get("isCollect")));
			powerStationDetail.setPowerCommentStar(JudgeNullUtils
					.nvlStr0(powerStationInfo.get("powerCommentStar")));
			String phone = JudgeNullUtils.nvlStr(powerStationInfo
					.get("postPhone"));
			powerStationDetail.setTotalChargeAmt(JudgeNullUtils
					.nvlStr0(powerStationInfo.get("totalChargeAmt")));
			powerStationDetail.setTotalChargeDl(JudgeNullUtils
					.nvlStr0(powerStationInfo.get("totalChargeDl")));
			powerStationDetail.setTotalChargeTime(JudgeNullUtils
					.nvlStr0(powerStationInfo.get("totalChargeTime")));
			powerStationDetail.setPoStOnlineTime(JudgeNullUtils
					.nvlStr(powerStationInfo.get("poStOnlineTime")));
			powerStationDetail.setPowerStationTell(StringUtils
					.isNotBlank(phone) ? phone : WanmaConstants.WANMA_PHONE);
			// 02：获取电桩列表
			List<Map<String, Object>> electricpileList = tblElectricpileMapper
					.getElectricpileById(tblPowerstation);
			List<PowerStationElictric> powerStationElictricList = new ArrayList<PowerStationElictric>();

			for (int i = 0; i < electricpileList.size(); i++) {
				Map<String, Object> electricpileListMap = (Map<String, Object>) electricpileList
						.get(i);
				PowerStationElictric powerStationElictric = new PowerStationElictric();
				powerStationElictric.setElictricPicId(JudgeNullUtils
						.nvlStr(electricpileListMap.get("pk_ElectricPile")));
				powerStationElictric.setElictricPicName(JudgeNullUtils
						.nvlStr(electricpileListMap
								.get("elPi_ElectricPileName")));
				// powerStationElictric.setPicPowerSum(JudgeNullUtils.nvlStr(electricpileListMap.get("pileHeadSum")));
				powerStationElictric.setElictricPicImage(JudgeNullUtils
						.nvlStr(electricpileListMap.get("elPiImage")));
				powerStationElictric.setElectricPileNo(JudgeNullUtils
						.nvlStr(electricpileListMap
								.get("elPi_ElectricPileCode")));
				powerStationElictric.setElectricPileUserName(JudgeNullUtils
						.nvlStr(electricpileListMap.get("elPi_PowerUser")));
				powerStationElictric.setElectricPileChargingMode(JudgeNullUtils
						.nvlStr(electricpileListMap.get("elPi_ChargingMode")));
				powerStationElictric.setElectricPowerSize(JudgeNullUtils
						.nvlStr(electricpileListMap.get("elPi_PowerSize")));
				powerStationElictric
						.setElectricPowerInterface(JudgeNullUtils
								.nvlStr(electricpileListMap
										.get("elPi_PowerInterface")));
				powerStationElictric.setElectricNum(JudgeNullUtils
						.nvlStr(electricpileListMap.get("ep_num")));
				powerStationElictric
						.setRaInReservationRate(JudgeNullUtils
								.nvlStr0(electricpileListMap
										.get("raInReservationRate")));
				powerStationElictric.setRaInServiceCharge(JudgeNullUtils
						.nvlStr0(electricpileListMap.get("raInServiceCharge")));
				powerStationElictric.setElectricPileConnStatus(JudgeNullUtils
						.nvlStr0(electricpileListMap.get("comm_status")));
				powerStationElictric.setElectricPileState(JudgeNullUtils
						.nvlStr0(electricpileListMap.get("elPi_State")));
				powerStationElictric.setElPiElectricPileAddress(JudgeNullUtils
						.nvlStr0(electricpileListMap
								.get("elpiElectricpileaddress")));
				powerStationElictric.setTotalChargeAmt(JudgeNullUtils
						.nvlStr0(electricpileListMap.get("totalChargeAmt")));
				powerStationElictric.setTotalChargeDl(JudgeNullUtils
						.nvlStr0(electricpileListMap.get("totalChargeDl")));
				powerStationElictric.setTotalChargeTime(JudgeNullUtils
						.nvlStr0(electricpileListMap.get("totalChargeTime")));
				// 03:获取枪头列表
				TblElectricpilehead tblElectricpilehead = new TblElectricpilehead();
				tblElectricpilehead
						.setPkElectricpile(JudgeNullUtils
								.nvlInteger(electricpileListMap
										.get("pk_ElectricPile")));
				List<Map<String, Object>> electricpileheadList = tblElectricpileheadMapper
						.findMonitorHeadInfo(tblElectricpilehead);
				List<PowerElectricPileHead> pileHeadList = new ArrayList<PowerElectricPileHead>();
				for (int j = 0; j < electricpileheadList.size(); j++) {
					Map<String, Object> electricpileheadListMap = (Map<String, Object>) electricpileheadList
							.get(j);
					PowerElectricPileHead powerElectricPileHead = new PowerElectricPileHead();
					powerElectricPileHead.setPileHeadName(JudgeNullUtils
							.nvlStr(electricpileheadListMap
									.get("epheElectricpileheadname")));
					powerElectricPileHead.setPileHeadState(JudgeNullUtils
							.nvlStr(electricpileheadListMap
									.get("epheElectricpileheadstate")));
					powerElectricPileHead.setPileHeadId(JudgeNullUtils
							.nvlStr(electricpileheadListMap
									.get("pkElectricpilehead")));
					pileHeadList.add(powerElectricPileHead);
				}
				int headListSize = pileHeadList.size();
				if (headListSize < 3) {
					for (int j = headListSize; j < 3; j++)
						pileHeadList.add(new PowerElectricPileHead());
				} else if (headListSize > 3) {
					List<PowerElectricPileHead> sortPileHeadList = new ArrayList<PowerElectricPileHead>();
					for (int j = 0; j < headListSize; j++) {
						PowerElectricPileHead pileHead = pileHeadList.get(j);
						if ("0".equals(pileHead.getPileHeadState())) {
							sortPileHeadList.add(pileHead);
						}
					}
					for (int j = 0; j < headListSize; j++) {
						PowerElectricPileHead pileHead = pileHeadList.get(j);
						if (!"0".equals(pileHead.getPileHeadState())) {
							sortPileHeadList.add(pileHead);
						}
					}
					pileHeadList = sortPileHeadList;
				}
				// 添加枪头
				powerStationElictric.setPileHeadList(pileHeadList);
				powerStationElictricList.add(powerStationElictric);
			}
			// 添加电桩列表
			powerStationDetail
					.setPowerElectricpileList(powerStationElictricList);
			/*
			 * // 03：获取充电点评论 TblProductcommentMap tblProductcomment = new
			 * TblProductcommentMap();
			 * tblProductcomment.setPrcoProductid(JudgeNullUtils.nvlLang(
			 * tblPowerstation.getPkPowerstation()).intValue());
			 * tblProductcomment
			 * .setPrcoCommentType(ApplicationConsts.ElectricPileManager
			 * .ELECTRIC_POWER); List<TblProductcomment> commentList =
			 * productcommentMapper
			 * .findProCommentsByPowerId(tblProductcomment); Map<String, Object>
			 * params = new HashMap<String, Object>();
			 * params.put("prCoProductId",
			 * JudgeNullUtils.nvlLang(tblPowerstation.getPkPowerstation())
			 * .intValue()); params.put("prcoCommentType",
			 * ApplicationConsts.ElectricPileManager.ELECTRIC_POWER);
			 * powerStationDetail.setPowerCommentSum(String
			 * .valueOf(productcommentMapper.countProComments(params)));
			 * powerStationDetail.setCommentList(commentList);
			 */
		}

		return powerStationDetail;
	}

	@Override
	public Map<String, Integer> getPileCount(
			List<PowerStationElictric> powerElectricpileList) {
		int linkingCount = 0;
		int kongxianCount = 0;
		Map<String, Integer> countMap = new HashMap<String, Integer>();
		if (powerElectricpileList != null && powerElectricpileList.size() > 0) {
			for (PowerStationElictric powerStationElictric : powerElectricpileList) {
				if ("1".equals(powerStationElictric.getElectricPileConnStatus())) {
					linkingCount++;
					List<PowerElectricPileHead> headList = powerStationElictric
							.getPileHeadList();
					for (PowerElectricPileHead headInfo : headList) {
						if ("0".equals(headInfo.getPileHeadState())) {
							kongxianCount++;
							continue;
						}
					}
				}
			}
		}
		countMap.put("linkingCount", linkingCount);
		countMap.put("kongxianCount", kongxianCount);
		return countMap;
	}

	@Override
	public void makeFengzhiPrice(ModelMap map, String elictricPicId) {
		Map<String, Object> rateMap = getTblRateinformation(elictricPicId);// 费率
		if (rateMap != null) {
			map.put("fengzhiHtml", makeFengzhiStr(rateMap));
			map.put("raIn_ServiceCharge", rateMap.get("raIn_ServiceCharge"));
		}
	}

	public String makeFengzhiStr(Map<String, Object> rateMap) {
		String fengzhiHtml = "";
		Map<String, Map<String, Object>> jsonMap = JsonUtil
				.readJson2Map(rateMap.get("raIn_QuantumDate").toString());
		List<Map<String, Object>> rateList = (List<Map<String, Object>>) jsonMap
				.get("data");
		for (Map<String, Object> rateInfo : rateList) {
			int st = Integer.valueOf(rateInfo.get("st").toString());
			int et = Integer.valueOf(rateInfo.get("et").toString());
			Integer timeSt = (int) Math.floor(st / 60);
			String stMin = String.valueOf(st - timeSt * 60);
			Integer timeEt = (int) Math.floor(et / 60);
			String etMin = String.valueOf(et - timeEt * 60);
			rateInfo.put("timeStr", timeSt + ":"
					+ (stMin.length() == 1 ? "0" + stMin : stMin) + "--"
					+ timeEt + ":"
					+ (etMin.length() == 1 ? "0" + etMin : etMin));
			if ("1".equals(rateInfo.get("mark"))) {
				fengzhiHtml += "<li class='Jian'><span class='JFPG_BOX_l'>"
						+ timeSt + ":"
						+ (stMin.length() == 1 ? "0" + stMin : stMin) + "--"
						+ timeEt + ":"
						+ (etMin.length() == 1 ? "0" + etMin : etMin)
						+ "</span>" + "<span class='JFPG_BOX_c'>尖</span>"
						+ "<span class='JFPG_BOX_r'>"
						+ rateMap.get("raIn_TipTimeTariff") + "元/度</span>";

			}
			if ("2".equals(rateInfo.get("mark"))) {
				fengzhiHtml += "<li class='Feng'><span class='JFPG_BOX_l'>"
						+ timeSt + ":"
						+ (stMin.length() == 1 ? "0" + stMin : stMin) + "--"
						+ timeEt + ":"
						+ (etMin.length() == 1 ? "0" + etMin : etMin)
						+ "</span>" + "<span class='JFPG_BOX_c'>峰</span>"
						+ "<span class='JFPG_BOX_r'>"
						+ rateMap.get("raIn_PeakElectricityPrice")
						+ "元/度</span>";
			}
			if ("3".equals(rateInfo.get("mark"))) {
				fengzhiHtml += "<li class='Ping'><span class='JFPG_BOX_l'>"
						+ timeSt + ":"
						+ (stMin.length() == 1 ? "0" + stMin : stMin) + "--"
						+ timeEt + ":"
						+ (etMin.length() == 1 ? "0" + etMin : etMin)
						+ "</span>" + "<span class='JFPG_BOX_c'>平</span>"
						+ "<span class='JFPG_BOX_r'>"
						+ rateMap.get("raIn_UsualPrice") + "元/度</span>";
			}
			if ("4".equals(rateInfo.get("mark"))) {
				fengzhiHtml += "<li class='Gu'><span class='JFPG_BOX_l'>"
						+ timeSt + ":"
						+ (stMin.length() == 1 ? "0" + stMin : stMin) + "--"
						+ timeEt + ":"
						+ (etMin.length() == 1 ? "0" + etMin : etMin)
						+ "</span>" + "<span class='JFPG_BOX_c'>谷</span>"
						+ "<span class='JFPG_BOX_r'>"
						+ rateMap.get("raIn_ValleyTimePrice") + "元/度</span>";
			}

		}
		return fengzhiHtml;
	}

	public Map<String, Object> getTblRateinformation(String elictric) {
		Object obj = tblElectricpileMapper.getTblRateinformation(elictric);
		if (obj != null)
			return (Map<String, Object>) obj;
		return null;
	}

}
