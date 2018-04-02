package com.wanma.controller;

import com.bluemobi.product.model.common.DwzPagerMySQL;
import com.bluemobi.product.utils.StringUtil;
import com.wanma.common.SessionMgr;
import com.wanma.common.WanmaConstants;
import com.wanma.model.*;
import com.wanma.service.*;
import com.wanma.service.impl.CmsChargOrderServiceImpl;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author gx
 */
@Controller
@RequestMapping("/admin/orderExport")
public class CmsOrderExportController {
    // 日志输出对象
    private static Logger log = Logger
            .getLogger(CmsOrderExportController.class);

    @Autowired
    private CmsChargOrderServiceImpl chargOrderServiceImpl;

    @Autowired
    private CmsChargOrderService tblChargOrderService;

    @Autowired
    private CmsPurchasehistoryService purchasehistoryService;

    @Autowired
    private CmsBespokeService tblBespokeService;

    @Autowired
    private CmsFinanceService cmsFinanceService;

    @Autowired
    private CmsActivityService cmsActivityService;

    @Autowired
    private CmsCouponDetailService cmsCouponDetailService;

    @Autowired
	private ChargeCardService cardService;
    
    @RequestMapping(value = "/pureCharge")
    public void getFirmChargeList(@ModelAttribute("pager") DwzPagerMySQL pager,
                                  @ModelAttribute TblChargingOrder tblChargingOrder, Model model,
                                  HttpServletRequest request, HttpServletResponse res) throws ParseException {

        // 登陆用户
        TblUser loginUser = SessionMgr.getWebUser(request);
        if (loginUser.getUserLevel() == WanmaConstants.USER_LEVEL_BUSINESS_NORMAL) {// 个体商家
            tblChargingOrder.setChorUser(loginUser.getUserId() + "");
        } else if (loginUser.getUserLevel() == WanmaConstants.USER_LEVEL_BUSINESS) {// 纯商家
            tblChargingOrder.setChorUser(loginUser.getUserAccount());
        }

        // 登录充电消费信息
        List<TblChargingOrder> ChargeFirmList = null;

        if (tblChargingOrder == null) {
            // 取得充电消费列表
            ChargeFirmList = tblChargOrderService.getFirmChargeList();
        } else {
        	tblChargingOrder.setIsExport("1");
            tblChargingOrder.setLoginUserId(loginUser.getUserId().toString());
            tblChargingOrder.setUserLevel(loginUser.getUserLevel().toString());
            ChargeFirmList = chargOrderServiceImpl
                    .searchFirmChargeList(tblChargingOrder);
        }
        //计算充电时长
        for (int i = 0; i < ChargeFirmList.size(); i++) {
            String beginChargetime = ChargeFirmList.get(i).getBeginChargetime();
            String endChargetime = ChargeFirmList.get(i).getEndChargetime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date beginDate = sdf.parse(beginChargetime);
            Date endDate = sdf.parse(endChargetime);
            double c = (endDate.getTime() - beginDate.getTime()) / 60000d;
            String minute = String.format("%.2f", c);
            ChargeFirmList.get(i).setChargeTimeMinute(minute);
            multiply(ChargeFirmList.get(i));
        }
        log.info("开始导出纯商家充电信息");
        try {
            exports(res, ChargeFirmList, "纯商家充电收益订单报表.xls", "20");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("导出纯商家充电信息出错，数据写入出错");
        }
    }

    @RequestMapping(value = "/pureBespoke")
    public void getFirmBespokeList(
            @ModelAttribute("pager") DwzPagerMySQL pager,
            @ModelAttribute TblBespoke tblBespoke, Model model,
            HttpServletRequest request, HttpServletResponse res) {

        // 登录预约信息
        List<TblBespoke> firmBespokeList = null;
        // 预约总数
        // long total = 0;
        if (tblBespoke == null) {
            // 取得预约列表
            firmBespokeList = tblBespokeService.getBespokeList();
        } else {
            // 预约总数
            // total =tblBespokeService.searchFirmBespokeCount(tblBespoke);

            // 设置分页对象
            // tblBespoke.setPager(pager);
            // 登陆用户
            TblUser loginUser = SessionMgr.getWebUser(request);
            tblBespoke.setBespUser(loginUser.getUserId().toString());
            tblBespoke.setUserLevel(loginUser.getUserLevel().toString());
            if (("2").equals(tblBespoke.getBespOrderType())) {
                tblBespoke.setBespOrderType("");
                tblBespoke.setBespBespokestatus(7);
            }
            // 取得预约列表
            firmBespokeList = tblBespokeService
                    .searchFirmBespokeList(tblBespoke);
            if (tblBespoke.getBespBespokestatus() != null && tblBespoke.getBespBespokestatus() == 7) {
                tblBespoke.setBespOrderType("2");
            }
        }
        log.info("开始导出纯商家预约信息");
        try {
            exports(res, firmBespokeList, "纯商家预约收益订单报表.xls", "21");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("导出纯商家预约信息出错，数据写入出错");
        }

    }

    @RequestMapping(value = "/UnitCharge")
    public void getUnitChargeList(@ModelAttribute("pager") DwzPagerMySQL pager,
                                  @ModelAttribute TblChargingOrder tblChargingOrder, Model model,
                                  HttpServletRequest request, HttpServletResponse res) {

        // 登录充电消费信息
        List<TblChargingOrder> ChargeUnitList = null;

        if (tblChargingOrder == null) {
            // 取得充电消费列表
            ChargeUnitList = tblChargOrderService.getUnitChargeList();
        } else {
            TblUser loginUser = SessionMgr.getWebUser(request);
            tblChargingOrder.setLoginUserId(loginUser.getUserId().toString());
            tblChargingOrder.setUserLevel(loginUser.getUserLevel().toString());
            // 取得充电消费列表
            ChargeUnitList = tblChargOrderService
                    .searchUnitChargeList(tblChargingOrder);

        }

        log.info("开始导出个体商家充电信息报表");
        try {
            exports(res, ChargeUnitList, "个体商家充电收益订单报表.xls", "30");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("导出个体商家充电信息出错，数据写入出错");
        }

    }

    @RequestMapping(value = "/unitBespoke")
    public void getUnitBespokeList(
            @ModelAttribute("pager") DwzPagerMySQL pager,
            @ModelAttribute TblBespoke tblBespoke, Model model,
            HttpServletRequest request, HttpServletResponse res) {
        // 登陆用户
        TblUser loginUser = SessionMgr.getWebUser(request);
        // 登录预约信息
        List<TblBespoke> unitBespokeList = null;

        if (tblBespoke == null) {
            // 取得预约列表
            unitBespokeList = tblBespokeService.getUnitBespokeList();
        } else {
            if (("2").equals(tblBespoke.getBespOrderType())) {
                tblBespoke.setBespOrderType("");
                tblBespoke.setBespBespokestatus(7);
            }
            // 取得预约列表
            tblBespoke.setBespUser(loginUser.getUserId().toString());
            tblBespoke.setUserLevel(loginUser.getUserLevel().toString());
            unitBespokeList = tblBespokeService
                    .searchUnitBespokeList(tblBespoke);
            if (tblBespoke.getBespBespokestatus() != null && tblBespoke.getBespBespokestatus() == 7) {
                tblBespoke.setBespOrderType("2");
            }
        }
        log.info("开始导出个人商家预约信息");
        try {
            exports(res, unitBespokeList, "个人商家预约收益订单报表.xls", "31");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("导出个人商家预约信息出错，数据写入出错");
        }

    }

