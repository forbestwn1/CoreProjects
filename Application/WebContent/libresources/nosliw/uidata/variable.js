//get/create package
var packageObj = library.getChildPackage("variable");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_makeObjectWithType;
var node_getObjectType;
var node_makeObjectWithId;
var node_eventUtility;
var node_requestUtility;
var node_wrapperFactory;
var node_basicUtility;
var node_createEventObject;
var node_requestUtility;

//*******************************************   Start Node Definition  ************************************** 	
/**
 * two input model : 
 * 		1. parent variable + path from parent + requestInfo
 * 		2. wrapper + requestInfo
 *      3. data + path + requestInfo
 */
var node_createWrapperVariable = function(data1, data2, data3){
	
	/*
	 * clear up wrapper within this variable
	 * no event triggered
	 */
	var loc_clearupWrapper = function(requestInfo){
		//if no wrapper, no effect
		if(loc_out.prv_wrapper==undefined)   return;

		//unregister listener from wrapper
		loc_out.prv_wrapper.unregisterDataOperationListener(loc_out.prv_dataOperationEventObject);
		
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
			node_eventUtility.triggerEvent(loc_out.prv_lifecycleSource, node_CONSTANT.WRAPPERVARIABLE_EVENT_SETWRAPPER, requestInfo);
			return;
		}
		
		//clear up old wrapper first
		loc_clearupWrapper(requestInfo); 

		//set new wrapper
		loc_out.prv_wrapper = wrapper;
		//inform child variable that wrapper is set to new object
		node_eventUtility.triggerEvent(loc_out.prv_lifecycleEventObject, node_CONSTANT.WRAPPERVARIABLE_EVENT_SETWRAPPER, requestInfo);

		//listen to wrapper event
		loc_registerWrapperDataOperationEvent();
	};
	
	//listen to wrapper event
	var loc_registerWrapperDataOperationEvent = function(){
		if(loc_out.prv_wrapper==undefined)  return;
		loc_out.prv_wrapper.registerDataOperationListener(loc_out.prv_dataOperationEventObject, function(event, path, data, requestInfo){
			//ignore forward event
			if(event==node_CONSTANT.WRAPPER_EVENT_FORWARD)  return;
			//inform the operation
			loc_out.prv_dataOperationEventObject.triggerEvent(event, path, data, requestInfo);
		});
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(data1, data2, data3){
		//parent variable
		loc_out.prv_parent = undefined;
		//path from parent variable to this variable
		loc_out.prv_path = undefined;
		
		//wrapper object
		loc_out.prv_wrapper = undefined;
		
		//event source for event that communicate with child wrapper variables 
		loc_out.prv_lifecycleEventObject = node_createEventObject();
		//event source for event that communicate operation information with outsiders
		loc_out.prv_dataOperationEventObject = node_createEventObject();

		var requestInfo = node_requestUtility.getRequestInfoFromFunctionArguments(arguments);
		var data1Type = node_getObjectType(data1);
		if(data1Type==node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE){
			//for variable having parent variable
			loc_out.prv_parent = data1;
			loc_out.prv_path = node_basicUtility.emptyStringIfUndefined(data2);
			loc_out.prv_wrapper = node_wrapperFactory.createWrapper(loc_out.prv_parent.getWrapper(), loc_out.prv_path);
			loc_out.prv_parent.registerLifecycleEventListener(loc_out.prv_lifecycleEventObject, function(event, data, requestInfo){
				switch(event){
				case node_CONSTANT.WRAPPERVARIABLE_EVENT_SETWRAPPER:
					//create new wrapper based on wrapper in parent and path
					var newWrapper = node_wrapperFactory.createWrapper(loc_out.prv_parent.getWrapper(), loc_out.prv_path);
					loc_setWrapper(newWrapper, requestInfo);
					break;
				case node_CONSTANT.WRAPPERVARIABLE_EVENT_CLEARUP:
					loc_clearupWrapper(requestInfo);
					loc_lifecycleEventObject.triggerEvent(node_CONSTANT.WRAPPERVARIABLE_EVENT_CLEARUP, {}, requestInfo);
					break;
				case node_CONSTANT.WRAPPERVARIABLE_EVENT_DESTROY:
					loc_out.destroy(requestInfo);
					break;
				};
			});
		}
		else if(data1Type==node_CONSTANT.TYPEDOBJECT_TYPE_WRAPPER){
			//for wrapper
			loc_out.prv_wrapper = data1;
		}
		else{
			//for object
			loc_out.prv_wrapper = node_wrapperFactory.createWrapper(data1, data2, requestInfo);
		}
		if(loc_out.prv_wrapper!=undefined){
			loc_registerWrapperDataOperationEvent();
		}
	};
	
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		loc_clearupWrapper(requestInfo);
		loc_out.prv_lifecycleEventObject.triggerEvent(node_CONSTANT.WRAPPERVARIABLE_EVENT_DESTROY, {}, requestInfo);
		loc_out.prv_dataOperationEventObject.clearup();
		loc_out.prv_lifecycleEventObject.clearup();
	};
	
	var loc_out = {
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
				this.prv_lifecycleEventObject.triggerEvent(node_CONSTANT.WRAPPERVARIABLE_EVENT_CLEARUP, {}, requestInfo);
			},
			
			getValue : function(){
				if(this.prv_wrapper==undefined)   return undefined;
				else return this.prv_wrapper.getValue();
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
			createChildVariable : function(path, requestInfo){		return node_createWrapperVariable(this, path, requestInfo);			},

			/*
			 * register handler for operation event
			 */
			registerDataChangeEventListener : function(listenerEventObj, handler, thisContext){return this.prv_dataOperationEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);		},

			/*
			 * register handler for event of communication between parent and child variables
			 */
			registerLifecycleEventListener : function(listenerEventObj, handler, thisContext){return this.prv_lifecycleEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);	},
			
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
	
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE);
	loc_out = node_makeObjectWithId(loc_out, nosliw.generateId());
	
	node_getLifecycleInterface(loc_out).init(data1, data2, data3);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithid.makeObjectWithId", function(){node_makeObjectWithId = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createWrapperVariable", node_createWrapperVariable); 

})(packageObj);
