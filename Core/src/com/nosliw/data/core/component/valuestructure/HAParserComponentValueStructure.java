package com.nosliw.data.core.component.valuestructure;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.valuestructure.HAPParserValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAParserComponentValueStructure {

	public static HAPValueStructureInComponent parseComponentValueStructure(JSONObject jsonObj) {
		return parseComponentValueStructure(jsonObj, null);
	}
	
	public static HAPValueStructureInComponent parseComponentValueStructure(JSONObject jsonObj, String typeIfEmpty) {
		
		HAPValueStructureInComponent out = new HAPValueStructureEmptyInComponent();

		HAPValueStructure valueStructure = HAPParserValueStructure.parseValueStructure(jsonObj, typeIfEmpty);
		
		if(valueStructure.getStructureType().equals(HAPConstantShared.STRUCTURE_TYPE_VALUEFLAT)) {
			HAPValueStructureDefinitionFlat flatValueStructure = (HAPValueStructureDefinitionFlat)valueStructure;
			out = new HAPValueStructureFlatInComponent();
			flatValueStructure.cloneToFlatValueStructure((HAPValueStructureFlatInComponent)out);
		}
		else if(valueStructure.getStructureType().equals(HAPConstantShared.STRUCTURE_TYPE_VALUEGROUP)) {
			HAPValueStructureDefinitionGroup groupValueStructure = (HAPValueStructureDefinitionGroup)valueStructure;
			out = new HAPValueStructureGroupInComponent();
			groupValueStructure.cloneToValueStructureDefinitionGroup((HAPValueStructureDefinitionGroup)out);
		}

		return out;
	}
	
}
