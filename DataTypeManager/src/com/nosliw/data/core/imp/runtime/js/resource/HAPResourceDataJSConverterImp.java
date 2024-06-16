package com.nosliw.data.core.imp.runtime.js.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.strvalue.HAPStringableValueEntityWithID;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.resource.HAPResourceDataJSConverter;

public class HAPResourceDataJSConverterImp extends HAPStringableValueEntityWithID implements HAPResourceDataJSConverter{

	public static String _VALUEINFO_NAME;
	
	public HAPResourceDataJSConverterImp(){}
	
	public HAPResourceDataJSConverterImp(String script, HAPDataTypeId dataTypeName){
		this.setValue(script);
		this.setDataTypeName(dataTypeName);
	}
	
	@Override
	public HAPDataTypeId getDataTypeName() {	return (HAPDataTypeId)this.getAtomicAncestorValueObject(DATATYPENAME, HAPDataTypeId.class);	}
	public void setDataTypeName(HAPDataTypeId dataTypeName){ this.updateAtomicChildObjectValue(DATATYPENAME, dataTypeName); }

	@Override
	public String getValue(){  return this.getAtomicAncestorValueString(VALUE);  }
	public void setValue(String value){  this.updateAtomicChildStrValue(VALUE, value);  }

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
