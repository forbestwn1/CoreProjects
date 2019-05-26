//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_eventUtility;
	var node_getObjectType;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){
	var loc_out = {
			
			buildService : function(service){
				if(service!=undefined)  return service;
				else{
					return {
						name : loc_out.buildService.caller.caller.name,
					};
				} 
			},
			
			/**
			 * last one in argus should be request info
			 */
			getRequestInfoFromFunctionArguments : function(argsArray){
				return argsArray[argsArray.length-1];
			},

			getHandlersFromFunctionArguments : function(argsArray){
				return argsArray[argsArray.length-2];
			},
			
			/*
			 * register listener for event related with request 
			 */
			registerEventWithRequest : function(source, eventName, handler, thisContext){
				var listener = {};
				node_eventUtility.registerEvent(listener, source, eventName, function(event, data){
					handler.call(this, event, data.data, data.requestInfo);
				}, thisContext);
				return listener;
			},
			
			/*
			 * trigger event related with request
			 */
			triggerEventWithRequest : function(source, event, data, requestInfo){
				node_eventUtility.triggerEvent(source, event, {
					data : data,
					requestInfo : requestInfo,
				});		
			},

			
			/*
			 * get remote service task by requestInfo according to  convention
			 * convention : 
			 * 		remote request service name : requestInfo service name
			 * 		remote request service data : requestInfo service data
			 */
			getRemoteServiceTask : function(syncTaskName, requestInfo){
				//since is building remote service task, this request need remote call
				requestInfo.setIsLocalRequest(false);
				//create remote request handlers based on service request handlers 
				var handlers = node_requestUtility.getRemoteServiceTaskHandlersFromRequestHandlers(requestInfo.getHandlers());
				var remoteReq = new NosliwRemoteServiceTask(syncTaskName, new NosliwServiceInfo(requestInfo.getService().command, requestInfo.getParms()), handlers, requestInfo);
				requestInfo.setRemoteRequest(remoteReq);
				return remoteReq;
			},
			
			/*
			 * clone handlers
			 */
			cloneHandlers : function(handlers){
				return this.mergeHandlers(handlers);
			},
			
			/*
			 * merge two handlers together to create a new one
			 * the second handler will override the first one
			 */
			mergeHandlers : function(handlers, overrideHandlers){
				var out = {};
				
				out.start = handlers.start;
				if(overrideHandlers!=undefined && overrideHandlers.start!=undefined)  out.start=overrideHandlers.start;

				out.success = handlers.success;
				if(overrideHandlers!=undefined && overrideHandlers.success!=undefined)  out.success=overrideHandlers.success;

				out.error = handlers.error;
				if(overrideHandlers!=undefined && overrideHandlers.error!=undefined)  out.error=overrideHandlers.error;

				out.exception = handlers.exception;
				if(overrideHandlers!=undefined && overrideHandlers.exception!=undefined)  out.exception=overrideHandlers.exception;

				return out;
			},

			/*
			 * create remote service tasks handlers from request handlers
			 */
			getRemoteServiceTaskHandlersFromRequestHandlers : function(handlers){
				var requestHandlers = handlers;
				var out = {};
				
				var success = requestHandlers.success;
				if(success!=undefined){
					out.success = function(serviceTask, data){
						var requestInfo = serviceTask.requestInfo;
						success.call(requestInfo, requestInfo, data);
					};
				}

				var error = requestHandlers.error;
				if(error!=undefined){
					out.error = function(serviceTask, serviceData){
						var requestInfo = serviceTask.requestInfo;
						error.call(requestInfo, requestInfo, serviceData);
					};
				}
				
				var exception = requestHandlers.exception;
				if(exception!=undefined){
					out.exception = function(serviceTask, serviceData){
						var requestInfo = serviceTask.requestInfo;
						exception.call(requestInfo, requestInfo, serviceData);
					};
				}

				return out;
			},

			/*
			 * create a new handler function that insert  requestProcessor into handler input
			 */
			createRequestProcessorHandlerFunction : function(handler1, requestProcessor1){
				var handler = handler1;
				var requestProcessor = requestProcessor1;
				if(requestProcessor==undefined)  return handler;
				return function(requestInfo, data){
					var processorOut = requestProcessor.call(requestInfo, requestInfo, data);
					if(handler==undefined)  return processorOut;
					return handler.call(requestInfo, requestInfo, processorOut);
				};
			},

			/*
			 * create a new handler function that do post process into handler input
			 */
			createRequestPostProcessorHandlerFunction : function(handler1, requestProcessor1){
				var handler = handler1;
				var requestProcessor = requestProcessor1;
				if(requestProcessor==undefined)  return handler;
				return function(requestInfo, data){
					var handlerOut = undefined;
					if(handler!=undefined)  handlerOut = handler.call(requestInfo, requestInfo, data);
					var processorOut = requestProcessor.call(requestInfo, requestInfo, handlerOut);
					return processorOut;
				};
			},
	};
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
