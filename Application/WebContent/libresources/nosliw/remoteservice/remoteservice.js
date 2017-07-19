//get/create package
var packageObj = library;    

(function(packageObj){
//get used node
//*******************************************   Start Node Definition  ************************************** 	


/*
 * handle all the remote service
 *  	network detection
 *  	remote call
 * this manager has four status: 
 * 		start:		not init yet 
 * 		active:		running receiving request
 * 		suspend:	when exception happened, the system is turned into suspend, and will not process any request
 * 		dead:
 *  for the user of remote service manager, it should listen to the manager status's transit event, 
 *  and send request only when the service manager is in active status
 *  request on any other status will not be process: 
 *  	suspend:	exeption handler will be called
 *  	others:   	ignored 
 */
var node_createRemoteServiceManager = function(){
	var loc_moduleName = "remoteService";

	//store all sync tasks by name
	var loc_tasks = {};
	
	//store the reason why this manager is suspended
	var loc_suspendReason = undefined;

	//a timed processor that triggered every 3 second
	var loc_timerProcessor = undefined;
	
	//create default configure object for sync task  
	var loc_syncTaskDefaultConfigure = createConfiguresBase(nosliwRemoteServiceUtility.createRemoteServiceConfigures(NOSLIWCOMMONCONSTANT.CONS_SERVICENAME_SERVICE, NOSLIWCOMMONCONSTANT.CONS_SERVICECOMMAND_GROUPREQUEST));
	
	//predefined sync task configure, so that we don't need to create it everytime, just get it by name
	var loc_syncTaskConfigures = {};
	
	/*
	 * add one service task to system
	 * put task into queue/queue status, but do not process them in this function
	 */
	function loc_addServiceTask(serviceTask){
		//sync name for this task
		var syncName = serviceTask.syncName;
		
		//if sync tasks for this service task is not exist, then create one 
		var syncTasks = loc_tasks[syncName];
		if(syncTasks==undefined){
			
			var configureName = loc_getConfigureName(syncName);
			var syncTaksConfigure = loc_getSyncTaskConfiguresByName(configureName);
			if(syncTaksConfigure==undefined){
				syncTaksConfigure = loc_syncTaskDefaultConfigure.getBaseConfigures();
			}
			
			syncTasks = nosliwCreateRemoteSyncTask(syncName, loc_out, syncTaksConfigure);
			loc_tasks[syncName] = syncTasks;
		}
		syncTasks.addTask(serviceTask);
	};

	/*
	 * get sync task configure by name
	 */
	function loc_getSyncTaskConfiguresByName(name){
		if(name==undefined)  return undefined;
		return loc_syncTaskConfigures[name];
	};
	
	/*
	 * get configure name from sync name
	 * sync name is in the format of :   configure name : real name
	 */
	function loc_getConfigureName(syncName){
		var out = syncName;
		var index = syncName.indexOf(NOSLIWCOMMONCONSTANT.CONS_SEPERATOR_PART);
		if(index!=-1){
			out = syncName.subString(index+1);
		}
		return out;
	};
	
	/*
	 * process all tasks
	 */
	function loc_processTasks(){
		var emptyTasks = [];
		_.each(loc_tasks, function(syncTask, name, list){
			if(syncTask.isEmpty()==false){
				syncTask.processTasks();
			}
			else{
				//if sync task is empty, remove it
				emptyTasks.push(syncTask);
			}
		});
		
		for(var i in emptyTasks){
			delete loc_tasks[emptyTasks[i].name];
		}
	}
	
	/*
	 * this is a timed function to do task every 3 second
	 */
	var loc_timerProcess = function(){
		var status = loc_out.getResourceStatus();
		if(status==NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_ACTIVE){
			//try to process all tasks
			loc_processTasks();
		}
		else if(status==NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_SUSPENDED){
			//check recover
			loc_out.resume();
		}		
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT"] = function(){
//		loc_timerProcessor = setInterval(loc_timerProcess, 3000);
	};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_SUSPEND"] = function(){
	};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_RESUME"] = function(){
	};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY"] = function(){
		clearInterval(loc_timerProcessor);
	};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DEACTIVE"] = function(){
	};
	
	
	var loc_out = {
		ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},

		/*
		 * register predefined sync task configure
		 */
		registerSyncTaskConfigure : function(name, configure){
			var newConfigure = loc_syncTaskDefaultConfigure.createConfigures(configure);
			loc_syncTaskConfigures[name] = newConfigure;
		},
		
		/*
		 * add a service task or array of service task
		 */
		addServiceTask : function(serviceTask){
			var status = this.getResourceStatus();
			if(status==NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_ACTIVE){
				//if in active status, then process them
				if(_.isArray(serviceTask)==true){
					for(var i in serviceTask){
						loc_addServiceTask(serviceTask[i]);
					}
				}
				else{
					loc_addServiceTask(serviceTask);
				}
				
				//process all tasks (sync and async)
				loc_processTasks();
			}
			else if(status==NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_SUSPENDED){
				//if in suspend status, not accept any service task
				//inform outsider through exception handler
				var serviceData = nosliwRemoteServiceErrorUtility.createRemoteServiceSuspendedServiceData(loc_suspendReason);
				loc_handleServiceResult(serviceTask, serviceData);
			}
			return serviceTask;
		},
		
		removeServiceTask : function(id){},
	};
	
	//append resource life cycle method to out obj
	loc_out = nosliwLifecycleUtility.makeResourceObject(loc_out, loc_moduleName);

	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("createRemoteServiceManager", node_createRemoteServiceManager); 

	var module = {
		start : function(packageObj){
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
