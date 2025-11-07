//get/create package
var packageObj = library.getChildPackage("valuestructure");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_COMMONCONSTANT;
var node_COMMONATRIBUTECONSTANT;
var node_makeObjectWithType;
var node_getObjectType;
var node_createEventObject;
var node_eventUtility;
var node_createContextElement;
var node_createValueStructureVariableRef;
var node_createVariable;
var node_DataOperationService;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_namingConvensionUtility;
var node_ServiceInfo;
var node_dataUtility;
var node_createServiceRequestInfoSimple;
var node_createVariableWrapper;
var node_getHandleEachElementRequest;
var node_createServiceRequestInfoSequence;
var node_createServiceRequestInfoSet;
var node_createValueInVarOperationRequest;
var node_valueInVarOperationServiceUtility;
var node_ValueInVarOperation;
var node_createRequestEventGroupHandler;
var node_createEmptyValue;
var node_createValueStructureElementInfo;

//*******************************************   Start Node Definition  ************************************** 	
/*
 * elementInfosArray : an array of element info describing value structure element
 * 
 */
var node_createValueStructure = function(id, valuePortContainer, request){
	
	var loc_updateRequest = {};
	
	var loc_valuePortContainer = valuePortContainer;
	
	var loc_init = function(id){
    	//id, for debug purpose
	    loc_out.prv_id = id;
		//context elements (wrapper variables)
		loc_out.prv_elements = {};
		//object used as event object
		loc_out.prv_eventObject = node_createEventObject();
		//adapter variables by path
		loc_out.prv_adapters = {};
	}
	
	//according to contextVariableInfo, find the base variable from Context
	//base variable contains two info: 1. variable,  2. path from variable
	var loc_findBaseVariable = function(elementRef){
		var fullPath = elementRef.getFullPath();
		
		//get parent var from adapter first
		//find longest matching path
		var parentVar;
		var varPath = elementRef.path;
		var pathLength = -1;
		_.each(loc_out.prv_adapters, function(adapterVariable, path){
			var comparePath = node_dataUtility.comparePath(fullPath, path);
			if(path.length>pathLength){
				if(comparePath.compare==0){
					parentVar = adapterVariable;
					varPath = "";
					pathLength = path.length;
				}
				else if(comparePath.compare==1){
					parentVar = adapterVariable;
					varPath = comparePath.subPath;
					pathLength = path.length;
				}
			}
		});
		
		//not found, use variable from elements
		if(parentVar==undefined){
			if(loc_out.prv_elements[elementRef.name]!=undefined){
				parentVar = loc_out.prv_elements[elementRef.name].variable;
				varPath = elementRef.path;
			}
			else return;
		}
		
		return {
			variable : parentVar,
			path : varPath
		}
	};
	
	/*
	 * get context element variable by name
	 */
	var loc_getContextElementVariable = function(name){ 
		var contextEle = loc_out.prv_elements[name];
		if(contextEle==undefined)  return undefined;
		return contextEle.variable;
	};
	
	var loc_createVariableFromContextVariableInfo = function(elementRef, adapterInfo, requestInfo){
		var baseVar = loc_findBaseVariable(elementRef);
		if(baseVar==undefined){
			return;
//			nosliw.error(contextVariableInfo);
//			loc_findBaseVariable(contextVariableInfo);
		}
		var variable = baseVar.variable.createChildVariable(baseVar.path, adapterInfo, requestInfo); 
		return variable;
	};
	
	var loc_buildAdapterVariableFromMatchers = function(rootName, path, matchers, reverseMatchers){
		var contextVar = node_createValueStructureVariableRef(rootName, path);
		var adapter = {
			getInValueRequest : function(value, handlers, request){
				return nosliw.runtime.getExpressionService().getMatchDataRequest(value, matchers, handlers, request);
			},
			getOutValueRequest : function(value, handlers, request){
				return nosliw.runtime.getExpressionService().getMatchDataRequest(value, reverseMatchers, handlers, request);
			},
		};
		var variable = loc_createVariableFromContextVariableInfo(contextVar, {
			valueAdapter : adapter
		});
		return variable;
	};
	
	var loc_flatArray = function(valueArray, out){
		if(Array.isArray(valueArray)){
			_.each(valueArray, function(value, index){
				loc_flatArray(value, out);
			});
		}
		else{
			out.push(valueArray);
		}
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		_.each(loc_out.prv_elements, function(element, name){
			//clear up variable
			element.variable.release(requestInfo);
		});
		loc_out.prv_elements = {};
		
		loc_out.prv_eventObject.clearup();
	};
	
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(elementInfosArray, request){
		
		loc_out.prv_valueChangeEventEnable = false;
		loc_out.prv_valueChangeEventSource = node_createEventObject();
		loc_out.prv_eleVariableGroup = loc_createVariableGroup([], function(request){
			if(loc_out.prv_valueChangeEventEnable == true){
				if(loc_updateRequest[request.getId()]!=null){
					//change from update context
					loc_out.prv_valueChangeEventSource.triggerEvent(node_CONSTANT.CONTEXT_EVENT_UPDATE, undefined, request);
//					delete loc_updateRequest[request.getId()];
				}
				else{
					//change from data operation
					loc_out.prv_valueChangeEventSource.triggerEvent(node_CONSTANT.CONTEXT_EVENT_VALUECHANGE, undefined, request);
				}
			}
		});
				
		_.each(elementInfosArray, function(elementInfo, key){
			loc_addValueStructureElement(elementInfo, request);
		});
		loc_out.prv_valueChangeEventEnable = true;
	};

	var loc_addValueStructureElement = function(elementInfo, request){
		//create empty wrapper variable for each element
		var valueStructureElement = loc_createValueStructureElement(elementInfo, loc_valuePortContainer, request);

		if(valueStructureElement!=undefined){
			loc_out.prv_eleVariableGroup.addVariable(valueStructureElement.variable, valueStructureElement.path);
			loc_out.prv_elements[valueStructureElement.name] = valueStructureElement;
			
			var eleVar = valueStructureElement.variable;
			nosliw.logging.info("************************  Named variable creation  ************************");
			nosliw.logging.info("Name: " + valueStructureElement.name);
			nosliw.logging.info("ID: " + eleVar.prv_id);
			nosliw.logging.info("Wrapper: " + (eleVar.prv_wrapper==undefined?"":eleVar.prv_wrapper.prv_id));
//					nosliw.logging.info("Parent: " , ((eleVar.prv_getRelativeVariableInfo()==undefined)?"":eleVar.prv_getRelativeVariableInfo().parent.prv_id));
//					nosliw.logging.info("ParentPath: " , ((eleVar.prv_getRelativeVariableInfo()==undefined)?"":eleVar.prv_getRelativeVariableInfo().path)); 
			nosliw.logging.info("***************************************************************");

/*			
			//get all adapters from elementInfo
			if(elementInfo.info.matchers!=undefined){
				var matchersInfo = elementInfo.info.matchers;
    			_.each(matchersInfo.matchers, function(matchers, path){
	    			loc_out.prv_adapters[node_dataUtility.combinePath(valueStructureElement.name, path)] = loc_buildAdapterVariableFromMatchers(valueStructureElement.name, path, matchers, matchersInfo.reverseMatchers[path]);
		    	});
			}
*/
//			_.each(contextElesInfo.variables, function(contextEleVarInfo, i){
//			});
			
		}
		
	};
	
	var loc_out = {
		
		getElement : function(name){
			return loc_getContextElementVariable(name);
		},
		
		addVariable : function(variable, name, request){
			loc_out.prv_eleVariableGroup.addVariable(variable, name);
			loc_out.prv_elements[name] = {
				name : name,
				variable : variable
			};
		},
			
		addElement : function(elementInfo, request){		
			var flatedelEmentInfosArray = [];
			loc_flatArray(elementInfo, flatedelEmentInfosArray);
			_.each(flatedelEmentInfosArray, function(elementInfo, key){
				loc_addValueStructureElement(elementInfo, request);
			});
		},	
			
		/*
		 * create context variable
		 */
		createVariable : function(contextVariableInfo, requestInfo){
			return loc_createVariableFromContextVariableInfo(contextVariableInfo, requestInfo);
		},
		
		getDataOperationRequest : function(eleName, operationService, handlers, request){
			var operationPath = operationService.parms.path;
			var baseVariable = loc_findBaseVariable(node_createValueStructureVariableRef(eleName, operationPath));
			if(baseVariable!=undefined){
				if(operationPath!=undefined){
					operationService.parms.path = baseVariable.path;
				}
				return baseVariable.variable.getDataOperationRequest(operationService, handlers, request);
			}
		},
		
		createHandleEachElementProcessor : function(name, path){
			var eleVar = loc_out.prv_elements[name].variable;
			return node_createHandleEachElementProcessor(eleVar, path);
		},
		
		getElementsName : function(){
			var out = [];
			_.each(loc_out.prv_elements, function(ele, eleName){  out.push(eleName);});
			return out;
		},

		getElementsVariable : function(){
			var out = [];
			_.each(loc_out.prv_elements, function(ele, eleName){	out.push(ele.variable);	});
			return out;
		},

		destroy : function(requestInfo){	node_getLifecycleInterface(loc_out).destroy(requestInfo);	},
		
		getUpdateContextRequest : function(values, handlers, requestInfo){
			loc_out.prv_valueChangeEventEnable = false;
			var that = this;
			var outRequest = node_createServiceRequestInfoSequence({}, handlers, requestInfo);
			outRequest.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				loc_updateRequest[request.getId()] = request;
			}));
			var setRequest = node_createServiceRequestInfoSet({}, {
				success : function(requestInfo, result){
					loc_out.prv_valueChangeEventEnable = true;
				}
			});
			
			_.each(loc_out.getElementsName(), function(name, index){
				var value = values[name];
				if(value!=undefined){
					var dataOpRequest = node_createValueInVarOperationRequest(that, new node_ValueInVarOperation(name, node_valueInVarOperationServiceUtility.createSetOperationService("", value)));
					setRequest.addRequest(name, dataOpRequest);
				}
			});
			
			outRequest.addRequest(setRequest);
			return outRequest;
		},
		
		getAllElementsValuesRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			var setRequest = node_createServiceRequestInfoSet(undefined, {
				success : function(request, result){
					var out = {};
					_.each(result.getResults(), function(result, name){
						out[name] = result.value;
					});
					return out;
//					return result.getResults();
				}
			});
			
			_.each(this.getElementsName(), function(varName, i){
				setRequest.addRequest(varName, loc_out.getElement(varName).getGetValueRequest());
			});
			
			out.addRequest(setRequest);
			return out;
		},
		
		registerValueChangeEventListener : function(listener, handler, thisContext){	return loc_out.prv_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext);},
		unregisterValueChangeEventListener : function(listener){	return loc_out.prv_valueChangeEventSource.unregister(listener);},
		
	};

    loc_init(id);

	//append resource life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VALUESTRUCTURE);
	
	return loc_out;
};

