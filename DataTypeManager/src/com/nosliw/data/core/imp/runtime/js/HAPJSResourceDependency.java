package com.nosliw.data.core.imp.runtime.js;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntityWithID;
import com.nosliw.data.core.runtime.HAPResourceId;

public class HAPJSResourceDependency  extends HAPStringableValueEntityWithID{

	public static String _VALUEINFO_NAME;
	
	@HAPAttribute
	public static String RESOURCEID = "resourceId";

	@HAPAttribute
	public static String DEPENDENCY = "dependency";
	
	public HAPJSResourceDependency(HAPResourceId resourceId,List<HAPResourceId> dependency){
		this.setResourceId(resourceId);
		this.setDependency(dependency);
	}
	
	public HAPResourceId getResourceId(){  return (HAPResourceId)this.getAtomicAncestorValueObject(RESOURCEID, HAPResourceId.class);  }
	public void setResourceId(HAPResourceId resourceId){  this.updateAtomicChildObjectValue(RESOURCEID, resourceId);  }
	
	public List<HAPResourceId> getDependency(){  		return this.getAtomicAncestorValueArray(DEPENDENCY, HAPResourceId.class);	}
	public void setDependency(List<HAPResourceId> resourcesId){		this.updateAtomicChildObjectValue(DEPENDENCY, resourcesId);	}
}
