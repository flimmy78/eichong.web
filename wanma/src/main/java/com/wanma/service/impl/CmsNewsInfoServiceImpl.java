package com.wanma.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanma.dao.CmsBannerMapper;
import com.wanma.dao.CmsNewsInfoMapper;
import com.wanma.model.TblAdvertisement;
import com.wanma.model.TblBanner;
import com.wanma.model.TblNewsInfo;
import com.wanma.service.CmsBannerService;
import com.wanma.service.CmsCommitLogService;
import com.wanma.service.CmsNewsInfoService;


@Service
public class CmsNewsInfoServiceImpl implements CmsNewsInfoService {
	@Autowired 
	private CmsNewsInfoMapper cmsNewsInfoMapper;
	@Autowired
	CmsCommitLogService commitLogService;
	@Override
	public long getNewsInfoListCount(TblNewsInfo newsInfo) {
		return cmsNewsInfoMapper.getNewsInfoListCount(newsInfo);
	}
	@Override
	public List<TblNewsInfo> getNewsInfoList(TblNewsInfo newsInfo) {
		 return cmsNewsInfoMapper.getNewsInfoList(newsInfo);
	}
	@Override
	public void insertNewsInfo(TblNewsInfo newsInfo) {
		cmsNewsInfoMapper.insertNewsInfo(newsInfo);
		 updateImageInfo(newsInfo);
		
	}
	private void updateImageInfo(TblNewsInfo model) {
		String newsInfoFileId = model.getNewsInfoFileId();
		if (StringUtils.isNotBlank(newsInfoFileId)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("referenceId", model.getNewsInfoId().toString());
			map.put("fileId", newsInfoFileId);
			cmsNewsInfoMapper.updateImageInfo(map);
		}
	}
	@Override
	public TblNewsInfo getNewsInfoById(int newsInfoId) {
		return cmsNewsInfoMapper.getNewsInfoById(newsInfoId);
	}
	@Override
	public void updateNewsInfo(TblNewsInfo newsInfo) {
		cmsNewsInfoMapper.updateNewsInfo(newsInfo);
		 updateImageInfo(newsInfo);
		
	}
	@Override
	public void deleteNewsInfoById(int newsInfoId) {
		cmsNewsInfoMapper.deleteNewsInfoById(newsInfoId);
		
	}
	@Override
	public void downNewsInfoById(int newsInfoId) {
		cmsNewsInfoMapper.downNewsInfoById(newsInfoId);
	}
	
	
}
