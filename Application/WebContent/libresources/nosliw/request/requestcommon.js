//get/create package
var packageObj = library.getChildPackage("request");    

(function(packageObj){
	//get used node
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_CONSTANT;
	var node_requestUtility;
	var node_eventUtility;
	var node_basicUtility;
	var node_errorUtility;
	var node_createEventObject;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * requester_parent: requester or parent request
 */
var node_createServiceRequestInfoCommon = function(service, handlers, requester_parent){
	
	var loc_moduleName = "requestInfo";
	
	var loc_constructor = function(service, handlers, requester_parent){
		//parse requester_parent parm to get parent or requester info
		var parent = undefined;
		var requester = undefined;
		if(node_getObjectType(requester_parent)==node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST)		parent = requester_parent;
		else		requester = requester_parent;

		//who initilize this request
		loc_out.pri_requester = requester;
		//set parent request
		loc_out.setParentRequest(parent);
		
		//unique id for this request sequence
		if(loc_out.pri_id==undefined)		loc_out.pri_id = nosliw.generateId();
		//unique id for each request, so that we can trace each request in log
		loc_out.pri_innerId = nosliw.generateId();
		
		//what want to do 
		loc_out.pri_service = service;
		//original request handlers
		if(handlers!=undefined)		loc_out.pri_handlers = handlers;
		else loc_out.pri_handlers = {};

		//store all the information for implement the request (runtime)
		loc_out.pri_metaData = {
			//the execute information to process this request directly
			pri_execute : undefined,
			//final handlers used during implementation
			pri_handlers : {},
			//a list of thing to do after normal handler
			pri_postProcessors : [],
			//whether this request need remote request
			pri_isLocal : true, 
			//remote request this service request depend on
			pri_remoteRequest : undefined,
			//other data
			pri_data : {},
			//all parms for this request
			pri_parmData : {},
			//request status : init, processing, done
			pri_status : node_CONSTANT.REQUEST_STATUS_INIT,
			//request process result
			pri_result : undefined,
			//request process input, it is for runtime input which cannot be determined during request creation 
			pri_input : undefined,
			//event object
			pri_eventObject : node_createEventObject(),
		};
		
		//construct handlers
		loc_out.pri_metaData.pri_handlers = {
			start : loc_constructHandler("start"),
			success : loc_constructHandler("success"),
			error : loc_constructHandler("error"),
			exception : loc_constructHandler("exception"),
		};
		
		if(loc_out.pri_service!=undefined){
			//copy all the service data to metaData.data
			_.each(loc_out.pri_service.parms, function(value, name, list){
				this[name] = value;
			}, loc_out.pri_metaData.pri_parmData);
		};
		
		//set status, trigger event, clear result
		loc_initRequest();
	};
	
	/*
	 * construct handler so that it can return data 
	 */
	var loc_constructHandler = function(type){
		return function(requestInfo, data){
			if(type=="start") {
				//start to process request
				loc_startRequest();
			}
			
			//execute configured handler
			var handler = loc_out.pri_handlers[type];
			var out = data;
			if(handler!=undefined){
				var d = handler.call(loc_out, loc_out, data);
				if(d!=undefined){
					if(node_basicUtility.isEmptyValue(d))   out = undefined;  
					else out = d;
				}
			}
			
			//execute configured post process
			var postProcessors = loc_out.getPostProcessors();
			for(var i in postProcessors){
				var postHandler = postProcessors[i][type];
				if(postHandler!=undefined)			out = postHandler.call(loc_out, loc_out, out);
			}
			
			if(type!="start"){
				//do sth when request is done
				loc_finishRequest(out);
			}
			return out;
		};
	};
	
	/*
	 * do sth when request is created
	 *     change status
	 *     clear result
	 *     trigue event
	 */
	var loc_initRequest = function(){
		loc_out.setStatus(node_CONSTANT.REQUEST_STATUS_INIT);
		loc_out.setResult();
		loc_out.pri_metaData.pri_eventObject.triggerEvent(node_CONSTANT.REQUEST_EVENT_NEW, {}, loc_out);
	};
	
	/*
	 * do sth when request start processing
	 *     change status
	 *     clear result
	 *     trigue event
	 */
	var loc_startRequest = function(){
		loc_out.setStatus(node_CONSTANT.REQUEST_STATUS_PROCESSING);
		loc_out.setResult();
		loc_out.pri_metaData.pri_eventObject.triggerEvent(node_CONSTANT.REQUEST_EVENT_ACTIVE, {}, loc_out);
	};
	
	/*
	 * do sth when request finish processing
	 *     change status
	 *     set result
	 *     trigue event
	 */
	var loc_finishRequest = function(data){
		loc_out.setStatus(node_CONSTANT.REQUEST_STATUS_DONE);
		loc_out.setResult(data);
	};
	
	var loc_destroy = function(){
		loc_out.pri_metaData.pri_eventObject.clearup();
	};

	var loc_out = {
			
			getId : function(){return this.pri_id;},
			setId : function(id){
				this.pri_id = id;
				//do something after id get changed, for instance, change id of all child
				this.ovr_afterSetId();
			},
			ovr_afterSetId : function(){},
			
			getInnerId : function(){return this.getId()+"-"+this.pri_innerId;},
			
			getRequester : function(){return this.pri_requester;},
			setRequester : function(requester){this.pri_requester = requester;},
			
			getService : function(){return this.pri_service;},
			
			getType : function(){return this.pri_type;},
			setType : function(type){this.pri_type=type;},
			
			/*
			 * get/set meta data 
			 */
			getParms : function(){ return this.pri_metaData.pri_parmData; },
			getParmData : function(name){return this.pri_metaData.pri_parmData[name];},
			setParmData : function(name, data){this.pri_metaData.pri_parmData[name]=data;},

			
			getData : function(name){return this.pri_metaData.pri_data[name];},
			setData : function(name, data){this.pri_metaData.pri_data[name]=data;},
			
			/*
			 * execute info: provide function to run for this request
			 */
			getRequestExecuteInfo : function(){return this.pri_metaData.pri_execute;},
			setRequestExecuteInfo : function(execute){this.pri_metaData.pri_execute=execute;},
			
			/*
			 * hanlers within metadata is current handlers for request
			 */
			getHandlers : function(){return this.pri_metaData.pri_handlers;},
			setHandlers : function(handlers){this.pri_metaData.pri_handlers = handlers;},
			getPostProcessors : function(){ return this.pri_metaData.pri_postProcessors},
			addPostProcessor : function(processor){ 
				this.pri_metaData.pri_postProcessors.push(processor); 
			},
			
			exectueHandlerByServiceData : function(serviceData, thisContext){
				var resultStatus = node_errorUtility.getServiceDataStatus(serviceData);
				switch(resultStatus){
				case node_CONSTANT.REMOTESERVICE_RESULT_SUCCESS:
					return this.executeSuccessHandler(serviceData.data, thisContext);
					break;
				case node_CONSTANT.REMOTESERVICE_RESULT_EXCEPTION:
					return this.executeErrorHandler(serviceData, thisContext);
					break;
				case node_CONSTANT.REMOTESERVICE_RESULT_ERROR:
					return this.executeExceptionHandler(serviceData, thisContext);
					break;
				}
				
			},
			
			executeHandler : function(type, thisContext, data){
				if(type=="start")		return this.executeStartHandler(thisContext);
				if(type=="success")		return this.executeSuccessHandler(data, thisContext);
				if(type=="error")		return this.executeErrorHandler(data, thisContext);
				if(type=="exception")		return this.executeExceptionHandler(data, thisContext);
			},
			
			executeStartHandler : function(thisContext){
//				nosliw.logging.info(loc_moduleName, this.getInnerId(), "Start handler");
				var out = undefined;
				//internal handler
				var handler = this.getHandlers().start;
				if(handler!=undefined)		out = handler.call(thisContext, this);
				return out;
			},
			
			executeSuccessHandler : function(data, thisContext){
//				nosliw.logging.info(loc_moduleName, this.getInnerId(), "Success handler");
//				nosliw.logging.trace(loc_moduleName, this.getInnerId(), "Data ", data);

				var out = undefined;
				//internal handler
				var handler = this.getHandlers().success;
				if(handler!=undefined)			out = handler.call(thisContext, this, data);
				return out;
			},
			
			executeErrorHandler : function(serviceData, thisContext){
//				nosliw.logging.error(loc_moduleName, this.getInnerId(), "Error handler");
//				nosliw.logging.trace(loc_moduleName, this.getInnerId(), serviceData);

				var out = undefined;
				//internal handler
				var handler = this.getHandlers().error;
				if(handler!=undefined)			out = handler.call(thisContext, this, serviceData);
				return out;
			},

			executeExceptionHandler : function(serviceData, thisContext){
//				nosliw.logging.error(loc_moduleName, this.getInnerId(), "Exception handler");
//				nosliw.logging.trace(loc_moduleName, this.getInnerId(), serviceData);

				var out = undefined;
				//internal handler
				var handler = this.getHandlers().exception;
				if(handler!=undefined)			out = handler.call(thisContext, this, serviceData);
				return out;
			},
			
			
			/*
			 * set processor so that they can do sth before call the handlers
			 * we can keep call this method to insert mutiple processor 
			 */
			setRequestProcessors : function(processors){
				
				var handlers = this.getHandlers();
				var newHandlers = {
					start : node_requestUtility.createRequestProcessorHandlerFunction(handlers.start, processors.start),
					success : node_requestUtility.createRequestProcessorHandlerFunction(handlers.success, processors.success),
					error : node_requestUtility.createRequestProcessorHandlerFunction(handlers.error, processors.error),
					exception : node_requestUtility.createRequestProcessorHandlerFunction(handlers.exception, processors.exception),
				};
				this.setHandlers(node_requestUtility.mergeHandlers(handlers, newHandlers));
			},

			/*
			 * set processor so that they can do sth after call the handlers 
			 */
/*			
			setRequestPostProcessors : function(processors){
				var handlers = this.getHandlers();
				var newHandlers = {
					start : node_requestUtility.createRequestPostProcessorHandlerFunction(handlers.start, processors.start),
					success : node_requestUtility.createRequestPostProcessorHandlerFunction(handlers.success, processors.success),
					error : node_requestUtility.createRequestPostProcessorHandlerFunction(handlers.error, processors.error),
					exception : node_requestUtility.createRequestPostProcessorHandlerFunction(handlers.exception, processors.exception),
				};
				this.setHandlers(node_requestUtility.mergeHandlers(handlers, newHandlers));
			},
*/
			
			/*
			 * whether this request do remote ajax call 
			 */
			isLocalRequest : function(){return this.pri_metaData.pri_isLocal;},
			setIsLocalRequest : function(local){this.pri_metaData.pri_isLocal=local;},

			/*
			 * 
			 */
			getRemoteRequest : function(){return this.pri_metaData.pri_remoteRequest;},
			setRemoteRequest : function(remoteRequest){this.pri_metaData.pri_remoteRequest=remoteRequest;},
			
			getParentRequest : function(){  return this.pri_parentRequest;  },
			setParentRequest : function(parentRequest){  
				if(parentRequest!=undefined){
					this.pri_parentRequest = parentRequest;
					
					//set dependent request id based on parent request id
					this.setId(parentRequest.getId());
					//set dependent requester base on parent requester
					this.setRequester(parentRequest.getRequester());
				}
			},
			
			/*
			 * root request 
			 */
			getRootRequest : function(){
				var request = this;
				var parent = request.getParentRequest(); 
				while(parent!=undefined){
					request = parent;
					parent = request.getParentRequest();
				}
				return request;
			},
			
			getStatus : function(){  return this.pri_metaData.pri_status;},
			setStatus : function(status){  this.pri_metaData.pri_status = status;},
			
			getResult : function(){  return this.pri_metaData.pri_result; },
			setResult : function(result){  this.pri_metaData.pri_result = result; },

			getInput : function(){  return this.pri_metaData.pri_input; },
			setInput : function(input){  this.pri_metaData.pri_input = input; },
			
			//it is for root request only
			registerEventListener : function(listener, handler, thisContext){	this.pri_metaData.pri_eventObject.registerListener(undefined, listener, handler, thisContext);	},
			unregisterEventListener : function(listener, handler, thisContext){	this.pri_metaData.pri_eventObject.unregister(listener);	},
			trigueEvent : function(event, eventData){	this.pri_metaData.pri_eventObject.triggerEvent(event, eventData, this);	},

			//it is for root request only
			almostDone : function(){	this.trigueEvent(node_CONSTANT.REQUEST_EVENT_ALMOSTDONE);		},
			done : function(){
				this.trigueEvent(node_CONSTANT.REQUEST_EVENT_DONE);
				loc_destroy();
			},
			
			trackRequestStack : function(){
				if(loc_out.pri_parentRequest!=undefined)   loc_out.pri_parentRequest.trackRequestStack();
				console.log(node_basicUtility.stringify(loc_out.pri_service));
			}
	};
	
	loc_constructor(service, handlers, requester_parent);
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("error.utility", function(){node_errorUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});


//Register Node by Name
packageObj.createChildNode("createServiceRequestInfoCommon", node_createServiceRequestInfoCommon); 

})(packageObj);
