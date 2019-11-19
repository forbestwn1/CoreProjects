package com.nosliw.miniapp.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.miniapp.HAPDataAccess;
import com.nosliw.miniapp.entity.HAPOwnerInfo;

public class HAPAppDataManagerImp implements HAPAppDataManager{

	private HAPDataAccess m_dataAccess;

	private Map<String, Map<String, List<HAPAppDataHandler>>> m_appDataHandlers;
	
	public HAPAppDataManagerImp(HAPDataAccess dataAccess) {
		this.m_dataAccess = dataAccess;
		this.m_appDataHandlers = new LinkedHashMap<String, Map<String, List<HAPAppDataHandler>>>();
		addSoccerHandler();
	}
	
	@Override
	public HAPMiniAppSettingData getAppData(HAPOwnerInfo ownerInfo) {
		return this.m_dataAccess.getSettingData(ownerInfo);
	}

	@Override
	public HAPMiniAppSettingData getAppData(HAPOwnerInfo ownerInfo, String[] dataNames) {
		return this.m_dataAccess.getSettingData(ownerInfo, dataNames);
	}

	@Override
	public HAPMiniAppSettingData updateAppData(HAPMiniAppSettingData miniAppSettingData) {
		List<HAPAppDataHandler> processores = this.findProcessors(miniAppSettingData.getOwnerInfo());
		if(processores!=null) {
			for(HAPAppDataHandler processor : processores) {
				processor.updateSettingData(miniAppSettingData);
			}
		}
		return this.m_dataAccess.updateSettingData(miniAppSettingData);
	}

	private List<HAPAppDataHandler> findProcessors(HAPOwnerInfo ownerInfo) {
		Map<String, List<HAPAppDataHandler>> processByName = this.m_appDataHandlers.get(ownerInfo.getComponentType());
		if(processByName==null)  return null;
		return processByName.get(ownerInfo.getComponentId());
	}
	
	private void addSoccerHandler() {
		try {
			HAPOwnerInfo ownerInfo = new HAPOwnerInfo(null, "SoccerForFun", "group");
			HAPAppDataHandler appDataProcessor = (HAPAppDataHandler)Class.forName("com.nosliw.service.soccer.HAPAppDataProcessorImp").newInstance();
			this.addHandler(ownerInfo, appDataProcessor);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addHandler(HAPOwnerInfo ownerInfo, HAPAppDataHandler appDataHandler) {
		Map<String, List<HAPAppDataHandler>> handlersByName = this.m_appDataHandlers.get(ownerInfo.getComponentType());
		if(handlersByName==null) {
			handlersByName = new LinkedHashMap<String, List<HAPAppDataHandler>>();
			this.m_appDataHandlers.put(ownerInfo.getComponentType(), handlersByName);
		}
		
		List<HAPAppDataHandler> handlers = handlersByName.get(ownerInfo.getComponentId());
		if(handlers==null) {
			handlers = new ArrayList<HAPAppDataHandler>();
			handlersByName.put(ownerInfo.getComponentId(), handlers);
		}
		handlers.add(appDataHandler);
	}
}
