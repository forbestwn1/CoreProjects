//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

var node_requestServiceProcessor = function(){

	var loc_moduleName = "requestManager";
	
	var loc_processRequest = function(requestInfo, processRemote){
		nosliwLogging.info(loc_moduleName, requestInfo.getInnerId(), "Start request : ", JSON.stringify(requestInfo.getService()));
		
		//add request processor in order to logging the result
		requestInfo.setRequestProcessors({
			start : function(requestInfo){
				nosliwLogging.info(loc_moduleName, requestInfo.getInnerId(), "Start handler");
			},
			success : function(requestInfo, data){
				nosliwLogging.info(loc_moduleName, requestInfo.getInnerId(), "Success handler");
				nosliwLogging.trace(loc_moduleName, requestInfo.getInnerId(), "Data ", data);
				return data;
			}, 
			fail : function(requestInfo, data){
				nosliwLogging.error(loc_moduleName, requestInfo.getInnerId(), "Error handler");
				nosliwLogging.error(loc_moduleName, requestInfo.getInnerId(), "Data ", data);
				return data;
			}, 
			exception : function(requestInfo, data){
				nosliwLogging.error(loc_moduleName, requestInfo.getInnerId(), "Exception handler");
				nosliwLogging.error(loc_moduleName, requestInfo.getInnerId(), "Data ", data);
				return data;
			}, 
		});

		
		//execute start handler
		var startOut = requestInfo.executeStartHandler(requestInfo);

		var execute = requestInfo.getRequestExecuteInfo();
		if(execute!=undefined){
			//run execute function, return remote task info if have
			var remoteTask = execute.execute(requestInfo);
			//whether submit the remoteTask
			if(remoteTask==undefined){
				nosliwLogging.info(loc_moduleName, requestInfo.getInnerId(), "Finish request locally : ", JSON.stringify(requestInfo.getService()));
			}
			else{
				if(processRemote!=false){
					//submit the remote task
					nosliwLogging.info(loc_moduleName, requestInfo.getInnerId(), "Finish request with remote request Id :", remoteTask.requestId);
					nosliw.getRemoteServiceManager().addServiceTask(remoteTask);
				}
				else{
					nosliwLogging.info(loc_moduleName, requestInfo.getInnerId(), "Finish request by creating remote request info object Id :", remoteTask.requestId);
					//return the remote task, let the call to decide what to do with remoteTask
					return remoteTask;
				}
			}
		}
		else{
			var service = requestInfo.service;
			if(service.type=='dataoperation'){
				requestOperateContextPathValue(requestInfo);
			}
		}
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT"] = function(){
	};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_SUSPEND"] = function(){
	};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_RESUME"] = function(){
	};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY"] = function(){
	};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DEACTIVE"] = function(){
	};
	
	var loc_out = {
		ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},

		processRequest : function(requestInfo, processRemote){
			return loc_processRequest(requestInfo, processRemote);
		},	
	};

	//append resource life cycle method to out obj
	loc_out = nosliwLifecycleUtility.makeResourceObject(loc_out, loc_moduleName);

	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("requestServiceProcessor", node_requestServiceProcessor); 

	var module = {
		start : function(packageObj){
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
