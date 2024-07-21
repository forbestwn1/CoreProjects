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
	var node_createTaskInput;
	var node_taskUtility;
	var node_createValuePortValueContext;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIContentPlugin = function(){
	
	var loc_out = {

		getCreateEntityCoreRequest : function(complexEntityDef, valueContextId, bundleCore, configure, handlers, request){
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

	var loc_valuePort = node_createValuePortValueContext(loc_valueContextId, loc_bundleCore.getVariableDomain());
	var loc_envValuePort = {
		getValuePort : function(valuePortGroup, valuePortName){
			return loc_valuePort;
		}
	};



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

	var loc_out = {
		
		setParentUIEntity : function(parentUIEntity){	loc_parentUIEntity = parentUIEntity;	},
		
		getParentUIEntity : function(){   return loc_parentUIEntity;     },
		
		findEntityLocally : function(entityType, entityName){  return loc_findEntityLocally(entityType, entityName);  },
		
//		findHandlerLocally : function(handlerName){   return loc_findHandlerLocally(handlerName);  },
		
		callScriptFunction : function(funName, args){   return loc_callScriptFunction(funName, args);     },
		
		
		setEnvironmentInterface : function(envInterface){	loc_envInterface = envInterface;	},
		
		getEntityInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

			//content view			
			loc_viewContainer = node_createViewContainer(loc_idNameSpace);
			var html = _.unescape(loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKCOMPLEXUICONTENT_HTML));
			loc_viewContainer.setContentView(node_uiContentUtility.updateHtmlUIId(html, loc_idNameSpace));
			
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				//init expression in content
				_.each(loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKCOMPLEXUICONTENT_SCRIPTEXPRESSIONINCONTENT), function(embededContentDef, i){
					var embededContent = node_createEmbededScriptExpressionInContent(embededContentDef);
					var viewEle = loc_getLocalElementByUIId(embededContent.getUIId());
					
					var scriptExpressionGroup = loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKCOMPLEXUICONTENT_SCRIPTEXPRESSIONS);


					node_getLifecycleInterface(embededContent).init(viewEle, scriptExpressionGroup, loc_envValuePort);
					loc_expressionContents.push(embededContent);
				});

			}));

			return out;
		},
		
		updateView : function(view){
			loc_viewContainer.appendTo(view);
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
nosliw.registerSetNodeDataEvent("task.createTaskInput", function(){node_createTaskInput = this.getData();});
nosliw.registerSetNodeDataEvent("task.taskUtility", function(){node_taskUtility = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.createValuePortValueContext", function(){	node_createValuePortValueContext = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createUIContentPlugin", node_createUIContentPlugin); 

})(packageObj);
