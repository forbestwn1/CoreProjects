//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

var node_requestServiceProcessor = function(){

	var loc_moduleName = "requestManager";
	
	//store all the live requests
	//request organized according to root request
	//{root request id  -- {  current request id --- request  }  } 
	var loc_requests = {};
	var loc_requestsSum = 0;
	
	var loc_requestQueue = [];
	
	var loc_addRequest = function(request){
		var id = request.getId();
		var requestsById = loc_requests[id];
		if(requestsById==undefined){
			requestsById = {};
			loc_requests[id] = requestsById;
			requestsById.requestsSum = 0;
			requestsById.rootRequest = request.getRootRequest();
		}
		requestsById[request.getInnerId()] = request;
		requestsById.requestsSum++;
		loc_requestsSum++;
	};
	
	//when request finished, remove it
	var loc_removeRequest = function(request){
		var id = request.getId();
		var requestsById = loc_requests[id];
		delete requestsById[request.getInnerId()];
		requestsById.requestsSum--;
		loc_requestsSum--;
		if(requestsById.requestsSum==0){
			//when no child request under root request, it means root request almost done
			//almost done with root request
			requestsById.rootRequest.almostDone();
			if(requestsById.requestsSum==0){
				//if no more child request created, then done with root request
				requestsById.rootRequest.done();
				delete loc_requests[id];
				loc_processRequestInQueue();
			}
		}
	};
	
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
				loc_removeRequest(requestInfo);
				return data;
			}, 
			error : function(requestInfo, data){
				nosliw.logging.error(loc_moduleName, requestInfo.getInnerId(), "Error handler : ", data);
//				nosliw.logging.error(loc_moduleName, requestInfo.getInnerId(), "Data ", data);
				loc_removeRequest(requestInfo);
				return data;
			}, 
			exception : function(requestInfo, data){
				nosliw.logging.error(loc_moduleName, requestInfo.getInnerId(), "Exception handler : ", data);
//				nosliw.logging.error(loc_moduleName, requestInfo.getInnerId(), "Data ", data);
				loc_removeRequest(requestInfo);
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
//				nosliw.logging.info(loc_moduleName, requestInfo.getInnerId(), "Finish request locally : ", node_basicUtility.stringify(requestInfo.getService()));
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
	
	var loc_processRequestInQueue = function(){
		if(loc_requestQueue.length>0){
			var requestInfo = loc_requestQueue[0];
			loc_requestQueue.shift();//slice(1);
			loc_addRequest(requestInfo.request);
			return loc_processRequest(requestInfo.request, requestInfo.processRemote);
		}
	};
	
	var loc_addRequestToQueue = function(requestInfo, processRemote){
		if(loc_requests[requestInfo.getId()]!=undefined){
			loc_addRequest(requestInfo);
			return loc_processRequest(requestInfo, processRemote);
		}
		else{
			loc_requestQueue.push({
				request : requestInfo,
				processRemote : processRemote
			});
			if(loc_requestsSum==0){
				return loc_processRequestInQueue();
			}
		}
	};
	
	var loc_out = {
		processRequest1 : function(requestInfo, processRemote){
			if(requestInfo.getInnerId()=="399-400"){
				var kkkk = 5555;
				kkkk++;
			}
			nosliw.logging.info(loc_moduleName, requestInfo.getInnerId(), "Start Request");
			
			if(processRemote==undefined){
				processRemote = true;
			}
			return loc_addRequestToQueue(requestInfo, processRemote);
		},	

		processRequest : function(requestInfo, processRemote){
			nosliw.logging.info(loc_moduleName, requestInfo.getInnerId(), "Start Request");
			
			if(processRemote==undefined){
				processRemote = true;
			}
			
			loc_addRequest(requestInfo);
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
