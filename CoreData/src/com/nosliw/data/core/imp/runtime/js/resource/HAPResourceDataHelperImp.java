package com.nosliw.data.core.imp.runtime.js.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.strvalue.HAPStringableValueEntityWithID;
import com.nosliw.core.data.HAPResourceDataJSHelper;
import com.nosliw.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.core.resource.HAPResourceDependency;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public class HAPResourceDataHelperImp extends HAPStringableValueEntityWithID implements HAPResourceDataJSHelper{

	public static String _VALUEINFO_NAME;
	
	public HAPResourceDataHelperImp(){}
	
	public HAPResourceDataHelperImp(String script){
		this.updateAtomicChildStrValue(VALUE, script);
	}
	
	@Override
	public String getValue(){  return this.getAtomicAncestorValueString(VALUE);  }

	@Override
	public HAPResourceDataOrWrapper getDescendant(HAPPath path) {  throw new RuntimeException();  }

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo) {   return new ArrayList<HAPResourceDependency>();  } 

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		typeJsonMap.put(VALUE, HAPJsonTypeScript.class);
	}
}
