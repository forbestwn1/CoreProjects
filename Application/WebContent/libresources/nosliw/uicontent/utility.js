//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_resourceUtility;
	var node_buildServiceProvider;
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_createContextElementInfo;
	var node_dataUtility;
	var node_basicUtility;
	var node_getObjectType;
	var node_complexEntityUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){
	
	
	
	var loc_out = {

		findEntityUp : function(currentUIContent, entityType, entityName){
			var entityInfo;
			var currentUIEntity = currentUIContent;
			while(entityInfo==undefined){
				var objType = node_getObjectType(currentUIEntity);
				if(objType==node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UICONTENT){
					entityInfo = currentUIEntity.findEntityLocally(entityType, entityName);
					if(entityInfo==undefined){
						currentUIEntity = currentUIEntity.getParentUIEntity();
					}
				}
				else if(objType==node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UITAG){
					currentUIEntity = currentUIEntity.getParentUIEntity();
				}
				else if(objType==node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIPAGE){
					break;
				}
			}
			if(entityInfo!=undefined){
				return {
					owner : currentUIEntity,
					info : entityInfo
				};
			}
		},

		getInvokeServiceRequest : function(currentUIContent, serviceName, adapterName, handlers, request){
			var serviceInfo = this.findEntityUp(currentUIContent, node_CONSTANT.UICONTENT_ENTITYTYPE_SERVICE, serviceName);
			return node_complexEntityUtility.getAttributeAdapterExecuteRequest(serviceInfo.info.parent, serviceName, adapterName, handlers, request);
		},

	    callHandlerUp : function(currentUIContent, handlerName){
			var handlerInfo = this.findEntityUp(currentUIContent, node_CONSTANT.UICONTENT_ENTITYTYPE_HANDLER, handlerName);
			if(handlerInfo!=undefined){
				var args = Array.prototype.slice.call(arguments, 2);
				this.executeHandler(handlerInfo, args);
			}
		},
	
		executeHandler : function(handlerInfo, args){
			var info = handlerInfo.info;
			if(info.handlerType==node_CONSTANT.HANDLER_TYPE_SCRIPT){
				handlerInfo.owner.callScriptFunction(info.handlerName, args);
			}
			else if(info.handlerType==node_CONSTANT.HANDLER_TYPE_TASK){
	//			loc_taskRuntime.executeExecuteEmbededTaskInSuiteRequest(handlerInfo.handlerSuite, handlerInfo.handlerName, loc_viewIO);
			}
		},
		
		/*
		 * create place holder html with special ui id 
		 */
		createPlaceHolderWithId : function(id){
			return "<nosliw style=\"display:none;\" nosliwid=\"" + id + "\"></nosliw>";
		},
		
		/*
		 * build context
		 * 		1. read context information for this resource from uiResource
		 * 		2. add extra element infos
		 */
		buildUIResourceContext : function(uiResource, contextElementInfoArray){
			var contextElementsInf = [];
			
			//get element info from resource definition
			var resourceAttrs = uiResource[node_COMMONATRIBUTECONSTANT.EXECUTABLEUIUNIT_ATTRIBUTES];
			if(resourceAttrs!=undefined){
				var contextStr = resourceAttrs.contexts;
				var contextSegs = nosliwCreateSegmentParser(contextStr, node_COMMONCONSTANT.SEPERATOR_ELEMENT);
				while(contextSegs.hasNext()){
					var name = undefined;
					var element = undefined;
					var contextSeg = contextSegs.next();
					var index = contextSeg.indexOf("@");
					if(index==-1){
						name = contextSeg;
						info = {};
					}
					else{
						name = contextSeg.substring(0, index);
						var type = contextSeg.substring(index+1);
						info = {wrapperType:type};
					}
					contextElementsInf.push(nosliwCreateContextElementInfo(name, info));
				}
			}

			//add extra element info
			_.each(contextElementInfoArray, function(contextElementInfo, key){
				contextElementsInf.push(contextElementInfo);
			}, this);
			
			return nosliwCreateContext(contextElementsInf);
		},
		
		/*
		 * update all the ui id within html by adding space name ahead of them
		 */
		updateHtmlUIId : function(html, idNameSpace){
			var find = node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"=\"";
			return html.replace(new RegExp(find, 'g'), node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"=\""+idNameSpace+node_COMMONCONSTANT.SEPERATOR_FULLNAME);
		},
		
		createTagResourceId : function(name){
			return new node_ResourceId(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UITAG, name);
		},
		
		callScriptFunction : function(funName, args){
			var that = this;
			var fun = that.prv_getScriptObject()[funName];
			var env = {
					context : that.getContext(),
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
//			var args = Array.prototype.slice.call(arguments, 1);
			if(args==undefined)  args = [];
			args.push(env);
			return fun.apply(this, args);
		},
		
	};
	
	
	return loc_out;		
		
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("variable.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("variable.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
