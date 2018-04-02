package com.wanma.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanma.dao.CmsBomListMapper;
import com.wanma.dao.CmsTypespanMapper;
import com.wanma.model.TblBomList;
import com.wanma.model.TblElectricpile;
import com.wanma.model.TblTypespan;
import com.wanma.service.CmsTblTypespanService;
import com.wanma.web.support.utils.ApiUtil;
import com.wanma.web.support.utils.HttpsUtil;

@Service
public class CmsTblTypespanServiceImpl implements CmsTblTypespanService {
	@Autowired
	CmsTypespanMapper cmsTypespanMapper;
	
	@Autowired
	CmsBomListMapper cmsBomListMapper;

	@Override
	public long getTblTypespanListCount(TblTypespan tblTypespan) {
		return cmsTypespanMapper.getTblTypespanListCount(tblTypespan);
	}

	@Override
	public List<Map<String, Object>> getTblTypespanList(TblTypespan tblTypespan) {
		return cmsTypespanMapper.getTblTypespanList(tblTypespan);
	}

	@Override
	public void addTypeSpan(TblTypespan tblTypespan) {
		List<TblBomList> bomList = tblTypespan.getBomList();
		cmsTypespanMapper.tblTypespanInsert(tblTypespan);
		if(bomList != null && bomList.size() > 0){
			for(TblBomList bomListInfo:bomList){
				bomListInfo.setPkTypeSpanId(tblTypespan.getPkTypeSpanId());
				cmsBomListMapper.tblBomListInsert(bomListInfo);
			}
		}
	}

	@Override
	public TblTypespan findOne(TblTypespan tblTypespan) {
		return cmsTypespanMapper.findOne(tblTypespan);
	}

	@Override
	public List<TblBomList> getBomList(TblTypespan tblTypespanInfo) {
		return cmsBomListMapper.getTblBomList(tblTypespanInfo);
	}

	@Override
	public void changeTypeSpan(TblTypespan tblTypespan) {
		cmsTypespanMapper.tblTypespanUpdate(tblTypespan);
		List<TblBomList> bomList = tblTypespan.getBomList();
		if(bomList != null && bomList.size() > 0){
			for(TblBomList bomListInfo:bomList){
				if(bomListInfo.getPkBomListId() != null){
					cmsBomListMapper.tblBomListUpdate(bomListInfo);
				}else{
					bomListInfo.setPkTypeSpanId(tblTypespan.getPkTypeSpanId());
					cmsBomListMapper.tblBomListInsert(bomListInfo);
				}
			}
		}
	}
	
	@Override
	public String updateEpVision(String pkTypeSpanId,String pkBomListId, String apiBaseUrl) throws Exception {
		TblTypespan tblTypespan = new TblTypespan();
		tblTypespan.setPkTypeSpanId(Integer.valueOf(pkTypeSpanId));
		List<TblBomList> bomList = getBomList(tblTypespan);
		String bomListStr = "";
		if(bomList != null && bomList.size() > 0){
			for(TblBomList bomInfo:bomList){
				if(bomInfo.getPkBomListId().toString().equals(pkBomListId)){
					bomListStr = bomInfo.getBlHardwareNumber()+";"
						    +bomInfo.getBlHardwareVersion().replace("V", "").replace("v", "")+";"
						    +bomInfo.getBlFirmwareNumber()+";"
							+bomInfo.getBlFirmwareVersion().replace("V", "").replace("v", "")+";"
							+bomInfo.getBlForceUpdate()+";"
							+bomInfo.getBlFileMd5()+";|";
				}
			}
			bomListStr = bomListStr.substring(0, bomListStr.length() - 1);
			//调用接口更新费率
			return HttpsUtil.getResponseParam(apiBaseUrl + "/app/net/updateEpVersion.do?pId=" + pkTypeSpanId + "&bomStrs=" + bomListStr+ "&t=" + ApiUtil.getToken(), "status");
		}else{
			TblTypespan tblTypespanInfo = findOne(tblTypespan);
			throw new Exception(tblTypespanInfo.getTsModelName()+"无BOM清单");
		}
		
	}

	@Override
	public Integer getTypeSpanId(String typeSpan) {
		return cmsTypespanMapper.getTypeSpanId(typeSpan);
	}

	@Override
	public int selectTsTypeSpan(String tsTypeSpan) {
		return cmsTypespanMapper.selectTsTypeSpan(tsTypeSpan);
	}

	@Override
	public List<Map<String, Object>> getPileListById(TblElectricpile electricpile) {
		return cmsTypespanMapper.getPileListById(electricpile);
	}

	@Override
	public long getPileListByIdCount(TblElectricpile electricpile) {
		return cmsTypespanMapper.getPileListByIdCount(electricpile);
	}

	@Override
	public 	List<Map<String,String>> getBomIdUpgrade(String pkEp) {
		return cmsTypespanMapper.getBomIdUpgrade(pkEp);
	}

	@Override
	public List<Map<String, Object>> getCheckUpList(String item) {
		return cmsTypespanMapper.getCheckUpList(item);
	}



}
