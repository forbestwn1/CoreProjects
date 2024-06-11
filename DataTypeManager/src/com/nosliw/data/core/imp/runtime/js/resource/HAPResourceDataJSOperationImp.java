package com.nosliw.data.core.imp.runtime.js.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.strvalue.HAPStringableValueEntityWithID;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPOperation;
import com.nosliw.data.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.resource.HAPResourceDataJSOperation;

@HAPEntityWithAttribute
public class HAPResourceDataJSOperationImp extends HAPStringableValueEntityWithID implements HAPResourceDataJSOperation{

	public static String _VALUEINFO_NAME;

	@HAPAttribute
	public static final String OPERATIONINFO = "operationInfo";

	private List<HAPResourceDependency> m_resourceDependency = new ArrayList<HAPResourceDependency>();
	
	private HAPOperation m_operationInfo;
	
	public HAPResourceDataJSOperationImp(){}
	
	public HAPResourceDataJSOperationImp(String script, String operationId, HAPDataTypeId dataTypeName, String operationName){
		this.setValue(script);
		this.setOperationName(operationName);
		this.setDataTypeName(dataTypeName);
		this.setOperationId(operationId);
	}
	
	@Override
	public String getValue(){  return this.getAtomicAncestorValueString(VALUE);  }
	public void setValue(String value){  this.updateAtomicChildStrValue(VALUE, value);  }
	
	@Override
	public String getOperationId(){  return this.getAtomicAncestorValueString(OPERATIONID);  }
	public void setOperationId(String operationId){  this.updateAtomicChildStrValue(OPERATIONID, operationId);  }
	
	@Override
	public String getOperationName(){  return this.getAtomicAncestorValueString(OPERATIONNAME);  }
	public void setOperationName(String operationName){  this.updateAtomicChildStrValue(OPERATIONNAME, operationName);  }
	
	@Override
	public HAPDataTypeId getDataTypeName() {	return (HAPDataTypeId)this.getAtomicAncestorValueObject(DATATYPENAME, HAPDataTypeId.class);	}
	public void setDataTypeName(HAPDataTypeId dataTypeName){ this.updateAtomicChildObjectValue(DATATYPENAME, dataTypeName); }

	@Override
	public HAPResourceDataOrWrapper getDescendant(HAPPath path) {  throw new RuntimeException();  }

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo) {  return this.m_resourceDependency;  }
	public void setResourceDependency(List<HAPResourceDependency> resourceDependency) {      this.m_resourceDependency = resourceDependency;      }
	
	public void setOperationInfo(HAPOperation operationInfo) {
		this.m_operationInfo = operationInfo;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_operationInfo!=null) {
			jsonMap.put(OPERATIONINFO, HAPSerializeManager.getInstance().toStringValue(this.m_operationInfo, HAPSerializationFormat.JSON));
		}
		typeJsonMap.put(VALUE, HAPJsonTypeScript.class);
	}
}
