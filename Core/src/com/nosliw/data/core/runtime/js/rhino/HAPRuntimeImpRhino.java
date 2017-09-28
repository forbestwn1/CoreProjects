package com.nosliw.data.core.runtime.js.rhino;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.debugger.Main;

import com.google.common.util.concurrent.SettableFuture;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
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
import com.nosliw.data.core.runtime.js.HAPResourceDataJSGateway;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSScriptUtility;

public class HAPRuntimeImpRhino implements HAPRuntime{

	//info used to library resource that do not need to add to resource manager
	public static final String ADDTORESOURCEMANAGER = "ADDTORESOURCEMANAGER";
	
	//root scope
	private Scriptable m_scope;
	
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
	}
	
	/**
	 * Register object by name to rhino runtime, so that all the js (for instance operation implementation) can use it to implement something
	 * @param name
	 * @param gateWayPoint
	 */
	public void registerGatewayPoint(String name, Object gateWayPoint){
		try{
	        Object wrappedObject = Context.javaToJS(gateWayPoint, this.m_scope);
	        NativeObject nosliwObj = (NativeObject)this.m_scope.get("nosliw", m_scope);
	        Function createNodeFun = (Function)nosliwObj.get("createNode");
	        createNodeFun.call(Context.enter(), m_scope, nosliwObj, new Object[]{name, wrappedObject});
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			Context.exit();
		}
	}
	
	public void unregisterGatewayPoint(String name){
		
	}
	
	
	@Override
	public HAPServiceData executeExpressionSync(String expressionStr, Map<String, HAPData> parmsData) {
		//new expression definition
		HAPExpressionDefinition expDefinition = this.getRuntimeEnvironment().getExpressionManager().newExpressionDefinition(expressionStr, null, parmsData, null); 
		//build expression obj
		HAPExpression expression = this.getRuntimeEnvironment().getExpressionManager().processExpression(null, expDefinition, null, null);
		//execute task
		HAPRuntimeTask task = new HAPRuntimeTaskExecuteExpressionRhino(expression, parmsData);
		return this.executeTaskSync(task);
	}

	@Override
	public void executeTask(HAPRuntimeTask task){
		//prepare expression id
		task.setId(this.m_idIndex+++"");

		this.m_tasks.put(task.getTaskId(), task);

		task.registerListener(new HAPRunTaskEventListener(){
			@Override
			public void finish(HAPRuntimeTask task) {
				HAPServiceData taskServiceData = task.getResult();
				if(taskServiceData.isSuccess()){
					System.out.println("Task " + task.getTaskType() + " " + task.getTaskId() + " finished successfully!!!");
					System.out.println(taskServiceData.getData());
				}
				else{
					System.err.println("Task " + task.getTaskType() + " " + task.getTaskId() + " finished fail!!!");
					System.err.println(taskServiceData);
				}
				m_tasks.remove(task.getTaskId());	
			}}
		);
		
		//create a new thread to execute task
		new HAPRunnalbeInner(task, this).start();
	}

	class HAPRunnalbeInner extends Thread{
		private HAPRuntimeTask m_task;
		private HAPRuntime m_runtime;
		
		public HAPRunnalbeInner(HAPRuntimeTask task, HAPRuntime runtime){
			this.m_task = task;
			this.m_runtime = runtime;
		}
		
        @Override
        public void run() {
    		HAPRuntimeTask newTask = m_task.execute(this.m_runtime);
    		//if execute reaturn a task, it means that it depend on the task
    		if(newTask!=null){
    			executeTask(newTask);
    		}
        }
	}
	
	@Override
	public HAPServiceData executeTaskSync(HAPRuntimeTask task){
		try {
		    final SettableFuture<HAPServiceData> future = SettableFuture.create();
			task.registerListener(new HAPRunTaskEventListener(){
				@Override
				public void finish(HAPRuntimeTask task) {	
		            future.set(task.getResult());
				}});
			executeTask(task);
			return future.get();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	return HAPServiceData.createFailureData(e, "");
	    }
	}

	public void finishTask(String taskId, HAPServiceData taskServiceData){
		HAPRuntimeTask task = this.m_tasks.get(taskId);
		task.finish(taskServiceData);
		this.m_tasks.remove(taskId);
	}
	
	//Load all resources into rhino runtime, no discovery
	//if all success : load script, return null
	//if any fail : not load any script, return response
	public HAPLoadResourceResponse loadResources(List<HAPResourceInfo> resourcesIdInfo, Scriptable scope){
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

		List<HAPResourceId> failedResourceIds = loadResourceResponse.getFailedResourcesId();
		if(failedResourceIds.size()==0){
			//if all loaded, build script, return null
			//build scripts info
			List<HAPJSScriptInfo> scriptsInfo = new ArrayList<HAPJSScriptInfo>();
			for(HAPResource resource : loadResourceResponse.getLoadedResources()){
				if(resource.getResourceData() instanceof HAPResourceDataJSGateway){
					//for gateway resource
					HAPResourceDataJSGateway resourceData = (HAPResourceDataJSGateway)resource.getResourceData();
					this.registerGatewayPoint(resource.getId().getId(), resourceData.getGateway());
				}
				HAPResourceInfo resourceInfo = resourcesInfo.get(resource.getId());
				scriptsInfo.addAll(HAPRuntimeJSScriptUtility.buildScriptForResource(resourceInfo, resource));
			}
			
			//Load all resources to rhino runtime
			for(HAPJSScriptInfo scriptInfo : scriptsInfo){  this.loadScript(scriptInfo, scope);  }
			return null;
		}
		else{
			//if some resources fail to load, then do not build script, just return response back
			System.err.println("Failed to load resources : ");
			for(HAPResourceId resourceId : failedResourceIds){
				System.err.println(resourceId.toString());
			}
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
		this.m_scope = context.initStandardObjects(null);
		
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
		this.loadResources(resourceIdInfos, m_scope);
		
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
		this.loadResources(resourceIdInfos, m_scope);
		
		//data type
		
		//set gateway
		this.registerGatewayPoint(HAPConstant.RUNTIME_LANGUAGE_JS_GATEWAY, new HAPRuntimeGatewayRhinoImp(this.getRuntimeEnvironment(), this, m_scope));
		
		return m_scope;
	}
	
	private void loadScript(HAPJSScriptInfo scriptInfo, Scriptable scope){
		String file = scriptInfo.isFile(); 
		if(HAPBasicUtility.isStringEmpty(file)){
			//for script
			this.m_sciprtTracker.addScript(scriptInfo.getScript());
		}
		else{
			//for file
			this.m_sciprtTracker.addFile(file);
		}
		HAPRhinoRuntimeUtility.loadScript(scriptInfo.getScript(), scope, scriptInfo.getName());
	}
	
	public void loadScriptFromFile(String fileName, Class cs, Scriptable scope){
		HAPJSScriptInfo scriptInfo = HAPJSScriptInfo.buildByFile(HAPFileUtility.getFileNameOnClassPath(cs, fileName), "File__" + fileName);
		this.loadScript(scriptInfo, scope==null?this.m_scope:scope);
	}

	public void loadTaskScript(HAPJSScriptInfo scriptInfo, String taskId){		this.loadScript(scriptInfo, m_scope);	}
	
	public HAPRuntimeInfo getRuntimeInfo() {		return new HAPRuntimeInfo(HAPConstant.RUNTIME_LANGUAGE_JS, HAPConstant.RUNTIME_ENVIRONMENT_RHINO);	}

	@Override
	public void close(){
		this.m_sciprtTracker.export();
	}
	
	@Override
	public void start(){
        this.m_sciprtTracker = new HAPScriptTracker();
		
		ContextFactory factory = ContextFactory.getGlobal(); 

	    Main dbg = new Main("Hello");
	    dbg.attachTo(factory);

	    Context context = factory.enterContext();
		
//		this.m_context = Context.enter();
	    try {
	        this.init(context, null);

//	        System.setIn(dbg.getIn());
//	        System.setOut(dbg.getOut());
//	        System.setErr(dbg.getErr());
	        
		    dbg.setBreakOnEnter(true);
//		    dbg.setBreakOnExceptions(true);
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
	    finally{
	    	Context.exit();
	    }
	}
	
	public HAPRuntimeEnvironment getRuntimeEnvironment(){  return this.m_runtimeEnvironment;  }

}