    @RequestMapping(value = "/normalBespoke")
    public void getBespokeList(@ModelAttribute("pager") DwzPagerMySQL pager,
                               @ModelAttribute TblBespoke tblBespoke, Model model,
                               HttpServletRequest request, HttpServletResponse res) {
        // 登陆用户
        TblUser loginUser = SessionMgr.getWebUser(request);
        if (loginUser.getUserLevel() == WanmaConstants.USER_LEVEL_BUSINESS_NORMAL) {// 个体商家
            tblBespoke.setBespUser(loginUser.getUserId() + "");
        } else if (loginUser.getUserLevel() == WanmaConstants.USER_LEVEL_BUSINESS) {// 纯商家
            tblBespoke.setBespUser(loginUser.getUserAccount());
        }

        // 登录预约信息
        List<TblBespoke> BespokeList = null;

        if (tblBespoke == null) {
            // 取得预约列表
            BespokeList = tblBespokeService.getBespokeList();
        } else {
            if (("2").equals(tblBespoke.getBespOrderType())) {
                tblBespoke.setBespOrderType("");
                tblBespoke.setBespBespokestatus(7);
            }
            BespokeList = tblBespokeService.searchBespokeList(tblBespoke);
            if (tblBespoke.getBespBespokestatus() != null && tblBespoke.getBespBespokestatus() == 7) {
                tblBespoke.setBespOrderType("2");
            }
        }
        log.info("开始导出普通用户预约信息");
        try {
            exports(res, BespokeList, "普通用户预约消费订单报表.xls", "10");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("导出普通用户预约信息出错，数据写入出错");
        }

    }

    @RequestMapping(value = "/normalCharge")
    public void getChargeList(@ModelAttribute("pager") DwzPagerMySQL pager,
                              @ModelAttribute TblChargingOrder tblChargingOrder, Model model,
                              HttpServletRequest request, HttpServletResponse res) throws ParseException {

        // 登陆用户
        TblUser loginUser = SessionMgr.getWebUser(request);
        if (loginUser.getUserLevel() == WanmaConstants.USER_LEVEL_BUSINESS_NORMAL) {// 个体商家
            tblChargingOrder.setChorUser(loginUser.getUserId() + "");
        } else if (loginUser.getUserLevel() == WanmaConstants.USER_LEVEL_BUSINESS) {// 纯商家
            tblChargingOrder.setChorUser(loginUser.getUserAccount());
        }

        // 登录充电消费信息
        List<TblChargingOrder> ChargeList = null;

        if (tblChargingOrder == null) {
            // 取得充电消费列表
            ChargeList = tblChargOrderService.getChargeList();
        } else {
            
        	tblChargingOrder.setIsExport("1");
            // 取得充电消费列表
            ChargeList = tblChargOrderService
                    .searchChargeList(tblChargingOrder);

        }
        //计算充电时长
        for (int i = 0; i < ChargeList.size(); i++) {
            String beginChargetime = ChargeList.get(i).getBeginChargetime();
            String endChargetime = ChargeList.get(i).getEndChargetime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date beginDate = sdf.parse(beginChargetime);
            Date endDate = sdf.parse(endChargetime);
            double c = (endDate.getTime() - beginDate.getTime()) / 60000d;
            String minute = String.format("%.2f", c);
            ChargeList.get(i).setChargeTimeMinute(minute);
        }
        log.info("开始导出普通用户充电消费订单");
        try {
            exports(res, ChargeList, "普通用户充电消费订单报表.xls", "11");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("导出普通用户充电消费订单出错，数据写入出错");
        }
    }

    @RequestMapping(value = "/payOrder")
    public void getpayList(@ModelAttribute("pager") DwzPagerMySQL pager,
                           @ModelAttribute TblPurchasehistory tblPurchasehistory, Model model,
                           HttpServletRequest request, HttpServletResponse res) {

        // 充值订单信息
        List<TblPurchasehistory> payList = null;
        // 充值订单总数
        //	long total = 0;
        // 消费类型 1-充电消费 2-预约消费 3-购物消费 4-充值
        tblPurchasehistory.setPuhiType(4);
        // 取得充值订单列表
        payList = purchasehistoryService
                .findPurchasehistory(tblPurchasehistory);

        log.info("开始导出充值消费订单");
        try {
            exports(res, payList, "普通用户充值消费订单报表.xls", "01");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("导出充值消费订单出错，数据写入出错");
        }
    }

    @RequestMapping(value = "/invoice")
    public void invoice(@ModelAttribute("pager") DwzPagerMySQL pager,
                        @ModelAttribute TblInvoice TblInvoice, Model model,
                        HttpServletRequest request, HttpServletResponse res) {

        List<TblInvoice> invoiceList = cmsFinanceService
                .getInvoiceManageList_export(TblInvoice);

        log.info("开始导出发票申请列表");
        try {
            exports(res, invoiceList, "发票申请报表.xls", "invoice");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("导出发票申请信息出错，数据写入出错");
        }
    }

    @RequestMapping(value = "/carvin")
    public void carvin(@ModelAttribute("pager") DwzPagerMySQL pager,
                       @ModelAttribute TblCarVin tblCarVin, Model model,
                       HttpServletRequest request, HttpServletResponse res) {

        List<TblCarVin> carvinList = cmsActivityService
                .getCarVinList_export(tblCarVin);

        log.info("开始导出车型优惠列表");
        try {
            exports(res, carvinList, "车型优惠报表.xls", "carvin");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("导出车型优惠信息出错，数据写入出错");
        }
    }

    @RequestMapping(value = "/couponDeatil")
    public void couponDeatil(@ModelAttribute("pager") DwzPagerMySQL pager,
                             @ModelAttribute TblCoupon tblCoupon, Model model,
                             HttpServletRequest request, HttpServletResponse res) {
        Date date = new Date();
        tblCoupon.setCurrentDate(date);
        List<TblCoupon> couponList = cmsCouponDetailService
                .getCouponList_export(tblCoupon);

        log.info("开始导出优惠券明细列表");
        try {
            exports(res, couponList, "优惠券明细报表.xls", "coupon");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("导出优惠券明细列表信息出错，数据写入出错");
        }
    }
    
    @RequestMapping(value = "/cardInfoExport")
    public void cardInfoExport(@ModelAttribute("pager") DwzPagerMySQL pager,
                             @ModelAttribute TblUserCard userCard, Model model,
                             HttpServletRequest request, HttpServletResponse res) {
    	List<TblUserCard> userCardList = cardService.getCardUserList(userCard);

        log.info("开始导出充电卡管理列表");
        try {
            exports(res, userCardList, "充电卡管理列表.xls", "cardInfo");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("导出充电卡管理列表信息出错，数据写入出错");
        }
    }
    protected HSSFCellStyle style;
    public final static int sheetMaxDataCount = 60000;
    protected HSSFCellStyle style2;

