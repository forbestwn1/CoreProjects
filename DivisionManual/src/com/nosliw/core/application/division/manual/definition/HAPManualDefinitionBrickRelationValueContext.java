package com.nosliw.core.application.division.manual.definition;

import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityValueStructure;

public class HAPManualDefinitionBrickRelationValueContext extends HAPManualDefinitionBrickRelation{

	//how to handle parent context merge with child context
	private static final String MODE = "mode";
	
	//not inherit some info item from parent
	private static final String EXCLUDEDINFO = "excludedInfo";

	private static final String GROUPTYPE = "groupType";
	
	public HAPManualDefinitionBrickRelationValueContext() {
		super(HAPConstantShared.MANUAL_RELATION_TYPE_VALUECONTEXT);
		//init by default
		this.setValue(MODE, HAPConstantShared.INHERITMODE_DEFINITION);
		this.setValue(GROUPTYPE, HAPUtilityValueStructure.getInheritableCategaries());
	}

	
	public String getMode() {    return (String)this.getValue(MODE);     }
	public void setMode(String mode) {    this.setValue(MODE, mode);    }
	
	public Set<String> getExcludedInfo(){   return (Set<String>)this.getValue(EXCLUDEDINFO);     }
	public void setExcludedInfo(Set<String> info) {    this.setValue(EXCLUDEDINFO, info);     }

	public String[] getGroupTypes() {    return (String[])this.getValue(GROUPTYPE);   }
	
	public void mergeHard(HAPManualDefinitionBrickRelationValueContext configure) {
		this.mergeHardString(MODE, configure);
		this.mergeHardSet(EXCLUDEDINFO, configure);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildStringValue(jsonObj, MODE);
		this.buildStringValue(jsonObj, EXCLUDEDINFO);
		return true;
	}
	
	
}
