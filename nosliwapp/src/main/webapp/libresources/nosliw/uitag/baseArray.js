//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
	var node_getLifecycleInterface;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;
	var node_createHandleEachElementProcessor;
	var node_uiDataOperationServiceUtility;
	var node_requestServiceProcessor;
	var node_namingConvensionUtility;
	var node_createContextVariable;
	var node_dataUtility;

//*******************************************   Start Node Definition  ************************************** 	

var node_createUITagOnBaseArray = function(env, uiTagDef){
	
	var loc_env = env;
	var loc_uiTagDef = uiTagDef;
	var loc_coreObj;
	
	//container data variable
	var loc_containerVariable;

	var loc_childVaraibles = [];

	var loc_id = 0;

	var loc_handleEachElementProcessor;
	
	var loc_generateId = function(){
		loc_id++;
		return loc_id+"";
	};
	
	var loc_getElementContextVariable = function(key){
		var out = node_createContextVariable(loc_dataContextEleName);
		out.path = node_namingConvensionUtility.cascadePath(out.path, key+"");
		return out;
	};
	
	var loc_getElementVariableName = function(){   return loc_env.getAttributeValue("element"); 	};
	
	var loc_getIndexVariableName = function(){   return loc_env.getAttributeValue("index");  };

	var loc_getUpdateViewRequest = function(handlers, requestInfo){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);

		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(requestInfo){
			for(var i in loc_childVaraibles){
				loc_out.prv_deleteEle(0, requestInfo);
			}
		}));

		out.addRequest(loc_handleEachElementProcessor.getLoopRequest({
			success : function(requestInfo, eles){
				var addEleRequest = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
				_.each(eles, function(ele, index){
					addEleRequest.addRequest(loc_getAddEleRequest(ele.elementVar, ele.indexVar, index));
				});
				addEleRequest.setParmData("processMode", "promiseBased");
				return addEleRequest;
			}
		}));
		
		return out;
	};
	
	/**
	*  eleVar : variable for element
	*  indexVar : index variable for index of element
	*  path : element's path from parent
	**/
	var loc_getAddEleRequest = function(eleVar, indexVar, index, handlers, requestInfo){
		var variables = {};
		variables[loc_getElementVariableName()] = eleVar;
		variables[loc_getIndexVariableName()] = indexVar;
		
		var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
		out.addRequest(loc_coreObj.getCreateElementViewRequest(loc_env.getId()+"."+loc_generateId(), index, variables, {
			success : function(requestInfo, view){
				loc_childVaraibles.splice(index, 0, variables);
			}
		}));
		return out;
	};

	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(env, uiTagDef, handlers, requestInfo){
	};

	var loc_createCoreObj = function(){
		loc_coreObj = _.extend({
			initViews : function(requestInfo){},
			updateView : function(data, request){},
			getViewData : function(){},
			getDataForDemo : function(){},
			destroy : function(request){},
			getCreateElementViewRequest : function(id, index, variables, handlers, request){},
			deleteElement : function(index, requestInfo){}, 
			
		}, loc_uiTagDef[node_COMMONATRIBUTECONSTANT.UITAGDEFINITION_SCRIPT].call(loc_out, loc_baseObj));
	};

	var loc_baseObj = {
		getEnv : function(){   return loc_env;   },
		getElementVariableName : function(){    return loc_getElementVariableName();    },
		getIndexVariableName : function(){  return loc_getIndexVariableName();   },
	};

	var loc_out = {
			prv_deleteEle : function(index, requestInfo){
				loc_coreObj.deleteElement(index, requestInfo);
				var variables = loc_childVaraibles[index];
				variables[loc_getElementVariableName()].release(requestInfo);
				variables[loc_getIndexVariableName()].release(requestInfo);
				loc_childVaraibles.splice(index, 1);
			},
			
			created : function(){
				loc_createCoreObj();
			},
			preInit : function(request){
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("uiTagPreInitRequest", {}), undefined, request);
				loc_containerVariable = loc_env.createVariable("internal_data");
//				out.addRequest(loc_processDataRuleRequest());
				return out;
			},
			initViews : function(handlers, request){
				return loc_coreObj.initViews(handlers, request);
			},
			postInit : function(requestInfo){
				loc_handleEachElementProcessor = node_createHandleEachElementProcessor(loc_containerVariable, ""); 
				loc_handleEachElementProcessor.registerEventListener(undefined, function(event, eventData, requestInfo){
					if(event=="EACHELEMENTCONTAINER_EVENT_RESET"){
						node_requestServiceProcessor.processRequest(loc_getUpdateViewRequest(undefined, requestInfo));
					}
					else if(event=="EACHELEMENTCONTAINER_EVENT_NEWELEMENT"){
						var req = node_createServiceRequestInfoSequence(undefined, {}, requestInfo);
						req.addRequest(eventData.indexVar.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(""), {
							success : function(request, data){
								return loc_getAddEleRequest(eventData.elementVar, eventData.indexVar, data.value.getValue());
							}
						}));
						node_requestServiceProcessor.processRequest(req);
					}
					else if(event=="EACHELEMENTCONTAINER_EVENT_DELETEELEMENT"){
						eventData.executeDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(""), {
							success : function(request, data){
								loc_out.prv_deleteEle(node_dataUtility.getValueOfData(data), request);
							}
						}, requestInfo);
					}
				});
				
				return loc_getUpdateViewRequest(undefined, requestInfo);
			},
			destroy : function(request){
				loc_containerVariable.release();	
				loc_coreObj.destroy();
				loc_handleEachElementProcessor.destroy(request);
			},
			createContextForDemo : function(id, parentContext, matchersByName, request){
				var node_createData = nosliw.getNodeData("variable.data.entity.createData");
				var node_createContextElementInfo = nosliw.getNodeData("variable.context.createContextElementInfo");
				var node_createContext = nosliw.getNodeData("variable.context.createContext");
				
				var dataVarPar;
				if(parentContext!=undefined)	dataVarPar = parentContext.getContextElement("data");
				var dataVarEleInfo = undefined;
				if(dataVarPar!=undefined){
					var matchersCombo = matchersByName==undefined?{}:matchersByName["internal_data"];
					var info;
					if(matchersCombo!=undefined){
						info = {
								matchers : matchersCombo[node_COMMONATRIBUTECONSTANT.MATCHERSCOMBO_MATCHERS],
								reverseMatchers : matchersCombo[node_COMMONATRIBUTECONSTANT.MATCHERSCOMBO_REVERSEMATCHERS]
						};
					}
					dataVarEleInfo = node_createContextElementInfo("internal_data", dataVarPar, undefined, undefined, info);
				}
				else{
					var data = node_createData(loc_coreObj.getDataForDemo(), node_CONSTANT.WRAPPER_TYPE_APPDATA);
					dataVarEleInfo = node_createContextElementInfo("internal_data", data);
				}
				
				var elementInfosArray = [dataVarEleInfo];
				return node_createContext(id, elementInfosArray, request);
			},

			
	};
	
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	node_getLifecycleInterface(loc_out).init(env, uiTagDef);
	
	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("variable.orderedcontainer.createHandleEachElementProcessor", function(){node_createHandleEachElementProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("variable.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("variable.context.createContextVariable", function(){node_createContextVariable = this.getData();});
nosliw.registerSetNodeDataEvent("variable.data.utility", function(){node_dataUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUITagOnBaseArray", node_createUITagOnBaseArray); 

})(packageObj);
