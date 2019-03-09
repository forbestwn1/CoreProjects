package com.nosliw.data.core.script.context.dataassociation;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;

@HAPEntityWithAttribute
public class HAPExecutableDataAssociation extends HAPExecutableImp{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String DEFINITION = "definition";

	@HAPAttribute
	public static String ASSOCIATION = "association";

	private HAPDefinitionDataAssociation m_definition;
	
	private Map<String, HAPExecutableAssociation> m_associations;
	
	private HAPConfigureContextProcessor m_processConfigure;
	
	public HAPExecutableDataAssociation(HAPDefinitionDataAssociation definition) {
		this.m_associations = new LinkedHashMap<String, HAPExecutableAssociation>();
		this.m_definition = definition;
	}
	
	public String getName() {   return this.m_definition.getName();   }
	
	public HAPDefinitionDataAssociation getDefinition() {  return this.m_definition;   }
	public void setDefinition(HAPDefinitionDataAssociation definition) {  this.m_definition = definition;   }
	
	public HAPInfo getInfo() {  return this.m_definition.getInfo();  }
	
	public void addAssociation(String name, HAPExecutableAssociation association) {   this.m_associations.put(name, association);  }
	public HAPExecutableAssociation getAssociation(String name) {return this.m_associations.get(name);  }
	public HAPExecutableAssociation getAssociation() {return this.m_associations.get(HAPConstant.DATAASSOCIATION_RELATEDENTITY_DEFAULT);  }
	public Map<String, HAPExecutableAssociation> getAssociations(){ return m_associations;  }
	
	public HAPConfigureContextProcessor getProcessConfigure() {   return this.m_processConfigure;   }
	public void setProcessConfigure(HAPConfigureContextProcessor processConfigure) {   this.m_processConfigure = processConfigure;    }
	
	public boolean isEmpty() {   return this.m_associations==null || this.m_associations.isEmpty();   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(NAME, this.getName());
		jsonMap.put(DEFINITION, this.m_definition.toStringValue(HAPSerializationFormat.JSON));
		Map<String, String> assocationJsonMap = new LinkedHashMap<String, String>();
		for(String assosName : this.m_associations.keySet()) {
			assocationJsonMap.put(assosName, this.m_associations.get(assosName).toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ASSOCIATION, HAPJsonUtility.buildMapJson(assocationJsonMap));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		Map<String, String> assocationJsonMap = new LinkedHashMap<String, String>();
		for(String assosName : this.m_associations.keySet()) {
			assocationJsonMap.put(assosName, this.m_associations.get(assosName).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(ASSOCIATION, HAPJsonUtility.buildMapJson(assocationJsonMap));
	}

}
