//get/create package
var packageObj = library.getChildPackage("context");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_COMMONATRIBUTECONSTANT;
var node_COMMONCONSTANT;
var node_basicUtility;
var node_parseSegment;
var node_createContextElementInfo;
var node_createContext;
var node_createContextVariableInfo;
var node_createServiceRequestInfoSequence;
var node_ServiceInfo;
var node_createServiceRequestInfoSet;
var node_uiDataOperationServiceUtility;
var node_dataUtility;

//*******************************************   Start Node Definition  ************************************** 	
var node_utility = function(){
		
	var loc_out = {
			
		getContextElementDefinitionFromFlatContext : function(flatContext, name){
			var globalName = flatContext[node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_LOCAL2GLOBAL][name];
			if(globalName==undefined)   globalName = name;
			return flatContext[node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXT_ELEMENT][globalName];
		},
			
		parseContextElementName : function(name){
			var segs = name.split(node_COMMONCONSTANT.SEPERATOR_CONTEXT_CATEGARY_NAME);
			var out = {};
			if(segs.length==1){
				out.name = name;
			}
			else if(segs.length==2){
				out.categary = segs[1];
				out.name = segs[0];
			}
			return out;
		},

		getContextValueAsParmsRequest : function(context, handlers, requestInfo){
			return this.getContextEleValueAsParmsRequest(context.prv_elements, handlers, requestInfo);
		},

		//context value with only simple name element
		//only context element without categary info
		getContextEleValueAsParmsRequest : function(contextItems, handlers, requestInfo){
			var outRequest = node_createServiceRequestInfoSequence({}, handlers, requestInfo);
			var setRequest = node_createServiceRequestInfoSet({}, {
				success : function(requestInfo, result){
					var out = {};
					_.each(result.getResults(), function(contextData, name){
						if(contextData!=undefined)		out[name] = contextData.value;
					});
					return out;
				}
			});
			_.each(contextItems, function(ele, eleName){
				var eleNameInfo = loc_out.parseContextElementName(eleName);
				if(eleNameInfo.categary==undefined){
					setRequest.addRequest(eleName, ele.variable.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService()));
				}
			});
			outRequest.addRequest(setRequest);
			return outRequest;
		},

		//context name with only base variable
		getContextStateRequest : function(contextItems, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("GetContextState", {}), handlers, request);
			var calContextValue = node_createServiceRequestInfoSet(undefined, {
				success : function(request, resultSet){
					var state = {};
					_.each(resultSet.getResults(), function(eleData, eleName){
						if(eleData!=undefined)			state[eleName] = eleData.value;
					});
					return state;
				}
			});
	
			var validVariable = {};
			_.each(contextItems, function(contextItem, eleName){
				var variable = contextItem.variable.prv_variable;
				if(variable.prv_isBase==true){
					if(validVariable[variable.prv_id]==undefined){
						validVariable[variable.prv_id] = variable;
						//only base element
						calContextValue.addRequest(eleName, contextItem.variable.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService()));
					}
				}
			});
			
			out.addRequest(calContextValue);
			return out;
		},

		//context value with context group structure
		//from flat context to context group
		buildContextGroupRequest : function(contextItems, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("GetContextValue", {}), handlers, request);
			var calContextValue = node_createServiceRequestInfoSet(undefined, {
				success : function(request, resultSet){
					var value = {};
					_.each(resultSet.getResults(), function(eleValue, eleName){
						var eleNameInfo = loc_out.parseContextElementName(eleName);
						var categaryContext = value[eleNameInfo.categary];
						if(categaryContext==undefined){
							categaryContext = {};
							value[eleNameInfo.categary] = categaryContext;
						}
						categaryContext[eleNameInfo.name] = eleValue==undefined? undefined:eleValue.value;
					});
					return value;
				}
			});
	
			_.each(contextItems, function(contextItem, eleName){
				var eleNameInfo = loc_out.parseContextElementName(eleName);
				if(eleNameInfo.categary!=undefined){
					//only those with category info
					calContextValue.addRequest(eleName, contextItem.variable.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService()));
				}
			});
			
			out.addRequest(calContextValue);
			return out;
		},
		
		buildContextFromFlat : function(id, flatContext, parentContext, requestInfo){
			
			var contextDef = flatContext[node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXT_ELEMENT];
			var namesByVarId = flatContext[node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_NAMESBYVARID];
			var varIdByName = flatContext[node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_VARIDBYNAME];
			
			//build context element first
			var contextElementInfosArray = [];
			
			_.each(contextDef, function(contextDefRootObj, eleName){
				eleName = namesByVarId[varIdByName[eleName]];   //expand name to alias
				
				var contextDefRootEle = contextDefRootObj[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFINITION];
				
				var info = {
					matchers : contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_MATCHERS],
					reverseMatchers : contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_REVERSEMATCHERS]
				};
				var type = contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_TYPE];
				var contextInfo = contextDefRootObj[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
				//if context.info.instantiate===manual, context does not need to create in the framework
				if(contextInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE]!=node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE_MANUAL){
					if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE && 
							contextInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION]!=node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_LOGICAL){
						//physical relative
						if(contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_PARENT]==node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT){
	//					if(contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_ISTOPARENT]==true){
							//process relative that  refer to element in parent context
							var pathObj = contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_PATH];
							var rootName = pathObj[node_COMMONATRIBUTECONSTANT.CONTEXTPATH_ROOTNAME];
							var path = pathObj[node_COMMONATRIBUTECONSTANT.CONTEXTPATH_PATH];
							contextElementInfosArray.push(node_createContextElementInfo(eleName, parentContext, node_createContextVariableInfo(rootName, path), undefined, info));
						}
					}
					else{
						//not relative or logical relative variable
						var defaultValue = contextDefRootObj[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFAULT];
						
						var criteria;
						if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE)	criteria = contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_DEFINITION][node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_CRITERIA];
						else  criteria = contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_CRITERIA]; 
						if(criteria!=undefined){
							//app data, if no default, empty variable with wrapper type
							if(defaultValue!=undefined) 	contextElementInfosArray.push(node_createContextElementInfo(eleName, node_dataUtility.createDataOfAppData(defaultValue), "", undefined, info));
							else  contextElementInfosArray.push(node_createContextElementInfo(eleName, undefined, node_CONSTANT.DATA_TYPE_APPDATA, undefined, info));
						}
						else{
							//object, if no default, empty variable with wrapper type
							if(defaultValue!=undefined)		contextElementInfosArray.push(node_createContextElementInfo(eleName, defaultValue, "", undefined, info));
							else contextElementInfosArray.push(node_createContextElementInfo(eleName, undefined, node_CONSTANT.DATA_TYPE_OBJECT, undefined, info));
						}
					}
				}
			});	
				
			var context = node_createContext(id, contextElementInfosArray, requestInfo);
	
			//for relative which refer to context ele in same context
			_.each(contextDef, function(contextDefRootObj, eleName){
				var contextDefRootEle = contextDefRootObj[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFINITION];
				var info = {
						matchers : contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_MATCHERS],
						reverseMatchers : contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_REVERSEMATCHERS]
				};
				var type = contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_TYPE];
				var contextInfo = contextDefRootObj[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
				//if context.info.instantiate===manual, context does not need to create in the framework
				if(contextInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE]!=node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE_MANUAL){
					if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE && contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_PARENT]==node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_SELF){
	//				if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE && contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_ISTOPARENT]==false){
						var pathObj = contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_PATH];
						var rootName = pathObj[node_COMMONATRIBUTECONSTANT.CONTEXTPATH_ROOTNAME];
						var path = pathObj[node_COMMONATRIBUTECONSTANT.CONTEXTPATH_PATH];
						//only process element that parent is created
						if(context.getContextElement(rootName)!=undefined){
							context.addContextElement(node_createContextElementInfo(eleName, context, node_createContextVariableInfo(rootName, path), undefined, info));
						}
					}
				}
			});	
			
			return context;
		},
		
		//build context according to context definition and parent context
		buildContext : function(id, contextDef, parentContext, requestInfo){
			//build context element first
			var contextElementInfosArray = [];
			
			_.each(contextDef, function(contextDefRootObj, eleName){
				var contextDefRootEle = contextDefRootObj[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFINITION];
				
				var info = {
					matchers : contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_MATCHERS],
					reverseMatchers : contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_REVERSEMATCHERS]
				};
				var type = contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_TYPE];
				var contextInfo = contextDefRootObj[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
				//if context.info.instantiate===manual, context does not need to create in the framework
				if(contextInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE]!=node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE_MANUAL){
					if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE && 
							contextInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION]!=node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_LOGICAL){
						//physical relative
						if(contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_PARENT]==node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT){
	//					if(contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_ISTOPARENT]==true){
							//process relative that  refer to element in parent context
							var pathObj = contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_PATH];
							var rootName = pathObj[node_COMMONATRIBUTECONSTANT.CONTEXTPATH_ROOTNAME];
							var path = pathObj[node_COMMONATRIBUTECONSTANT.CONTEXTPATH_PATH];
							contextElementInfosArray.push(node_createContextElementInfo(eleName, parentContext, node_createContextVariableInfo(rootName, path), undefined, info));
						}
					}
					else{
						//not relative or logical relative variable
						var defaultValue = contextDefRootObj[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFAULT];
						
						var criteria;
						if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE)	criteria = contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_DEFINITION][node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_CRITERIA];
						else  criteria = contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_CRITERIA]; 
						if(criteria!=undefined){
							//app data, if no default, empty variable with wrapper type
							if(defaultValue!=undefined) 	contextElementInfosArray.push(node_createContextElementInfo(eleName, node_dataUtility.createDataOfAppData(defaultValue), "", undefined, info));
							else  contextElementInfosArray.push(node_createContextElementInfo(eleName, undefined, node_CONSTANT.DATA_TYPE_APPDATA, undefined, info));
						}
						else{
							//object, if no default, empty variable with wrapper type
							if(defaultValue!=undefined)		contextElementInfosArray.push(node_createContextElementInfo(eleName, defaultValue, "", undefined, info));
							else contextElementInfosArray.push(node_createContextElementInfo(eleName, undefined, node_CONSTANT.DATA_TYPE_OBJECT, undefined, info));
						}
					}
				}
			});	
				
			var context = node_createContext(id, contextElementInfosArray, requestInfo);
	
			//for relative which refer to context ele in same context
			_.each(contextDef, function(contextDefRootObj, eleName){
				var contextDefRootEle = contextDefRootObj[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFINITION];
				var info = {
						matchers : contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_MATCHERS],
						reverseMatchers : contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_REVERSEMATCHERS]
				};
				var type = contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_TYPE];
				var contextInfo = contextDefRootObj[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
				//if context.info.instantiate===manual, context does not need to create in the framework
				if(contextInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE]!=node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE_MANUAL){
					if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE && contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_PARENT]==node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_SELF){
	//				if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE && contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_ISTOPARENT]==false){
						var pathObj = contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_PATH];
						var rootName = pathObj[node_COMMONATRIBUTECONSTANT.CONTEXTPATH_ROOTNAME];
						var path = pathObj[node_COMMONATRIBUTECONSTANT.CONTEXTPATH_PATH];
						//only process element that parent is created
						if(context.getContextElement(rootName)!=undefined){
							context.addContextElement(node_createContextElementInfo(eleName, context, node_createContextVariableInfo(rootName, path), undefined, info));
						}
					}
				}
			});	
			
			return context;
		},
	};
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.segmentparser.parseSegment", function(){node_parseSegment = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContext", function(){node_createContext = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextVariableInfo", function(){node_createContextVariableInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
