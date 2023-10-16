package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.expression.data.HAPParserDataExpression;
import com.nosliw.data.core.scriptexpression.HAPWithConstantScriptExpression;

//wrapper for value structure
//extra info for value structure, group name
public class HAPDefinitionWrapperValueStructure extends HAPSerializableImp implements HAPWithConstantScriptExpression{

	public static final String GROUPNAME = "groupName";
	public static final String GROUPTYPE = "groupType";
	public static final String INFO = "info";
	public static final String VALUESTRUCTURE = "valueStructure";
	
	private String m_groupName;

	//group type for value structure (public, private, protected, internal)
	private String m_groupType;
	
	private HAPInfoImpSimple m_info;
	
	private HAPIdEntityInDomain m_valueStructureId;
	
	public HAPDefinitionWrapperValueStructure() {
		this.m_info = new HAPInfoImpSimple();
	}

	public HAPDefinitionWrapperValueStructure(HAPIdEntityInDomain valueStructureId) {
		this();
		this.m_valueStructureId = valueStructureId;
	}
	
	public HAPInfo getInfo() {    return this.m_info;     }
	public void setInfo(HAPInfoImpSimple info) {   this.m_info = info;     }
	
	public HAPIdEntityInDomain getValueStructureId() {	return this.m_valueStructureId;	}
	public void setValueStructureId(HAPIdEntityInDomain valueStructureId) {   this.m_valueStructureId = valueStructureId;       }
	
	public String getGroupName() {   return this.m_groupName;   }
	public void setGroupName(String groupName) {   this.m_groupName = groupName;    }
	
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
		jsonMap.put(GROUPNAME, this.m_groupName);
		jsonMap.put(INFO, HAPUtilityJson.buildJson(this.m_info, HAPSerializationFormat.JSON));
		jsonMap.put(VALUESTRUCTURE, this.m_valueStructureId.toStringValue(HAPSerializationFormat.JSON));
	}

	public String toExpandedJsonString(HAPDomainEntityDefinitionGlobal entityDefDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		this.buildJsonMap(jsonMap, null);
		jsonMap.put(VALUESTRUCTURE, HAPUtilityDomain.getEntityExpandedJsonString(this.m_valueStructureId, entityDefDomain));
		return HAPUtilityJson.buildMapJson(jsonMap);
	}

	public HAPDefinitionWrapperValueStructure cloneValueStructureWrapper() {
		HAPDefinitionWrapperValueStructure out = new HAPDefinitionWrapperValueStructure();
		out.m_groupName = this.m_groupName;
		out.m_groupType = this.m_groupType;
		out.m_info = this.m_info.cloneInfo();
		out.m_valueStructureId = this.m_valueStructureId.cloneValue();
		return out;
	}

}