/*
 * create real value structure element based on element info 
 * it contains following attribute:
 * 		name
 * 		variable
 * 		info
 */
var loc_createValueStructureElement = function(elementInfo, valuePortContainer, requestInfo){
	var loc_out = {
		name : elementInfo.name,
		info : elementInfo.info,
	};

	var adapterInfo = elementInfo.adapterInfo;
	//get variable
	if(elementInfo.placeholder==true){
		loc_out.variable= node_createVariableWrapper(node_createEmptyValue(), undefined, adapterInfo, requestInfo);
	}
	else if(elementInfo.valueStructureElementDefinition!=undefined){
		loc_out.variable = loc_createValueStructureVariable(valuePortContainer, elementInfo.valueStructureElementDefinition);
	}
	else if(elementInfo.valueStructure!=undefined){
		//element by context
		var eleVariable = elementInfo.valueStructure.createVariable(elementInfo.valueStructureVariableRef, adapterInfo, requestInfo);
		//cannot create context element variable
		if(eleVariable==undefined)   return;
		loc_out.variable = eleVariable;
	}
	else if(elementInfo.variable!=undefined){
		//element by variable
		loc_out.variable= node_createVariableWrapper(elementInfo.variable, elementInfo.path, adapterInfo, requestInfo);
	}
	else  loc_out.variable = node_createVariableWrapper(elementInfo.data1, elementInfo.data2, adapterInfo, requestInfo);
	
	return loc_out;
};

