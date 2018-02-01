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
//element info expose to end user
//including two variable: element variabe, index variable
var node_OrderedContainerElementInfo = function(elementVarWrapper, indexVarWrapper){
	this.elementVarWrapper = elementVarWrapper;
	this.indexVarWrapper = indexVarWrapper;
	return this;
};

var node_createVariableWrapper = function(variable){
	var loc_variable = variable;
	
	var loc_out = {
		get : function(){
			loc_variable.use();
			return loc_variable;
		}
	};
	return loc_out;
};

var node_createOrderedContainersInfo = function(baseVariable){
	
	var loc_baseVariable = baseVariable;
	
	var loc_typeHelper;
	
	var loc_containerByPath = {};
	
	var loc_getContainerByPathRequest = function(path, handlers, request){
		var out;
		var container = loc_containerByPath[path];
		if(container!=undefined){
			out = node_createServiceRequestInfoSimple(operationService, function(){return container;}, handlers, request);
		}
		else{
			out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getContainerByPath"), handlers, request);
			//get current value first
			out.addRequest(baseVariable.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(path), {
				success : function(request, data){
					//get all elements
					return loc_typeHelper.getGetElementsRequest(data.value, {
						success : function(request, valueElements){
							var containerInfo = node_createOrderVariableContainer(baseVariable, path, valueElements);
							loc_containerByPath[path] = containerInfo;
							return containerInfo;
						}
					});
				}
			}));
		}
		return out;
	};
	
	var loc_out = {
			
		setDataTypeHelper : function(typeHelper){ loc_typeHelper = typeHelper;  },
			
		getContainerInfoByPath : function(path){
			return loc_containerByPath[path];
		},	
		
		getContainerElementsByPathRequest : function(path, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getContainerElementsByPath"), handlers, request);
			out.addRequest(loc_getContainerByPathRequest(path, {
				success : function(request, ordedContainer){
					return ordedContainer;
				}
			}));
			return out;
		},
			
		clearup : function(){
			
		},
			
	};
	
	return loc_out;
};	



var node_createOrderVariableContainer = function(variable, path, valueElements){
	
	var loc_generateId = function(){
		loc_out.prv_id++;
		return "id"+loc_out.prv_id+"";
	};
	
	var loc_insertValue = function(value, index, id){
		var path = id;
		if(path==undefined)  path = loc_generateId();
		
		var eleVariable = loc_out.prv_containerVariable.createChildVariable(path);
		loc_out.prv_elementVarByPath[path] = eleVariable;  
		loc_out.prv_paths.splice(index, 0, path);
		
		if(id!=undefined)  loc_out.prv_idByPath[path] = id;
		
//		eleVariable.registerDataChangeEventListenen(undefined, function(event, eventData, request){
//			if(event==node_CONSTANT.WRAPPER_EVENT_DELETE){
//				//when element was deleted
//				
//			}
//		});
		
		return eleVariable;
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(variable, path, valueElements){

		loc_out.prv_id = 0;
		
		loc_out.prv_elementVarByPath = {};
		loc_out.prv_idByPath = {};
		loc_out.prv_paths = [];
		
		//get variable as container
		loc_out.prv_containerVariable = variable;
		if(!node_basicUtility.isStringEmpty(path)){
			loc_out.prv_containerVariable = variable.createChildVariable(path);
		}

		//create element variables and add to list
		_.each(valueElements, function(valueEle, index){
			loc_insertValue(valueEle.value, index, valueEle.id);
		});

		//event adapter
		loc_out.prv_containerVariable.setEventAdapter(function(event, eventData, request){
			var events = [];
			events.push({
				event: event,
				value : eventData
			});
			
			if(event==node_CONSTANT.WRAPPER_EVENT_ADDELEMENT){
				var eleVar = loc_insertValue(eventData.value, eventData.index, eventData.id);
				events.push({
					event : node_CONSTANT.WRAPPER_EVENT_NEWELEMENT,
					value : new node_OrderedContainerElementInfo(node_createVariableWrapper(eleVar), undefined),
				});
			}
			return events;
		});
		
		//path adapter
		loc_out.prv_containerVariable.setPathAdapter({
			toRealPath : function(path){
				//find first path seg
				var index = path.indexOf(".");
				var elePath = path;
				if(index!=-1)  elePath = path.substring(0, index);
				
				//convert first path seg from path to real path
				var realElePath = loc_out.prv_idByPath[elePath];       //find from provided
				if(realElePath==undefined)		realElePath = loc_out.prv_paths.indexOf(elePath)+"";   //not provided, then use index as path
				
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
					adapteredElePath = loc_out.prv_paths[parseInt(elePath)];
				}
				
				//build full path again
				var out = adapteredElePath;
				if(index!=-1){
					out = out + path.substring(index);
				}
				return out;
			},
			
		});
		
	};
	
	var loc_out = {
		getElements : function(){
			var that = this;
			var out = [];
			_.each(this.prv_paths, function(path, index){
				var eleVar = that.prv_elementVarByPath[path];
				out.push(new node_OrderedContainerElementInfo(node_createVariableWrapper(eleVar)));
			});
			return out;	
		},
		
		getContainer : function(){
			return this.prv_containerVariable;
		},
		
		clearup : function(){
			
		},
		
		populateDeleteElementOperationData : function(operationData){
			//try to find the missing part id/index
			var index = operationData.index;
			var id = operationData.id;
			if(id==undefined){
				var path = this.prv_paths[index];
				id = this.prv_idByPath[path];
			}
			else if(index==undefined){
				var path;
				_.each(this.prv_idByPath, function(id1, path1){	if(id==id1)  path = path1;	});
				_.each(this.prv_paths, function(path1, index1){  if(path==path1) index = index1; });
			}
			
			operationData.index = index;
			operationData.id = id;
		},
		
		populateDeleteElementOperationDataByPath : function(operationData, childPath){
			operationData.id = this.prv_idByPath[childPath];
			_.each(this.prv_paths, function(path, index){
				if(path==childPath)  operationData.index = index;
			});
		}
	};
	
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE);
	loc_out = node_makeObjectWithId(loc_out, nosliw.generateId());
	
	node_getLifecycleInterface(loc_out).init(variable, path, valueElements);
	
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
packageObj.createChildNode("createVariableWrapper", node_createVariableWrapper); 
packageObj.createChildNode("createOrderedContainersInfo", node_createOrderedContainersInfo); 
packageObj.createChildNode("createOrderVariableContainer", node_createOrderVariableContainer); 

})(packageObj);
