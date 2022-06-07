package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPUtilityValueStructure;

public class HAPConfigureProcessorInherit extends HAPInfoImpSimple{
	
	//how to handle parent context merge with child context
	private static final String MODE = "mode";
	
	//not inherit some info item from parent
	private static final String EXCLUDEDINFO = "excludedInfo";

	private static final String GROUPTYPE = "groupType";
	
	public HAPConfigureProcessorInherit() {
		//init by default
		this.setValue(MODE, HAPConstantShared.INHERITMODE_DEFINITION);
		this.setValue(GROUPTYPE, HAPUtilityValueStructure.getInheritableCategaries());
	}
	
	public String getMode() {    return (String)this.getValue(MODE);     }
	public void setMode(String mode) {    this.setValue(MODE, mode);    }
	
	public Set<String> getExcludedInfo(){   return (Set<String>)this.getValue(EXCLUDEDINFO);     }
	public void setExcludedInfo(Set<String> info) {    this.setValue(EXCLUDEDINFO, info);     }

	public String[] getGroupTypes() {    return (String[])this.getValue(GROUPTYPE);   }
	
	public void mergeHard(HAPConfigureProcessorInherit configure) {
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
