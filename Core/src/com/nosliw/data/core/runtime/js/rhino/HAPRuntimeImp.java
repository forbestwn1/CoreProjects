package com.nosliw.data.core.runtime.js.rhino;

import java.util.List;
import java.util.Set;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPResourceDiscovery;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSUtility;

public class HAPRuntimeImp implements HAPRuntime{

	private HAPResourceDiscovery m_resourceDiscovery;
	
	private HAPResourceManager m_resourceManager;
	
	private Context m_context;
	
	private Scriptable m_scope;
	
	public HAPRuntimeImp(HAPResourceDiscovery resourceDiscovery, HAPResourceManager resourceMan){
		this.m_resourceDiscovery = resourceDiscovery;
		this.m_resourceManager = resourceMan;
		this.init();
	}
	
	@Override
	public HAPRuntimeInfo getRuntimeInfo() {
		return new HAPRuntimeInfo(HAPConstant.RUNTIME_LANGUAGE_JS, HAPConstant.RUNTIME_ENVIRONMENT_RHINO);
	}

	@Override
	public HAPData executeExpression(HAPExpression expression) {
		//discover required resources
		List<HAPResourceId> resourcesId = this.getResourceDiscovery().discoverResourceRequirement(expression);
		
		//find which resource is missing
		List<HAPResourceId> missedResourceId = this.findMissedResources(resourcesId);
		
		//init scope
		Scriptable scope = this.initScope();
		
		//load missed resources
		this.loadResources(missedResourceId, scope, this.m_context);
		
		//execute expression
		HAPData out = this.execute(expression);
		
		return out;
	}

	private void init(){
		this.m_context = Context.enter();
	    try {
	        this.m_scope = this.initEsencialScope(m_context, null);
	        
	        Object wrappedRuntime = Context.javaToJS(this, this.m_scope);
	        ScriptableObject.putProperty(this.m_scope, "resourceManager", wrappedRuntime);
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	}
	
	private Scriptable initScope(){
		return this.m_scope;
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
	
	private List<HAPResourceId> findMissedResources(List<HAPResourceId> resourcesId){
		return null;
	}
	
	private void loadResources(List<HAPResourceId> resourcesId, Scriptable scope, Context context){
		List<HAPResource> missedResource = this.getResourceManager().getResources(resourcesId);
		for(HAPResource resource : missedResource){
			String resourceScript = HAPRuntimeJSUtility.buildScriptForResource(resource);
			context.evaluateString(scope, resourceScript, "Resource_"+resource.getId().toStringValue(HAPSerializationFormat.LITERATE), 1, null);
		}
	}
	

	
	
	private HAPData execute(HAPExpression expression){
		
		
		return null;
	}

	@Override
	public HAPResourceManager getResourceManager() {		return this.m_resourceManager;	}

	@Override
	public HAPResourceDiscovery getResourceDiscovery() {		return this.m_resourceDiscovery;	}
}
