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

import com.nosliw.common.info.HAPInfoUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.runtime.HAPExecuteExpressionTask;
import com.nosliw.data.core.runtime.HAPLoadResourcesTask;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPResourceDiscovery;
import com.nosliw.data.core.runtime.HAPResourceHelper;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.js.HAPJSLibraryId;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeImpJS;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSScriptUtility;

public class HAPRuntimeImpJSRhino extends HAPRuntimeImpJS implements HAPRuntimeGatewayRhino{

	//info used to library resource that do not need to add to resource manager
	public static final String ADDTORESOURCEMANAGER = "ADDTORESOURCEMANAGER";
	
	private HAPResourceDiscovery m_resourceDiscovery;
	
	private HAPResourceManager m_resourceManager;
	
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
	
	public HAPRuntimeImpJSRhino(){}
	
	public HAPRuntimeImpJSRhino(HAPResourceDiscovery resourceDiscovery, HAPResourceManager resourceMan){
		this.m_resourceDiscovery = resourceDiscovery;
		this.m_resourceManager = resourceMan;
		this.m_tasks = new LinkedHashMap<String, HAPRuntimeTask>();
		this.m_taskScope = new LinkedHashMap<String, Scriptable>();
	}

	//gateway callback method
	@Override
	public void loadResources(Object resources, Object callBackFunction){
		System.out.println("Load resources !!!!!!!!!!!!!!!!!!!!" + resources + "  " + callBackFunction);

		List<HAPResourceInfo> resourceIds = new ArrayList<HAPResourceInfo>();
		
		NativeArray resourcesArray = (NativeArray)resources;
		for(int i=0; i<resourcesArray.size(); i++){
			NativeObject resourceIdObject = (NativeObject)resourcesArray.get(i);
			String type = (String)resourceIdObject.get(HAPResourceId.TYPE);
			String id = (String)resourceIdObject.get(HAPResourceId.ID);
			resourceIds.add(new HAPResourceInfo(new HAPResourceId(type, id)));
		}
		this.loadResources(resourceIds, m_scope, m_context);
		
		((Function)callBackFunction).call(this.m_context, this.m_scope, null, new Object[]{});
	}
	
	//gateway callback method
	@Override
	public void expressionExecuteResult(String taskId, String result){
		HAPRuntimeTask expressionTask = this.m_tasks.get(taskId);
		HAPDataWrapper resultData = new HAPDataWrapper(result); 
		expressionTask.success(resultData);
	}
	
	//gatewary callback method
	@Override
	public void resourcesLoaded(String taskId){
		this.m_tasks.get(taskId).success(null);
	}
	
	@Override
	public void executeExpressionTask(HAPExecuteExpressionTask expressionTask) {
		//prepare expression id
		expressionTask.setId(this.m_idIndex+++"");

		HAPExpressionTaskRhino rhinoExpressionTask = (HAPExpressionTaskRhino)expressionTask;
		rhinoExpressionTask.setRuntime(this);
		
		this.m_tasks.put(expressionTask.getTaskId(), expressionTask);
		
		//init rhino runtime, init scope
		Scriptable scope = this.initExecuteExpression(expressionTask.getTaskId());
		
		//prepare resources for expression in the runtime
		//execute expression after load required resources
		List<HAPResourceId> resourcesId = this.getResourceDiscovery().discoverResourceRequirement(rhinoExpressionTask.getExpression());
		HAPLoadResourcesTask loadResourcesTask = new HAPLoadResourcesTask(resourcesId){
			@Override
			protected void doSuccess() {
				removeTask(this.getTaskId());
			}
		};
		loadResourcesTask.setId(this.m_idIndex+++"");
		loadResourcesTask.setParent(expressionTask);
		this.m_tasks.put(loadResourcesTask.getTaskId(), loadResourcesTask);
		this.executeLoadResources(loadResourcesTask);
	}

	public void removeTask(String taskId){
		this.m_tasks.remove(taskId);
	}
	
	private void executeLoadResources(HAPLoadResourcesTask loadResourcesTask){
		HAPJSScriptInfo scriptInfo = HAPRuntimeJSScriptUtility.buildScriptForLoadResourceTask(loadResourcesTask);
		this.loadTaskScript(scriptInfo, loadResourcesTask.getTaskId());
	}
	
	private Scriptable initExecuteExpression(String taskId){
		this.m_taskScope.put(taskId, this.m_scope);
		return this.m_scope;
	}
	
	private void loadResources(List<HAPResourceInfo> resourcesIdInfo, Scriptable scope, Context context){
		List<HAPJSScriptInfo> scriptsInfo = new ArrayList<HAPJSScriptInfo>();
		
		for(HAPResourceInfo resourceIdInfo : resourcesIdInfo){
			List<HAPResourceId> resourcesId = new ArrayList<HAPResourceId>();
			resourcesId.add(resourceIdInfo.getId());
			List<HAPResource> resources = this.getResourceManager().getResources(resourcesId);
			if(resources!=null && resources.size()==1){
				HAPResource resource = resources.get(0);
				resource.setInfo(HAPInfoUtility.merge(resource.getInfo(), resourceIdInfo.getInfo()));
				scriptsInfo.addAll(HAPRuntimeJSScriptUtility.buildScriptForResource(resource));
			}
			else{
				
			}
		}
		
		for(HAPJSScriptInfo scriptInfo : scriptsInfo)			this.loadScript(scriptInfo, context, scope);
	}
	
	/**
	 * Init essencial object, include base, all the library for expression and all basic data type
	 * @param context
	 * @param parent
	 * @return
	 */
	private Scriptable initEsencialScope(Context context, Scriptable parent){
		Scriptable out = context.initStandardObjects(null);
		
		List<HAPResourceInfo> resourceIdInfos = new ArrayList<HAPResourceInfo>();
		//library
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.core", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("external.Underscore", "1.6.0")));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("external.Backbone", "1.1.2")));
		
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.constant", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.common", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.expression", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.request", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.id", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.init", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.logging", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.resource", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.runtime", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.runtimerhino", null)));
		
		for(HAPResourceId resourceId : resourceIds){
			resourceIdInfos.add(new HAPResourceInfo(resourceId).withInfo(ADDTORESOURCEMANAGER, ADDTORESOURCEMANAGER));
		}
		
		//data type
		
		
		this.loadResources(resourceIdInfos, out, context);
		
		return out;
	}
	
	@Override
	public HAPResourceManager getResourceManager() {		return this.m_resourceManager;	}

	@Override
	public HAPResourceDiscovery getResourceDiscovery() {		return this.m_resourceDiscovery;	}
	
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
	
	@Override
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
	        this.m_scope = this.initEsencialScope(m_context, null);

//	        System.setIn(dbg.getIn());
//	        System.setOut(dbg.getOut());
//	        System.setErr(dbg.getErr());
	        
//		    dbg.setBreakOnEnter(true);
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

		    //set gateway object
	        Object wrappedRuntime = Context.javaToJS(this, this.m_scope);
	        NativeObject nosliwObj = (NativeObject)m_scope.get("nosliw", m_scope);
	        Function createNodeFun = (Function)nosliwObj.get("createNode");
	        createNodeFun.call(m_context, this.m_scope, nosliwObj, new Object[]{HAPConstant.RUNTIME_LANGUAGE_JS_GATEWAY, wrappedRuntime});
		    
	        this.loadScriptFromFile("init.js", HAPRuntimeImpJSRhino.class, m_context, m_scope);
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	}
}
