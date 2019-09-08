
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"id":"base",
"type":"uiModuleDecoration"
},
"children":[],
"dependency":{},
"info":{}
}, function(gate){
	
	var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");

	var loc_gate = gate;
	var loc_uiModule = loc_gate.getComponentCore();

	var loc_out = {
			
		processComponentCoreEvent : function(eventName, eventData, request){
			var eventHandler = loc_uiModule.getEventHandler(eventData.uiName, eventData.eventName);
			//if within module, defined the process for this event
			if(eventHandler!=undefined){
				var extraInput = {
					public : {
						EVENT : {
							event : eventData.eventName,
							data : eventData.eventData
						} 
					}
				};
				loc_gate.processRequest(loc_gate.getExecuteProcessRequest(eventHandler[node_COMMONATRIBUTECONSTANT.EXECUTABLEEVENTHANDLER_PROCESS], extraInput, undefined, request));
			}
			
		},
		
		getInterface : function(){
			return {
				getPart : function(partId){  return loc_uiModule.getPart(partId);	},

				getExecutePartCommandRequest : function(partId, commandName, commandData, handlers, requestInfo){
					return loc_out.getInterface().getPart(partId).getExecuteCommandRequest(commandName, commandData, handlers, requestInfo);
				},
			};
		},
		
		getLifeCycleRequest : function(transitName, handlers, request){
			var out;
			if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_INIT){
//				out = loc_uiModule.getInitIOContextRequest(handlers, request);
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE){
//				out = loc_uiModule.getInitIOContextRequest(handlers, request);
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND){
				out = node_createServiceRequestInfoSet(undefined, {
					success : function(request, resultSet){
						var uiData = {};
						_.each(resultSet.getResults(), function(uiDataEle, uiName){
							uiData[uiName] = uiDataEle;
						});
						loc_gate.setStateValue("uiData", uiData);
					}
				}, handlers, request);
				_.each(loc_uiModule.getUIs(), function(ui, index){	out.addRequest(ui.getName(), ui.getGetStateRequest());	});
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESUME){
				out = node_createServiceRequestInfoSet(undefined, handlers, request);
				var uiData = loc_gate.getStateValue("uiData");
				_.each(loc_uiModule.getUIs(), function(ui, index){
					out.addRequest(ui.getName(), ui.getSetStateRequest(uiData[ui.getName()]));	
				});
			}
			return out;
		},
	};
	return loc_out;
}
, {"loadPattern":"file"
});

