package com.nosliw.core.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPPackageBrickInBundle extends HAPSerializableImp{

	@HAPAttribute
	public static final String BRICKID = "brickId";

	@HAPAttribute
	public static final String ADAPATERS = "adapters";

	@HAPAttribute
	public static final String ISADAPTEREXPLICIT = "isAdapterExplicit";

	private HAPIdBrickInBundle m_brickId;
	
    private List<String> m_adapterNames;
    
    //whether to find adapter during runtime
    private boolean m_isAdapterExplicit;
    
    public HAPPackageBrickInBundle() {
    	this.m_adapterNames = new ArrayList<String>();
    	this.m_isAdapterExplicit = false; 
    }
    
    public HAPIdBrickInBundle getBrickId() {   return this.m_brickId;    }
	
    public List<String> getAdapters(){    return this.m_adapterNames;     }
    public void addAdapter(String adapter) {
    	this.m_adapterNames.add(adapter);
    	this.m_isAdapterExplicit = true; 
    }
    
    public boolean isAdapterExplicit() {    return this.m_isAdapterExplicit;      }
    
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		
		JSONObject jsonObj = (JSONObject)json;
		
		JSONObject brickIdJsonObj = jsonObj.optJSONObject(BRICKID);
		if(brickIdJsonObj!=null) {
			JSONArray adapterJsonArray = jsonObj.optJSONArray(ADAPATERS);
			if(adapterJsonArray!=null&&adapterJsonArray.length()>0) {
		    	this.m_isAdapterExplicit = true; 
				for(int i=0; i<adapterJsonArray.length(); i++) {
					this.m_adapterNames.add(adapterJsonArray.getString(i));
				}
			}
		}
		else {
			brickIdJsonObj = jsonObj;
		}
		
		this.m_brickId = new HAPIdBrickInBundle();
		this.m_brickId.buildObject(brickIdJsonObj, HAPSerializationFormat.JSON);
		
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(BRICKID, this.m_brickId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ADAPATERS, HAPManagerSerialize.getInstance().toStringValue(this.m_adapterNames, HAPSerializationFormat.JSON));
		jsonMap.put(ISADAPTEREXPLICIT, this.m_isAdapterExplicit+"");
		typeJsonMap.put(ISADAPTEREXPLICIT, Boolean.class);
	}
	
}
