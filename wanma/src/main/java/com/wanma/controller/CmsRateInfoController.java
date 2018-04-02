package com.wanma.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluemobi.product.common.MessageManager;
import com.bluemobi.product.controller.BaseController;
import com.bluemobi.product.model.common.DwzAjaxResult;
import com.bluemobi.product.model.common.DwzPagerMySQL;
import com.bluemobi.product.utils.AccessErrorResult;
import com.bluemobi.product.utils.AccessSuccessResult;
import com.bluemobi.product.utils.JsonObject;
import com.wanma.common.SessionMgr;
import com.wanma.common.WanmaConstants;
import com.wanma.model.TblRateInfo;
import com.wanma.model.TblUser;
import com.wanma.service.CmsCommitLogService;
import com.wanma.service.ElectricPileService;
import com.wanma.service.impl.CmsRateInfoServiceImpl;
import com.wanma.web.support.utils.ApiUtil;
import com.wanma.web.support.utils.HttpsUtil;

@Controller
@RequestMapping(value = "admin/rateinfo")
public class CmsRateInfoController extends BaseController {
	// 日志输出对象
   private static Logger log = Logger.getLogger(CmsRateInfoController.class);

	@Autowired
	CmsRateInfoServiceImpl cmsRateInfoService;
	@Autowired
	ElectricPileService epService;
	@Autowired
	CmsCommitLogService commitLogService;

	@RequestMapping("getRateInfo")
	public String getRateInfoByUserId() {

		return "backstage/rateinfo/rateinfo";
	}

