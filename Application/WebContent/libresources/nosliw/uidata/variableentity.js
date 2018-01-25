//entity to describe relative variable : parent + path to parent
var node_RelativeVariableInfo = function(parent, path){
	this.parent = parent;
	this.path = node_basicUtility.emptyStringIfUndefined(path);
	return this;
};

//element info expose to end user
//including two variable: element variabe, index variable
var node_SortedContainerElementInfo = function(elementVar, indexVar){
	this.element = elementVar;
	this.index = indexVar;
};

var node_createSortedContainersInfo = function(baseVaraible, typeHelper){
	
	var loc_baseVariable = baseVariable;
	
	var loc_typeHelper = typeHelper;
	
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
			out.addRequest(loc_getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(""), {
				success : function(request, data){
					//get all elements
					return loc_out.pri_typeHelper.getGetElementsRequest(data.value, {
						success : function(request, valueElements){
							//handle each element
							var handleElementsRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("HandleElements", {"elements":valueElements}));
							container = node_createSortedContainer(loc_baseVariable, path, valueElements);
							loc_containerByPath[path] = container;
						}
					});
				}
			}));
		}
		return out;
	};
	
	var loc_out = {
			
		getContainerElementsByPathRequest : function(path, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getContainerElementsByPath"), handlers, request);
			out.addRequest(loc_getContainerByPathRequest(path, {
				success : function(request, sortedContainer){
					var elementsOut = [];
					var elements = sortedContainer.getElements();
					_.each(elements, function(element, index){
						elementsOut.push(new node_SortedContainerElementInfo(element.createChildVariable(), index));
					});
					return elementsOut;
				}
			}));
			return out;
		},
			
		clearup : function(){
			
		},
			
	};
	
	return loc_out;
};	

var node_createSortedContainer = function(baseVariable, path, valueElements, typeHelper){
	
	//container wrapper
	var loc_containerVariable = baseVariable.createChildVariable(path);
	
	loc_containerVariable.registerDataOperationListener(loc_out.prv_dataOperationEventObject, function(event, eventData, requestInfo){
		
		//change, 
		
		//ignore forward event
		//we should not ignore forward event, as forward event also indicate that something get changed on child, in that case, the data also get changed
		//inform the operation
		loc_out.prv_dataOperationEventObject.triggerEvent(event, eventData, requestInfo);
	});

	
	//convert global path to local path
	var loc_getIdPath = function(path){
		var out = path;
		if(loc_out.pri_pathAdapter!=undefined){
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
	
	//get real path from parent, may do mapping from local path to real path
	var loc_getRealPath = function(){
		var out = loc_out.pri_path;
		if(loc_out.pri_parent.pri_isContainer && !node_basicUtility.isStringEmpty(out)){
			//for container, do mapping from pri_path to real path
			out = loc_out.pri_parent.pri_elements.getPathById(out);
		}
		return out;
	};
	

	
	//elements variable
	var loc_elementsContainer = node_createOrderedContainer();
	
	loc_containerVariable.setPathAdapter(loc_elementsContainer);
	
	_.each(valueElements, function(valueEle, index){
		
		var eleId = loc_elementsContainer.insertValue(valueEle.value, index, valueEle.id);

	});
	
	var loc_out = {
		getElements : function(){	return loc_elements;	},
		
		clearup : function(){
			
		},
	};
	
	return loc_out;
};


/**
 * Container for ordered elements in wrapper
 * value : value of element
 * index : position in array, index for element may change
 * id	 : id for element that wont change; id is also internal path
 * 		id use path if path exist
 * 		otherwise, generate id 
 * path  : path related with container. 
 * 		if path exists, it is used as id. 
 * 		if path not exists, use index as path.
 */
var node_createOrderedContainer = function(){

	//id generation 
	var loc_id = 0;
	
	var loc_elementById = {};
	var loc_pathById = {};
	var loc_ids = [];
	
	var loc_generateId = function(){
		loc_id++;
		return "id"+loc_id+"";
	};
	
	var loc_getIdByIndex = function(index){
		return loc_ids[index];
	};
	
	var loc_out = {
		getValueByIndex : function(index){		return this.getValueById(loc_getIdByIndex(index));		},
		
		getValueById : function(id){	return loc_valueById[id];	},
		
		getPathById : function(id){
			var path = loc_pathById[id];       //find from provided
			if(path==undefined)		path = this.getIndexById(id)+"";   //not provided, then use index as path
			return path;
		},
		
		getIndexById : function(id){	return loc_ids.indexOf(id);	},
		
		getIdByPath : function(path){
			var out;
			_.each(loc_pathById, function(p, id){
				if(path==p){
					out = p;
				}
			});
			if(out==undefined)  out = loc_ids[path];    //if not provided, treat path as index
			return out;
		},
		
		insertValue : function(value, index, path){
			var id = path;
			if(id==undefined)  id = loc_generateId();
			
			loc_valueById[id] = value;
			loc_ids.splice(index, 0, id);
			
			if(path!=undefined)  loc_pathById[id] = path;
			return id;
		},
		
		deleteElementByIndex : function(index){
			var id = loc_ids[index];
			var path = this.getPathById(id);
			
			loc_ids.splice(index, 1);
			delete loc_valueById[id];
			delete loc_pathById[id];
			
			return path;
		},
	
		getAllValue : function(){
			var out = [];
			for(var i in loc_ids){
				out.push(this.getValueByIndex(i));
			}
			return out;
		},
		
		clear : function(){
			loc_id = 0;
			loc_valueById = {};
			loc_pathById = {};
			loc_ids = [];
		}
		
	};
	return loc_out;
};	


	