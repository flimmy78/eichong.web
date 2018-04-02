package com.wanma.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluemobi.product.model.common.DwzAjaxResult;
import com.bluemobi.product.model.common.DwzPagerMySQL;
import com.bluemobi.product.utils.JsonObject;
import com.wanma.common.SessionMgr;
import com.wanma.common.WanmaConstants;
import com.wanma.model.TblUser;
import com.wanma.service.CmsMoneyRecordService;

/**
 * 用户收支列表
 * 
 * @author ZB
 * 
 * 
 */
@Controller
@RequestMapping("/admin/moneyRecord")
public class CmsMoneyRecordController {

	@Autowired
	private CmsMoneyRecordService moneyRecordService;

	/**
	 * 用户收支列表
	 * 
	 * @author wbc 2015年12月29日
	 * @return: String
	 */
	@RequestMapping("/getMoneyRecordList")
	public String getMoneyRecordList(String phone, String startTime,
			String endTime, Map<String, Object> params,
			@ModelAttribute("pager") DwzPagerMySQL pager, Model model,
			HttpServletRequest request) {
		// 获取登陆用户
		TblUser loginUser = SessionMgr.getWebUser(request);
		HashMap<String, Object> totalRecharge = moneyRecordService
				.getTotalRecharge();
		HashMap<String, Object> totalPurchase = moneyRecordService
				.getTotalPurchase();
		HashMap<String, Object> totalAccount = moneyRecordService
				.getTotalAccount();
		params.put("offset", pager.getOffset());
		params.put("numPerPage", pager.getNumPerPage());
		params.put("usIn_Phone", phone);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		long total = moneyRecordService.getUserMoneyRecordCount(params);
		if (total <= pager.getOffset()) {
			pager.setPageNum(1L);
		}
		List<HashMap<String, Object>> userMoneyRecordList = moneyRecordService
				.getUserMoneyRecordList(params);

		// 将用户收支列表放到会话中
		pager.setTotal(total);
		model.addAttribute("totalRecharge", totalRecharge);
		model.addAttribute("totalPurchase", totalPurchase);
		model.addAttribute("totalAccount", totalAccount);
		model.addAttribute("params", params);
		model.addAttribute("userMoneyRecordList", userMoneyRecordList);
		model.addAttribute("pager", pager);
		if (loginUser.getUserId() == 8231) {
			return "backstage/moneyRecord/moneyRecord-listForShow";
		} else {
			return "backstage/moneyRecord/moneyRecord-list";
		}

	}

	/**
	 * 用户收支明细
	 * 
	 * @author mb
	 * 
	 * 
	 */
	@RequestMapping("/getUserMoneyRecordDetials")
	public String getUserMoneyRecordDetials(String puHi_UserId,
			String puHi_Type, String startTime, String endTime,
			Map<String, Object> params, HttpServletRequest request,
			@ModelAttribute("pager") DwzPagerMySQL pager, Model model) {
		// 获取登陆用户
		TblUser loginUser = SessionMgr.getWebUser(request);

		int UserId = Integer.parseInt(puHi_UserId);
		if (!StringUtils.isBlank(puHi_Type)) {
			int puHiType = Integer.parseInt(puHi_Type);
			params.put("puHi_Type", puHiType);
		}
		params.put("startTime", startTime);
		// 传进来的参数不包括时分秒，在这里补上
		if (!(endTime == null || endTime == "")) {
			params.put("endTime", endTime + " 23:59:59");
		}

		params.put("puHi_UserId", UserId);
		HashMap<String, Object> userTotalRecharge = moneyRecordService
				.getUserTotalRecharge(params);
		HashMap<String, Object> userTotalPurchase = moneyRecordService
				.getUserTotalPurchase(params);
		params.put("offset", pager.getOffset());
		params.put("numPerPage", pager.getNumPerPage());
		model.addAttribute("proviceMap", WanmaConstants.provinceMap);
		String proviceCode = request.getParameter("provinceCode");
		if (StringUtils.isNotBlank(proviceCode)) {
			List<Object> cityList = new ArrayList<Object>();
			for (String citycode : WanmaConstants.provinceCityMap
					.get(proviceCode)) {
				Map<String, Object> cityMap = WanmaConstants.cityMap;
				cityList.add(cityMap.get(citycode));
			}
			model.addAttribute("cityList", cityList);
		}
		String cityCode = request.getParameter("cityCode");
		if (StringUtils.isNotBlank(cityCode)) {
			List<Object> areaList = new ArrayList<Object>();
			for (String areacode : WanmaConstants.cityAreaMap.get(cityCode)) {
				Map<String, Object> areaMap = WanmaConstants.areaMap;
				areaList.add(areaMap.get(areacode));
			}
			model.addAttribute("areaList", areaList);
		}
		params.put("proviceCode", proviceCode);
		params.put("cityCode", cityCode);
		params.put("countyCode", request.getParameter("countyCode"));
		long total = moneyRecordService.getMoneyRecordCountByUserId(params);
		if (total <= pager.getOffset()) {
			pager.setPageNum(1L);
		}
		List<HashMap<String, Object>> moneyRecordListByUserId = moneyRecordService
				.getMoneyRecordListByUserId(params);
		// 使用完毕后将时间还原回去
		if (!StringUtils.isEmpty((String) params.get("endTime"))) {
			params.put("endTime",
					((String) params.get("endTime")).replace(" 23:59:59", ""));
		}
		if (!(endTime == null || endTime == "")) {
			params.put("endTime", endTime);
		}
		// 将意见反馈信息放到会话中
		pager.setTotal(total);
		model.addAttribute("userTotalRecharge", userTotalRecharge);
		model.addAttribute("userTotalPurchase", userTotalPurchase);
		model.addAttribute("params", params);
		model.addAttribute("moneyRecordListByUserId", moneyRecordListByUserId);
		model.addAttribute("pager", pager);

		if (loginUser.getUserId() == 8231) {
			return "backstage/moneyRecord/userMoneyRecord-detailsForShow";
		} else {
			return "backstage/moneyRecord/userMoneyRecord-details";
		}
	}

