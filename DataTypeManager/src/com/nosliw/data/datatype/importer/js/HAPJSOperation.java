package com.nosliw.data.datatype.importer.js;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.datatype.importer.HAPResourceIdImp;

public class HAPJSOperation extends HAPStringableValueEntity{

	@HAPAttribute
	public static String ID = "id";
	
	@HAPAttribute
	public static String SCRIPT = "script";
	
	@HAPAttribute
	public static String OPERATIONID = "operationId";
	
	@HAPAttribute
	public static String DATATYPEID = "dataTypeId";
	
	@HAPAttribute
	public static String DEPENDENCY = "dependency";
	
	public String getId(){  return this.getAtomicAncestorValueString(ID);  }
	public void setId(String id){  this.updateAtomicChildStrValue(ID, id);  }
	
	public String getScript(){  return this.getAtomicAncestorValueString(SCRIPT);  }
	public void setScript(String script){  this.updateAtomicChildStrValue(SCRIPT, script);  }
	
	public String getOperationId(){  return this.getAtomicAncestorValueString(OPERATIONID);  }
	public void setOperationId(String operationId){  this.updateAtomicChildStrValue(OPERATIONID, operationId);  }
	
	public String getDataTypeId(){  return this.getAtomicAncestorValueString(DATATYPEID);  }
	public void setDataTypeId(String dataTypeId){  this.updateAtomicChildStrValue(DATATYPEID, dataTypeId);  }
	
	public List<HAPResourceId> getDependency(){  
		return this.getAtomicAncestorValueArray(DEPENDENCY, HAPResourceIdImp.class); 
	}
	public void setDependency(List<HAPResourceId> resourcesId){
		this.updateAtomicChildObjectValue(DEPENDENCY, resourcesId);
	}
	
}
