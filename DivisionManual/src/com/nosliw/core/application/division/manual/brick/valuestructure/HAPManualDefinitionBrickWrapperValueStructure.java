package com.nosliw.core.application.division.manual.brick.valuestructure;

import com.nosliw.core.application.common.structure.HAPValueStructure;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructure;
import com.nosliw.core.application.division.manual.core.a.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrickWithEntityInfo;

//wrapper for value structure
//extra info for value structure, group name
public class HAPManualDefinitionBrickWrapperValueStructure extends HAPManualDefinitionBrickWithEntityInfo implements HAPWrapperValueStructure{

	public HAPManualDefinitionBrickWrapperValueStructure() {
		super(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100);
	}

	public HAPManualDefinitionBrickWrapperValueStructure(HAPManualDefinitionBrickValueStructure valueStructure) {
		this();
		this.setValueStructure(valueStructure);
	}
	
	public HAPManualDefinitionBrickValueStructure getValueStructureBlock() {	return (HAPManualDefinitionBrickValueStructure)this.getAttributeValueOfBrick(VALUESTRUCTURE);   }
	public void setValueStructure(HAPManualDefinitionBrickValueStructure valueStructure) {   this.setAttributeValueWithBrick(VALUESTRUCTURE, valueStructure);  }

	@Override
	public HAPValueStructure getValueStructure() {   return this.getValueStructureBlock().getValue();  }

	@Override
	public void setValueStructure(HAPValueStructure valueStructure) {   this.getValueStructureBlock().setValue(valueStructure);  }

	@Override
	public String getGroupType() {   return (String)this.getAttributeValueOfValue(GROUPTYPE);    }
	@Override
	public void setGroupType(String groupType) {    this.setAttributeValueWithValue(GROUPTYPE, groupType);     }

	@Override
	public String getInheritMode() {   return (String)this.getAttributeValueOfValue(INHERITMODE);  }

	@Override
	public void setInheritMode(String mode) {    this.setAttributeValueWithValue(INHERITMODE, mode);  }


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
