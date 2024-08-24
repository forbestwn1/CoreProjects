package com.nosliw.data.core.process1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPParserDataAssociation;
import com.nosliw.data.core.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.valuestructure1.HAPValueStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionEmpty;

public abstract class HAPDefinitionActivityBranch extends HAPDefinitionActivity{

	@HAPAttribute
	public static String INPUTMAPPING = "inputMapping";

	@HAPAttribute
	public static String BRANCH = "branch";

	//associate variable in process to input required by activity 
	private HAPDefinitionDataAssociation m_inputMapping;

	private List<HAPDefinitionResultActivityBranch> m_branchs;
	
	public HAPDefinitionActivityBranch(String type) {
		super(type);
		this.m_branchs = new ArrayList<HAPDefinitionResultActivityBranch>();
	}

	public HAPDefinitionDataAssociation getInputMapping() {  return this.m_inputMapping;   }
	public void setInputMapping(HAPDefinitionDataAssociation input) {   this.m_inputMapping = input;   }

	//get input context structure for activity
	//it is for process input mapping
	//param: parent context structure
	public HAPValueStructureInValuePort11111 getInputContextStructure(HAPValueStructureInValuePort11111 parentContextStructure) {  return HAPValueStructureDefinitionEmpty.flatStructure();   }
	
	public List<HAPDefinitionResultActivityBranch> getBranch(){    return this.m_branchs;    }
	
	//if no inputmapping, build default one which is mirror
	protected void buildDefaultInputMapping() {
		if(this.getInputMapping()==null)	this.setInputMapping(new HAPDefinitionDataAssociationMirror());
	}


	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			
			JSONObject inputJson = jsonObj.optJSONObject(INPUTMAPPING);
			if(inputJson!=null) {
				this.m_inputMapping = HAPParserDataAssociation.buildDefinitionByJson(inputJson); 
			}
			
			JSONArray branchJson = jsonObj.optJSONArray(BRANCH);
			if(branchJson!=null) {
				for(int i=0; i<branchJson.length(); i++) {
					HAPDefinitionResultActivityBranch branch = new HAPDefinitionResultActivityBranch();
					branch.buildObject(branchJson.get(i), HAPSerializationFormat.JSON);
					this.m_branchs.add(branch);
				}
			}
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_inputMapping!=null)		jsonMap.put(INPUTMAPPING, this.m_inputMapping.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(BRANCH, HAPUtilityJson.buildJson(this.m_branchs, HAPSerializationFormat.JSON));
	}

	public void cloneToBranchActivityDefinition(HAPDefinitionActivityBranch out) {
		this.cloneToActivityDefinition(out);
		out.m_inputMapping = this.m_inputMapping.cloneDataAssocation();
		for(HAPDefinitionResultActivityBranch branch : this.m_branchs) {
			out.m_branchs.add(branch.cloneBranchActivityResultDefinition());
		}
	}
}
