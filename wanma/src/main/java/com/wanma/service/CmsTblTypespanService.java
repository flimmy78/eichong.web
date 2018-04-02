package com.wanma.service;

import java.util.List;
import java.util.Map;

import com.wanma.model.TblBomList;
import com.wanma.model.TblElectricpile;
import com.wanma.model.TblEquipmentVersion;
import com.wanma.model.TblTypespan;

public interface CmsTblTypespanService {

	public long getTblTypespanListCount(TblTypespan tblTypespan);

	public List<Map<String, Object>> getTblTypespanList(TblTypespan tblTypespan);

	public void addTypeSpan(TblTypespan tblTypespan);

	public TblTypespan findOne(TblTypespan tblTypespan);

	public List<TblBomList> getBomList(TblTypespan tblTypespanInfo);
	
	public void changeTypeSpan(TblTypespan tblTypespan);

	public String updateEpVision(String pkTypeSpanId, String pkBomListId, String apiBaseUrl) throws Exception;

	public Integer getTypeSpanId(String typeSpan);

	public int selectTsTypeSpan(String tsTypeSpan);

	public List<Map<String, Object>> getPileListById(TblElectricpile electricpile);

	public long getPileListByIdCount(TblElectricpile electricpile);

	public 	List<Map<String,String>> getBomIdUpgrade(String pkEp);

	public List<Map<String, Object>> getCheckUpList(String pkElectricPiles);


}
