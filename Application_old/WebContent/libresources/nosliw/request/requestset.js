//get/create package
var packageObj = library.getChildPackage("request");    

(function(packageObj){
	//get used node
	var node_requestProcessor;
	var node_ServiceRequestExecuteInfo;
	var node_createServiceRequestInfoCommon;
	var node_CONSTANT;
	var node_requestUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createServiceRequestInfoSet = function(service, handlers, requester_parent){

	service = node_requestUtility.buildService(service);

	var loc_constructor = function(service, handlers, requester_parent){
		//all the requests   name -- request
		loc_out.pri_requests = {};
		//total request number
		loc_out.pri_requestSum = 0;
		//number of request processed
		loc_out.pri_requestNum = 0;
		//request results (only exception)
		loc_out.pri_requestResults = loc_reateRequestSetResult();
	};
	
	/*
	 * exectue function 
	 */
	var loc_process = function(requestInfo){
		
		//if no child request, 
		if(loc_out.pri_requestSum==0)		loc_processResult();
		
		var remoteRequests = [];
		_.each(loc_out.pri_requests, function(childRequestInfo, name, list){
			var remoteRequest = node_requestProcessor.processRequest(childRequestInfo, false);
			if(remoteRequest!=undefined){
				if(_.isArray(remoteRequest)){
					for(var i in remoteRequest){
						remoteRequests.push(remoteRequest[i]);
					}
				}
				else{
					remoteRequests.push(remoteRequest);
				}
			}
		}, this);
		if(remoteRequests.length==0)  return;
		return remoteRequests;
	};
		
	/*
	 * create handlers for child request
	 */
	var loc_updateChildRequestHandlers = function(name, childRequest){
		childRequest.addPostProcessor({
			success : loc_createChildRequestSuccessProcessor(name),
			error : function(requestInfo, serviceData){
				loc_out.errorFinish(serviceData, loc_out);
			},
			exception : function(requestInfo, serviceData){
				loc_out.exceptionFinish(serviceData, loc_out);
			},
		});
	};

	/*
	 * create handler for child request
	 * 	this handler combine original handler and childResultProcessor to create new handler
	 */
	var loc_createChildRequestSuccessProcessor = function(name){
		return function(childRequestInfo, data){
			var out = data;
			loc_out.pri_requestResults.addResult(name, out);
			loc_processResult();
			return out;
		};
	};
	
	/*
	 * 
	 */
	var loc_processResult = function(){
		loc_out.pri_requestNum++;
		if(loc_out.pri_requestNum>=loc_out.pri_requestSum){
			//if finish all requests
			loc_out.successFinish(loc_out.pri_requestResults, loc_out);
		}
	};
	
	var loc_out = {
		ovr_afterSetId : function(){
			//change all children's id
			var id = this.getId();
			_.each(loc_out.pri_requests, function(childRequestInfo, name, list){
				childRequestInfo.setId(id);
			}, this);			
		},
		
		addRequest : function(name, childRequest){
			if(childRequest!=undefined){
				childRequest.setParentRequest(this);
				this.pri_requests[name] = childRequest;
				loc_updateChildRequestHandlers(name, childRequest);
				this.pri_requestSum++;
			}
		},
			
	};
	
	loc_out = _.extend(node_createServiceRequestInfoCommon(service, handlers, requester_parent), loc_out);
	
	loc_constructor(service, handlers, requester_parent);
	
	//request type
	loc_out.setType(node_CONSTANT.REQUEST_TYPE_SET);
	
	loc_out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(loc_process, this));
	
	return loc_out;
};


loc_reateRequestSetResult = function(){
	var loc_results = {};
	var loc_out = {
		addResult : function(name, result){
			loc_results[name] = result;
		},
		
		getResult : function(name){
			return loc_results[name];
		},
		
		getResults : function(){
			return loc_results;
		},
		
	};
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.entity.ServiceRequestExecuteInfo", function(){node_ServiceRequestExecuteInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){node_createServiceRequestInfoCommon = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createServiceRequestInfoSet", node_createServiceRequestInfoSet); 

})(packageObj);
