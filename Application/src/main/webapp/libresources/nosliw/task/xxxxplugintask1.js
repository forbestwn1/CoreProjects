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
	var node_taskUtility;
	var node_requestServiceProcessor;
	var node_complexEntityUtility;
	
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

var loc_createTaskEntityCoreRequest = function(envInterface, taskId, handlers, request){
	var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
	out.addRequest(envInterface[node_CONSTANT.INTERFACE_ENTITY].createBrickChildByAttributeRequest(taskId, node_COMMONATRIBUTECONSTANT.BLOCKTASKWRAPPER_TASK, undefined, {
		success : function(request, node){
			return node.getChildValue().getCoreEntity();
		}
	}));
	return out;
};

var loc_createTaskWrapper = function(adapterName, taskContext, taskId, getEnvInterface){
	var loc_adapterName = adapterName;
	var loc_taskContext = taskContext;
	var loc_taskId = taskId;
	var loc_getEnvInterface = getEnvInterface;

	var loc_taskEntityCore;
	var loc_task;
	
	var loc_taskNode;

	var loc_out = {

		getTaskInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_getEnvInterface()[node_CONSTANT.INTERFACE_ENTITY].createChildByAttributeRequest(taskId, node_COMMONATRIBUTECONSTANT.BLOCKTASKWRAPPER_TASK, undefined, {
				success : function(request, node){
					loc_taskNode = node;
					loc_taskEntityCore = node_complexEntityUtility.getBrickNode(loc_taskNode).getChildValue().getCoreEntity();
				}
			}));
			return out;
		},
		
		getTaskExecuteRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(node_taskUtility.getExecuteTaskWithAdapterRequest(loc_taskEntityCore, loc_adapterName, loc_taskContext, {
				success : function(request, task){
					loc_task = task;					
				}
			}));
			return out;
		},

		getTaskResult : function(){
			return loc_task.getTaskResult();
		},
		
		isTaskWrapper : true,

	};
	return loc_out;
};


var loc_createTaskCore = function(taskDef, configure){

	var loc_taskDef = taskDef;
	
	var loc_envInterface;

	var loc_indexId = 0;
	
	var loc_createTaskId = function(){
		loc_indexId++;
		return loc_indexId + "";
	};

	var loc_facadeTaskFactory = {
		//return a task
		createTask : function(taskContext, adapterName){
			return loc_createTaskWrapper(adapterName, taskContext, loc_createTaskId(), function(){ return loc_envInterface;  });
		},
	};

	var loc_out = {

		setEnvironmentInterface : function(envInterface){
			loc_envInterface = envInterface;
		},
		
		updateView : function(view){
			var rootView =  $('<div>Task' + '</div>');
			$(view).append(rootView);
			
			var taskTrigueView = $('<button>Execute Task</button>');
			taskTrigueView.click(function() {
				var out = node_createServiceRequestInfoSequence();
	
				//create task entity runtime
				var taskId = loc_createTaskId();
				out.addRequest(loc_createTaskEntityCoreRequest(loc_envInterface, taskId, {
					success : function(request, taskEntityCore){
						return node_taskUtility.getExecuteTaskWithAdapterRequest(taskEntityCore);
					}
				}));
				node_requestServiceProcessor.processRequest(out);
			});
			rootView.append(taskTrigueView);
		},
		
		getExecuteTaskWithAdapter : function(adapterName, taskContext, handlers, request){
			var task = loc_facadeTaskFactory.createTask(taskContext, adapterName);
			return node_taskUtility.getExecuteTaskRequest(task, undefined, undefined, handlers, request);
		}
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
nosliw.registerSetNodeDataEvent("task.taskUtility", function(){node_taskUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("xxxxcreateTaskPlugin1", node_createTaskPlugin); 

})(packageObj);
