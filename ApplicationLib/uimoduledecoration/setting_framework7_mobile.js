function(gate){
	var node_createServiceRequestInfoCommon = nosliw.getNodeData("request.request.createServiceRequestInfoCommon");
	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");
	var node_ServiceRequestExecuteInfo = nosliw.getNodeData("request.entity.ServiceRequestExecuteInfo");
	var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
	var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");

	var loc_gate = gate;
	var loc_uiModule = loc_gate.getComponent();
	
	//runtime execute request through this method, so that ui can do something (for instance, spinning circle)
	loc_processRequest = function(request){     node_requestServiceProcessor.processRequest(request);   };
	
	var loc_out = {
			
		processComponentEvent : function(eventName, eventData, request){
		},
		
		getExecuteCommandRequest : function(command, parms, handlers, request){
		},

		getInitRequest :function(handlers, requestInfo){
			var out = node_createServiceRequestInfoCommon(undefined, handlers, requestInfo);
			out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(function(requestInfo){
				//put ui to root
				_.each(loc_uiModule.getUIs(), function(ui, index){
					ui.getPage().appendTo(loc_gate.getConfigure().root);
				});
				
				out.executeSuccessHandler();
			}));
			return out;
		},
		
		getInterface : function(){
			return {
			}
		},
	};
	return loc_out;
}
