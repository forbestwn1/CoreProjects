//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildComponentInterface;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_ResourceId;
	var node_resourceUtility;
	var node_componentUtility;
	var node_createServiceRequestInfoSimple;
	var node_expressionUtility;
	var node_makeObjectWithApplicationInterface;
	var node_createServiceRequestInfoSet;
	var node_createViewContainer;
	var node_uiContentUtility;
	var node_createEmbededScriptExpressionInContent;
	var node_createEmbededScriptExpressionInTagAttribute;
	var node_createEmbededScriptExpressionInCustomTagAttribute;
	var node_getLifecycleInterface;
	var node_basicUtility;
	var node_uiContentUtility;
	var node_getEntityTreeNodeInterface;
	var node_requestServiceProcessor;
	var node_complexEntityUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIContentPlugin = function(){
	
	var loc_out = {

		getCreateComplexEntityCoreRequest : function(complexEntityDef, valueContextId, bundleCore, configure, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return loc_createUIContentComponentCore(complexEntityDef, valueContextId, bundleCore, configure);
			}, handlers, request);
		}
	};

	return loc_out;
};

var loc_createUIContentComponentCore = function(complexEntityDef, valueContextId, bundleCore, configure){

	var loc_complexEntityDef = complexEntityDef;
	var loc_valueContextId = valueContextId;
	var loc_bundleCore = bundleCore;
	var loc_valueContext = loc_bundleCore.getVariableDomain().getValueContext(loc_valueContextId);
	var loc_envInterface = {};

	//object store all the functions for js block
	var loc_scriptObject = loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEXUICONTENT_SCRIPT);

	//all embeded script expression(in content, attribute, ...)
	var loc_expressionContents = [];
	
	//all events on regular elements
	var loc_elementEvents = [];
	
	//all events on custom tag elements
	var loc_customTagEvents = [];
	
	var loc_services;
	
	//view container
	var loc_viewContainer;

	//name space for this ui resource view
	//every element/customer tag have unique ui id within a web page
	//during compilation, ui id is unique within ui resoure, however, not guarenteed between different ui resource view within same web page
	//name space make sure of it as different ui resource view have different name space
	var loc_idNameSpace = nosliw.generateId();

	var loc_parentUIEntity;
	
	var loc_customerTagByUIId = {};

	/*
	 * init element event object
	 */
	var loc_initElementEvent = function(eleEvent){
		//get element for this event
		var ele = loc_getLocalElementByUIId(eleEvent[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_UIID]);
		var subEle = ele;
		//if have sel attribute set, then find sub element according to sel
		var selection = eleEvent[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_SELECTION];
		if(!node_basicUtility.isStringEmpty(selection))		subEle = ele.find(selection);

		//register event
		var eventValue = eleEvent;
		var eventName = eleEvent[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_EVENT];
		subEle.bind(eventName, function(event){
			var info = {
				event : event, 
				source : this,
			};
			event.preventDefault();
			node_uiContentUtility.callHandlerUp(loc_out, eventValue[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_FUNCTION], eventName, info);
		});
		
		return {
			source : subEle,
			event :  eventName,
		};
	};

	/*
	 * init element event object
	 */
	var loc_initCustomTagEvent = function(tagEvent){
		//get custom tag for this event
		var customTag = loc_customerTagByUIId[tagEvent[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_UIID]];
		var eventName = tagEvent[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_EVENT];
		
		var listener = customTag.registerTagEventListener(eventName, function(event, eventData, requestInfo){
			var info = {
				event : event,
				eventData : eventData,
				source : customTag,
				requestInfo: requestInfo,
			};
			node_uiContentUtility.callHandlerUp(loc_out, tagEvent[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_FUNCTION], eventName, info);
		});
		
		return {
			source : customTag,
			event :  eventName,
			listener: listener,
		};
	};

	/*
	 * update ui id by adding space name ahead of them
	 */
	var loc_getUpdateUIId = function(uiId){	return loc_idNameSpace+node_COMMONCONSTANT.SEPERATOR_FULLNAME+uiId;	};

	/*
	 * find matched elements according to selection
	 */
	var loc_findLocalElement = function(select){return loc_viewContainer.findElement(select);};

	/*
	 * find matched element according to uiid
	 */
	var loc_getLocalElementByUIId = function(id){return loc_findLocalElement("["+node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"='"+loc_getUpdateUIId(id)+"']");};

	var loc_findEntityLocally = function(entityType, entityName){
		var out;
		if(entityType==node_CONSTANT.UICONTENT_ENTITYTYPE_HANDLER){
	/*		
			//search task first
			var taskSuite;
			if(loc_tasks!=undefined){
				if(loc_tasks[node_COMMONATRIBUTECONSTANT.EXECUTABLETASKSUITE_TASK][handlerName]!=undefined){
					taskSuite = loc_tasks;
				}
			}
			if(taskSuite!=undefined){
				handlerInfo = {
					handlerSuite : taskSuite,
					handlerName : handlerName,
					handlerType : node_CONSTANT.HANDLER_TYPE_TASK,
					uiUnit : loc_out,
				};
			}
	*/
			
			if(out==null){
				//if not found in task, try to find in script function
				var fun = loc_scriptObject==undefined?undefined:loc_scriptObject[entityName];
				if(fun!=undefined){
					out = {
						handlerSuite : loc_scriptObject,
						handlerName : entityName,
						handlerType : node_CONSTANT.HANDLER_TYPE_SCRIPT,
					};
				}
			}
		}
		else if(entityType==node_CONSTANT.UICONTENT_ENTITYTYPE_SERVICE){
			if(loc_services!=undefined){
				var serviceChild = node_getEntityTreeNodeInterface(loc_services).getChild(entityName);
				if(serviceChild!=undefined){
					out = {
						parent : loc_services,
						serviceNode : serviceChild
					};
				}
			}
		}
		return out;
	};
		
	var loc_callScriptFunction = function(funName, args){
		var that = this;
		var fun = loc_scriptObject[funName];
		var env = {
			getInvokeServiceRequest : function(serviceName, adapterName, handlers, request){
				return node_uiContentUtility.getInvokeServiceRequest(loc_out, serviceName, adapterName, handlers, request);
			},
			executeGetInvokeServiceRequest : function(serviceName, handlers, request){
				node_requestServiceProcessor.processRequest(this.getInvokeServiceRequest(serviceName, handlers, request));
			}
		};
		if(args==undefined)  args = [];
		args.push(env);
		return fun.apply(this, args);
	};


	var loc_out = {
		
		setParentUIEntity : function(parentUIEntity){	loc_parentUIEntity = parentUIEntity;	},
		
		getParentUIEntity : function(){   return loc_parentUIEntity;     },
		
		findEntityLocally : function(entityType, entityName){  return loc_findEntityLocally(entityType, entityName);  },
		
//		findHandlerLocally : function(handlerName){   return loc_findHandlerLocally(handlerName);  },
		
		callScriptFunction : function(funName, args){   return loc_callScriptFunction(funName, args);     },
		
		
		setEnvironmentInterface : function(envInterface){	loc_envInterface = envInterface;	},
		
		getComplexEntityInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

			//content view			
			loc_viewContainer = node_createViewContainer(loc_idNameSpace);
			var html = _.unescape(loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEXUICONTENT_HTML));
			loc_viewContainer.setContentView(node_uiContentUtility.updateHtmlUIId(html, loc_idNameSpace));
			
			out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_COMPLEXENTITY].createAttributeRequest(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEX_SCRIPTEEXPRESSIONGROUP));

			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				//init expression in content
				_.each(loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEXUICONTENT_SCRIPTEXPRESSIONINCONTENT), function(embededContentDef, i){
					var embededContent = node_createEmbededScriptExpressionInContent(embededContentDef);
					var viewEle = loc_getLocalElementByUIId(embededContent.getUIId());
					
					var scriptGroupCore = loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].getChild(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEX_SCRIPTEEXPRESSIONGROUP).getChildValue().getCoreEntity();
					
					node_getLifecycleInterface(embededContent).init(viewEle, scriptGroupCore);
					loc_expressionContents.push(embededContent);
				});


				//init expression in regular tag attribute
				_.each(loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEXUICONTENT_SCRIPTEXPRESSIONINATTRIBUTE), function(embededContentDef, i){
					var embededContent = node_createEmbededScriptExpressionInTagAttribute(embededContentDef);
					var viewEle = loc_getLocalElementByUIId(embededContent.getUIId());
					
					var scriptGroupCore = loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].getChild(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEX_SCRIPTEEXPRESSIONGROUP).getChildValue().getCoreEntity();
					
					node_getLifecycleInterface(embededContent).init(viewEle, scriptGroupCore);
					loc_expressionContents.push(embededContent);
				});
				
				//init regular tag event
				_.each(loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEXUICONTENT_NORMALTAGEVENT), function(eleEvent, key, list){
					loc_elementEvents.push(loc_initElementEvent(eleEvent));
				});

			}));
			
			//init custom tag
			out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_COMPLEXENTITY].createAttributeRequest(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEXUICONTENT_CUSTOMERTAG, {
				success: function(request, attrNode){
					_.each(attrNode.getChildValue().getCoreEntity().getChildrenEntity(), function(child){
						var customTag = child.getCoreEntity();
						loc_customerTagByUIId[customTag.getUIId()] = customTag;
						customTag.setParentUIEntity(loc_out);
					});
				}
			}));

			//init service
			out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_COMPLEXENTITY].createAttributeRequest(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEXUICONTENT_SERVICE, {
				success: function(request, attrNode){
					loc_services = attrNode.getChildValue().getCoreEntity();
				}
			}));


			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				//init expression in custom tag attribute
				_.each(loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEXUICONTENT_SCRIPTEXPRESSIONINTAGATTRIBUTE), function(embededContentDef, i){
					var embededContent = node_createEmbededScriptExpressionInCustomTagAttribute(embededContentDef);
					var customTag = loc_customerTagByUIId[embededContent.getUIId()];
					var scriptGroupCore = loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].getChild(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEX_SCRIPTEEXPRESSIONGROUP).getChildValue().getCoreEntity();
					
					node_getLifecycleInterface(embededContent).init(customTag, scriptGroupCore);
					loc_expressionContents.push(embededContent);
				});

				//init regular tag event
				_.each(loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEXUICONTENT_CUSTOMTAGEVENT), function(eleEvent, key, list){
					loc_elementEvents.push(loc_initCustomTagEvent(eleEvent));
				});
			}));
			
			return out;
		},
		
		updateView : function(view){
			loc_viewContainer.appendTo(view);
			
			var customTagGroupCore = loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].getChild(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEXUICONTENT_CUSTOMERTAG).getChildValue().getCoreEntity();
			var customTagGroupCoreTreeNodeInterface = node_getEntityTreeNodeInterface(customTagGroupCore);
			var customTagKeys = customTagGroupCoreTreeNodeInterface.getChildrenName();
			_.each(customTagKeys, function(customTagKey, i){
				var uiCustomTag = customTagGroupCoreTreeNodeInterface.getChild(customTagKey).getChildValue().getCoreEntity();
				
				var uiId = loc_getUpdateUIId(uiCustomTag.getUIId());
				var tagWrapperView = $("<span>BBBBBBBBBBBBBBBBBBBBBB</span>");
				var tagPostfix = loc_getLocalElementByUIId(uiCustomTag.getUIId()+node_COMMONCONSTANT.UIRESOURCE_CUSTOMTAG_WRAPER_START_POSTFIX);
				tagWrapperView.insertAfter(tagPostfix);
				uiCustomTag.updateView(tagWrapperView);
			});
		},

		setEnvironmentInterface : function(envInterface){
			loc_envInterface = envInterface;
		},
		
		
		getStartElement : function(){  return loc_viewContainer.getStartElement();   },
		getEndElement : function(){  return loc_viewContainer.getEndElement(); },
		
		//get all elements of this ui resourve view
		getViews : function(){	return loc_viewContainer.getViews();	},

		//append this views to some element as child
		appendTo : function(ele){  loc_viewContainer.appendTo(ele);   },
		//insert this resource view after some element
		insertAfter : function(ele){	loc_viewContainer.insertAfter(ele);		},

		//remove all elements from outsiders parents and put them back under parentView
		detachViews : function(){	 loc_viewContainer.detachViews();		},

	};
	
	return loc_out;	
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.buildComponentCore", function(){node_buildComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){node_ResourceId = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("expression.utility", function(){node_expressionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithApplicationInterface", function(){node_makeObjectWithApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){	node_createServiceRequestInfoSet = this.getData();	});
nosliw.registerSetNodeDataEvent("uicommon.createViewContainer", function(){node_createViewContainer = this.getData();});
nosliw.registerSetNodeDataEvent("uicontent.utility", function(){node_uiContentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uicontent.createEmbededScriptExpressionInContent", function(){node_createEmbededScriptExpressionInContent = this.getData();});
nosliw.registerSetNodeDataEvent("uicontent.createEmbededScriptExpressionInTagAttribute", function(){node_createEmbededScriptExpressionInTagAttribute = this.getData();});
nosliw.registerSetNodeDataEvent("uicontent.createEmbededScriptExpressionInCustomTagAttribute", function(){node_createEmbededScriptExpressionInCustomTagAttribute = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uicontent.utility", function(){node_uiContentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIContentPlugin", node_createUIContentPlugin); 

})(packageObj);
