package com.nosliw.core.application.division.manual.brick.valuestructure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.HAPParserStructure;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualContextParse;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPPluginParserBrickImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginParserBrickImpValueStructure extends HAPPluginParserBrickImpSimple{

	public HAPPluginParserBrickImpValueStructure(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.VALUESTRUCTURE_100, HAPManualBrickValueStructure.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentJson(HAPManualBrick entityDefinition, Object jsonValue, HAPManualContextParse parseContext) {
		JSONObject structureJson = (JSONObject)jsonValue;
		if(structureJson!=null) {
			HAPManualBrickValueStructure valueStructure = (HAPManualBrickValueStructure)entityDefinition;
			Object rootsObj = structureJson.opt(HAPManualBrickValueStructure.ROOT);
			if(rootsObj==null) {
				rootsObj = structureJson;
			}
			else {
				valueStructure.setInitValue(structureJson.opt(HAPManualBrickValueStructure.INITVALUE));
			}
			List<HAPManualRootInValueStructure> roots = parseStructureRoots(rootsObj);
			for(HAPManualRootInValueStructure root : roots) {
				valueStructure.addRoot(root);
			}
		}
	}
	
	private List<HAPManualRootInValueStructure> parseStructureRoots(Object rootsObj){
		List<HAPManualRootInValueStructure> out = new ArrayList<HAPManualRootInValueStructure>();
		if(rootsObj instanceof JSONObject) {
			JSONObject elementsJson = (JSONObject)rootsObj;
			Iterator<String> it = elementsJson.keys();
			while(it.hasNext()){
				String eleName = it.next();
				JSONObject eleDefJson = elementsJson.optJSONObject(eleName);
				HAPManualRootInValueStructure root = parseStructureRootFromJson(eleDefJson);
				if(root!=null) {
					root.setName(eleName);
					out.add(root);
				}
			}
		}
		else if(rootsObj instanceof JSONArray) {
			JSONArray elementsArray = (JSONArray)rootsObj;
			for(int i=0; i<elementsArray.length(); i++) {
				JSONObject eleDefJson = elementsArray.getJSONObject(i);
				HAPManualRootInValueStructure root = parseStructureRootFromJson(eleDefJson);
				out.add(root);
			}
		}
		return out;
	}
	
	//parse context root
	private HAPManualRootInValueStructure parseStructureRootFromJson(JSONObject eleDefJson){
		HAPManualRootInValueStructure out = new HAPManualRootInValueStructure();

		//info
		out.buildEntityInfoByJson(eleDefJson);
		if(!HAPUtilityEntityInfo.isEnabled(out)) {
			return null;
		}

		//definition
		JSONObject defJsonObj = eleDefJson.optJSONObject(HAPManualRootInValueStructure.DEFINITION);
		if(defJsonObj!=null) {
			out.setDefinition(HAPParserStructure.parseStructureElement(defJsonObj));
		} else{
			//if no definition, then treat it as data leaf
			out.setDefinition(new HAPElementStructureLeafData());
		}
		return out;
	}
	
}
