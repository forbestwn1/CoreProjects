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
var node_ServiceInfo;
var node_createServiceRequestInfoSet;
var node_requestServiceProcessor;
var node_createWrapperOrderedContainer;
	
//*******************************************   Start Node Definition  ************************************** 	
/**
 * 
 */
var node_createWraperCommon = function(parm1, path, typeHelper, dataType, request){
	//sync task name for remote call 
	var loc_moduleName = "wrapper";

	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(parm1, path, typeHelper, dataType, request){
		//every wrapper has a id, it is for debuging purpose
		loc_out.pri_id = nosliw.runtime.getIdService().generateId();
		
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
		//path is valid for wrapper based only
		loc_out.pri_path = path;
		
		//adapter for converting value
		//with adapter, we can insert some converting job into this wrapper, 
		//this converting job can transform the wrapper value during read and set
		loc_out.adapter;
		
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
		loc_out.pri_elements = node_createWrapperOrderedContainer();
		
		
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
					loc_trigueEvent(node_CONSTANT.WRAPPER_EVENT_CHANGE, {}, requestInfo);
				}
				else{
					var pathCompare = node_dataUtility.comparePath(loc_out.pri_path, eventData.path);
					if(pathCompare.compare == 0){
						//event happens on this wrapper, trigue the same
						//inform the change of wrapper
						if(event==node_CONSTANT.WRAPPER_EVENT_DESTROY){
							loc_out.destroy(requestInfo);
						}
						else if(event==node_CONSTANT.WRAPPER_EVENT_ADDELEMENT){
							//store data operation event
							eventData = eventData.clone();
							eventData.path = "";
							eventData.elePath = loc_out.pri_elements.insertValue(eventData.value, eventData.index);
							
							loc_addToBeDoneDataOperation(event, eventData);
							//inform outside about change
							loc_trigueEvent(event, eventData, requestInfo);
						}
						else if(event==node_CONSTANT.WRAPPER_EVENT_DELETEELEMENT){
							//store data operation event
							eventData = eventData.clone();
							eventData.path = "";
							eventData.elePath = loc_out.pri_elements.deleteValue(eventData.index)
							
							loc_addToBeDoneDataOperation(event, eventData);
							//inform outside about change
							loc_trigueEvent(event, eventData, requestInfo);
						}
						else if(event==node_CONSTANT.WRAPPER_EVENT_SET){
							eventData = eventData.clone();
							eventData.path = "";
							if(loc_out.pri_adapter==undefined){
								loc_setValue(eventData.value);
								loc_trigueEvent(event, eventData, requestInfo);
							}
							else{
								//apply adapter to value
								var r = loc_out.pri_adapter.getInValueRequest(eventData.value, {
									success: function(request, value){
										loc_setValue(value);
										eventData.value = value;
										loc_trigueEvent(event, eventData, request);
									}
								}, requestInfo);
								node_requestServiceProcessor.processRequest(r, true);
							}
						}
					}
					else if(pathCompare.compare == 1){
						//something happens in the middle between parent and this
						if(event==node_CONSTANT.WRAPPER_EVENT_DESTROY){
							loc_out.destroy(requestInfo);
						}
						else if(event==node_CONSTANT.WRAPPER_EVENT_SET){
							loc_invalidateData(requestInfo);
							loc_trigueEvent(node_CONSTANT.WRAPPER_EVENT_CHANGE, {}, requestInfo);
						}
					}
					else if(pathCompare.compare == 2){
						//something happens beyond this, just forward the event with sub path, only set event
						//store the change
						eventData.path = loc_getIdPath(pathCompare.subPath);
						loc_addToBeDoneDataOperation(event, eventData);
						var forwardEventData = eventData.clone();
//						forwardEventData.path = pathCompare.subPath;
						loc_triggerForwardEvent(event, forwardEventData, requestInfo);
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
		loc_trigueEvent(node_CONSTANT.WRAPPER_EVENT_DESTROY, {}, requestInfo);
		//clear up event source
		loc_out.pri_dataOperationEventObject.clearup();
		
		loc_out.pri_parent = undefined;
		loc_out.pri_rootData = undefined;
		loc_out.pri_path = undefined;
	};

	//get real path from parent, may do mapping
	var loc_getRealPath = function(){
		var out = loc_out.pri_path;
		if(loc_out.pri_parent.pri_isContainer){
			//for container, do mapping from pri_path to real path
			out = loc_out.pri_parent.pri_elements.getPathById(out);
		}
		return out;
	};
	
	//convert global path to local path
	var loc_getIdPath = function(path){
		var out = path;
		if(loc_out.pri_isContainer){
			//for container, do mapping from path to id path
			var index = path.indexOf(".");
			var elePath = path;
			if(index!=-1)  elePath = path.substring(0, index);
			out = loc_out.pri_elements.getIdByPath(elePath);
			if(index!=-1){
				out = out + path.substring(index);
			}
		}
		return out;
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
				out = node_createServiceRequestInfoSimple(operationService, 
					function(){
						return loc_getData();
					}, handlers, requester_parent);
			}
			else{
				var service = operationService.clone();
				out = loc_getDataOperationOnRootValue(service, handlers, requester_parent); 
			}
		}
		else{
			if(command==node_CONSTANT.WRAPPER_OPERATION_GET){
				
				if(loc_out.pri_isValidData==false){
					//calculate data
					out = node_createServiceRequestInfoSequence(operationService, handlers, requester_parent);
					//get parent data first
					var calParentDataRequest = loc_out.pri_parent.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(), {
						success : function(request, parentData){
							//calculate current value from parent
							var childPath = loc_getRealPath(); 

							return loc_out.pri_typeHelper.getChildValueRequest(parentData.value, childPath, {
								success : function(requestInfo, value){
									//set local value
									if(loc_out.pri_adapter==undefined){
										loc_setValue(value);
										return loc_getData();
									}
									else{
										//apply adapter to value
										return loc_out.pri_adapter.getInValueRequest(value, {
											success: function(request, value){
												loc_setValue(value);
												return loc_getData();
											}
										}, requestInfo);
									}
								}
							});
						}
					});
					out.addRequest(calParentDataRequest);
				}
				else{
					if(loc_out.pri_toBeDoneWrapperOperations.length>0){
						//calculate current value 
						out = node_createServiceRequestInfoSequence(operationService, handlers, requester_parent);
						out.addRequest(loc_getProcessToBeDoneValueOperationRequest(0, loc_out.pri_value));
						
						out.setRequestProcessors({
							success : function(requestInfo, value){
								loc_out.pri_value = value;
								return loc_getData();
							}
						});
					}
					else{
						out = node_createServiceRequestInfoSimple(operationService, function(){return loc_getData();}, handlers, requester_parent);
					}
				}
			}
			else{
				//other operation
				if(command==node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT){
					if(loc_out.pri_isContainer && dataOperation.index==undefined){
						var eleId = loc_out.pri_element.getIdByPath(dataOperation.elePath);
						dataOperation.index = loc_out.pri_element.getIndexById(eleId);
					}
				}
				
				out = loc_out.pri_getModifyOperationRequest(operationService, handlers, requester_parent);
			}
		}
		return out;
	};
	
	var loc_getDataOperationOnRootValue = function(dataOperationService, handlers, request){
		var service = dataOperationService.clone();
		var out = loc_out.pri_typeHelper.getDataOperationRequest(loc_out.pri_rootValue, service, handlers, request);
		out.addPostProcessor({
			success : function(requestInfo, data){
				//trigue event
				loc_triggerEventByDataOperation(dataOperationService.command, dataOperationService.parms, requestInfo);
			}
		});
		return out;
	};
	
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
	
	};
	
	//add to be done operation
	//it only when data is valid
	//if data is not valid, data should be recalculated
	var loc_addToBeDoneDataOperation = function(event, eventData){
		if(loc_out.pri_isValidData==true){
			var command;
			switch(event){
			case node_CONSTANT.WRAPPER_EVENT_SET:
				command = node_CONSTANT.WRAPPER_OPERATION_SET;
				break;
			case node_CONSTANT.WRAPPER_EVENT_ADDELEMENT:
				command = node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT;
				break;
			}

			loc_out.pri_toBeDoneWrapperOperations.push(new node_ServiceInfo(command, eventData));
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
			loc_out.pri_elements.clear();
			loc_out.pri_isContainer = false;
		}
	};
	
	var loc_getData = function(){
		var value;
		if(loc_out.pri_dataBased==true)		value = loc_out.pri_rootValue;
		else	value = loc_out.pri_value;
		return loc_makeDataFromValue(value);
	};
	
	var loc_makeDataFromValue = function(value){    
		return node_dataUtility.createDataByObject(value, loc_out.pri_dataType);
	};

	var loc_trigueEvent = function(event, eventData, requestInfo){
		loc_out.pri_dataOperationEventObject.triggerEvent(event, eventData, requestInfo);
	};
	
	var loc_triggerForwardEvent = function(event, eventData, requestInfo){
		var eData = {
				event : event, 
				value : eventData 
		};
		loc_trigueEvent(node_CONSTANT.WRAPPER_EVENT_FORWARD, eData, requestInfo);
	};
	
	var loc_triggerEventByDataOperation = function(command, dataOperationParms, requestInfo){
		var event;
		var eventData = dataOperationParms.clone();
		switch(command){
		case node_CONSTANT.WRAPPER_OPERATION_SET:
			event = node_CONSTANT.WRAPPER_EVENT_SET;
			break;
		case node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT:
			event = node_CONSTANT.WRAPPER_EVENT_ADDELEMENT;
			break;
		case node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT:
			event = node_CONSTANT.WRAPPER_EVENT_DESTROY;
			var path = node_dataUtility.combinePath(dataOperationParms.path, dataOperationParms.index);
			eventData = node_uiDataOperationServiceUtility.createDestroyOperationData(path); 
			break;
		case node_CONSTANT.WRAPPER_OPERATION_DESTROY:
			event = node_CONSTANT.WRAPPER_EVENT_DESTROY;
			break;
		}
		loc_trigueEvent(event, eventData, requestInfo);
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
			//data operation for modify : set, addelement, deleteelement
			pri_getModifyOperationRequest : function(dataOperationService, handlers, requester_parent){
				var out;
				if(this.pri_dataBased==true){
					out = loc_getDataOperationOnRootValue(dataOperationService.clone(), handlers, requester_parent); 
				}
				else{
					if(dataOperationService.command==node_CONSTANT.WRAPPER_OPERATION_SET && this.pri_adapter!=undefined){
						//apply adapter for SET command
						out = node_createServiceRequestInfoSequence({}, handlers, requester_parent);
						//apply adapter to value
						var that  = this;
						out.addRequest(this.pri_adapter.getOutValueRequest(dataOperationService.parms.value, {
							success: function(request, value){
								var parentDataOperationService = dataOperationService.clone();
								parentDataOperationService.parms.path = node_dataUtility.combinePath(loc_getRealPath(), parentDataOperationService.parms.path);
								parentDataOperationService.parms.value = value;
								return that.pri_parent.pri_getModifyOperationRequest(parentDataOperationService);
							}
						}));
					}
					else{
						var parentDataOperationService = dataOperationService.clone();
						parentDataOperationService.parms.path = node_dataUtility.combinePath(loc_getRealPath(), parentDataOperationService.parms.path);
						out = this.pri_parent.pri_getModifyOperationRequest(parentDataOperationService, handlers, requester_parent);
					}
				}
				return out;
			},

			setAdapter : function(adapter){  this.pri_adapter = adapter;  },
			
			getDataType : function(){  return this.pri_dataType;   },
			
			getDataOperationRequest : function(operationService, handlers, requester_parent){
				var that = this;
				var out = loc_getDataOperationRequest(operationService, handlers, requester_parent);
				//logging wrapper operation
				out.setRequestProcessors({
					success : function(requestInfo, data){
						nosliw.logging.info("************************  wrapper operation   ************************");
						nosliw.logging.info("ID: " + that.pri_id);
						nosliw.logging.info("Parent: " , ((that.pri_parent==undefined)?"":that.pri_parent.pri_id));
						nosliw.logging.info("ParentPath: " , that.pri_path);
						nosliw.logging.info("Request: " , JSON.stringify(operationService));
						nosliw.logging.info("Result: " , JSON.stringify(data));
						nosliw.logging.info("***************************************************************");
						return data;
					}
				});
				return out;
			},
			
			getHandleEachElementRequest : function(elementHandleRequestFactory, handlers, request){
				var that  = this;
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("HandleEachElement"), handlers, request);
				//get current value first
				out.addRequest(loc_getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(""), {
					success : function(request, data){
						that.pri_isContainer = true;
						//get all elements
						return loc_out.pri_typeHelper.getGetElementsRequest(data.value, {
							success : function(request, elements){
								//handle each element
								var handleElementsRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("HandleElements", {"elements":elements}));
								_.each(elements, function(element, index){
									//add to container
									var eleId = loc_out.pri_elements.insertValue(element.value, index, element.path, element.id);
									//add child request from factory
									//eleId as path
									handleElementsRequest.addRequest(eleId, elementHandleRequestFactory.call(this, loc_makeDataFromValue(element.value), eleId+""));
								});
								return handleElementsRequest;
							}
						});
					}
				}));
				return out;
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
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.createWrapperOrderedContainer", function(){node_createWrapperOrderedContainer = this.getData();});

//Register Node by Name
packageObj.createChildNode("createWraperCommon", node_createWraperCommon); 

})(packageObj);
