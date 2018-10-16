package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute(parent="com.nosliw.data.core.script.context.HAPContextNodeRoot")
public abstract class HAPContextNodeRootVariable extends HAPContextNode implements HAPContextNodeRoot{

	@HAPAttribute
	public static final String DEFAULT = "default";

	private HAPEntityInfo m_info;
	
	//default value for the root, used in runtime when no value is set
	private Object m_defaultValue;

	public HAPContextNodeRootVariable() {
		this.m_info = new HAPEntityInfo();
	}
	
	@Override
	public HAPEntityInfo getInfo() {	return this.m_info; 	}
	
	public void setInfo(HAPEntityInfo info) {  this.m_info = info;   }
	
	public void setDefaultValue(Object defaultValue){		this.m_defaultValue = defaultValue;	}

	public Object getDefaultValue(){   return this.m_defaultValue;  }

	protected void toSolidContextNode(HAPContextNodeRootVariable solidRootNode, Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv) {
		solidRootNode.m_info = HAPUtilityContext.toSolidEntityInfo(this.m_info, constants, contextProcessorEnv);
		solidRootNode.m_defaultValue = this.m_defaultValue;
		HAPProcessorContextSolidate.buildSolidContextNode(this, solidRootNode, constants, contextProcessorEnv);
	}
	
	protected void toContextNodeRootVariable(HAPContextNodeRootVariable rootNode) {
		rootNode.m_info = this.m_info.clone();
		rootNode.m_defaultValue = this.m_defaultValue;
		this.toContextNode(rootNode);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(INFO, HAPJsonUtility.buildJson(this.m_info, HAPSerializationFormat.JSON));
		if(this.m_defaultValue!=null){
			jsonMap.put(DEFAULT, this.m_defaultValue.toString());
			typeJsonMap.put(DEFAULT, this.m_defaultValue.getClass());
		}
	}
	
}
