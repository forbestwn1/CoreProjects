//get/create package
var packageObj = library.getChildPackage("service");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
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
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIModuleRequest = function(uiModule, handlers, request){

	var module = node_createUIModule(uiModule);
	
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createUIModule", {"uiModule":uiModule}), handlers, request);

	var buildModuleUIRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("BuildModuleUIs", {}), {
		success : function(request, resultSet){
			_.each(resultSet.getResults(), function(moduleUI, name){
				module.prv_addUI(name, moduleUI);
			});
			return module;
		}
	});

	// build uis
	_.each(uiModule[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_UI], function(ui, name){
		buildModuleUIRequest.addRequest(name, node_createModuleUIRequest(ui, module.prv_getContext()));
	});
	out.addRequest(buildModuleUIRequest);
	
	return out;
};	
	
var node_createUIModule = function(uiModule){

	var loc_context;
	
	var loc_uis = {};

	var loc_uiStacks = [];
	
	var loc_eventObject = node_createEventObject();

	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(uiModule){
		loc_context = node_contextUtility.buildContext(uiModule[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXT_ELEMENT], undefined);
	};

	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		
	};

	var loc_env = {
			getPresentUIRequest : function(uiName, mode){
				
			},
			
			
	};
	
	var loc_moduleUIEventHandler = function(eventName, eventData){
		
	};
	
	var loc_buildPage = function(moduleUI, env){
		var pageDiv = $("<div data-role='page' id='"+uiModule.getName()+"'></div>");
		uiModule.getPage().appendTo(pageDiv);
		pageDiv.appendTo(evn.root);
	};
	
	var loc_getExecuteUIModuleRequest = function(input, env, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteUIModule", {"uiModule":uiModule, "input":input, "env":env}), handlers, request);
		
		//init
		out.addRequest(node_contextUtility.getGetContextValueRequest(loc_context), {
			success :function(request, contextValue){
				return nosliw.runtime.getProcessService().getExecuteEmbededProcessRequest(uiModule[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_PROCESS].init, contextValue);
			}
		});

		return out;
	};
	
	var loc_out = {
		
		prv_addUI : function(name, ui){
			loc_uis[name] = ui;
			//register listener for module ui
			ui.registerListener(loc_eventObject, loc_moduleUIEventHandler);
			loc_buildPage(ui, env);
		},
		
		prv_getContext : function(){  return loc_context;  },
		
		getExecuteRequest : function(input, env, handlers, requester_parent){
			return loc_getExecuteUIModuleRequest(input, env, handlers, requester_parent);
		},
			
		executeUIModuleRequest : function(input, env, handlers, requester_parent){
			var requestInfo = this.getExecuteRequest(input, env, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
	};

	loc_out = node_buildServiceProvider(loc_out, "processService");
	
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_UIMODULE);

	node_getLifecycleInterface(loc_out).init(uiModule);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});

nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
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

//Register Node by Name
packageObj.createChildNode("createUIModuleService", node_createUIModuleService); 

})(packageObj);
