//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;
	var node_makeObjectWithLifecycle;
	var node_makeObjectWithType;
	var node_createContext;
	var node_createContextElementInfo;
	var node_dataUtility;
	var node_uiResourceUtility;
	var node_utilityUIError;
	var node_createEmbededScriptExpressionInContent;
	var node_createEmbededScriptExpressionInAttribute;
	var node_createEmbededScriptExpressionInTagAttribute;
	var node_getLifecycleInterface;
	var node_basicUtility;
	var node_createUITag;
	var node_createEventObject;
	var node_createUIDataOperationRequest;
	var node_requestServiceProcessor;
	var node_uiDataOperationServiceUtility;
	var node_UIDataOperation;
	var node_contextUtility;
	var node_IOTaskResult;
	var node_createDynamicIOData;
	var node_createViewContainer;
	var node_UICommonUtility;
	var node_createBatchUIDataOperationRequest;
	var node_namingConvensionUtility;
	var node_dataRuleUtility;
	var node_requestUtility;
//*******************************************   Start Node Definition  ************************************** 	

var loc_uiResourceViewFactory = function(){
	
	var loc_out = {
		getCreateUIViewRequest : function(uiResource, id, parent, context, handlers, requestInfo){
			var uiBody = uiResource[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIUNIT_BODYUNIT];
			var attributes = uiResource[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIUNIT_ATTRIBUTES];
			return this.getCreateUIBodyViewRequest(uiResource, uiBody, attributes, id, parent, context, handlers, requestInfo);
		},

		getCreateUIBodyViewRequest : function(uiResource, uiBody, attributes, id, parent, context, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUIView", {}), handlers, requestInfo);

			var uiView = loc_createUIView(uiResource, uiBody, attributes, id, parent, context, requestInfo);

			//create tags
			var createUITagRequest = node_createServiceRequestInfoSet(undefined, {
				success: function(requestInfo, tagResults){
					_.each(tagResults.getResults(), function(uiTag, uiTagId){
						uiView.prv_addUITag(uiTagId, uiTag);
					});
					
					//init customer tags
					var initUITagRequest = node_createServiceRequestInfoSequence(undefined, {
						success : function(){
							//others
							uiView.prv_initCustomTagEvent();
							uiView.prv_initCustomTagExpressionAttribute();
							return uiView;
						}
					}, requestInfo);
					_.each(uiView.prv_getUITags(), function(uiTag, tagUiId, list){
						initUITagRequest.addRequest(node_getLifecycleInterface(uiTag).initRequest({
							success : function(requestInfo){
								uiTag.getViewContainer().insertAfter(uiView.get$EleByUIId(tagUiId+node_COMMONCONSTANT.UIRESOURCE_CUSTOMTAG_WRAPER_START_POSTFIX));
							}
						}));
					});
					return initUITagRequest;
			}});
			
			_.each(uiBody[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIBODY_UITAGS], function(uiTagResource, tagUiId, list){
				var uiTagId = uiView.prv_getUpdateUIId(tagUiId);
				createUITagRequest.addRequest(uiTagId, node_createUITagRequest(uiTagId, uiTagResource, uiView, {
					success : function(requestInfo, uiTag){
						return uiTag;
					}
				}));
			});
			out.addRequest(createUITagRequest);
			return out;
		},
		
	};
	
	return loc_out;
}();	
	
	
var node_createUITagRequest = function(id, uiTagResource, parentUIResourceView, handlers, requestInfo){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUITag", {}), handlers, requestInfo);
	var tagId = uiTagResource[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIUNIT_TAGNAME];
	out.addRequest(nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([tagId], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UITAG, {
		success : function(requestInfo, resources){
			var uiTagDefResourceObj = resources[tagId];

			var uiTag = node_createUITag(
					uiTagDefResourceObj, 
					id, 
					uiTagResource[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIUNIT_ATTRIBUTES], 
					parentUIResourceView!=undefined?parentUIResourceView.getContext():undefined,
					{
						mode : node_CONSTANT.TAG_RUNTIME_MODE_PAGE,
						viewContainer : node_createViewContainer(id, uiTagResource[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]),
						rootView : parentUIResourceView,
						contextDef : uiTagResource[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIUNIT_TAGCONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXT_ELEMENT],
						bodyContextDef : uiTagResource[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIUNIT_BODYUNIT][node_COMMONATRIBUTECONSTANT.EXECUTABLEUIBODY_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXT_ELEMENT],
						eventNameMapping : uiTagResource[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIUNIT_EVENTMAPPING],
						varNameMapping : uiTagResource[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIUNIT_TAGCONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_LOCAL2GLOBAL],
//						startElement : parentUIResourceView.get$EleByUIId(id+node_COMMONCONSTANT.UIRESOURCE_CUSTOMTAG_WRAPER_START_POSTFIX),
//						endElement : parentUIResourceView.get$EleByUIId(id+node_COMMONCONSTANT.UIRESOURCE_CUSTOMTAG_WRAPER_END_POSTFIX),
						resource : uiTagResource,
					}, 
					uiTagResource[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIUNIT_BODYUNIT]);

			return uiTag;
		}
	}));
	return out;
};