	/**
	 * 合作商用户收支列表
	 * 
	 * @author mb
	 * 
	 * 
	 */
	@RequestMapping("/getPartnerMoneyRecordList")
	public String getPartnerMoneyRecordList(String companyName,
			Map<String, Object> params,
			@ModelAttribute("pager") DwzPagerMySQL pager, Model model,
			HttpServletRequest request) {
		// 获取登陆用户
		TblUser loginUser = SessionMgr.getWebUser(request);
		params.put("offset", pager.getOffset());
		params.put("numPerPage", pager.getNumPerPage());
		params.put("companyName", companyName);
		long total = moneyRecordService.getPartnerMoneyRecordCount(params);
		if (total <= pager.getOffset()) {
			pager.setPageNum(1L);
		}
		List<HashMap<String, Object>> partnerMoneyRecordList = moneyRecordService
				.getPartnerMoneyRecordList(params);

		// 将用户收支列表放到会话中
		pager.setTotal(total);
		model.addAttribute("params", params);
		model.addAttribute("partnerMoneyRecordList", partnerMoneyRecordList);
		model.addAttribute("pager", pager);
		if (loginUser.getUserId() == 8231) {
			return "backstage/moneyRecord/partnerMoneyRecord-listForShow";
		} else {
			return "backstage/moneyRecord/partnerMoneyRecord-list";
		}
	}

	/**
	 * 合作商用户收支明细
	 * 
	 * @author mb
	 */
	@RequestMapping("/getPartnerMoneyRecordDetials")
	public String getPartnerMoneyRecordDetials(String chOrOrgNo,
			String puHi_Type, String startTime, String endTime,
			Map<String, Object> params, HttpServletRequest request,
			@ModelAttribute("pager") DwzPagerMySQL pager, Model model) {
		// 获取登陆用户
		TblUser loginUser = SessionMgr.getWebUser(request);
		if (!StringUtils.isBlank(puHi_Type)) {
			int puHiType = Integer.parseInt(puHi_Type);
			params.put("puHi_Type", puHiType);
		}
		params.put("startTime", startTime);
		// 传进来的参数不包括时分秒，在这里补上
		if (!(endTime == null || endTime == "")) {
			params.put("endTime", endTime + " 23:59:59");
		}
		params.put("chOrOrgNo", chOrOrgNo);
		params.put("offset", pager.getOffset());
		params.put("numPerPage", pager.getNumPerPage());
		long total = moneyRecordService
				.getMoneyRecordCountByCompanyNameNumber(params);
		if (total <= pager.getOffset()) {
			pager.setPageNum(1L);
		}
		List<HashMap<String, Object>> moneyRecordListByUserId = moneyRecordService
				.getMoneyRecordListByCompanyNameNumber(params);
		// 使用完毕后将时间还原回去
		if (!StringUtils.isEmpty((String) params.get("endTime"))) {
			params.put("endTime",
					((String) params.get("endTime")).replace(" 23:59:59", ""));
		}
		if (!(endTime == null || endTime == "")) {
			params.put("endTime", endTime);
		}
		// 将意见反馈信息放到会话中
		pager.setTotal(total);
		model.addAttribute("params", params);
		model.addAttribute("moneyRecordListByUserId", moneyRecordListByUserId);
		model.addAttribute("pager", pager);
		if (loginUser.getUserId() == 8231) {
			return "backstage/moneyRecord/partnerMoneyRecord-detailsForShow";
		} else {
			return "backstage/moneyRecord/partnerMoneyRecord-details";
		}
	}

	/**
	 * 退款：跳转退款页面
	 * 
	 * @param all_account
	 * @param usIn_FacticityName
	 * @param user_account
	 * @param params
	 * @param pager
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/getRecordRefund")
	public String getRecordRefund(String userId, String user_account,
			Map<String, Object> params, Model model) {
		params = moneyRecordService.getRefundInfo(userId);
		params.put("user_account", user_account);
		/*
		 * params.put("usIn_FacticityName", usIn_FacticityName);
		 * params.put("all_account", all_account);
		 */
		model.addAttribute("params", params);
		return "backstage/moneyRecord/partnerMoneyRecord-refund";
	}

	/**
	 * 退款：执行退款
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/refundByUserAccount")
	@ResponseBody
	public String refundByUserAccount(HttpServletRequest request)
			throws Exception {
		String userAccount = request.getParameter("userAccount");
		String refundAccount = request.getParameter("refundAccount");
		String puHiUserId = moneyRecordService.getPuHiUserId(userAccount);
		int refund = moneyRecordService.refund(userAccount, refundAccount,
				puHiUserId);
		// 返回处理结果信息
		return new JsonObject(refund).toString();
	}

}
