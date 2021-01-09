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
	var node_uiResourceViewFactory;
	var node_createEventObject;
	var node_contextUtility;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;
	var node_getObjectType;
	var node_resourceUtility;
	var node_createUITagOnBaseSimple;
	var node_createUITagOnBaseArray;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 * base customer tag object, child tag just provide extendObj which implements its own method 
 * it is also constructor object for customer tag object
 * 		uiTagResourceObj: definition of this tag  
 * 		id: 	id for this tag
 * 		attributeValues: values for tag attributes
 * 		parentContext : variable context from parent
 * 		tagConfigure : configure infor for generate tag, for instance, mode, name mapping, event mapping, variable matcher
 * 		tagBody : body in tag, it maybe uiresoruce or story node
 */
var node_createUITag = function(uiTagResourceObj, id, attributeValues, parentContext, tagConfigure, tagBody){
	
	var loc_uiTagResourceObj = uiTagResourceObj;   //tag definition from resource
	var loc_tagName = uiTagResourceObj.name;    //tag name
	var loc_tagConfigure = tagConfigure;     //
	var loc_mode = loc_tagConfigure.mode;
	var loc_rootView = tagConfigure.rootView;
	var loc_tagBody = tagBody;               //
	var loc_parentContext = parentContext;
	var loc_attributeDefinition = {};
	
	var loc_viewContainer = loc_tagConfigure.viewContainer;
	
	var loc_uiTagObj;
	
	var loc_tagEventObject = node_createEventObject();
	var loc_eventObject = node_createEventObject();
	
	//id of this tag object
	var loc_id = id;
	//all tag attributes
	var loc_attributes = {};
	//init all attributes value
	_.each(attributeValues, function(attrValue, attribute, list){
		loc_attributes[attribute] = attrValue;
	});
	
	var loc_context;
	
	//related name: name, name with categary
	var loc_getRelatedName = function(name){
		var out = [];
		out.push(name);
		var mappedName;
		if(loc_tagConfigure.varNameMapping!=undefined) mappedName = loc_tagConfigure.varNameMapping[name];
		if(mappedName!=undefined)  out.push(mappedName);
		return out;
	};

	//exContext extra context element used when create context for tag resource
	var loc_createContextForTagBody = function(exContext){
		var contextId = "TagContent_"+loc_tagName;
		var out;
		if(loc_mode==node_CONSTANT.TAG_RUNTIME_MODE_PAGE){
			if(exContext==undefined)   exContext = loc_context;
			out = node_contextUtility.buildContext(
					contextId, 
					loc_tagConfigure.bodyContextDef, 
					exContext);
		}
		else if(loc_mode==node_CONSTANT.TAG_RUNTIME_MODE_DEMO){
			out = node_createContext(contextId, []);
		}
		return out;
	};
	
	var loc_processChildUIViewEvent = function(eventName, eventData, requestInfo){
		var en = loc_tagConfigure.eventNameMapping[eventName];
		if(en==undefined)  en = eventName;
		loc_eventObject.triggerEvent(en, eventData, requestInfo);
	};
	
//	var loc_getStartElement = function(){   return loc_tagConfigure.startElement;     };
//	var loc_getEndElement = function(){   return loc_tagConfigure.endElement;     };
	var loc_getStartElement = function(){   return loc_viewContainer.getStartElement();     };
	var loc_getEndElement = function(){   return loc_viewContainer.getEndElement();     };

	var loc_getAttributeValue = function(name){  
		var out = loc_attributes[name];
		if(out==undefined){
			var attrDef = loc_attributeDefinition[name];
			if(attrDef!=undefined)	out = attrDef[node_COMMONATRIBUTECONSTANT.UITAGDEFINITIONATTRIBUTE_DEFAULTVALUE];
		}
		return out;
	};

	//runtime env for uiTagObj
	//include : basic info, utility method
	var loc_envObj = {
		getId : function(){  return loc_id;  },
		getContext : function(){   return loc_context;  },
		getAttributeValue : function(name){  return loc_getAttributeValue(name); },
		getAttributes : function(){   return loc_attributes;   },
		getTagBody : function(){  return  loc_tagBody; },
		
		getTags : function(query){   return loc_rootView.getTags(query);  },

		getStartElement : function(){  return loc_getStartElement();  },
		getEndElement : function(){  return loc_getEndElement();  },
		
		//utility methods
		createVariable : function(fullPath){  return loc_context.createVariable(node_createContextVariableInfo(fullPath));  },
		processRequest : function(requestInfo){   node_requestServiceProcessor.processRequest(requestInfo);  },
		
		//---------------------------------ui resource view
		getCreateUIViewWithIdRequest : function(id, context, handlers, requestInfo){
			if(loc_mode==node_CONSTANT.TAG_RUNTIME_MODE_PAGE){
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUIViewWithId", {}), handlers, requestInfo);
				out.addRequest(node_uiResourceViewFactory.getCreateUIBodyViewRequest(loc_tagConfigure.resource, loc_tagBody, {}, id, loc_tagConfigure.parentResourceView, context, {
					success : function(request, uiBodyView){
						uiBodyView.registerEventListener(loc_eventObject, loc_processChildUIViewEvent, loc_out);
						return uiBodyView;
					}
				}, requestInfo));
				return out;
			}
			else if(loc_mode==node_CONSTANT.TAG_RUNTIME_MODE_DEMO){
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUINodeWithId", {}), handlers, requestInfo);
				out.addRequest(node_uiNodeViewFactory.getCreateUINodeViewRequest(loc_tagBody.getChildren(), id, context, undefined, {
					success : function(request, uiBodyView){
						return uiBodyView;
					}
				}));
				return out;
			}
		},

		getCreateDefaultUIViewRequest : function(handlers, requestInfo){
			return this.getCreateUIViewWithIdRequest(loc_id, loc_createContextForTagBody(), handlers, requestInfo);
		},
		
		//---------------------------------context definition
		getTagContextElementDefinition : function(name){
			if(loc_mode==node_CONSTANT.TAG_RUNTIME_MODE_PAGE){
				return loc_tagConfigure.contextDef[name];
			}			
			else if(loc_mode==node_CONSTANT.TAG_RUNTIME_MODE_DEMO){
				return node_contextUtility.getContextElementDefinitionFromFlatContext(uiTagResourceObj[node_COMMONATRIBUTECONSTANT.UITAGDEFINITION_FLATCONTEXT], name);
			}
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
			var context = loc_createContextForTagBody(extendedContext);
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
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(handlers, requestInfo){
		_.each(loc_uiTagResourceObj[node_COMMONATRIBUTECONSTANT.UITAGDEFINITION_ATTRIBUTES], function(attDef, i){
			loc_attributeDefinition[attDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME]] = attDef;
		});
		
		var uiTagCore;
		var uiTagBase = loc_uiTagResourceObj[node_COMMONATRIBUTECONSTANT.UITAGDEFINITION_BASE];
		if(uiTagBase==undefined){
			uiTagCore = loc_uiTagResourceObj[node_COMMONATRIBUTECONSTANT.UITAGDEFINITION_SCRIPT].call(loc_out, loc_envObj);
		}
		else if(uiTagBase=="simpleData"){
			uiTagCore = node_createUITagOnBaseSimple(loc_envObj, loc_uiTagResourceObj);
		}
		else if(uiTagBase=="arrayData"){
			uiTagCore = node_createUITagOnBaseArray(loc_envObj, loc_uiTagResourceObj);
		}
		
		
		loc_uiTagObj = _.extend({
			findFunctionDown : function(name){},	
			created : function(){},
			preInit : function(request){},
			initViews : function(request){},
			postInit : function(request){},
			destroy : function(request){},
			getChildUITags : function(){},
			getValidateDataRequest : function(handlers, request){},
			createContextForDemo : function(id, parentContext){},
		}, uiTagCore);

		loc_uiTagObj.created();

		//create context
		if(loc_mode==node_CONSTANT.TAG_RUNTIME_MODE_PAGE){
			loc_context = node_contextUtility.buildContext(
					"Tag_"+loc_tagName,
					loc_tagConfigure.contextDef, 
					loc_parentContext);
		}
		else if(loc_mode==node_CONSTANT.TAG_RUNTIME_MODE_DEMO){
			loc_context = loc_uiTagObj.createContextForDemo("Tag_"+loc_tagName, loc_parentContext, loc_tagConfigure.matchers);
		}

		var uiTagInitRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("UITagInit"), handlers, requestInfo);
		
		//overriden method before view is attatched to dom
		if(loc_uiTagObj.preInit!=undefined){
			var initObj = loc_uiTagObj.preInit(requestInfo);
			if(initObj!=undefined && node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==node_getObjectType(initObj)){
				uiTagInitRequest.addRequest(initObj);
			}
		}
		
		//overridden method to create init view
		if(loc_uiTagObj.initViews!=undefined){
			uiTagInitRequest.addRequest(node_createServiceRequestInfoSimple(undefined, function(requestInfo){
				var views = loc_uiTagObj.initViews(requestInfo);
				//attach view to resourve view
				if(views!=undefined){
//					loc_getStartElement().after(views);
					loc_viewContainer.setContentView(views);
				}

				//overridden method to do sth after view is attatched to dom
				if(loc_uiTagObj.postInit!=undefined){
					var postInitObj = loc_uiTagObj.postInit(requestInfo);
					if(postInitObj!=undefined && node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==node_getObjectType(postInitObj)){
						return postInitObj;
					}
				}
			}));
		}

		return uiTagInitRequest;
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY]  = function(){
		loc_uiTagObj.destroy();
		if(loc_context!=undefined)  loc_context.destroy();
	};
	
	var loc_out = {
		prv_getEnvObj : function(){  return loc_envObj;  },
		prv_setUITagObj : function(uiTagObj){  loc_uiTagObj = uiTagObj;   },
//		prv_getStartElement : function(){  return loc_startEle;  },

		getId : function(){  return loc_id;   },
		
		getTagName : function(){ return loc_tagName;   },
	
		getViewContainer : function(){   return loc_viewContainer;     },
		
		getTagObject : function(){ return loc_uiTagObj;  },
		
		getChildUITags : function(){  
			var out = loc_uiTagObj.getChildUITags();
			if(out!=undefined)  return out;
			else return [];
		},
		
		getValidateDataRequest : function(handlers, request){  return loc_uiTagObj.getValidateDataRequest(handlers, request);     },
		
		destroy : function(requestInfo){	node_getLifecycleInterface(loc_out).destroy(requestInfo);	},
		
		getAttributeValue : function(name){   return loc_getAttributeValue(name);      },
		setAttribute : function(name, value){
			if(loc_uiTagObj.processAttribute!=undefined)  loc_uiTagObj.processAttribute(name, value);  
		},
		
		registerTagEventListener : function(eventName, handler, thisContext){	return loc_tagEventObject.registerListener(eventName, undefined, handler, thisContext);	},
		registerEventListener : function(listener, handler, thisContext){	return loc_eventObject.registerListener(undefined, listener, handler, thisContext);	},
		
	};
	
	//append resource and object life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_UITAG);

//	node_getLifecycleInterface(loc_out).init(uiTagResourceObj, id, attributeValues, parentContext, tagConfigure, tagBody);
	
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
nosliw.registerSetNodeDataEvent("uipage.uiResourceViewFactory", function(){node_uiResourceViewFactory = this.getData();});
nosliw.registerSetNodeDataEvent("uinode.uiNodeViewFactory", function(){node_uiNodeViewFactory = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uitag.createUITagOnBaseSimple", function(){node_createUITagOnBaseSimple = this.getData();});
nosliw.registerSetNodeDataEvent("uitag.createUITagOnBaseArray", function(){node_createUITagOnBaseArray = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUITag", node_createUITag); 

})(packageObj);
