package com.nosliw.data.core.valuestructure1.resource;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.core.application.common.structure.HAPParserStructure;
import com.nosliw.core.application.common.structure.HAPRootStructure;
import com.nosliw.data.core.resource.HAPParserResourceEntityImp;

public class HAPParserResourceDefinitionStructure extends HAPParserResourceEntityImp{

	@Override
	public HAPResourceDefinitionValueStructure parseJson(JSONObject jsonObj) {	
		HAPResourceDefinitionValueStructure out = new HAPResourceDefinitionValueStructure();
		
		Object rootsObj = jsonObj.get(HAPResourceDefinitionValueStructure.ROOT);
		List<HAPRootStructure> roots = HAPParserStructure.parseStructureRoots(rootsObj);
		for(HAPRootStructure root : roots)  out.addRoot(root);
		
		JSONArray refArray = jsonObj.optJSONArray(HAPResourceDefinitionValueStructure.REFERENCE);
		if(refArray!=null) {
			for(int i=0; i<refArray.length(); i++) {
				out.addReference(new HAPInfoEntityReference(refArray.get(i)));
			}
		}

		return out;
	}
	
}
