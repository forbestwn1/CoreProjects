package com.nosliw.data.core.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;

public abstract class HAPExecutableActivityBranch extends HAPExecutableActivity{

	@HAPAttribute
	public static String INPUTMAPPING = "inputMapping";
	
	@HAPAttribute
	public static String BRANCH = "branch";

	private HAPExecutableDataAssociation m_inputMapping;

	private List<HAPExecutableResultActivityBranch> m_branchs;
	
	public HAPExecutableActivityBranch(String id, HAPDefinitionActivity activityDef) {
		super(HAPConstant.ACTIVITY_CATEGARY_BRANCH, id, activityDef);
		this.m_branchs = new ArrayList<HAPExecutableResultActivityBranch>();
	}

	public void setInputDataAssociation(HAPExecutableDataAssociation input) {  this.m_inputMapping = input;  }
	public HAPExecutableDataAssociation getInputDataAssociation() {   return this.m_inputMapping;   }
	
	public HAPDefinitionActivityBranch getBranchActivityDefinition() {   return (HAPDefinitionActivityBranch)this.getActivityDefinition();  }
	
	public List<HAPExecutableResultActivityBranch> getBranchs(){   return this.m_branchs;   }
	public void addBranch(HAPExecutableResultActivityBranch result) {   this.m_branchs.add(result);   }
	
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