var loc_createValueStructureVariable = function(currentValuePortContainer, valueStructureEleDef){
	var eleDef = valueStructureEleDef.getDefinition();
	var variableInfo = {
		definition: eleDef
	};
	var type = eleDef[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_TYPE];
    if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_VALUE){
		var resolveInfo = eleDef[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_RESOLVEDINFO];
	
		var parentValueStructureRuntimeId = resolveInfo[node_COMMONATRIBUTECONSTANT.INFORELATIVERESOLVE_STRUCTUREID];
		var parentValueStructure = currentValuePortContainer.getValueStructure(parentValueStructureRuntimeId);
							
		var resolvePathObj = resolveInfo[node_COMMONATRIBUTECONSTANT.INFORELATIVERESOLVE_PATH];
		var resolveRootName = resolvePathObj[node_COMMONATRIBUTECONSTANT.COMPLEXPATH_ROOT];
		var resolvePath = resolvePathObj[node_COMMONATRIBUTECONSTANT.COMPLEXPATH_PATH];
							
		var valueAdapter;
    	var matchers = eleDef[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_MATCHERS]==undefined? undefined : eleDef[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_MATCHERS][""];
    	if(matchers!=undefined){
    	    var reverseMatchers = eleDef[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_REVERSEMATCHERS]==undefined? undefined : eleDef[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_REVERSEMATCHERS][""];
    		valueAdapter = {
	    		getInValueRequest : function(value, handlers, request){
		    		return nosliw.runtime.getExpressionService().getMatchDataRequest(value, matchers, handlers, request);
			    },
    			getOutValueRequest : function(value, handlers, request){
	    			return nosliw.runtime.getExpressionService().getMatchDataRequest(value, reverseMatchers, handlers, request);
		    	},
    		};
		}

		return parentValueStructure.createVariable(
			node_createValueStructureVariableRef(resolveRootName, resolvePath), 
			{
			    valueAdapter : valueAdapter
		    },
		    variableInfo);
	}
	else{
		var elementInfo;
		
		//not relative or logical relative variable
		var defaultValue = valueStructureEleDef.getInitValue();  
			
		var criteria;
		if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE)	criteria = eleDef[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DEFINITION][node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DATA];
		else  criteria = eleDef[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DATA]; 
		if(criteria!=undefined){
			//app data, if no default, empty variable with wrapper type
			if(defaultValue!=undefined) 	elementInfo = node_createValueStructureElementInfo(undefined, node_dataUtility.createDataOfAppData(defaultValue), "");
			else  elementInfo = node_createValueStructureElementInfo(undefined, undefined, node_CONSTANT.DATA_TYPE_APPDATA);
		}
		else{
			//object, if no default, empty variable with wrapper type
			if(defaultValue!=undefined)		elementInfo = node_createValueStructureElementInfo(undefined, defaultValue, "");
			else elementInfo = node_createValueStructureElementInfo(undefined, undefined, node_CONSTANT.DATA_TYPE_OBJECT);
		}
		return loc_createValueStructureElement(elementInfo, currentValuePortContainer).variable;
	}
	
};

	var loc_createVariableFromContextVariableInfo = function(elementRef, adapterInfo, requestInfo){
		var baseVar = loc_findBaseVariable(elementRef);
		if(baseVar==undefined){
			return;
//			nosliw.error(contextVariableInfo);
//			loc_findBaseVariable(contextVariableInfo);
		}
		var variable = baseVar.variable.createChildVariable(baseVar.path, adapterInfo, requestInfo); 
		return variable;
	};
	
	var loc_buildAdapterVariableFromMatchers = function(rootName, path, matchers, reverseMatchers){
		var contextVar = node_createValueStructureVariableRef(rootName, path);
		var adapter = {
			getInValueRequest : function(value, handlers, request){
				return nosliw.runtime.getExpressionService().getMatchDataRequest(value, matchers, handlers, request);
			},
			getOutValueRequest : function(value, handlers, request){
				return nosliw.runtime.getExpressionService().getMatchDataRequest(value, reverseMatchers, handlers, request);
			},
		};
		var variable = loc_createVariableFromContextVariableInfo(contextVar, {
			valueAdapter : adapter
		});
		return variable;
	};


