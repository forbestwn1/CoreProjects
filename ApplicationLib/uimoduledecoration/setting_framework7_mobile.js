function(gate){
	var node_createServiceRequestInfoCommon = nosliw.getNodeData("request.request.createServiceRequestInfoCommon");
	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");
	var node_ServiceRequestExecuteInfo = nosliw.getNodeData("request.entity.ServiceRequestExecuteInfo");
	var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
	var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_createUIDecorationsRequest = nosliw.getNodeData("uipage.createUIDecorationsRequest");
	var node_commandResult = nosliw.getNodeData("component.commandResult");

	var loc_gate = gate;
	var loc_uiModule = loc_gate.getComponent();
	var loc_view;
	var loc_decoration;
	
	//runtime execute request through this method, so that ui can do something (for instance, spinning circle)
	loc_processRequest = function(request){     node_requestServiceProcessor.processRequest(request);   };
	
	var loc_out = {
			
		processComponentEvent : function(eventName, eventData, request){
		},
		
		getExecuteCommandRequest : function(command, parms, handlers, request){
			if(command=="updateModuleInfo"){
				var contextUpdate = {};
				if(parms.persist!=undefined)  contextUpdate.persist = parms.persist;
				if(parms.modified!=undefined)  contextUpdate.modified = parms.modified;
				if(parms.name!=undefined)  contextUpdate.name = parms.name;
				
				return new node_commandResult(loc_decoration.getUpdateContextRequest(contextUpdate, handlers, request));
			}
		},
		
		updateView : function(view){
			loc_view = view;
			loc_decoration.appendTo(view);
			return loc_decoration.getPlaceHolderView();
		},

		getPreDisplayInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(node_createUIDecorationsRequest([loc_gate.getConfigureData().uiResource], {
				success : function(request, decs){
					loc_decoration = decs[0];
					loc_decoration.registerEventListener(undefined, function(eventName, eventData, request){
						loc_gate.trigueEvent(eventName, eventData);
					});
				}
			}));
			return out;
		},
		
		getInitRequest :function(handlers, requestInfo){
			var out = node_createServiceRequestInfoCommon(undefined, handlers, requestInfo);
			out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(function(requestInfo){
				//put ui to root
				_.each(loc_uiModule.getUIs(), function(ui, index){
//					ui.getPage().appendTo(loc_gate.getConfigureData().root);
					ui.getPage().appendTo(loc_view);
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
