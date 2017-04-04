package com.nosliw.data.core.runtime.js.rhino;

import java.util.Map;
import java.util.Set;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceDiscovery;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceIdDataType;
import com.nosliw.data.core.runtime.js.HAPResourceIdHelper;
import com.nosliw.data.core.runtime.js.HAPResourceIdLibrary;
import com.nosliw.data.core.runtime.js.HAPResourceIdOperation;

public class HAPRuntimeImp implements HAPRuntime{

	private HAPResourceDiscovery m_resourceMan;
	
	@Override
	public HAPRuntimeInfo getRuntimeInfo() {
		return new HAPRuntimeInfo(HAPConstant.RUNTIME_LANGUAGE_JS, HAPConstant.RUNTIME_ENVIRONMENT_RHINO);
	}

	@Override
	public HAPData executeExpression(HAPExpression expression) {
		Set<HAPResourceId> resourcesId = this.getResourceDiscovery().discoverResourceRequirement(expression);
		
		Set<HAPResourceId> missedResourceId = this.findMissedResources(resourcesId);
		this.loadResources(missedResourceId);
		
		HAPData out = this.execute(expression);
		
		return out;
	}

	@Override
	public HAPResourceDiscovery getResourceDiscovery() {
		return this.m_resourceMan;
	}

	private Scriptable initScope(HAPExpression expression){
		Scriptable out = null;
		Context context = Context.enter();
	    try {
	        Scriptable scope = this.initEsencialScope(context, null);
	        scope = this.initRelatedResource(expression, context, scope);
	        
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	    return out;
	}
	
	/**
	 * Init resources related with expression
	 * @param context
	 * @param parent
	 * @return
	 */
	private Scriptable initRelatedResource(HAPExpression expression, Context context, Scriptable parent){
		return null;
	}
	
	/**
	 * Init essencial object, include base, all the library for expression and all basic data type
	 * @param context
	 * @param parent
	 * @return
	 */
	private Scriptable initEsencialScope(Context context, Scriptable parent){
		Scriptable out = context.initStandardObjects(null);
		
		//library
		
		//data type
		
		return out;
	}
	
	private Set<HAPResourceId> findMissedResources(Set<HAPResourceId> resourcesId){
		return null;
	}
	
	private Map<HAPResourceId, HAPResource> loadResources(Set<HAPResourceId> resourcesId){
		
		
		
		return null;
	}
	
	private HAPResource loadResource(){
		switch(out.getType()){
		case HAPConstant.DATAOPERATION_RESOURCE_TYPE_DATATYPEOPERATION:
			out = new HAPResourceIdOperation(out);
			break;
		case HAPConstant.DATAOPERATION_RESOURCE_TYPE_DATATYPE:
			out = new HAPResourceIdDataType(out);
			break;
		case HAPConstant.DATAOPERATION_RESOURCE_TYPE_LIBRARY:
			out = new HAPResourceIdLibrary(out);
			break;
		case HAPConstant.DATAOPERATION_RESOURCE_TYPE_HELPER:
			out = new HAPResourceIdHelper(out);
			break;
		}

	}
	
	private HAPData execute(HAPExpression expression){
		return null;
	}
	
}
