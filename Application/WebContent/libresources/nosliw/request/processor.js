//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

var node_requestServiceProcessor = function(){

	var loc_moduleName = "requestManager";
	
	var loc_processRequest = function(requestInfo, processRemote){
		
		nosliw.logging.info(loc_moduleName, requestInfo.getInnerId(), "Start request : ", requestInfo.getService());
		
		//add request processor in order to logging the result
		requestInfo.setRequestProcessors({
			start : function(requestInfo){
//				nosliw.logging.info(loc_moduleName, requestInfo.getInnerId(), "Start handler");
			},
			success : function(requestInfo, data){
				nosliw.logging.info(loc_moduleName, requestInfo.getInnerId(), "Success handler : ", data);
//				nosliw.logging.trace(loc_moduleName, requestInfo.getInnerId(), "Data ", data);
				return data;
			}, 
			error : function(requestInfo, data){
				nosliw.logging.error(loc_moduleName, requestInfo.getInnerId(), "Error handler : ", data);
//				nosliw.logging.error(loc_moduleName, requestInfo.getInnerId(), "Data ", data);
				return data;
			}, 
			exception : function(requestInfo, data){
				nosliw.logging.error(loc_moduleName, requestInfo.getInnerId(), "Exception handler : ", data);
//				nosliw.logging.error(loc_moduleName, requestInfo.getInnerId(), "Data ", data);
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
//				nosliw.logging.info(loc_moduleName, requestInfo.getInnerId(), "Finish request locally : ", JSON.stringify(requestInfo.getService()));
			}
			else{
				if(processRemote!=false){
					//submit the remote task
//					nosliw.logging.info(loc_moduleName, requestInfo.getInnerId(), "Finish request with remote request Id :", remoteTask.requestId);
					nosliw.runtime.getRemoteService().addServiceTask(remoteTask);
				}
				else{
//					nosliw.logging.info(loc_moduleName, requestInfo.getInnerId(), "Finish request by creating remote request info object Id :", remoteTask.requestId);
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
	
	var loc_out = {
		processRequest : function(requestInfo, processRemote){
			if(processRemote==undefined){
				processRemote = true;
			}
			return loc_processRequest(requestInfo, processRemote);
		},	
	};

	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data

//Register Node by Name
packageObj.createChildNode("requestServiceProcessor", node_requestServiceProcessor); 

})(packageObj);
