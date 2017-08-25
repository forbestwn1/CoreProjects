package com.nosliw.data.core.runtime.js.rhino;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.debugger.Main;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.runtime.HAPLoadResourceResponse;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceHelper;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPRunTaskEventListener;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.js.HAPJSLibraryId;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSScriptUtility;

public class HAPRuntimeImpRhino implements HAPRuntime, HAPRuntimeGatewayRhino{

	//info used to library resource that do not need to add to resource manager
	public static final String ADDTORESOURCEMANAGER = "ADDTORESOURCEMANAGER";
	
	private Context m_context;
	
	//root scope
	private Scriptable m_scope;
	
	//different task have different scope
	private Map<String, Scriptable> m_taskScope;
	
	//track all the script loaded to rhino
	private HAPScriptTracker m_sciprtTracker;
	
	//for reate id
	private int m_idIndex = 0;
	
	//store all the task, for instance, execute expression, load resources
	private Map<String, HAPRuntimeTask> m_tasks;

	private HAPRuntimeEnvironment m_runtimeEnvironment;
	
	public HAPRuntimeImpRhino(HAPRuntimeEnvironment runtimeEnvironment){
		this.m_runtimeEnvironment = runtimeEnvironment;
		this.m_tasks = new LinkedHashMap<String, HAPRuntimeTask>();
		this.m_taskScope = new LinkedHashMap<String, Scriptable>();
	}
	
	//gateway callback method
	@Override
	public void requestDiscoverResources(Object objResourceIds, Object handlers){
		try{
			List<HAPResourceId> resourceIds = HAPRhinoRuntimeUtility.rhinoResourcesIdToResourcesId((NativeArray)objResourceIds); 
			List<HAPResourceInfo> resourceInfos = this.getRuntimeEnvironment().getResourceDiscovery().discoverResource(resourceIds);
			HAPServiceData serviceData = HAPServiceData.createSuccessData(resourceInfos);
			HAPRhinoRuntimeUtility.invokeGatewayHandlers(serviceData, handlers, m_context, m_scope);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//gateway callback method
	@Override
	public void requestDiscoverAndLoadResources(Object objResourceIds, Object handlers){
		try{
			List<HAPResourceId> resourceIds = HAPRhinoRuntimeUtility.rhinoResourcesIdToResourcesId((NativeArray)objResourceIds);
			//discovery
			List<HAPResourceInfo> resourceInfos = this.getRuntimeEnvironment().getResourceDiscovery().discoverResource(resourceIds);
			//load resources to rhino runtime
			HAPLoadResourceResponse response = this.loadResources(resourceInfos, m_scope, m_context);

			HAPServiceData serviceData = null;
			if(response.isSuccess()){
				serviceData = HAPServiceData.createSuccessData(resourceInfos);
			}
			else{
				serviceData = HAPServiceData.createFailureData(response.getFailedResourcesId(), "");
			}
			
			//callback with resourceInfos
			HAPRhinoRuntimeUtility.invokeGatewayHandlers(serviceData, handlers, m_context, m_scope);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//gateway callback method
	@Override
	public void requestLoadResources(Object objResourcesInfo, Object handlers){
		try{
			List<HAPResourceInfo> resourcesInfo = HAPRhinoRuntimeUtility.rhinoResourcesInfoToResourcesInfo((NativeArray)objResourcesInfo);
			//load resources to rhino runtime
			HAPLoadResourceResponse response = this.loadResources(resourcesInfo, m_scope, m_context);
			HAPServiceData serviceData = null;
			if(response==null){
				serviceData = HAPServiceData.createSuccessData();
			}
			else{
				serviceData = HAPServiceData.createFailureData(response.getFailedResourcesId(), "");
			}
			HAPRhinoRuntimeUtility.invokeGatewayHandlers(serviceData, handlers, m_context, m_scope);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//gateway callback method
	@Override
	public void notifyExpressionExecuteResult(String taskId, Object result){
		try{
			HAPRuntimeTask expressionTask = this.m_tasks.get(taskId);
			String resultStr = (String)HAPRhinoDataUtility.toJson(result);
			HAPDataWrapper resultData = new HAPDataWrapper(resultStr); 
			expressionTask.success(resultData);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//gatewary callback method
	@Override
	public void notifyResourcesLoaded(String taskId){
		try{
			this.m_tasks.get(taskId).success(null);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void executeTask(HAPRuntimeTask task){
		//prepare expression id
		task.setId(this.m_idIndex+++"");

		this.m_tasks.put(task.getTaskId(), task);

		task.registerListener(new HAPRunTaskEventListener(){
			@Override
			public void success(HAPRuntimeTask task) {	m_tasks.remove(task.getTaskId());	}});
		
		HAPRuntimeTask newTask = task.execute(this);
		if(newTask!=null){
			this.executeTask(newTask);
		}
	}
	
	public void finishTask(String taskId){
		this.m_tasks.remove(taskId);
	}
	
	public Scriptable initExecuteExpression(String taskId){
		this.m_taskScope.put(taskId, this.m_scope);
		return this.m_scope;
	}
	
	//Load all resources into rhino runtime, no discovery
	//if all success : load script, return null
	//if any fail : not load any script, return response
	private HAPLoadResourceResponse loadResources(List<HAPResourceInfo> resourcesIdInfo, Scriptable scope, Context context){
		//Get all resource data
		//organize resource infos by id
		Map<HAPResourceId, HAPResourceInfo> resourcesInfo = new LinkedHashMap<HAPResourceId, HAPResourceInfo>();
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
		for(HAPResourceInfo resourceInfo : resourcesIdInfo){ 
			resourceIds.add(resourceInfo.getId());  
			resourcesInfo.put(resourceInfo.getId(), resourceInfo);  
		}

		//Retrieve resources
		HAPLoadResourceResponse loadResourceResponse = this.getRuntimeEnvironment().getResourceManager().getResources(resourceIds);

		if(loadResourceResponse.getFailedResourcesId().size()==0){
			//if all loaded, build script, return null
			//build scripts info
			List<HAPJSScriptInfo> scriptsInfo = new ArrayList<HAPJSScriptInfo>();
			for(HAPResource resource : loadResourceResponse.getLoadedResources()){
				HAPResourceInfo resourceInfo = resourcesInfo.get(resource.getId());
				scriptsInfo.addAll(HAPRuntimeJSScriptUtility.buildScriptForResource(resourceInfo, resource));
			}
			
			//Load all resources to rhino runtime
			for(HAPJSScriptInfo scriptInfo : scriptsInfo){  this.loadScript(scriptInfo, context, scope);  }
			return null;
		}
		else{
			//if some resources fail to load, then do not build script, just return response back
			return loadResourceResponse;
		}
	}
	
	/**
	 * Init essencial object, include base, all the library for expression and all basic data type
	 * @param context
	 * @param parent
	 * @return
	 */
	private Scriptable init(Context context, Scriptable parent){
		Scriptable out = context.initStandardObjects(null);
		
		//library
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("external.Underscore", "1.6.0")));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("external.Backbone", "1.1.2")));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.core", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.runtimerhinoinit", null)));

		List<HAPResourceInfo> resourceIdInfos = new ArrayList<HAPResourceInfo>();
		for(HAPResourceId resourceId : resourceIds){
			resourceIdInfos.add(new HAPResourceInfo(resourceId).withInfo(ADDTORESOURCEMANAGER, ADDTORESOURCEMANAGER));
		}
		this.loadResources(resourceIdInfos, out, context);
		
		//set gateway
        Object wrappedRuntime = Context.javaToJS(this, out);
        NativeObject nosliwObj = (NativeObject)out.get("nosliw", out);
        Function createNodeFun = (Function)nosliwObj.get("createNode");
        createNodeFun.call(m_context, out, nosliwObj, new Object[]{HAPConstant.RUNTIME_LANGUAGE_JS_GATEWAY, wrappedRuntime});

        
		resourceIds = new ArrayList<HAPResourceId>();
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.constant", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.logging", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.common", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.error", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.expression", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.request", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.id", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.resource", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.runtime", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.runtimerhino", null)));
		
		resourceIdInfos = new ArrayList<HAPResourceInfo>();
		for(HAPResourceId resourceId : resourceIds){
			resourceIdInfos.add(new HAPResourceInfo(resourceId).withInfo(ADDTORESOURCEMANAGER, ADDTORESOURCEMANAGER));
		}
		this.loadResources(resourceIdInfos, out, context);
		
		//data type
		

		return out;
	}
	
