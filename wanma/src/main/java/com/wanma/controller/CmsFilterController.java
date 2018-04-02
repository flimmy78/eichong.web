package com.wanma.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluemobi.product.model.common.DwzAjaxResult;
import com.bluemobi.product.model.common.DwzPagerMySQL;
import com.bluemobi.product.utils.AccessErrorResult;
import com.bluemobi.product.utils.JsonObject;
import com.wanma.common.WanmaConstants;
import com.wanma.model.Cooperate;
import com.wanma.model.TblElectricpile;
import com.wanma.model.TblPowerstation;
import com.wanma.service.CmsFilterService;
import com.wanma.service.impl.CmsFilterExportServiceImpl;

@Controller
@RequestMapping("/admin/filter")
public class CmsFilterController {

	// 日志输出对象
	private static Logger logger = Logger.getLogger(CmsFilterController.class);

	@Autowired
	private CmsFilterService cmsFilterService;
	@Autowired
	private CmsFilterExportServiceImpl cmsFilterExportService;

	/**
	 * 联营合作商列表
	 * 
	 * @param pager
	 * @param tblActivity
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listCooperate")
	public String listCooperate(@ModelAttribute("pager") DwzPagerMySQL pager,
			@ModelAttribute Cooperate cooperate, Model model,
			HttpServletRequest request) {

		try {
			logger.info("开始查询联营合作商");
			long total = cmsFilterService.getCooperateCount(cooperate);

			if (total <= pager.getOffset()) {
				pager.setPageNum(1L);
			}
			// 设置查询参数
			cooperate.setPager(pager);
			List<Cooperate> cooperateList = cmsFilterService
					.getCooperateList(cooperate);
			pager.setTotal(total);
			model.addAttribute("pager", pager);
			model.addAttribute("cooperateList", cooperateList);
			model.addAttribute("Cooperate", cooperate);

		} catch (Exception e) {
			logger.error("查询联营合作商失败！");
			// 返回登录信息错误信息
			return new AccessErrorResult(1000, "error.msg.invalid.parameter")
					.toString();
		}
		logger.info("查询联营合作商成功！");
		// 跳转至管理员主页面
		return "backstage/filter/listCooperate";
	}
	/**
	 * 新增合作商跳转
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addCooperate")
	public String addCooperate(Model model, HttpServletRequest request) {
		// 获取优惠券品种
		List<Cooperate> cooperate = null;
		cooperate = cmsFilterService.getCpyCompany();
		model.addAttribute("Cooperate", cooperate);

		return "backstage/filter/addCooperate";
	}
	/**
	 * 
	 * @param Cooperate
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/saveCooperate")
	@ResponseBody
	public String saveCooperate(Cooperate cooperate, HttpSession session,
			HttpServletRequest request) {
		// 处理结果信息
		DwzAjaxResult dwzResult = null;

		try {
			if (cooperate== null) {
				dwzResult = new DwzAjaxResult("300", "请选择要新增的合作商",
						"listCooperate", "closeCurrent", "");
			} 
			else{
				Map<String,String> map =new HashMap<String,String>();
				map.put("pkId", cooperate.getPkCompanyId());
				map.put("status", "1");
				cmsFilterService.ChangeCpyOperate(map);

			dwzResult = new DwzAjaxResult("200", "新增成功", "listCooperate",
					"closeCurrent", "");
			}
		} catch (Exception e) {
			// 出错日志
			logger.error(e.toString() + e.getStackTrace()[0]);
			// 设置错误信息
			dwzResult = new DwzAjaxResult("300", "新增失败", "listCooperate",
					"closeCurrent", "");
		}
		// 返回处理结果信息
		return new JsonObject(dwzResult).toString();
	}
	/**
	 * 删除合作商
	 */
	@RequestMapping(value = "/deleteCooperate")
	@ResponseBody
	public String deleteCooperate(String  id, HttpSession session,
			HttpServletRequest request) {
		// 处理结果信息
		DwzAjaxResult dwzResult = null;

		try {
			if (id== null) {
				dwzResult = new DwzAjaxResult("300", "请选择要删除的合作商",
						"listCooperate", "", "");
			} 
			else{
				Map<String,String> map =new HashMap<String,String>();
				map.put("pkId", id);
				map.put("status", "0");
				
				cmsFilterService.modifyCpyOperate(map);

			dwzResult = new DwzAjaxResult("200", "删除成功", "listCooperate",
					"", "");
			}
		} catch (Exception e) {
			// 出错日志
			logger.error(e.toString() + e.getStackTrace()[0]);
			// 设置错误信息
			dwzResult = new DwzAjaxResult("300", "删除失败", "listCooperate",
					"", "");
		}
		// 返回处理结果信息
		return new JsonObject(dwzResult).toString();
	}
	
