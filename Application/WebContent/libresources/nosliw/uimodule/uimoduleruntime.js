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

var loc_envFactory = function(uiModule){
	
	var loc_uiModule = uiModule;
	
	var loc_buildPage = function(moduleUI, env){
		var pageDiv = $("<div data-role='page' id='"+uiModule.getName()+"'></div>");
		uiModule.getPage().appendTo(pageDiv);
		pageDiv.appendTo(evn.root);
	};
	
	var loc_out = {
			getPresentUIRequest : function(uiName, mode){
				
			},
			
			getPreStartModuleRequest :function(handlers, requestInfo){
				out = node_createServiceRequestInfoSimple(new node_ServiceInfo("PreExecuteModule", {"uiModule":uiModule}), 
					function(requestInfo){
						_.each(loc_uiModule.getUIs(), function(ui, index){
							var pageDiv = $("<div data-role='page' id='"+ui.getName()+"'></div>");
							ui.getPage().appendTo(pageDiv);
							pageDiv.appendTo($('#testDiv'));
						});
					}, 
					handlers, requestInfo);
				return out;
			},
			
			executeUICommand : function(uiName, commandName, commandData){
				loc_uiModule.getUI(uiName).executeCommandRequest(commandName, commandData);
			}
			
	};
	return loc_out;
};
	
	
var node_createModuleRuntimeRequest = function(uiModuleDef, input, envFactory, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createModuleRuntime", {"moduleDef":uiModuleDef, "input":input}), handlers, request);
	out.addRequest(node_createUIModuleRequest(uiModuleDef, input, {
		success : function(request, uiModule){
			return loc_createModuleRuntime(uiModule, loc_envFactory(uiModule));
		}
	}));
	return out;
};

var loc_createModuleRuntime = function(uiModule, env){
	
	var loc_uiModule = uiModule;
	var loc_env = env;
	
	loc_uiModule.registerUIListener(function(eventName, uiName, eventData){
		var eventHandler = loc_uiModule.getEventHandler(uiName, eventName);
		if(eventHandler!=undefined){
			var extraInput = {
				EVENT : {
					event : eventName,
					data : eventData
				} 
			};
			node_requestServiceProcessor.processRequest(loc_getExecuteModuleProcessRequest(eventHandler[node_COMMONATRIBUTECONSTANT.DEFINITIONMODULEUIEVENTHANDER_PROCESS], extraInput));
		}
	});

	var loc_getExecuteModuleProcessRequest = function(process, extraInput, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteModuleProcess", {"process":process}), handlers, request);
		var processInput = {};
		_.each(node_ioTaskUtility.getContextTypes(), function(categary, index){
			var context = loc_uiModule.getContext()[categary];
			if(context!=undefined){
				_.each(context, function(ele, name){
					processInput[name] = ele;
				});
			}			
		});
		
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
		getStartRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("StartUIModule", {"uiModule":loc_uiModule}), handlers, request);
			
			//env pre exe
			out.addRequest(loc_env.getPreStartModuleRequest());
			
			//init
			out.addRequest(loc_getExecuteModuleProcessByNameRequest("init"));

			return out;
		},
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
packageObj.createChildNode("createModuleRuntimeRequest", node_createModuleRuntimeRequest); 

})(packageObj);
