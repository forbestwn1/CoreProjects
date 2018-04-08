//get/create package
var packageObj = library.getChildPackage("context");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_COMMONCONSTANT;
var node_makeObjectWithType;
var node_getObjectType;
var node_createEventObject;
var node_eventUtility;
var node_createContextElement;
var node_createContextVariableInfo;
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

//*******************************************   Start Node Definition  ************************************** 	
/*
 * elementInfosArray : an array of element info describing context element
 * 
 */
var node_createContext = function(elementInfosArray, request){
	
	//according to contextVariableInfo, find the base variable from Context
	//base variable contains two info: 1. variable,  2. path from variable
	var loc_findBaseVariable = function(contextVariableInfo){
		var fullPath = contextVariableInfo.getFullPath();
		
		//get parent var from adapter first
		//find longest matching path
		var parentVar;
		var varPath = contextVariableInfo.path;
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
			parentVar = loc_out.prv_elements[contextVariableInfo.name].variable;
			varPath = contextVariableInfo.path;
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
	
	var loc_createVariableFromContextVariableInfo = function(contextVariableInfo, adapterInfo){
		var baseVar = loc_findBaseVariable(contextVariableInfo);
		var variable = baseVar.variable.createChildVariable(baseVar.path, adapterInfo); 
		//add extra attribute "contextPath" to variable for variables name under context
		variable.contextPath = contextVariableInfo.getFullPath();
		return variable;
	};
	
	var loc_buildAdapterVariableFromMatchers = function(rootName, path, matchers, reverseMatchers){
		var contextVar = node_createContextVariableInfo(rootName, path);
		var adapter = {
			getInValueRequest : function(value, handlers, request){
				return nosliw.runtime.getExpressionService().getMatchDataRequest(value, matchers, handlers, request);
//				return node_createServiceRequestInfoSimple({}, function(request){
//					value.value = "0123456789" + value.value;
//					return value;
//				}, handlers, request);
			},
			getOutValueRequest : function(value, handlers, request){
//				return nosliw.runtime.getExpressionService().getMatchDataRequest(value, reverseMatchers, handlers, request);
				return nosliw.runtime.getExpressionService().getMatchDataRequest(value, reverseMatchers, {
					success : function(request, data){
						var kkkk = 5555;
						kkkk++;
					}
				}, request);
//				return node_createServiceRequestInfoSimple({}, function(request){
//					value.value = value.value.substring(10);
//					return value;
//				}, handlers, request);
			},
		};
		var variable = loc_createVariableFromContextVariableInfo(contextVar, {
			valueAdapter : adapter
		});
		return variable;
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
		//context elements (wrapper variables)
		loc_out.prv_elements = {};
		//object used as event object
		loc_out.prv_eventObject = node_createEventObject();
		//adapter variables by path
		loc_out.prv_adapters = {};
		
		_.each(elementInfosArray, function(elementInfo, key){
			//create empty wrapper variable for each element
			var contextEle = node_createContextElement(elementInfo, request);
			loc_out.prv_elements[elementInfo.name] = contextEle;
			
			var eleVar = contextEle.variable;
			nosliw.logging.info("************************  Named variable creation  ************************");
			nosliw.logging.info("Name: " + contextEle.name);
			nosliw.logging.info("ID: " + eleVar.prv_id);
			nosliw.logging.info("Wrapper: " + (eleVar.prv_wrapper==undefined?"":eleVar.prv_wrapper.prv_id));
			nosliw.logging.info("Parent: " , ((eleVar.prv_relativeVariableInfo==undefined)?"":eleVar.prv_relativeVariableInfo.parent.prv_id));
			nosliw.logging.info("ParentPath: " , ((eleVar.prv_relativeVariableInfo==undefined)?"":eleVar.prv_relativeVariableInfo.path)); 
			nosliw.logging.info("***************************************************************");
			
			//get all adapters from elementInfo
			_.each(elementInfo.info.matchers, function(matchers, path){
				loc_out.prv_adapters[node_dataUtility.combinePath(elementInfo.name, path)] = loc_buildAdapterVariableFromMatchers(elementInfo.name, path, matchers, elementInfo.info.reverseMatchers[path]);
			});
		});
	};
	
	var loc_out = {
		
		/*
		 * create context variable
		 */
		createVariable : function(contextVariableInfo){
			return loc_createVariableFromContextVariableInfo(contextVariableInfo);
		},
		
		getDataOperationRequest : function(eleName, operationService, handlers, request){
			var operationPath = operationService.parms.path;
			var baseVariable = loc_findBaseVariable(node_createContextVariableInfo(eleName, operationPath));
			if(operationPath!=undefined){
				operationService.parms.path = baseVariable.path;
			}
			return baseVariable.variable.getDataOperationRequest(operationService, handlers, request);
		},
		
		getHandleEachElementRequest : function(name, path, elementHandleRequestFactory, handlers, request){
			var eleVar = loc_out.prv_elements[name].variable;
			return node_getHandleEachElementRequest(eleVar, path, elementHandleRequestFactory, handlers, request)
		},
		
		getElementsName : function(){
			var out = [];
			_.each(loc_out.prv_elements, function(ele, eleName){
				out.push(eleName);
			});
			return out;
		},

		destroy : function(requestInfo){	node_getLifecycleInterface(loc_out).destroy(requestInfo);	},
		
		/*
		 * update context wrappers
		 * elements: 
		 * 		name --- wrapper
		 * 		new wrappers
		 * only update those element variables contains within wrappers 
		 */
//		updateContext : function(wrappers, requestInfo){
//			this.prv_eventObject.triggerEvent(node_CONSTANT.CONTEXT_EVENT_BEFOREUPDATE, this, requestInfo);
//
//			var that = this;
//			_.each(wrappers, function(wrapper, name){
//				//set wrapper to each variable
//				var eleVar = loc_getContextElementVariable(name);
//				if(eleVar!=undefined){
//					eleVar.setWrapper(wrapper, requestInfo);
//				}
//			});
//
//			this.prv_eventObject.triggerEvent(node_CONSTANT.CONTEXT_EVENT_UPDATE, this, requestInfo);
//			
//			this.prv_eventObject.triggerEvent(node_CONSTANT.CONTEXT_EVENT_AFTERUPDATE, this, requestInfo);
//		},
		
	};

	//append resource life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_CONTEXT);
	
	node_getLifecycleInterface(loc_out).init(elementInfosArray, request);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElement", function(){node_createContextElement = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextVariableInfo", function(){node_createContextVariableInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createVariable", function(){node_createVariable = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.dataoperation.DataOperationService", function(){node_DataOperationService = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createVariableWrapper", function(){node_createVariableWrapper = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.orderedcontainer.getHandleEachElementRequest", function(){node_getHandleEachElementRequest = this.getData();});


//Register Node by Name
packageObj.createChildNode("createContext", node_createContext); 

})(packageObj);
