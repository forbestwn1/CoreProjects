//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
var node_COMMONATRIBUTECONSTANT;
var node_COMMONCONSTANT;
var node_CONSTANT;
var node_remoteServiceErrorUtility;
var node_remoteServiceUtility;
var node_errorUtility;
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
	var loc_syncRemoteCall = function(){
		//set flag so that no new processing allowed before this function is finished
		loc_syncReady = false;

		//id for this remote call
		loc_id = nosliw.runtime.getIdService().generateId();
		
		//prepare remote request and do ajax call
		nosliw.logging.info("************************  Remote AJAX Request   ************************");
		nosliw.logging.info("Syntask: " + loc_name);
		nosliw.logging.info("Remote call Id: " + loc_id);
		
		var serviceTaskRequests = [];
		for(var i in loc_syncTasks){
			var remoteRequest = loc_syncTasks[i].getRemoteServiceRequest();
			serviceTaskRequests.push(remoteRequest);
			nosliw.logging.info("Task ID: " + loc_syncTasks[i].id);
		}
		nosliw.logging.info("***************************************************************");
		
		var remoteRequestData = {};
//		remoteRequestData[node_COMMONATRIBUTECONSTANT.SERVLETPARMS_CLIENTID] = nosliw.getClientId();
		remoteRequestData[node_COMMONATRIBUTECONSTANT.SERVICESERVLET_SERVLETPARMS_COMMAND] = loc_setting.getConfigure(node_COMMONATRIBUTECONSTANT.SERVICESERVLET_SERVLETPARMS_COMMAND);
		remoteRequestData[node_COMMONATRIBUTECONSTANT.SERVICESERVLET_SERVLETPARMS_PARMS] = JSON.stringify(serviceTaskRequests);
	
		$.ajax(_.extend({
			data : remoteRequestData,
			success : function(serviceDataResult, status){
				var syncTasks = loc_syncTasks;
				//clear tasks
				loc_syncTasks = [];
				//ready to process new task
				loc_syncReady = true;

				if(node_errorUtility.isSuccess(serviceDataResult)==true){
					var serviceDatas = serviceDataResult.data;
					//processed normally
					for(var j in serviceDatas){
						var serviceData = serviceDatas[j];
						var task = syncTasks[j];
						var taskType = task.type;

						if(taskType==node_COMMONCONSTANT.REMOTESERVICE_TASKTYPE_GROUP){
							//for group task, handle child task first
							for(var k in task.children)		loc_handleServiceResult(task.children[k], serviceData.data[k]);
						}
						//handle task
						loc_handleServiceResult(task, serviceData);
					}
				}
				else{
					nosliw.logging.error(loc_moduleName, loc_name, "System error when processing tasks in snycTask :", serviceDataResult);
					loc_processSyncRequestSystemError(serviceDataResult);
				}
				
				nosliw.logging.info("************************  Remote AJAX Request Finish  ************************");
				nosliw.logging.info("Syntask: " + loc_name);
				nosliw.logging.info("Remote call Id: " + loc_id);
				nosliw.logging.info("Syn status: " + loc_syncReady);
				nosliw.logging.info("***************************************************************");
				
				//process sync task again
				loc_processTasks();

			},
			error: function(obj, textStatus, errorThrown){
				nosliw.logging.error(loc_moduleName, loc_name, "Exception when processing tasks in snycTask ", textStatus, errorThrown);

				//when ajax error happened, which may be caused by network error, server is down or server internal error
				//remote service manager is put into suspend status
				//the service request is not removed
				var serviceData = node_remoteServiceErrorUtility.createRemoteServiceExceptionServiceData(obj, textStatus, errorThrown); 
				
				node_remoteServiceUtility.handleServiceTask(loc_syncTasks, function(serviceTask){
					loc_handleServiceResult(serviceTask, serviceData);
					serviceTask.status = node_CONSTANT.REMOTESERVICE_SERVICESTATUS_FAIL;
				});
				
				//suspend the system
				loc_remoteServiceMan.interfaceObjectLifecycle.suspend();
				//finish processing, so that ready to process again
				loc_syncReady = true;
			},
		}, loc_setting.getConfiguresObject()));
	};
	
	/*
	 * process the system error (invalid client id, ...) 
	 */
	var loc_processSyncRequestSystemError = function(serviceData){
		
	};
	
	/*
	 * call the responding handler according to service data
	 */
	var loc_handleServiceResult = function(serviceTask, serviceData){
		var resultStatus = node_errorUtility.getServiceDataStatus(serviceData);
		switch(resultStatus){
		case node_CONSTANT.REMOTESERVICE_RESULT_SUCCESS:
			nosliw.logging.info(loc_moduleName, loc_name, serviceTask.requestId, "Success handler");
			nosliw.logging.trace(loc_moduleName, loc_name, serviceTask.requestId, "Data", serviceData.data);
			return node_remoteServiceUtility.executeServiceTaskSuccessHandler(serviceTask, serviceData.data, serviceTask);
			break;
		case node_CONSTANT.REMOTESERVICE_RESULT_EXCEPTION:
			nosliw.logging.error(loc_moduleName, loc_name, serviceTask.requestId, "Error handler with data", serviceData);
			return node_remoteServiceUtility.executeServiceTaskExceptionHandler(serviceTask, serviceData, serviceTask);
			break;
		case node_CONSTANT.REMOTESERVICE_RESULT_ERROR:
			nosliw.logging.error(loc_moduleName, loc_name, serviceTask.requestId, "Exception handler with data", serviceData);
			return node_remoteServiceUtility.executeServiceTaskErrorHandler(serviceTask, serviceData, serviceTask);
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
				nosliw.logging.info(loc_moduleName, loc_name, "Start remote processing tasks in snycTask ");
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
			nosliw.logging.info(loc_moduleName,  task.requestId, "New remote task is added to sync task ", loc_name, task.id, task.service.command, ":", JSON.stringify(task.service.parms));
//			this.logSyncTask();
		},
		
		logSyncTask : function(){
			nosliw.logging.trace(loc_moduleName, loc_name, "***********************  info start ***********************" );
			nosliw.logging.trace(loc_moduleName, loc_name, "info", "		"+"task:"+loc_syncTasks.length +"  queue:"+loc_syncTaskQueue.length);
			nosliw.logging.trace(loc_moduleName, loc_name, "tasks", "		");
			nosliw.logging.trace(loc_moduleName, loc_name, "queue", "		");
			for(var i in loc_syncTaskQueue){
				nosliw.logging.trace(loc_moduleName, loc_name, "		", loc_syncTaskQueue[i].requestId, JSON.stringify(loc_syncTaskQueue[i].service));
			}
			nosliw.logging.trace(loc_moduleName, loc_name, "*********************** info end ***********************" );
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
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("remote.errorUtility", function(){node_remoteServiceErrorUtility = this.getData();});
nosliw.registerSetNodeDataEvent("remote.utility", function(){node_remoteServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("error.utility", function(){node_errorUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createRemoteSyncTask", node_createRemoteSyncTask); 

})(packageObj);

