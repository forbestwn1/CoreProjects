package com.nosliw.data.core.service.interfacee;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.displayresource.HAPDisplayResourceNode;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceDefinitionImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

//static information for a service. readable, query for service
//information needed during configuration time
@HAPEntityWithAttribute
public class HAPInfoServiceInterface extends HAPResourceDefinitionImp{

	@HAPAttribute
	public static String TAG = "tag";

	@HAPAttribute
	public static String INTERFACE = "interface";

	@HAPAttribute
	public static String DISPLAY = "display";

	private List<String> m_tags;
	
	private HAPServiceInterface m_serviceInterface;
	
	private HAPDisplayResourceNode m_displayResource;
	
	public HAPInfoServiceInterface() {
		this.m_tags = new ArrayList<String>();
		this.m_serviceInterface = new HAPServiceInterface();
		this.m_displayResource = new HAPDisplayResourceNode();
	}

	@Override
	public String getResourceType() {  return HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE; }

	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.ENTITY;    }

	public HAPServiceInterface getInterface() {  return this.m_serviceInterface;  } 
	
	public List<String> getTags(){   return this.m_tags;    }
	
	public HAPDisplayResourceNode getDisplayResource() {   return this.m_displayResource;     }
	
	public void process(HAPRuntimeEnvironment runtimeEnv) {  this.m_serviceInterface.process(runtimeEnv);	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			super.buildObjectByJson(objJson);
			this.m_serviceInterface = new HAPServiceInterface();
			this.m_serviceInterface.buildObject(objJson.getJSONObject(INTERFACE), HAPSerializationFormat.JSON);
			this.m_tags.clear();
			JSONArray tagArray = objJson.optJSONArray(TAG);
			if(tagArray!=null) {
				for(int i=0; i<tagArray.length(); i++) {
					this.m_tags.add(tagArray.getString(i));
				}
			}
			JSONObject displayResourceObj = objJson.optJSONObject(DISPLAY);
			if(displayResourceObj!=null) {
				this.m_displayResource.buildObject(displayResourceObj, HAPSerializationFormat.JSON);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;  
	}
	
	@Override
	public void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INTERFACE, HAPJsonUtility.buildJson(this.m_serviceInterface, HAPSerializationFormat.JSON));
		jsonMap.put(TAG, HAPJsonUtility.buildJson(this.m_tags, HAPSerializationFormat.JSON));
		jsonMap.put(DISPLAY, HAPJsonUtility.buildJson(this.m_displayResource, HAPSerializationFormat.JSON));
	}
}
