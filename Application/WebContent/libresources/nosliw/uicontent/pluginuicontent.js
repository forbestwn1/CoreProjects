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
	var node_getLifecycleInterface;
	var node_basicUtility;
	var node_uiContentUtility;
	var node_getEntityTreeNodeInterface;
	
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
	var loc_scriptObject = loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.DEFINITIONENTITYINDOMAIN_SCRIPT);

	//all embeded script expression(in content, attribute, ...)
	var loc_expressionContents = [];
	
	//all events on regular elements
	var loc_elementEvents = [];
	
	//view container
	var loc_viewContainer;

	//name space for this ui resource view
	//every element/customer tag have unique ui id within a web page
	//during compilation, ui id is unique within ui resoure, however, not guarenteed between different ui resource view within same web page
	//name space make sure of it as different ui resource view have different name space
	var loc_idNameSpace = nosliw.generateId();


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
			loc_callHandlerUp(eventValue[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_FUNCTION], info);
		});
		
		return {
			source : subEle,
			event :  eventName,
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

    var loc_callHandlerUp = function(handlerName){
		var args = Array.prototype.slice.call(arguments, 1);
		var handlerInfo = loc_findHandlerUp(handlerName);
		loc_executeHandler(handlerInfo, args);
	};
		
	var loc_findHandlerUp = function(handlerName){
		var handlerInfo = loc_findHandlerLocally(handlerName);
		if(handlerInfo==undefined){
			handlerInfo = loc_parentResourveView.prv_findHandlerUp(handlerName);
		}
		return handlerInfo;
	};

	var loc_findHandlerLocally = function(handlerName){
		var handlerInfo;
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
		
		if(handlerInfo==null){
			//if not found in task, try to find in script function
			var fun = loc_scriptObject==undefined?undefined:loc_scriptObject[handlerName];
			if(fun!=undefined){
				handlerInfo = {
					handlerSuite : loc_scriptObject,
					handlerName : handlerName,
					handlerType : node_CONSTANT.HANDLER_TYPE_SCRIPT,
					uiUnit : loc_out,
				};
			}
		}
		return handlerInfo;
	};

	var loc_executeHandler = function(handlerInfo, args){
		if(handlerInfo.handlerType==node_CONSTANT.HANDLER_TYPE_SCRIPT){
			loc_callScriptFunction.apply(handlerInfo.uiUnit, [handlerInfo.handlerName, args]);
		}
		else if(handlerInfo.handlerType==node_CONSTANT.HANDLER_TYPE_TASK){
//			loc_taskRuntime.executeExecuteEmbededTaskInSuiteRequest(handlerInfo.handlerSuite, handlerInfo.handlerName, loc_viewIO);
		}
	};
	
	var loc_callScriptFunction = function(funName, args){
		var that = this;
		var fun = loc_scriptObject[funName];
		var env = {
//				context : that.getContext(),
				uiUnit : that,
				trigueEvent : function(eventName, eventData, requestInfo){
					that.prv_trigueEvent(eventName, eventData, requestInfo);
				},
				trigueNosliwEvent : function(eventName, eventData, requestInfo){
					that.prv_trigueEvent(node_basicUtility.buildNosliwFullName(eventName), eventData, requestInfo);
				},
				getServiceRequest : function(serviceName, handlers, request){
					return that.prv_getExecuteServiceRequest(serviceName, handlers, request);
				},
				getTagsByAttribute : function(attributeName, attributeValue){
					return that.getTags({
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

					var clearErrorRequest = node_createBatchUIDataOperationRequest(that.getContext());
					clearErrorRequest.addUIDataOperation(new node_UIDataOperation(node_COMMONCONSTANT.UIRESOURCE_CONTEXTELEMENT_NAME_UIVALIDATIONERROR, node_uiDataOperationServiceUtility.createSetOperationService("", {})));
					out.addRequest(clearErrorRequest);

					var allSetRequest = node_createServiceRequestInfoSet(undefined, {
						success : function(requestInfo, validationsResult){
							var results = validationsResult.getResults();
							var allMessages = {};
							var opsRequest = node_createBatchUIDataOperationRequest(that.getContext(), {
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
				
				
				getUIValidationRequest : function(uiTags, handlers, request){
					var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);

					//clear previous error data
					out.addRequest(node_utilityUIError.getClearUIValidationErrorRequest(this));

					//
					out.addRequest(node_utilityUIError.getUITagsValidationRequest(uiTags));
					
					return out;
				},

		};
		if(args==undefined)  args = [];
		args.push(env);
		return fun.apply(this, args);
	};


	var loc_out = {
		setEnvironmentInterface : function(envInterface){
			loc_envInterface = envInterface;
		},
		
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


				//init expression in tag attribute
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
			
			out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_COMPLEXENTITY].createAttributeRequest(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEXUICONTENT_CUSTOMERTAG));
			
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
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uicontent.utility", function(){node_uiContentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIContentPlugin", node_createUIContentPlugin); 

})(packageObj);
