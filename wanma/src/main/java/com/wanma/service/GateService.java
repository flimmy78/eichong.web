package com.wanma.service;

import java.util.List;

import com.bluemobi.product.model.common.DwzAjaxResult;
import com.wanma.model.TblGateservice;

/**
 * 预约业务处理器
 * 
 *
 */
public interface GateService {
	
	public List<TblGateservice> getGateList();
	public void addGate(TblGateservice tblGateservice);
	public TblGateservice getGateById(int pkGateid);
	public void modifyGate(TblGateservice tblGateservice);
	public DwzAjaxResult  deleteGate(String pkGateid);
	public long findElectricByGateCount(java.lang.Integer pkGateid );
	public boolean checkGateName(String name);
}
