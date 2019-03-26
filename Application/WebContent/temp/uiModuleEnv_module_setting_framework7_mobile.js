
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"id":"module_setting_framework7_mobile",
"type":"uiModuleEnv"
},
"children":[],
"dependency":{},
"info":{}
}, function(uiModule){
	var node_createServiceRequestInfoCommon = nosliw.getNodeData("request.request.createServiceRequestInfoCommon");
	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");
	var node_ServiceRequestExecuteInfo = nosliw.getNodeData("request.entity.ServiceRequestExecuteInfo");
	var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
	var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");

	var loc_uiModule = uiModule;
	
	//runtime execute request through this method, so that ui can do something (for instance, spinning circle)
	loc_processRequest = function(request){     node_requestServiceProcessor.processRequest(request);   };
	
	var loc_out = {
			getPresentUIRequest : function(uiName, mode, handlers, requestInfo){
				return loc_getTransferToRequest(uiName, mode, handlers, requestInfo);
			},
			
			getExecuteCommandRequest : function(uiName, commandName, commandData, handlers, requestInfo){
				return loc_uiModule.getUI(uiName).getExecuteCommandRequest(commandName, commandData, handlers, requestInfo);
			},
			
			processUIEvent : function(eventName, uiName, eventData, request){
				var eventProcessor = loc_uiModule.getStatelessData().eventProcessor;
				if(eventProcessor!=undefined){
					eventProcessor(eventName, uiName, eventData, request);
				}
			},
			
			processRequest : function(request){     loc_processRequest(request);   },
			
			getInitRequest :function(handlers, requestInfo){
				var out = node_createServiceRequestInfoCommon(undefined, handlers, requestInfo);
				out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(function(requestInfo){
					//put ui to root
					_.each(loc_uiModule.getUIs(), function(ui, index){
						ui.getPage().appendTo(loc_uiModule.getStatelessData().root);
					});
					
					out.executeSuccessHandler();
				}));
				return out;
			},
			
			registerEventListener : function(){
				
			}
	};
	return loc_out;
}
, {"loadPattern":"file"
});

