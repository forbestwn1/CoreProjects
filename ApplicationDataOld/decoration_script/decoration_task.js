function(complexEntityDef, valueContextId, bundleCore, configure){
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
	var node_taskUtility = nosliw.getNodeData("task.taskUtility");

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
		loc_logContent = loc_logContent + JSON.stringify(loc_configure.getConfigureValue(), null, 4) + "\n";
	};

	var loc_postInit = function(){
		var decorationInterface = loc_envInterface[node_CONSTANT.INTERFACE_ENV_DECORATION];
		var coreEntity = decorationInterface[node_CONSTANT.INTERFACE_ENV_DECORATION_COMMAND_GETCORE]();
		
		node_complexEntityUtility.traverseNode(coreEntity, {
			processRoot : function(entityCore){
				var taskInterface = loc_getCoreTaskInterface(entityCore);
				if(taskInterface!=undefined){
					loc_taskListView.append($('<option value="">'+'Root'+'</option>'));
					return false;	
				}
			},
			
			processLeaf : function(parentEntityCore, childName){
				var treeNodeInterface = node_getEntityTreeNodeInterface(parentEntityCore);
				var childNode = treeNodeInterface.getChild(childName);
				var childValue = childNode.getChildValue();
				var childEntityCore = node_complexEntityUtility.getCoreEntity(childValue);
				var taskInterface = loc_getCoreTaskInterface(childEntityCore);
				if(taskInterface!=undefined){
					var parentEntityDef = node_getBasicEntityObjectInterface(parentEntityCore).getEntityDefinition();
					var pathFromRoot = node_namingConvensionUtility.cascadePath(parentEntityDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITY_PATHFROMROOT], childName);
					loc_taskListView.append($('<option value="'+pathFromRoot+'">'+pathFromRoot+'</option>'));
					return false;
				}
			}
		});
	
	};

	var loc_executeTask = function(){
		var decorationInterface = loc_envInterface[node_CONSTANT.INTERFACE_ENV_DECORATION];
		var coreEntity = decorationInterface[node_CONSTANT.INTERFACE_ENV_DECORATION_COMMAND_GETCORE]();
		var complexEntityInterface = node_getEntityObjectInterface(coreEntity);

/*		
		var taskInterface = loc_getCoreTaskInterface(coreEntity);
		
		//collect requirement
		var taskInputRequirement = {
			interface : {
				
			}
		};
		var taskRequirements = taskInterface.getItemRequirement();
		_.each(taskRequirements, function(taskRequirement, i){
			if(taskRequirement.interface!=undefined){
				taskInputRequirement.interface[taskRequirement.interface] = function(){
					return "This is from interface: " + taskRequirement.interface;
				}
			}
		});
*/
		var taskInput = eval(loc_infoView.val());

		var taskInfo = node_createTaskInfo(loc_taskListView.val());
		
		var request = node_taskUtility.getInvokeTaskRequest(taskInfo, taskInput, undefined, complexEntityInterface.getBundle());

/*		
		var request = taskInterface.getExecuteRequest(taskInput, {
			success : function(request, result){
				loc_taskResultView.val(JSON.stringify(result));
			}
		});
*/
		
		node_requestServiceProcessor.processRequest(request);
	};

	var loc_getCoreTaskInterface = function(coreEntity){
		return node_getApplicationInterface(coreEntity, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK);
	};


	var loc_out = {

		updateView : function(view){
			loc_parentView = $(view);
			loc_mainView = $('<div class="dock" style="border-width:thick; border-style:solid; border-color:green">Decoration Task</div>');
			loc_wrapperView = $('<div></div>');

			loc_executeView = $('<a>Execute</a>');
			loc_executeView.on("click",function(){
				loc_executeTask();
			});

			var taskListViewWapper = $('<div>Select task: </div>');
			loc_taskListView = $('<select id="taskList"/>');
			taskListViewWapper.append(loc_taskListView);

			var infoViewWapper = $('<div>Input: </div>');
			loc_infoView = $('<textarea rows="10" cols="50" style="resize: none;" data-role="none"></textarea>');
			infoViewWapper.append(loc_infoView);

			loc_taskResultView = $('<textarea rows="10" cols="150" style="resize: none;" data-role="none"></textarea>');

			loc_mainView.append(loc_executeView);
			loc_mainView.append(taskListViewWapper);
			loc_mainView.append(infoViewWapper);
			loc_mainView.append(loc_taskResultView);
			loc_mainView.append(loc_wrapperView);
			loc_parentView.append(loc_mainView);

			return loc_wrapperView.get();
		},
		
		getPostInitRequest : function(handlers, request){
			loc_postInit();
		},
		
		setEnvironmentInterface : function(envInterface){
			loc_envInterface = envInterface;
		},
	};
	
	loc_init();
	return loc_out;
}
