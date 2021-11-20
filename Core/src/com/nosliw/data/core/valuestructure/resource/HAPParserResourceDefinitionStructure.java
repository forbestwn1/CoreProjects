package com.nosliw.data.core.valuestructure.resource;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.core.complex.valuestructure.HAPInfoEntityReference;
import com.nosliw.data.core.resource.HAPParserResourceEntityImp;
import com.nosliw.data.core.structure.HAPParserStructure;
import com.nosliw.data.core.structure.HAPRootStructure;

public class HAPParserResourceDefinitionStructure extends HAPParserResourceEntityImp{

	@Override
	public HAPResourceDefinitionValueStructure parseJson(JSONObject jsonObj) {	
		HAPResourceDefinitionValueStructure out = new HAPResourceDefinitionValueStructure();
		
		Object rootsObj = jsonObj.get(HAPResourceDefinitionValueStructure.ROOT);
		List<HAPRootStructure> roots = HAPParserStructure.parseRoots(rootsObj);
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
