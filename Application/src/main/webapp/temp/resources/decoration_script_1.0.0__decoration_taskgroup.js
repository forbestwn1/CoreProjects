
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"decoration_script",
"version":"1.0.0"
},
"id":"*decoration_taskgroup"
},
"children":[],
"dependency":{},
"info":{}
}, {"brick":{"attribute":[{"id":"script",
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
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_createUIDecorationRequest = nosliw.getNodeData("uipage.createUIDecorationRequest");
	var node_basicUtility = nosliw.getNodeData("common.utility.basicUtility");
	var node_createTaskInput = nosliw.getNodeData("task.createTaskInput");
	 
	var loc_parentView;
	var loc_mainView;
	var loc_wrapperView;
	var loc_executeView;
	var loc_itemListView;
	var loc_resultView;
	var loc_infoView;
	
	var loc_configure = configure;

	var loc_envInterface;

	var loc_logContent;

	var loc_init = function(){
		loc_logContent = loc_logContent + JSON.stringify(loc_configure.getConfigureValue(), null, 4) + "\n";
	};

	var loc_executeItem = function(itemId){
		var decorationInterface = loc_envInterface[node_CONSTANT.INTERFACE_ENV_DECORATION];
		var coreEntity = decorationInterface[node_CONSTANT.INTERFACE_ENV_DECORATION_COMMAND_GETCORE]();
		var taskContainerInterface = loc_getCoreTaskContainerInterface(coreEntity);
		
		//collect requirement
		var taskInputRequirement = {
			interface : {
				
			}
		};
		var taskRequirements = taskContainerInterface.getItemRequirement(itemId);
		_.each(taskRequirements, function(taskRequirement, i){
			if(taskRequirement.interface!=undefined){
				taskInputRequirement.interface[taskRequirement.interface] = function(){
					return "This is from interface: " + taskRequirement.interface;
				}
			}
		});

		var info = eval(loc_infoView.val());

		var taskInput = node_createTaskInput(info, taskInputRequirement);
		var request = taskContainerInterface.getExecuteItemRequest(itemId, taskInput, {
			success : function(request, result){
				loc_resultView.val(JSON.stringify(result));
			}
		});
		node_requestServiceProcessor.processRequest(request);
	};

	var loc_executeAllItems = function(){
		var decorationInterface = loc_envInterface[node_CONSTANT.INTERFACE_ENV_DECORATION];
		var coreEntity = decorationInterface[node_CONSTANT.INTERFACE_ENV_DECORATION_COMMAND_GETCORE]();
		var request = loc_getCoreTaskInterface(coreEntity).getExecuteRequest({
			success : function(request, result){
				loc_resultView.val(JSON.stringify(result));
			}
		});
		node_requestServiceProcessor.processRequest(request);
	};

	var loc_getCoreTaskInterface = function(coreEntity){
		return node_getApplicationInterface(coreEntity, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK);
	};

	var loc_getCoreTaskContainerInterface = function(coreEntity){
		return node_getApplicationInterface(coreEntity, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASKCONTAINER);
	};

	var loc_out = {

		updateView : function(view){
			loc_parentView = $(view);
			loc_mainView = $('<div class="dock" style="border-width:thick; border-style:solid; border-color:green">Decoration Task Group</div>');
			loc_wrapperView = $('<div></div>');

			loc_executeView = $('<a>Execute</a>');
			loc_executeView.on("click",function(){
				loc_executeAllItems();
			});

			var infoViewWapper = $('<div>Input: </div>');
			loc_infoView = $('<textarea rows="10" cols="50" style="resize: none;" data-role="none"></textarea>');
			infoViewWapper.append(loc_infoView);

			loc_itemListView = $('<div></div>');
			loc_resultView = $('<textarea rows="10" cols="150" style="resize: none;" data-role="none"></textarea>');

			loc_mainView.append(loc_executeView);
			loc_mainView.append(infoViewWapper);
			loc_mainView.append(loc_itemListView);
			loc_mainView.append(loc_resultView);
			loc_mainView.append(loc_wrapperView);
			
			loc_parentView.append(loc_mainView);

			return loc_wrapperView.get();
		},
		
		getPostInitRequest : function(handlers, request){
			var decorationInterface = loc_envInterface[node_CONSTANT.INTERFACE_ENV_DECORATION];
			var coreEntity = decorationInterface[node_CONSTANT.INTERFACE_ENV_DECORATION_COMMAND_GETCORE]();
			var itemIds = loc_getCoreTaskContainerInterface(coreEntity).getAllItemIds();
			
			loc_itemListView.empty();
			loc_itemListView.append($('<br>'));
			_.each(itemIds, function(itemId){
				var itemView = $('<a>'+itemId+'</a>');
				itemView.on("click",function(){
					loc_executeItem(itemId);
				});
				
				loc_itemListView.append(itemView);
				loc_itemListView.append($('<br>'));
			});
			loc_itemListView.append($('<br>'));
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
}],
"brickType":{"brickType":"decoration_script",
"version":"1.0.0"
},
"isComplex":true
},
"valueStructureDomain":{"valueStructureDefinition":{},
"valueStructureRuntime":{},
"definitionByRuntime":{}
}
}, {"loadPattern":"file"
});

