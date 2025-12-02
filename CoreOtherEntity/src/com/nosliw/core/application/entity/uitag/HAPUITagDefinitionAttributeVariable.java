package com.nosliw.core.application.entity.uitag;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.datadefinition.HAPDataDefinitionWritable;

public class HAPUITagDefinitionAttributeVariable extends HAPUITagDefinitionAttribute{

	@HAPAttribute
	public static final String DATADEFINITION = "dataDefinition";

	@HAPAttribute
	public static final String SCOPE = "scope";

	private HAPDataDefinitionWritable m_dataDefinition;
	
	private String m_scope;
	
	public HAPUITagDefinitionAttributeVariable() {
		super(HAPConstantShared.UITAGDEFINITION_ATTRIBUTETYPE_VARIABLE);
	}
	
	public HAPDataDefinitionWritable getDataDefinition() {	return this.m_dataDefinition;	}
	public void setDataDefinition(HAPDataDefinitionWritable dataDef) {   this.m_dataDefinition = dataDef;      }

	public String getScope() {    return this.m_scope;     }
	public void setScope(String scope) {    this.m_scope = scope;      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	
}
