package com.nosliw.data.core.component.valuestructure;

import org.json.JSONArray;
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
			HAPValueStructureFlatInComponent flat = new HAPValueStructureFlatInComponent();
			flatValueStructure.cloneToFlatValueStructure(flat);
			
			//parse reference
			JSONArray refArray = jsonObj.optJSONArray(HAPValueStructureInComponent.REFERENCE);
			if(refArray!=null) {
				for(int i=0; i<refArray.length(); i++) {
					flat.addReference(new HAPInfoReference(refArray.get(i)));
				}
			}
			out = flat;
		}
		else if(valueStructure.getStructureType().equals(HAPConstantShared.STRUCTURE_TYPE_VALUEGROUP)) {
			HAPValueStructureDefinitionGroup groupValueStructure = (HAPValueStructureDefinitionGroup)valueStructure;
			HAPValueStructureGroupInComponent group = new HAPValueStructureGroupInComponent();
			groupValueStructure.cloneToGroupValueStructure(group);
			
			//parse reference
			JSONObject refByCategary = jsonObj.optJSONObject(HAPValueStructureDefinitionGroup.GROUP);
			if(refByCategary!=null) {
				for(Object key : refByCategary.keySet()) {
					String categary = (String)key;
					JSONArray refArray = refByCategary.getJSONObject(categary).optJSONArray(HAPValueStructureInComponent.REFERENCE);
					for(int i=0; i<refArray.length(); i++) {
						group.addReference(categary, new HAPInfoReference(refArray.get(i)));
					}
				}
			}
			out = group;
		}

		return out;
	}
	
}
