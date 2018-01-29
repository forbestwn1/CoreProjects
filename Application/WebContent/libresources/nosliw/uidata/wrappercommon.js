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
var node_RelativeEntityInfo;
	
//*******************************************   Start Node Definition  ************************************** 	
/**
 * 
 */
var node_createWraperCommon = function(parm1, path, typeHelper, dataType, request){

	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(parm1, path, typeHelper, dataType, request){
		//every wrapper has a id, it is for debuging purpose
		loc_out.prv_id = nosliw.runtime.getIdService().generateId();
		
		//helper object that depend on data type in wraper
		loc_out.prv_typeHelper = typeHelper;
		
		//what kind of data this wrapper represent
		loc_out.prv_dataType = dataType;
		
		//if true, this wrapper is based on root data, otherwise, this wrapper is based on parent wrapper, 
		loc_out.prv_dataBased = true;

		//for base wrapper, it is root value
		//for relative wrapper, it is temperory calculated value based on operation on value 
		loc_out.prv_value = undefined

		//information for relative wrapper : parent, path
		loc_out.prv_relativeWrapperInfo = undefined;
		
		//event and listener for data operation event
		loc_out.prv_dataOperationEventObject = node_createEventObject();
		loc_out.prv_lifecycleEventObject = node_createEventObject();

		//a list of wrapper operations that should be applied to wrapper
		//if this list is not empty, that means we only need to apply all operation, then get data
		//in this case, isValidData is true
		loc_out.prv_toBeDoneWrapperOperations = [];
		
		//whether the data need to calculated from parent
		loc_out.prv_isValidData = false;

		//adapter for converting value
		//with adapter, we can insert some converting job into this wrapper, 
		//this converting job can transform the wrapper value during read and set
		loc_out.prv_valueAdapter;
		
		//path adapter for child
		loc_out.prv_pathAdapter;
		
		//whether data based or wrapper based
		if(node_getObjectType(parm1)==node_CONSTANT.TYPEDOBJECT_TYPE_WRAPPER){
			//wrapper based
			loc_out.prv_dataBased = false;
			loc_out.prv_relativeWrapperInfo = new node_RelativeEntityInfo(parm1, path);
			loc_out.prv_dataType = loc_out.prv_relativeWrapperInfo.parent.getDataType(); 
		}
		else{
			//data based
			loc_out.prv_dataBased = true;
			loc_out.prv_value = parm1;
		}
		
		if(loc_out.prv_dataBased==false){
			//if parent based, then listen to parent's event
			loc_out.prv_relativeWrapperInfo.parent.registerLifecycleListener(this.prv_lifecycleEventObject, function(event, eventData, requestInfo){
				if(event==node_CONSTANT.WRAPPER_EVENT_CLEARUP){
					loc_out.destroy(requestInfo);
				}				
			});
			
			loc_out.prv_relativeWrapperInfo.parent.registerDataOperationListener(this.prv_dataOperationEventObject, function(event, eventData, requestInfo){

				if(event==node_CONSTANT.WRAPPER_EVENT_FORWARD){
					//for forward event, expand it
					event = eventData.event;
					eventData = eventData.value;
				}
				
				//clone event data so that we can modify it and resent it
				eventData = eventData.clone();
				if(event==node_CONSTANT.WRAPPER_EVENT_CHANGE){
					//for change event from parent, just make data invalid & forward the event, 
					loc_invalidateData(requestInfo);
					loc_trigueDataOperationEvent(node_CONSTANT.WRAPPER_EVENT_CHANGE, {}, requestInfo);
				}
				else{
					var pathCompare = node_dataUtility.comparePath(loc_out.prv_relativeWrapperInfo.path, eventData.path);
					if(pathCompare.compare == 0){
						//event happens on this wrapper, trigue the same
						//inform the change of wrapper
						eventData.path = "";
						if(event==node_CONSTANT.WRAPPER_EVENT_DELETE){
							loc_trigueDataOperationEvent(event, eventData, requestInfo);
							loc_destroy(requestInfo);
						}
						else if(event==node_CONSTANT.WRAPPER_EVENT_ADDELEMENT || event==node_CONSTANT.WRAPPER_EVENT_DELETEELEMENT){
							//store data operation event
							loc_addToBeDoneDataOperation(event, eventData);
							//inform outside about change
							loc_trigueDataOperationEvent(event, eventData, requestInfo);
						}
						else if(event==node_CONSTANT.WRAPPER_EVENT_SET){
							if(loc_out.prv_valueAdapter==undefined){
								loc_setValue(eventData.value);
								loc_trigueDataOperationEvent(event, eventData, requestInfo);
							}
							else{
								//apply adapter to value
								var r = loc_out.prv_valueAdapter.getInValueRequest(eventData.value, {
									success: function(request, value){
										loc_setValue(value);
										eventData.value = value;
										loc_trigueDataOperationEvent(event, eventData, request);
									}
								}, requestInfo);
								node_requestServiceProcessor.processRequest(r, true);
							}
						}
					}
					else if(pathCompare.compare == 1){
						//something happens in the middle between parent and this
						if(event==node_CONSTANT.WRAPPER_EVENT_DELETE){
							eventData.path = "";
							loc_trigueDataOperationEvent(event, eventData, requestInfo);
							loc_destroy(requestInfo);
						}
						else if(event==node_CONSTANT.WRAPPER_EVENT_SET){
							loc_invalidateData(requestInfo);
							loc_trigueDataOperationEvent(node_CONSTANT.WRAPPER_EVENT_CHANGE, {}, requestInfo);
						}
					}
					else if(pathCompare.compare == 2){
						//something happens beyond this, just forward the event with sub path, only set event
						//store the change
						eventData.path = loc_out.toAdapteredPath(pathCompare.subPath);
						loc_addToBeDoneDataOperation(event, eventData);
						var forwardEventData = eventData.clone();
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
		//clear up event source
		loc_out.prv_dataOperationEventObject.clearup();
		loc_out.prv_lifecycleEventObject.clearup();
		
		loc_out.prv_relativeWrapperInfo = undefined;
		loc_out.prv_value = undefined;
	};

	//get value of current wrapper request
	var loc_getGetValueRequest = function(handlers, requester_parent){
		var out;
		var operationService = new node_ServiceInfo("Internal_GetWrapperValue", {});
		if(loc_out.prv_dataBased==true){
			//root data
			out = node_createServiceRequestInfoSimple(operationService,	function(){	return loc_out.prv_value;  }, handlers, requester_parent);
		}
		else{
			if(loc_out.prv_relativeWrapperInfo.path.indexOf("a")!=-1){
				var kkkk = 5555;
				kkkk++;
			}
			
			
			if(loc_out.prv_isValidData==false){
				//calculate data
				out = node_createServiceRequestInfoSequence(operationService, handlers, requester_parent);
				//get parent data first
				var calParentDataRequest = loc_out.prv_relativeWrapperInfo.parent.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(), {
					success : function(request, parentData){
						//calculate current value from parent
						var childPath = loc_out.prv_relativeWrapperInfo.parent.toRealPath(loc_out.prv_relativeWrapperInfo.path); 

						return loc_out.prv_typeHelper.getChildValueRequest(parentData.value, childPath, {
							success : function(requestInfo, value){
								//set local value
								if(loc_out.prv_valueAdapter==undefined){
									loc_setValue(value);
									return value;
								}
								else{
									//apply adapter to value
									return loc_out.prv_valueAdapter.getInValueRequest(value, {
										success: function(request, value){
											loc_setValue(value);
											return value;
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
				if(loc_out.prv_toBeDoneWrapperOperations.length>0){
					//calculate current value 
					out = node_createServiceRequestInfoSequence(operationService, handlers, requester_parent);
					out.addRequest(loc_getProcessToBeDoneValueOperationRequest(0, loc_out.prv_value));
					
					out.setRequestProcessors({
						success : function(requestInfo, value){
							loc_out.prv_toBeDoneWrapperOperations.splice(0,loc_out.prv_toBeDoneWrapperOperations.length)
							loc_out.prv_value = value;
							return value;
						}
					});
				}
				else{
					out = node_createServiceRequestInfoSimple(operationService, function(){return loc_out.prv_value;}, handlers, requester_parent);
				}
			}
		}
		return out;
	};
	
	//modify value in 
	var loc_getModifyDataOperationOnRootValue = function(dataOperationService, handlers, request){
		var service = dataOperationService.clone();
		var out = loc_out.prv_typeHelper.getDataOperationRequest(loc_out.prv_value, service, handlers, request);
		out.addPostProcessor({
			success : function(requestInfo, data){
				//trigue event
				loc_triggerEventByDataOperation(dataOperationService.command, dataOperationService.parms, requestInfo);
			}
		});
		return out;
	};
	
	var loc_getProcessToBeDoneValueOperationRequest = function(i, value, handlers, request){
		var out = loc_out.prv_typeHelper.getDataOperationRequest(value, loc_out.prv_toBeDoneWrapperOperations[i], {
			success : function(requestInfo, value){
				i++;
				if(i<loc_out.prv_toBeDoneWrapperOperations.length){
					return loc_getProcessToBeDoneValueOperationRequest(i, value);
				}
				else{
					return value;
				}
			}
		}, request);
		
		return out;
	};
	
	//change value totally
	var loc_setValue = function(value){
		//make value invalid first
		loc_invalidateData();
		//then store value
		loc_out.prv_isValidData = true;
		loc_out.prv_value = value;
	
	};
	
	//add to be done operation
	//it only when data is valid
	//if data is not valid, data should be recalculated
	var loc_addToBeDoneDataOperation = function(event, eventData){
		if(loc_out.prv_isValidData==true){
			var command;
			switch(event){
			case node_CONSTANT.WRAPPER_EVENT_SET:
				command = node_CONSTANT.WRAPPER_OPERATION_SET;
				break;
			case node_CONSTANT.WRAPPER_EVENT_ADDELEMENT:
				command = node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT;
				break;
			case node_CONSTANT.WRAPPER_EVENT_DELETE:
				command = node_CONSTANT.WRAPPER_OPERATION_DELETE;
				break;
			}

			loc_out.prv_toBeDoneWrapperOperations.push(new node_ServiceInfo(command, eventData));
		}
	};
	
	/*
	 * mark data as invalid so that it would be recalculated
	 */
	var loc_invalidateData = function(requestInfo){
		loc_out.prv_isValidData = false;
		loc_out.prv_value = undefined;
		loc_out.prv_toBeDoneWrapperOperations = [];
	};
	
	var loc_getData = function(){
		var value;
		if(loc_out.prv_dataBased==true)		value = loc_out.prv_value;
		else	value = loc_out.prv_value;
		return loc_makeDataFromValue(value);
	};
	
	var loc_makeDataFromValue = function(value){    
		return node_dataUtility.createDataByObject(value, loc_out.prv_dataType);
	};

	var loc_trigueDataOperationEvent = function(event, eventData, requestInfo){		loc_out.prv_dataOperationEventObject.triggerEvent(event, eventData, requestInfo);	};
	var loc_trigueLifecycleEvent = function(event, eventData, requestInfo){		loc_out.prv_lifecycleEventObject.triggerEvent(event, eventData, requestInfo);	};
	
	var loc_triggerForwardEvent = function(event, eventData, requestInfo){
		var eData = {
				event : event, 
				value : eventData 
		};
		loc_trigueDataOperationEvent(node_CONSTANT.WRAPPER_EVENT_FORWARD, eData, requestInfo);
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
		loc_trigueDataOperationEvent(event, eventData, requestInfo);
	};

	var loc_destroy = function(requestInfo){
		node_getLifecycleInterface(loc_out).destroy(requestInfo);
	};
	
	var loc_out = {
			getDataOperationRequest : function(operationService, handlers, requester_parent){
				var that = this;
				
				var command = operationService.command;
				var operationData = operationService.parms;
				var out;
				
				if(command==node_CONSTANT.WRAPPER_OPERATION_GET){
					out = node_createServiceRequestInfoSequence(operationService, handlers, requester_parent);
					//get current value first
					out.addRequest(loc_getGetValueRequest({
						success : function(request, value){
							if(node_basicUtility.isStringEmpty(operationData.path))		return loc_makeDataFromValue(value);
							else
								//calculate value according to path
								return that.prv_typeHelper.getChildValueRequest(value, operationData.path, {
									success : function(requestInfo, value){
										return loc_makeDataFromValue(value);
									}
								});
						}
					}));
				}
				else{
					var opService = operationService.clone();
					
					if(this.prv_dataBased==true){
						//operate on root value
						out = loc_getModifyDataOperationOnRootValue(opService, handlers, requester_parent); 
					}
					else{
						//otherwise, convert to operation on parent, util reach root
						if(command==node_CONSTANT.WRAPPER_OPERATION_SET && this.prv_valueAdapter!=undefined){
							//apply adapter for SET command
							out = node_createServiceRequestInfoSequence({}, handlers, requester_parent);
							//apply adapter to value
							out.addRequest(this.prv_valueAdapter.getOutValueRequest(operationData.value, {
								success: function(request, value){
									opService.parms.path = that.prv_relativeWrapperInfo.parent.toRealPath(node_dataUtility.combinePath(that.prv_relativeWrapperInfo.path, opService.parms.path)) ;
									opService.parms.value = value;
									return that.prv_relativeWrapperInfo.parent.getDataOperationRequest(opService);
								}
							}));
						}
						else{
							opService.parms.path = this.prv_relativeWrapperInfo.parent.toRealPath(node_dataUtility.combinePath(this.prv_relativeWrapperInfo.path, opService.parms.path)) ;
							out = this.prv_relativeWrapperInfo.parent.getDataOperationRequest(opService, handlers, requester_parent);
						}
					}
				}
				
				//logging wrapper operation
				out.setRequestProcessors({
					success : function(requestInfo, data){
						nosliw.logging.info("************************  wrapper operation   ************************");
						nosliw.logging.info("ID: " + that.prv_id);
						nosliw.logging.info("Parent: " , ((that.prv_relativeWrapperInfo==undefined)?"":that.prv_relativeWrapperInfo.parent.prv_id));
						nosliw.logging.info("ParentPath: " , ((that.prv_relativeWrapperInfo==undefined)?"":that.prv_relativeWrapperInfo.path)); 
						nosliw.logging.info("Request: " , JSON.stringify(operationService));
						nosliw.logging.info("Result: " , JSON.stringify(data));
						nosliw.logging.info("***************************************************************");
						return data;
					}
				});
				return out;
			},

			setValueAdapter : function(valueAdapter){  this.prv_valueAdapter = valueAdapter;  },
			
			getDataType : function(){  return this.prv_dataType;   },
			getDataTypeHelper : function(){  return this.prv_typeHelper;   },
			
			destroy : function(requestInfo){
				//forward the event
				loc_trigueLifecycleEvent(node_CONSTANT.WRAPPER_EVENT_CLEARUP, {}, requestInfo);
				loc_destroy(requestInfo);
			},
			
			/*
			 * handler : function (event, path, operationValue, requestInfo)
			 */
			registerDataOperationListener : function(listenerEventObj, handler, thisContext){		this.prv_dataOperationEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);		},

			registerLifecycleListener : function(listenerEventObj, handler, thisContext){		this.prv_lifecycleEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);		},
			
			unregisterDataOperationListener : function(listenerEventObj){		this.prv_dataOperationEventObject.unregister(listenerEventObj);		},

			unregisterLifecycleListener : function(listenerEventObj){		this.prv_lifecycleEventObject.unregister(listenerEventObj);		},
			
			createChildWrapper : function(path, request){		return node_wrapperFactory.createWrapper(this, path, this.prv_typeHelper, this.prv_dataType, request);		},
			
			//path conversion using path adapter
			setPathAdapter : function(pathAdapter){  this.prv_pathAdapter = pathAdapter;  },
			toRealPath : function(path){	return loc_out.prv_pathAdapter!=undefined ? this.prv_pathAdapter.toRealPath(path) : path;	},
			toAdapteredPath : function(path){	return loc_out.prv_pathAdapter!=undefined ? this.prv_pathAdapter.toAdapteredPath(path) : path;		},
			
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
nosliw.registerSetNodeDataEvent("uidata.entity.RelativeEntityInfo", function(){node_RelativeEntityInfo = this.getData();});


//Register Node by Name
packageObj.createChildNode("createWraperCommon", node_createWraperCommon); 

})(packageObj);
