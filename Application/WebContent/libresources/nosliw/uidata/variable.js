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

//*******************************************   Start Node Definition  ************************************** 	
/**
 * two input model : 
 * 		1. parent variable + path from parent
 *      2. data + undefined
 *      3. value + value type
 */
var node_createVariable = function(data1, data2, adapterInfo){
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(data1, data2, adapterInfo){
		//every variable has a id, it is for debuging purpose
		loc_out.prv_id = nosliw.runtime.getIdService().generateId();

		//adapter that will apply to value of wrapper
		loc_out.prv_valueAdapter = adapterInfo==undefined ? undefined : adapterInfo.valueAdapter;

		loc_out.prv_pathAdapter = adapterInfo==undefined ? undefined : adapterInfo.pathAdapter;
		
		//adapter that affect the event from wrapper and listener of this variable
		loc_out.prv_eventAdapter = adapterInfo==undefined ? undefined : adapterInfo.eventAdapter;

		loc_out.prv_destoryAdapter = adapterInfo==undefined ? undefined : adapterInfo.destroyAdapter; 
		
		//adapter that affect the data operation on wrapper
		loc_out.prv_dataOperationRequestAdapter = adapterInfo==undefined ? undefined : adapterInfo.dataOperationRequestAdapter;;
		
		//event source for event that communicate with child wrapper variables 
		loc_out.prv_lifecycleEventObject = node_createEventObject();
		//event source for event that communicate operation information with outsiders
		loc_out.prv_dataOperationEventObject = node_createEventObject();

		//wrapper object
		loc_out.prv_wrapper = undefined;

		loc_out.prv_isBase = true;
		
		//relative to parent : parent + path
		loc_out.prv_relativeVariableInfo;
		
		//normal child variables by path
		loc_out.prv_childrenVariable = {};
		//child variables with extra adapter
		loc_out.prv_extraChildrenVariable = {};
		
		//store information for children order info
		loc_out.prv_orderChildrenInfo;
		
		//record how many usage of this variable.
		//when usage go to 0, that means it should be clean up
		loc_out.prv_usage = 0;
		
		var data1Type = node_getObjectType(data1);
		if(data1Type==node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE){
			//for variable having parent variable
			loc_out.prv_isBase = false;
			loc_out.prv_relativeVariableInfo = new node_RelativeEntityInfo(data1, data2);
			
			//build wrapper relationship with parent
			loc_updateWrapperByParent();
		}
		else{
			//for object/data
			loc_out.prv_isBase = true;
			var r = loc_out.prv_getSetBaseDataRequest(data1, data2);
			node_requestServiceProcessor.processRequest(r, true);
		}
	};
	
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		
		if(loc_out.prv_destoryAdapter!=undefined)   loc_out.prv_destoryAdapter();
		
		//clear children variable first
		for (var key in loc_out.prv_childrenVariable){
		    if (loc_out.prv_childrenVariable.hasOwnProperty(key)){
		    	loc_out.prv_childrenVariable[key].destroy(requestInfo);
		        delete loc_out.prv_childrenVariable[key];
		    }
		}	
		
		//trigger lifecycle event
		loc_out.prv_lifecycleEventObject.triggerEvent(node_CONSTANT.WRAPPER_EVENT_CLEARUP, {}, requestInfo);
		
		//destroy wrapper 
		loc_destroyWrapper(requestInfo);

		//clean up event object
		loc_out.prv_dataOperationEventObject.clearup();
		loc_out.prv_lifecycleEventObject.clearup();
		
		for (var key in loc_out){
		    if (loc_out.hasOwnProperty(key)){
		        delete loc_out[key];
		    }
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
		if(loc_out.prv_wrapper==undefined)   return;

		//unregister listener from wrapper
		loc_out.prv_wrapper.unregisterDataOperationEventListener(loc_out.prv_dataOperationEventObject);
		
		//destroy wrapper
		loc_out.prv_wrapper.destroy(requestInfo);

		loc_out.prv_wrapper = undefined;
	};
	
	var loc_updateWrapperByParent = function(requestInfo){
		var parentWrapper = loc_out.prv_relativeVariableInfo.parent.prv_getWrapper();
		if(parentWrapper!=undefined)   loc_setWrapper(node_wrapperFactory.createWrapper(parentWrapper, loc_out.prv_relativeVariableInfo.path), requestInfo);
		else loc_setWrapper(undefined, requestInfo);
	};
	

	var loc_setWrapper = function(wrapper, requestInfo){
		loc_out.prv_wrapper = wrapper;
		if(loc_out.prv_wrapper!=undefined){
			loc_out.prv_wrapper.setValueAdapter(loc_out.prv_valueAdapter);
			loc_out.prv_wrapper.setPathAdapter(loc_out.prv_pathAdapter);
			loc_registerWrapperDataOperationEvent();
		}
	};

	//listen to wrapper data operation event
	var loc_registerWrapperDataOperationEvent = function(){
		if(loc_out.prv_wrapper==undefined)  return;
		loc_out.prv_wrapper.registerDataOperationEventListener(loc_out.prv_dataOperationEventObject, function(event, eventData, requestInfo){
			var events = [{
				event : event,
				value : eventData
			}];
			if(loc_out.prv_eventAdapter!=undefined){
				//if have eventAdapter, then apply eventAdapter to wrapper event
				events = loc_out.prv_eventAdapter(event, eventData, requestInfo);
			}
			
			_.each(events, function(eventInfo, index){
				loc_out.prv_dataOperationEventObject.triggerEvent(eventInfo.event, eventInfo.value, requestInfo);
			});
		});
	};

	var loc_addNormalChildVariable = function(childVar, path){
		loc_addChildVariable(loc_out.prv_childrenVariable, childVar, path);
		return {
			variable : childVar,
			path : path
		};
	};

	var loc_addChildVariableWithAdapter = function(childVar, path){
		var p = path+"_"+childVar.prv_id;
		loc_addChildVariable(loc_out.prv_extraChildrenVariable, childVar, p);
		return {
			variable : childVar,
			path : p
		};
	};
	
	var loc_addChildVariable = function(container, childVar, path){
		container[path] = childVar;
		childVar.registerDataOperationEventListener(loc_out.prv_dataOperationEventObject, function(event, eventData, request){
			if(event==node_CONSTANT.WRAPPER_EVENT_DELETE)	delete container[path];
		});
		childVar.registerLifecycleEventListener(loc_out.prv_lifecycleEventObject, function(event, eventData, request){
			if(event==node_CONSTANT.WRAPPER_EVENT_CLEARUP)	delete container[path];
		});
	};
	
	var loc_updateChildrenData = function(requestInfo){
		_.each(loc_out.prv_childrenVariable, function(childVariable, path){
			childVariable.prv_updateRelativeData(requestInfo);
		});
	};
	
	
	var loc_out = {
	
			prv_getWrapper : function(){return this.prv_wrapper;},

			//has to be base variable
			prv_getSetBaseDataRequest : function(parm1, parm2, handlers, requestInfo){	
				loc_destroyWrapper(requestInfo);
				
				//create empty wrapper fist
				var wrapperValue;      //store the value
				var entityType = node_getObjectType(parm1);
				if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_DATA){
					wrapperValue = parm1.value;
					parm1.value = undefined;
				}
				else if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_VALUE){
					wrapperValue = parm1;
					parm1 = undefined;
				}
				else{
					wrapperValue = parm1;
					parm1 = undefined;
				}
				var wrapper = node_wrapperFactory.createWrapper(parm1, parm2, requestInfo);
				loc_setWrapper(wrapper, requestInfo);
				//update children's wrapper according to base wrapper changes
				loc_updateChildrenData(requestInfo);
				//set new value
				return wrapper.getDataOperationRequest(node_uiDataOperationServiceUtility.createSetOperationService("", wrapperValue), handlers, requestInfo);
			},

			//update data when parent's wrapper changed
			prv_updateRelativeData : function(requestInfo){
				//create new wrapper based on wrapper in parent and path
				loc_destroyWrapper(requestInfo);
				loc_updateWrapperByParent(requestInfo);
				loc_updateChildrenData(requestInfo);
			},
			
			setValueAdapter : function(valueAdapter){  
				this.prv_valueAdapter = valueAdapter;
				if(this.prv_wrapper!=undefined)		this.prv_wrapper.setValueAdapter(valueAdapter);
			},
			
			setPathAdapter : function(pathAdapter){  
				this.prv_pathAdapter = pathAdapter;
				if(this.prv_wrapper!=undefined)		this.prv_wrapper.setPathAdapter(pathAdapter);
			},

			setEventAdapter : function(eventAdapter){	this.prv_eventAdapter = eventAdapter;	},
			
			
			use : function(){	
				loc_out.prv_usage++;
				return this;
			},
			
			release : function(requestInfo){
				loc_out.prv_usage--;
				if(loc_out.prv_usage<=0){
					node_getLifecycleInterface(loc_out).destroy(requestInfo);
				}
			},
			
			destroy : function(requestInfo){
				loc_out.prv_usage = 0;
				this.release(requestInfo);
			},

			//create child variable, if exist, then reuse it
			createChildVariable : function(path, adapterInfo){
				var out;
				if(adapterInfo==undefined){
					//normal child, try to reuse exsiting one
					var childVar = loc_out.prv_childrenVariable[path];
					if(childVar==undefined){
						childVar = node_createVariable(loc_out, path);
						out = loc_addNormalChildVariable(childVar, path);
					}
					else{
						out = {
							variable : childVar,
							path : path
						}
					}
				}
				else{
					//child with extra info
					out = loc_addChildVariableWithAdapter(node_createVariable(loc_out, path, adapterInfo), path);
				}

				return out;
			},

			/*
			 * register handler for operation event
			 */
			registerDataOperationEventListener : function(listenerEventObj, handler, thisContext){return this.prv_dataOperationEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);		},
			getDataOperationEventObject : function(){   return this.prv_dataOperationEventObject;   },
			
			/*
			 * register handler for event of communication between parent and child variables
			 */
			registerLifecycleEventListener : function(listenerEventObj, handler, thisContext){return this.prv_lifecycleEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);	},
			getLifecycleEventObject : function(){   return this.prv_lifecycleEventObject;   },
			
			getDataOperationRequest : function(operationService, handlers, requester_parent){
				var that = this;
				var out;
				
				if(operationService.command==node_CONSTANT.WRAPPER_OPERATION_SET && loc_out.prv_isBase==true){
					//for set root data
					return loc_out.prv_getSetBaseDataRequest(operationService.value, operationService.dataType, handlers, requestInfo);
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
						nosliw.logging.info("Parent: " , ((that.prv_relativeVariableInfo==undefined)?"":that.prv_relativeVariableInfo.parent.prv_id));
						nosliw.logging.info("ParentPath: " , ((that.prv_relativeVariableInfo==undefined)?"":that.prv_relativeVariableInfo.path)); 
						nosliw.logging.info("Request: " , JSON.stringify(operationService));
						nosliw.logging.info("Result: " , JSON.stringify(data));
						nosliw.logging.info("***************************************************************");
						return data;
					}
				});
				
				return out;
			},
	};
	
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE);
	loc_out = node_makeObjectWithId(loc_out, nosliw.generateId());
	
	node_getLifecycleInterface(loc_out).init(data1, data2, adapterInfo);
	return loc_out;
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


//Register Node by Name
packageObj.createChildNode("createVariable", node_createVariable); 

})(packageObj);
