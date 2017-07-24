//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
//*******************************************   Start Node Definition  ************************************** 	


/**
 * 
 */
var node_createRemoteSyncTask = function(name, remoteServiceMan, setting){
	var loc_moduleName = "syncTask";
	
	//reference to remote service manager obj
	var loc_remoteServiceMan = remoteServiceMan;
	
	//name of this sync task
	var loc_name = name;

	//setting for this sync task
	var loc_setting = setting;
	
	//store all sync tasks that have been processed
	var loc_syncTasks = [];
	//store all sync tasks that have not been processed
	var loc_syncTaskQueue = [];
	//flag whether sync tasks in queue is ready for process
	var loc_syncReady = true;
	
	/*
	 * process all sync tasks in task array
	 * 		service : url service
	 * 		command : command to use
	 */
	function loc_syncRemoteCall(){
		//set flag so that no new processing allowed before this function is finished
		loc_syncReady = false;
		
		//prepare remote request and do ajax call
		var serviceTaskRequests = [];
		for(var i in loc_syncTasks){
			serviceTaskRequests.push(loc_syncTasks[i].getRemoteServiceRequest());
		}
		var serviceRequests = {};
		serviceRequests[NOSLIWATCOMMONTRIBUTECONSTANT.ATTR_SERVLETPARMS_CLIENTID] = nosliw.getClientId();
		serviceRequests[NOSLIWATCOMMONTRIBUTECONSTANT.ATTR_SERVLETPARMS_COMMAND] = loc_setting.getConfigure(NOSLIWATCOMMONTRIBUTECONSTANT.ATTR_SERVLETPARMS_COMMAND);
		serviceRequests[NOSLIWATCOMMONTRIBUTECONSTANT.ATTR_SERVLETPARMS_PARMS] = JSON.stringify(serviceTaskRequests);
	
		$.ajax({
			url : loc_setting.getConfigure(NOSLIWATCOMMONTRIBUTECONSTANT.ATTR_SERVLETPARMS_SERVICE),
			type : "POST",
			dataType: "json",
			data : serviceRequests,
			async : true,
			success : function(serviceDataResult, status){
				if(nosliwErrorUtility.isSuccess(serviceDataResult)==true){
					nosliwLogging.info(loc_moduleName, loc_name, "Success remote processing");
					
					var serviceDatas = serviceDataResult.data;
					//processed normally
					for(var j in serviceDatas){
						var serviceData = serviceDatas[j];
						var task = loc_syncTasks[j];
						var taskType = task.type;

						if(taskType==NOSLIWCOMMONCONSTANT.CONS_REMOTESERVICE_TASKTYPE_GROUP){
							//for group task, handle child task first
							for(var k in task.children)		loc_handleServiceResult(task.children[k], serviceData.data[k]);
						}
						//handle task
						loc_handleServiceResult(task, serviceData);
					}
					//empty synTasks
					loc_syncTasks = [];
				}
				else{
					nosliwLogging.error(loc_moduleName, loc_name, "System error when processing tasks in snycTask :", serviceDataResult);
					loc_processSyncRequestSystemError(serviceDataResult);
				}
				
				//clear tasks
				loc_syncTasks = [];
				//ready to process new task
				loc_syncReady = true;
				//process sync task again
				loc_processTasks();
			},
			error: function(obj, textStatus, errorThrown){
				nosliwLogging.error(loc_moduleName, loc_name, "Exception when processing tasks in snycTask ", textStatus, errorThrown);

				//when ajax error happened, which may be caused by network error, server is down or server internal error
				//remote service manager is put into suspend status
				//the service request is not removed
				var serviceData = nosliwRemoteServiceErrorUtility.createRemoteServiceExceptionServiceData(obj, textStatus, errorThrown); 
				
				nosliwRemoteServiceUtility.handleServiceTask(loc_syncTasks, function(serviceTask){
					loc_handleServiceResult(serviceTask, serviceData);
					serviceTask.status = NOSLIWCONSTANT.REMOTESERVICE_SERVICESTATUS_FAIL;
				});
				
				//suspend the system
				loc_remoteServiceMan.suspend();
				//finish processing, so that ready to process again
				loc_syncReady = true;
			},
		});
	};

	/*
	 * process the system error (invalid client id, ...) 
	 */
	var loc_processSyncRequestSystemError = function(serviceData){
		
	}
	
	/*
	 * call the responding handler according to service data
	 */
	var loc_handleServiceResult = function(serviceTask, serviceData){
		var resultStatus = nosliwErrorUtility.getServiceDataStatus(serviceData);
		switch(resultStatus){
		case NOSLIWCONSTANT.REMOTESERVICE_RESULT_SUCCESS:
			nosliwLogging.info(loc_moduleName, loc_name, serviceTask.requestId, "Success handler");
			nosliwLogging.trace(loc_moduleName, loc_name, serviceTask.requestId, "Data", serviceData.data);
			return nosliwRemoteServiceUtility.executeServiceTaskSuccessHandler(serviceTask, serviceData.data, serviceTask);
			break;
		case NOSLIWCONSTANT.REMOTESERVICE_RESULT_EXCEPTION:
			nosliwLogging.error(loc_moduleName, loc_name, serviceTask.requestId, "Error handler with data", serviceData);
			return nosliwRemoteServiceUtility.executeServiceTaskExceptionHandler(serviceTask, serviceData, serviceTask);
			break;
		case NOSLIWCONSTANT.REMOTESERVICE_RESULT_ERROR:
			nosliwLogging.error(loc_moduleName, loc_name, serviceTask.requestId, "Exception handler with data", serviceData);
			return nosliwRemoteServiceUtility.executeServiceTaskErrorHandler(serviceTask, serviceData, serviceTask);
			break;
		}
	};
	
	/*
	 * process all tasks
	 */
	var loc_processTasks = function(){
		if(loc_syncReady==true){
			//if ready to process
			if(loc_isEmpty()==false){
				nosliwLogging.info(loc_moduleName, loc_name, "Start remote processing tasks in snycTask ");
				//if not empty 
				//put all tasks in queue into tasks array and process all tasks in it
				for(var i in loc_syncTaskQueue){
					loc_syncTasks.push(loc_syncTaskQueue[i]);
				}
				loc_syncTaskQueue = [];
				loc_syncRemoteCall();
			}
		}
	};
	
	var loc_isEmpty = function(){
		var out = false;
		if(loc_syncTaskQueue.length==0){
			if(loc_syncTasks.length==0){
				out = true;
			}
		}
		return out;
	};
	
	var loc_out = {
		/*
		 * add a new task
		 */
		addTask : function(task){	
			loc_syncTaskQueue.push(task);	
			nosliwLogging.info(loc_moduleName,  task.requestId, "New remote task is added to sync task: ", JSON.stringify(task.service));
			this.logSyncTask();
		},
		
		logSyncTask : function(){
			nosliwLogging.trace(loc_moduleName, loc_name, "***********************  info start ***********************" );
			nosliwLogging.trace(loc_moduleName, loc_name, "info", "		"+"task:"+loc_syncTasks.length +"  queue:"+loc_syncTaskQueue.length);
			nosliwLogging.trace(loc_moduleName, loc_name, "tasks", "		");
			nosliwLogging.trace(loc_moduleName, loc_name, "queue", "		");
			for(var i=0 in loc_syncTaskQueue){
				nosliwLogging.trace(loc_moduleName, loc_name, "		", loc_syncTaskQueue[i].requestId, JSON.stringify(loc_syncTaskQueue[i].service));
			}
			nosliwLogging.trace(loc_moduleName, loc_name, "*********************** info end ***********************" );
		},
	
		/*
		 * process all tasks
		 */
		processTasks : function(){	loc_processTasks();		},
		
		isEmpty : function(){	return loc_isEmpty();	},
		
		isReady : function(){  return loc_syncReady; },
		
		clear : function(){
			loc_syncTaskQueue = [];
			loc_syncTasks = [];
		},
		
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data


//Register Node by Name
packageObj.createChildNode("createRemoteSyncTask", node_createRemoteSyncTask); 

})(packageObj);

