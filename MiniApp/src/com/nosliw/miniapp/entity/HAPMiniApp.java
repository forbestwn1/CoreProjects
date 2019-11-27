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
	public static String CATEGARY = "dataOwnerType";

	private String m_id;
	
	private String m_categary;

	public HAPMiniApp(String id, String name) {
		this(id, name, null);
	}
	
	public HAPMiniApp(String id, String name, String categary) {
		this.m_id = id;
		if(HAPBasicUtility.isStringEmpty(categary))  this.m_categary = HAPConstant.MINIAPP_DATAOWNER_APP;
		else this.m_categary = categary;
		this.setName(name);
	}
	
	public String getCategary() {    return this.m_categary;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
		jsonMap.put(CATEGARY, this.m_categary);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_id = (String)jsonObj.opt(ID);
		this.m_categary = (String)jsonObj.opt(CATEGARY);
		return true;
	}
}
