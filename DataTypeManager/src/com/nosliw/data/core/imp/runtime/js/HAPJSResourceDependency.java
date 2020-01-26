package com.nosliw.data.core.imp.runtime.js;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntityWithID;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPJSResourceDependency  extends HAPStringableValueEntityWithID{

	public static String _VALUEINFO_NAME;
	
	@HAPAttribute
	public static String RESOURCEID = "resourceId";

	@HAPAttribute
	public static String DEPENDENCY = "dependency";
	
	public HAPJSResourceDependency(){}
	
	public HAPJSResourceDependency(HAPResourceIdSimple resourceId,List<HAPResourceDependency> dependency){
		this.setResourceId(resourceId);
		this.setDependency(dependency);
	}
	
	public HAPResourceIdSimple getResourceId(){  
		return (HAPResourceIdSimple)this.getAtomicAncestorValueObject(RESOURCEID, HAPResourceIdSimple.class);  
	}
	
	public void setResourceId(HAPResourceIdSimple resourceId){  this.updateAtomicChildObjectValue(RESOURCEID, resourceId);  }
	
	public List<HAPResourceDependency> getDependency(){  		return this.getAtomicAncestorValueArray(DEPENDENCY, HAPResourceDependency.class.getName());	}
	public void setDependency(List<HAPResourceDependency> resourcesId){		this.updateAtomicChildObjectValue(DEPENDENCY, resourcesId);	}
}
