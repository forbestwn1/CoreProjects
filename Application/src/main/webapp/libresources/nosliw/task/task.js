//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createEventObject;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;
	var node_createTaskContextInterface;

//*******************************************   Start Node Definition  ************************************** 	

var loc_createTaskContextCompose = function(entityCore){
	
	var loc_taskContexts = [];
	
	var loc_entityCore = entityCore;
	
	var loc_runtimeEnv = {
		getRuntimeEnvValue : function(name){
			for(var i in loc_taskContexts){
				var value = loc_taskContexts[i].getRuntimeEnvValue(name);
				if(value!=undefined)  return value;
			}
		}
	}
	
	var loc_out = {
	
		addTaskContext : function(taskContext){
			loc_taskContexts.push(node_createTaskContextInterface(taskContext));
		},
		
		getRuntimeEnv : function(){
			return loc_runtimeEnv;
		},
		
		getTaskInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			for(var i in loc_taskContexts){
				out.addRequest(loc_taskContexts[i].getInitTaskRequest(loc_entityCore));
			}
			return out;
		},
		
	};
	
	return loc_out;
	
};

var node_createTaskCore = function(taskImp, entityCore){
	
	var loc_taskImp = taskImp;
	
	var loc_taskContexts = loc_createTaskContextCompose(entityCore);
	
	var loc_taskResult;
	
	var loc_lifecycleHandlers = [];
	
	var loc_out = {
		
		getTaskCreationRequest : function(taskContext, handlers, request){
			loc_taskContexts.addTaskContext(taskContext);
			return node_createServiceRequestInfoSequence(undefined, handlers, request);
		},

		getTaskInitRequest : function(taskContext, handlers, request){
			loc_taskContexts.addTaskContext(taskContext);
			return loc_taskContexts.getTaskInitRequest(handlers, request);
		},
		
		getTaskExecuteRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_taskImp.getTaskExecuteRequest(loc_taskContexts.getRuntimeEnv(), {
				success : function(request, taskResult){
					loc_taskResult = taskResult;
					return loc_taskResult;
				}
			}));
			return out;
		},
		
		registerLifecycleHandler : function(handler){  loc_lifecycleHandlers.push(handler);  },
		
		getTaskResult : function(){   return loc_taskResult;    }
	
	};
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("task.createTaskContextInterface", function(){node_createTaskContextInterface = this.getData();	});


//Register Node by Name
packageObj.createChildNode("createTaskCore", node_createTaskCore); 

})(packageObj);
