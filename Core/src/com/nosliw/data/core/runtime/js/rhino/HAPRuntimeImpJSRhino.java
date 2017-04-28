package com.nosliw.data.core.runtime.js.rhino;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPResourceDiscovery;
import com.nosliw.data.core.runtime.HAPResourceHelper;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPJSLibraryId;
import com.nosliw.data.core.runtime.js.HAPRuntimeImpJS;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSScriptUtility;

public class HAPRuntimeImpJSRhino extends HAPRuntimeImpJS{

	private HAPResourceDiscovery m_resourceDiscovery;
	
	private HAPResourceManager m_resourceManager;
	
	private Context m_context;
	
	//root scope
	private Scriptable m_scope;
	
	//different task have different scope
	private Map<String, Scriptable> m_taskScope;
	
	private ScriptTracker m_sciprtTracker;
	
	public HAPRuntimeImpJSRhino(){}
	
	public HAPRuntimeImpJSRhino(HAPResourceDiscovery resourceDiscovery, HAPResourceManager resourceMan){
		this.m_resourceDiscovery = resourceDiscovery;
		this.m_resourceManager = resourceMan;
	}
	
	@Override
	public HAPRuntimeInfo getRuntimeInfo() {
		return new HAPRuntimeInfo(HAPConstant.RUNTIME_LANGUAGE_JS, HAPConstant.RUNTIME_ENVIRONMENT_RHINO);
	}

	@Override
	public void start(){
		this.m_sciprtTracker = new ScriptTracker();
		
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
	
	@Override
	public void close(){
		this.m_sciprtTracker.export();
	}

	/**
	 * this method is call back method by js 
	 */
	public void loadResources(String[][] resourcesIdArray, String taskId){
		List<HAPResourceId> resourcesId = new ArrayList<HAPResourceId>();
		for(String[] resourceIdArray : resourcesIdArray){
			resourcesId.add(new HAPResourceId(resourceIdArray[0], resourceIdArray[1], null));
		}
		this.loadResources(resourcesId, this.getTaskScope(taskId), m_context);
	}
	
	@Override
	public HAPData executeExpression(HAPExpression expression) {
		
		/*		
		//discover required resources
		List<HAPResourceId> resourcesId = this.getResourceDiscovery().discoverResourceRequirement(expression);
		
		//find which resource is missing
		List<HAPResourceId> missedResourceId = this.findMissedResources(resourcesId);
		
		//init scope
		String taskId = this.initExecuteExpression();
		Scriptable scope = this.getTaskScope(taskId); 
		
		//load missed resources
		this.loadResources(missedResourceId, scope, this.m_context);
		
		//execute expression
		HAPData out = this.execute(expression, scope, this.m_context);

		return out;
*/
		return null;
	}

	private String initExecuteExpression(){
		String out = this.getTaskId();
		this.m_taskScope.put(out, this.m_scope);
		return out;
	}
	
	private Scriptable getTaskScope(String taskId){  return this.m_taskScope.get(taskId);  }
	
	/**
	 * Init essencial object, include base, all the library for expression and all basic data type
	 * @param context
	 * @param parent
	 * @return
	 */
	private Scriptable initEsencialScope(Context context, Scriptable parent){
		Scriptable out = context.initStandardObjects(null);
		
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
		//library
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.init", null), null));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("external.Underscore", "1.6.0"), null));
//		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("external.log4javascript", "1.0.0"), null));
		
		
		//data type
		
		
		this.loadResources(resourceIds, out, context);
		
		return out;
	}
	
	private List<HAPResourceId> findMissedResources(List<HAPResourceId> resourcesId){
		NativeObject nosliwObjJS = (NativeObject)this.m_scope.get("nosliw", this.m_scope);
		NativeObject resourceManJS = (NativeObject)nosliwObjJS.get("resourceManager");
		Function findMissingResourcesFunJS = (Function)resourceManJS.get("findMissingResources");
		
		List<ResourceIdJS> jsArgs = new ArrayList<ResourceIdJS>();
		for(HAPResourceId resourceId : resourcesId){
			jsArgs.add(new ResourceIdJS(resourceId.getType(), resourceId.getId()));
		}
		
		List<HAPResourceId> out = new ArrayList<HAPResourceId>();
		NativeArray missedArrayJS = (NativeArray)findMissingResourcesFunJS.call(this.m_context, this.m_scope, resourceManJS, new Object[]{jsArgs.toArray(new ResourceIdJS[0])});
		for(Object o : missedArrayJS.getIds()){
		    int index = (Integer) o;
		    Object a = missedArrayJS.get(index);
		    int missedIndex = Integer.valueOf(a.toString());
			out.add(resourcesId.get(missedIndex));
		}
		return out;
	}
	
	private void loadResources(List<HAPResourceId> resourcesId, Scriptable scope, Context context){
		List<HAPResource> missedResource = this.getResourceManager().getResources(resourcesId);
		for(HAPResource resource : missedResource){
			try{
				String resourceScript = HAPRuntimeJSScriptUtility.buildScriptForResource(resource, this.m_sciprtTracker);
//				context.evaluateString(scope, resourceScript, "Resource_"+resource.getId().toStringValue(HAPSerializationFormat.LITERATE), 1, null);
				context.evaluateString(scope, resourceScript, "<cmd>", 1, null);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private HAPData execute(HAPExpression expression, Scriptable scope, Context context){
		
		String script = HAPRuntimeJSScriptUtility.buildScriptForExecuteExpression(expression);
		NativeObject dataJS = (NativeObject)context.evaluateString(scope, script, "<cmd>", 1, null);
		
		return null;
	}

	@Override
	public HAPResourceManager getResourceManager() {		return this.m_resourceManager;	}

	@Override
	public HAPResourceDiscovery getResourceDiscovery() {		return this.m_resourceDiscovery;	}
	
	private String getTaskId(){return System.currentTimeMillis()+"";}
	
	class ResourceIdJS{
		public String type;
		public String id;
		
		public ResourceIdJS(String type, String id){
			this.type = type;
			this.id = id;
		}
	}
}