/*
 * method to create ui resource view according to 
 * 		uiBody body for view
 * 		attributes : 
 * 	 	name space id
 * 		parent uiresource
 */
var loc_createUIView = function(uiResource, uiBody, attributes, id, parent, context, requestInfo){

	//event source used to register and trigger event
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	//for context value change event
	var loc_valueChangeEventSource = node_createEventObject();
	var loc_valueChangeEventListener = node_createEventObject();
	
	//temporately store uiResource
	var loc_uiBody = uiBody;
	var loc_uiResource = uiResource;

	//parent ui resource view
	var loc_parentResourveView = parent;
	//name space for this ui resource view
	//every element/customer tag have unique ui id within a web page
	//during compilation, ui id is unique within ui resoure, however, not guarenteed between different ui resource view within same web page
	//name space make sure of it as different ui resource view have different name space
	var loc_idNameSpace = id;

	//all constants defined. they are used in expression
	var loc_constants = loc_uiBody[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIBODY_CONSTANTS];
	
	//context object for this ui resource view
	var loc_context = context;

	//all content expression objects
	var loc_expressionContents = [];
	var loc_scriptGroup = loc_uiBody[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIBODY_SCRIPTGROUP];
	
	//all events on regular elements
	var loc_elementEvents = [];
	
	//object store all the functions for js block
	var loc_scriptObject = loc_uiBody[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIBODY_SCRIPT];
	
	var loc_services = loc_uiBody[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIBODY_SERVICES]; 
	var loc_serviceProviders = loc_uiBody[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIBODY_SERVICEPROVIDERS]; 
	
	//all customer tags
	var loc_uiTags = {};
	//all events on customer tag elements
	var loc_tagEvents = [];
	
	//all the attributes on this ui resource
	var loc_attributes = {};
	
	//view container
	var loc_viewContainer;
	
	//the ui resource view created 
	var loc_out = undefined;

	//if this ui resource is defined within a customer tag, then this object store all the information about that parent tag
	var loc_parentTagInfo = undefined;
	
	/*
	 * init element event object
	 */
	var loc_initElementEvent = function(eleEvent){
		//get element for this event
		var ele = loc_out.prv_getLocalElementByUIId(loc_out.prv_getUpdateUIId(eleEvent[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_UIID]));
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
			loc_out.prv_callScriptFunctionUp(eventValue[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_FUNCTION], info);
		});
		
		return {
			source : subEle,
			event :  eventName,
		};
	};

	/*
	 * init ui tag event object
	 */
	var loc_initTagEvent = function(tagEvent){
		var tag = loc_uiTags[loc_out.prv_getUpdateUIId(tagEvent[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_UIID])];
		var eventName = tagEvent[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_EVENT];
		
		var listener = tag.registerTagEventListener(eventName, function(event, eventData, requestInfo){
			var info = {
				event : event,
				eventData : eventData,
				source : tag,
				requestInfo: requestInfo,
			};
			loc_out.prv_callScriptFunctionUp(tagEvent[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_FUNCTION], info);
		});
		
		return {
			source : tag,
			event :  eventName,
			listener: listener,
		};
	};
	
	
	/*
	 * find matched element according to attribute value
	 */
	var loc_getLocalElementByAttributeValue = function(name, value){return loc_findLocalElement("["+name+"='"+value+"']");};
	
	/*
	 * find matched elements according to selection
	 */
	var loc_findLocalElement = function(select){return loc_viewContainer.findElement(select);};
	
	/*
	 * update everything again
	 */
	var loc_refresh = function(){
		loc_setContext(loc_context);
	};

	//io between module context and page context
	var loc_viewIO = node_createDynamicIOData(
		function(handlers, request){
			return node_contextUtility.getContextValueAsParmsRequest(loc_context, handlers, request);
		}, 
		function(value, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			//update page with data
			out.addRequest(loc_context.getUpdateContextRequest(value));
			return out;
		}
	);

	var loc_getServiceRequest = function(serviceName, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteService", {"serviceName":serviceName}), handlers, request);
		var service = loc_services[serviceName];
		out.addRequest(nosliw.runtime.getDataService().getExecuteEmbededDataServiceByNameRequest(service[node_COMMONATRIBUTECONSTANT.EXECUTABLESERVICEUSE_PROVIDER], loc_serviceProviders, service, loc_viewIO));
		return out;
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(uiBody, attributes, id, parent, context, requestInfo){

		loc_attributes = attributes;
		
		//build context element first
		if(loc_context==undefined){
			//if context not provide, then build context by parent context and current context definition
			var parentContext = parent==undefined?undefined:parent.getContext();
			loc_context = node_contextUtility.buildContext("View_"+id, loc_uiBody[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIBODY_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXT_ELEMENT], parentContext);
		}

		var viewAttrs = {
			nosliwdefid : uiResource[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID],
		};
		loc_viewContainer = node_createViewContainer(loc_idNameSpace, uiResource[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID], viewAttrs);
		loc_viewContainer.setContentView(node_uiResourceUtility.updateHtmlUIId(_.unescape(loc_uiBody[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIBODY_HTML]), loc_idNameSpace));

		//init expression content
		_.each(loc_uiBody[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIBODY_SCRIPTEXPRESSIONSINCONTENT], function(expressionContent, key, list){
			loc_expressionContents.push(node_createEmbededScriptExpressionInContent(expressionContent, loc_scriptGroup, loc_out, requestInfo));
		});

		//init normal expression attribute
		_.each(loc_uiBody[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIBODY_SCRIPTEXPRESSIONINATTRIBUTES], function(expressionAttr, key, list){
			loc_expressionContents.push(node_createEmbededScriptExpressionInAttribute(expressionAttr, loc_scriptGroup, loc_out, requestInfo));
		});

		//init regular tag event
		_.each(loc_uiBody[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIBODY_ELEMENTEVENTS], function(eleEvent, key, list){
			loc_elementEvents.push(loc_initElementEvent(eleEvent));
		});

	};

	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		
		//call destroy funtion in uiresource definition
//		loc_out.prv_getScriptObject().prv_callLocalFunction(NOSLIWCONSTANT.UIRESOURCE_FUNCTION_DESTROY);
		
		//detach view from dom
		loc_out.detachViews();
		
		loc_attributes = {};
		
		_.each(loc_uiTags, function(uiTag, tag, list){
			uiTag.destroy();
		});
		loc_uiTags = undefined;

		_.each(loc_expressionContents, function(expressionContent, key, list){
			expressionContent.destroy();
		});
		loc_expressionContents = undefined;
		
		_.each(loc_elementEvents, function(eleEvent, key, list){
			eleEvent.source.unbind(eleEvent.event);
		});
		loc_elementEvents = undefined;

		loc_eventSource.clearup();
		loc_valueChangeEventSource.clearup();
		
		loc_tagEvents = undefined;

		loc_parentResourveView = undefined;
		loc_resource = undefined;
		loc_idNameSpace = undefined;
		
		loc_context = undefined;
	};

	loc_out = {
		ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},
		
		prv_getScriptObject : function(){return loc_scriptObject;},
		//get the parent resource view that contain this resource view, when this resource is within tag
		prv_getParentResourceView : function(){		return loc_parentResourveView;		},
		//get root resource view: the resource view that don't have parent
		prv_getRootResourceView : function(){
			var view = this;
			var parent = view.prv_getParentResourceView();
			while(parent!=undefined){
				view = parent;
				parent = view.prv_getParentResourceView();
			}
			return view;
		},
		
		prv_initCustomTagEvent : function(){
			//init customer tag event
			_.each(loc_uiBody[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIBODY_TAGEVENTS], function(tagEvent, key, list){
				loc_tagEvents.push(loc_initTagEvent(tagEvent));
			});
		},
		
		prv_initCustomTagExpressionAttribute : function(){
			//init tag expression attribute
			_.each(loc_uiBody[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIBODY_SCRIPTEXPRESSIONINTAGATTRIBUTES], function(expressionAttr, key, list){
				loc_expressionContents.push(node_createEmbededScriptExpressionInTagAttribute(expressionAttr, loc_scriptGroup, loc_out, requestInfo));
			});
		},
		
		prv_trigueEvent : function(eventName, data, requestInfo){loc_eventSource.triggerEvent(eventName, data, requestInfo); },

		prv_getTagByUIId : function(uiId){ return loc_uiTags[uiId];  },
		prv_getUITags : function(uiId){ return loc_uiTags;  },
		prv_addUITag : function(uiId, uiTag){  
			loc_uiTags[uiId] = uiTag;  
			uiTag.registerEventListener(loc_eventListener, function(eventName, eventData, requestInfo){
				loc_out.prv_trigueEvent(eventName, eventData, requestInfo);
			});
		},
		
		/*
		 * update ui id by adding space name ahead of them
		 */
		prv_getUpdateUIId : function(uiId){	return loc_idNameSpace+node_COMMONCONSTANT.SEPERATOR_FULLNAME+uiId;	},

		/*
		 * find matched element according to uiid
		 */
		prv_getLocalElementByUIId : function(id){return loc_findLocalElement("["+node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"='"+id+"']");},
		
		prv_callScriptFunction : function(funName){   
			var fun = loc_scriptObject[funName];
			var env = {
					context : loc_out.getContext(),
					uiUnit : loc_out,
					trigueEvent : function(eventName, eventData, requestInfo){
						loc_out.prv_trigueEvent(eventName, eventData, requestInfo);
					},
					trigueNosliwEvent : function(eventName, eventData, requestInfo){
						loc_out.prv_trigueEvent(node_basicUtility.buildNosliwFullName(eventName), eventData, requestInfo);
					},
					getServiceRequest : function(serviceName, handlers, request){
						return loc_getServiceRequest(serviceName, handlers, request);
					},
					getTagsByAttribute : function(attributeName, attributeValue){
						return loc_out.getTags({
							attribute : [
								{
									name : attributeName,
									value : attributeValue,
								}
							]
						}, true);
					},
					getUIValidationRequest1 : function(uiTags, handlers, request){
						var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUIViewWithId", {}), handlers, requestInfo);

						var clearErrorRequest = node_createBatchUIDataOperationRequest(loc_context);
						clearErrorRequest.addUIDataOperation(new node_UIDataOperation(node_COMMONCONSTANT.UIRESOURCE_CONTEXTELEMENT_NAME_UIVALIDATIONERROR, node_uiDataOperationServiceUtility.createSetOperationService("", {})));
						out.addRequest(clearErrorRequest);

						var allSetRequest = node_createServiceRequestInfoSet(undefined, {
							success : function(requestInfo, validationsResult){
								var results = validationsResult.getResults();
								var allMessages = {};
								var opsRequest = node_createBatchUIDataOperationRequest(loc_context, {
									success : function(request){
										return allMessages;
									}
								}, requestInfo);
								_.each(results, function(message, uiTagId){
									if(message!=undefined){
										allMessages[uiTagId] = message;
										opsRequest.addUIDataOperation(new node_UIDataOperation(node_COMMONCONSTANT.UIRESOURCE_CONTEXTELEMENT_NAME_UIVALIDATIONERROR, node_uiDataOperationServiceUtility.createSetOperationService(uiTagId, message)));
									}
								});
								if(!opsRequest.isEmpty())	return opsRequest;
								else return node_requestUtility.getEmptyRequest();

							},
						});
						_.each(uiTags, function(uiTag, i){
							var uiTagDataValidationRequest = node_createServiceRequestInfoSequence();
							var varName = uiTag.getAttribute("data");
							uiTagDataValidationRequest.addRequest(node_createUIDataOperationRequest(loc_context, this.getDataOperationGet(varName, ""), {
								success : function(request, uiData){
									var dataEleDef = node_contextUtility.getContextElementDefinitionFromFlatContext(loc_uiResource[node_COMMONATRIBUTECONSTANT.UITAGDEFINITION_FLATCONTEXT], varName);
									var rules = dataEleDef[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFINITION]
															[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_DEFINITION]
															[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_CRITERIA]
															[node_COMMONATRIBUTECONSTANT.VARIABLEDATAINFO_RULE];
									var data;
									if(uiData!=undefined)  data = uiData.value;
									return node_dataRuleUtility.getDataValidationByRulesRequest(data, rules);
								}
							}));

							allSetRequest.addRequest(uiTag.getId(), uiTagDataValidationRequest);
						});
						out.addRequest(allSetRequest);
						return out;
					},
					
					
					getUIValidationRequest2 : function(uiTags, handlers, request){
						var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUIViewWithId", {}), handlers, requestInfo);

						var clearErrorRequest = node_createBatchUIDataOperationRequest(loc_context);
						clearErrorRequest.addUIDataOperation(new node_UIDataOperation(node_COMMONCONSTANT.UIRESOURCE_CONTEXTELEMENT_NAME_UIVALIDATIONERROR, node_uiDataOperationServiceUtility.createSetOperationService("", {})));
						out.addRequest(clearErrorRequest);

						var allSetRequest = node_createServiceRequestInfoSet(undefined, {
							success : function(requestInfo, validationsResult){
								var results = validationsResult.getResults();
								var allMessages = {};
								var opsRequest = node_createBatchUIDataOperationRequest(loc_context, {
									success : function(request){
										return allMessages;
									}
								}, requestInfo);
								_.each(results, function(message, uiTagId){
									if(message!=undefined&&message.length!=0){
										allMessages[uiTagId] = message;
										opsRequest.addUIDataOperation(new node_UIDataOperation(node_COMMONCONSTANT.UIRESOURCE_CONTEXTELEMENT_NAME_UIVALIDATIONERROR, node_uiDataOperationServiceUtility.createSetOperationService(uiTagId, message)));
									}
								});
								if(!opsRequest.isEmpty())	return opsRequest;
								else return node_requestUtility.getEmptyRequest();

							},
						});
						_.each(uiTags, function(uiTag, i){
							allSetRequest.addRequest(uiTag.getId(), uiTag.getValidateDataRequest());
						});
						out.addRequest(allSetRequest);
						return out;
					},

					getUIValidationRequest : function(uiTags, handlers, request){
						var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUIViewWithId", {}), handlers, requestInfo);

						//clear previous error data
						out.addRequest(node_utilityUIError.getClearUIValidationErrorRequest(loc_out));

						//
						out.addRequest(node_utilityUIError.getUITagsValidationRequest(uiTags));
						
						return out;
					},

			};
			var args = Array.prototype.slice.call(arguments, 1);
			args.push(env);
			return fun.apply(this, args);
		},
		
		prv_callScriptFunctionUp : function(funName){   
			var find = this.prv_findFunctionUp(funName);
			if(find!=undefined)		return find.uiUnit.prv_callScriptFunction.apply(find.uiUnit, arguments);
			else  nosliw.error("Cannot find function : " + funName);
		},

		prv_callScriptFunctionDown : function(funName){
			var find = this.prv_findFunctionDown(funName);
			if(find!=undefined)		return find.uiUnit.prv_callScriptFunction.apply(find.uiUnit, arguments);
			else nosliw.warning("Cannot find function : " + funName);
		},

		prv_findFunctionDown : function(funName){
			var fun = loc_scriptObject==undefined?undefined:loc_scriptObject[funName];
			if(fun!=undefined){
				return {
					fun : fun,
					uiUnit : loc_out,
				};
			}
			else{
				for (var id in loc_uiTags) {
				    if (!loc_uiTags.hasOwnProperty(id)) continue;
				    var childUITag = loc_uiTags[id];
			    	var funInfo = childUITag.getTagObject().findFunctionDown(funName);
			    	if(funInfo!=undefined)  return funInfo;
				}				
			}
		},

		prv_findFunctionUp : function(funName){
			var fun = loc_scriptObject==undefined?undefined:loc_scriptObject[funName];
			if(fun!=undefined){
				return {
					fun : fun,
					uiUnit : loc_out,
				};
			}
			else{
				return loc_parentResourveView.prv_findFunctionUp(funName);
			}
		},
		
		prv_getTags : function(query, cascade, output){
			_.each(loc_uiTags, function(uiTag, tagId, list){
				var ok = true;
				if(query!=undefined){
					//check name
					if(ok && query.name!=undefined){
						var tagName = uiTag.getTagName();
						if(tagName!=name)  ok = false;
					}

					//check attributes
					if(ok && query.attribute!=undefined){
						_.each(query.attribute, function(attr, i){
							if(uiTag.getAttributeValue(attr.name)!=attr.value)	ok = false;
						});
					}
				}

				if(ok)  output.push(uiTag);
				
				//process child
				if(cascade==true){
					var childUITags = uiTag.getChildUITags();
					_.each(childUITags, function(child, i){
						child.prv_getTags(query, cascade, output);
					});
				}
			});
		},


		getId : function(){   return loc_idNameSpace;  },
		
		getContext : function(){return loc_context;},
		getUpdateContextRequest : function(values, handlers, requestInfo){	return loc_context.getUpdateContextRequest(values, handlers, requestInfo);		},
		getContextElements : function(){  return this.getContext().prv_elements; },

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
		
		//return dom element
		getElementByUIId : function(uiId){return this.prv_getLocalElementByUIId(uiId)[0];},
		getElementsByAttributeValue : function(attr, value){return loc_getLocalElementByAttributeValue(attr, value).get();},
		getElementByAttributeValue : function(attr, value){return loc_getLocalElementByAttributeValue(attr, value).get(0);},
		
		//return jquery object
		get$EleByUIId : function(uiId){return this.prv_getLocalElementByUIId(uiId);},
		get$EleByAttributeValue : function(attr, value){return loc_getLocalElementByAttributeValue(attr, value);},

		//find tag object according to tag name
		getTagsByName : function(name){
			return this.getTags({
				name : name
			}, false);
		},

		//find tag object that have tag name and particular attribute/value
		getTagsByNameAttribute : function(name, attr, value){
			return this.getTags({
				name : name,
				attribute : [{
					name : attr,
					value : value
				}],
			}, false);
		},
		
		getTags : function(query, cascade){
			var output = [];
			this.prv_getTags(query, cascade, output);
			return output;
		},
		
