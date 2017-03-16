package com.nosliw.data.datatype.importer.js;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.datatype.importer.HAPResourceIdImp;

public class HAPJSResourceDependency  extends HAPStringableValueEntity{

	public static String _VALUEINFO_NAME;
	
	@HAPAttribute
	public static String ID = "id";
	
	@HAPAttribute
	public static String RESOURCEID = "resourceId";

	@HAPAttribute
	public static String DEPENDENCY = "dependency";
	
	public HAPJSResourceDependency(HAPResourceIdImp resourceId,List<HAPResourceId> dependency){
		this.setResourceId(resourceId);
		this.setDependency(dependency);
	}
	
	public String getId(){  return this.getAtomicAncestorValueString(ID);  }
	public void setId(String id){  this.updateAtomicChildStrValue(ID, id);  }
	
	public HAPResourceIdImp getResourceId(){  return (HAPResourceIdImp)this.getAtomicAncestorValueObject(RESOURCEID, HAPResourceIdImp.class);  }
	public void setResourceId(HAPResourceIdImp resourceId){  this.updateAtomicChildObjectValue(RESOURCEID, resourceId);  }
	
	public List<HAPResourceId> getDependency(){  		return this.getAtomicAncestorValueArray(DEPENDENCY, HAPResourceIdImp.class);	}
	public void setDependency(List<HAPResourceId> resourcesId){		this.updateAtomicChildObjectValue(DEPENDENCY, resourcesId);	}
}
