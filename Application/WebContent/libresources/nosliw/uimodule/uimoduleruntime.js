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
	var node_IOTaskResult;
	var node_NormalActivityOutput;
	var node_EndActivityOutput;
	var node_ProcessResult;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_requestServiceProcessor;
	var node_contextUtility;
	var node_createUIModuleRequest;
	
//*******************************************   Start Node Definition  ************************************** 	
 
var loc_createRuntimeRequest = function(uiModuleDef, input, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createModuleRuntime", {"moduleDef":moduleDef, "input":input}), handlers, requestInfo);

	node_createUIModuleRequest(uiModuleDef, input, {
		success : function(request, uiModule){
			return loc_createRuntime(uiModule);
		}
	});
	
};

var loc_createRuntime = function(uiModule){
	
	var loc_uiModule = uiModule;
	
	loc_uiModule.registerUIListener(function(eventName, eventData){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("processUIEvent", {"eventName":eventName, "eventData":eventData}));
		out.addRequest(node_contextUtility.getGetContextValueRequest(loc_context, {
			success : function(request, contextValue){
				var input = contextValue[node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PUBLIC];
				var inputEvent = input.EVENT;
				if(inputEvent==undefined){
					inputEvent = {};
					input.EVENT = inputEvent;
				}
				inputEvent.event = eventName;
				inputEvent.data = eventData;
				return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_env).
				getExecuteEmbededProcessRequest(loc_moduleUI.getEventHandler(eventName)[node_COMMONATRIBUTECONSTANT.DEFINITIONMODULEUIEVENTHANDER_PROCESS], input);
			}
		}));
		node_requestServiceProcessor.processRequest(out);
	});

	
	var loc_env = {
		setModule : function(module){
			loc_module = module;
		},
		
		getPresentUIRequest : function(uiName, mode){
			
		},
		
		getPreExecuteModuleRequest :function(uiModule, handlers, requestInfo){
			out = node_createServiceRequestInfoSimple(new node_ServiceInfo("PreExecuteModule", {"uiModule":uiModule}), 
				function(requestInfo){
					_.each(uiModule.getUIs(), function(ui, index){
						var pageDiv = $("<div data-role='page' id='"+ui.getName()+"'></div>");
						ui.getPage().appendTo(pageDiv);
						pageDiv.appendTo($('#testDiv'));
					});
				}, 
				handlers, requestInfo);
			return out;
		},
		
		executeUICommand : function(uiName, commandName, commandData){
			loc_module.getUI(uiName).executeCommand(commandName, commandData);
		}
	};

	var loc_buildPage = function(moduleUI, env){
		var pageDiv = $("<div data-role='page' id='"+uiModule.getName()+"'></div>");
		uiModule.getPage().appendTo(pageDiv);
		pageDiv.appendTo(evn.root);
	};
	
	var loc_out = {
		startRequest : function(){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteUIModule", {"uiModule":uiModule, "input":input, "env":env}), handlers, request);
			
			//env pre exe
			out.addRequest(loc_env.getPreExecuteModuleRequest(loc_out));
			
			//init
			out.addRequest(node_contextUtility.getGetContextValueRequest(loc_context), {
				success :function(request, contextValue){
					return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_env).getExecuteEmbededProcessRequest(uiModule[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_PROCESS].init, contextValue);
				}
			});

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

//Register Node by Name
packageObj.createChildNode("createUIModuleService", node_createUIModuleService); 

})(packageObj);