    // 导出方法
    public void exports(HttpServletResponse response, Object data,
                        String fileName, String status) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        try {
            // 处理中文名称
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HSSFWorkbook wb = new HSSFWorkbook();
        // 设置单元格格式
        style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        style.setWrapText(true);// 自动换行

        style2 = wb.createCellStyle();
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        style2.setWrapText(true);// 自动换行
        if ("20".equals(status)) {
            makeExcelData20(wb, data);// 填充数据
        } else if ("21".equals(status)) {
            makeExcelData21(wb, data);
        } else if ("30".equals(status)) {
            makeExcelData30(wb, data);
        } else if ("31".equals(status)) {
            makeExcelData31(wb, data);
        } else if ("10".equals(status)) {
            makeExcelData10(wb, data);
        } else if ("11".equals(status)) {
            makeExcelData11(wb, data);
        } else if ("01".equals(status)) {
            makeExcelData01(wb, data);
        } else if ("invoice".equals(status)) {
            makeExcelData_invoice(wb, data);
        } else if ("carvin".equals(status)) {
            makeExcelData_Carvin(wb, data);
        } else if ("coupon".equals(status)) {
            makeExcelData_Coupon(wb, data);
        } else if ("cardInfo".equals(status)){
        	makeExcelData_CardInfo(wb, data);
        }

        writeData(wb, response);// 向页面写数据
    }

    private String[] columns01 = {"用户姓名", "用户手机", "金额", "充值时间", "充值来源"};// 初始化列名

    // 填充数据
    protected void makeExcelData01(HSSFWorkbook wb, Object dataList) {
        @SuppressWarnings("unchecked")
        List<TblPurchasehistory> list = (List<TblPurchasehistory>) dataList;
        double dataCount = list.size();
        double sheetCount = Math.ceil(dataCount / sheetMaxDataCount);
        for (int k = 0; k < sheetCount; k++) {
            HSSFSheet sheet = wb.createSheet("充值消费报表" + k + 1);
            makeHead(sheet, columns01);
            int rowIndex = 0;
            for (int i = k * sheetMaxDataCount; i < (k + 1) * sheetMaxDataCount
                    && i < dataCount; i++) {
                TblPurchasehistory tblPurchase = list.get(i);

                int rowNumUp = ++rowIndex;
                // int rowNumDown = ++rowIndex;
                HSSFRow row0 = sheet.createRow(rowNumUp);
                // HSSFRow row1 = sheet.createRow(rowNumDown);
                HSSFCell cell00 = row0.createCell(0);
                cell00.setCellValue(select(tblPurchase.getUserName(),
                        tblPurchase.getUserPhone()));
                cell00.setCellStyle(style);

                HSSFCell cell01 = row0.createCell(1);
                cell01.setCellValue(StringUtil.nullToEmpty(tblPurchase
                        .getUserPhone()));
                cell01.setCellStyle(style);

                HSSFCell cell02 = row0.createCell(2);
                cell02.setCellValue(StringUtil.nullToEmpty(tblPurchase
                        .getPuhiMonetary()));
                cell02.setCellStyle(style);

                HSSFCell cell03 = row0.createCell(3);
                cell03.setCellValue(dateformat((tblPurchase
                        .getPuhiPurchasehistorytime())));
                cell03.setCellStyle(style);

                HSSFCell cell04 = row0.createCell(4);
                cell04.setCellValue(decode22(StringUtil.nullToEmpty(tblPurchase
                        .getPuhiChargeType())));
                cell04.setCellStyle(style);

            }
        }
    }

    private String[] columns20 = {"订单编号", "桩体编号", "充电点名称", "充电点地址", "企业名称",
            "收益（元）", "电量", "尖电量", "峰电量", "平电量", "谷电量", "充电金额", "尖电费", "峰电费", "平电费", "谷电费", "充电服务费", "充电时间段", "枪口", "订单状态", "卡号", "vin码", "充电时长", "车牌号", "实际优惠金额", "开始soc", "结束soc"};// 初始化列名

