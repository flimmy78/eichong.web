package com.wanma.dao;


import java.util.List;
import java.util.Map;

import com.wanma.model.TblPurchasehistory;
import com.wanma.page.Page;

/**
 * 消费记录
 * 消费记录表数据接口
 * 
 * @author xiay
 *
 */
public interface CmsPurchasehistoryMapper {    
    
	/**
	 * 取得消费记录列表
	 * 
	 * @return
	 */
	public List<TblPurchasehistory> findPurchasehistory(TblPurchasehistory tblPurchasehistory);
	
	/**
	 * 取得消费记录总数
	 * 
	 * @return
	 */
	public long findPurchasehistoryCount(TblPurchasehistory tblPurchasehistory);
	


}


