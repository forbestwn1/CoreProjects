//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_ServiceInfo;
	var node_objectOperationUtility;
	var node_NormalActivityResult;
	var node_NormalActivityOutput;
	var node_EndActivityOutput;
	var node_ProcessResult;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_requestServiceProcessor;
	var node_contextUtility;
	var node_createEventObject;
	var node_makeObjectWithLifecycle;
	var node_makeObjectWithType;
	var node_getLifecycleInterface;
	var node_createModuleUIRequest;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIModuleRequest = function(uiModule, externalContext, env, handlers, request){

	var module = node_createUIModule(uiModule, externalContext, env);
	
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createUIModule", {"uiModule":uiModule}), handlers, request);

	var buildModuleUIRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("BuildModuleUIs", {}), {
		success : function(request, resultSet){
			_.each(resultSet.getResults(), function(moduleUI, index){
				module.prv_addUI(moduleUI);
			});
			return module;
		}
	});

	// build uis
	_.each(uiModule[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_UI], function(ui, index){
		buildModuleUIRequest.addRequest(index, node_createModuleUIRequest(ui, module.prv_getContext()));
	});
	out.addRequest(buildModuleUIRequest);
	
	return out;
};	
	
var node_createUIModule = function(uiModule, externalContext, env){

	var loc_env = env;
	
	var loc_context;
	
	var loc_uis = [];
	var loc_uisByName = {};

	var loc_uiStacks = [];
	
	var loc_eventObject = node_createEventObject();

	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(uiModule, externalContext){
		loc_context = node_contextUtility.buildContext(uiModule[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXT_ELEMENT], externalContext);
	};

	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		
	};

	var loc_createModuleUIEventHandler = function(ui){
		var loc_moduleUI = ui;
		var loc_moduleUIEventHandler = function(eventName, eventData){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("processUIEvent", {"eventName":eventName, "eventData":eventData}));
			out.addRequest(node_contextUtility.getGetContextValueRequest(loc_context, {
				success : function(request, contextValue){
					var input = contextValue[node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PUBLIC];
					input.EVENT = eventData;
					return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_env).
					getExecuteEmbededProcessRequest(loc_moduleUI.getEventHandler(eventName)[node_COMMONATRIBUTECONSTANT.DEFINITIONMODULEUIEVENTHANDER_PROCESS], input);
					/*
					var eventCategary = contextValue[node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_INTERNAL];
					if(eventCategary==undefined){
						eventCategary = {};
						contextValue[node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_INTERNAL] = eventCategary;
					}
					eventCategary.EVENT = eventData;
					return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_env).
						getExecuteEmbededProcessRequest(loc_moduleUI.getEventHandler(eventName)[node_COMMONATRIBUTECONSTANT.DEFINITIONMODULEUIEVENTHANDER_PROCESS], contextValue);
					*/
				}
			}));
			node_requestServiceProcessor.processRequest(out);
		};
		return loc_moduleUIEventHandler;
	};
	
	
	var loc_getExecuteUIModuleRequest = function(input, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteUIModule", {"uiModule":uiModule, "input":input, "env":env}), handlers, request);
		
		//env pre exe
		out.addRequest(loc_env.getPreExecuteModuleRequest(loc_out));
		
		//init
		out.addRequest(node_contextUtility.getGetContextValueRequest(loc_context), {
			success :function(request, contextValue){
				return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_env).getExecuteEmbededProcessRequest(uiModule[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_PROCESS].init, contextValue);
			}
		});

		return out;
	};
	
	var loc_out = {
		
		prv_addUI : function(ui){
			loc_uis.push(ui);
			loc_uisByName[ui.getName()] = ui;
			//register listener for module ui
			ui.registerListener(loc_createModuleUIEventHandler(ui));
		},
		
		prv_getContext : function(){  return loc_context;  },
		
		getExecuteRequest : function(input, handlers, requester_parent){
			return loc_getExecuteUIModuleRequest(input, handlers, requester_parent);
		},
		
		getUIs : function(){  return loc_uis;  },
		
		getUI : function(name) {  return loc_uisByName[name];   }
			
	};

	loc_out = node_buildServiceProvider(loc_out, "processService");
	
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_UIMODULE);

	node_getLifecycleInterface(loc_out).init(uiModule, externalContext);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});

nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityResult", function(){node_NormalActivityResult = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityOutput", function(){node_NormalActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.EndActivityOutput", function(){node_EndActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.ProcessResult", function(){node_ProcessResult = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("uimodule.createModuleUIRequest", function(){node_createModuleUIRequest = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIModuleRequest", node_createUIModuleRequest); 

})(packageObj);