    // 填充数据
    protected void makeExcelData20(HSSFWorkbook wb, Object dataList) {
        @SuppressWarnings("unchecked")
        List<TblChargingOrder> list = (List<TblChargingOrder>) dataList;
        double dataCount = list.size();
        double sheetCount = Math.ceil(dataCount / sheetMaxDataCount);
        for (int k = 0; k < sheetCount; k++) {
            HSSFSheet sheet = wb.createSheet("纯商家充电信息报表" + k + 1);
            makeHead(sheet, columns20);
            int rowIndex = 0;
            for (int i = k * sheetMaxDataCount; i < (k + 1) * sheetMaxDataCount
                    && i < dataCount; i++) {
                TblChargingOrder tblCharge = list.get(i);

                int rowNumUp = ++rowIndex;
                // int rowNumDown = ++rowIndex;
                HSSFRow row0 = sheet.createRow(rowNumUp);
                // HSSFRow row1 = sheet.createRow(rowNumDown);
                HSSFDataFormat df = wb.createDataFormat();
                HSSFCell cell00 = row0.createCell(0);
                cell00.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getChorCode()));
                cell00.setCellStyle(style);

                HSSFCell cell01 = row0.createCell(1);
                cell01.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getEleCode()));
                cell01.setCellStyle(style);

                HSSFCell cell02 = row0.createCell(2);
                cell02.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getChargePointName()));
                cell02.setCellStyle(style);

                HSSFCell cell03 = row0.createCell(3);
                cell03.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getChargePointAddress()));
                cell03.setCellStyle(style);

                HSSFCell cell04 = row0.createCell(4);
                cell04.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getComName()));
                cell04.setCellStyle(style);

                HSSFCell cell05 = row0.createCell(5);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell05.setCellStyle(style);
                cell05.setCellValue(Double.parseDouble(tblCharge.getChorMoeny().toString()));


                HSSFCell cell06 = row0.createCell(6);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell06.setCellStyle(style);
                cell06.setCellValue(Double.parseDouble(tblCharge.getChorQuantityelectricity().toString()));

                HSSFCell cell07 = row0.createCell(7);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell07.setCellValue(tblCharge.getChOr_tipPower().doubleValue());
                cell07.setCellStyle(style);

                HSSFCell cell08 = row0.createCell(8);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell08.setCellValue(tblCharge.getChOr_peakPower().doubleValue());
                cell08.setCellStyle(style);

                HSSFCell cell09 = row0.createCell(9);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell09.setCellValue(tblCharge.getChOr_usualPower().doubleValue());
                cell09.setCellStyle(style);

                HSSFCell cell10 = row0.createCell(10);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell10.setCellValue(tblCharge.getChOr_valleyPower().doubleValue());
                cell10.setCellStyle(style);

                HSSFCell cell11 = row0.createCell(11);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell11.setCellStyle(style);
                cell11.setCellValue(Double.parseDouble(tblCharge.getChorChargemoney().toString()));

                HSSFCell cell12 = row0.createCell(12);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell12.setCellValue(tblCharge.getjPriceCount().doubleValue());
                cell12.setCellStyle(style);

                HSSFCell cell13 = row0.createCell(13);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell13.setCellValue(tblCharge.getfPriceCount().doubleValue());
                cell13.setCellStyle(style);

                HSSFCell cell14 = row0.createCell(14);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell14.setCellValue(tblCharge.getpPriceCount().doubleValue());
                cell14.setCellStyle(style);

                HSSFCell cell15 = row0.createCell(15);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell15.setCellValue(tblCharge.getgPriceCount().doubleValue());
                cell15.setCellStyle(style);

                HSSFCell cell16 = row0.createCell(16);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell16.setCellStyle(style);
                cell16.setCellValue(Double.parseDouble(tblCharge.getChorServicemoney().toString()));

                HSSFCell cell17 = row0.createCell(17);
                cell17.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getChorTimequantum()));
                cell17.setCellStyle(style);

                HSSFCell cell18 = row0.createCell(18);
                cell18.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getEleheadName()));
                cell18.setCellStyle(style);

                HSSFCell cell19 = row0.createCell(19);
                cell19.setCellValue(StringUtil.nullToEmpty(decode20(tblCharge
                        .getChorChargingstatus())));
                cell19.setCellStyle(style);

                HSSFCell cell20 = row0.createCell(20);
                cell20.setCellValue(tblCharge.getExterCardNumber());
                cell20.setCellStyle(style);

                HSSFCell cell21 = row0.createCell(21);
                cell21.setCellValue(tblCharge.getVinCode());
                cell21.setCellStyle(style);

                HSSFCell cell22 = row0.createCell(22);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell22.setCellStyle(style);
                cell22.setCellValue(Double.parseDouble(tblCharge.getChargeTimeMinute().toString()));

                HSSFCell cell23 = row0.createCell(23);
                cell23.setCellValue(tblCharge.getCvLicenseNumber());
                cell23.setCellStyle(style);

                HSSFCell cell24 = row0.createCell(24);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell24.setCellValue(Double.parseDouble(tblCharge.getCouponMoney().toString()));
                cell24.setCellStyle(style);

                HSSFCell cell25 = row0.createCell(25);
                style2.setDataFormat(df.getBuiltinFormat("0"));
                cell25.setCellStyle(style2);
                cell25.setCellValue(tblCharge.getStartSoc());


                HSSFCell cell26 = row0.createCell(26);
                style2.setDataFormat(df.getBuiltinFormat("0"));
                cell26.setCellStyle(style2);
                cell26.setCellValue(tblCharge.getEndSoc());

            }
        }
    }

    private String[] columns21 = {"订单编号", "桩体编号", "充电点名称", "充电点地址", "企业名称",
            "收益（元）", "预约开始时间", "预约结束时间", "实际预约结束时间", "订单状态"};// 初始化列名

    // 填充数据
    protected void makeExcelData21(HSSFWorkbook wb, Object dataList) {
        @SuppressWarnings("unchecked")
        List<TblBespoke> list = (List<TblBespoke>) dataList;
        double dataCount = list.size();
        double sheetCount = Math.ceil(dataCount / sheetMaxDataCount);
        for (int k = 0; k < sheetCount; k++) {
            HSSFSheet sheet = wb.createSheet("纯商家预约信息报表" + k + 1);
            makeHead(sheet, columns21);
            int rowIndex = 0;
            for (int i = k * sheetMaxDataCount; i < (k + 1) * sheetMaxDataCount
                    && i < dataCount; i++) {
                TblBespoke tblBespoke = list.get(i);

                int rowNumUp = ++rowIndex;
                // int rowNumDown = ++rowIndex;
                HSSFRow row0 = sheet.createRow(rowNumUp);
                // HSSFRow row1 = sheet.createRow(rowNumDown);
                HSSFCell cell00 = row0.createCell(0);
                cell00.setCellValue(StringUtil.nullToEmpty(tblBespoke
                        .getBespResepaymentcode()));
                cell00.setCellStyle(style);

                HSSFCell cell01 = row0.createCell(1);
                cell01.setCellValue(StringUtil.nullToEmpty(tblBespoke
                        .getEleCode()));
                cell01.setCellStyle(style);

                HSSFCell cell02 = row0.createCell(2);
                cell02.setCellValue(StringUtil.nullToEmpty(tblBespoke
                        .getChargePointName()));
                cell02.setCellStyle(style);

                HSSFCell cell03 = row0.createCell(3);
                cell03.setCellValue(StringUtil.nullToEmpty(tblBespoke
                        .getChargePointAddress()));
                cell03.setCellStyle(style);

                HSSFCell cell04 = row0.createCell(4);
                cell04.setCellValue(StringUtil.nullToEmpty(tblBespoke
                        .getComName()));
                cell04.setCellStyle(style);

                HSSFCell cell05 = row0.createCell(5);
                cell05.setCellValue(StringUtil.nullToEmpty(tblBespoke
                        .getBespBespokeprice()));
                cell05.setCellStyle(style);

                HSSFCell cell06 = row0.createCell(6);
                cell06.setCellValue(dateformat(tblBespoke.getBespBeginTime()));
                cell06.setCellStyle(style);

                HSSFCell cell07 = row0.createCell(7);
                cell07.setCellValue(dateformat(tblBespoke.getBespEndTime()));
                cell07.setCellStyle(style);

                HSSFCell cell08 = row0.createCell(8);
                cell08.setCellValue(dateformat(tblBespoke.getBespRealityTime()));
                cell08.setCellStyle(style);

                HSSFCell cell09 = row0.createCell(9);
                cell09.setCellValue(decode21(tblBespoke.getBespOrderType(), tblBespoke.getBespBespokestatus()));
                cell09.setCellStyle(style);

            }
        }
    }

    private String[] columns30 = {"订单编号", "商家名称", "桩体编号", "充电点名称", "充电点地址",
            "用户姓名", "收益（元）", "电量", "充电金额", "充电服务费", "充电时间段", "订单状态"};// 初始化列名

    protected void makeExcelData30(HSSFWorkbook wb, Object dataList) {
        @SuppressWarnings("unchecked")
        List<TblChargingOrder> list = (List<TblChargingOrder>) dataList;
        double dataCount = list.size();
        double sheetCount = Math.ceil(dataCount / sheetMaxDataCount);
        for (int k = 0; k < sheetCount; k++) {
            HSSFSheet sheet = wb.createSheet("个体商家充电信息报表" + k + 1);
            makeHead(sheet, columns30);
            int rowIndex = 0;
            for (int i = k * sheetMaxDataCount; i < (k + 1) * sheetMaxDataCount
                    && i < dataCount; i++) {
                TblChargingOrder tblCharge = list.get(i);

                int rowNumUp = ++rowIndex;
                // int rowNumDown = ++rowIndex;
                HSSFRow row0 = sheet.createRow(rowNumUp);
                // HSSFRow row1 = sheet.createRow(rowNumDown);
                HSSFCell cell00 = row0.createCell(0);
                cell00.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getChorCode()));
                cell00.setCellStyle(style);
                HSSFCell cell01 = row0.createCell(1);
                cell01.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getComName()));
                cell01.setCellStyle(style);

                HSSFCell cell02 = row0.createCell(2);
                cell02.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getEleCode()));
                cell02.setCellStyle(style);

                HSSFCell cell03 = row0.createCell(3);
                cell03.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getChargePointName()));
                cell03.setCellStyle(style);

                HSSFCell cell04 = row0.createCell(4);
                cell04.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getChargePointAddress()));
                cell04.setCellStyle(style);

                HSSFCell cell05 = row0.createCell(5);
                cell05.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getUsName()));
                cell05.setCellStyle(style);

                HSSFCell cell06 = row0.createCell(6);
                cell06.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getChorMoeny()));
                cell06.setCellStyle(style);

                HSSFCell cell07 = row0.createCell(7);
                cell07.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getChorQuantityelectricity()));
                cell07.setCellStyle(style);

                HSSFCell cell08 = row0.createCell(8);
                cell08.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getChorChargemoney()));
                cell08.setCellStyle(style);

                HSSFCell cell09 = row0.createCell(9);
                cell09.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getChorServicemoney()));
                cell09.setCellStyle(style);

                HSSFCell cell10 = row0.createCell(10);
                cell10.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getChorTimequantum()));
                cell10.setCellStyle(style);

                HSSFCell cell11 = row0.createCell(11);
                cell11.setCellValue(StringUtil.nullToEmpty(decode20(tblCharge
                        .getChorChargingstatus())));
                cell11.setCellStyle(style);

            }
        }
    }

    private String[] columns31 = {"订单编号", "商家名称", "桩体编号", "充电点名称", "充电点地址",
            "收益（元）", "预约开始时间", "实际预约结束时间", "订单状态"};// 初始化列名

    // 填充数据
    protected void makeExcelData31(HSSFWorkbook wb, Object dataList) {
        @SuppressWarnings("unchecked")
        List<TblBespoke> list = (List<TblBespoke>) dataList;
        double dataCount = list.size();
        double sheetCount = Math.ceil(dataCount / sheetMaxDataCount);
        for (int k = 0; k < sheetCount; k++) {
            HSSFSheet sheet = wb.createSheet("个人商家预约信息报表" + k + 1);
            makeHead(sheet, columns31);
            int rowIndex = 0;
            for (int i = k * sheetMaxDataCount; i < (k + 1) * sheetMaxDataCount
                    && i < dataCount; i++) {
                TblBespoke tblBespoke = list.get(i);

                int rowNumUp = ++rowIndex;
                // int rowNumDown = ++rowIndex;
                HSSFRow row0 = sheet.createRow(rowNumUp);
                // HSSFRow row1 = sheet.createRow(rowNumDown);
                HSSFCell cell00 = row0.createCell(0);
                cell00.setCellValue(StringUtil.nullToEmpty(tblBespoke
                        .getBespResepaymentcode()));
                cell00.setCellStyle(style);

                HSSFCell cell01 = row0.createCell(1);
                cell01.setCellValue(StringUtil.nullToEmpty(tblBespoke
                        .getComName()));
                cell01.setCellStyle(style);

                HSSFCell cell02 = row0.createCell(2);
                cell02.setCellValue(StringUtil.nullToEmpty(tblBespoke
                        .getEleCode()));
                cell02.setCellStyle(style);

                HSSFCell cell03 = row0.createCell(3);
                cell03.setCellValue(StringUtil.nullToEmpty(tblBespoke
                        .getChargePointName()));
                cell03.setCellStyle(style);

                HSSFCell cell04 = row0.createCell(4);
                cell04.setCellValue(StringUtil.nullToEmpty(tblBespoke
                        .getChargePointAddress()));
                cell04.setCellStyle(style);

                HSSFCell cell05 = row0.createCell(5);
                cell05.setCellValue(StringUtil.nullToEmpty(tblBespoke
                        .getBespBespokeprice()));
                cell05.setCellStyle(style);

                HSSFCell cell06 = row0.createCell(6);
                cell06.setCellValue(dateformat(tblBespoke.getBespBeginTime()));
                cell06.setCellStyle(style);

                HSSFCell cell07 = row0.createCell(7);
                cell07.setCellValue(dateformat(tblBespoke.getBespRealityTime()));
                cell07.setCellStyle(style);

                HSSFCell cell08 = row0.createCell(8);
                cell08.setCellValue(decode21(tblBespoke.getBespOrderType(), tblBespoke.getBespBespokestatus()));
                cell08.setCellStyle(style);

            }
        }
    }

    private String[] columns10 = {"订单编号", "桩体编号", "电桩地址", "电桩所有权", "用户姓名",
            "用户手机", "预约单价（元）", "金额（元）", "预约开始时间", "预约结束时间", "实际预约结束时间", "订单状态",
            "开票状态"};// 初始化列名

    // 填充数据
    protected void makeExcelData10(HSSFWorkbook wb, Object dataList) {
        @SuppressWarnings("unchecked")
        List<TblBespoke> list = (List<TblBespoke>) dataList;
        double dataCount = list.size();
        double sheetCount = Math.ceil(dataCount / sheetMaxDataCount);
        for (int k = 0; k < sheetCount; k++) {
            HSSFSheet sheet = wb.createSheet("普通用户预约信息报表" + k + 1);
            makeHead(sheet, columns10);
            int rowIndex = 0;
            for (int i = k * sheetMaxDataCount; i < (k + 1) * sheetMaxDataCount
                    && i < dataCount; i++) {
                TblBespoke tblBespoke = list.get(i);

                int rowNumUp = ++rowIndex;
                // int rowNumDown = ++rowIndex;
                HSSFRow row0 = sheet.createRow(rowNumUp);
                // HSSFRow row1 = sheet.createRow(rowNumDown);
                HSSFCell cell00 = row0.createCell(0);
                cell00.setCellValue(StringUtil.nullToEmpty(tblBespoke
                        .getBespResepaymentcode()));
                cell00.setCellStyle(style);

                HSSFCell cell01 = row0.createCell(1);
                cell01.setCellValue(StringUtil.nullToEmpty(tblBespoke
                        .getEleCode()));
                cell01.setCellStyle(style);

                HSSFCell cell02 = row0.createCell(2);
                cell02.setCellValue(StringUtil.nullToEmpty(tblBespoke
                        .getChargePointAddress()));
                cell02.setCellStyle(style);

                HSSFCell cell03 = row0.createCell(3);
                cell03.setCellValue(StringUtil.nullToEmpty(tblBespoke
                        .getOwnerShip()));
                cell03.setCellStyle(style);

                HSSFCell cell04 = row0.createCell(4);
                cell04.setCellValue(select(tblBespoke.getUsername(),
                        tblBespoke.getUserPhone()));
                cell04.setCellStyle(style);

                HSSFCell cell05 = row0.createCell(5);
                cell05.setCellValue(StringUtil.nullToEmpty(tblBespoke
                        .getUserPhone()));
                cell05.setCellStyle(style);

                HSSFCell cell06 = row0.createCell(6);
                cell06.setCellValue(StringUtil.nullToEmpty(tblBespoke
                        .getBespBespokeprice()));
                cell06.setCellStyle(style);

                HSSFCell cell07 = row0.createCell(7);
                cell07.setCellValue(StringUtil.nullToEmpty(tblBespoke
                        .getBespFrozenamt()));
                cell07.setCellStyle(style);

                HSSFCell cell08 = row0.createCell(8);
                cell08.setCellValue(dateformat(tblBespoke.getBespBeginTime()));
                cell08.setCellStyle(style);

                HSSFCell cell09 = row0.createCell(9);
                cell09.setCellValue(dateformat(tblBespoke.getBespEndTime()));
                cell09.setCellStyle(style);

                HSSFCell cell10 = row0.createCell(10);
                cell10.setCellValue(dateformat(tblBespoke.getBespRealityTime()));
                cell10.setCellStyle(style);

                HSSFCell cell11 = row0.createCell(11);
                cell11.setCellValue(decode21(tblBespoke.getBespOrderType(), tblBespoke.getBespBespokestatus()));
                cell11.setCellStyle(style);

                HSSFCell cell12 = row0.createCell(12);
                cell12.setCellValue(decode10(tblBespoke.getPuhiInvoiceStatus()));
                cell12.setCellStyle(style);

            }
        }
    }

    private String[] columns11 = {"订单编号", "桩体编号", "充电点名称", "电桩所有权", "充电卡卡号", "用户手机", "金额（元）",
            "电量", "充电金额", "充电服务费", "消费时间段", "订单状态", "开票状态", "实际优惠金额(元)", "充电时长"};// 初始化列名

    // 填充数据
    protected void makeExcelData11(HSSFWorkbook wb, Object dataList) {
        @SuppressWarnings("unchecked")
        List<TblChargingOrder> list = (List<TblChargingOrder>) dataList;
        double dataCount = list.size();
        double sheetCount = Math.ceil(dataCount / sheetMaxDataCount);
        for (int k = 0; k < sheetCount; k++) {
            HSSFSheet sheet = wb.createSheet("普通用户充电信息报表" + k + 1);
            makeHead(sheet, columns11);
            int rowIndex = 0;
            for (int i = k * sheetMaxDataCount; i < (k + 1) * sheetMaxDataCount
                    && i < dataCount; i++) {
                TblChargingOrder tblCharge = list.get(i);

                int rowNumUp = ++rowIndex;
                // int rowNumDown = ++rowIndex;
                HSSFRow row0 = sheet.createRow(rowNumUp);
                HSSFDataFormat df = wb.createDataFormat();
                // HSSFRow row1 = sheet.createRow(rowNumDown);
                HSSFCell cell00 = row0.createCell(0);
                cell00.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getChorCode()));
                cell00.setCellStyle(style);

                HSSFCell cell01 = row0.createCell(1);
                cell01.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getEleCode()));
                cell01.setCellStyle(style);

                HSSFCell cell02 = row0.createCell(2);
                cell02.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getChargePointName()));
                cell02.setCellStyle(style);

                HSSFCell cell03 = row0.createCell(3);
                cell03.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getOwnerShip()));
                cell03.setCellStyle(style);

			/*	HSSFCell cell04 = row0.createCell(4);
                cell04.setCellValue(select(tblCharge.getUsName(),
						tblCharge.getUserPhone()));
				cell04.setCellStyle(style);*/

                HSSFCell cell04 = row0.createCell(4);
                cell04.setCellValue(tblCharge.getExterCardNumber());
                cell04.setCellStyle(style);

                HSSFCell cell05 = row0.createCell(5);
                cell05.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getUserPhone()));
                cell05.setCellStyle(style);

                HSSFCell cell06 = row0.createCell(6);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell06.setCellStyle(style);
                cell06.setCellValue(Double.parseDouble(tblCharge.getChorMoeny().toString()));

                HSSFCell cell07 = row0.createCell(7);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell07.setCellStyle(style);
                cell07.setCellValue(Double.parseDouble(tblCharge.getChorQuantityelectricity().toString()));

                HSSFCell cell08 = row0.createCell(8);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell08.setCellStyle(style);
                cell08.setCellValue(Double.parseDouble(tblCharge.getChorChargemoney().toString()));

                HSSFCell cell09 = row0.createCell(9);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell09.setCellStyle(style);
                cell09.setCellValue(Double.parseDouble(tblCharge.getChorServicemoney().toString()));

                HSSFCell cell10 = row0.createCell(10);
                cell10.setCellValue(StringUtil.nullToEmpty(tblCharge
                        .getChorTimequantum()));
                cell10.setCellStyle(style);

                HSSFCell cell11 = row0.createCell(11);
                cell11.setCellValue(StringUtil.nullToEmpty(decode20(tblCharge
                        .getChorChargingstatus())));
                cell11.setCellStyle(style);

                HSSFCell cell12 = row0.createCell(12);
                cell12.setCellValue(StringUtil.nullToEmpty(decode10(tblCharge
                        .getPuhiInvoiceStatus())));
                cell12.setCellStyle(style);

                HSSFCell cell13 = row0.createCell(13);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell13.setCellValue(Double.parseDouble(tblCharge.getCouponMoney().toString()));
                cell13.setCellStyle(style);


                HSSFCell cell14 = row0.createCell(14);
                style.setDataFormat(df.getBuiltinFormat("0.0000"));
                cell14.setCellStyle(style);
                cell14.setCellValue(Double.parseDouble(tblCharge.getChargeTimeMinute()));

            }
        }
    }

    private String[] columns_invoice = {"用户手机号", "处理状态", "邮费支付方式", "发票号码",
            "申请日期", "完成日期", "发票类型", "公司抬头", "发票金额", "纳税人识别号", "公司地址", "公司电话", "开户行", "对公账户", "收件人", "收件人号码", "收件人所在地区", "收件人详细地址"};// 初始化列名

    // 填充数据
    protected void makeExcelData_invoice(HSSFWorkbook wb, Object dataList) {
        @SuppressWarnings("unchecked")
        List<TblInvoice> list = (List<TblInvoice>) dataList;
        double dataCount = list.size();
        double sheetCount = Math.ceil(dataCount / sheetMaxDataCount);
        for (int k = 0; k < sheetCount; k++) {
            HSSFSheet sheet = wb.createSheet("发票申请报表" + k + 1);
            makeHead(sheet, columns_invoice);
            int rowIndex = 0;
            for (int i = k * sheetMaxDataCount; i < (k + 1) * sheetMaxDataCount
                    && i < dataCount; i++) {
                TblInvoice tblInvoice2 = list.get(i);

                int rowNumUp = ++rowIndex;
                // int rowNumDown = ++rowIndex;
                HSSFRow row0 = sheet.createRow(rowNumUp);
                // HSSFRow row1 = sheet.createRow(rowNumDown);
                HSSFCell cell00 = row0.createCell(0);
                cell00.setCellValue(tblInvoice2.getIvRecipientsNumber());
                cell00.setCellStyle(style);

                HSSFCell cell01 = row0.createCell(1);
                cell01.setCellValue(select_ivState(tblInvoice2.getIvState()));
                cell01.setCellStyle(style);


                HSSFCell cell02 = row0.createCell(2);
                cell02.setCellValue(select_ivPay(tblInvoice2.getIvPayFreight()));
                cell02.setCellStyle(style);

                HSSFCell cell03 = row0.createCell(3);
                cell03.setCellValue((tblInvoice2.getIvNumber()));
                cell03.setCellStyle(style);

                HSSFCell cell04 = row0.createCell(4);
                cell04.setCellValue(tblInvoice2.getCreateDates());
                cell04.setCellStyle(style);

                HSSFCell cell05 = row0.createCell(5);
                cell05.setCellValue(tblInvoice2.getUpdateDates());
                cell05.setCellStyle(style);

                HSSFCell cell06 = row0.createCell(6);
                cell06.setCellValue(select_ivType(tblInvoice2.getIvType()));
                cell06.setCellStyle(style);

                HSSFCell cell07 = row0.createCell(7);
                cell07.setCellValue(tblInvoice2.getIvCompanyName());
                cell07.setCellStyle(style);

                HSSFCell cell08 = row0.createCell(8);
                cell08.setCellValue(tblInvoice2.getIvInvoiceAmount());
                cell08.setCellStyle(style);


                HSSFCell cell09 = row0.createCell(9);
                cell09.setCellValue(tblInvoice2.getIvTaxpayerNumber());
                cell09.setCellStyle(style);

                HSSFCell cell10 = row0.createCell(10);
                cell10.setCellValue((tblInvoice2.getIvCompanyAddress()));
                cell10.setCellStyle(style);

                HSSFCell cell11 = row0.createCell(11);
                cell11.setCellValue(tblInvoice2.getIvPhoneNumber());
                cell11.setCellStyle(style);

                HSSFCell cell12 = row0.createCell(12);
                cell12.setCellValue(tblInvoice2.getIvBankName());
                cell12.setCellStyle(style);

                HSSFCell cell13 = row0.createCell(13);
                cell13.setCellValue(tblInvoice2.getIvBankAccount());
                cell13.setCellStyle(style);

                HSSFCell cell14 = row0.createCell(14);
                cell14.setCellValue(tblInvoice2.getIvRecipients());
                cell14.setCellStyle(style);

                HSSFCell cell15 = row0.createCell(15);
                cell15.setCellValue(tblInvoice2.getIvRecipientsNumber());
                cell15.setCellStyle(style);


                HSSFCell cell16 = row0.createCell(16);
                cell16.setCellValue(tblInvoice2.getBelongArea());
                cell16.setCellStyle(style);


                HSSFCell cell17 = row0.createCell(17);
                cell17.setCellValue(tblInvoice2.getIvConsigneeAddress());
                cell17.setCellStyle(style);


            }
        }
    }

    private String[] columns_carvin = {"车辆识别码", "合作方", "服务费", "创建时间", "车牌号"};// 初始化列名

    // 填充数据
    protected void makeExcelData_Carvin(HSSFWorkbook wb, Object dataList) {
        @SuppressWarnings("unchecked")
        List<TblCarVin> list = (List<TblCarVin>) dataList;
        double dataCount = list.size();
        double sheetCount = Math.ceil(dataCount / sheetMaxDataCount);
        for (int k = 0; k < sheetCount; k++) {
            HSSFSheet sheet = wb.createSheet("车型优惠报表" + k + 1);
            makeHead(sheet, columns_carvin);
            int rowIndex = 0;
            for (int i = k * sheetMaxDataCount; i < (k + 1) * sheetMaxDataCount
                    && i < dataCount; i++) {
                TblCarVin tblCarVin = list.get(i);

                int rowNumUp = ++rowIndex;
                // int rowNumDown = ++rowIndex;
                HSSFRow row0 = sheet.createRow(rowNumUp);
                // HSSFRow row1 = sheet.createRow(rowNumDown);
                HSSFCell cell00 = row0.createCell(0);
                cell00.setCellValue(tblCarVin.getCvVinCode());
                cell00.setCellStyle(style);

                HSSFCell cell01 = row0.createCell(1);
                cell01.setCellValue(tblCarVin.getCvName());
                cell01.setCellStyle(style);


                HSSFCell cell02 = row0.createCell(2);
                cell02.setCellValue(tblCarVin.getCvServicemoney().toString());
                cell02.setCellStyle(style);

                HSSFCell cell03 = row0.createCell(3);
                cell03.setCellValue(dateformat(tblCarVin.getCvCreatedate()));
                cell03.setCellStyle(style);

                HSSFCell cell04 = row0.createCell(4);
                cell04.setCellValue(tblCarVin.getCvLicenseNumber());
                cell04.setCellStyle(style);


            }
        }
    }

    private String[] columns_cardInfo = {"内卡号", "外卡号", "卡类型", "金额", "用户姓名", "手机号", "公司标识", "公司名称", 
    		"状态", "绑定时间"};// 初始化列名

    // 填充数据
    protected void makeExcelData_CardInfo(HSSFWorkbook wb, Object dataList) {
        @SuppressWarnings("unchecked")
		List<TblUserCard> list =  (List<TblUserCard>) dataList;
        double dataCount = list.size();
        double sheetCount = Math.ceil(dataCount / sheetMaxDataCount);
        for (int k = 0; k < sheetCount; k++) {
            HSSFSheet sheet = wb.createSheet("充电卡管理列表" + k + 1);
            makeHead(sheet, columns_cardInfo);
            int rowIndex = 0;
            for (int i = k * sheetMaxDataCount; i < (k + 1) * sheetMaxDataCount
                    && i < dataCount; i++) {
            	TblUserCard userCard = list.get(i);

                int rowNumUp = ++rowIndex;
                // int rowNumDown = ++rowIndex;
                HSSFRow row0 = sheet.createRow(rowNumUp);
                // HSSFRow row1 = sheet.createRow(rowNumDown);
                HSSFCell cell00 = row0.createCell(0);
                cell00.setCellValue(userCard.getUcInternalCardNumber());
                cell00.setCellStyle(style);

                HSSFCell cell01 = row0.createCell(1);
                cell01.setCellValue(userCard.getUcExternalCardNumber());
                cell01.setCellStyle(style);


                HSSFCell cell02 = row0.createCell(2);
                String payMode=userCard.getUcPayMode().toString();
                if("10".equals(payMode)){
                	cell02.setCellValue("爱充普通公共储蓄卡");
                }
                if("11".equals(payMode)){
                	cell02.setCellValue("爱充普通专属储蓄卡");
                }
                if("12".equals(payMode)){
                	cell02.setCellValue("爱充企业信用卡");
                }
                if("13".equals(payMode)){
                	cell02.setCellValue("爱充合作公共储蓄卡");
                }
                if("14".equals(payMode)){
                	cell02.setCellValue("爱充合作专属储蓄卡");
                }
                cell02.setCellStyle(style);

                HSSFCell cell03 = row0.createCell(3);
                cell03.setCellValue(userCard.getUcBalance());
                cell03.setCellStyle(style);

                HSSFCell cell04 = row0.createCell(4);
                cell04.setCellValue(userCard.getNormRealName());
                cell04.setCellStyle(style);

                HSSFCell cell05 = row0.createCell(5);
                cell05.setCellValue(userCard.getUserAccount());
                cell05.setCellStyle(style);

                HSSFCell cell06 = row0.createCell(6);
                cell06.setCellValue(userCard.getUcCompanyNumber());
                cell06.setCellStyle(style);

                HSSFCell cell07 = row0.createCell(7);
                cell07.setCellValue(userCard.getCpyCompanyname());
                cell07.setCellStyle(style);

                HSSFCell cell08 = row0.createCell(8);
                String us=userCard.getUcStatus().toString();
                if("0".equals(us)){
                	  cell08.setCellValue("正常");
                }
                if("1".equals(us)){
              	  cell08.setCellValue("挂失");
              }
                cell08.setCellStyle(style);

                HSSFCell cell09 = row0.createCell(9);
                cell09.setCellValue(dateformat(userCard.getUcUpdateDate()));
                cell09.setCellStyle(style);

                
            }
        }
    }

    private String[] columns_coupon = {"编号", "现金券品种", "电桩限制", "状态", "获取时间", "使用时间", "生效时间", "到期时间", "优惠码", "用户手机号", "活动名称"};// 初始化列名

    // 填充数据
    protected void makeExcelData_Coupon(HSSFWorkbook wb, Object dataList) {
        @SuppressWarnings("unchecked")
        List<TblCoupon> list = (List<TblCoupon>) dataList;
        double dataCount = list.size();
        double sheetCount = Math.ceil(dataCount / sheetMaxDataCount);
        for (int k = 0; k < sheetCount; k++) {
            HSSFSheet sheet = wb.createSheet("优惠券明细" + k + 1);
            makeHead(sheet, columns_coupon);
            int rowIndex = 0;
            for (int i = k * sheetMaxDataCount; i < (k + 1) * sheetMaxDataCount
                    && i < dataCount; i++) {
                TblCoupon tblCoupon = list.get(i);

                int rowNumUp = ++rowIndex;
                // int rowNumDown = ++rowIndex;
                HSSFRow row0 = sheet.createRow(rowNumUp);
                // HSSFRow row1 = sheet.createRow(rowNumDown);
                HSSFCell cell00 = row0.createCell(0);
                cell00.setCellValue(tblCoupon.getPkCoupon());
                cell00.setCellStyle(style);

                HSSFCell cell01 = row0.createCell(1);
                cell01.setCellValue(tblCoupon.getCovaActivityname());
                cell01.setCellStyle(style);


                HSSFCell cell02 = row0.createCell(2);
                cell02.setCellValue(select_cpLimit(tblCoupon.getCpLimitation()));
                cell02.setCellStyle(style);

                HSSFCell cell03 = row0.createCell(3);
                cell03.setCellValue(select_cpstates(tblCoupon.getCpstates()));
                cell03.setCellStyle(style);

                HSSFCell cell04 = row0.createCell(4);
                cell04.setCellValue(dateformat_forday(tblCoupon.getCpBegindate()));
                cell04.setCellStyle(style);

                HSSFCell cell05 = row0.createCell(5);
                cell05.setCellValue(dateformat(tblCoupon.getCpUpdatedate()));
                cell05.setCellStyle(style);

                HSSFCell cell06 = row0.createCell(6);
                cell06.setCellValue(dateformat_forday(tblCoupon.getCpBegindate()));
                cell06.setCellStyle(style);

                HSSFCell cell07 = row0.createCell(7);
                cell07.setCellValue(dateformat_forday(tblCoupon.getCpEnddate()));
                cell07.setCellStyle(style);

                HSSFCell cell08 = row0.createCell(8);
                cell08.setCellValue(tblCoupon.getCpCouponcode());
                cell08.setCellStyle(style);


                HSSFCell cell09 = row0.createCell(9);
                cell09.setCellValue(tblCoupon.getUserPhone());
                cell09.setCellStyle(style);

                HSSFCell cell10 = row0.createCell(10);
                cell10.setCellValue((tblCoupon.getActActivityname()));
                cell10.setCellStyle(style);
            }
        }
    }
    
    protected void makeHead(HSSFSheet sheet, Object[] columns) {
        HSSFRow row = sheet.createRow((int) 0);
        for (int i = 0; i < columns.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(columns[i].toString());
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i, true);
            sheet.setColumnWidth(i, 3500);// 设置列宽
        }
    }

    private String decode20(String i) {
        if ("1".equals(i)) {
            return "未支付";
        } else if ("2".equals(i)) {
            return "支付成功";
        } else if ("3".equals(i)) {
            return "完成未结算";
        } else {
            return "异常订单";
        }

    }

    private String decode21(String i, Integer m) {
        if (m == 7) {
            return "完成未结算";
        }
        if ("0".equals(i)) {
            return "未支付";
        } else {
            return "订单结束";
        }

    }


    private String decode10(String i) {
        if ("0".equals(i)) {
            return "未开票";
        } else if ("1".equals(i)) {
            return "待开票";
        } else if ("2".equals(i)) {
            return "已开票";
        } else {
            return "-";
        }
    }

    private String decode22(String i) {
        if ("1".equals(i)) {
            return "支付宝";
        } else if ("2".equals(i)) {
            return "微信";
        } else if ("3".equals(i)) {
            return "银联";
        } else if ("4".equals(i)) {
            return "充电卡现金";
        } else if ("5".equals(i)) {
            return "换卡转账";
        } else if ("6".equals(i)) {
            return "7月活动送";
        } else
            return "平台人工充值";

    }

    private String select_ivState(String i) {
        if ("0".equals(i)) {
            return "受理中";
        } else if ("1".equals(i)) {
            return "处理完成";
        } else if ("2".equals(i)) {
            return "未支付";
        } else {
            return "-";
        }

    }


    private String select_ivPay(String i) {
        switch (i) {
            case "0":
                return "账户余额支付";
            case "1":
                return "支付宝支付";
            case "2":
                return "微信支付";
            case "3":
                return "银联支付";
            case "4":
                return "货到付款";
            default:
                return "其他";
        }
    }

    private String select_ivType(String i) {
        switch (i) {
            case "0":
                return "个人发票";
            default:
                return "公司发票";
        }
    }


    private String dateformat(Date i) {
        SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return dateFormater.format(i);
    }

    private String dateformat_forday(Date i) {
        SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyy-MM-dd");
        return dateFormater.format(i);
    }

    private String select(String a, String b) {
        if ("".equals(a) || "" == a) {
            return b;
        } else
            return a;
    }

    private String select_cpLimit(int i) {
        switch (i) {
            case 1:
                return "仅限交流电桩";
            case 2:
                return "仅限直流电装";
            case 3:
                return "不限充电桩 ";
            case 0:
                return "-";
            default:
                return "其他";
        }
    }

    private String select_cpstates(int i) {
        switch (i) {
            case 1:
                return "未兑换未过期";
            case 2:
                return "未兑换已过期";
            case 3:
                return "已兑换已使用 ";
            case 4:
                return "已兑换未使用未过期";
            case 5:
                return "已兑换未使用已过期";
            default:
                return "其他";
        }
    }

    // 向页面写数据
    private void writeData(HSSFWorkbook wb, HttpServletResponse response)
            throws Exception {
        OutputStream ouputStream = null;
        try {
            ouputStream = response.getOutputStream();
            wb.write(ouputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("导出Excel时IO异常");
        } finally {
            try {
                ouputStream.flush();
                ouputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void multiply(TblChargingOrder tblChargingOrder) {
        BigDecimal tipPower = tblChargingOrder.getChOr_tipPower();
        BigDecimal peakPower = tblChargingOrder.getChOr_peakPower();
        BigDecimal usualPower = tblChargingOrder.getChOr_usualPower();
        BigDecimal valleyPower = tblChargingOrder.getChOr_valleyPower();

        BigDecimal jPrice = tblChargingOrder.getChRe_JPrice() == null ? new BigDecimal(0) : tblChargingOrder.getChRe_JPrice();
        BigDecimal fPrice = tblChargingOrder.getChRe_FPrice() == null ? new BigDecimal(0) : tblChargingOrder.getChRe_FPrice();
        BigDecimal pPrice = tblChargingOrder.getChRe_PPrice() == null ? new BigDecimal(0) : tblChargingOrder.getChRe_PPrice();
        BigDecimal gPrice = tblChargingOrder.getChRe_GPrice() == null ? new BigDecimal(0) : tblChargingOrder.getChRe_GPrice();

        BigDecimal jPriceCount = tipPower.multiply(jPrice).setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal fPriceCount = peakPower.multiply(fPrice).setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal pPriceCount = usualPower.multiply(pPrice).setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal gPriceCount = valleyPower.multiply(gPrice).setScale(4, BigDecimal.ROUND_HALF_UP);
        tblChargingOrder.setjPriceCount(jPriceCount);
        tblChargingOrder.setfPriceCount(fPriceCount);
        tblChargingOrder.setpPriceCount(pPriceCount);
        tblChargingOrder.setgPriceCount(gPriceCount);
    }

    public static void main(String[] args) {
        BigDecimal usualPower = new BigDecimal(0.00);
        BigDecimal pPrice = new BigDecimal(1);
        BigDecimal pPriceCount = usualPower.multiply(pPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.println(pPriceCount);

    }
}