	private void loadScript(HAPJSScriptInfo scriptInfo, Context context, Scriptable scope){
		String file = scriptInfo.isFile(); 
		if(HAPBasicUtility.isStringEmpty(file)){
			//for script
			this.m_sciprtTracker.addScript(scriptInfo.getScript());
		}
		else{
			//for file
			this.m_sciprtTracker.addFile(file);
		}
		HAPRhinoRuntimeUtility.loadScript(scriptInfo.getScript(), context, scope, scriptInfo.getName());
	}
	
	public void loadScriptFromFile(String fileName, Class cs, Context context, Scriptable scope){
		HAPJSScriptInfo scriptInfo = HAPJSScriptInfo.buildByFile(HAPFileUtility.getFileNameOnClassPath(cs, fileName), "File__" + fileName);
		this.loadScript(scriptInfo, context==null?this.m_context:context, scope==null?this.m_scope:scope);
	}

	public void loadTaskScript(HAPJSScriptInfo scriptInfo, String taskId){		this.loadScript(scriptInfo, m_context, m_scope);	}
	
	public HAPRuntimeInfo getRuntimeInfo() {		return new HAPRuntimeInfo(HAPConstant.RUNTIME_LANGUAGE_JS, HAPConstant.RUNTIME_ENVIRONMENT_RHINO);	}

	@Override
	public void close(){
		this.m_sciprtTracker.export();
	}
	
	@Override
	public void start(){
        this.m_sciprtTracker = new HAPScriptTracker();
		
		ContextFactory factory = new ContextFactory();

	    Main dbg = new Main("Hello");
	    dbg.attachTo(factory);

	    this.m_context = factory.enterContext();
		
//		this.m_context = Context.enter();
	    try {
	        this.m_scope = this.init(m_context, null);

//	        System.setIn(dbg.getIn());
//	        System.setOut(dbg.getOut());
//	        System.setErr(dbg.getErr());
	        
		    dbg.setBreakOnEnter(true);
		    dbg.setBreakOnExceptions(true);
		    dbg.setScope(m_scope);
		    dbg.setSize(1200, 800);
		    dbg.setVisible(true);
		    dbg.setExitAction(new Runnable(){
			    @Override
			    public void run() {
			      System.exit(0);
			    }
			  });	    

	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	}
	
	protected HAPRuntimeEnvironment getRuntimeEnvironment(){  return this.m_runtimeEnvironment;  }
}
