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
var node_utility = {
		//from flat context to context group
		getGetContextValueRequest : function(context, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("GetContextValue", {}), handlers, request);
			var calContextValue = node_createServiceRequestInfoSet(undefined, {
				success : function(request, resultSet){
					var value = {};
					_.each(resultSet.getResults(), function(eleValue, eleName){
						var segs = eleName.split("___");
						if(segs.length==2){
							var categary = segs[1];
							var name = segs[0];
							var categaryContext = value[categary];
							if(categaryContext==undefined){
								categaryContext = {};
								value[categary] = categaryContext;
							}
							categaryContext[name] = eleValue;
						}
					});
					return value;
				}
			});

			_.each(context.getElementsName(), function(eleName, i){
				calContextValue.addRequest(eleName, context.getContextElement(eleName).getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService()));
			});
			
			out.addRequest(calContextValue);
			return out;
		},
		
		//build context according to context definition and parent context
		buildContext : function(contextDef, parentContext, requestInfo){
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
						if(contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_ISTOPARENT]==true){
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
				
			var context = node_createContext(contextElementInfosArray, requestInfo);

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
					if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE && contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_ISTOPARENT]==false){
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
