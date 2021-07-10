package com.nosliw.data.core.process1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPParserDataAssociation;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public abstract class HAPExecutableActivityBranch extends HAPExecutableActivity{

	@HAPAttribute
	public static String INPUTMAPPING = "inputMapping";
	
	@HAPAttribute
	public static String BRANCH = "branch";

	private HAPExecutableDataAssociation m_inputMapping;

	private List<HAPExecutableResultActivityBranch> m_branchs;
	
	public HAPExecutableActivityBranch(String id, HAPDefinitionActivity activityDef) {
		super(HAPConstantShared.ACTIVITY_CATEGARY_BRANCH, id, activityDef);
		this.m_branchs = new ArrayList<HAPExecutableResultActivityBranch>();
	}

	public void setInputDataAssociation(HAPExecutableDataAssociation input) {  this.m_inputMapping = input;  }
	public HAPExecutableDataAssociation getInputDataAssociation() {   return this.m_inputMapping;   }
	
//	public HAPDefinitionActivityBranch getBranchActivityDefinition() {   return (HAPDefinitionActivityBranch)this.getActivityDefinition();  }
	
	public List<HAPExecutableResultActivityBranch> getBranchs(){   return this.m_branchs;   }
	public void addBranch(HAPExecutableResultActivityBranch result) {   this.m_branchs.add(result);   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_inputMapping = HAPParserDataAssociation.buildExecutalbeByJson(jsonObj.getJSONObject(INPUTMAPPING));
		JSONObject branchJsonObj = jsonObj.getJSONObject(BRANCH);
		for(Object key : branchJsonObj.keySet()) {
			HAPExecutableResultActivityBranch branch = new HAPExecutableResultActivityBranch();
			branch.buildObject(branchJsonObj.get((String)key), HAPSerializationFormat.JSON);
			this.m_branchs.add(branch);
		}
		return true;  
	}

	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_inputMapping!=null)		jsonMap.put(INPUTMAPPING, this.m_inputMapping.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(BRANCH, HAPJsonUtility.buildJson(this.m_branchs, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		if(this.m_inputMapping!=null)  jsonMap.put(INPUTMAPPING, this.m_inputMapping.toResourceData(runtimeInfo).toString());
	}	
}
