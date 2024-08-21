package com.nosliw.core.application.division.manual.brick.valuestructure;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.core.application.common.structure.HAPValueStructureDefinition;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructure;
import com.nosliw.core.application.division.manual.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;

//wrapper for value structure
//extra info for value structure, group name
public class HAPManualDefinitionBrickWrapperValueStructure extends HAPManualDefinitionBrick implements HAPWrapperValueStructure{

	public HAPManualDefinitionBrickWrapperValueStructure() {
		super(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100);
	}

	public HAPManualDefinitionBrickWrapperValueStructure(HAPManualDefinitionBrickValueStructure valueStructure) {
		this();
		this.setValueStructure(valueStructure);
	}
	
	@Override
	public HAPInfo getInfo() {    return (HAPInfo)this.getAttributeValueWithValue(INFO);     }
	@Override
	public void setInfo(HAPInfo info) {   this.setAttributeWithValueValue(INFO, info);     }
	
	public HAPManualDefinitionBrickValueStructure getValueStructureBlock() {	return (HAPManualDefinitionBrickValueStructure)this.getAttributeValueWithBrick(VALUESTRUCTURE);   }
	public void setValueStructure(HAPManualDefinitionBrickValueStructure valueStructure) {   this.setAttributeWithValueBrick(VALUESTRUCTURE, valueStructure);  }

	@Override
	public HAPValueStructureDefinition getValueStructure() {   return this.getValueStructureBlock().getValue();  }

	@Override
	public void setValueStructure(HAPValueStructureDefinition valueStructure) {   this.getValueStructureBlock().setValue(valueStructure);  }

	@Override
	public String getName() {   return (String)this.getAttributeValueWithValue(NAME);   }
	@Override
	public void setName(String groupName) {   this.setAttributeWithValueValue(NAME, groupName);    }
	
	@Override
	public String getGroupType() {   return (String)this.getAttributeValueWithValue(GROUPTYPE);    }
	@Override
	public void setGroupType(String groupType) {    this.setAttributeWithValueValue(GROUPTYPE, groupType);     }

	@Override
	public Object cloneValue() {	return this.cloneValueStructureWrapper();	}

	public HAPManualDefinitionBrickWrapperValueStructure cloneValueStructureWrapper() {
		HAPManualDefinitionBrickWrapperValueStructure out = new HAPManualDefinitionBrickWrapperValueStructure();
		
		out.m_name = this.m_name;
		out.m_groupType = this.m_groupType;
		out.m_info = this.m_info.cloneInfo();
		return out;
	}

}