	/**
	 * 根据登录用户获取对应费率列表
	 * 
	 * @param pager
	 * @param tblRateInfo
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("getRateInfoList")
	public String getRateInfoListByuId(
			@ModelAttribute("pager") DwzPagerMySQL pager,
			@ModelAttribute TblRateInfo tblRateInfo,String rateInformation,
			HttpServletRequest request, Model model) {

		TblUser user = SessionMgr.getWebUser(request);
		tblRateInfo.setUserId(user.getUserId().toString());
		tblRateInfo.setUser_level(user.getUserLevel());
		Integer total = cmsRateInfoService.getRateInfoNumByUserId(tblRateInfo);
		if (total <= pager.getOffset()) {
			pager.setPageNum(1L);
		}
		// 根据登录session中的用户id去获取费率列表
		tblRateInfo.setPager(pager);
		List<Map<String, Object>> list = cmsRateInfoService
				.getRateInfoListByUserId(tblRateInfo);
		model.addAttribute("rateInfoList", list);
		pager.setTotal(total.longValue());
		model.addAttribute("pager", pager);
		model.addAttribute("tblRateInfo",tblRateInfo);
		model.addAttribute("proviceMap", WanmaConstants.provinceMap);
		model.addAttribute("loginUserLevel", user.getUserLevel());
		return "backstage/rateinfo/rateinfo-list";
	}

	@RequestMapping("addRateInfoUi")
	public String addRateInfoUi(@ModelAttribute TblRateInfo tblRateInfo,
			Model model) {

		String RateAddselProvinceId = tblRateInfo.getProvinceId();
		String RateAddselCityId = tblRateInfo.getCityId();
		model.addAttribute("provinceMap", WanmaConstants.provinceMap);
		if (StringUtils.isNotBlank(RateAddselProvinceId)) {
			List<Object> cityList = new ArrayList<Object>();
			for (String citycode : WanmaConstants.provinceCityMap
					.get(RateAddselProvinceId)) {
				Map<String, Object> cityMap = WanmaConstants.cityMap;
				cityList.add(cityMap.get(citycode));
			}
			model.addAttribute("cityList", cityList);
		}
		if (StringUtils.isNotBlank(RateAddselCityId)) {
			List<Object> areaList = new ArrayList<Object>();
			for (String areacode : WanmaConstants.cityAreaMap
					.get(RateAddselCityId)) {
				Map<String, Object> areaMap = WanmaConstants.areaMap;
				areaList.add(areaMap.get(areacode));
			}
			model.addAttribute("areaList", areaList);
		}
		return "backstage/rateinfo/rateinfo-add";
	}

	@RequestMapping("addRateInfo")
	@ResponseBody
	public String addRateInfo(@ModelAttribute TblRateInfo tblRateInfo,
			HttpServletRequest request, Model model) {
		TblUser user = SessionMgr.getWebUser(request);

		// 告警金额使用默认值10元
		tblRateInfo.setRaIn_WarnMoney(new BigDecimal(10));
		// 最小欲动结金额默认10元
		if (tblRateInfo.getRaIn_MinFreezingMoney() == null
				|| tblRateInfo.getRaIn_MinFreezingMoney() + "" == "") {
			tblRateInfo.setRaIn_MinFreezingMoney(new BigDecimal(10));
		}
		if ("1".equals(tblRateInfo.getRaInType())) {
			tblRateInfo
					.setRaIn_TipTimeTariff(tblRateInfo.getRaIn_TipTimeTariff()
							.setScale(2, BigDecimal.ROUND_DOWN));
			tblRateInfo.setRaIn_PeakElectricityPrice(tblRateInfo
					.getRaIn_PeakElectricityPrice().setScale(2,
							BigDecimal.ROUND_DOWN));
			tblRateInfo.setRaIn_UsualPrice(tblRateInfo.getRaIn_UsualPrice()
					.setScale(2, BigDecimal.ROUND_DOWN));
			tblRateInfo.setRaIn_ValleyTimePrice(tblRateInfo
					.getRaIn_ValleyTimePrice().setScale(2,
							BigDecimal.ROUND_DOWN));
			tblRateInfo
					.setRaIn_ServiceCharge(tblRateInfo.getRaIn_ServiceCharge()
							.setScale(2, BigDecimal.ROUND_DOWN));
		}

		DwzAjaxResult dwzResult = null;
		try {
			tblRateInfo.setUserId(user.getUserId().toString());
			tblRateInfo.setUpdateUserId(user.getUserId().toString());

			cmsRateInfoService.insertRateInfo(tblRateInfo);
			dwzResult = new DwzAjaxResult("200", "新增成功", "getRateInfoList",
					"closeCurrent", "");
		} catch (Exception e) {
			log.error(e.getMessage());
			dwzResult = new DwzAjaxResult("300", "新增失败", "rateInfoAddPage", "",
					"");
		}
		return new JsonObject(dwzResult).toString();
	}

	@RequestMapping("removeRateInfo")
	@ResponseBody
	public String delRateInfo(String ids) {
		DwzAjaxResult dwzResult = null;
		try {
			String[] idsn = ids.split(",");
			String errorCode = checkIds(idsn);
			if (StringUtils.isNotBlank(errorCode)) {
				return new DwzAjaxResult("300", "费率已经绑定桩体:" + errorCode,
						"getRateInfoList", "", "").toJSONString();
			}
			for (String id : idsn) {
				cmsRateInfoService.delRateInfo(Integer.parseInt(id));
			}
			dwzResult = new DwzAjaxResult("200", "删除成功", "getRateInfoList", "",
					"");
		} catch (Exception e) {
			log.error(e.getMessage());
			dwzResult = new DwzAjaxResult("300", "删除失败", "getRateInfoList", "",
					"");
		}

		return new JsonObject(dwzResult).toString();
	}

	private String checkIds(String[] ids) {
		String errorCode = "";
		for (String id : ids) {
			List<HashMap<String, Object>> list = null;
			list = cmsRateInfoService.getRateAndElectricSend(Integer
					.parseInt(id));
			if (list.size() > 0)
				errorCode += id + ",";
		}
		return StringUtils.removeEnd(errorCode, ",");

	}

	/**
	 * 跳转到修改界面
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("changeRateInfoUi")
	public String changeRateInfoUi(int id, Model model) {
		// SimpleDateFormat dateformat= new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, Object> map = cmsRateInfoService.getRateInfoById(id);
		String RateAddselProvinceId = String.valueOf(map
				.get("raIn_province_id"));
		String RateAddselCityId = String.valueOf(map.get("raIn_city_id"));
		// String RateAddselDistrictId = (String)map.get("raIn_area_id");
		model.addAttribute("provinceMap", WanmaConstants.provinceMap);
		if (StringUtils.isNotBlank(RateAddselProvinceId)) {
			List<Object> cityList = new ArrayList<Object>();
			Map<String, Object> cityMap = WanmaConstants.cityMap;
			for (String citycode : WanmaConstants.provinceCityMap
					.get(RateAddselProvinceId)) {
				cityList.add(cityMap.get(citycode));
			}
			model.addAttribute("cityList", cityList);
		}
		if (StringUtils.isNotBlank(RateAddselCityId)) {
			List<Object> areaList = new ArrayList<Object>();
			Map<String, Object> areaMap = WanmaConstants.areaMap;
			for (String areacode : WanmaConstants.cityAreaMap
					.get(RateAddselCityId)) {
				areaList.add(areaMap.get(areacode));
			}
			model.addAttribute("areaList", areaList);
		}

		// 去掉timestemp后面跟的毫秒
		// map.put("raIn_EffectiveDates",
		// dateformat.format(map.get("raIn_EffectiveDates")));
		// map.put("raIn_ExpiryDate",
		// dateformat.format(map.get("raIn_ExpiryDate")));
		model.addAttribute("rateMap", map);

		return "backstage/rateinfo/rateinfo-change";
	}

	/**
	 * 跳转到修改界面
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("showRateInfoUi")
	public String showRateInfoUi(int id, Model model) {
		Map<String, Object> map = cmsRateInfoService.getRateInfoById(id);
		String RateAddselProvinceId = String.valueOf(map
				.get("raIn_province_id"));
		String RateAddselCityId = String.valueOf(map.get("raIn_city_id"));
		model.addAttribute("provinceMap", WanmaConstants.provinceMap);

		if (StringUtils.isNotBlank(RateAddselProvinceId)) {
			List<Object> cityList = new ArrayList<Object>();
			Map<String, Object> cityMap = WanmaConstants.cityMap;
			for (String citycode : WanmaConstants.provinceCityMap
					.get(RateAddselProvinceId)) {
				cityList.add(cityMap.get(citycode));
			}
			model.addAttribute("cityList", cityList);
		}
		if (StringUtils.isNotBlank(RateAddselCityId)) {
			List<Object> areaList = new ArrayList<Object>();
			Map<String, Object> areaMap = WanmaConstants.areaMap;
			for (String areacode : WanmaConstants.cityAreaMap
					.get(RateAddselCityId)) {
				areaList.add(areaMap.get(areacode));
			}
			model.addAttribute("areaList", areaList);
		}
		model.addAttribute("rateMap", map);

		return "backstage/rateinfo/rateinfo-show";
	}

	@RequestMapping("updateRateInfo")
	@ResponseBody
	public String updateRateInfo(TblRateInfo tblRateInfo,
			HttpServletRequest request, Model model) {
		TblUser user = SessionMgr.getWebUser(request);
		DwzAjaxResult dwzResult = null;
		String flag = "false";
		try {
			if ("1".equals(tblRateInfo.getRaInType())) {
				tblRateInfo.setRaIn_TipTimeTariff(tblRateInfo
						.getRaIn_TipTimeTariff().setScale(2,
								BigDecimal.ROUND_DOWN));
				tblRateInfo.setRaIn_PeakElectricityPrice(tblRateInfo
						.getRaIn_PeakElectricityPrice().setScale(2,
								BigDecimal.ROUND_DOWN));
				tblRateInfo.setRaIn_UsualPrice(tblRateInfo.getRaIn_UsualPrice()
						.setScale(2, BigDecimal.ROUND_DOWN));
				tblRateInfo.setRaIn_ValleyTimePrice(tblRateInfo
						.getRaIn_ValleyTimePrice().setScale(2,
								BigDecimal.ROUND_DOWN));
				tblRateInfo.setRaIn_ServiceCharge(tblRateInfo
						.getRaIn_ServiceCharge().setScale(2,
								BigDecimal.ROUND_DOWN));
			}

			tblRateInfo.setUpdateUserId(user.getUserId().toString());
			cmsRateInfoService.updateRateInfo(tblRateInfo);
			flag = "success";
			dwzResult = new DwzAjaxResult("200", "修改成功", "getRateInfoList",
					"closeCurrent", "");
			List<String> list = epService.getEpCodesByRateId(tblRateInfo
					.getPk_RateInformation());
			// 将桩体编号拼成要发送的格式
			String sendStr = "";
			for (String code : list) {
				sendStr += code + ",";
			}
			if (sendStr.length() > 0) {
				sendStr = sendStr.substring(0, sendStr.length() - 1);
				sendStr += ":" + tblRateInfo.getPk_RateInformation();
				MessageManager m = new MessageManager();
				String apiBaseUrl = m.getSystemProperties("apiRoot");
				String token = ApiUtil.getToken();
				// 调用接口更新费率
				HttpsUtil.getResponseParam(apiBaseUrl
						+ "/app/net/sendRate.do?paramStrs=" + sendStr + "&t="
						+ token, "status");
				commitLogService.insert("费率更新命令下发，主键：["
						+ tblRateInfo.getPk_RateInformation() + "]");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			if ("success".equals(flag)) {
				dwzResult = new DwzAjaxResult("300", "修改成功，但下发失败",
						"rateInfoEditPage", "", "");
			} else {
				dwzResult = new DwzAjaxResult("300", "修改失败",
						"rateInfoEditPage", "", "");
			}
		}

		return new JsonObject(dwzResult).toString();
	}

	@RequestMapping("searchProvinceList")
	@ResponseBody
	public String searchProvinceList(
			@RequestParam HashMap<String, Object> params) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		try {
			list = cmsRateInfoService.searchProvinceList(params);

		} catch (Exception e) {
			log.error(e.getMessage());
			// 返回省级列表错误信息
			return new AccessErrorResult(2004, "error.msg.invalid.parameter")
					.toString();
		}
		log.info("******************获取省级列表-end************************");
		// 返回处理成功信息
		return new AccessSuccessResult(list).toString();

	}

	@RequestMapping("searchCityList")
	@ResponseBody
	public String searchCityList(@RequestParam Map<String, Object> params) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		try {

			list = cmsRateInfoService.searchCityList(params);
		} catch (Exception e) {
			log.error(e.getMessage());
			// 返回市级列表错误信息
			return new AccessErrorResult(2004, "error.msg.invalid.parameter")
					.toString();
		}
		log.info("******************获取市级列表-end************************");
		return new AccessSuccessResult(list).toString();
	}

	@RequestMapping("searchAreaList")
	@ResponseBody
	public String searchAreaList(@RequestParam Map<String, Object> params) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		try {
			list = cmsRateInfoService.searchAreaList(params);
		} catch (Exception e) {
			log.error(e.getMessage());
			// 返回区县级列表错误信息
			return new AccessErrorResult(2004, "error.msg.invalid.parameter")
					.toString();
		}
		log.info("******************获取区县级列表-end************************");
		return new AccessSuccessResult(list).toString();
	}

}
