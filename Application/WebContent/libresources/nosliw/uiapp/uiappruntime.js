//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_objectOperationUtility;
	var node_IOTaskResult;
	var node_NormalActivityOutput;
	var node_EndActivityOutput;
	var node_ProcessResult;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_requestServiceProcessor;
	var node_contextUtility;
	var node_createUIModuleRequest;
	var node_ioTaskUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createAppRuntimeRequest = function(uiAppDef, appConfigure, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createAppRuntime", {}), handlers, request);
	out.addRequest(node_createUIModuleRequest(uiModuleDef, input, view, decorations, {
		success : function(request, uiModule){
			return loc_createModuleRuntime(uiModule, envFactory(uiModule));
		}
	}));
	return out;
};

var loc_createModuleRuntime = function(uiModule, env){
	
	var loc_uiModule = uiModule;
	var loc_env = env;
	
	loc_uiModule.registerUIEventListener(function(eventName, uiName, eventData, request){
		var eventHandler = loc_uiModule.getEventHandler(uiName, eventName);
		if(eventHandler!=undefined){
			var extraInput = {
				EVENT : {
					event : eventName,
					data : eventData
				} 
			};
			loc_env.processRequest(loc_getExecuteModuleProcessRequest(eventHandler[node_COMMONATRIBUTECONSTANT.DEFINITIONMODULEUIEVENTHANDER_PROCESS], extraInput, undefined, request));
		}
		else{
			loc_env.processUIEvent(eventName, uiName, eventData, request);
		}
	});

	var loc_getExecuteModuleProcessRequest = function(process, extraInput, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteModuleProcess", {"process":process}), handlers, request);
		var processInput = {};
		//input data association
		_.each(node_ioTaskUtility.getContextTypes(), function(categary, index){
			var context = loc_uiModule.getContext()[categary];
			if(context!=undefined){
				_.each(context, function(ele, name){
					processInput[name] = ele;
				});
			}			
		});
		
		//append extra input
		if(extraInput!=undefined){
			_.each(extraInput, function(input, name){
				processInput[name] = input;
			});
		}
		
		out.addRequest(nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_env).getExecuteEmbededProcessRequest(process, processInput, {
			success : function(request, processResult){
				loc_uiModule.setContext(node_ioTaskUtility.assignToContext(processResult.value, loc_uiModule.getContext(), false));
			}
		}));
		return out;
	};
	
	var loc_getExecuteModuleProcessByNameRequest = function(processName, extraInput, handlers, request){
		var process = loc_uiModule.getProcess(processName);
		if(process!=undefined)  return loc_getExecuteModuleProcessRequest(process, extraInput, handlers, request);
	};
	
	var loc_out = {
		getModule : function(){  return loc_uiModule;  },

		//init runtime, env
		getInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("InitUIModuleRuntime", {}), handlers, request);
			if(loc_env.getInitRequest!=undefined)  out.addRequest(loc_env.getInitRequest());
			return out;
		},	
			
		//start 
		getStartRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("StartUIModuleRuntime", {}), handlers, request);
			//init module
			out.addRequest(loc_getExecuteModuleProcessByNameRequest("init"));
			return out;
		},
		
		executeStartRequest : function(handlers, request){		loc_env.processRequest(this.getStartRequest(handlers, request));	},
		
	};
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
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityResult", function(){node_IOTaskResult = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityOutput", function(){node_NormalActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.EndActivityOutput", function(){node_EndActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.ProcessResult", function(){node_ProcessResult = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uimodule.createUIModuleRequest", function(){node_createUIModuleRequest = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.ioTaskUtility", function(){node_ioTaskUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createAppRuntimeRequest", node_createAppRuntimeRequest); 

})(packageObj);