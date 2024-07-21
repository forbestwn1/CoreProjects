package com.nosliw.core.application.division.manual.brick.valuestructure;

import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructure;
import com.nosliw.core.application.division.manual.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPExpandable;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

//wrapper for value structure
//extra info for value structure, group name
public class HAPManualBrickWrapperValueStructure extends HAPManualDefinitionBrickBlockSimple implements HAPWrapperValueStructure, HAPExpandable{

	public HAPManualBrickWrapperValueStructure() {
		super(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100);
	}

	public HAPManualBrickWrapperValueStructure(HAPManualBrickValueStructure valueStructure) {
		this();
		this.setValueStructure(valueStructure);
	}
	
	@Override
	public HAPInfo getInfo() {    return (HAPInfo)this.getAttributeValueWithValue(INFO);     }
	public void setInfo(HAPInfoImpSimple info) {   this.setAttributeWithValueValue(INFO, info);     }
	
	public HAPManualBrickValueStructure getValueStructureBlock() {	return (HAPManualBrickValueStructure)this.getAttributeValueWithBrick(VALUESTRUCTURE);   }
	public void setValueStructure(HAPManualBrickValueStructure valueStructure) {   this.setAttributeWithValueBrick(VALUESTRUCTURE, valueStructure);  }
	
	@Override
	public String getName() {   return (String)this.getAttributeValueWithValue(NAME);   }
	@Override
	public void setName(String groupName) {   this.setAttributeWithValueValue(NAME, groupName);    }
	
	@Override
	public String getGroupType() {   return (String)this.getAttributeValueWithValue(GROUPTYPE);    }
	@Override
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
