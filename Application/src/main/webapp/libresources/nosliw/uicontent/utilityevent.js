//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoService;
	var node_complexEntityUtility;
	var node_createTaskSetup;
	var node_taskUtility;
	var node_taskExecuteUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_uiEventUtility = function(){
	
    var loc_getHandleEventWithTask = function(eventHandlerRefTask, eventData, currentBrickCore, handlers, request){
		var taskBrickId = eventHandlerRefTask[node_COMMONATRIBUTECONSTANT.EVENTREFERENCEHANDLER_TASKBRICKID];
		var relativePath = taskBrickId[node_COMMONATRIBUTECONSTANT.IDBRICKINBUNDLE_RELATIVEPATH];
		var handlerEntityCoreWrapper = node_complexEntityUtility.getBrickCoreByRelativePath(currentBrickCore, relativePath);

		var taskSetup = node_createTaskSetup(
			node_taskUtility.createTaskFunctionWithSettingValuePortValues(
				node_COMMONCONSTANT.VALUEPORTGROUP_TYPE_EVENT, 
				node_COMMONCONSTANT.VALUEPORT_TYPE_EVENT, 
				eventData)
		);
					
		var out = node_taskExecuteUtility.getExecuteWrapperedTaskWithAdapterRequest(handlerEntityCoreWrapper, undefined, taskSetup, {
			success : function(request, taskResult){
//					eventResultView.val(JSON.stringify(taskResult));
			}
		});
		return out;
	};

	var loc_out = {
		
        getHandleEventRequest : function(eventHandlerRef, eventData, currentBrickCore, handlers, request){
	    	var out;
		    var handlerType = eventHandlerRef[node_COMMONATRIBUTECONSTANT.EVENTREFERENCEHANDLER_TYPE];
    		if(handlerType==node_COMMONCONSTANT.EVENT_HANDLERTYPE_TASK){
	        	out = loc_getHandleEventWithTask(eventHandlerRef, eventData, currentBrickCore, handlers, request);
		    }
		    return out;
	    }

	};	
	
	return loc_out;		
		
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});
nosliw.registerSetNodeDataEvent("task.createTaskSetup", function(){node_createTaskSetup = this.getData();});
nosliw.registerSetNodeDataEvent("task.taskUtility", function(){node_taskUtility = this.getData();});
nosliw.registerSetNodeDataEvent("task.taskExecuteUtility", function(){node_taskExecuteUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("uiEventUtility", node_uiEventUtility); 

})(packageObj);
