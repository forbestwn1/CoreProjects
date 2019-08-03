//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_createEventObject;
	var node_CONSTANT;
	var node_ServiceData;
	var node_requestProcessErrorUtility;
//*******************************************   Start Node Definition  ************************************** 	

var loc_RequestInfo = function(request, processRemote, attchedTo){
	this.request = request;
	this.processRemote = processRemote;
	this.attchedTo = attchedTo;
};

var loc_trigueRequestEvent = function(request, eventName, eventData){
	request.trigueIndividualEvent(eventName, eventData);
};

var loc_processRequest = function(request, processRemote, processedCallBack){
	var loc_moduleName = "requestManager";
	nosliw.logging.info(loc_moduleName, request.getInnerId(), "Start request : ", request.getService());
	
	//add request processor in order to logging the result
	request.setRequestProcessors({
		start : function(request){
//				nosliw.logging.info(loc_moduleName, request.getInnerId(), "Start handler");
		},
		success : function(request, data){
			nosliw.logging.info(loc_moduleName, request.getInnerId(), "Success handler : ", data);
			return data;
		}, 
		error : function(request, data){
			nosliw.logging.error(loc_moduleName, request.getInnerId(), "Error handler : ", JSON.stringify(data));
			return data;
		}, 
		exception : function(request, data){
			nosliw.logging.error(loc_moduleName, request.getInnerId(), "Exception handler : ", JSON.stringify(data));
			return data;
		}, 
	});

	request.insertPostProcessor({
		start : function(request){},
		success : function(request, data){		processedCallBack(request);		}, 
		error : function(request, data){		processedCallBack(request);		}, 
		exception : function(request, data){	processedCallBack(request);		}, 
	});

	//execute start handler
	var startOut = request.start(request);

	var execute = request.getRequestExecuteInfo();
	if(execute!=undefined){
		//run execute function, return remote task info if have
		try{
			var remoteTask = execute.execute(request);
			//whether submit the remoteTask
			if(remoteTask==undefined){
//					nosliw.logging.info(loc_moduleName, request.getInnerId(), "Finish request locally : ", node_basicUtility.stringify(request.getService()));
			}
			else{
				processRemote = true;   //kkkk
				if(processRemote!=false){
					//submit the remote task
//						nosliw.logging.info(loc_moduleName, request.getInnerId(), "Finish request with remote request Id :", remoteTask.requestId);
					nosliw.runtime.getRemoteService().addServiceTask(remoteTask);
				}
				else{
//						nosliw.logging.info(loc_moduleName, request.getInnerId(), "Finish request by creating remote request info object Id :", remoteTask.requestId);
					//return the remote task, let the call to decide what to do with remoteTask
					return remoteTask;
				}
			}
		}
		catch(err){
			nosliw.runtime.getErrorManager().logError(err);
			request.errorFinish(node_requestProcessErrorUtility.createRequestProcessErrorServiceData(err));
		}
	}
	else{
		var service = request.service;
		if(service.type=='dataoperation'){
			requestOperateContextPathValue(request);
		}
	}
};
	
	
var loc_createRequestGroup = function(firstRequest, doneCallBack){
	var loc_requestSum = 0;
	
	var loc_rootRequest;
	
	var loc_requestInfosById = {};
	var loc_requestIdList = [];
	
	var loc_eventObject = node_createEventObject();

	var loc_doneCallBack = doneCallBack;
	
	var loc_attached = [];
	var loc_attachTo = undefined;
	
	var loc_status = node_CONSTANT.REQUESTGROUP_STATUS_INIT;
	
	var loc_requestProcessed = function(request){
		var requestId = loc_getRequestId(request);
		if(loc_requestInfosById[requestId]!=undefined){
			delete loc_requestInfosById[requestId];
			loc_requestIdList.splice( loc_requestIdList.indexOf(requestId), 1 );
			loc_requestSum--;
		}
		if(loc_requestSum==0){
			if(loc_status==node_CONSTANT.REQUESTGROUP_STATUS_ACTIVE){
				loc_status = node_CONSTANT.REQUESTGROUP_STATUS_ALMOSTDONE;
				loc_rootRequest.registerEventListener(loc_eventObject, 
						function(eventName, eventData){
							if(eventName==node_CONSTANT.REQUEST_EVENT_ALMOSTDONE){
								loc_rootRequest.unregisterEventListener(loc_eventObject);
								if(loc_requestSum==0){
									loc_doneCallBack(loc_out, request.getResult());
									loc_rootRequest.done();
									loc_status = node_CONSTANT.REQUESTGROUP_STATUS_DONE;
								}
								else{
									loc_status = node_CONSTANT.REQUESTGROUP_STATUS_ACTIVE;
								}
							}						
						}
					);
				//when no child request under root request, it means root request almost done
				//almost done with root request
				loc_rootRequest.almostDone();
			}
		}
	};

	var loc_getRequestId = function(request){	return request.getInnerId();	};
	
	var loc_out = {
			
		getId : function(){  return loc_rootRequest.getId();  },
		
		getStatus : function(){  return loc_status;   },
		
		addAttached : function(group){			
			loc_attached.push(group);
			group.setAttachTo(loc_out);
		},
		getAttached : function(){  return loc_attached;   },
		getAttachTo : function(){  return loc_attachTo;  },
		setAttachTo : function(group){  loc_attachTo = group;   },
		
		addRequestInfo : function(requestInfo){
			if(loc_rootRequest==undefined){
				//find root request
				loc_rootRequest = requestInfo.request.getRootRequest();
				loc_id = loc_rootRequest.getId();
			}
			var requestId = loc_getRequestId(requestInfo.request);
			loc_requestInfosById[requestId] = requestInfo;
			loc_requestIdList.push(requestId);
			loc_requestSum++;
			
			if(loc_status==node_CONSTANT.REQUESTGROUP_STATUS_ACTIVE || loc_status==node_CONSTANT.REQUESTGROUP_STATUS_ALMOSTDONE){
				loc_processRequest(requestInfo.request, requestInfo.processRemote, loc_requestProcessed);
			}
		},
		
		startProcess : function(){
			loc_status = node_CONSTANT.REQUESTGROUP_STATUS_ACTIVE;
			_.each(loc_requestInfosById, function(requestInfo, id){
				loc_processRequest(requestInfo.request, requestInfo.processRemote, loc_requestProcessed);
			});
		},
		
		destroy : function(){
			loc_eventObject.clearup();
		},
		
		pause : function(){
			loc_status = node_CONSTANT.REQUESTGROUP_STATUS_PAUSED;
			_.each(loc_requestInfosById, function(requestInfo, id){
				var requestStatus = requestInfo.request.getStatus();
				if(requestStatus==node_CONSTANT.REQUEST_STATUS_PROCESSING)   requestInfo.request.pause();
			});
		},

		resume : function(){
			loc_status = node_CONSTANT.REQUESTGROUP_STATUS_ACTIVE;
			_.each(loc_requestInfosById, function(requestInfo, id){
				var requestStatus = requestInfo.request.getStatus();
				if(requestStatus==node_CONSTANT.REQUEST_STATUS_PAUSED)  requestInfo.request.resume();  //for paused request, resume it
				else if(requestStatus==node_CONSTANT.REQUEST_STATUS_INIT)	loc_processRequest(requestInfo.request, requestInfo.processRemote, loc_requestProcessed);  //for not started request, start it
			});
		},
		
		attachedSolved : function(group, requestResult){
			for(var i in loc_attached){
				if(loc_attached[i].getId()==group.getId()){
					break;
				}
			}
			loc_attached.splice(i, 1);
			if(loc_attached.length==0){
				//all attached solved
				loc_out.resume();
			}
		},
		
	};
	
	loc_out.addRequestInfo(firstRequest);
	nosliw.logging.info("Request Group New !!!!!!  " + loc_id + (firstRequest.attchedTo==undefined?"":("   Attached to : "+ firstRequest.attchedTo.getId())));
	
	return loc_out;
};
	
	
var node_createRequestServiceProcessor = function(){
	//store all the live requests
	//request organized according to root request
	//{root request id  -- {  current request id --- request  }  } 
	var loc_groups = {};
	var loc_processingGroupSum = 0;
	var loc_requestsSum = 0;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_groupQueue = [];
	
	var loc_findGroupInQueue = function(request){
		var out;
		_.each(loc_groupQueue, function(group, index){
			if(group.getId()==request.getId())  out = group;
			_.each(group.getAttached(), function(attached, index){
				if(attached.getId()==request.getId())  out = attached;
			});
		});
		return out;
	};
	
	var loc_findGroupInProcessing = function(request){
		var out;
		_.each(loc_groups, function(group, id){
			if(group.getId()==request.getId())  out = group;
			_.each(group.getAttached(), function(attached, index){
				if(attached.getId()==request.getId())  out = attached;
			});
		});
		return out;
	};

	var loc_findGroup = function(request){
		var out = loc_findGroupInQueue(request);
		if(out!=undefined)  return out;
		return loc_findGroupInProcessing(request);
	};
	
	var loc_addGroupToQueue = function(group){
		loc_groupQueue.push(group);
		loc_processNextGroup();
	};
	
	var loc_requestGroupDoneHandler = function(group, requestResult){
		var attachedToGroup = group.getAttachTo();
		if(attachedToGroup==undefined){
			//independent group
			loc_eventSource.triggerEvent(node_CONSTANT.EVENT_REQUESTPROCESS_DONE, {
				requestId : group.getId(),
				result : requestResult
			});
		}
		else{
			if(group.getId()=="1034"){
				var kkkkk = 5555;
				kkkkk++;
			}
			
			//dependent group
			attachedToGroup.attachedSolved(group, requestResult);
		}
		nosliw.logging.info("Request Group : ", group.getId(), " Done !!!!!!");
		group.destroy();
		delete loc_groups[group.getId()];
		loc_processingGroupSum--;
		loc_processNextGroup();
	}; 
	
	var loc_processGroup = function(group){
		if(group.getAttachTo()==undefined) loc_eventSource.triggerEvent(node_CONSTANT.EVENT_REQUESTPROCESS_START, group.getId());  //for independent request
		nosliw.logging.info("Request Group : ", group.getId(), " Start Processing !!!!!!");
		loc_processingGroupSum++;
		loc_groups[group.getId()] = group;

		//pause attachedToGroup first
		var attachedToGroup = group.getAttachTo();
		if(attachedToGroup!=undefined && attachedToGroup.getStatus()!=node_CONSTANT.REQUESTGROUP_STATUS_PAUSED){
			attachedToGroup.pause();
		}
		
		//process attached group
		_.each(group.getAttached(), function(attached, index){
			loc_processGroup(attached);
		});
		
		if(group.getStatus()!=node_CONSTANT.REQUESTGROUP_STATUS_PAUSED){
			group.startProcess();
		}
	};
	
	var loc_start
	
	var loc_processNextGroup = function(){
		if(loc_processingGroupSum==0)
		loc_processGroupInQueue();
	};
	
	var loc_processGroupInQueue = function(){
		if(loc_groupQueue.length>0){
			var group = loc_groupQueue[0];
			loc_groupQueue.shift();//slice(1);
			loc_processGroup(group);
		}
	};
	
	var loc_addRequest = function(requestInfo){
		var group = loc_findGroup(requestInfo.request);
		if(group!=undefined){
			//group exists, then add to group
			group.addRequestInfo(requestInfo);
		}
		else{
			group = loc_createRequestGroup(requestInfo, loc_requestGroupDoneHandler);
			if(requestInfo.attchedTo==undefined){
				loc_addGroupToQueue(group);
			}
			else{
				var attachedGroup = loc_findGroupInQueue(requestInfo.attchedTo);
				if(attachedGroup!=undefined){
					//attached in queue, then append to it
					attachedGroup.addAttached(group);
				}
				else{
					attachedGroup = loc_findGroupInProcessing(requestInfo.attchedTo);
					if(attachedGroup!=undefined){
						//attached in processing, then add to processing
						attachedGroup.addAttached(group);
						loc_processGroup(group);
					}
					else{
						//otherwise, treat it as normal request
						loc_addGroupToQueue(group);
					}
				}
			}
		}
	};
	
	var loc_out = {
		processRequest : function(request, configure){
			var loc_moduleName = "requestManager";
			nosliw.logging.info(loc_moduleName, request.getInnerId(), "Start Request");
			
			var processRemote = true;
			if(configure!=undefined && configure.processRemote!=undefined){
				processRemote = configure.processRemote;
			}
			
			var attchedTo = undefined;
			if(configure!=undefined && configure.attchedTo!=undefined){
				attchedTo = configure.attchedTo;
			}
			
			return loc_addRequest(new loc_RequestInfo(request, processRemote, attchedTo));
		},	

		registerEventListener : function(listener, handler, thisContext){  return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },
	};

	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("error.entity.ServiceData", function(){node_ServiceData = this.getData();});
nosliw.registerSetNodeDataEvent("request.errorUtility", function(){node_requestProcessErrorUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createRequestServiceProcessor", node_createRequestServiceProcessor); 

})(packageObj);
