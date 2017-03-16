package com.nosliw.data.datatype.importer.js;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.datatype.importer.HAPDataTypeIdImp;

public class HAPJSOperation extends HAPStringableValueEntity{

	public static String _VALUEINFO_NAME;
	
	@HAPAttribute
	public static String ID = "id";
	
	@HAPAttribute
	public static String SCRIPT = "script";
	
	@HAPAttribute
	public static String OPERATIONID = "operationId";

	@HAPAttribute
	public static String OPERATIONNAME = "operationName";
	
	@HAPAttribute
	public static String DATATYPENAME = "dataTypeName";
	
	public HAPJSOperation(String script, String operationId, HAPDataTypeId dataTypeName, String operationName){
		this.setScript(script);
		this.setOperationName(operationName);
		this.setDataTypeName(dataTypeName);
		this.setOperationId(operationId);
	}
	
	public String getId(){  return this.getAtomicAncestorValueString(ID);  }
	public void setId(String id){  this.updateAtomicChildStrValue(ID, id);  }
	
	public String getScript(){  return this.getAtomicAncestorValueString(SCRIPT);  }
	public void setScript(String script){  this.updateAtomicChildStrValue(SCRIPT, script);  }
	
	public String getOperationId(){  return this.getAtomicAncestorValueString(OPERATIONID);  }
	public void setOperationId(String operationId){  this.updateAtomicChildStrValue(OPERATIONID, operationId);  }
	
	public String getOperationName(){  return this.getAtomicAncestorValueString(OPERATIONNAME);  }
	public void setOperationName(String operationName){  this.updateAtomicChildStrValue(OPERATIONNAME, operationName);  }
	
	public HAPDataTypeId getDataTypeName() {	return (HAPDataTypeIdImp)this.getAtomicAncestorValueObject(DATATYPENAME, HAPDataTypeIdImp.class);	}
	public void setDataTypeName(HAPDataTypeId dataTypeName){ this.updateAtomicChildObjectValue(DATATYPENAME, dataTypeName); }
}