	@RequestMapping("/lookPowerStation")
	public String lookPowerStation(@ModelAttribute("pager") DwzPagerMySQL pager,
			String id,TblPowerstation powerStation,Model model, HttpServletRequest request) {
		
		try {
			logger.info("开始查询联营合作商充电点");
			if(id!=null){
			powerStation.setCompanyId(id);
			}
			long total = cmsFilterService.getFilterPwCount(powerStation);

			if (total <= pager.getOffset()) {
				pager.setPageNum(1L);
			}
			// 设置查询参数
			powerStation.setPager(pager);
			List<TblPowerstation> powerList = cmsFilterService
					.getFilterPwList(powerStation);
			pager.setTotal(total);
			model.addAttribute("proviceMap", WanmaConstants.provinceMap);
			model.addAttribute("pager", pager);
			model.addAttribute("powerList", powerList);
			model.addAttribute("TblPowerstation", powerStation);
			


		} catch (Exception e) {
			logger.error("查询联营合作商充电点失败！");
			// 返回登录信息错误信息
			return new AccessErrorResult(1000, "error.msg.invalid.parameter")
					.toString();
		}
		logger.info("查询联营合作商充电点成功！");
		// 跳转至管理员主页面
		return "backstage/filter/listPowerStation";
	}
	
	@RequestMapping(value = "/addPowerStation")
	public String addPowerStation(@ModelAttribute("pager") DwzPagerMySQL pager,
			Model model,TblPowerstation tblPowerstation,String AddCompanyId, HttpServletRequest request) {
		// 获取优惠券品种
		try {
			if(AddCompanyId!=null){
			tblPowerstation.setCompanyId(AddCompanyId);
			}
			long total = cmsFilterService.getAllPwCount(tblPowerstation);

			if (total <= pager.getOffset()) {
				pager.setPageNum(1L);
			}
			// 设置查询参数
			tblPowerstation.setPager(pager);
			List<TblPowerstation> allPwList = cmsFilterService
					.getAllPwList(tblPowerstation);
			pager.setTotal(total);
			model.addAttribute("pager", pager);
			model.addAttribute("allPwList", allPwList);
			model.addAttribute("TblPowerstation", tblPowerstation);
			model.addAttribute("proviceMap", WanmaConstants.provinceMap);
			//model.addAttribute("companyId", companyId);

		} catch (Exception e) {
		e.printStackTrace();
			// 返回登录信息错误信息
			return new AccessErrorResult(1000, "error.msg.invalid.parameter")
					.toString();
		}

		return "backstage/filter/addPowerStation";
	}
	
	@RequestMapping(value = "/savePowerStation")
	@ResponseBody
	public String savePowerStation(@RequestParam("ids") String ids,String companyId) {
		// 处理结果信息
		DwzAjaxResult dwzResult = new DwzAjaxResult("200", "新增成功", "addPowerStation",
				"closeCurrent", "");
		try {
			String[] idArray = ids.split(",");

			cmsFilterService.addPwStation(idArray,companyId);
			// 不出错执行删除操作
			
		} catch (Exception e) {
			// 出错日志
			e.printStackTrace();
			// 设置错误信息
			dwzResult = new DwzAjaxResult("300", "新增失败", "addPowerStation",
					"", "");
		}
		// 返回处理结果信息
		return new JsonObject(dwzResult).toString();
	}
	
	
	@RequestMapping(value = "/removePowerStation")
	@ResponseBody
	public String removePowerStation(@RequestParam("ids") String ids,String RemoveCompanyId) {
		// 处理结果信息
		DwzAjaxResult dwzResult = new DwzAjaxResult("200", "删除成功", "lookPowerStation",
				"", "");
		try {
			String[] idArray = ids.split(",");
			

			cmsFilterService.deletePileByPw(idArray,RemoveCompanyId);
			// 不出错执行删除操作
			
			
		} catch (Exception e) {
			// 出错日志
			e.printStackTrace();
			// 设置错误信息
			dwzResult = new DwzAjaxResult("300", "删除失败", "lookPowerStation",
					"", "");
		}
		// 返回处理结果信息
		return new JsonObject(dwzResult).toString();
	}
	
	@RequestMapping("/lookPile")
	public String lookPile(@ModelAttribute("pager") DwzPagerMySQL pager,
			String LookPileCompanyId,String LookPilePowerId ,TblElectricpile tblElectricpile,Model model, HttpServletRequest request) {
		
		try {
		
			
			if(LookPileCompanyId!=null){
			tblElectricpile.setComPanyId(LookPileCompanyId);
			tblElectricpile.setRelevancePowerStation(Integer.parseInt(LookPilePowerId));
			}
			long total = cmsFilterService.getFilterPileCount(tblElectricpile);

			if (total <= pager.getOffset()) {
				pager.setPageNum(1L);
			}
			// 设置查询参数
			tblElectricpile.setPager(pager);
			List<TblElectricpile> pileList = cmsFilterService
					.getFilterPileList(tblElectricpile);
			pager.setTotal(total);
			model.addAttribute("pager", pager);
			model.addAttribute("pileList", pileList);
			model.addAttribute("tblElectricpile", tblElectricpile);
			/*model.addAttribute("powerId", powerId);
			model.addAttribute("companyId", companyIds);*/
		} catch (Exception e) {
			// 返回登录信息错误信息
			return new AccessErrorResult(1000, "error.msg.invalid.parameter")
					.toString();
		}
		// 跳转至管理员主页面
		return "backstage/filter/listPile";
	}
	
