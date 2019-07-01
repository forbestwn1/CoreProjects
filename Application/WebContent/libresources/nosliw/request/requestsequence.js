//get/create package
var packageObj = library.getChildPackage("request");    

(function(packageObj){
	//get used node
	var node_getObjectType;
	var node_createServiceRequestInfoCommon;
	var node_ServiceRequestExecuteInfo;
	var node_requestProcessor;
	var node_CONSTANT;
	var node_requestUtility;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * a group of requests that will be processed one by one
 * for cases that have uncertain request consequence when request group is created (or next request is depend on previous request result)
 * the item within requests can be two type of value:
 * 		request info object
 * 		or function that return request info object
 * the return value of success handler of each request tells a lot of information about next requestinfo:
 * 		undefined:  	no indication for next  
 * 		request info :	used as next request info
 * 		array:			used as a array of request info	
 */
var node_createServiceRequestInfoSequence = function(service, handlers, requester_parent){

	var loc_startOutDataName = "startOutDataName";
	
	service = node_requestUtility.buildService(service);

	var loc_constructor = function(service, handlers, requester_parent){
		//store all request info objects 
		loc_out.pri_requestInfos = [];
		//the next requester index
		loc_out.pri_cursor = 0;
		
		//modify start handler, in order to process output from start handler
		loc_out.addPostProcessor({
			start : function(requestInfo, startOut){
				loc_out.setData(startOut, loc_startOutDataName);
			}
		});
	};
	
	//add child request, 
	//requestInfo can be single request or a array of request
	var loc_addChildRequest = function(requestInfo){
		if(loc_out.getStatus()!=node_CONSTANT.REQUEST_STATUS_INIT){
			if(loc_out.pri_cursor<loc_out.pri_requestInfos.length-1){
				loc_out.pri_requestInfos = loc_out.pri_requestInfos.slice(0, loc_out.pri_cursor+1);
			}
		}
		if(_.isArray(requestInfo)==true){
			_.each(requestInfo, function(req, i){
				loc_out.pri_requestInfos.push(req);
			});
		}
		else{
			loc_out.pri_requestInfos.push(requestInfo);	
		}
	}
	
	
	/*
	 * process request in sequence according to its index
	 * data : return value from previous request
	 */
	var loc_processNextRequestInSequence = function(previousRequest, data){
		
		if(_.isFunction(data)){
			data = data.call(loc_out, loc_out);
		}
		
		if(_.isArray(data)==true){
			//check if this array is data or a array of request
			var isRequestArray = true;
			if(data.length==0)  isRequestArray = false;
			else{
				for(var i in data){
					if(node_getObjectType(data[i])!=node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST){
						isRequestArray = false;
						break;
					}
				}
			}
			if(isRequestArray==true){
				//if data is an array of request
				loc_addChildRequest(data);
				data = undefined;
			}
		}
		else{
			if(node_getObjectType(data)==node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST){
				//for request
				loc_addChildRequest(data);
				data = undefined;
			}
		}
		
		if(loc_out.pri_requestInfos.length<=loc_out.pri_cursor){
			//not more request in queue
			loc_out.executeSuccessHandler(data, loc_out);
		}
		else{
			var requestInfo = loc_out.pri_requestInfos[loc_out.pri_cursor];

			requestInfo.setParentRequest(loc_out);
			
			//pass the result from previous request to input of current request
			if(previousRequest!=undefined)		requestInfo.setInput(previousRequest.getResult());
			
			var processMode = requestInfo.getParmData('processMode');
			if(processMode=="eventBased"){
				var listener = requestInfo.registerIndividualEventListener(undefined, function(eventName, eventData){
					if(eventName==node_CONSTANT.REQUEST_EVENT_INDIVIDUAL_SUCCESS){
						loc_out.pri_cursor++;
						loc_processNextRequestInSequence(requestInfo, eventData);
					}
					else if(eventName==node_CONSTANT.REQUEST_EVENT_INDIVIDUAL_ERROR){
						loc_out.executeErrorHandler(eventData, loc_out);
					}
					if(eventName==node_CONSTANT.REQUEST_EVENT_INDIVIDUAL_EXCEPTION){
						loc_out.executeExceptionHandler(eventData, loc_out);
					}
					requestInfo.unregisterIndividualEventListener(listener);
				}, requestInfo);
			}
			else{
				requestInfo.addPostProcessor({
					success : function(requestInfo, out){
						loc_out.pri_cursor++;
						loc_processNextRequestInSequence(requestInfo, out);
					},
					error : function(requestInfo, serviceData){
						loc_out.executeErrorHandler(serviceData, loc_out);
					},
					exception : function(requestInfo, serviceData){
						loc_out.executeExceptionHandler(serviceData, loc_out);
					},
				});
			}
			node_requestProcessor.processRequest(requestInfo);
		}
		
	};
	
	var loc_process = function(){
		//retrieve start handler out
		var startHandlerOut = loc_out.getData(loc_startOutDataName);
		//start process first request
		loc_processNextRequestInSequence(undefined, startHandlerOut);
	};
	
	var loc_out = {
			
		ovr_afterSetId : function(){
			//change all children's id
			var id = this.getId();
			_.each(loc_out.pri_requestInfos, function(childRequestInfo, name, list){
				childRequestInfo.setId(id);
			}, this);			
		},
			
		addRequest : function(requestInfo){
			if(requestInfo!=undefined)	loc_addChildRequest(requestInfo);
		},
	};
	
	loc_out = _.extend(node_createServiceRequestInfoCommon(service, handlers, requester_parent), loc_out);
	
	//request type
	loc_out.setType(node_CONSTANT.REQUEST_TYPE_SEQUENCE);
	
	loc_constructor(service, handlers, requester_parent);
	
	loc_out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(loc_process, this));
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){node_createServiceRequestInfoCommon = this.getData();});
nosliw.registerSetNodeDataEvent("request.entity.ServiceRequestExecuteInfo", function(){node_ServiceRequestExecuteInfo = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createServiceRequestInfoSequence", node_createServiceRequestInfoSequence); 

})(packageObj);
