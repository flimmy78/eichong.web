/** 
 * FileName UserService.java
 * 
 * Version 1.0
 *
 * Create by yangwr 2014/6/9
 * 
 * Copyright 2000-2001 Bluemobi. All Rights Reserved.
 */

package com.wanma.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanma.dao.CmsEquipmentVersionMapper;
import com.wanma.model.TblEquipmentVersion;
import com.wanma.service.CmsEquipmentVersionService;

/**
 * FileName UserService.java
 * 
 * Version 1.0
 * 
 * Create by mb 2017/5/26
 * 
 * 设备版本信息实现
 */
@Service
public class CmsEquipmentVersionServiceImpl implements CmsEquipmentVersionService {
	@Autowired
	CmsEquipmentVersionMapper equipmentVersionMapper;
	
	@Override
	public List<TblEquipmentVersion> getVersionByProductID(int pkElectricpile) {
		return equipmentVersionMapper.getVersionByProductID(pkElectricpile);
	}

	@Override
	public void deleteByProductID(String evProductID) {
		 equipmentVersionMapper.deleteByProductID(evProductID);
	}

	@Override
	public TblEquipmentVersion getBomById(String pkBomListId) {
		return  equipmentVersionMapper.getBomById(pkBomListId);
	}

	@Override
	public void insertEpVersion(TblEquipmentVersion epVersion) {
		equipmentVersionMapper.insertEpVersion(epVersion);
		
	}
	
}
