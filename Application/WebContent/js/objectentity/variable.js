/**
 * two input model : 
 * 		1. parent variable + path from parent + requestInfo
 * 		2. wrapper + requestInfo
 */
var nosliwCreateWrapperVariable = function(data1, data2, data3){
	
	/*
	 * clear up wrapper within this variable
	 * no event triggered
	 */
	var loc_clearupWrapper = function(requestInfo){
		//if no wrapper, no effect
		if(loc_out.prv_wrapper==undefined)   return;

		//unregister wrapper listener
		nosliwEventUtility.unregisterAllListeners(loc_wrapperListeners);
		loc_out.prv_wrapperListeners = [];
		
		//destroy wrapper
		loc_out.prv_wrapper.destroy(requestInfo);
		loc_out.prv_wrapper = undefined;
	};
	
	/*
	 * set new wrapper
	 */
	var loc_setWrapper = function(wrapper, requestInfo){
		
		if(wrapper==undefined){
			//if no wrapper , then clear up
			loc_out.clearupWrapper(requestInfo);
			loc_out.prv_lifecycleSource.triggerEvent(NOSLIWCONSTANT.WRAPPERVARIABLE_EVENT_CLEARUP, {}, requestInfo);
			return;
		}
		
		//clear up old wrapper first
		loc_clearupWrapper(requestInfo); 

		//set new wrapper
		loc_out.prv_wrapper = wrapper;
		//inform child variable that wrapper is set to new object
		loc_out.prv_lifecycleSource.triggerEvent(NOSLIWCONSTANT.WRAPPERVARIABLE_EVENT_SETWRAPPER, {}, requestInfo);

		//listen to wrapper event
		loc_registerWrapperDataOperationEvent();
	};
	
	
	//listen to wrapper event
	var loc_registerWrapperDataOperationEvent = function(){
		if(loc_out.prv_wrapper==undefined)  return;
		var listener = loc_out.prv_wrapper.registerDataOperationEvent(function(event, path, data, requestInfo){
			//ignore forward event
			if(event==NOSLIWCONSTANT.WRAPPER_EVENT_FORWARD)  return;
			//inform the operation
			loc_out.prv_dataOperationSource.triggerEvent(event, path, data, requestInfo);
		});
		loc_out.prv_wrapperListeners.push(listener);
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT"] = function(data1, data2, data3){
		//parent variable
		loc_out.prv_parent = undefined;
		//path from parent variable to this variable
		loc_out.prv_path = undefined;
		
		//wrapper object
		loc_out.prv_wrapper = undefined;
		
		//event source for event that communicate with child wrapper variables 
		loc_out.prv_lifecycleSource = nosliwCreateRequestEventSource();
		//event source for event that communicate operation information with outsiders
		loc_out.prv_dataOperationSource = nosliwCreateWrapperOperationEventSource();

		//all the listeners object for event from wrapper inside
		loc_out.prv_wrapperListeners = [];
		//all the listeners object that wrapper variable used to other's event
		loc_out.prv_listeners = [];
		
		var requestInfo = nosliwRequestUtility.getRequestInfoFromFunctionArguments(arguments);
		var data1Type = nosliwTypedObjectUtility.getObjectType(data1);
		if(data1Type==NOSLIWCONSTANT.TYPEDOBJECT_TYPE_VARIABLE){
			//for variable having parent variable
			loc_out.prv_parent = data1;
			loc_out.prv_path = nosliwCommonUtility.emptyStringIfUndefined(data2);
			loc_out.prv_wrapper = nosliwCreateWraper(loc_out.prv_parent.getWrapper(), loc_out.prv_path);
			loc_out.prv_listeners.push(loc_out.prv_parent.registerLifecycleEvent(function(event, data, requestInfo){
				switch(event){
				case NOSLIWCONSTANT.WRAPPERVARIABLE_EVENT_SETWRAPPER:
					//create new wrapper based on wrapper in parent and path
					var newWrapper = nosliwCreateWraper(loc_out.prv_parent.getWrapper(), loc_out.prv_path);
					loc_setWrapper(newWrapper, requestInfo);
					break;
				case NOSLIWCONSTANT.WRAPPERVARIABLE_EVENT_CLEARUP:
					loc_clearupWrapper(requestInfo);
					loc_lifecycleSource.triggerEvent(NOSLIWCONSTANT.WRAPPERVARIABLE_EVENT_CLEARUP, {}, requestInfo);
					break;
				case NOSLIWCONSTANT.WRAPPERVARIABLE_EVENT_DESTROY:
					loc_out.destroy(requestInfo);
					break;
				};
			}));
		}
		else if(data1Type==NOSLIWCONSTANT.TYPEDOBJECT_TYPE_WRAPPER){
			//for wrapper
			loc_out.prv_wrapper = data1;
		}
		else{
			//for object
			loc_out.prv_wrapper = nosliwCreateWraper(data1, data2, requestInfo);
		}
		if(loc_out.prv_wrapper!=undefined){
			loc_registerWrapperDataOperationEvent();
		}
	};
	
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY"] = function(requestInfo){
		loc_clearupWrapper(requestInfo);
		loc_lifecycleSource.triggerEvent(NOSLIWCONSTANT.WRAPPERVARIABLE_EVENT_DESTROY, {}, requestInfo);
		
		nosliwEventUtility.unregisterAllListeners(loc_out.prv_listeners);
	};
	
	var loc_out = {
			ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},

			/*
			 * set wrapper object to variable
			 * only for root variable
			 */
			setWrapper : function(wrapper, requestInfo){	loc_setWrapper(wrapper, requestInfo);	},
			
			/*
			 * get wrapper object contained in variable
			 */
			getWrapper : function(){return this.prv_wrapper;},
			
			/*
			 * clear up wrapper within this variable
			 */
			clearupWrapper : function(requestInfo){
				loc_clearupWrapper(requestInfo);
				loc_lifecycleSource.triggerEvent(NOSLIWCONSTANT.WRAPPERVARIABLE_EVENT_CLEARUP, {}, requestInfo);
			},
			
			/*
			 * get data object within this variable
			 */
			getData : function(){
				if(this.prv_wrapper==undefined)   return undefined;
				else return this.prv_wrapper.getData();
			},
			
			/*
			 * create child variable
			 */
			createChildVariable : function(path, requestInfo){		return nosliwCreateWrapperVariable(this, path, requestInfo);			},

			/*
			 * register handler for operation event
			 */
			registerDataChangeEvent : function(handler, thisContext){return this.prv_dataOperationSource.registerEventHandler(handler, thisContext);		},
			
			/*
			 * register handler for event of communication between parent and child variables
			 */
			registerLifecycleEvent : function(handler, thisContext){return this.prv_lifecycleSource.registerEventHandler(handler, thisContext);	},
			
			/*
			 * request to do data operation
			 */
			requestDataOperation : function(service, request){
				if(this.prv_wrapper!=undefined){
					this.prv_wrapper.requestDataOperation(service, request);
				}
			},
			
			handleEachElement : function(handler, thatContext){
				if(this.prv_wrapper!=undefined){
					this.prv_wrapper.handleEachElement(handler, thatContext);
				}
			}			
	};
	
	//append resource life cycle method to out obj
	loc_out = nosliwLifecycleUtility.makeResourceObject(loc_out);
	loc_out = nosliwTypedObjectUtility.makeTypedObject(loc_out, NOSLIWCONSTANT.TYPEDOBJECT_TYPE_VARIABLE);
	loc_out = nosliwObjectWithIdUtility.makeObjectWithId(loc_out);
	
	loc_out.init(data1, data2, data3);
	
	return loc_out;
};
