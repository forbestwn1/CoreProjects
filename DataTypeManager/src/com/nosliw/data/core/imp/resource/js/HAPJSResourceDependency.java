package com.nosliw.data.core.imp.resource.js;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntityWithID;
import com.nosliw.data.core.imp.resource.HAPResourceIdImp;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPJSResourceDependency  extends HAPStringableValueEntityWithID{

	public static String _VALUEINFO_NAME;
	
	@HAPAttribute
	public static String RESOURCEID = "resourceId";

	@HAPAttribute
	public static String DEPENDENCY = "dependency";
	
	public HAPJSResourceDependency(HAPResourceIdImp resourceId,List<HAPResourceId> dependency){
		this.setResourceId(resourceId);
		this.setDependency(dependency);
	}
	
	public HAPResourceIdImp getResourceId(){  return (HAPResourceIdImp)this.getAtomicAncestorValueObject(RESOURCEID, HAPResourceIdImp.class);  }
	public void setResourceId(HAPResourceIdImp resourceId){  this.updateAtomicChildObjectValue(RESOURCEID, resourceId);  }
	
	public List<HAPResourceId> getDependency(){  		return this.getAtomicAncestorValueArray(DEPENDENCY, HAPResourceIdImp.class);	}
	public void setDependency(List<HAPResourceId> resourcesId){		this.updateAtomicChildObjectValue(DEPENDENCY, resourcesId);	}
}
