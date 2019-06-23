package com.nosliw.miniapp.entity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

@HAPEntityWithAttribute
public class HAPMiniApp extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String ID = "id";
	
	@HAPAttribute
	public static String DATAOWNERTYPE = "dataOwnerType";

	@HAPAttribute
	public static String DATAOWNERID = "dataOwnerId";
	
	private String m_id;
	
	private String m_dataOwnerType;

	private String m_dataOwnerId;

	public HAPMiniApp(String id, String name) {
		this(id, name, null);
	}
	
	public HAPMiniApp(String id, String name, String dataOwnerType) {
		this.m_id = id;
		this.m_dataOwnerId = id;
		if(HAPBasicUtility.isStringEmpty(dataOwnerType))  this.m_dataOwnerType = HAPConstant.MINIAPP_DATAOWNER_APP;
		else this.m_dataOwnerType = dataOwnerType;
		this.setName(name);
	}
	
	public String getDataOwnerType() {    return this.m_dataOwnerType;  }
	public String getDataOwnerId() {   return this.m_dataOwnerId;   }
	public void setDataOwnerId(String id) {    this.m_dataOwnerId = id;    }
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
		jsonMap.put(DATAOWNERTYPE, this.m_dataOwnerType);
		jsonMap.put(DATAOWNERID, this.m_dataOwnerId);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_id = (String)jsonObj.opt(ID);
		this.m_dataOwnerType = (String)jsonObj.opt(DATAOWNERTYPE);
		return true;
	}
}
