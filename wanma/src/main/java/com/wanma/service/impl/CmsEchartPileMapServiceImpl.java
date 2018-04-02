package com.wanma.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bluemobi.product.model.echarts.EchartsParamModel;
import com.wanma.dao.CmsStatisticMapper;
import com.wanma.web.support.utils.JsonUtil;

@Service
public class CmsEchartPileMapServiceImpl extends CmsEchartsServiceImpl {
	@Autowired
	CmsStatisticMapper statisticsMapper;

	@Override
	public void setData(JSONObject obj, EchartsParamModel paramsModel) {
		setPileBespokingData(obj, paramsModel);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setPileBespokingData(JSONObject obj,
			EchartsParamModel paramsModel) {
		JSONObject jsonModel = JsonUtil.getJsonObject(path
				+ "/json/pileMap.json");
		List<Map<Object, Object>> pileProviceCountlist = statisticsMapper
				.getPileProviceCount(paramsModel);
		String userId = paramsModel.getUserIdForShow();
		if (pileProviceCountlist != null && !pileProviceCountlist.isEmpty()) {
			List<String> provinceGroup = new ArrayList<String>();
			List<Map<Object, Object>> provinceList = new ArrayList<Map<Object, Object>>();
			Map provinceMap = null;
			String province = "";
			String count = "";
			for (Map<Object, Object> tempObj : pileProviceCountlist) {
				province = (String) tempObj.get("province_name");
				province = getSimpleNameByProvinceName(province);
				count = tempObj.get("pile_count") == null ? "" : (tempObj
						.get("pile_count").toString());
				provinceMap = new HashMap();
				provinceMap.put("name", province);
				if("8231".equals(userId)&&count!=""){
					count = String.valueOf(Integer.parseInt(count)*5);
				}
				provinceMap.put("value", count);
				provinceList.add(provinceMap);
				provinceGroup.add(province);
			}
			// series
			Map map = new HashMap();
			map.put("name", "电站数量");
			map.put("type", "map");
			map.put("mapType", "china");
			map.put("roam", false);
			// 设置地图显示身份名称并设置字体颜色
			Map itemMap = new HashMap();
			Map itemMap1 = new HashMap();
			Map itemMap2 = new HashMap();
			itemMap2.put("show", true);
			itemMap1.put("label", itemMap2);
			itemMap1.put("color", "#FF8040");
			itemMap.put("normal", itemMap1);
			Map textStyleMap = new HashMap();
			textStyleMap.put("color", "#842B00");
			itemMap2.put("textStyle", textStyleMap);
			Map empMap = new HashMap();
			Map empMap1 = new HashMap();
			empMap1.put("show", true);
			empMap.put("label", empMap1);
			itemMap.put("emphasis", empMap);
			map.put("itemStyle", itemMap);

			map.put("data", provinceList);
			List list = new ArrayList();
			list.add(map);
			jsonModel.put("series", list);
			obj.put("data1", jsonModel);
			if (paramsModel.getUserId() == null){
				List<Map<Object, Object>> pileProviceTypeCountlist = statisticsMapper
						.getPileProviceAndCompanyCount();
				setData2(provinceGroup, pileProviceTypeCountlist, obj,userId);
				obj.put("isShowTypeCheckBox", "Y");
			}
		}
	}

	private void setData2(List<String> provinceGroup,
			List<Map<Object, Object>> pileProviceTypeCountlist, JSONObject obj, String userId) {
		Map<Object, Object> dataTempMap = new HashMap<Object, Object>();
		for (Map<Object, Object> bean : pileProviceTypeCountlist) {
			String province = getSimpleNameByProvinceName((String) bean
					.get("province_name"));
			dataTempMap.put(province + bean.get("elPi_OwnerCompany"),
					bean.get("pile_count"));
		}
		Map<Object, Object> data2Map = new HashMap<Object, Object>();
		;
		String[] typeGroup = new String[] { "0", "1", "2", "3" };
		for (String province : provinceGroup) {
			Map<Object, Object> typeAndCountMap = new HashMap<Object, Object>();
			for (String type : typeGroup) {
				Object count = dataTempMap.get(province + type);
				if (count != null)
					if("8231".equals(userId)){
						if(count instanceof Integer){
							count = (int)count*5;
						}
					}
					typeAndCountMap.put(type, count);
			}
			data2Map.put(province, typeAndCountMap);
		}
		obj.put("data2", data2Map);

	}

	// private void setPileBespokingDataTemp(JSONObject obj,
	// EchartsParamModel paramsModel) {
	// JSONObject jsonModel = JsonUtil.getJsonObject(path
	// + "/json/pileMap.json");
	// List<Map<Object, Object>> pileProviceCountlist = statisticsMapper
	// .getPileProviceCount(paramsModel);
	// if (pileProviceCountlist != null && !pileProviceCountlist.isEmpty()) {
	// List<Map<Object, Object>> provinceList=new ArrayList<Map<Object,
	// Object>>();
	// Map provinceMap = null;
	// String province = "";
	// int count = 0;
	// for (Map<Object, Object> tempObj : pileProviceCountlist) {
	// province = (String) tempObj.get("province_name");
	// province = getSimpleNameByProvinceName(province);
	// count = tempObj.get("pile_count") == null ? 0 : ((Long) tempObj
	// .get("pile_count")).intValue();
	// provinceMap=new HashMap();
	// provinceMap.put("name", province);
	// provinceMap.put("value", count);
	// provinceList.add(provinceMap);
	// }
	// // series
	// Map map = new HashMap();
	// map.put("name", "分享桩体数量");
	// map.put("type", "map");
	// map.put("mapType", "china");
	// map.put("roam", "false");
	// map.put("data", provinceList);
	// List list = new ArrayList();
	// list.add(map);
	// jsonModel.put("series", list);
	// obj.put("data1", jsonModel);
	// }
	// }

	private String getSimpleNameByProvinceName(String provinceName) {
		return provinceName.replace("市", "").replace("省", "")
				.replace("自治区", "").replace("特别行政区", "").replace("维吾尔", "")
				.replace("回族", "").replace("壮族", "");
	}
}
