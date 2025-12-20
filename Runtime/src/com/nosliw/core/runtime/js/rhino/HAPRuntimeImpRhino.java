package com.nosliw.core.runtime.js.rhino;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.debugger.Main;

import com.google.common.util.concurrent.SettableFuture;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.script.HAPJSScriptInfo;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.entity.js.library.HAPJSLibraryId;
import com.nosliw.core.gateway.HAPGatewayManager;
import com.nosliw.core.gateway.HAPGatewayOutput;
import com.nosliw.core.resource.HAPGatewayResource;
import com.nosliw.core.resource.HAPResourceHelper;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.resource.HAPResourceInfo;
import com.nosliw.core.runtime.HAPRunTaskEventListener;
import com.nosliw.core.runtime.HAPRuntime;
import com.nosliw.core.runtime.HAPRuntimeInfo;
import com.nosliw.core.runtime.HAPRuntimeManager;
import com.nosliw.core.runtime.HAPRuntimeTask;
import com.nosliw.core.runtime.HAPRuntimeWithScript;
import com.nosliw.core.xxx.runtimeenv.HAPRuntimeEnvironmentJS;

@HAPEntityWithAttribute
public class HAPRuntimeImpRhino implements HAPRuntimeWithScript{

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

	private HAPGatewayManager m_gatewayManager;
	
	public HAPRuntimeImpRhino(HAPGatewayManager gatewayManager){
		this.m_gatewayManager = gatewayManager;
		this.m_tasks = new LinkedHashMap<String, HAPRuntimeTask>();
	}
	
	private synchronized String generateTaskId() {
		return this.m_idIndex+++"";
	}

	@Override
	public HAPRuntimeInfo getRuntimeInfo() {		return HAPRuntimeManager.RUNTIME_JS_RHION;	}

	/**
	 * embed gateway point into rhino env which provide different gateway by name. 
	 * @param name
	 * @param gateWayPoint
	 */
	private void embedGatewayPoint(){
		try{
			HAPGatewayEmbededPoint embededPoint = new HAPGatewayEmbededPoint(this.m_gatewayManager, this, this.m_scope);
	        Object wrappedObject = Context.javaToJS(embededPoint, this.m_scope);
	        NativeObject nosliwObj = (NativeObject)this.m_scope.get("nosliw", m_scope);
	        Function createNodeFun = (Function)nosliwObj.get("createNode");
	        createNodeFun.call(Context.enter(), m_scope, nosliwObj, new Object[]{HAPRuntimeEnvironmentJS.NODENAME_GATEWAY, wrappedObject});
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			Context.exit();
		}
	}
	
	//register relevant gateway
	private void registerGateway(){
		//register TaskResponse gateway
		m_gatewayManager.registerGateway(new HAPGatewayRhinoTaskResponse(this));
	}
	
//	@Override
//	public HAPServiceData executeExpressionSync(String expressionStr, Map<String, HAPData> parmsData) {
//		HAPExecutableEntityExpressionDataGroup expression = this.getRuntimeEnvironment().getExpressionManager().getExpression(expressionStr);
//		//execute task
//		HAPRuntimeTask task = new HAPRuntimeTaskExecuteRhinoDataExpressionGroup(new HAPInfoRuntimeTaskDataExpressionGroup(expression, null, parmsData, null), this.getRuntimeEnvironment());
//		HAPServiceData serviceData = this.executeTaskSync(task);
//		return serviceData;
//
//		return null;
		
//		//new expression definition
//		HAPResourceDefinitionExpressionGroup expDefinition = new HAPResourceDefinitionExpressionGroup(expressionStr); 
//		//build expression obj
//		HAPProcessTracker processTracker = new HAPProcessTracker();
//		HAPExecutableExpressionGroup expression = this.getRuntimeEnvironment().getExpressionSuiteManager().compileExpression(expDefinition, null, null, null, HAPUtilityExpressionProcessConfigure.setDontDiscovery(null), processTracker);
//		//execute task
//		HAPRuntimeTask task = new HAPRuntimeTaskExecuteExpressionRhino(expression, parmsData, null);
//		HAPServiceData serviceData = this.executeTaskSync(task);
//		return serviceData;
//	}

//	@Override
//	public HAPServiceData executeDataOperationSync(HAPDataTypeId dataTypeId, String operation,
//			List<HAPOperationParm> parmsData) {
		//execute task
//		HAPRuntimeTask task = new HAPRuntimeTaskExecuteDataOperationRhino(dataTypeId, operation, parmsData);
//		HAPServiceData serviceData = this.executeTaskSync(task);
//		return serviceData;
//		
//		return null;
//	}

