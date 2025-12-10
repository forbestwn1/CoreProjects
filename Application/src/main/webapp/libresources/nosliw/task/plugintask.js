//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildComponentInterface;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_ResourceId;
	var node_resourceUtility;
	var node_componentUtility;
	var node_createServiceRequestInfoSimple;
	var node_expressionUtility;
	var node_makeObjectWithApplicationInterface;
	var node_getEntityTreeNodeInterface;
	var node_getApplicationInterface;
	var node_taskExecuteUtility;
	var node_requestServiceProcessor;
	var node_complexEntityUtility;
	var node_basicUtility;
	var node_getEntityObjectInterface;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createTaskPlugin = function(){
	
	var loc_out = {

		getCreateEntityCoreRequest : function(entityDef, internalValuePortContainerId, externalValuePortContainerId, bundleCore, configure, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return loc_createTaskCore(entityDef, configure);
			}, handlers, request);
		},
	};

	return loc_out;
};

var loc_createTaskCore = function(taskDef, configure){

	var loc_taskDef = taskDef;
	
	var loc_envInterface;

	var loc_indexId = 0;
	
	var loc_taskInfoView;
	var loc_outputView;
	var loc_taskViewContainer;
	
	var loc_createTaskId = function(){
		loc_indexId++;
		return loc_indexId + "";
	};

	var loc_updateTaskValuePortContainerInfo = function(taskEntityCore){
		var taskEntityCore = node_complexEntityUtility.getCoreEntity(taskEntityCore);
		if(loc_taskInfoView!=undefined){
				loc_taskInfoView.text("    valuePortContainer:  "
					+node_getEntityObjectInterface(taskEntityCore).getInternalValuePortContainer().getId()
					+"--"
					+node_getEntityObjectInterface(taskEntityCore).getExternalValuePortContainer().getId());
		}
		
	};
	
	var loc_updateTaskResultView = function(taskResult){
		var resultStr = node_basicUtility.stringify(taskResult);
		console.log(resultStr);
		loc_outputView.val(resultStr);
	};

	var loc_createTaskEntityCoreRequest = function(taskId, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_ENTITY].createChildByAttributeWithInitRequest(taskId, node_COMMONATRIBUTECONSTANT.BLOCKTASKWRAPPER_TASK, undefined, loc_taskViewContainer, {
			success : function(request, node){
				return node_complexEntityUtility.getBuildAttributeWithResourceId(loc_out, {
					success : function(request){
						var taskEntityCore = node.getChildValue().getCoreEntity();
						loc_updateTaskValuePortContainerInfo(taskEntityCore);
						
						node_taskExecuteUtility.registerTaskLifecycleEventListener(taskEntityCore, undefined, function(event, eventData, request){
							if(event=="finish"){
								loc_updateTaskResultView(eventData);
							}
						}, taskEntityCore);
				
						return taskEntityCore;
					}
				});
			}
		}));
		return out;
	};

	var loc_facadeTaskFactory = {
		getCreateEntityRequest : function(handlers, request){
			var taskId = loc_createTaskId();
			return loc_createTaskEntityCoreRequest(taskId, handlers, request);
		}
	};

	var loc_out = {

		setEnvironmentInterface : function(envInterface){
			loc_envInterface = envInterface;
		},
		
		updateView : function(view){
			var rootView =  $('<div>Task' + '</div>');
			$(view).append(rootView);

			loc_taskInfoView = $('<span></span>');
			rootView.append(loc_taskInfoView);
			
			var taskTrigueView = $('<button>Execute Task</button>');
			taskTrigueView.click(function() {
				var out = node_createServiceRequestInfoSequence();
	
				//create task entity runtime
				var taskId = loc_createTaskId();
				out.addRequest(loc_createTaskEntityCoreRequest(taskId, {
					success : function(request, taskEntityCore){
						loc_updateTaskValuePortContainerInfo(taskEntityCore);
						return node_taskExecuteUtility.getExecuteEntityTaskWithAdapterRequest(taskEntityCore, undefined, undefined, {
							success : function(request, taskResult){
								loc_updateTaskResultView(taskResult);
								return taskResult;
							}
						});
					}
				}));
				node_requestServiceProcessor.processRequest(out);
			});
			rootView.append(taskTrigueView);

			loc_outputView =  $('<textarea rows="5" cols="200"></textarea>');
			rootView.append(loc_outputView);
			loc_taskViewContainer =  $('<div></div>');
			rootView.append(loc_taskViewContainer);
		},
		
	};
	
	return node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_FACTORY, loc_facadeTaskFactory);
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.buildComponentCore", function(){node_buildComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){node_ResourceId = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("expression.utility", function(){node_expressionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithApplicationInterface", function(){node_makeObjectWithApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getApplicationInterface", function(){node_getApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("task.taskExecuteUtility", function(){node_taskExecuteUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("complexentity.getEntityObjectInterface", function(){node_getEntityObjectInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("createTaskPlugin", node_createTaskPlugin); 

})(packageObj);
