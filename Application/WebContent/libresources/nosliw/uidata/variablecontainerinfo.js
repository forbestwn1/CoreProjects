//get/create package
var packageObj = library.getChildPackage("orderedcontainer");    

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
var node_createServiceRequestInfoSequence;
var node_uiDataOperationServiceUtility;
var node_createServiceRequestInfoSet;
var node_createVariableWrapper;
var node_requestServiceProcessor;
var node_wrapperFactory;
var node_dataUtility;

//*******************************************   Start Node Definition  ************************************** 	

var node_createHandleEachElementProcessor = function(baseVariable, path){
	//based variable and path
	var loc_baseVariable = baseVariable;
	var loc_path = path;
	
	//container variable to deal with element 
	var loc_containerVariable;

	//store child element order info
	var loc_orderChildrenInfo;

	//store element variable according to variable path
	var loc_elementsVariable;
	
	//event object
	var loc_eventObject = node_createEventObject();
	
	//add element to container
	//   index : position in container
	//   id : id of the element, optional
	var loc_addElement = function(index, id, requestInfo){
		//add element to order child info
		var eleInfo = loc_orderChildrenInfo.insertElement(index, id, requestInfo);
		//create child variable according to path provided by orderChildrenInfo
		var eleVariable = loc_containerVariable.createChildVariable(eleInfo.path);
		loc_elementsVariable[eleInfo.path] = eleVariable;
		//data operation event handle for child variable 
		eleVariable.registerDataOperationEventListener(loc_eventObject, function(event, eventData, request){
			if(event==node_CONSTANT.WRAPPER_EVENT_DELETE){
				loc_trigueEvent(node_CONSTANT.EACHELEMENTCONTAINER_EVENT_DELETEELEMENT, eleInfo.indexVariable);
				eleVariable.release();
				loc_orderChildrenInfo.deleteElement(eleInfo.path, request);
				delete loc_elementsVariable[eleInfo.path];
			}
		});
		return new node_OrderedContainerElementInfo(eleVariable, eleInfo.indexVariable);
	};
	
	var loc_getElements = function(){
		var out = [];
		_.each(loc_orderChildrenInfo.getElements(), function(ele, index){
			out.push(new node_OrderedContainerElementInfo(loc_elementsVariable[ele.path], ele.indexVariable));
		});
		return out;
	};
	
	var loc_getHandleEachElementOfOrderContainerRequest = function(elementHandler, handlers, request){
		//container looped
		//handle each element
		var i = 0;
		var handleElementsRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("HandleElements", {"elements":loc_orderChildrenInfo.getElements()}), handlers, request);
		_.each(loc_orderChildrenInfo.getElements(), function(ele, index){
			var elementHandlerResult = elementHandler.call(loc_out, loc_elementsVariable[ele.path], ele.indexVariable);
			//output of elementHandleRequestFactory method maybe request, maybe just object
			if(node_getObjectType(elementHandlerResult)==node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST){
				//add child request from factory
				//eleId as path
				handleElementsRequest.addRequest(i+"", elementHandlerResult);
				i++;
			}
		});
		return handleElementsRequest;
	};
	
	var loc_buildContainerVarWrapper = function(){
		loc_orderChildrenInfo = node_createContainerOrderInfo();
		loc_elementsVariable = {};
		
		//prepare adapter for container variable
		var adapterInfo = {
			pathAdapter : loc_orderChildrenInfo,
			eventAdapter : function(event, eventData, pathPosition, requestInfo){
				if(event==node_CONSTANT.WRAPPER_EVENT_CHANGE||(pathPosition==0 && event==node_CONSTANT.WRAPPER_EVENT_SET)){
					//when container value was changed
					//delete element data first
					loc_destroyContainerVariable();
					//trigue event
					loc_trigueEvent(node_CONSTANT.EACHELEMENTCONTAINER_EVENT_RESET, undefined, requestInfo);
					//no event to child
					return false;
				}
				return true;
			},
			destroyAdapter : function(){
//				loc_lifecycleEventObject.clearup();
//				loc_dataOperationEventObject.clearup();
//				loc_orderChildrenInfo.destroy();
			}
		};
		
		loc_containerVariable = loc_baseVariable.createChildVariable(loc_path, adapterInfo);
		
		loc_containerVariable.registerDataOperationEventListener(undefined, function(event, eventData, requestInfo){
			if(event==node_CONSTANT.WRAPPER_EVENT_ADDELEMENT){
				var newEventData = loc_addElement(eventData.index, eventData.id, requestInfo);
				loc_trigueEvent(node_CONSTANT.EACHELEMENTCONTAINER_EVENT_NEWELEMENT, newEventData, requestInfo);
			}
		}, this);
		return loc_containerVariable;
	};
	
	var loc_destroyContainerVariable = function(requestInfo){
		if(loc_orderChildrenInfo!=undefined){
			loc_orderChildrenInfo.destroy();
			loc_orderChildrenInfo = undefined;
		}
		if(loc_elementsVariable!=undefined){
			//release elements variable
			_.each(loc_elementsVariable, function(eleVar, path){
				eleVar.unregisterDataOperationEventListener(loc_eventObject);
				eleVar.release();
			});
			loc_elementsVariable = undefined;
		}
		if(loc_containerVariable!=undefined){
			//release container variable
			loc_containerVariable.unregisterDataOperationEventListener(loc_eventObject);
			loc_containerVariable.release();
			loc_containerVariable = undefined;
		}
	};
	
	var loc_trigueEvent = function(event, eventData, requestInfo){
		loc_eventObject.triggerEvent(event, eventData, requestInfo);
	};
	
	var loc_out = {
		
		getLoopRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence({}, handlers, request);
			if(loc_orderChildrenInfo==undefined){
				//if no loop request did before
				//build container related object (order child info, container variable)
				var containerVariable = loc_buildContainerVarWrapper();
				//get container value first
				out.addRequest(containerVariable.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(loc_path), {
					success : function(request, data){
						if(data==undefined)  return;
						
						//loop through element
						return node_wrapperFactory.getDataTypeHelper(data.dataTypeInfo).getGetElementsRequest(data.value, {
							success : function(request, valueElements){
								//create child variables
								_.each(valueElements, function(valueEle, index){
									loc_addElement(index, valueEle.id, request);
								});
								return loc_getElements(); 
//								return loc_getHandleEachElementOfOrderContainerRequest(elementHandler);
							}
						});
					}
				}));
			}
			else{
				//loop did before
				return node_createServiceRequestInfoSimple(undefined, function(){return loc_getElements();}, handlers, request);
//				out.addRequest(loc_getHandleEachElementOfOrderContainerRequest(elementHandler));
			}
			
			return out;
		},
		
		executeLoopRequest : function(elementHandler, handlers, request){
			var requestInfo = this.getLoopRequest(elementHandler, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		registerEventListener : function(listenerEventObj, handler, thisContext){
			loc_eventObject.registerListener(undefined, listenerEventObj, handler, thisContext);
		},
		
		//destroy 
		destroy : function(requestInfo){
			loc_eventObject.clearup();
			loc_destroyContainerVariable(requestInfo);
		},
	};
	
	return loc_out;
};



