//get/create package
var packageObj = library.getChildPackage("wrapper");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_createEventObject;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_makeObjectWithType;
var node_getObjectType;
var node_makeObjectWithId;
var node_basicUtility;
var node_dataUtility;
var node_wrapperFactory;
var node_namingConvensionUtility;
var node_createServiceRequestInfoSequence;
var node_uiDataOperationServiceUtility;
var node_createServiceRequestInfoSimple;
	
//*******************************************   Start Node Definition  ************************************** 	
/**
 * 
 */
var node_createWraperCommon = function(parm1, path, typeHelper, dataType, request){
	//sync task name for remote call 
	var loc_moduleName = "wrapper";

	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(parm1, path, typeHelper, dataType, request){
		//helper object that depend on data type in wraper
		loc_out.pri_typeHelper = typeHelper;
		
		//what kind of data this wrapper represent
		loc_out.pri_dataType = dataType;
		
		//if true, this wrapper is based on root data, otherwise, this wrapper is based on parent wrapper, 
		loc_out.pri_dataBased = true;

		//root value used when data based
		loc_out.pri_rootValue = undefined;
		
		//parent wrapper used when wraper based
		loc_out.pri_parent = undefined;
		
		//the path from basis (data or wraper) to current data
		//path is valid for both data based or wrapper based
		loc_out.pri_path = path;
		
		//event and listener for data operation event
		loc_out.pri_dataOperationEventObject = node_createEventObject();

		//a list of wrapper operations that should be applied to wrapper
		//if this list is not empty, that means we only need to apply all operation, then get data
		//in this case, isValidData is true
		loc_out.pri_toBeDoneWrapperOperations = [];
		
		//whether the data need to calculated from parent
		loc_out.pri_isValidData = false;
		//calculated value, with this value, we don't need do operation from root value everytime
		loc_out.pri_value = undefined
		
		//whether this wrapper is a container
		loc_out.pri_isContainer = false;
		//keep track of all children elements, so that it can be removed by container 
		loc_out.pri_elements = {};
		
		
		//whether data based or wrapper based
		if(node_getObjectType(parm1)==node_CONSTANT.TYPEDOBJECT_TYPE_WRAPPER){
			//wrapper based
			loc_out.pri_parent = parm1;
			loc_out.pri_dataBased = false;
			loc_out.pri_dataType = loc_out.pri_parent.getDataType(); 
		}
		else{
			//data based
			loc_out.pri_rootValue = parm1;
			loc_out.pri_dataBased = true;
			loc_out.pri_path = undefined;
		}
		
		if(loc_out.pri_dataBased==false){
			//if parent based, then listen to parent's event
			loc_out.pri_parent.registerDataOperationListener(this.pri_dataOperationEventObject, function(event, eventData, requestInfo){

				if(event==node_CONSTANT.WRAPPER_EVENT_FORWARD){
					//for forward event, expand it
					event = eventData.event;
					eventData = eventData.value;
				}
				
				if(event==node_CONSTANT.WRAPPER_EVENT_CHANGE){
					//for change event from parent, just make data invalid & forward the event, 
					loc_invalidateData(requestInfo);
					loc_trigueDataOperationEvent(node_CONSTANT.WRAPPER_EVENT_CHANGE, {}, requestInfo);
				}
				else{
					var pathCompare = node_dataUtility.comparePath(loc_out.pri_path, eventData.path);
					if(pathCompare.compare == 0){
						//event happens on this wrapper, trigue the same
						//inform the change of wrapper
						if(event==node_CONSTANT.WRAPPER_EVENT_DESTROY){
							loc_out.destroy(requestInfo);
						}
						else if(event==node_CONSTANT.WRAPPER_EVENT_ADDELEMENT || event==node_CONSTANT.WRAPPER_EVENT_DELETEELEMENT){
							//store data operation event
							eventData = eventData.clone();
							eventData.path = "";
							loc_addToBeDoneDataOperation(eventData);
							//inform outside about change
							loc_trigueDataOperationEvent(event, eventData, requestInfo);
						}
						else if(event==node_CONSTANT.WRAPPER_EVENT_SET){
							eventData = eventData.clone();
							eventData.path = "";
							loc_setValue(eventData.value);
							loc_trigueDataOperationEvent(event, eventData, requestInfo);
						}
					}
					else if(pathCompare.compare == 1){
						//something happens in the middle between parent and this
						if(event==node_CONSTANT.WRAPPER_EVENT_DESTROY){
							loc_out.destroy(requestInfo);
						}
						else if(event==node_CONSTANT.WRAPPER_EVENT_SET){
							loc_invalidateData(requestInfo);
							loc_trigueDataOperationEvent(node_CONSTANT.WRAPPER_EVENT_CHANGE, {}, requestInfo);
						}
					}
					else if(pathCompare.compare == 2){
						//something happens beyond this, just forward the event with sub path, only set event
						//store the change
						loc_addToBeDoneDataOperation(eventData);
						eventData = eventData.clone();
						eventData.path = pathCompare.subPath;
						loc_triggerForwardEvent(event, eventData, requestInfo);
					}
					else{
						//not on right path, do nothing
					}
				}
			}, this);
		}
	};

	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		//for delete event, it means itself and all children should be destroy
		loc_invalidateData();
		//forward the event
		loc_trigueDataOperationEvent(node_CONSTANT.WRAPPER_EVENT_DESTROY, {}, requestInfo);
		//clear up event source
		loc_out.pri_dataOperationEventObject.clearup();
		
		loc_out.pri_parent = undefined;
		loc_out.pri_rootData = undefined;
		loc_out.pri_path = undefined;
	};
	
	//data operation request
	var loc_getDataOperationRequest = function(operationService, handlers, requester_parent){
		
		var command = operationService.command;
		var dataOperation = operationService.parms;
		var path = dataOperation.path;
		
		var out;

		if(loc_out.pri_dataBased==true){
			//root data
			if(command==node_CONSTANT.WRAPPER_OPERATION_GET){
				out = node_createServiceRequestInfoSimple(operationService, function(){return loc_getData();}, handlers, requester_parent);
			}
			else{
				out = loc_getDataOperationOnRootValue(operationService.clone(), handlers, requester_parent); 
			}
		}
		else{
			if(command==node_CONSTANT.WRAPPER_OPERATION_GET){
				if(loc_out.pri_isValidData==false){
					//calculate data
					out = node_createServiceRequestInfoSequence(operationService, handlers, requester_parent);
					//get parent data first
					var calParentDataRequest = loc_out.pri_parent.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(), {
						success : function(request, data){
							//calculate current value
							return loc_out.pri_typeHelper.getChildValueRequest(data.value, loc_out.pri_path, {
								success : function(requestInfo, value){
									//set local value
									loc_setValue(value);
									return loc_getData();
								}
							});
						}
					});
					out.addRequest(calParentDataRequest);
				}
				else{
					if(loc_out.pri_toBeDoneWrapperOperations.length>0){
						//calculate current value 
						out = node_createServiceRequestInfoSequence(operationService, {
							success : function(request, data){
								return loc_getData();
							}
						}, requester_parent);
						out.addRequest(loc_getProcessToBeDoneValueOperationRequest(0, loc_out.pri_value, {
							success : function(requestInfo, value){
								//set local value
								loc_setValue(value);
								return loc_getData();
							}
						}));
					}
					else{
						out = node_createServiceRequestInfoSimple(operationService, function(){return loc_getData();}, handlers, requester_parent);
					}
				}
			}
			else{
				//other operation
				out = loc_out.pri_getWrapperOperationRequest(operationService, handlers, requester_parent);
			}
		}
		return out;
	};
	
	var loc_getDataOperationOnRootValue = function(dataOperationService, handlers, request){
		var out = loc_out.pri_typeHelper.getDataOperationRequest(loc_out.pri_rootValue, dataOperationService.clone(), handlers, request);
		out.addPostProcessor({
			success : function(requestInfo, data){
				//trigue event
				loc_triggerEventByDataOperation(dataOperationService.command, dataOperationService.parms, requestInfo);
			}
		});
		return out;
	}
	
	var loc_getProcessToBeDoneValueOperationRequest = function(i, value, handlers, request){
		var out = loc_out.pri_typeHelper.getDataOperationRequest(value, loc_out.pri_toBeDoneWrapperOperations[i], {
			success : function(requestInfo, value){
				i++;
				if(i<loc_out.pri_toBeDoneWrapperOperations.length){
					return loc_getProcessToBeDoneValueOperationRequest(i, value);
				}
				else{
					return value;
				}
			}
		}, request);
		
		return out;
	};
	
	var loc_setValue = function(value){
		//make value invalid first
		loc_invalidateData();
		//then stroe value
		loc_out.pri_isValidData = true;
		loc_out.pri_value = value;
	
	}
	
	//add to be done operation
	//it only when data is valid
	//if data is not valid, data should be recalculated
	var loc_addToBeDoneDataOperation = function(dataOperation){
		if(loc_out.pri_isValidData==true){
			loc_out.pri_toBeDoneWrapperOperations.push(dataOperation);
		}
	};
	
	/*
	 * mark data as invalid so that it would be recalculated
	 */
	var loc_invalidateData = function(requestInfo){
		loc_out.pri_isValidData = false;
		loc_out.pri_value = undefined;
		loc_out.pri_toBeDoneWrapperOperations = [];

		//for container, destroy all elements
		if(loc_out.pri_isContainer==true){
			_.each(loc_out.pri_elements, function(ele, name){
				ele.destroy(requestInfo);
			});
			loc_out.pri_elements = {};
			loc_out.pri_isContainer = false;
		}
	};
	
	var loc_getData = function(){
		var value;
		if(loc_out.pri_dataBased==true)		value = loc_out.pri_rootValue;
		else	value = loc_out.pri_value;
		return loc_makeDataFromValue(value);
	}
	
	var loc_makeDataFromValue = function(value){    
		return node_dataUtility.createDataByObject(value, loc_out.pri_dataType);
	};

	var loc_trigueDataOperationEvent = function(event, eventData, requestInfo){
		loc_out.pri_dataOperationEventObject.triggerEvent(event, eventData, requestInfo);
	};
	
	var loc_triggerForwardEvent = function(event, eventData, requestInfo){
		var eData = {
				event : event, 
				value : eventData 
		};
		loc_trigueDataOperationEvent(node_CONSTANT.WRAPPER_EVENT_FORWARD, eData, requestInfo);
	};
	
	var loc_triggerEventByDataOperation = function(command, dataOperation, requestInfo){
		var event;
		var eventData = dataOperation;
		switch(command){
		case node_CONSTANT.WRAPPER_OPERATION_SET:
			event = node_CONSTANT.WRAPPER_EVENT_SET;
			break;
		case node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT:
			event = node_CONSTANT.WRAPPER_EVENT_ADDELEMENT;
			break;
		case node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT:
			event = node_CONSTANT.WRAPPER_EVENT_DESTROY;
			var path = node_dataUtility.combinePath(dataOperationService.parms.path, dataOperationService.parms.index);
			eventData = node_uiDataOperationServiceUtility.createDestroyOperationData(path); 
			break;
		case node_CONSTANT.WRAPPER_OPERATION_DESTROY:
			event = node_CONSTANT.WRAPPER_EVENT_DESTROY;
			break;
		}
		loc_trigueDataOperationEvent(event, eventData, requestInfo);
	};

	var loc_getOperationObject = function(obj){
		var opObject = obj;
		if(node_getObjectType(obj)==node_CONSTANT.TYPEDOBJECT_TYPE_DATA){
			if(obj.dataTypeInfo==node_CONSTANT.DATA_TYPE_OBJECT){
				//if operation data is object, then use value
				opObject = obj.value;
			}
		}
		return opObject;
	};

	
	var loc_out = {
			
			pri_getWrapperOperationRequest : function(dataOperationService, handlers, requester_parent){
				var out;
				if(loc_out.pri_dataBased==true){
					out = loc_getDataOperationOnRootValue(dataOperationService.clone(), handlers, requester_parent); 
				}
				else{
					var parentDataOperationService = dataOperationService.clone();
					parentDataOperationService.parms.path = node_dataUtility.combinePath(loc_out.pri_path, parentDataOperationService.parms.path);
					out = loc_out.pri_parent.pri_getWrapperOperationRequest(parentDataOperationService);
				}
				return out;
			},
			
			getDataType : function(){  return this.pri_dataType;   },
			
			getDataOperationRequest : function(operationService, handlers, requester_parent){
				return loc_getDataOperationRequest(operationService, handlers, requester_parent);
			},
			
			getHandleEachElementRequest : function(handler, thatContext, requestHanders, request){
				out = node_createServiceRequestInfoSequence(new node_ServiceInfo("HandleEachElement"), requestHanders, request);
				out.addRequest(loc_getDataOperationRequest(operationService));
				this.pri_typeHelper.handleEachElement( handler, thatContext);
			},

			destroy(requestInfo){node_getLifecycleInterface(loc_out).destroy(requestInfo);},
			
			/*
			 * handler : function (event, path, operationValue, requestInfo)
			 */
			registerDataOperationListener : function(listenerEventObj, handler, thisContext){
				this.pri_dataOperationEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);
			},

			unregisterDataOperationListener : function(listenerEventObj){
				this.pri_dataOperationEventObject.unregister(listenerEventObj);
			},
			
			createChildWrapper : function(path, request){		return node_wrapperFactory.createWrapper(this, path, this.pri_typeHelper, this.pri_dataType, request);		},
	};
	
	//append resource life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_WRAPPER);
	
	loc_out = node_makeObjectWithId(loc_out, nosliw.generateId());
	
	node_getLifecycleInterface(loc_out).init(parm1, path, typeHelper, dataType, request);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithid.makeObjectWithId", function(){node_makeObjectWithId = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});


//Register Node by Name
packageObj.createChildNode("createWraperCommon", node_createWraperCommon); 

})(packageObj);
