//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
	var node_makeObjectWithType;
	var node_createContext;
	var node_createContextElementInfo;
	var node_createContextElement;
	var node_createExtendedContext;
	var node_dataUtility;
	var node_uiResourceUtility;
	var node_createEmbededScriptExpressionInContent;
	var node_createEmbededScriptExpressionInAttribute;
	var node_getLifecycleInterface;
	var node_basicUtility;
	var node_createContextVariableInfo;
	var node_requestServiceProcessor;
	var node_createUIDataOperationRequest;
	var node_UIDataOperation;
	var node_uiDataOperationServiceUtility;
	var node_createBatchUIDataOperationRequest;
	var node_createUIViewFactory;
	var node_createEventObject;
	var node_contextUtility;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;
	var node_getObjectType;
	var node_resourceUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUITagRequest = function(id, uiTagResource, parentUIResourceView, handlers, requestInfo){
	var uiTag = node_createUITag(id, uiTagResource, parentUIResourceView);
	
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUITag", {}), handlers, requestInfo);

	var createUITagRequest = node_createServiceRequestInfoSequence(undefined);
	var tagId = uiTagResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_TAGNAME];
//	tagId = node_resourceUtility.buildReourceCoreIdLiterate(tagId);
	createUITagRequest.addRequest(nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([tagId], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UITAG, {
		success : function(requestInfo, resources){
			var uiTagResourceObj = resources[tagId];

			var uiTagObj = _.extend({
				findFunctionDown : function(name){},	
				initViews : function(requestInfo){},
				postInit : function(){},
				preInit : function(){},
				destroy : function(){},
			}, uiTagResourceObj[node_COMMONATRIBUTECONSTANT.UITAGDEFINITION_SCRIPT].call(uiTag, uiTag.prv_getEnvObj()));
			uiTag.prv_setUITagObj(uiTagObj);
			
			var uiTagInitRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("UITagInit"));
			
			//overriden method before view is attatched to dom
			if(uiTagObj.preInit!=undefined){
				var initObj = uiTagObj.preInit(requestInfo);
				if(initObj!=undefined && node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==node_getObjectType(initObj)){
					uiTagInitRequest.addRequest(initObj);
				}
			}
			
			//overridden method to create init view
			if(uiTagObj.initViews!=undefined){
				var views = uiTagObj.initViews(requestInfo);
				//attach view to resourve view
				if(views!=undefined)  uiTag.prv_getStartElement().after(views);	
			}

			//overridden method to do sth after view is attatched to dom
			if(uiTagObj.postInit!=undefined){
				var postInitObj = uiTagObj.postInit(requestInfo);
				if(postInitObj!=undefined && node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==node_getObjectType(postInitObj)){
					uiTagInitRequest.addRequest(postInitObj);
				}
			}
			
			return uiTagInitRequest;
		}
	}));

	out.addRequest(createUITagRequest);
	out.addRequest(node_createServiceRequestInfoSimple(undefined, function(requestInfo){
		return uiTag;
	}));
	return out;
};
	
	
	
	/**
	 * 
	 * base customer tag object, child tag just provide extendObj which implements its own method 
	 * it is also constructor object for customer tag object  
	 * 		id: 	id for this tag
	 * 		uiTagResource:	ui tag resource 
	 * 		parentUIResourceView: 	parent ui resource view
	 */
