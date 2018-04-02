/**     
 * @Title:  CmsCardapplicationformController.java   
 * @Package com.wanma.controller   
 * @Description:    TODO  
 * @author: Android_Robot     
 * @date:   2016年1月14日 下午1:59:12   
 * @version V1.0     
 */
package com.wanma.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluemobi.product.model.common.DwzAjaxResult;
import com.bluemobi.product.model.common.DwzPagerMySQL;
import com.bluemobi.product.utils.AccessSuccessResult;
import com.bluemobi.product.utils.JsonObject;
import com.wanma.model.TblCardapplicationform;
import com.wanma.service.TblCardapplicationformService;

/**
 * @author 充电卡申请
 *
 */
@Controller
@RequestMapping("/admin/cardApply")
public class CmsCardapplicationformController {
	// 日志输出对象
	private static Logger log = Logger
			.getLogger(CmsCardapplicationformController.class);
	@Autowired
	TblCardapplicationformService cardapplicationformService;

	/**
	 * @Title: findCarList
	 * @Description: 获取充电卡申请列表
	 * @param elctrcplscrnconfgurtn
	 * @return
	 */
	@RequestMapping("/findCardApplyList")
	public String findCardApplyList(@ModelAttribute("pager") DwzPagerMySQL pager,
			TblCardapplicationform card, Model model, HttpServletRequest request) {
		log.info("******************获取充电卡申请列表-begin************************");
		long total = cardapplicationformService
				.findCardapplicationformListCount(card);
		if (total <= pager.getOffset()) {
			pager.setPageNum(1L);
		}
		pager.setTotal(total);
		// 设置查询参数
		card.setPager(pager);
		List<TblCardapplicationform> list = cardapplicationformService
				.findCardapplicationformList(card);
		// 将查询结果显示到页面
		model.addAttribute("list", list);
		model.addAttribute("pager", pager);
		model.addAttribute("card", card);
		log.info("******************获取充电卡申请列表-end************************");
		return "backstage/cardApply/listCardApply";
	}
	
	@ResponseBody
	@RequestMapping("/getCardApplyListSelect")
	public String getCardApplyListSelect(TblCardapplicationform card){
		card.setCafStatus(0);
		List<TblCardapplicationform> list = cardapplicationformService
				.findCardapplicationformList(card);
		return new AccessSuccessResult(list).toString();
	}
	
	@ResponseBody
	@RequestMapping("/getCardApplyId")
	public String getCardApplyId(TblCardapplicationform card){
		card.setCafStatus(0);
		String applyId = cardapplicationformService
				.findCardapplicationformId(card);
		return new AccessSuccessResult(applyId).toString();
	}
	
	@ResponseBody
	@RequestMapping("/updateCardStatus")
	public String updateCardStatus(HttpServletRequest request){
		DwzAjaxResult dwzResult = new DwzAjaxResult("200", "取消申请成功", "findCardApplyList","", "");;
		String pkCardAppFromId[]= request.getParameter("pkCardAppFromIds").split(":");
		for (int i = 0; i < pkCardAppFromId.length; i++) {
			cardapplicationformService.updateCardReject(pkCardAppFromId[i]);
			cardapplicationformService.updateApplyCard(pkCardAppFromId[i]);
		}
		return new JsonObject(dwzResult).toString();
	}
}
