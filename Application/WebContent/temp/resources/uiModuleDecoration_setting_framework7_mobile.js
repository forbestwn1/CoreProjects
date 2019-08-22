
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"id":"setting_framework7_mobile",
"type":"uiModuleDecoration"
},
"children":[],
"dependency":{},
"info":{}
}, function(gate){
	var node_createServiceRequestInfoCommon = nosliw.getNodeData("request.request.createServiceRequestInfoCommon");
	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");
	var node_ServiceRequestExecuteInfo = nosliw.getNodeData("request.entity.ServiceRequestExecuteInfo");
	var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
	var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_createUIDecorationsRequest = nosliw.getNodeData("uipage.createUIDecorationsRequest");
	var node_CommandResult = nosliw.getNodeData("component.CommandResult");
	var node_destroyUtil = nosliw.getNodeData("common.lifecycle.destroyUtil");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");

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
				
				return new node_CommandResult(loc_decoration.getUpdateContextRequest(contextUpdate, handlers, request));
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

		
		getLifeCycleRequest : function(transitName, handlers, request){
			var out;
			if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_INIT){
				out = node_createServiceRequestInfoCommon(undefined, handlers, request);
				out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(function(requestInfo){
					//put ui to root
					_.each(loc_uiModule.getUIs(), function(ui, index){
//						ui.getPage().appendTo(loc_gate.getConfigureData().root);
						ui.getPage().appendTo(loc_view);
					});
					
					out.successFinish();
				}));
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY){
				node_destroyUtil(loc_decoration);
			}
			return out;
		},
		
//		getDestroyRequest :function(handlers, requestInfo){
//			node_destroyUtil(loc_decoration);
//		},
		
//		getInitRequest :function(handlers, requestInfo){
//			var out = node_createServiceRequestInfoCommon(undefined, handlers, requestInfo);
//			out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(function(requestInfo){
//				//put ui to root
//				_.each(loc_uiModule.getUIs(), function(ui, index){
//					ui.getPage().appendTo(loc_view);
//				});
//				
//				out.successFinish();
//			}));
//			return out;
//		},
		
//		getInterface : function(){
//			return {
//			}
//		},
	};
	return loc_out;
}
, {"loadPattern":"file"
});
