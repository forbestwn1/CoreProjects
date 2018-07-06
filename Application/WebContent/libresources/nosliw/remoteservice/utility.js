//get/create package
var packageObj = library;    

(function(packageObj){
//get used node
var node_COMMONCONSTANT;
var node_COMMONATRIBUTECONSTANT;
var node_createConfigures;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){
	
	return {
//		createRemoteServiceConfigures : function(service, command){
//			var configuresObj = {};
//			configuresObj[node_COMMONATRIBUTECONSTANT.SERVICESERVLET_SERVLETPARMS_COMMAND] = command;
//			configuresObj[node_COMMONATRIBUTECONSTANT.SERVICESERVLET_SERVLETPARMS_SERVICE] = service;
//			return node_createConfigures(configuresObj);
//		},
		
		/*
		 * data is serviceData.data 
		 */
		executeServiceTaskSuccessHandler : function(serviceTask, data, thisContext){
			var handler = serviceTask.handlers.success;
			if(handler!=undefined){
				return handler.call(thisContext, serviceTask, data);
			}
		},

		/*
		 * data is service data 
		 */
		executeServiceTaskErrorHandler : function(serviceTask, serviceData, thisContext){
			var handler = serviceTask.handlers.error;
			if(handler!=undefined){
				return handler.call(thisContext, serviceTask, serviceData);
			}
		},

		/*
		 * data is service data 
		 */
		executeServiceTaskExceptionHandler : function(serviceTask, serviceData, thisContext){
			var handler = serviceTask.handlers.exception;
			if(handler!=undefined){
				return handler.call(thisContext, serviceTask, serviceData);
			}
		},

		/*
		 * check if a service task is a group task
		 */
		isGroupServiceTask : function(serviceTask){
			if(serviceTask.type==node_CONSTANT.REMOTESERVICE_TASKTYPE_GROUP)  return true;
			return false;
		},
		
		/*
		 * process service task by handler function
		 * serviceTask:   a task or array of task
		 */
		handleServiceTask : function(serviceTask, handler){
			var tasks = [];
			if(_.isArray(serviceTask))  tasks = serviceTask;
			else tasks.push(serviceTask);
			
			for(var i in tasks){
				var task = tasks[i];
				handler.call(task, task);
				if(this.isGroupServiceTask(task)){
					for(var j in task.children){
						handleServiceTask(task.children[j], handler);
					}
				}
			}
		},
		
	};
}();

//*******************************************   End Node Definition  ************************************** 	

nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.setting.createConfigures", function(){node_createConfigures = this.getData();});


//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