	@Override
	public void executeTask(HAPRuntimeTask task){
		//prepare expression id
		task.setId(generateTaskId());

		this.m_tasks.put(task.getTaskId(), task);

		task.registerListener(new HAPRunTaskEventListener(){
			@Override
			public void finish(HAPRuntimeTask task) {
				HAPServiceData taskServiceData = task.getResult();
				if(taskServiceData.isSuccess()){
//					System.out.println("Task " + task.getTaskType() + " " + task.getTaskId() + " finished successfully!!!");
//					System.out.println(taskServiceData.getData());
				}
				else{
//					System.err.println("Task " + task.getTaskType() + " " + task.getTaskId() + " finished fail!!!");
//					System.err.println(taskServiceData);
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

	/**
	 * Init essencial object, include base, all the library for expression and all basic data type
	 * @param context
	 * @param parent
	 * @return
	 */
	private Scriptable init(Context context, Scriptable parent){
		this.m_scope = context.initStandardObjects(null);

		//core library
		List<HAPResourceIdSimple> resourceIds = new ArrayList<HAPResourceIdSimple>();
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("external.Underscore", "1.6.0")));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("external.Backbone", "1.1.2")));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.core", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.runtimerhinoinit", null)));

		List<HAPResourceInfo> resourceIdInfos = new ArrayList<HAPResourceInfo>();
		for(HAPResourceIdSimple resourceId : resourceIds){
			resourceIdInfos.add(new HAPResourceInfo(resourceId).withInfo(ADDTORESOURCEMANAGER, ADDTORESOURCEMANAGER));
		}
		this.loadResources(resourceIdInfos);

		//embed gateway point
		embedGatewayPoint();
		
		//register gateway
		this.registerGateway();
		
		//other library
		resourceIds = new ArrayList<HAPResourceIdSimple>();
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.constant", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.logging", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.common", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.error", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.data", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.expression", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.activity", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.process", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.sequence", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.task", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.taskscript", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.taskflow", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.scripttaskgroup", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.dataservice", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.request", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.id", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.resource", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.dataservice", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.component", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.complexentity", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.module", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.entitycontainer", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.testcomponent", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.iovalue", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.valueport", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.scriptbased", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.statemachine", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.security", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.variable", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.rule", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.error", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.runtime", null)));
		resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPJSLibraryId("nosliw.runtimerhino", null)));
		
		resourceIdInfos = new ArrayList<HAPResourceInfo>();
		for(HAPResourceIdSimple resourceId : resourceIds){
			resourceIdInfos.add(new HAPResourceInfo(resourceId).withInfo(ADDTORESOURCEMANAGER, ADDTORESOURCEMANAGER));
		}
		this.loadResources(resourceIdInfos);
		
		//data type
		
		return m_scope;
	}

	private void loadResources(List<HAPResourceInfo> resourceInfos){
		try {
			Map<String, String> jsonMap = new LinkedHashMap<String, String>();
			
			jsonMap.put(HAPGatewayResource.COMMAND_LOADRESOURCES_RESOURCEINFOS, HAPManagerSerialize.getInstance().toStringValue(resourceInfos, HAPSerializationFormat.JSON));
			HAPServiceData serviceData = this.m_gatewayManager.executeGateway(HAPConstantShared.GATEWAY_RESOURCE, HAPGatewayResource.COMMAND_LOADRESOURCES, new JSONObject(HAPUtilityJson.buildMapJson(jsonMap)), this.getRuntimeInfo());

			HAPGatewayOutput output = (HAPGatewayOutput)serviceData.getData();
			List<HAPJSScriptInfo> scripts = output.getScripts();
			for(HAPJSScriptInfo scriptInfo : scripts){		this.loadScript(scriptInfo);	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void loadScript(HAPJSScriptInfo scriptInfo) throws Exception{
		this.loadScript(scriptInfo, this.m_scope);
	}

	private void loadScript(HAPJSScriptInfo scriptInfo, Scriptable scope){
		String file = scriptInfo.isFile(); 
		if(HAPUtilityBasic.isStringEmpty(file)){
			//for script
			this.m_sciprtTracker.addScript(scriptInfo.getScript());
		}
		else{
			//for file
			this.m_sciprtTracker.addFile(file);
		}
		HAPRhinoRuntimeUtility.loadScript(scriptInfo.getScript(), scope, scriptInfo.getName(), !HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSLIBRARY.equals(scriptInfo.getType()));
	}
	
	public void loadScriptFromFile(String fileName, Class cs, Scriptable scope){
		HAPJSScriptInfo scriptInfo = HAPJSScriptInfo.buildByFile(HAPUtilityFile.getFileNameOnClassPath(cs, fileName), "File__" + fileName);
		this.loadScript(scriptInfo, scope==null?this.m_scope:scope);
	}

	public void loadTaskScript(HAPJSScriptInfo scriptInfo, String taskId){		this.loadScript(scriptInfo, m_scope);	}

	@Override
	public void close(){
//		this.m_sciprtTracker.export();
		HAPRhinoRuntimeUtility.exportToHtml();
		this.m_gatewayManager.unregisterGateway(HAPConstantShared.GATEWAY_RHINOTASKRESPONSE);
	}
	
	@Override
	public void start(){
		try {
	        this.m_sciprtTracker = new HAPScriptTracker();
			
			ContextFactory factory = ContextFactory.getGlobal(); 

//			this.debug(factory);
			
		    Context context = factory.enterContext();
			
	        this.init(context, null);

	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	    finally{
	    	Context.exit();
	    }
	}
	
	private void debug(ContextFactory factory){
	    Main dbg = new Main("Hello");
	    dbg.attachTo(factory);
		
//        System.setIn(dbg.getIn());
//        System.setOut(dbg.getOut());
//        System.setErr(dbg.getErr());
        
//	    dbg.setBreakOnEnter(true);
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
	
}