//element info expose to end user
//including two variable: element variabe, index variable
var node_OrderedContainerElementInfo = function(elementVar, indexVar){
	this.elementVar = elementVar;
	this.indexVar = indexVar;
	
	this.getElement = function(){
		return node_createVariableWrapper(this.elementVar);
	};
	
	this.getIndex = function(){
		return node_createVariableWrapper(this.indexVar);
	};
	
	return this;
};

var node_ContainerElementInfo = function(path, indexVar){
	//path for locating element variable from container aprent
	this.path = path;
	//index variable for position in container
	this.indexVariable = indexVar;
	return this;
};

//store order information of container
//	path : identify element variable from container variable
//	id : identify element data from container data
//	index : position of element in container data
//in order to identify elelment data from container data, try use id if provided, otherwise, use position
var node_createContainerOrderInfo = function(){
	
	var loc_generateId = function(){
		loc_out.prv_id++;
		return "id"+loc_out.prv_id+"";
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(){
		loc_out.prv_id = 0;

		//map from path to id, if not provided, then no id can use, just use index instead
		loc_out.prv_idByPath = {};
		//all elements
		loc_out.prv_elementsInfo = [];
	};

	var loc_createIndexVariable = function(path, requestInfo){
		var value = {
				path : path,
				eventObject : node_createEventObject(),
				
				getValue : function(){
					var index = loc_out.prv_getIndexByPath(this.path);
					return index;
				},	
					
				registerListener : function(eventObj, handler, thisContext){
					this.eventObject.registerListener(undefined, eventObj, handler, thisContext);
				},

				trigueEvent : function(requestInfo){
					this.eventObject.triggerEvent(node_CONSTANT.WRAPPER_EVENT_CHANGE, undefined, requestInfo);
				},
				
				destroy : function(){
					this.eventObject.clearup();
				},
			};
		return node_createVariableWrapper(node_dataUtility.createDataOfDynamic(value), undefined, undefined, requestInfo);
	};

	
	var loc_trigueIndexChange = function(startIndex, requestInfo){
		for(var i=startIndex; i<loc_out.prv_elementsInfo.length; i++){
			loc_out.prv_elementsInfo[i].indexVariable.executeDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(""), {
				success : function(request, data){
					data.value.trigueEvent(request);
				}
			}, requestInfo);
		}
	};
	
	var loc_out = {
			
		prv_getIndexByPath : function(path){
			var out;
			_.some(this.prv_elementsInfo, function(eleInfo, index){
				if(eleInfo.path==path){
					out = index;
					return true;
				}
			});
			return out;
		},	
			
		//index : position to insert element
		//id : something to identify element with data
		insertElement : function(index, id, requestInfo){
			//path is something that won't change
			//if id for element is provided, use id as path
			//otherwise, generate id as path
			var path = id;
			if(path==undefined)  path = loc_generateId();

			//no id provided for element, then no mapping added
			if(id!=undefined)  loc_out.prv_idByPath[path] = id;
			
			//generate element info
			var eleInfo = new node_ContainerElementInfo(path, loc_createIndexVariable(path, requestInfo));
			loc_out.prv_elementsInfo.splice(index, 0, eleInfo);
			
			//trigue index change event
			loc_trigueIndexChange(index+1, requestInfo);
			return eleInfo;
		},
		
		deleteElement : function(path, requestInfo){
			delete this.prv_idByPath[path];
			
			var index = this.prv_getIndexByPath(path);
			this.prv_elementsInfo.splice(index, 1);
			
			loc_trigueIndexChange(index, requestInfo);
		},
		
		getElements : function(){		return loc_out.prv_elementsInfo;	},

		//from variable path to data path
		toRealPath : function(path){
			//find first path seg
			var index = path.indexOf(".");
			var elePath = path;
			if(index!=-1)  elePath = path.substring(0, index);
			
			//convert first path seg from path to real path
			var realElePath = loc_out.prv_idByPath[elePath];       //has id for this path
			if(realElePath==undefined){
				//not provided, then use index as path, can be improved using index variable
				realElePath = loc_out.prv_getIndexByPath(elePath) + "";
			}
			
			//build full path again
			var out = realElePath;
			if(index!=-1){
				out = out + path.substring(index);
			}
			return out;
		},
		
		//from data path to variable path
		toAdapteredPath : function(path){
			//find first path seg
			var index = path.indexOf(".");
			var elePath = path;
			if(index!=-1)  elePath = path.substring(0, index);
			
			//convert first path seg from real path to adapted path
			var adapteredElePath;
			//whether data path is id, if so find path from idByPaht 
			_.each(loc_out.prv_idByPath, function(id, p){
				if(id==elePath) adapteredElePath = p;
			});
		
			if(adapteredElePath==undefined){
				//otherwise, data path is index
				adapteredElePath = loc_out.prv_elementsInfo[parseInt(elePath)].path;
			}
			
			//build full path again
			var out = adapteredElePath;
			if(index!=-1){
				out = out + path.substring(index);
			}
			return out;
		},
		
		destroy : function(){
			_.each(this.prv_elementsInfo, function(eleInfo, index){
				eleInfo.indexVariable.release();
			});
		},

	};
	
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE);
	loc_out = node_makeObjectWithId(loc_out, nosliw.generateId());
	
	node_getLifecycleInterface(loc_out).init();
	
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
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createVariableWrapper", function(){node_createVariableWrapper = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});

//Register Node by Name

packageObj.createChildNode("createHandleEachElementProcessor", node_createHandleEachElementProcessor); 

packageObj.createChildNode("OrderedContainerElementInfo", node_OrderedContainerElementInfo); 
packageObj.createChildNode("ContainerElementInfo", node_ContainerElementInfo); 
packageObj.createChildNode("createContainerOrderInfo", node_createContainerOrderInfo); 


})(packageObj);
