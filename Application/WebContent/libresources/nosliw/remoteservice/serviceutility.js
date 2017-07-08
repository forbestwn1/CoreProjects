/**
 * 
 */
var nosliwRemoteServiceUtility = function(){
	
	return {
		createRemoteServiceConfigures : function(service, command){
			var configuresObj = {};
			configuresObj[NOSLIWATCOMMONTRIBUTECONSTANT.ATTR_SERVLETPARMS_COMMAND] = command;
			configuresObj[NOSLIWATCOMMONTRIBUTECONSTANT.ATTR_SERVLETPARMS_SERVICE] = service;
			return createConfigures(configuresObj);
		},
		
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
			if(serviceTask.type==NOSLIWCONSTANT.REMOTESERVICE_TASKTYPE_GROUP)  return true;
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
				if(nosliwRemoteServiceUtility.isGroupServiceTask(task)){
					for(var j in task.children){
						handleServiceTask(task.children[j], handler);
					}
				}
			}
		},
		
	};
}();
