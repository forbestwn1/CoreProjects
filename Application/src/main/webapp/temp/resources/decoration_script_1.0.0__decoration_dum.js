
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"decoration_script",
"version":"1.0.0"
},
"id":"*decoration_dum"
},
"children":[],
"dependency":{},
"info":{}
}, {"brick":{"brickType":{"brickType":"decoration_script",
"version":"1.0.0"
},
"attribute":[{"id":"script",
"name":"script",
"displayName":"script",
"info":{},
"valueWrapper":{"valueType":"value",
"value":function(complexEntityDef, valueContextId, bundleCore, configure){
	var node_getApplicationInterface = nosliw.getNodeData("component.getApplicationInterface");
	var node_createServiceRequestInfoCommon = nosliw.getNodeData("request.request.createServiceRequestInfoCommon");
	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");
	var node_ServiceRequestExecuteInfo = nosliw.getNodeData("request.entity.ServiceRequestExecuteInfo");
	var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
	var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
	var node_createUIDecorationRequest = nosliw.getNodeData("uipage.createUIDecorationRequest");
	var node_basicUtility = nosliw.getNodeData("common.utility.basicUtility");
	var node_createTaskInfo = nosliw.getNodeData("task.createTaskInfo");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_complexEntityUtility = nosliw.getNodeData("complexentity.complexEntityUtility");
	var node_getEntityTreeNodeInterface = nosliw.getNodeData("complexentity.getEntityTreeNodeInterface");
	var node_getBasicEntityObjectInterface = nosliw.getNodeData("common.interfacedef.getBasicEntityObjectInterface");
	var node_namingConvensionUtility = nosliw.getNodeData("common.namingconvension.namingConvensionUtility");
	var node_getEntityObjectInterface = nosliw.getNodeData("complexentity.getEntityObjectInterface");
	var node_taskExecuteUtility = nosliw.getNodeData("task.taskExecuteUtility");

	var loc_parentView;
	var loc_mainView;
	var loc_wrapperView;
	var loc_taskListView;
	var loc_taskResultView;
	var loc_infoView;
	
	var loc_configure = configure;

	var loc_envInterface;

	var loc_logContent;

	var loc_init = function(){
	};

	var loc_out = {

		updateView : function(view){
			loc_parentView = $(view);
			loc_wrapperView = $('<div></div>');
			loc_mainView = $('<div class="dock" style="border-width:thick; border-style:solid; border-color:green">Decoration Task</div>');
			loc_mainView.append(loc_wrapperView);
			loc_parentView.append(loc_mainView);

			return loc_wrapperView.get();
		},
		
		getPostInitRequest : function(handlers, request){
		},
		
		setEnvironmentInterface : function(envInterface){
			loc_envInterface = envInterface;
		},
	};
	
	loc_init();
	return loc_out;
}

},
"adapter":[]
}]
},
"valueStructureDomain":{"valueStructureDefinition":{},
"valueStructureRuntime":{},
"definitionByRuntime":{}
},
"supportBricks":{},
"aliasMapping":{},
"exportBrickInfo":{"id":"default",
"name":"default",
"displayName":"default",
"info":{},
"pathFromRoot":"#main"
}
}, {"loadPattern":"file"
});

