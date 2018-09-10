package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPContextNodeRootInfo extends HAPSerializableImp{

	@HAPAttribute
	public static final String DISPLAYNAME = "name";
	
	@HAPAttribute
	public static final String DESCRIPTION = "description";
	
	@HAPAttribute
	public static final String INFO = "info";
	
	//name of context. it is just for display purpose
	private String m_displayName;
	
	//description of context, it is just for display purpose
	private String m_description;
	
	private HAPInfoImpSimple m_info;

	public HAPContextNodeRootInfo() {
		this.m_info = new HAPInfoImpSimple(); 
	}
	
	public HAPInfo getInfo() {  return this.m_info;  }
	public void setDisplayName(String displayName) {  this.m_displayName = displayName;    }
	public void setDescription(String description) {   this.m_description = description;   }

	public HAPContextNodeRootInfo clone() {
		HAPContextNodeRootInfo out = new HAPContextNodeRootInfo();
		out.m_displayName = this.m_displayName; 
		out.m_description = this.m_description;
		out.m_info = this.m_info.clone();
		return out;
	}
	
	public HAPContextNodeRootInfo toSolidContextNode(Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv) {
		HAPContextNodeRootInfo out = new HAPContextNodeRootInfo();
		out.m_displayName = this.m_displayName; 
		out.m_description = this.m_description;
		out.m_info = this.m_info.clone();
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DISPLAYNAME, this.m_displayName);
		jsonMap.put(DESCRIPTION, this.m_description);
		jsonMap.put(INFO, HAPJsonUtility.buildJson(this.m_info, HAPSerializationFormat.JSON));
	}

}
