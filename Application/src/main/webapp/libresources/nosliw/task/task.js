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
	var node_basicUtility;
	var node_createEventObject;

//*******************************************   Start Node Definition  ************************************** 	

var node_createTaskSetup = function(initRequest, runtimeEnv){
	
	var loc_initRequest = initRequest;
	
	var loc_runtimeEnv = runtimeEnv;
	
	var loc_out = {
		
		getInitTaskRequest : function(){   return loc_initRequest;      },
		
		getRuntimeEnv : function(){  return loc_runtimeEnv;    }
		
	};
	
	return loc_out;
};

var node_createTaskCore = function(taskImp, entityCore){
	
	var loc_taskImp = taskImp;
	
	var loc_entityCore = entityCore;

	var loc_taskResult;
	
//	var loc_lifecycleHandlers = [];
	
	var loc_runtimeEnv = [];

	var loc_initSetupRequests = [];
	
	var loc_lifecycleEventObject = node_createEventObject();
	
	var loc_addTaskSetup = function(taskSetup){
		//add init request
		if(taskSetup.getInitTaskRequest()!=undefined) loc_initSetupRequests.push(taskSetup.getInitTaskRequest());
		
		//add runtime env
		if(taskSetup.getRuntimeEnv()!=undefined){
			if(node_basicUtility.isArray(taskSetup.getRuntimeEnv())==true){
				_.each(taskSetup.getRuntimeEnv(), function(item, i){
					loc_runtimeEnv(item);
				});
			}
			else{
    			loc_runtimeEnv.push(taskSetup.getRuntimeEnv());
			}
		}
	};
	
	var loc_triggerLifecycleEvent = function(event, eventData,request){
		loc_lifecycleEventObject.triggerEvent(event, eventData, request);
	}
	
    var loc_taskRuntimeEnv = {
	  
	   getRuntimeValue : function(domain, name, parms){
		   var i = loc_runtimeEnv.length-1;
           while(i>=0){
			   var value = loc_runtimeEnv[i].getRuntimeValue(domain, name, parms);
			   if(value!=undefined){
				   return value;
			   }
		       i--;  
		   }
	   }
    };

	
	var loc_out = {
	
//		getRuntimeEnv : function(){   return loc_runtimeEnv;      },
		
//		registerLifecycleHandler : function(handler){  loc_lifecycleHandlers.push(handler);  },
		
		registerLifecycleEventListener : function(listenerEventObj, handler, thisContext){return loc_lifecycleEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);},
		
		
		getTaskResult : function(){   return loc_taskResult;    },

		getTaskInitRequest : function(request, handlers){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			for(var i in loc_initSetupRequests){
				out.addRequest(loc_initSetupRequests[i](loc_entityCore));
			}
			return out;
		},
	
		getTaskExecuteRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_taskImp.getTaskExecuteRequest(loc_taskRuntimeEnv, {
				success : function(request, taskResult){
					loc_taskResult = taskResult;
					loc_triggerLifecycleEvent("finish", loc_taskResult, request);
					return loc_taskResult;
				}
			}));
			return out;
		},

		addTaskSetup : function(taskSetup){
			if(taskSetup!=undefined){
				if(node_basicUtility.isArray(taskSetup)==true){
					_.each(taskSetup, function(item, i){
						loc_addTaskSetup(item);
					});
				}
				else{
					loc_addTaskSetup(taskSetup);
				}
			}
		},

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
nosliw.registerSetNodeDataEvent("common.valuecontainer.createValueContainerList", function(){node_createValueContainerList = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});


//Register Node by Name
packageObj.createChildNode("createTaskSetup", node_createTaskSetup); 
packageObj.createChildNode("createTaskCore", node_createTaskCore); 

})(packageObj);
