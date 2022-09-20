package com.nosliw.data.core.domain.entity.valuestructure;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionLocal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.valuestructure.HAPParserValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPPluginEntityDefinitionInDomainValueStructureComplex2 extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainValueStructureComplex2(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityComplexValueStructure.class, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj,	HAPDomainEntityDefinitionLocal definitionDomain) {
		HAPDefinitionEntityComplexValueStructure valueStructureComplex = (HAPDefinitionEntityComplexValueStructure)this.getEntity(entityId, definitionDomain);

		HAPValueStructureInComplex valueStructureInComplex = new HAPValueStructureInComplexEmpty();

		JSONObject jsonObj = (JSONObject)obj;
		
		HAPValueStructure valueStructure = HAPParserValueStructure.parseValueStructure(jsonObj, null);
		
		if(valueStructure.getStructureType().equals(HAPConstantShared.STRUCTURE_TYPE_VALUEFLAT)) {
			HAPValueStructureDefinitionFlat flatValueStructure = (HAPValueStructureDefinitionFlat)valueStructure;
			HAPValueStructureInComplexFlat flat = new HAPValueStructureInComplexFlat();
			flatValueStructure.cloneToFlatValueStructure(flat);
			
			//parse reference
			if(jsonObj!=null) {
				JSONArray refArray = jsonObj.optJSONArray(HAPValueStructureInComplex.REFERENCE);
				if(refArray!=null) {
					for(int i=0; i<refArray.length(); i++) {
						flat.addReference(new HAPInfoEntityReference(refArray.get(i)));
					}
				}
			}
			valueStructureInComplex = flat;
		}
		else if(valueStructure.getStructureType().equals(HAPConstantShared.STRUCTURE_TYPE_VALUEGROUP)) {
			HAPValueStructureDefinitionGroup groupValueStructure = (HAPValueStructureDefinitionGroup)valueStructure;
			HAPValueStructureInComplexGroup group = new HAPValueStructureInComplexGroup();
			groupValueStructure.cloneToGroupValueStructure(group);
			
			//parse reference
			if(jsonObj!=null) {
				JSONObject refByCategary = jsonObj.optJSONObject(HAPValueStructureDefinitionGroup.GROUP);
				if(refByCategary!=null) {
					for(Object key : refByCategary.keySet()) {
						String categary = (String)key;
						JSONArray refArray = refByCategary.getJSONObject(categary).optJSONArray(HAPValueStructureInComplex.REFERENCE);
						if(refArray!=null) {
							for(int i=0; i<refArray.length(); i++) {
								group.addReference(categary, new HAPInfoEntityReference(refArray.get(i)));
							}
						}
					}
				}
			}
			valueStructureInComplex = group;
		}
		
		HAPUtilityComplexValueStructure.setValueStructureDefault(valueStructureComplex, valueStructureInComplex);

	}

}
