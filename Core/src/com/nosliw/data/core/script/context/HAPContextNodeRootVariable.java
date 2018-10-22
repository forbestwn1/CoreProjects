package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute(parent="com.nosliw.data.core.script.context.HAPContextNodeRoot")
public abstract class HAPContextNodeRootVariable extends HAPContextNode implements HAPContextNodeRoot{

	@HAPAttribute
	public static final String DEFAULT = "default";

	private HAPEntityInfoImp m_info;
	
	//default value for the root, used in runtime when no value is set
	private Object m_defaultValue;

	public HAPContextNodeRootVariable() {
		this.m_info = new HAPEntityInfoImp();
	}
	
	@Override
	public String getName() {	return this.m_info.getName(); 	}

	@Override
	public String getDescription() {	return this.m_info.getDescription(); 	}

	@Override
	public HAPInfo getInfo() {	return this.m_info.getInfo(); 	}

	@Override
	public void setName(String name) { this.m_info.setName(name);	}

	@Override
	public void setDescription(String description) {  this.m_info.setDescription(description);  }

	@Override
	public void setInfo(HAPInfo info) {   this.m_info.setInfo(info);  }

	@Override
	public void cloneToEntityInfo(HAPEntityInfo entityInfo) {   this.m_info.cloneToEntityInfo(entityInfo); }
	
	
	public Object getDefaultValue(){   return this.m_defaultValue;  }

	public void setDefaultValue(Object defaultValue){		this.m_defaultValue = defaultValue;	}

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
		
		jsonMap.put(HAPEntityInfo.NAME, this.m_info.getName());
		jsonMap.put(HAPEntityInfo.DESCRIPTION, this.m_info.getDescription());
		jsonMap.put(HAPEntityInfo.INFO, HAPJsonUtility.buildJson(this.m_info.getInfo(), HAPSerializationFormat.JSON));

		if(this.m_defaultValue!=null){
			jsonMap.put(DEFAULT, this.m_defaultValue.toString());
			typeJsonMap.put(DEFAULT, this.m_defaultValue.getClass());
		}
	}
	
}
