//get/create package
var packageObj = library.getChildPackage("valuestructure");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_COMMONATRIBUTECONSTANT;
var node_COMMONCONSTANT;
var node_basicUtility;
var node_parseSegment;
var node_createServiceRequestInfoSequence;
var node_ServiceInfo;
var node_createServiceRequestInfoSet;
var node_valueInVarOperationServiceUtility;
var node_dataUtility;
var node_aliasUtility;

//*******************************************   Start Node Definition  ************************************** 	
var node_utility = function(){
		
	var loc_out = {
			
			//build context according to context definition and parent context
			buildValueStructure : function(id, valueStructureDef, parentValueContext, requestInfo){
				//build context element first
				var valueStructureElementInfosArray = [];
				
				_.each(valueStructureDef, function(valueStructureDefRootObj, eleName){
					var valueStructureDefRootEle = valueStructureDefRootObj[node_COMMONATRIBUTECONSTANT.ROOTSTRUCTURE_DEFINITION];
					
					var info = {
						matchers : valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_MATCHERS],
						reverseMatchers : valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_REVERSEMATCHERS]
					};
					var type = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_TYPE];
					var valueStructureInfo = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
					//if context.info.instantiate===manual, context does not need to create in the framework
					if(valueStructureInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE]!=node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE_MANUAL){
						if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE && 
								valueStructureInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION]!=node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_LOGICAL){
							//physical relative
							if(valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_PATH][node_COMMONATRIBUTECONSTANT.INFOPATHREFERENCE_PARENT]==node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT){
		//					if(contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_ISTOPARENT]==true){
								//process relative that  refer to element in parent context
								var pathObj = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_RESOLVEDPATH];
								var rootName = pathObj[node_COMMONATRIBUTECONSTANT.COMPLEXPATH_ROOT];
								var path = pathObj[node_COMMONATRIBUTECONSTANT.COMPLEXPATH_PATH];
								valueStructureElementInfosArray.push(node_createValueStructureElementInfo(eleName, parentValueContext, node_createValueStructureVariableRef(rootName, path), undefined, info));
							}
						}
						else{
							//not relative or logical relative variable
							var defaultValue = valueStructureDefRootObj[node_COMMONATRIBUTECONSTANT.ROOTSTRUCTURE_DEFAULT];
							
							var criteria;
							if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE)	criteria = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DEFINITION][node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DATA];
							else  criteria = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DATA]; 
							if(criteria!=undefined){
								//app data, if no default, empty variable with wrapper type
								if(defaultValue!=undefined) 	valueStructureElementInfosArray.push(node_createContextElementInfo(eleName, node_dataUtility.createDataOfAppData(defaultValue), "", undefined, info));
								else  valueStructureElementInfosArray.push(node_createContextElementInfo(eleName, undefined, node_CONSTANT.DATA_TYPE_APPDATA, undefined, info));
							}
							else{
								//object, if no default, empty variable with wrapper type
								if(defaultValue!=undefined)		valueStructureElementInfosArray.push(node_createContextElementInfo(eleName, defaultValue, "", undefined, info));
								else valueStructureElementInfosArray.push(node_createContextElementInfo(eleName, undefined, node_CONSTANT.DATA_TYPE_OBJECT, undefined, info));
							}
						}
					}
				});	
					
				var valueStructure = node_createValueStructure(id, valueStructureElementInfosArray, requestInfo);
		
				//for relative which refer to context ele in same context
				_.each(valueStructureDef, function(valueStructureDefRootObj, eleName){
					var valueStructuretDefRootEle = valueStructureDefRootObj[node_COMMONATRIBUTECONSTANT.ROOTSTRUCTURE_DEFINITION];
					var info = {
							matchers : valueStructuretDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_MATCHERS],
							reverseMatchers : valueStructuretDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_REVERSEMATCHERS]
					};
					var type = valueStructuretDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_TYPE];
					var valueStructureInfo = valueStructureDefRootObj[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
					//if context.info.instantiate===manual, context does not need to create in the framework
					if(valueStructureInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE]!=node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE_MANUAL){
						if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE && valueStructuretDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_PARENT]==node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_SELF){
		//				if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE && contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_ISTOPARENT]==false){
							var pathObj = valueStructuretDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_PATH];
							var rootName = pathObj[node_COMMONATRIBUTECONSTANT.COMPLEXPATH_ROOT];
							var path = pathObj[node_COMMONATRIBUTECONSTANT.COMPLEXPATH_PATH];
							//only process element that parent is created
							if(valueStructure.getContextElement(rootName)!=undefined){
								valueStructure.addContextElement(node_createValueStructureElementInfo(eleName, context, node_createValueStructureVariableRef(rootName, path), undefined, info));
							}
						}
					}
				});	
				
				return context;
			},
			
			
			
			
			
			
			
			
			
			
			
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
				setRequest.addRequest(eleName, ele.variable.getDataOperationRequest(node_valueInVarOperationServiceUtility.createGetOperationService()));
//				var eleNameInfo = loc_out.parseContextElementName(eleName);
//				if(eleNameInfo.categary==undefined){
//					setRequest.addRequest(eleName, ele.variable.getDataOperationRequest(node_valueInVarOperationServiceUtility.createGetOperationService()));
//				}
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
						calContextValue.addRequest(eleName, contextItem.variable.getDataOperationRequest(node_valueInVarOperationServiceUtility.createGetOperationService()));
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
					calContextValue.addRequest(eleName, contextItem.variable.getDataOperationRequest(node_valueInVarOperationServiceUtility.createGetOperationService()));
				}
			});
			
			out.addRequest(calContextValue);
			return out;
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
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.valueInVarOperationServiceUtility", function(){node_valueInVarOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.aliasUtility", function(){node_aliasUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility1111", node_utility); 

})(packageObj);
