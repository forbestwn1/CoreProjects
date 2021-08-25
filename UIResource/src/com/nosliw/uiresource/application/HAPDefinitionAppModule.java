package com.nosliw.uiresource.application;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.component.HAPDefinitionEmbededComponent;
import com.nosliw.uiresource.module.HAPDefinitionModuleUI;

@HAPEntityWithAttribute
public class HAPDefinitionAppModule extends HAPDefinitionEmbededComponent{

	@HAPAttribute
	public static final String MODULE = "module";

	@HAPAttribute
	public static final String ROLE = "role";

	@HAPAttribute
	public static String STATUS = "status";
	
	private String m_module;

	private String m_role;
	
	private String m_status;
	
	public HAPDefinitionAppModule() {
	}
	
	public String getModule() {  return this.m_module;   }
	public void setModule(String module) {   this.m_module = module;    }
	
	public String getRole() {   return this.m_role;   }
	public void setRole(String role) {   this.m_role = role;     }
	
	public String getStatus() {   return this.m_status;    }
	public void setStatus(String status) {   this.m_status = status;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(MODULE, this.m_module);
		jsonMap.put(ROLE, this.m_role);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_module = (String)jsonObj.get(MODULE);
		this.m_role = (String)jsonObj.opt(ROLE);
		this.m_status = (String)jsonObj.opt(HAPDefinitionModuleUI.STATUS);
		return true;
	}
	
	public HAPDefinitionAppModule cloneAppModuleDef() {
		HAPDefinitionAppModule out = new HAPDefinitionAppModule();
		this.cloneToEmbededComponent(out);
		out.m_role = this.m_role;
		out.m_module = this.m_module;
		out.m_status = this.m_status;
		return out;
	}
}