//		//return map containing value/tag pair for particular tag name and its attribute
//		getTagsAttrMapByName : function(name, attr){
//			var tagsOut = {};
//			_.each(loc_uiTags, function(uiTag, tagId, list){
//				var tagName = uiTag.getTagName();
//				if(tagName==name){
//					tagsOut[uiTag.getAttribute(attr)] = uiTag;
//				}
//			});
//			return tagsOut;
//		},
		
		getConstants : function(){   return loc_constants;  },
		
		setAttribute : function(attribute, value){loc_attributes[attribute]=value;},
		getAttribute : function(attribute){return loc_attributes[attribute];},
		
		getIdNameSpace : function(){return loc_idNameSpace;},
		getParentTagInfo : function(){	return loc_parentTagInfo;	},
		setParentTagInfo : function(info){		loc_parentTagInfo = info;	},
		
		destroy : function(requestInfo){  node_getLifecycleInterface(loc_out).destroy(requestInfo);  },
		
		getDataOperationSet : function(target, path, value, dataTypeInfo){  return new node_UIDataOperation(target, node_uiDataOperationServiceUtility.createSetOperationService(path, value, dataTypeInfo)); },
		getDataOperationRequestSet : function(target, value, dataTypeInfo, handlers, request){	return node_createUIDataOperationRequest(loc_context, this.getDataOperationSet(target, undefined, value, dataTypeInfo), handlers, request);	},
		executeDataOperationRequestSet : function(target, value, dataTypeInfo, handlers, request){	return node_requestServiceProcessor.processRequest(this.getDataOperationRequestSet(target, value, dataTypeInfo, handlers, request));	},
	
		getDefaultOperationRequestSet : function(value, dataTypeInfo, handlers, request){	return this.getDataOperationRequestSet(this.getContext().getElementsName()[0], value, dataTypeInfo, handlers, request);	},
		executeDefaultDataOperationRequestSet : function(value, dataTypeInfo, handlers, request){	return node_requestServiceProcessor.processRequest(this.getDefaultOperationRequestSet(value, dataTypeInfo, handlers, request));	},
	
		registerEventListener : function(listener, handler, thisContext){	return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

		registerValueChangeEventListener : function(listener, handler, thisContext){    return loc_context.registerValueChangeEventListener(listener, handler, thisContext);     },
		unregisterValueChangeEventListener : function(listener){	return loc_context.unregisterValueChangeEventListener(listener); },
		
		command : function(command, data, requestInfo){			return this.prv_callScriptFunctionDown("command_"+command, data, requestInfo);		},
		findFunctionDown : function(funName){  return this.prv_findFunctionDown(funName);  },
	};

	//append resource and object life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_UIVIEW);

	node_getLifecycleInterface(loc_out).init(uiBody, attributes, id, parent, context, requestInfo);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){	node_createServiceRequestInfoSet = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContext", function(){node_createContext = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.utility", function(){node_uiResourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.utilityUIError", function(){node_utilityUIError = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createEmbededScriptExpressionInContent", function(){node_createEmbededScriptExpressionInContent = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createEmbededScriptExpressionInAttribute", function(){node_createEmbededScriptExpressionInAttribute = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createEmbededScriptExpressionInTagAttribute", function(){node_createEmbededScriptExpressionInTagAttribute = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uitag.createUITag", function(){node_createUITag = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.createUIDataOperationRequest", function(){node_createUIDataOperationRequest = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.UIDataOperation", function(){node_UIDataOperation = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.IOTaskResult", function(){node_IOTaskResult = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createDynamicData", function(){node_createDynamicIOData = this.getData();});
nosliw.registerSetNodeDataEvent("uicommon.createViewContainer", function(){node_createViewContainer = this.getData();});
nosliw.registerSetNodeDataEvent("uicommon.utility", function(){node_UICommonUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.createBatchUIDataOperationRequest", function(){node_createBatchUIDataOperationRequest  = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("data.dataRuleUtility", function(){node_dataRuleUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("uiResourceViewFactory", loc_uiResourceViewFactory); 

})(packageObj);
