package com.nosliw.data.core.entity.division.manual.valuestructure;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPExpandable;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.expression.data.HAPParserDataExpression;
import com.nosliw.data.core.entity.division.manual.HAPManualInfoValue;
import com.nosliw.data.core.scriptexpression.HAPWithConstantScriptExpression;

//wrapper for value structure
//extra info for value structure, group name
public class HAPDefinitionWrapperValueStructure extends HAPSerializableImp implements HAPWithConstantScriptExpression, HAPExpandable{

	public static final String NAME = "name";
	public static final String GROUPTYPE = "groupType";
	public static final String INFO = "info";
	public static final String VALUESTRUCTURE = "valueStructure";
	
	private String m_name;

	//group type for value structure (public, private, protected, internal)
	private String m_groupType;
	
	private HAPInfoImpSimple m_info;
	
	private HAPManualInfoValue m_valueStructureEntityInfo;
	
	public HAPDefinitionWrapperValueStructure() {
		this.m_info = new HAPInfoImpSimple();
	}

	public HAPDefinitionWrapperValueStructure(HAPManualInfoValue valueStructureEntityInfo) {
		this();
		this.m_valueStructureEntityInfo = valueStructureEntityInfo;
	}
	
	public HAPInfo getInfo() {    return this.m_info;     }
	public void setInfo(HAPInfoImpSimple info) {   this.m_info = info;     }
	
	public HAPDefinitionEntityValueStructure getValueStructureEntity() {	return this.m_valueStructureEntityInfo;	}
	public void setValueStructureEntity(HAPDefinitionEntityValueStructure valueStructureEntity) {   this.m_valueStructureEntityInfo = valueStructureEntity;   }
	
	public String getName() {   return this.m_name;   }
	public void setName(String groupName) {   this.m_name = groupName;    }
	
	public String getGroupType() {   return this.m_groupType;    }
	public void setGroupType(String groupType) {    this.m_groupType = groupType;     }

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
		jsonMap.put(VALUESTRUCTURE, this.m_valueStructureId.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		HAPDomainEntityDefinitionGlobal entityDefDomain = (HAPDomainEntityDefinitionGlobal)entityDomain;
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		this.buildJsonMap(jsonMap, null);
		jsonMap.put(VALUESTRUCTURE, HAPUtilityDomain.getEntityExpandedJsonString(this.m_valueStructureId, entityDefDomain));
		return HAPUtilityJson.buildMapJson(jsonMap);
	}

	@Override
	public Object cloneValue() {	return this.cloneValueStructureWrapper();	}

	public HAPDefinitionWrapperValueStructure cloneValueStructureWrapper() {
		HAPDefinitionWrapperValueStructure out = new HAPDefinitionWrapperValueStructure();
		out.m_name = this.m_name;
		out.m_groupType = this.m_groupType;
		out.m_info = this.m_info.cloneInfo();
		out.m_valueStructureId = this.m_valueStructureId.cloneValue();
		return out;
	}
}
