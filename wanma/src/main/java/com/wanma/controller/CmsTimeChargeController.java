package com.wanma.controller;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluemobi.product.common.MessageManager;
import com.bluemobi.product.model.common.DwzAjaxResult;
import com.bluemobi.product.model.common.DwzPagerMySQL;
import com.bluemobi.product.utils.AccessErrorResult;
import com.bluemobi.product.utils.JsonObject;
import com.wanma.common.SessionMgr;
import com.wanma.common.WanmaConstants;
import com.wanma.model.TblPowerstation;
import com.wanma.model.TblTimeCharge;
import com.wanma.model.TblUser;
import com.wanma.model.TblUserCard;
import com.wanma.service.CmsBannerService;
import com.wanma.service.CmsCommitLogService;
import com.wanma.service.CmsTimeChargeService;
import com.wanma.web.support.utils.ApiUtil;
import com.wanma.web.support.utils.HttpsUtil;




/**
 * @Description: 定时充电管理controller
 * @author mb
 * @updateTime：
 * @version：V3.5.4
 */
@Controller
@RequestMapping("/admin/timeCharge")
public class CmsTimeChargeController {
	// 日志输出对象
	private static Logger log = Logger
			.getLogger(CmsTimeChargeController.class);
	/* 制造厂商service */
	@Autowired
	CmsTimeChargeService cmsTimeChargeService;
	@Autowired
	CmsCommitLogService commitLogService;
	/**
	 * timeCharge列表
	 * 
	 * @param pager
	 * @param advModel
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/timeChargeList")
	public String listSplash(@ModelAttribute("pager") DwzPagerMySQL pager,TblTimeCharge timeCharge, Model model, HttpServletRequest request) {
		log.info("******************获取timeCharge信息列表-begin************************");
		try {
			
			TblUser loginUser = SessionMgr.getWebUser(request);
			timeCharge.setElpiUserid(loginUser.getUserId().toString());
			timeCharge.setUserLevel(loginUser.getUserLevel().toString());
			// 充电点总数
			long total = cmsTimeChargeService.getElectricpileCount(timeCharge);
			if (total <= pager.getOffset()) {
				pager.setPageNum(1L);
			}
			// 设置查询参数
			timeCharge.setPager(pager);
			// 获取电桩列表
			List<TblTimeCharge> timeChargeList = cmsTimeChargeService.getElectricpileList(timeCharge);
			pager.setTotal(total);
			//将数据放入会话
			model.addAttribute("pager",pager);
			model.addAttribute("proviceMap", WanmaConstants.provinceMap);
			model.addAttribute("timeCharge", timeCharge);
			model.addAttribute("timeChargeList",timeChargeList);
		} catch (Exception e) {
			log.error("获取timeCharge列表失败", e);
			// 返回登录信息错误信息
			return new AccessErrorResult(1000, "error.msg.invalid.parameter")
					.toString();
		}
		log.info("******************获取timeCharge信息列表-end************************");
		// 跳转至banner列表页面
		return "backstage/timeCharge/timeCharge";
	}
	/**
	 * 设置定时充电
	 * @throws ParseException 
	 */
	@RequestMapping("/setTimeCharge")
	@ResponseBody
	public String setTimeCharge(TblTimeCharge timeCharge,HttpServletRequest request) throws IOException, ParseException {
		DwzAjaxResult dwzResult;
		String elpiElectricpilecode[]= request.getParameter("elpiElectricpilecode").split(":");
		String hour = request.getParameter("hour");
		String minute = request.getParameter("minute");
		String status = request.getParameter("status");
		String second = "00";
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String timeStr = hour+":"+minute+":"+second;
		Time chargeTime= new Time(sdf.parse(timeStr).getTime());
		
		
		try {
			for(int i=0;i<elpiElectricpilecode.length;i++){
				int sum =cmsTimeChargeService.findCodeFormTimeCharge(elpiElectricpilecode[i]);
				timeCharge.setElpiElectricpilecode(elpiElectricpilecode[i]);
				timeCharge.setStatus(Integer.parseInt(status));
				timeCharge.setIssuedStatus(0);
				timeCharge.setChargeTime(chargeTime);
					if(sum==0){
						cmsTimeChargeService.insertTimeCharge(timeCharge);
					}else{
						cmsTimeChargeService.updateTimeCharge(timeCharge);
					}
				
			}
			//将桩体编号拼成要发送的格式
			String sendStr = "";
			for(String code : elpiElectricpilecode){
				sendStr += code.trim() + ",";
			}
			if(sendStr.length() > 0){
				sendStr = sendStr.substring(0, sendStr.length() - 1);
				sendStr += ":" + hour+":"+minute+":"+status;
				MessageManager m = new MessageManager();
				String apiBaseUrl = m.getSystemProperties("apiRoot");
				String token = ApiUtil.getToken();
				//调用接口定时充电
				HttpsUtil.getResponseParam(apiBaseUrl + "/app/net/sendTimeCharge.do?paramStrs=" + sendStr + "&t=" + token, "status");
			}
			dwzResult = new DwzAjaxResult("200", "绑定成功", "timeChargeList","","closeCurrent", "");
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			dwzResult = new DwzAjaxResult("300", "保存失败:系统错误", "", "", "");

		}
		return new JsonObject(dwzResult).toString();
	}
}


