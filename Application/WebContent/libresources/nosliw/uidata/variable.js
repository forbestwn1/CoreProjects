//get/create package
var packageObj = library.getChildPackage("variable");    

(function(packageObj){
//get used node
var node_ServiceInfo;
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
var node_RelativeEntityInfo;
var node_variableUtility;
var node_createServiceRequestInfoSequence;
var node_createServiceRequestInfoSet;
var node_OrderedContainerElementInfo;
var node_createVariableWrapper;
var node_createOrderedContainersInfo;
var node_createOrderVariableContainer;
var node_uiDataOperationServiceUtility;
var node_dataUtility;
var node_requestServiceProcessor;
var node_createServiceRequestInfoSimple;

//*******************************************   Start Node Definition  **************************************

/**
 * new variable
 * input model : 
 *		1. parent variable + path from parent
 *      2. data + undefined
 *      3. value + value type
 */
var node_newVariable = function(data1, data2, adapterInfo, requestInfo){
	//relative to parent : parent + path
	var loc_relativeVariableInfo;
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(data1, data2, adapterInfo, requestInfo){
		//whether this variable is live or destroyed
		loc_out.prv_isLive = true;

		//every variable has a id, it is for debuging purpose
		loc_out.prv_id = nosliw.runtime.getIdService().generateId();
		
		//adapter that will apply to value of wrapper
		loc_out.prv_valueAdapter = adapterInfo==undefined ? undefined : adapterInfo.valueAdapter;

		loc_out.prv_pathAdapter = adapterInfo==undefined ? undefined : adapterInfo.pathAdapter;
		
		//adapter that affect the event from wrapper and listener of this variable
		loc_out.prv_eventAdapter = adapterInfo==undefined ? undefined : adapterInfo.eventAdapter;

		loc_out.prv_destoryAdapter = adapterInfo==undefined ? undefined : adapterInfo.destroyAdapter; 
		
		//adapter that affect the data operation on wrapper, what is this for kkkkkk
		loc_out.prv_dataOperationRequestAdapter = adapterInfo==undefined ? undefined : adapterInfo.dataOperationRequestAdapter;;
		
		//lifecycle event object 
		loc_out.prv_lifecycleEventObject = node_createEventObject();
		//data operation event object
		loc_out.prv_dataOperationEventObject = node_createEventObject();
		//data change event object, it means child or itself changed
		loc_out.prv_dataChangeEventObject = node_createEventObject();

		//wrapper object
		loc_out.prv_wrapper = undefined;
		
		loc_out.prv_isBase = true;
		//wrapper type incase no wrapper object
		loc_out.prv_wrapperType;
		
		//normal child variables by path
		loc_out.prv_childrenVariable = {};
		
		//record how many usage of this variable.
		//when usage go to 0, that means it should be clean up
		loc_out.prv_usage = 0;
		
		var data1Type = node_getObjectType(data1);
		if(data1Type==node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE){
			//for variable having parent variable
			loc_out.prv_isBase = false;
			loc_relativeVariableInfo = new node_RelativeEntityInfo(data1, data2);
			
			//add current variable as child of data1 variable
			data1.prv_addChildVariable(loc_out, data2, adapterInfo);
			
			//build wrapper relationship with parent
			loc_out.prv_updateWrapperInRelativeVariable();
		}
		else{
			//for base object/data
			loc_out.prv_isBase = true;
			if(data1!=undefined){
				//create wrapper
				loc_setWrapper(node_wrapperFactory.createWrapper(data1, data2, requestInfo), requestInfo);
				//store wrapper type
				loc_out.prv_wrapperType = loc_out.prv_wrapper.getDataType();
			}
			else	loc_out.prv_wrapperType = data2;		//not create wrapper, just store the value type
		}
		
		nosliw.logging.info("************************  variable created   ************************");
		nosliw.logging.info("ID: " + loc_out.prv_id);
		if(loc_relativeVariableInfo!=undefined){
			nosliw.logging.info("Parent: " + loc_relativeVariableInfo.parent.prv_id);
			nosliw.logging.info("Parent Path: " + loc_relativeVariableInfo.path);
		}
		nosliw.logging.info("***************************************************************");
	};
	
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){loc_destroy();};
	
	//destroy variable and trigue lifecycle event 
	var loc_destroy = function(requestInfo){
		if(loc_out.prv_isLive){
			nosliw.logging.info("************************  variable destroying   ************************");
			nosliw.logging.info("ID: " + loc_out.prv_id);
			nosliw.logging.info("***************************************************************");
			
			//trigger lifecycle event
			loc_out.prv_lifecycleEventObject.triggerEvent(node_CONSTANT.WRAPPER_EVENT_CLEARUP_BEFORE, {}, requestInfo);

			loc_out.prv_isLive = false;
			
			//clean up event object
			loc_out.prv_dataOperationEventObject.clearup();
			loc_out.prv_dataChangeEventObject.clearup();
	
			//clean up adapter
			if(loc_out.prv_destoryAdapter!=undefined)   loc_out.prv_destoryAdapter();
	
			//clear children variable first
			_.each(loc_out.prv_childrenVariable, function(childVarInfo, path){		childVarInfo.variable.destroy(requestInfo);	});
			loc_out.prv_childrenVariable = {};
			
			//destroy wrapper 
			loc_destroyWrapper(requestInfo);
	
			//trigger lifecycle event
			loc_out.prv_lifecycleEventObject.triggerEvent(node_CONSTANT.WRAPPER_EVENT_CLEARUP_AFTER, {}, requestInfo);
			loc_out.prv_lifecycleEventObject.clearup();
			for (var key in loc_out){
			    if (loc_out.hasOwnProperty(key)){
			    	//don't delete function
			    	if(typeof loc_out[key] != 'function'){
			    		delete loc_out[key];
			    	}
			    }
			}
			loc_out.prv_isLive = false;
		}
	};
	
	/*
	 * destroy wrapper within this variable
	 * no event triggered
	 * destroy wrapper means wrapper's all resource get released
	 * it does not means variable is destroyed 
	 */
	var loc_destroyWrapper = function(requestInfo){
		//if no wrapper, no effect
		if(loc_out.prv_isWrapperExists()){
			//unregister listener from wrapper
			loc_out.prv_wrapper.unregisterDataOperationEventListener(loc_out.prv_dataOperationEventObject);
			loc_out.prv_wrapper.unregisterDataChangeEventListener(loc_out.prv_dataChangeEventObject);
			
			//destroy wrapper
			loc_out.prv_wrapper.destroy(requestInfo);
		}
		loc_out.prv_wrapper = undefined;
	};
	
	//listen to wrapper data operation event
	var loc_registerWrapperDataOperationEvent = function(){
		if(loc_out.prv_wrapper==undefined)  return;
		loc_out.prv_wrapper.registerDataOperationEventListener(loc_out.prv_dataOperationEventObject, function(event, eventData, requestInfo){
			loc_out.prv_dataOperationEventObject.triggerEvent(event, eventData, requestInfo);
			
			if(event==node_CONSTANT.WRAPPER_EVENT_DELETE){
				//data operation event turn to lifecycle event
				loc_out.destroy();
			}
		});
	};

	var loc_registerWrapperDataChangeEvent = function(){
		if(loc_out.prv_wrapper==undefined)  return;
		loc_out.prv_wrapper.registerDataChangeEventListener(loc_out.prv_dataChangeEventObject, function(event, eventData, requestInfo){
			loc_out.prv_dataChangeEventObject.triggerEvent(event, eventData, requestInfo);
		});
	};

	
	var loc_addNormalChildVariable = function(childVar, path){
		var childVarInfo = new node_ChildVariableInfo(childVar, path, path);
		loc_addChildVariable(loc_out.prv_childrenVariable, childVarInfo);
		return childVarInfo;
	};

	var loc_addChildVariableWithAdapter = function(childVar, path){
		var id = path+"_"+childVar.prv_id;
		var childVarInfo = new node_ChildVariableInfo(childVar, path, id, false);
		loc_addChildVariable(loc_out.prv_childrenVariable, childVarInfo);
		return childVarInfo;
	};
	
	var loc_addChildVariable = function(container, childVarInfo){
		container[childVarInfo.id] = childVarInfo;
		childVarInfo.variable.registerLifecycleEventListener(loc_out.prv_lifecycleEventObject, function(event, eventData, request){
			if(event==node_CONSTANT.WRAPPER_EVENT_CLEARUP_BEFORE){
				childVarInfo.variable.unregisterLifecycleEventListener(loc_out.prv_lifecycleEventObject);
				delete container[childVarInfo.id];
			}
		});
	};
	
	//set new wrapper
	var loc_setWrapper = function(wrapper, requestInfo){
		//destroy old wrapper first
		loc_destroyWrapper(requestInfo);
		//set wrapper
		loc_out.prv_wrapper = wrapper;
		
		if(wrapper!=undefined){
			nosliw.logging.info("************************  set wrapper to variable   ************************");
			nosliw.logging.info("variable: " + loc_out.prv_id);
			if(loc_out.prv_wrapper!=undefined)		nosliw.logging.info("wrapper: " + loc_out.prv_wrapper.prv_id);
			else  nosliw.logging.info("wrapper: " + undefined);
			nosliw.logging.info("***************************************************************");
			
			if(loc_out.prv_isWrapperExists()){
				//adapter
				loc_out.prv_wrapper.setValueAdapter(loc_out.prv_valueAdapter);
				loc_out.prv_wrapper.setPathAdapter(loc_out.prv_pathAdapter);
				loc_out.prv_wrapper.setEventAdapter(loc_out.prv_eventAdapter);
				//event listener
				loc_registerWrapperDataOperationEvent();
				loc_registerWrapperDataChangeEvent();
			}
			//update wrapper in children variable accordingly
			_.each(loc_out.prv_childrenVariable, function(childVariableInfo, id){
				childVariableInfo.variable.prv_updateWrapperInRelativeVariable(requestInfo);
			});
		}
	};

	var loc_out = {
			
			//update wrapper when parent's wrapper changed
			prv_updateWrapperInRelativeVariable : function(requestInfo){
				var parentVar = loc_relativeVariableInfo.parent;
				var parentWrapper = parentVar.prv_getWrapper();
				if(parentVar.prv_isWrapperExists())   loc_setWrapper(node_wrapperFactory.createWrapper(parentWrapper, loc_relativeVariableInfo.path), requestInfo);
				else loc_setWrapper(undefined, requestInfo);
			},
			
			prv_getWrapper : function(){	return this.prv_wrapper;	},

			prv_isWrapperExists : function(){
				if(this.prv_wrapper==null)   return false;
				return this.prv_wrapper.prv_isLive;
			},

			
			//has to be base variable
			//   data 
			//   value + dataTypeInfo
			//   value
			//   undefined, 
			prv_getSetBaseDataRequest : function(parm1, parm2, handlers, requestInfo){
				if(parm1==undefined){
					//no value means empty variable, no wrapper
					return node_createServiceRequestInfoSimple(new node_ServiceInfo("", {}), 
							function(requestInfo){  
								loc_setWrapper(undefined, requestInfo);
								if(parm2!=undefined)   loc_out.prv_wrapperType = parm2;
								loc_out.prv_dataChangeEventObject.triggerEvent(node_CONSTANT.WRAPPER_EVENT_REFRESH, undefined, requestInfo);
							}, 
							handlers, requestInfo);
				}
				
				//create empty wrapper fist
				var wrapperValue;      //store the value
				var entityType = node_getObjectType(parm1);
				var wrapper;
				if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_DATA){
					wrapperValue = parm1.value;
					parm1.value = undefined;
					//new wrapper according to data type
					wrapper = node_wrapperFactory.createWrapper(parm1, parm2, requestInfo);
					loc_setWrapper(wrapper, requestInfo);
				}
				else if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_VALUE){
					if(this.prv_wrapper==undefined){
						//no wrapper, generate one first
						wrapperValue = parm1;
						wrapper = node_wrapperFactory.createWrapper(undefined, loc_out.prv_wrapperType, requestInfo);
						loc_setWrapper(wrapper, requestInfo);
					}
					else{
						wrapper = this.prv_wrapper;
						wrapperValue = parm1;
						parm1 = undefined;
					}
				}
				//set new value
				return wrapper.getDataOperationRequest(node_uiDataOperationServiceUtility.createSetOperationService("", wrapperValue), handlers, requestInfo);
			},

			prv_getRelativeVariableInfo : function(){  return loc_relativeVariableInfo;   },

			prv_getChildren : function(){  return this.prv_childrenVariable;  },
			
			setValueAdapter : function(valueAdapter){  
				this.prv_valueAdapter = valueAdapter;
				if(this.prv_wrapper!=undefined)		this.prv_wrapper.setValueAdapter(valueAdapter);
			},
			
			setPathAdapter : function(pathAdapter){  
				this.prv_pathAdapter = pathAdapter;
				if(this.prv_wrapper!=undefined)		this.prv_wrapper.setPathAdapter(pathAdapter);
			},

			setEventAdapter : function(eventAdapter){	
				this.prv_eventAdapter = eventAdapter;	
				if(this.prv_wrapper!=undefined)		this.prv_wrapper.setEventAdapter(eventAdapter);
			},
			
			prv_addChildVariable : function(childVar, path, adapterInfo){
				var out;
				if(adapterInfo==undefined){
					out = loc_addNormalChildVariable(childVar, path);
				}
				else{
					out = loc_addChildVariableWithAdapter(childVar, path);
				}
				return out;
			},

			prv_getBaseVariable : function(){
				var current = loc_out;
				while(current.prv_isBase!=true){
					current = current.prv_getRelativeVariableInfo().parent;
				}
				return current;
			},
			
			prv_newWrapper : function(){
				loc_setWrapper(node_wrapperFactory.createWrapper(undefined, loc_out.prv_wrapperType), requestInfo);
			},
			
			//create child variable, if exist, then reuse it
			//return child variable info : {}
			//     variable : child variable 
			//     id : key in child container for child variable
			//		path
			createChildVariable : function(path, adapterInfo, requestInfo){
				return nosliw.runtime.getVariableManager().createChildVariable(this, path, adapterInfo);
			},

			/*
			 * register handler for operation event
			 */
			registerDataOperationEventListener : function(listenerEventObj, handler, thisContext){return this.prv_dataOperationEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);		},
			registerDataChangeEventListener : function(listenerEventObj, handler, thisContext){return this.prv_dataChangeEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);		},
			unregisterDataOperationEventListener : function(listenerEventObj){		this.prv_dataOperationEventObject.unregister(listenerEventObj);		},
			unregisterDataChangeEventListener : function(listenerEventObj){		this.prv_dataChangeEventObject.unregister(listenerEventObj);		},
			getDataOperationEventObject : function(){   return this.prv_dataOperationEventObject;   },
			getDataChangeEventObject : function(){   return this.prv_dataChangeEventObject;   },
			
			/*
			 * register handler for event of communication between parent and child variables
			 */
			registerLifecycleEventListener : function(listenerEventObj, handler, thisContext){return this.prv_lifecycleEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);	},
			unregisterLifecycleEventListener : function(listenerEventObj){		this.prv_lifecycleEventObject.unregister(listenerEventObj);		},
			getLifecycleEventObject : function(){   return this.prv_lifecycleEventObject;   },
			
			getDataOperationRequest : function(operationService, handlers, requester_parent){
				var that = this;
				var out;
				
				if(operationService.command!=node_CONSTANT.WRAPPER_OPERATION_SET && !this.prv_isWrapperExists())  return node_createServiceRequestInfoSimple(undefined, function(){}, handlers, requester_parent);
				
				if(operationService.command==node_CONSTANT.WRAPPER_OPERATION_SET && loc_out.prv_isBase==true && node_basicUtility.isStringEmpty(operationService.parms.path)){
					//for set root data
					return loc_out.prv_getSetBaseDataRequest(operationService.parms.value, operationService.parms.dataType, handlers, requester_parent);
				}

				if(this.prv_wrapper==undefined){
					loc_out.prv_getBaseVariable().prv_newWrapper()
				}

				
				if(this.prv_wrapper!=undefined){
					if(loc_out.prv_dataOperationRequestAdapter!=undefined){
						out = loc_out.prv_dataOperationRequestAdapter.dataOperationRequest(operationService, handlers, requester_parent);
					}
					else{
						out = this.prv_wrapper.getDataOperationRequest(operationService, handlers, requester_parent);
					}
				}
				
				out.setRequestProcessors({
					success : function(requestInfo, data){
						nosliw.logging.info("************************  variable operation   ************************");
						nosliw.logging.info("ID: " + that.prv_id);
						nosliw.logging.info("Wrapper: " + (that.prv_wrapper==undefined?"":that.prv_wrapper.prv_id));
						nosliw.logging.info("Parent: " , ((loc_relativeVariableInfo==undefined)?"":loc_relativeVariableInfo.parent.prv_id));
						nosliw.logging.info("ParentPath: " , ((loc_relativeVariableInfo==undefined)?"":loc_relativeVariableInfo.path)); 
						nosliw.logging.info("Request: " , node_basicUtility.stringify(operationService));
						nosliw.logging.info("Result: " , node_basicUtility.stringify(data));
						nosliw.logging.info("***************************************************************");
						return data;
					}
				});
				
				return out;
			},
			
			getGetValueRequest : function(handlers, request){
				return this.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(), handlers, request);				
			},
			
			executeDataOperationRequest : function(operationService, handlers, request){
				var requestInfo = this.getDataOperationRequest(operationService, handlers, request);
				node_requestServiceProcessor.processRequest(requestInfo);
			},
			
			use : function(){
				return nosliw.runtime.getVariableManager().useVariable(this);
			},
			
			release : function(requestInfo){
				nosliw.runtime.getVariableManager().releaseVariable(this);
			},
			
			destroy : function(requestInfo){
				node_getLifecycleInterface(loc_out).destroy(requestInfo);
//				nosliw.runtime.getVariableManager().destroyVariable(this);
			},
	};
	
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE);
	loc_out = node_makeObjectWithId(loc_out, nosliw.generateId());
	
	node_getLifecycleInterface(loc_out).init(data1, data2, adapterInfo, requestInfo);
	return loc_out;
};


var node_ChildVariableInfo = function(childVar, path, id, isNormal){
	this.variable = childVar;
	this.path = path;
	this.id = id;
	this.isNormal = isNormal==undefined?true:isNormal;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
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
nosliw.registerSetNodeDataEvent("uidata.entity.RelativeEntityInfo", function(){node_RelativeEntityInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.OrderedContainerElementInfo", function(){node_OrderedContainerElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createVariableWrapper", function(){node_createVariableWrapper = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createOrderedContainersInfo", function(){node_createOrderedContainersInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createOrderVariableContainer", function(){node_createOrderVariableContainer = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.utility", function(){node_variableUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});


//Register Node by Name

packageObj.createChildNode("ChildVariableInfo", node_ChildVariableInfo); 
packageObj.createChildNode("newVariable", node_newVariable); 

})(packageObj);
