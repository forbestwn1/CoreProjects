package com.nosliw.core.application.division.manual.brick.valuestructure;

import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.division.manual.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPExpandable;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.scriptexpression.HAPWithConstantScriptExpression;

//wrapper for value structure
//extra info for value structure, group name
public class HAPManualBrickWrapperValueStructure extends HAPManualDefinitionBrickBlockSimple implements HAPWithConstantScriptExpression, HAPExpandable{

	public static final String NAME = "name";
	public static final String GROUPTYPE = "groupType";
	public static final String INFO = "info";
	public static final String VALUESTRUCTURE = "valueStructure";
	
	private String m_name;

	//group type for value structure (public, private, protected, internal)
	private String m_groupType;
	
	private HAPInfoImpSimple m_info;
	
	public HAPManualBrickWrapperValueStructure() {
		super(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100);
	}

	public HAPManualBrickWrapperValueStructure(HAPManualBrickValueStructure valueStructure) {
		this();
		this.setValueStructure(valueStructure);
	}
	
	public HAPInfo getInfo() {    return (HAPInfo)this.getAttributeValueWithValue(INFO);     }
	public void setInfo(HAPInfoImpSimple info) {   this.setAttributeWithValueValue(INFO, info);     }
	
	public HAPManualBrickValueStructure getValueStructure() {	return (HAPManualBrickValueStructure)this.getAttributeValueWithBrick(VALUESTRUCTURE);   }
	public void setValueStructure(HAPManualBrickValueStructure valueStructure) {   this.setAttributeWithValueBrick(VALUESTRUCTURE, valueStructure);  }
	
	public String getName() {   return (String)this.getAttributeValueWithValue(NAME);   }
	public void setName(String groupName) {   this.setAttributeWithValueValue(NAME, groupName);    }
	
	public String getGroupType() {   return (String)this.getAttributeValueWithValue(GROUPTYPE);    }
	public void setGroupType(String groupType) {    this.setAttributeWithValueValue(GROUPTYPE, groupType);     }

	@Override
	public void discoverConstantScript(HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext, HAPParserDataExpression expressionParser) {
		HAPManualBrickValueStructure valueStructure = (HAPManualBrickValueStructure)parserContext.getGlobalDomain().getEntityInfoDefinition(m_valueStructureId).getAdapter();
		valueStructure.discoverConstantScript(complexEntityId, parserContext, expressionParser);
	}

	@Override
	public void solidateConstantScript(Map<String, String> values) {}

	@Override
	public Object cloneValue() {	return this.cloneValueStructureWrapper();	}

	public HAPManualBrickWrapperValueStructure cloneValueStructureWrapper() {
		HAPManualBrickWrapperValueStructure out = new HAPManualBrickWrapperValueStructure();
		out.m_name = this.m_name;
		out.m_groupType = this.m_groupType;
		out.m_info = this.m_info.cloneInfo();
		return out;
	}
}
