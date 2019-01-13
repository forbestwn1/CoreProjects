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

//*******************************************   Start Node Definition  ************************************** 	

var node_createUIModuleService = function(){

	var loc_getExecuteUIModuleRequest = function(uiModule, input, handlers, request){
		
		//init
		
		
		//
		
		
		//init, build context
		var context = process[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_INITSCRIPT](input);

		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteProcess", {"process":process, "context":context}), handlers, request);

		var startActivityId = process[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_STARTACTIVITYID];
		var activities = process[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_ACTIVITY];
		out.addRequest(loc_getExecuteActivitySequenceRequest(startActivityId, activities, context, {
			success : function(requestInfo, endActivityOutput){
				return loc_getGenerateDataAssociationOutputRequest(
						process[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_RESULT][endActivityOutput.resultName],
						endActivityOutput.context, {
							success : function(requestInfo, processOutputValue){
								return new node_ProcessResult(endActivityOutput.resultName, processOutputValue);
							}
						});
			}
		}));
		return out;
	};
	
	var loc_out = {

		getExecuteUIModuleRequest : function(uiModule, input, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteUIModuleResource", {"id":id, "input":input}), handlers, requestInfo);

			
			return out;
		},
			
		executeUIModuleRequest : function(id, input, handlers, requester_parent){
			var requestInfo = this.getExecuteProcessRequest(id, input, handlers, requester_parent);
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
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityResult", function(){node_NormalActivityResult = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityOutput", function(){node_NormalActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.EndActivityOutput", function(){node_EndActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.ProcessResult", function(){node_ProcessResult = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIModuleService", node_createUIModuleService); 

})(packageObj);
