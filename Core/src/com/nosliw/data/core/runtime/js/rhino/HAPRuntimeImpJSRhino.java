package com.nosliw.data.core.runtime.js.rhino;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
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
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.runtime.HAPExpressionResult;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPResourceDiscovery;
import com.nosliw.data.core.runtime.HAPResourceHelper;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPJSLibraryId;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeImpJS;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSScriptUtility;

public class HAPRuntimeImpJSRhino extends HAPRuntimeImpJS implements HAPRuntimeCallBack{

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
	
	//store all the expression execution result call back
	private Map<String, HAPExpressionResult> m_results;
	
	public HAPRuntimeImpJSRhino(){}
	
	public HAPRuntimeImpJSRhino(HAPResourceDiscovery resourceDiscovery, HAPResourceManager resourceMan){
		this.m_resourceDiscovery = resourceDiscovery;
		this.m_resourceManager = resourceMan;
		this.m_results = new LinkedHashMap<String, HAPExpressionResult>();
		this.m_taskScope = new LinkedHashMap<String, Scriptable>();
	}

	public void loadScriptFromFile(String fileName, Class cs){
		this.loadScriptFromFile(fileName, m_context, m_scope, cs);
	}
	
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
	}
	
	@Override
	public void returnResult(String expressionId, String result){
		HAPExpressionResult resultCallBack = this.m_results.remove(expressionId);
		HAPDataWrapper resultData = new HAPDataWrapper(result); 
		resultCallBack.result(resultData);
	}
	
	
	@Override
	public void executeExpression(HAPExpression expression, HAPExpressionResult result) {
		//prepare expression id
		String expressionId = expression.getId();
		if(HAPBasicUtility.isStringEmpty(expressionId)){
			expressionId = "Expression" + this.m_idIndex++ + "__" + expression.getExpressionInfo().getName();
			expression.setId(expressionId);
		}
		
		
		//init rhino runtime, init scope
		String taskId = this.initExecuteExpression();
		Scriptable scope = this.getTaskScope(taskId); 
		
		//discover required resources
//		List<HAPResourceId> resourcesId = this.getResourceDiscovery().discoverResourceRequirement(expression);
		
		//find which resource is missing
//		List<HAPResourceId> missedResourceId = this.findMissedResources(resourcesId);
		
		//load missed resources
//		this.loadResources(missedResourceId, scope, this.m_context);
		
		//execute expression
		this.m_results.put(expressionId, result);
		String script = HAPRuntimeJSScriptUtility.buildScriptForExecuteExpression(expression);
		this.loadScript(script, m_context, m_scope, expressionId);
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
		
		for(HAPJSScriptInfo scriptInfo : scriptsInfo){
			String file = scriptInfo.isFile(); 
			if(HAPBasicUtility.isStringEmpty(file)){
				//for script
				this.m_sciprtTracker.addScript(scriptInfo.getScript());
			}
			else{
				//for file
				this.m_sciprtTracker.addFile(file);
			}
			this.loadScript(scriptInfo.getScript(), context, scope, scriptInfo.getName());
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
	
	private String getTaskId(){return System.currentTimeMillis()+"";}
	
	private int index = 1; 
	private void loadScript(String script, Context context, Scriptable scope, String name){
		try{
			String folder = "C:/Temp/ScriptExport/scripts/";
			String scriptTempFile = folder + new Date().toString() + "_" + index + "_" + name+".js";
			index++;
			HAPFileUtility.writeFile(scriptTempFile, script);
			
			context.evaluateString(scope, script, name, 1, null);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void loadScriptFromFile(String fileName, Context context, Scriptable scope, Class cs){
		String script = HAPFileUtility.readFile(HAPFileUtility.getInputStreamOnClassPath(cs==null?this.getClass():cs, fileName));
		this.m_sciprtTracker.addFile(HAPFileUtility.getFileNameOnClassPath(cs==null?getClass():cs, fileName));
		this.loadScript(script, context, scope, fileName);
	}
	
	class ResourceIdJS{
		public String type;
		public String id;
		
		public ResourceIdJS(String type, String id){
			this.type = type;
			this.id = id;
		}
	}
	
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

//	        Object wrappedRuntime = Context.javaToJS(new HAPRuntimeImpJSRhino(null, null), this.m_scope);
	        Object wrappedRuntime = Context.javaToJS(this, this.m_scope);
	        ScriptableObject.putProperty(this.m_scope, "aaaa", wrappedRuntime);
	        
	        
	        System.setIn(dbg.getIn());
	        System.setOut(dbg.getOut());
	        System.setErr(dbg.getErr());
	        
//		    dbg.setBreakOnEnter(true);
		    dbg.setBreakOnExceptions(true);
		    dbg.setScope(m_scope);
		    dbg.setSize(800, 600);
		    dbg.setVisible(true);
		    dbg.setExitAction(new ExitOnClose());	    
		    
	        this.loadScriptFromFile("init.js", m_context, m_scope, null);
	     
//	        global.defineProperty( "resourceService", this, ScriptableObject.CONST );	        
	        
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
}
