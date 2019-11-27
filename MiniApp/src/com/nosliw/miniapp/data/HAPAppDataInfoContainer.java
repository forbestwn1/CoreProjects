package com.nosliw.miniapp.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.miniapp.entity.HAPOwnerInfo;

@HAPEntityWithAttribute
public class HAPAppDataInfoContainer extends HAPSerializableImp{

	@HAPAttribute
	public static final String APPDATAINFOS = "appDataInfos";

	private List<HAPAppDataInfo> m_appDataInfos;
	
	public HAPAppDataInfoContainer() {
		this.m_appDataInfos = new ArrayList<HAPAppDataInfo>();
	}

	public void addData(HAPAppDataInfo data) {	this.m_appDataInfos.add(data);	}
	
	public List<HAPAppDataInfo> getDatas(){  return this.m_appDataInfos;    }

	public Map<HAPOwnerInfo, HAPAppDataInfoContainer> sortByOwnerInfo(){
		Map<HAPOwnerInfo, HAPAppDataInfoContainer> out = new LinkedHashMap<>();
		for(HAPAppDataInfo appDataInfo : this.m_appDataInfos) {
			HAPAppDataInfoContainer byOwner = out.get(appDataInfo.getOwnerInfo());
			if(byOwner==null) {
				byOwner = new HAPAppDataInfoContainer();
				out.put(appDataInfo.getOwnerInfo(), byOwner);
			}
			byOwner.addData(appDataInfo);
		}
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		
		JSONArray infosArray = jsonObj.getJSONArray(APPDATAINFOS);
		for(int i=0; i<infosArray.length(); i++) {
			HAPAppDataInfo appDataInfo = new HAPAppDataInfo();
			appDataInfo.buildObject(infosArray.getJSONObject(i), HAPSerializationFormat.JSON);
			this.m_appDataInfos.add(appDataInfo);
		}
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(APPDATAINFOS, HAPJsonUtility.buildJson(this.m_appDataInfos, HAPSerializationFormat.JSON));
	}
}