var loc_createVariableGroup = function(variablesArray, handler, thisContext){

	//event handler
	var loc_handler = handler;
	
	var loc_requestEventGroupHandler = undefined;
	
	var loc_addElement = function(variable, key){
		loc_requestEventGroupHandler.addElement(variable.getDataChangeEventObject(), key+"");
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(variablesArray, handler, thisContext){
		loc_requestEventGroupHandler = node_createRequestEventGroupHandler(loc_handler, thisContext);
		
		for(var i in variablesArray){
			loc_addElement(variablesArray[i], i);
		}
	};	
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		loc_requestEventGroupHandler.destroy(requestInfo);
		loc_handler = undefined;
	};

	var loc_out = {

		addVariable : function(variable, key){	loc_addElement(variable, key);		},
		
		triggerEvent : function(requestInfo){   loc_requestEventGroupHandler.triggerEvent(requestInfo);  },
		
		destroy : function(requestInfo){	node_getLifecycleInterface(loc_out).destroy(requestInfo);	},
	};
	
	//append resource and object life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	node_getLifecycleInterface(loc_out).init(variablesArray, handler, thisContext);
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("variable.context.createContextElement", function(){node_createContextElement = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.valuestructure.createValueStructureVariableRef", function(){node_createValueStructureVariableRef = this.getData();});
nosliw.registerSetNodeDataEvent("variable.variable.createVariable", function(){node_createVariable = this.getData();});
nosliw.registerSetNodeDataEvent("variable.dataoperation.DataOperationService", function(){node_DataOperationService = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("variable.valueinvar.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("variable.wrapper.createVariableWrapper", function(){node_createVariableWrapper = this.getData();});
nosliw.registerSetNodeDataEvent("variable.orderedcontainer.createHandleEachElementProcessor", function(){node_createHandleEachElementProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.createValueInVarOperationRequest", function(){node_createValueInVarOperationRequest = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.valueInVarOperationServiceUtility", function(){node_valueInVarOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.ValueInVarOperation", function(){node_ValueInVarOperation = this.getData();});
nosliw.registerSetNodeDataEvent("request.event.createRequestEventGroupHandler", function(){node_createRequestEventGroupHandler = this.getData();});
nosliw.registerSetNodeDataEvent("common.empty.createEmptyValue", function(){node_createEmptyValue = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.valuestructure.createValueStructureElementInfo", function(){node_createValueStructureElementInfo = this.getData();});


//Register Node by Name
packageObj.createChildNode("createValueStructure", node_createValueStructure); 

})(packageObj);
