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
var node_createServiceRequestInfoSequence;
var node_uiDataOperationServiceUtility;

//*******************************************   Start Node Definition  ************************************** 	

var node_getHandleEachElementRequest = function(varWrapper, path, elementHandleRequestFactory, handlers, request){
	
	var loc_containerVarWrapper;
	var loc_containerVar;
	
	var loc_orderChildrenInfo;
	
	var loc_lifecycleEventObject = node_createEventObject();
	var loc_dataOperationEventObject = node_createEventObject();
	
	var loc_getHandleEachElementOfOrderContainer = function(elementHandleRequestFactory, handlers, request){
		//container looped
		//handle each element
		var i = 0;
		var handleElementsRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("HandleElements", {"elements":loc_orderChildrenInfo.getElements()}));
		_.each(loc_orderChildrenInfo.getElements(), function(ele, index){
			//add child request from factory
			//eleId as path
			handleElementsRequest.addRequest(i+"", elementHandleRequestFactory.call(loc_out, loc_containerVarWrapper, node_createVariableWrapper(loc_containerVar.prv_childrenVariable[ele.path]), node_createVariableWrapper(ele.indexVariable)));
			i++;
		});
		return handleElementsRequest;
	};
	
	
	
	//not loop yet, get value first, then loop it
	var out = node_createServiceRequestInfoSequence({}, handlers, request);
	out.addRequest(containerVar.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(), {
		success : function(request, data){
			//get current value
			containerVar.prv_wrapper.getDataTypeHelper().getGetElementsRequest(data.value, {
				success : function(request, valueElements){
					loc_orderChildrenInfo = node_createContainerOrderInfo();
					
					var adapterInfo = {
						pathAdapter : loc_orderChildrenInfo,
						eventAdapter : function(event, eventData, request){
							var events = [];
							events.push({
								event: event,
								value : eventData
							});
							
							if(event==node_CONSTANT.WRAPPER_EVENT_ADDELEMENT){
								var eleInfo = loc_orderChildrenInfo.insertElement(eventData.index, eventData.id);
								var newEleVarInfo = containerVar.createChildVariable(eleInfo.path);
								newEleVarInfo.variable.registerDataOperationEventListener(loc_dataOperationEventObject, function(event, eventData, request){
									if(event==node_CONSTANT.WRAPPER_EVENT_DELETE)	loc_out.prv_orderChildrenInfo.deleteElement(newEleVarInfo.path);
								});
								newEleVarInfo.variable.registerLifecycleEventListener(loc_lifecycleEventObject, function(event, eventData, request){
									if(event==node_CONSTANT.WRAPPER_EVENT_CLEARUP)	loc_out.prv_orderChildrenInfo.deleteElement(newEleVarInfo.path);
								});
								events.push({
									event : node_CONSTANT.WRAPPER_EVENT_NEWELEMENT,
									value : new node_OrderedContainerElementInfo(newEleVar, eleInfo.indexVariable),
								});
							}
							return events;
						},
						destroyAdapter : function(){
							loc_lifecycleEventObject.clearup();
							loc_dataOperationEventObject.clearup();
							loc_orderChildrenInfo.destroy();
						}
					};
					
					loc_containerVarWrapper = varWrapper.createChildVariable(path, adapterInfo);
					loc_containerVar = containerVarWrapper.getVariable();
					
					//loop through element
					//create child variables
					_.each(valueElements, function(valueEle, index){
						var eleInfo = loc_orderChildrenInfo.insertElement(index, valueEle.id);
						var childVarInfo = containerVar.createChildVariable(eleInfo.path);
						childVarInfo.variable.registerDataOperationEventListener(loc_dataOperationEventObject, function(event, eventData, request){
							if(event==node_CONSTANT.WRAPPER_EVENT_DELETE)	containerVar.prv_orderChildrenInfo.deleteElement(childVarInfo.path);
						});
						childVarInfo.variable.registerLifecycleEventListener(loc_lifecycleEventObject, function(event, eventData, request){
							if(event==node_CONSTANT.WRAPPER_EVENT_CLEARUP)	containerVar.prv_orderChildrenInfo.deleteElement(childVarInfo.path);
						});
					});
					return loc_getHandleEachElementOfOrderContainer(elementHandleRequestFactory, handlers, request);
				}
			});
		}
	}));
	return out;
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
	this.path = path;
	this.indexVariable = indexVar;
	return this;
};

//store order information of container
var node_createContainerOrderInfo = function(){
	
	var loc_generateId = function(){
		loc_out.prv_id++;
		return "id"+loc_out.prv_id+"";
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(){
		loc_out.prv_id = 0;

		loc_out.prv_idByPath = {};
		loc_out.prv_elementsInfo = [];
	};

	//create variable for index
	var loc_createIndexVariable = function(){
	};
	
	var loc_out = {
		insertElement : function(index, id){
			var path = id;
			if(path==undefined)  path = loc_generateId();
				
			if(id!=undefined)  loc_out.prv_idByPath[path] = id;
			
			var eleInfo = new node_ContainerElementInfo(path, loc_createIndexVariable());
			loc_out.prv_elementsInfo.splice(index, 0, eleInf);
			return eleInfo;
		},
			
		getElements : function(){		return loc_out.prv_elementsInfo;	},

		toRealPath : function(path){
			//find first path seg
			var index = path.indexOf(".");
			var elePath = path;
			if(index!=-1)  elePath = path.substring(0, index);
			
			//convert first path seg from path to real path
			var realElePath = loc_out.prv_idByPath[elePath];       //find from provided
			if(realElePath==undefined){
				//not provided, then use index as path
				_.each(loc_out.prv_elementsInfo, function(eleInfo, index){
					if(eleInfo.path==elePath){
						realElePath = index + "";
					}
				});
			}
			
			//build full path again
			var out = realElePath;
			if(index!=-1){
				out = out + path.substring(index);
			}
			return out;
		},
		
		toAdapteredPath : function(path){
			//find first path seg
			var index = path.indexOf(".");
			var elePath = path;
			if(index!=-1)  elePath = path.substring(0, index);
			
			//convert first path seg from real path to adapted path
			var adapteredElePath;
			_.each(loc_out.prv_idByPath, function(id, p){
				if(id==elePath) adapteredElePath = p;
			});
			if(adapteredElePath==undefined){
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
			
		},
		
		populateDeleteElementOperationData : function(operationData){
			//try to find the missing part id/index
			var index = operationData.index;
			var id = operationData.id;
			if(id==undefined){
				var path = this.prv_elementsInfo[index].path;
				id = this.prv_idByPath[path];
			}
			else if(index==undefined){
				var path;
				_.each(this.prv_idByPath, function(id1, path1){	if(id==id1)  path = path1;	});
				_.each(this.prv_elementsInfo, function(eleInfo, index1){  if(eleInfo.path==path1) index = index1; });
			}
			
			operationData.index = index;
			operationData.id = id;
		},
		
		populateDeleteElementOperationDataByPath : function(operationData, childPath){
			operationData.id = this.prv_idByPath[childPath];
			_.each(this.prv_elementsInfo, function(eleInfo, index){
				if(eleInfo==childPath)  operationData.index = index;
			});
		}
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

//Register Node by Name
packageObj.createChildNode("OrderedContainerElementInfo", node_OrderedContainerElementInfo); 
packageObj.createChildNode("node_createContainerOrderInfo", node_createContainerOrderInfo); 
packageObj.createChildNode("node_ContainerElementInfo", node_ContainerElementInfo); 

})(packageObj);