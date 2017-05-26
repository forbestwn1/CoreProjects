//get/create package
var packageObj = library.getChildPackage("request");    

(function(packageObj){
	//get used node
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_CONSTANT;
	var node_requestUtility;
	var node_eventUtility;
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
		loc_out.pri_id = nosliw.generateId();
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
			//event source
			pri_eventSource : {},
			//event listeners
			pri_eventListeners : [],
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
					if(nosliwCommonUtility.isEmptyValue(d))   out = undefined;  
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
		node_requestUtility.triggerEventWithRequest(loc_out.pri_metaData.pri_eventSource, node_CONSTANT.REQUEST_EVENT_NEW, {}, loc_out);
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
		node_requestUtility.triggerEventWithRequest(loc_out.pri_metaData.pri_eventSource, node_CONSTANT.REQUEST_EVENT_ACTIVE, {}, loc_out);
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
		
		node_requestUtility.triggerEventWithRequest(loc_out.pri_metaData.pri_eventSource, node_CONSTANT.REQUEST_EVENT_DONE, data, loc_out);

		//unregister all listeners
		_.each(loc_out.pri_metaData.pri_eventListeners, function(listener, key, list){
			node_eventUtilty.unregister(listener);
		}, this);
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
			
			executeHandler : function(type, thisContext, data){
				if(type=="start")		return this.executeStartHandler(thisContext);
				if(type=="success")		return this.executeSuccessHandler(data, thisContext);
				if(type=="error")		return this.executeErrorHandler(data, thisContext);
				if(type=="exception")		return this.executeExceptionHandler(data, thisContext);
			},
			
			executeStartHandler : function(thisContext){
				nosliwLogging.info(loc_moduleName, this.getInnerId(), "Start handler");
				var out = undefined;
				//internal handler
				var handler = this.getHandlers().start;
				if(handler!=undefined)		out = handler.call(thisContext, this);
				return out;
			},
			
			executeSuccessHandler : function(data, thisContext){
				nosliwLogging.info(loc_moduleName, this.getInnerId(), "Success handler");
				nosliwLogging.trace(loc_moduleName, this.getInnerId(), "Data ", data);

				var out = undefined;
				//internal handler
				var handler = this.getHandlers().success;
				if(handler!=undefined)			out = handler.call(thisContext, this, data);
				return out;
			},
			
			executeErrorHandler : function(serviceData, thisContext){
				nosliwLogging.error(loc_moduleName, this.getInnerId(), "Error handler");
				nosliwLogging.trace(loc_moduleName, this.getInnerId(), serviceData);

				var out = undefined;
				//internal handler
				var handler = this.getHandlers().error;
				if(handler!=undefined)			out = handler.call(thisContext, this, serviceData);
				return out;
			},

			executeExceptionHandler : function(serviceData, thisContext){
				nosliwLogging.error(loc_moduleName, this.getInnerId(), "Exception handler");
				nosliwLogging.trace(loc_moduleName, this.getInnerId(), serviceData);

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
					start : nosliwRequestUtility.createRequestProcessorHandlerFunction(handlers.start, processors.start),
					success : nosliwRequestUtility.createRequestProcessorHandlerFunction(handlers.success, processors.success),
					error : nosliwRequestUtility.createRequestProcessorHandlerFunction(handlers.error, processors.error),
					exception : nosliwRequestUtility.createRequestProcessorHandlerFunction(handlers.exception, processors.exception),
				};
				this.setHandlers(nosliwRequestUtility.mergeHandlers(handlers, newHandlers));
			},

			/*
			 * set processor so that they can do sth after call the handlers 
			 */
/*			
			setRequestPostProcessors : function(processors){
				var handlers = this.getHandlers();
				var newHandlers = {
					start : nosliwRequestUtility.createRequestPostProcessorHandlerFunction(handlers.start, processors.start),
					success : nosliwRequestUtility.createRequestPostProcessorHandlerFunction(handlers.success, processors.success),
					error : nosliwRequestUtility.createRequestPostProcessorHandlerFunction(handlers.error, processors.error),
					exception : nosliwRequestUtility.createRequestPostProcessorHandlerFunction(handlers.exception, processors.exception),
				};
				this.setHandlers(nosliwRequestUtility.mergeHandlers(handlers, newHandlers));
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
			
			registerEventListener : function(handler){
				var listener = nosliwRequestUtility.registerEventWithRequest(this.pri_metaData.pri_eventSource, node_CONSTANT.EVENT_EVENTNAME_ALL, handler, this);
				this.pri_metaData.pri_eventListeners.push(listener);
				return listener;
			},
	};
	
	loc_constructor(service, handlers, requester_parent);
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("createServiceRequestInfoCommon", node_createServiceRequestInfoCommon); 

	var module = {
		start : function(packageObj){
			node_makeObjectWithType = packageObj.getNodeData("common.objectwithtype.makeObjectWithType");
			node_getObjectType = packageObj.getNodeData("common.objectwithtype.getObjectType");
			node_CONSTANT = packageObj.getNodeData("constant.NOSLIWCONSTANT");
			node_requestUtility = packageObj.getNodeData("request.utility");
			node_eventUtility = packageObj.getNodeData("common.event.utility");
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
