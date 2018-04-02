package com.wanma.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluemobi.product.model.excel.ExcelParamModel;
import com.bluemobi.product.utils.StringUtil;
import com.wanma.dao.CmsExcelReportMapper;

@Service
public class CmsGtsjBespokeIncomeExcelServiceImpl extends CmsExcelServiceImpl {
	@Autowired
	private CmsExcelReportMapper excelReportMapper;
	private String[] columns = { "个体商家名称", "月份","总预约支付次数","总预约时间(分钟)","预约单价(元/分钟)","消费金额(元)","预约状态","订单支付类型" };//初始化列名

	/**
	 * 数据填充方法实现
	 */
	protected void makeExcelData(HSSFWorkbook wb, Object paramsModel) {
		HSSFSheet sheet = wb.createSheet("sheet1");
		List<Map<String, Object>> list = excelReportMapper
				.queryGtsjBespokeIncomeList((ExcelParamModel)paramsModel);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> data = list.get(i);
			if (i == 0)
				makeHead(sheet, columns);
			HSSFRow row = sheet.createRow(i + 1);
			HSSFCell cell0 = row.createCell(0);
			cell0.setCellValue(StringUtil.nullToEmpty(data.get(columns[0])));
			cell0.setCellStyle(style);
			HSSFCell cell1 = row.createCell(1);
			cell1.setCellValue(StringUtil.nullToEmpty(data.get(columns[1])));
			cell1.setCellStyle(style);
			HSSFCell cell2 = row.createCell(2);
			cell2.setCellValue(StringUtil.nullToEmpty(data.get(columns[2])));
			cell2.setCellStyle(style);
			HSSFCell cell3 = row.createCell(3);
			cell3.setCellValue(StringUtil.nullToEmpty(data.get(columns[3])));
			cell3.setCellStyle(style);
			HSSFCell cell4 = row.createCell(4);
			cell4.setCellValue(StringUtil.nullToEmpty(data.get(columns[4])));
			cell4.setCellStyle(style);
			HSSFCell cell5 = row.createCell(5);
			cell5.setCellValue(StringUtil.nullToEmpty(data.get(columns[5])));
			cell5.setCellStyle(style);
			HSSFCell cell6 = row.createCell(6);
			cell6.setCellValue(StringUtil.nullToEmpty(data.get(columns[6])));
			cell6.setCellStyle(style);
			HSSFCell cell7 = row.createCell(7);
			cell7.setCellValue(StringUtil.nullToEmpty(data.get(columns[7])));
			cell7.setCellStyle(style);
		}
	}
}