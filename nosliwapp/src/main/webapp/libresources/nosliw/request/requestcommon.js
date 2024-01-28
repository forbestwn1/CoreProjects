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
	var node_ServiceData;
	var node_requestProcessErrorUtility;
	var node_RequestResult;
//*******************************************   Start Node Definition  ************************************** 	

var node_RequestFinishInfo = function(finishType, data, thisContext){
	this.finishType = finishType;
	this.data = data;
	this.thisContext = thisContext;
};
	
/**
 * requester_parent: requester or parent request
 */
var node_createServiceRequestInfoCommon = function(service, handlers, requester_parent){
	
	var loc_moduleName = "requestInfo";

	service = node_requestUtility.buildService(service);

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
			pri_eventObjectIndividual : node_createEventObject(),
			//function for resume request process
			pri_resumeProcessor : undefined,
		};
		
		//construct handlers
		loc_out.pri_metaData.pri_handlers = {
			start : loc_constructHandler(node_CONSTANT.REQUEST_HANDLERNAME_START),
			success : loc_constructHandler(node_CONSTANT.REQUEST_HANDLERNAME_SUCCESS),
			error : loc_constructHandler(node_CONSTANT.REQUEST_HANDLERNAME_ERROR),
			exception : loc_constructHandler(node_CONSTANT.REQUEST_HANDLERNAME_EXCEPTION),
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
				if(postHandler!=undefined)			postHandler.call(loc_out, loc_out, out);
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
	 * do sth when request finish processing
	 *     change status
	 *     set result
	 *     trigue event
	 */
	var loc_finishRequest = function(type, data){
		loc_out.setStatus(node_CONSTANT.REQUEST_STATUS_DONE);
		loc_out.setResult(new node_RequestResult(type, data));
	};
	
	var loc_destroy = function(){
		loc_out.pri_metaData.pri_eventObject.clearup();
	};

	var loc_callHandler = function(handlerName, thisContext, data){
		var out = undefined;
		var handler = loc_out.getHandlers()[handlerName];
		if(handler!=undefined)			out = handler.call(thisContext, loc_out, data);
		return out;
	};
	
	var loc_requestFinish = function(requestFinishInfo){
		try{
			loc_finishRequest(requestFinishInfo.finishType, requestFinishInfo.data);
			return loc_callHandler(requestFinishInfo.finishType, requestFinishInfo.thisContext, requestFinishInfo.data);
		}
		catch(err){
			nosliw.runtime.getErrorManager().logError(err);
			if(requestFinishInfo.finishType==node_CONSTANT.REQUEST_FINISHTYPE_SUCCESS){
				loc_requestFinish(new node_RequestFinishInfo(node_CONSTANT.REQUEST_FINISHTYPE_ERROR, node_requestProcessErrorUtility.createRequestHandleErrorServiceData(err), requestFinishInfo.thisContext));
			}
//			loc_out.errorFinish(node_requestProcessErrorUtility.createRequestHandleErrorServiceData(err));
		}
	}; 
	
	var loc_tryRequestFinish = function(finishType, data, thisContext){
		var requestFinishInfo = new node_RequestFinishInfo(finishType, data, thisContext);
		if(loc_out.getStatus()==node_CONSTANT.REQUEST_STATUS_PROCESSING)	loc_requestFinish(requestFinishInfo);
		else if(loc_out.getStatus()==node_CONSTANT.REQUEST_STATUS_PAUSED){
			loc_setResumeProcessor(function(){
				loc_requestFinish(requestFinishInfo);
			});
		}
	};
	
	var loc_setResumeProcessor = function(processor){	loc_out.pri_metaData.pri_resumeProcessor = processor;	};
	
	var loc_getResumeProcessor = function(){   return loc_out.pri_metaData.pri_resumeProcessor;  };
	
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

			
			getData : function(name){
				if(name==undefined)  name="default";
				return this.pri_metaData.pri_data[name];
			},
			setData : function(data, name){this.pri_metaData.pri_data[name]=data;},
			withData : function(data, name){
				if(name==undefined)  name="default";
				this.setData(data, name);
				return this;
			},
			
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
			//post processor won't change the return value
			getPostProcessors : function(){ return this.pri_metaData.pri_postProcessors},
			addPostProcessor : function(processor){ this.pri_metaData.pri_postProcessors.push(processor); },
			insertPostProcessor : function(processor){ this.pri_metaData.pri_postProcessors.splice(0, 0, processor); },
			exectueHandlerByServiceData : function(serviceData, thisContext){
				var resultStatus = node_errorUtility.getServiceDataStatus(serviceData);
				switch(resultStatus){
				case node_CONSTANT.REMOTESERVICE_RESULT_SUCCESS:
					return this.successFinish(serviceData.data, thisContext);
					break;
				case node_CONSTANT.REMOTESERVICE_RESULT_EXCEPTION:
					return this.errorFinish(serviceData, thisContext);
					break;
				case node_CONSTANT.REMOTESERVICE_RESULT_ERROR:
					return this.exceptionFinish(serviceData, thisContext);
					break;
				}
			},
			
			start : function(thisContext){
				loc_out.setStatus(node_CONSTANT.REQUEST_STATUS_PROCESSING);
				loc_out.setResult();
				loc_out.pri_metaData.pri_eventObject.triggerEvent(node_CONSTANT.REQUEST_EVENT_ACTIVE, {}, loc_out);
				
				var out = undefined;
				//internal handler
				var handler = this.getHandlers().start;
				if(handler!=undefined)		out = handler.call(thisContext, this);
				return out;
			},
			
			successFinish : function(data, thisContext){
				loc_tryRequestFinish(node_CONSTANT.REQUEST_FINISHTYPE_SUCCESS, data, thisContext);
			},
			
			errorFinish : function(serviceData, thisContext){
				loc_tryRequestFinish(node_CONSTANT.REQUEST_FINISHTYPE_ERROR, serviceData, thisContext);
			},

			exceptionFinish : function(serviceData, thisContext){
				loc_tryRequestFinish(node_CONSTANT.REQUEST_FINISHTYPE_EXCEPTION, serviceData, thisContext);
			},
			
			pause : function(resumeProcessor){
				loc_out.setStatus(node_CONSTANT.REQUEST_STATUS_PAUSED);
				if(resumeProcessor!=undefined)   loc_setResumeProcessor(resumeProcessor);
			},
			
			resume : function(){
				loc_out.setStatus(node_CONSTANT.REQUEST_STATUS_PROCESSING);
				var processor = loc_getResumeProcessor();
				if(processor!=undefined)  processor();
			},
			
			/*
			 * set processor so that they can do sth before call the handlers
			 * we can keep call this method to insert mutiple processor 
			 */
			setRequestProcessors : function(processors){
				var handlers = this.getHandlers();				
				var newHandlers = {};
				_.each(node_requestUtility.getAllHandlerNames(), function(handlerName, i){
					newHandlers[handlerName] = node_requestUtility.createRequestProcessorHandlerFunction(handlers[handlerName], processors[handlerName]);
				});
				this.setHandlers(node_requestUtility.mergeHandlers(handlers, newHandlers));
			},

			/*
			 * set processor so that they can do sth after call the handlers 
			 */
//			setRequestPostProcessors : function(processors){
//				var handlers = this.getHandlers();
//				var newHandlers = {
//					start : node_requestUtility.createRequestPostProcessorHandlerFunction(handlers.start, processors.start),
//					success : node_requestUtility.createRequestPostProcessorHandlerFunction(handlers.success, processors.success),
//					error : node_requestUtility.createRequestPostProcessorHandlerFunction(handlers.error, processors.error),
//					exception : node_requestUtility.createRequestPostProcessorHandlerFunction(handlers.exception, processors.exception),
//				};
//				this.setHandlers(node_requestUtility.mergeHandlers(handlers, newHandlers));
//			},
			
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
			isRootRequest : function(){  return this.getParentRequest()==null;   },
			
			getStatus : function(){  return this.pri_metaData.pri_status;},
			setStatus : function(status){  		this.pri_metaData.pri_status = status;	},
			
			getResult : function(){  return this.pri_metaData.pri_result; },
			setResult : function(result){  this.pri_metaData.pri_result = result; },

			getInput : function(){  return this.pri_metaData.pri_input; },
			setInput : function(input){  this.pri_metaData.pri_input = input; },
			
			registerIndividualEventListener : function(listener, handler, thisContext){	return this.pri_metaData.pri_eventObjectIndividual.registerListener(undefined, listener, handler, thisContext);	},
			unregisterIndividualEventListener : function(listener){	this.pri_metaData.pri_eventObjectIndividual.unregister(listener);	},
			trigueIndividualEvent : function(event, eventData){	
				this.pri_metaData.pri_eventObjectIndividual.triggerEvent(event, eventData, this);
				this.pri_metaData.pri_eventObjectIndividual.clearup();
			},
			
			
			//it is for root request only
			registerEventListener : function(listener, handler, thisContext){	return this.pri_metaData.pri_eventObject.registerListener(undefined, listener, handler, thisContext);	},
			unregisterEventListener : function(listener){	this.pri_metaData.pri_eventObject.unregister(listener);	},
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
nosliw.registerSetNodeDataEvent("error.entity.ServiceData", function(){node_ServiceData = this.getData();});
nosliw.registerSetNodeDataEvent("request.errorUtility", function(){node_requestProcessErrorUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.entity.RequestResult", function(){node_RequestResult = this.getData();});


//Register Node by Name
packageObj.createChildNode("createServiceRequestInfoCommon", node_createServiceRequestInfoCommon); 

})(packageObj);
