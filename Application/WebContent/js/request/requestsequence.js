/**
 * a group of requests that will be processed one by one
 * for cases that have uncertain request consequence when request group is created (or next request is depend on previous request result)
 * the item within requests can be two type of value:
 * 		request info object
 * 		function that return request info object
 * the return value of success handler of each request tells a lot of information about next requestinfo:
 * 		undefined:  	no indication for next  
 * 		request info :	used as next request info
 * 		array:			used as a array of request info	
 */
var nosliwCreateRequestSequence = function(service, handlers, requester_parent){

	var loc_startOutDataName = "startOutDataName";
	
	var loc_constructor = function(service, handlers, requester_parent){
		//store a list of requests, it is static
		loc_out.pri_requests = [];
		
		//store all request info objects that have been processed
		loc_out.pri_requestInfos = [];
		//whether the next request is dynamiclly created
		loc_out.pri_isDynamic = false;
		//the next requester index
		loc_out.pri_cursor = 0;
		
		//modify start handler, in order to process output from start handler
		loc_out.addPostProcessor({
			start : function(requestInfo, startOut){
				loc_out.setData(loc_startOutDataName, startOut);
			}
		});
	};
	
	/*
	 * process request in sequence according to its index
	 */
	var loc_processNextRequestInSequence = function(data){
		if(data!=undefined)		loc_out.pri_isDynamic = true;
		
		if(loc_out.pri_isDynamic==false){
			//static 
			if(loc_out.pri_requests.length<=loc_out.pri_cursor){
				//not more request in queue
				loc_out.executeSuccessHandler(data, loc_out);
				return;
			}
			
			var item = loc_out.pri_requests[loc_out.pri_cursor];
			if(_.isFunction(item)==true){
				//if item is function, then execute the function and return the requestinfo object or out object
				item = item.call(loc_out, loc_out);
			}

			if(nosliwTypedObjectUtility.getObjectType(item)==NOSLIWCONSTANT.TYPEDOBJECT_TYPE_REQUEST){
				//for request
				loc_out.pri_requestInfos.push(item);
			}
			else{
				//other object, finish this request
				loc_out.executeSuccessHandler(item, loc_out);
				return;
			}
		}
		else{
			//dynamic
			if(data==undefined){
				loc_out.executeSuccessHandler(data, loc_out);
				return;
			}

			if(_.isFunction(data)){
				data = data.call(loc_out, loc_out);
			}

			if(data==undefined){
				loc_out.executeSuccessHandler(data, loc_out);
				return;
			}
			
			if(_.isArray(data)==true){
				//check if this array is data or a array of request
				var isRequestArray = true;
				if(data.length==0)  isRequestArray = false;
				else{
					for(var i in data){
						if(!nosliwTypedObjectUtility.getObjectType(data[i])==NOSLIWCONSTANT.TYPEDOBJECT_TYPE_REQUEST){
							isRequestArray = false;
							break;
						}
					}
				}
				if(isRequestArray==true){
					//if data is an array of request
					for(var i in data){
						loc_out.pri_requestInfos.push(data[i]);
					}
				}
				else{
					//other object, finish this request
					loc_out.executeSuccessHandler(data, loc_out);
					return;
				}
			}
			else{
				if(nosliwTypedObjectUtility.getObjectType(data)==NOSLIWCONSTANT.TYPEDOBJECT_TYPE_REQUEST){
					//for request
					loc_out.pri_requestInfos.push(data);
				}
				else{
					//other object, finish this request
					loc_out.executeSuccessHandler(data, loc_out);
					return;
				}
			}
		}
		var requestInfo = loc_out.pri_requestInfos[loc_out.pri_cursor];
		
		requestInfo.setParentRequest(loc_out);

		requestInfo.addPostProcessor({
			success : function(requestInfo, out){
				loc_out.pri_cursor++;
				
				if(out!=undefined){
					//dynamic, keep processing next request
					loc_processNextRequestInSequence(out);
				}
				else{
					var requestLen = undefined;
					if(loc_out.pri_isDynamic==false)   requestLen = loc_out.pri_requests.length;
					else requestLen = loc_out.pri_requestInfos.length;
					if(loc_out.pri_cursor<requestLen){
						//still more in request list, keep processing next request
						loc_processNextRequestInSequence();
					}
					else{
						//if finish all requests
						loc_out.executeSuccessHandler(undefined, loc_out);
					}
				}
			},
			error : function(requestInfo, serviceData){
				loc_out.executeErrorHandler(serviceData, loc_out);
			},
			exception : function(requestInfo, serviceData){
				loc_out.executeExceptionHandler(serviceData, loc_out);
			},
		});
		
		nosliw.getRequestServiceManager().processRequest(requestInfo);
	};
	
	var loc_process = function(){
		//retrieve start handler out
		startHandlerOut = loc_out.getData(loc_startOutDataName);
		//start process first request
		loc_processNextRequestInSequence(startHandlerOut);
	};
	
	var loc_out = {
		addRequest : function(requestInfo){
			if(!_.isFunction(requestInfo)){
				requestInfo.setParentRequest(this);
			}
			this.pri_requests.push(requestInfo);	
		},
	};
	
	loc_out = _.extend(nosliwCreateServiceRequestInfoCommon(service, handlers, requester_parent), loc_out);
	
	//request type
	loc_out.setType(NOSLIWCONSTANT.REQUEST_TYPE_SEQUENCE);
	
	loc_constructor(service, handlers, requester_parent);
	
	loc_out.setRequestExecuteInfo(new NosliwServiceRequestExecuteInfo(loc_process, this));
	
	return loc_out;
};
