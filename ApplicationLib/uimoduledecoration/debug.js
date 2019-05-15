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
	var loc_component = loc_gate.getComponent();
	var loc_componentIOContext = loc_component.getIOContext();

	var loc_view;
	var loc_debugView; 
	var loc_dataView;
	var loc_childRoot;

	var loc_updateDataSet = function(request){	
		node_requestServiceProcessor.processRequest(loc_componentIOContext.getGetDataSetValueRequest({
			success : function(request, dataSet){
				var kkkk = loc_componentIOContext;
				loc_dataView.val(JSON.stringify(dataSet, null, 4));	
			}
		}, request));
	};
	

	var loc_setup = function(request){
		loc_componentIOContext.registerEventListener(undefined, function(eventName, eventData, request){
			loc_updateDataSet(request);
		});
		loc_updateDataSet(request);
	};
	
	
	var loc_out = {
			
		processComponentEvent : function(eventName, eventData, request){
		},
		
		getExecuteCommandRequest : function(command, parms, handlers, request){
			
		},
		
		updateView : function(view, request){
			loc_view = $(view);

			loc_debugView = $('<div>Component Data: </div>'); 
			loc_dataView = $('<textarea rows="5" cols="150" style="resize: none;" data-role="none"></textarea>');
			loc_debugView.append(loc_dataView);
			loc_view.append(loc_debugView);
			loc_childRoot = $('<div></div>');
			$(loc_view).append(loc_childRoot);
			return loc_childRoot.get();
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

		getInitRequest :function(handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				loc_setup(request);
			}, handlers, request);
		},
	};
	return loc_out;
}
