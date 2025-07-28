package com.nosliw.core.application.common.parentrelation;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPManualDefinitionBrickRelationValueContext extends HAPManualDefinitionBrickRelation{

	//how to handle parent context merge with child context
	private static final String MODE = "mode";
	
	//not inherit some info item from parent
	private static final String EXCLUDEDINFO = "excludedInfo";

	private static final String GROUPTYPE = "groupType";

	private String m_mode = HAPConstantShared.INHERITMODE_RUNTIME;
	
	public HAPManualDefinitionBrickRelationValueContext() {
		super(HAPConstantShared.MANUAL_RELATION_TYPE_VALUECONTEXT);
	}

	public String getMode() {    return this.m_mode;     }
	public void setMode(String mode) {    this.m_mode = mode;    }
	
//	public Set<String> getExcludedInfo(){   return (Set<String>)this.getValue(EXCLUDEDINFO);     }
//	public void setExcludedInfo(Set<String> info) {    this.setValue(EXCLUDEDINFO, info);     }
//
//	public String[] getGroupTypes() {    return (String[])this.getValue(GROUPTYPE);   }
	
	public HAPManualDefinitionBrickRelationValueContext mergeHard(HAPManualDefinitionBrickRelationValueContext relation) {
		if(relation!=null && relation.m_mode!=null) {
			this.m_mode = relation.m_mode;
		}
		return this;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_mode = (String)jsonObj.opt(MODE);
		return true;
	}
}
