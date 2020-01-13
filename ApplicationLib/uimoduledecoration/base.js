function(gate){
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
	var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");
	var node_basicUtility = nosliw.getNodeData("common.utility.basicUtility");
	var node_createIODataSet = nosliw.getNodeData("iotask.entity.createIODataSet");
	
	var loc_gate = gate;
	var loc_uiModule = loc_gate.getComponentCore();

	var loc_out = {
		
		//process event from beneath (core or decoration)
		processComponentCoreEvent : function(eventName, eventDataInfo, request){
			//for event from module ui
			//find event handler process defined in module ui 
			var moduleUISource = eventDataInfo.getSourceByType(node_CONSTANT.TYPEDOBJECT_TYPE_APPMODULEUI);
			var eventHandler = loc_uiModule.getEventHandler(moduleUISource.getId(), eventName);
			//if within module, defined the process for this event
			if(eventHandler!=undefined){
				//create event data as extra input for event handler process
				var extraInput = {
					public : {
					}
				};
				var eventData = eventDataInfo.getEventData();
				extraInput.public[node_basicUtility.buildNosliwFullName('EVENT')] = {
					event : eventName,
					data : eventData
				};
				//execute event handler process
				loc_gate.processRequest(loc_gate.getExecuteProcessRequest(eventHandler, node_createIODataSet(extraInput), undefined, request));
			}
		},
	};
	return loc_out;
}
