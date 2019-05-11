function(gate){
	var node_createServiceRequestInfoCommon = nosliw.getNodeData("request.request.createServiceRequestInfoCommon");
	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");
	var node_ServiceRequestExecuteInfo = nosliw.getNodeData("request.entity.ServiceRequestExecuteInfo");
	var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
	var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_createComponentDataView = nosliw.getNodeData("component.createComponentDataView");

	var loc_gate = gate;
	var loc_uiModule = loc_gate.getComponent();
	var loc_view;
	var loc_componentDataView;
	
	var loc_out = {
			
		processComponentEvent : function(eventName, eventData, request){
		},
		
		getExecuteCommandRequest : function(command, parms, handlers, request){
			
		},
		
		updateView : function(view){
			loc_view = view;
			
			loc_componentDataView = node_createComponentDataView();
			$(loc_view).append(loc_componentDataView.getView());
			
			var childRoot = $('<div></div>');
			$(loc_view).append(childRoot);
			return childRoot.get();
		},
		
		getInterface : function(){
			return {
			}
		},
		
		getDeactiveRequest :function(handlers, request){
		},
		
		getSuspendRequest :function(handlers, request){
		},
		
		getResumeRequest :function(handlers, request){
		},

		getInitRequest :function(handlers, requestInfo){
			loc_componentDataView.setComponent(loc_uiModule);
		},
	};
	return loc_out;
}
