package com.nosliw.data.core.entity.division.manual.valuestructure;

import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPExpandable;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.expression.data.HAPParserDataExpression;
import com.nosliw.data.core.entity.HAPEnumEntityType;
import com.nosliw.data.core.entity.division.manual.HAPManualEntitySimple;
import com.nosliw.data.core.scriptexpression.HAPWithConstantScriptExpression;

//wrapper for value structure
//extra info for value structure, group name
public class HAPDefinitionEntityWrapperValueStructure extends HAPManualEntitySimple implements HAPWithConstantScriptExpression, HAPExpandable{

	public static final String NAME = "name";
	public static final String GROUPTYPE = "groupType";
	public static final String INFO = "info";
	public static final String VALUESTRUCTURE = "valueStructure";
	
	private String m_name;

	//group type for value structure (public, private, protected, internal)
	private String m_groupType;
	
	private HAPInfoImpSimple m_info;
	
	private HAPDefinitionEntityValueStructure m_valueStructure;
	
	public HAPDefinitionEntityWrapperValueStructure() {
		super(HAPEnumEntityType.VALUESTRUCTUREWRAPPER_100);
	}

	public HAPDefinitionEntityWrapperValueStructure(HAPDefinitionEntityValueStructure valueStructure) {
		this();
		this.setValueStructure(valueStructure);
	}
	
	public HAPInfo getInfo() {    return (HAPInfo)this.getAttributeValue(INFO);     }
	public void setInfo(HAPInfoImpSimple info) {   this.setAttributeValue(INFO, info);     }
	
	public HAPDefinitionEntityValueStructure getValueStructure() {	return (HAPDefinitionEntityValueStructure)this.getAttributeEntity(VALUESTRUCTURE);   }
	public void setValueStructure(HAPDefinitionEntityValueStructure valueStructure) {   this.setAttributeEntity(VALUESTRUCTURE, valueStructure);  }
	
	public String getName() {   return (String)this.getAttributeValue(NAME);   }
	public void setName(String groupName) {   this.setAttributeValue(NAME, groupName);    }
	
	public String getGroupType() {   return (String)this.getAttributeValue(GROUPTYPE);    }
	public void setGroupType(String groupType) {    this.setAttributeValue(GROUPTYPE, groupType);     }

	@Override
	public void discoverConstantScript(HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext, HAPParserDataExpression expressionParser) {
		HAPDefinitionEntityValueStructure valueStructure = (HAPDefinitionEntityValueStructure)parserContext.getGlobalDomain().getEntityInfoDefinition(m_valueStructureId).getEntity();
		valueStructure.discoverConstantScript(complexEntityId, parserContext, expressionParser);
	}

	@Override
	public void solidateConstantScript(Map<String, String> values) {}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(GROUPTYPE, this.m_groupType);
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(INFO, HAPUtilityJson.buildJson(this.m_info, HAPSerializationFormat.JSON));
		jsonMap.put(VALUESTRUCTURE, this.m_valueStructure.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	public Object cloneValue() {	return this.cloneValueStructureWrapper();	}

	public HAPDefinitionEntityWrapperValueStructure cloneValueStructureWrapper() {
		HAPDefinitionEntityWrapperValueStructure out = new HAPDefinitionEntityWrapperValueStructure();
		out.m_name = this.m_name;
		out.m_groupType = this.m_groupType;
		out.m_info = this.m_info.cloneInfo();
		return out;
	}
}
