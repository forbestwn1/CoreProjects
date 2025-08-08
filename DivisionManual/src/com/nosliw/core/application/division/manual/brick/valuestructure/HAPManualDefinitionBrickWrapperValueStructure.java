package com.nosliw.core.application.division.manual.brick.valuestructure;

import com.nosliw.core.application.common.structure.HAPInfoStructureInWrapper;
import com.nosliw.core.application.common.structure.HAPValueStructure;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructureDefinition;
import com.nosliw.core.application.division.manual.core.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrickWithEntityInfo;

//wrapper for value structure
//extra info for value structure, group name
public class HAPManualDefinitionBrickWrapperValueStructure extends HAPManualDefinitionBrickWithEntityInfo implements HAPWrapperValueStructureDefinition{

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
	public HAPInfoStructureInWrapper getStructureInfo() {    return (HAPInfoStructureInWrapper)this.getAttributeValueOfValue(VALUESTRUCTUREINFO);    }

	@Override
	public void setStructureInfo(HAPInfoStructureInWrapper info) {   this.setAttributeValueWithValue(VALUESTRUCTUREINFO, info);   }
	
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
