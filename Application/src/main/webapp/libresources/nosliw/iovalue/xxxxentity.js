//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_CONSTANT;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSet;
	var node_dataIOUtility;
	var node_createEventObject;
	var node_destroyUtil;
	var node_objectOperationUtility;
//*******************************************   Start Node Definition  ************************************** 	

//task result 
//  resultName : name of the result
//  result: result value map (value name / value)
var node_IOTaskResult = function(resultName, resultValue){
	this.resultName = resultName;
	this.resultValue = resultValue; 
};

var node_IOTaskInfo = function(taskRequestFun, taskName, inIO, outIO){
	this.taskRequestFun = taskRequestFun;
	this.taskName = taskName;
	this.inIO = inIO;
	this.outIO = outIO;
};


//dynamic io data which read and write through function
//dynamic io data inform the data change through listen to eventObj
var node_createDynamicIOData = function(callBackFuns, eventObj, thisContext){
	var loc_callBackFuns = callBackFuns;
	var loc_eventObj = eventObj;
	var loc_thisContext = thisContext;
	
	var loc_out = {
		getDataOperationRequest : function(dataOperationService, handlers, request){     return loc_callBackFuns.getDataOperationRequest.call(loc_thisContext, dataOperationService, handlers, request);      },
		getDataValueRequest : function(handlers, request){    return loc_callBackFuns.getDataValueRequest.call(handlers, request);      },
		
		
		registerEventListener : function(listener, handler, thisContext){   if(loc_eventObj!=undefined) loc_eventObj.registerListener(undefined, listener, handler, thisContext);   },
		unregisterEventListener : function(listener){    if(loc_eventObj!=undefined) loc_eventObj.unregister(listener);   },
	};
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_DYNAMICIODATA);
	return loc_out;
};


//dynamic io data which read and write through function
//dynamic io data inform the data change through listen to eventObj
var node_createDynamicIOData1 = function(getValueRequestFun, setValueRequestFun, dataOperationRequestFun, eventObj){
	var loc_getValueRequestFun = getValueRequestFun;
	var loc_setValueRequestFun = setValueRequestFun;
	var loc_dataOperationRequestFun = dataOperationRequestFun;
	var loc_eventObj = eventObj;
	
	var loc_out = {
		getGetValueRequest : function(handlers, request){		return loc_getValueRequestFun(handlers, request);		},
		getSetValueRequest : function(value, handlers, request){		return loc_setValueRequestFun(value, handlers, request);	},
		
		getDataOperationRequest : function(dataOperationService, handlers, request){     return loc_dataOperationRequestFun(dataOperationService, handlers, request);      },
		
		registerEventListener : function(listener, handler, thisContext){   if(loc_eventObj!=undefined) loc_eventObj.registerListener(undefined, listener, handler, thisContext);   },
		unregisterEventListener : function(listener){    if(loc_eventObj!=undefined) loc_eventObj.unregister(listener);   },
	};
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_DYNAMICIODATA);
	return loc_out;
};

