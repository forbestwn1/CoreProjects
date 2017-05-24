package com.nosliw.data.core.runtime.js.rhino;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.debugger.Main;

import com.nosliw.common.info.HAPInfoUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceIdInfo;
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPResourceDiscovery;
import com.nosliw.data.core.runtime.HAPResourceHelper;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPJSLibraryId;
import com.nosliw.data.core.runtime.js.HAPRuntimeImpJS;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSScriptUtility;

public class HAPRuntimeImpJSRhino extends HAPRuntimeImpJS{

	//info used to library resource that do not need to add to resource manager
	public static final String ADDTORESOURCEMANAGER = "ADDTORESOURCEMANAGER";
	
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
		
		
		ContextFactory factory = new ContextFactory();

	    Main dbg = new Main("Hello");
	    dbg.attachTo(factory);

	    this.m_context = factory.enterContext();
		
//		this.m_context = Context.enter();
	    try {
	        this.m_scope = this.initEsencialScope(m_context, null);

	        System.setIn(dbg.getIn());
	        System.setOut(dbg.getOut());
	        System.setErr(dbg.getErr());
	        
		    dbg.setBreakOnEnter(true);
		    dbg.setScope(m_scope);
		    dbg.setSize(640, 400);
		    dbg.setVisible(true);
		    dbg.setExitAction(new ExitOnClose());	    
		    
	        
	        
	        Object wrappedRuntime = Context.javaToJS(this, this.m_scope);
	        ScriptableObject.putProperty(this.m_scope, "resourceService", wrappedRuntime);
	        
	        this.executeStartScript(this.m_context, this.m_scope);
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	}
	
	private static class ExitOnClose implements Runnable {
	    @Override
	    public void run() {
	      System.exit(0);
	    }
	  }
	
	@Override
	public void close(){
		this.m_sciprtTracker.export();
	}

	private void executeStartScript(Context context, Scriptable scope){
		String initJSFile = "init.js";
		String script = HAPFileUtility.readFile(HAPFileUtility.getInputStreamOnClassPath(this.getClass(), initJSFile));
		this.m_sciprtTracker.addFile(HAPFileUtility.getFileNameOnClassPath(getClass(), initJSFile));
//		context.evaluateString(scope, script, "<cmd>", 1, null);
		this.loadScript(script, context, scope, "init");
	}
	
	/**
	 * this method is call back method by js 
	 */
	public void loadResources(String[][] resourcesIdArray, String taskId){
		List<HAPResourceId> resourcesId = new ArrayList<HAPResourceId>();
		for(String[] resourceIdArray : resourcesIdArray){
			resourcesId.add(new HAPResourceId(resourceIdArray[0], resourceIdArray[1], null));
		}
//		this.loadResources(resourcesId, this.getTaskScope(taskId), m_context);
	}
	
	@Override
	public HAPData executeExpression(HAPExpression expression) {
		
		//init rhino runtime, init scope
		String taskId = this.initExecuteExpression();
		Scriptable scope = this.getTaskScope(taskId); 
		
		//discover required resources
		List<HAPResourceId> resourcesId = this.getResourceDiscovery().discoverResourceRequirement(expression);
		
		//find which resource is missing
		List<HAPResourceId> missedResourceId = this.findMissedResources(resourcesId);
		
		//load missed resources
//		this.loadResources(missedResourceId, scope, this.m_context);
		
		//execute expression
		HAPData out = this.execute(expression, scope, this.m_context);

		return out;
	}

	public void loadResources(Object resources, Object callBackFunction){
		System.out.println("Load resources !!!!!!!!!!!!!!!!!!!!");
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
	
	private void loadResources(List<HAPResourceIdInfo> resourcesIdInfo, Scriptable scope, Context context){
		for(HAPResourceIdInfo resourceIdInfo : resourcesIdInfo){
			List<HAPResourceId> resourcesId = new ArrayList<HAPResourceId>();
			resourcesId.add(resourceIdInfo.getResourceId());
			List<HAPResource> resources = this.getResourceManager().getResources(resourcesId);
			if(resources!=null && resources.size()==1){
				HAPResource resource = resources.get(0);
				resource.setInfo(HAPInfoUtility.merge(resource.getInfo(), resourceIdInfo.getInfo()));
				String resourceScript = HAPRuntimeJSScriptUtility.buildScriptForResource(resource, this.m_sciprtTracker);
				this.loadScript(resourceScript, context, scope, "Resource_"+resource.getId().toStringValue(HAPSerializationFormat.LITERATE));
			}
			else{
				
			}
		}
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
		
		List<HAPResourceIdInfo> resourceIdInfos = new ArrayList<HAPResourceIdInfo>();
		//library
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.core", null), null));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("external.Underscore", "1.6.0"), null));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("external.Backbone", "1.1.2"), null));
		
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.constant", null), null));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.common", null), null));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.expression", null), null));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.request", null), null));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.id", null), null));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.init", null), null));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.logging", null), null));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.resource", null), null));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.runtime", null), null));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.runtimerhino", null), null));
		
		for(HAPResourceId resourceId : resourceIds){
			resourceIdInfos.add(new HAPResourceIdInfo(resourceId).withInfo(ADDTORESOURCEMANAGER, ADDTORESOURCEMANAGER));
		}
		
		
		//data type
		
		
		this.loadResources(resourceIdInfos, out, context);
		
		return out;
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
	
	private int index = 1; 
	private void loadScript(String script, Context context, Scriptable scope, String name){
		try{
			if(name==null)		name = "";
			String scriptName = new Date().toString() + "_" + index + "_" + name;
			index++;
			
			String folder = "C:/Temp/ScriptExport/scripts/";
			HAPFileUtility.writeFile(folder+scriptName+".js", script);
			
			context.evaluateString(scope, script, scriptName, 1, null);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	class ResourceIdJS{
		public String type;
		public String id;
		
		public ResourceIdJS(String type, String id){
			this.type = type;
			this.id = id;
		}
	}
}
