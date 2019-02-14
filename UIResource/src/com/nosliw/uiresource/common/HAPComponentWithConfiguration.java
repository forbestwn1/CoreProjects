package com.nosliw.uiresource.common;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.service.use.HAPDefinitionServiceInEntity;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPWithServiceProvider;
import com.nosliw.uiresource.module.HAPInfoPage;

@HAPEntityWithAttribute
public class HAPComponentWithConfiguration extends HAPEntityInfoWritableImp  implements HAPWithServiceProvider{

	@HAPAttribute
	public static final String PAGEINFO = "pageInfo";
	
	@HAPAttribute
	public static final String SERVICE = "service";

	// all the page required by app (name -- page id)
	private Map<String, HAPInfoPage> m_pagesInfo;

	//service definition
	private HAPDefinitionServiceInEntity m_serviceDefinition;
	
	public HAPComponentWithConfiguration() {
		this.m_serviceDefinition = new HAPDefinitionServiceInEntity();
		this.m_pagesInfo = new LinkedHashMap<String, HAPInfoPage>();
	} 

	public HAPInfoPage getPageInfo(String pageName) {  return this.m_pagesInfo.get(pageName);   }
	public void addPageInfo(HAPInfoPage pageInfo) {   this.m_pagesInfo.put(pageInfo.getName(), pageInfo);   }

	public void setServiceDefinition(HAPDefinitionServiceInEntity serviceDef) {   this.m_serviceDefinition = serviceDef;   }
	public Map<String, HAPDefinitionServiceUse> getServiceUseDefinitions(){  return this.m_serviceDefinition.getServiceUseDefinitions();   }
	@Override
	public Map<String, HAPDefinitionServiceProvider> getServiceProviderDefinitions(){  return this.m_serviceDefinition.getServiceProviderDefinitions();   }
	public void addServiceUseDefinition(HAPDefinitionServiceUse def) {  this.m_serviceDefinition.addServiceUseDefinition(def);   }
	public void addServiceProviderDefinition(HAPDefinitionServiceProvider def) {  this.m_serviceDefinition.addServiceProviderDefinition(def);   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SERVICE, HAPJsonUtility.buildJson(this.m_serviceDefinition, HAPSerializationFormat.JSON));
		jsonMap.put(PAGEINFO, HAPJsonUtility.buildJson(this.m_pagesInfo, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
	
		JSONObject serviceJsonObj = jsonObj.optJSONObject(SERVICE);
		if(serviceJsonObj!=null) this.m_serviceDefinition.buildObject(serviceJsonObj, HAPSerializationFormat.JSON);

		JSONArray pageInfoArray = jsonObj.optJSONArray(PAGEINFO);
		if(pageInfoArray!=null) {
			for(int i=0; i<pageInfoArray.length(); i++) {
				HAPInfoPage pageInfo = new HAPInfoPage();
				pageInfo.buildObject(pageInfoArray.get(i), HAPSerializationFormat.JSON);
				this.addPageInfo(pageInfo);
			}
		}
		return true;
	}

}