//a set of data by name, if no name provided, then use "default" as name
//this set of data is used as io end point (input or output)
//each data can be static which is an object or dynamic which has read and write function
//any data change will trigue event 
var node_createIODataSet = function(value){
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();
	
	if(value!=undefined&&node_getObjectType(value)==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_IODATASET){
		return value;
	}

	var loc_init = function(value){
		//value is default value
		if(value!=undefined) loc_out.prv_dataSet[node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT] = value;
	};
	
	var loc_trigueEvent = function(eventName, eventData, requestInfo){loc_eventSource.triggerEvent(eventName, eventData, requestInfo); };

	var loc_processName = function(name){  return name!=undefined?  name : node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT;  };

	//directly get data element
	var loc_getDataElement = function(name){
		name = loc_processName(name);
		var out = loc_out.prv_dataSet[name];
		if(out==undefined){
			out = {};
			loc_out.prv_dataSet[name] = out;
		}
		return out;
	};


	
	var loc_out = {
		
		prv_dataSet : {},
		prv_id : nosliw.generateId(),
		

		//directly set data element
		setData : function(name, data, request){ 
			name = loc_processName(name);
			loc_out.prv_dataSet[name] = data;   
			var dataEleType = node_getObjectType(data);
			if(dataEleType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_DYNAMICIODATA){
				//for dynamic iodata, listen to value change
				data.registerEventListener(loc_eventListener, function(eventName, eventData, request){
					if(eventName==node_CONSTANT.IODATASET_EVENT_CHANGE)  loc_trigueEvent(eventName, undefined, request);
				});
			}
			loc_trigueEvent(node_CONSTANT.IODATASET_EVENT_CHANGE, undefined, request);
		},

		getDataOperationRequest : function(name, dataOperationService, handlers, request){
			var dataEle = loc_getDataElement(name);
			var dataEleType = node_getObjectType(dataEle);
			if(dataEleType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_DYNAMICIODATA){
				return dataEle.getDataOperationRequest(dataOperationService, handlers, request);
			}
			else{
				return node_createServiceRequestInfoSimple(undefined, function(request){
					return node_objectOperationUtility.objectOperation(dataEle, dataOperationService);
				}, handlers, request);
			}
		},
		
		getDataValueRequest : function(name, handlers, request){
			name = loc_processName(name);

			var dataEle = loc_getDataElement(name);
			var dataEleType = node_getObjectType(dataEle);
			if(dataEleType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_DYNAMICIODATA){
				return dataEle.getDataValueRequest(handlers, request);
			}
			else{
				return node_createServiceRequestInfoSimple(undefined, function(request){
					return dataEle;
				}, handlers, request);
			}
		},
		
		
		
		
		
		
		//get value of data request
		getGetDataValueRequest : function(name, handlers, request){
			var dataEle = loc_getDataElement(name);
			var dataEleType = node_getObjectType(dataEle);
			if(dataEleType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_DYNAMICIODATA){
				return dataEle.getGetValueRequest(handlers, request);
			}
			else{
				return node_createServiceRequestInfoSimple(undefined, function(request){
					return dataEle;
				}, handlers, request);
			}
		},






		getDataSet : function(){   return loc_out.prv_dataSet;   },

		//set value of data request
		getSetDataValueRequest : function(name, value, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			var dataEle = loc_getDataElement(name);
			var dataEleType = node_getObjectType(dataEle);
			if(dataEleType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_DYNAMICIODATA){
				return loc_getDataElement(name).getSetValueRequest(value, {
					success : function(request, value){
						loc_trigueEvent(node_CONSTANT.IODATASET_EVENT_CHANGE, undefined, request);
						return value;
					}
				});
			}
			else{
				out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
					loc_out.setData(name, value, request);
					return value;
				}));
			}
			return out;
		},
		
		//get set of value request
		getGetDataSetValueRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			var getDataItemRequest = node_createServiceRequestInfoSet(undefined, {
				success : function(request, resultSet){
					var dataSetValue = {};
					_.each(resultSet.getResults(), function(value, name){
						dataSetValue[name] = value;
					});
					return dataSetValue;
				}
			});
			
			_.each(loc_out.prv_dataSet, function(dataSetEle, name){
				getDataItemRequest.addRequest(name, loc_out.getGetDataValueRequest(name));
			});
			out.addRequest(getDataItemRequest);
			return out;
		},

		//generate new io data from data element 
		//this new io data can be used to build another io data set
		generateIOData : function(name){
			name = loc_processName(name);
			return node_createDynamicIOData(
				function(handlers, request){
					return loc_out.getGetDataValueRequest(name, handlers, request);
				},
				function(value, handlers, request){
					return loc_out.getSetDataValueRequest(name, value, handlers, request);
				}
			);
		},
		
		//merge ioData with value
		getMergeDataValueRequest : function(name, value, isDataFlat, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			var dataEle = loc_getDataElement(name);
			var dataEleType = node_getObjectType(dataEle);
			if(dataEleType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_DYNAMICIODATA){
				//get value first
				out.addRequest(loc_out.getGetDataValueRequest(name, {
					success : function(request, value){
						//do merge
						var mergedValue = node_dataIOUtility.mergeContext(request.getData('value'), value, isDataFlat);
						//then set value back
						return loc_getDataElement(request.getData('name')).getSetValueRequest(mergedValue, {
							success : function(request, value){
								loc_trigueEvent(node_CONSTANT.IODATASET_EVENT_CHANGE, undefined, request);
								return value;
							}
						});
					}
				}).withData(name, 'name').withData(value, 'value'));
			}
			else{
				out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
					var mergedValue = node_dataIOUtility.mergeContext(request.getData('value'), loc_getDataElement(request.getData('name')), isDataFlat);
					loc_trigueEvent(node_CONSTANT.IODATASET_EVENT_CHANGE, undefined, request);
					return mergedValue;
				}).withData(name, 'name').withData(value, 'value'));
			}
			return out;
		},
		
		registerEventListener : function(listener, handler, thisContext){  return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },
		
		destroy : function(request){
			_.each(loc_out.prv_dataSet, function(data, name){
				var dataEleType = node_getObjectType(data);
				if(dataEleType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_DYNAMICIODATA){
					data.unregisterEventListener(loc_eventListener);
				}
				node_destroyUtil(data, request);
			});
			loc_eventSource.clearup();
			loc_eventListener.clearup();
			loc_out.prv_dataSet = undefined;
		}
	};
	
	loc_init(value);
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_IODATASET);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.dataIOUtility", function(){node_dataIOUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.destroyUtil", function(){node_destroyUtil = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("xxxxIOTaskResult", node_IOTaskResult); 
packageObj.createChildNode("xxxxcreateIODataSet", node_createIODataSet); 
packageObj.createChildNode("xxxxcreateDynamicData", node_createDynamicIOData); 
packageObj.createChildNode("xxxxIOTaskInfo", node_IOTaskInfo); 

})(packageObj);