var node_createUITag = function(id, uiTagResource, parentUIResourceView){
	//object to implement tag logic, it is from tag library
	var loc_uiTagObj;
	
	//id of this tag object
	var loc_id = id;
	//ui resource definition
	var loc_uiTagResource = uiTagResource;
	//parent resource view
	var loc_parentResourceView = parentUIResourceView;
	//all tag attributes
	var loc_attributes = {};

	var loc_tagName = uiTagResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_TAGNAME];
	var loc_varNameMapping = uiTagResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_TAGCONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_LOCAL2GLOBAL];
	
	var loc_eventNameMapping = uiTagResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_EVENTMAPPING];
	
	var loc_context;
	
	//boundary element for this tag
	var loc_startEle = undefined;
	var loc_endEle = undefined;
	
	var loc_tagEventObject = node_createEventObject();
	var loc_eventObject = node_createEventObject();
	
	//get wraper element
	loc_startEle = loc_parentResourceView.get$EleByUIId(loc_id+node_COMMONCONSTANT.UIRESOURCE_CUSTOMTAG_WRAPER_START_POSTFIX);
	loc_endEle = loc_parentResourceView.get$EleByUIId(loc_id+node_COMMONCONSTANT.UIRESOURCE_CUSTOMTAG_WRAPER_END_POSTFIX);
	
	//init all attributes value
	_.each(uiTagResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_ATTRIBUTES], function(attrValue, attribute, list){
		loc_attributes[attribute] = attrValue;
	});
	
	//create context
	var parentContext;
	if(parentUIResourceView!=undefined)   parentContext = parentUIResourceView.getContext();
	loc_context = node_contextUtility.buildContext(
			"Tag_"+loc_tagName,
			uiTagResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_TAGCONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXT_ELEMENT], 
			parentContext);
	
	
	//related name: name, name with categary
	var loc_getRelatedName = function(name){
		var out = [];
		out.push(name);
		var mappedName = loc_varNameMapping[name];
		if(mappedName!=undefined)  out.push(mappedName);
		return out;
	};
	
	//exContext extra context element used when create context for tag resource
	var loc_createContextForTagResource = function(exContext){
		if(exContext==undefined)   exContext = loc_context;
		var context = node_contextUtility.buildContext("TagContent_"+loc_tagName, loc_uiTagResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXT_ELEMENT], exContext);
		return context;
	};
	
	var loc_processChildUIViewEvent = function(eventName, eventData, requestInfo){
		var en = loc_eventNameMapping[eventName];
		if(en==undefined)  en = eventName;
		loc_eventObject.triggerEvent(en, eventData, requestInfo);
	};
	
	
	//runtime env for uiTagObj
	//include : basic info, utility method
	var loc_envObj = {
		getId : function(){  return loc_id;  },
		getStartElement : function(){  return loc_startEle;  },
		getEneElement : function(){  return loc_endEle;  },
		getContext : function(){   return loc_context;  },
		getAttributeValue : function(name){  return loc_attributes[name];  },
		getAttributes : function(){   return loc_attributes;   },
		getParentResourceView : function(){ return loc_parentResourceView;  },
		getUIResource : function(){  return  loc_uiTagResource; },
		
		//utility methods
		createVariable : function(fullPath){  return loc_context.createVariable(node_createContextVariableInfo(fullPath));  },
		processRequest : function(requestInfo){   node_requestServiceProcessor.processRequest(requestInfo);  },
		
		//---------------------------------ui resource view
		getCreateUIViewWithIdRequest : function(id, context, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUIViewWithId", {}), handlers, requestInfo);
			out.addRequest(node_createUIViewFactory().getCreateUIViewRequest(loc_uiTagResource, id, loc_parentResourceView, context, {
				success : function(request, uiView){
					uiView.registerEventListener(loc_eventObject, loc_processChildUIViewEvent, loc_out);
				}
			}, requestInfo));
			return out;
		},

		getCreateDefaultUIViewRequest : function(handlers, requestInfo){
			return this.getCreateUIViewWithIdRequest(loc_id, loc_createContextForTagResource(), handlers, requestInfo);
		},
		
		//---------------------------------build context
		createContextElementInfo : function(name, data1, data2, adapterInfo, info){
			var out = [];
			_.each(loc_getRelatedName(name), function(name, index){
				out.push(node_createContextElementInfo(name, data1, data2, adapterInfo, info));  
			});
			return out;
		},
		createContextElementInfoFromContext : function(name, contextEle, path){	 
			return node_createContextElementInfo(name, loc_context, node_createContextVariableInfo(contextEle, path));	
		},
		
		//create extended ui tag resource context : 
		createExtendedContext : function(extendedEleInfos, requestInfo){
			var extendedVarEles = {};
			_.each(extendedEleInfos, function(eleInfo, index){
				if(!Array.isArray(eleInfo))			extendedVarEles[eleInfo.name] = node_createContextElement(eleInfo);
				else{
					_.each(eleInfo, function(eleInfo){
						extendedVarEles[eleInfo.name] = node_createContextElement(eleInfo);
					});
				}
			});
			var extendedContext = node_createExtendedContext(loc_context, extendedVarEles);
			var context = loc_createContextForTagResource(extendedContext);
			
			
//			var context = loc_createContextForTagResource();
//			_.each(extendedEleInfos, function(eleInfo, index){
//				context.addContextElement(eleInfo);
//			});
			return context;
		},
		
		//---------------------------------operation request
		getDataOperationGet : function(target, path){  return new node_UIDataOperation(target, node_uiDataOperationServiceUtility.createGetOperationService(path)); },
		getDataOperationRequestGet : function(target, path, handler, request){	return node_createUIDataOperationRequest(loc_context, this.getDataOperationGet(target, path), handler, request);	},
		executeDataOperationRequestGet : function(target, path, handler, request){			return this.processRequest(this.getDataOperationRequestGet(target, path, handler, request));		},

		getDataOperationSet : function(target, path, value){  return new node_UIDataOperation(target, node_uiDataOperationServiceUtility.createSetOperationService(path, value)); },
		getDataOperationRequestSet : function(target, path, value, handler, request){	return node_createUIDataOperationRequest(loc_context, this.getDataOperationSet(target, path, value), handler, request);	},
		executeDataOperationRequestSet : function(target, path, value, handler, request){	return this.processRequest(this.getDataOperationRequestSet(target, path, value, handler, request));	},

		createHandleEachElementProcessor : function(name, path){  return this.getContext().createHandleEachElementProcessor(name, path);  },
		
		getBatchDataOperationRequest : function(operations, handlers, request){
			var requestInfo = node_createBatchUIDataOperationRequest(loc_context, handlers, request);
			_.each(operations, function(operation, i){
				requestInfo.addUIDataOperation(operation);						
			});
			return requestInfo;
		},
		executeBatchDataOperationRequest : function(operations, handlers, request){		this.processRequest(this.getBatchDataOperationRequest(operations, handlers, request));		},
		
		//---------------------------------
		getExecuteOperationRequest : function(dataTypeId, operation, parmsArray, handlers, requester_parent){  return nosliw.runtime.getExpressionService().getExecuteOperationRequest(dataTypeId, operation, parmsArray, handlers, requester_parent)  },
		executeExecuteOperationRequest : function(dataTypeId, operation, parmsArray, handlers, requester_parent){
			this.processRequest(this.getExecuteOperationRequest(dataTypeId, operation, parmsArray, handlers, requester_parent));
		},
		
		//---------------------------------other request
		getGatewayCommandRequest : function(gatewayId, command, parms, handlers, requestInfo){	return nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms, handlers, requestInfo);	},
		executeGatewayCommandRequest : function(gatewayId, command, parms, handlers, requestInfo){	return nosliw.runtime.getGatewayService().executeExecuteGatewayCommandRequest(gatewayId, command, parms, handlers, requestInfo);	},
		executeGatewayCommand : function(gatewayId, command, parms, handlers, requestInfo){	return nosliw.runtime.getGatewayService().executeGatewayCommand(gatewayId, command, parms, handlers, requestInfo);	},
	
		//--------------------------------- event
		trigueEvent : function(event, eventData, requestInfo){   loc_tagEventObject.triggerEvent(event, eventData, requestInfo);  },
	
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(id, uiTagResource, parentUIResourceView){
		
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY]  = function(){
		loc_uiTagObj.destroy();
		if(loc_context!=undefined)  loc_context.destroy();
	};
	
	var loc_out = {
		prv_getEnvObj : function(){  return loc_envObj;  },
		prv_setUITagObj : function(uiTagObj){  loc_uiTagObj = uiTagObj;   },
		prv_getStartElement : function(){  return loc_startEle;  },

		getId : function(){  return loc_id;   },
		
		getTagName : function(){ return loc_tagName;   },
	
		getTagObject : function(){ return loc_uiTagObj;  },
		
		destroy : function(requestInfo){	node_getLifecycleInterface(loc_out).destroy(requestInfo);	},
		
		setAttribute : function(name, value){
			if(loc_uiTagObj.processAttribute!=undefined)  loc_uiTagObj.processAttribute(name, value);  
		},
		
		registerTagEventListener : function(eventName, handler, thisContext){	return loc_tagEventObject.registerListener(eventName, undefined, handler, thisContext);	},
		registerEventListener : function(listener, handler, thisContext){	return loc_eventObject.registerListener(undefined, listener, handler, thisContext);	},
		
	};
	
	//append resource and object life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_UITAG);

	node_getLifecycleInterface(loc_out).init(id, uiTagResource, parentUIResourceView);
	
	return loc_out;
	
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContext", function(){node_createContext = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElement", function(){node_createContextElement = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createExtendedContext", function(){node_createExtendedContext = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.utility", function(){node_uiResourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createEmbededScriptExpressionInContent", function(){node_createEmbededScriptExpressionInContent = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createEmbededScriptExpressionInAttribute", function(){node_createEmbededScriptExpressionInAttribute = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextVariableInfo", function(){node_createContextVariableInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.createUIDataOperationRequest", function(){node_createUIDataOperationRequest = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.UIDataOperation", function(){node_UIDataOperation = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.createBatchUIDataOperationRequest", function(){node_createBatchUIDataOperationRequest  = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createUIViewFactory", function(){node_createUIViewFactory = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUITagRequest1", node_createUITagRequest); 

})(packageObj);

