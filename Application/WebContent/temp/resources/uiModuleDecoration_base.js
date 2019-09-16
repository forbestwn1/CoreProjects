
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"id":"base",
"type":"uiModuleDecoration"
},
"children":[],
"dependency":{},
"info":{}
}, function(gate){
	
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
	var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");

	var loc_gate = gate;
	var loc_uiModule = loc_gate.getComponentCore();

	var loc_out = {
		
		//process event from beneath (core or decoration)
		processComponentCoreEvent : function(eventName, eventData, request){
			if(eventName==node_CONSTANT.MODULE_EVENT_UIEVENT){
				//for event from module ui
				//find event handler process defined in module ui 
				var eventHandler = loc_uiModule.getEventHandler(eventData.uiId, eventData.eventName);
				//if within module, defined the process for this event
				if(eventHandler!=undefined){
					//create event data as extra input for event handler process
					var extraInput = {
						public : {
						}
					};
					extraInput.public[node_basicUtility.buildNosliwFullName('EVENT')] = {
						event : eventData.eventName,
						data : eventData.eventData
					};
					//execute event handler process
					loc_gate.processRequest(loc_gate.getExecuteProcessRequest(eventHandler[node_COMMONATRIBUTECONSTANT.EXECUTABLEEVENTHANDLER_PROCESS], extraInput, undefined, request));
				}
			}
		},
	};
	return loc_out;
}
, {"loadPattern":"file"
});

