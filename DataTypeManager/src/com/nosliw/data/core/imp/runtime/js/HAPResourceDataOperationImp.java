package com.nosliw.data.core.imp.runtime.js;

import com.nosliw.common.strvalue.HAPStringableValueEntityWithID;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.runtime.js.HAPResourceDataOperation;

public class HAPResourceDataOperationImp extends HAPStringableValueEntityWithID implements HAPResourceDataOperation{

	public static String _VALUEINFO_NAME;
	
	public HAPResourceDataOperationImp(String script, String operationId, HAPDataTypeId dataTypeName, String operationName){
		this.setScript(script);
		this.setOperationName(operationName);
		this.setDataTypeName(dataTypeName);
		this.setOperationId(operationId);
	}
	
	@Override
	public String getScript(){  return this.getAtomicAncestorValueString(VALUE);  }
	public void setScript(String script){  this.updateAtomicChildStrValue(VALUE, script);  }
	
	@Override
	public String getOperationId(){  return this.getAtomicAncestorValueString(OPERATIONID);  }
	public void setOperationId(String operationId){  this.updateAtomicChildStrValue(OPERATIONID, operationId);  }
	
	@Override
	public String getOperationName(){  return this.getAtomicAncestorValueString(OPERATIONNAME);  }
	public void setOperationName(String operationName){  this.updateAtomicChildStrValue(OPERATIONNAME, operationName);  }
	
	@Override
	public HAPDataTypeId getDataTypeName() {	return (HAPDataTypeId)this.getAtomicAncestorValueObject(DATATYPENAME, HAPDataTypeId.class);	}
	public void setDataTypeName(HAPDataTypeId dataTypeName){ this.updateAtomicChildObjectValue(DATATYPENAME, dataTypeName); }
}
