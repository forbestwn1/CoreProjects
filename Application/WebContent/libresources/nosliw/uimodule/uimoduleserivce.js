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

var node_createUIModuleService = function(){
	
	var loc_createEnv = function(){
		var loc_module;
		
		var loc_out = {
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
					loc_module.getUI(uiName).executeCommand(commandName, commandData.parm);
				}
			};
		
		return loc_out;
	};
	
	
	var loc_buildPage = function(moduleUI, env){
		var pageDiv = $("<div data-role='page' id='"+uiModule.getName()+"'></div>");
		uiModule.getPage().appendTo(pageDiv);
		pageDiv.appendTo(evn.root);
	};
	
	var loc_getExecuteUIModuleRequest = function(uiModule, input, env, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteUIModule", {"uiModule":uiModule, "input":input, "env":env}), handlers, request);

		var env = loc_createEnv();
		out.addRequest(node_createUIModuleRequest(uiModule, input, env, {
			success : function(request, uiModuleObj){
				env.setModule(uiModuleObj);
				return uiModuleObj.getExecuteRequest(input);
			}
		}));
		
		return out;
	};
	
	var loc_out = {

		getExecuteUIModuleRequest : function(id, input, env, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteUIModuleResource", {"id":id, "input":input}), handlers, requestInfo);

			out.addRequest(nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([id], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIMODULE, {
				success : function(requestInfo, uiModules){
					var uiModule = uiModules[id];
					return loc_getExecuteUIModuleRequest(uiModule, input, env);
				}
			}));
			
			return out;
		},
			
		executeUIModuleRequest : function(id, input, env, handlers, requester_parent){
			var requestInfo = this.getExecuteUIModuleRequest(id, input, env, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
			
	};

	loc_out = node_buildServiceProvider(loc_out, "processService");
	
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