	@RequestMapping(value = "/addPile")
	public String addPile(@ModelAttribute("pager") DwzPagerMySQL pager,
			Model model,TblElectricpile tblElectricpile,String AddPileCompanyId, String AddPilePowerId,HttpServletRequest request) {
		// 获取优惠券品种
		try {
			if(AddPileCompanyId!=null){
			tblElectricpile.setComPanyId(AddPileCompanyId);
			tblElectricpile.setRelevancePowerStation(Integer.parseInt(AddPilePowerId));
			}
			long total = cmsFilterService.getFilterAddPileCount(tblElectricpile);

			if (total <= pager.getOffset()) {
				pager.setPageNum(1L);
			}
			// 设置查询参数
			tblElectricpile.setPager(pager);
			List<TblElectricpile> addPileList = cmsFilterService
					.getFilterAddPileList(tblElectricpile);
			pager.setTotal(total);
			model.addAttribute("pager", pager);
			model.addAttribute("addPileList", addPileList);
			model.addAttribute("tblElectricpile", tblElectricpile);
			/*model.addAttribute("powerId",powerId);
			model.addAttribute("companyId", companyId);*/

		} catch (Exception e) {
		e.printStackTrace();
			// 返回登录信息错误信息
			return new AccessErrorResult(1000, "error.msg.invalid.parameter")
					.toString();
		}

		return "backstage/filter/addPile";
	}
	
	@RequestMapping(value = "/removePile")
	@ResponseBody
	public String removePile(@RequestParam("ids") String ids,String RemovePileCompanyId,String RemovePilePowerId) {
		// 处理结果信息
		DwzAjaxResult dwzResult = new DwzAjaxResult("200", "删除成功", "lookPowerStation",
				"", "");
		try {
			String[] idArray = ids.split(",");
			
			HashMap<String,String> map =new HashMap<String,String>();
		
				for (String id : idArray) {
					map.put("companyId", RemovePileCompanyId);
					map.put("id", id);
					map.put("powerId", RemovePilePowerId);
					cmsFilterService.deletePile(map);
				}
			
			//cmsFilterService.deletePileByPw(idArray,companyId);
			// 不出错执行删除操作
			
			
		} catch (Exception e) {
			// 出错日志
			e.printStackTrace();
			// 设置错误信息
			dwzResult = new DwzAjaxResult("300", "删除失败", "lookPowerStation",
					"", "");
		}
		// 返回处理结果信息
		return new JsonObject(dwzResult).toString();
	}
	
	@RequestMapping(value = "/savePile")
	@ResponseBody
	public String savePile(@RequestParam("ids") String ids,String SaveCompanyId,String SavePowerId) {
		// 处理结果信息
		DwzAjaxResult dwzResult = new DwzAjaxResult("200", "添加成功", "lookPowerStation",
				"", "");
		try {
			String[] idArray = ids.split(",");
			HashMap<String,String> map =new HashMap<String,String>();
			
			map.put("companyId", SaveCompanyId);
			map.put("powerId", SavePowerId);
			cmsFilterService.savePile(idArray,map);
			
			
			//cmsFilterService.deletePileByPw(idArray,companyId);
			// 不出错执行删除操作
			
			
		} catch (Exception e) {
			// 出错日志
			e.printStackTrace();
			// 设置错误信息
			dwzResult = new DwzAjaxResult("300", "添加失败", "lookPowerStation",
					"", "");
		}
		// 返回处理结果信息
		return new JsonObject(dwzResult).toString();
	}
	/**
	 * 导出联营充电桩信息
	 * @param paramModel
	 * @param id
	 * @param req
	 * @param res
	 */
	@RequestMapping(value="/filterExport")
	@ResponseBody
	public void filterExport(
			@RequestParam("companyId") String id,
			@ModelAttribute("paramModel") TblPowerstation paramModel,
			HttpServletRequest req, HttpServletResponse res) {
		logger.info("开始导出充电点电桩信息");
		try {
			id = req.getParameter("companyId");
			paramModel.setCompanyId(id);
			cmsFilterExportService.export(res, paramModel,"联营充电桩信息表.xls");
		} catch (Exception e) {
			e.printStackTrace();
		logger.info("导出充电点电桩信息出错");
		}
	}
	
	
	
}
