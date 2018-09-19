//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
	var node_makeObjectWithType;
	var node_createContext;
	var node_createContextElementInfo;
	var node_dataUtility;
	var node_uiResourceUtility;
	var node_createEmbededScriptExpressionInContent;
	var node_createEmbededScriptExpressionInAttribute;
	var node_getLifecycleInterface;
	var node_basicUtility;
	var node_createUITag;
	var node_createEventObject;
	var node_createUIDataOperationRequest;
	var node_requestServiceProcessor;
	var node_uiDataOperationServiceUtility;
	var node_UIDataOperation;
//*******************************************   Start Node Definition  ************************************** 	

var loc_createUIResourceViewFactory = function(){
	
	var loc_out = {
		createUIResourceView : function(uiResource, id, parent, context, requestInfo){
			return loc_createUIResourceView(uiResource, id, parent, context, requestInfo);
		}
	};
	
	return loc_out;
};	
	
	
/*
 * method to create ui resource view according to 
 * 		uiresource object
 * 	 	name space id
 * 		parent uiresource
 */
var loc_createUIResourceView = function(uiResource, id, parent, context, requestInfo){
	//temporately store uiResource
	var loc_uiResource = uiResource;

	//parent ui resource view
	var loc_parentResourveView = parent;
	//name space for this ui resource view
	//every element/customer tag have unique ui id within a web page
	//during compilation, ui id is unique within ui resoure, however, not guarenteed between different ui resource view within same web page
	//name space make sure of it as different ui resource view have different name space
	var loc_idNameSpace = id;

	//all constants defined. they are used in expression
	var loc_constants = loc_uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_CONSTANTS];
	
	//context object for this ui resource view
	var loc_context = context;

	//all content expression objects
	var loc_expressionContents = [];
	
	//all events on regular elements
	var loc_elementEvents = [];
	
	//object store all the functions for js block
	var loc_scriptObject = loc_uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_SCRIPT];
	
	//all customer tags
	var loc_uiTags = {};
	//all events on customer tag elements
	var loc_tagEvents = [];
	
	//all the attributes on this ui resource
	var loc_attributes = {};
	
	//ui resource view wraper element
	var loc_startEle = undefined;
	var loc_endEle = undefined;

	//temporary object for ui resource view container
	var	loc_fragmentDocument = undefined;
	var loc_parentView = undefined;
	
	//the ui resource view created 
	var loc_out = undefined;

	//if this ui resource is defined within a customer tag, then this object store all the information about that parent tag
	var loc_parentTagInfo = undefined;

	
	//event source used to register and trigger event
	var loc_eventSource = node_createEventObject();
	
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
				element : subEle,
				context : loc_out.getContext()
			};
			loc_out.prv_callScriptFunction(eventValue[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_FUNCTION], undefined, info);
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
		var tag = loc_uiTags[loc_out.prv_getUpdateUIId(tagEvent[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_ELEMENTEVENT_UIID])];
		var eventName = tagEvent[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_EVENT];
		
		var listener = tag.registerEvent(eventName, function(event, data, requestInfo){
			var info = {
				event : event,
				tag : tag,
				requestInfo: requestInfo,
			};
			loc_out.prv_callScriptFunction(eventValue[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_FUNCTION], undefined, info);
			loc_scriptObject.callEventFunction(tagEvent[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_ELEMENTEVENT_FUNCTION], data, info);
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
	var loc_findLocalElement = function(select){return loc_startEle.nextUntil(loc_endEle.next()).find(select).addBack(select);};
	
	
	/*
	 * update everything again
	 */
	var loc_refresh = function(){
		loc_setContext(loc_context);
	};

	/*
	 * get all views for this resource view
	 */
	var loc_getViews = function(){	return loc_startEle.add(loc_startEle.nextUntil(loc_endEle)).add(loc_endEle);  };

	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(uiResource, id, parent, context, requestInfo){
		
		//build context element first
		var parentContext;
		if(parent!=undefined)   parentContext = parent.getContext();
		if(loc_context==undefined)	loc_context = node_uiResourceUtility.buildContext(uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_CONTEXT], parentContext, requestInfo);
		
		//wrap html by start and end element
		var resourceStartId = "-resource-start";
		var resourceEndId = "-resource-end";
		var html = node_uiResourceUtility.createPlaceHolderWithId(resourceStartId) + _.unescape(loc_uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_HTML]) + node_uiResourceUtility.createPlaceHolderWithId(resourceEndId);
		
		//update all uiid within html by adding space name to uiid
		html = node_uiResourceUtility.updateHtmlUIId(html, loc_idNameSpace);
		
		//render html to temporary document fragment
		loc_fragmentDocument = $(document.createDocumentFragment());
		loc_parentView = $("<div></div>");
		loc_fragmentDocument.append(loc_parentView);
		var views = $($.parseHTML(html));
		loc_parentView.append(views);
		
		//get wraper dom element (start and end element)
		loc_startEle = loc_parentView.find("["+node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"='"+loc_out.prv_getUpdateUIId(resourceStartId)+"']");
		loc_endEle = loc_parentView.find("["+node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"='"+loc_out.prv_getUpdateUIId(resourceEndId)+"']");
		

		//init expression content
		_.each(loc_uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_SCRIPTEXPRESSIONSINCONTENT], function(expressionContent, key, list){
			loc_expressionContents.push(node_createEmbededScriptExpressionInContent(expressionContent, loc_out, requestInfo));
		});

		//init normal expression attribute
		_.each(loc_uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_SCRIPTEXPRESSIONINATTRIBUTES], function(expressionAttr, key, list){
			loc_expressionContents.push(node_createEmbededScriptExpressionInAttribute(expressionAttr, loc_out, requestInfo));
		});
		
		//init regular tag event
		_.each(loc_uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_ELEMENTEVENTS], function(eleEvent, key, list){
			loc_elementEvents.push(loc_initElementEvent(eleEvent));
		});

		//init customer tags
		_.each(loc_uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_UITAGS], function(uiTagResource, tagUiId, list){
			var uiTagId = loc_out.prv_getUpdateUIId(tagUiId);
			var uiTag = node_createUITag(uiTagId, uiTagResource, loc_out, requestInfo);
			loc_uiTags[uiTagId] =  uiTag;
		});

		
		
		
/*		
		
		//create script object
//		loc_scriptObject=  nosliwCreateUIResourceScriptObject(loc_uiResource[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_UIRESOURCE_SCRIPTFACTORYNAME], loc_out);
		
		//init attributes of ui resource
		_.each(loc_uiResource[node_COMMONATRIBUTECONSTANT.ATTR_UIRESOURCE_ATTRIBUTES], function(value, key, list){
			loc_attributes[key] = value;			return list;
		});
		

		//init customer tag expression attribute
		_.each(loc_uiResource[node_COMMONATRIBUTECONSTANT.ATTR_UIRESOURCE_EXPRESSIONTAGATTRIBUTES], function(expressionTagAttr, key, list){
			loc_expressionContents.push(nosliwCreateUIResourceExpressionContent(expressionTagAttr, "tagAttribute", loc_out, requestInfo));
		});
		

		//init customer tag event
		_.each(loc_uiResource[node_COMMONATRIBUTECONSTANT.ATTR_UIRESOURCE_TAGEVENTS], function(tagEvent, key, list){
			loc_tagEvents.push(loc_initTagEvent(tagEvent));
		});
		
		//call init funtion in uiresource definitio
//		loc_out.prv_getScriptObject().prv_callLocalFunction(node_COMMONCONSTANT.UIRESOURCE_FUNCTION_INIT);
		
//		loc_eventSource = nosliwCreateRequestEventSource();
		
		loc_uiResource = undefined;
*/		
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
		
		loc_tagEvents = undefined;

		loc_startEle = undefined;
		loc_endEle = undefined;
		
		loc_parentResourveView = undefined;
		loc_resource = undefined;
		loc_idNameSpace = undefined;
		
		loc_fragmentDocument = undefined;
		loc_parentView = undefined;

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
		
		prv_trigueEvent : function(eventName, data, requestInfo){loc_eventSource.triggerEvent(eventName, data, requestInfo); },
		
		prv_getTagByUIId : function(uiId){ return loc_uiTags[uiId];  },
		
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
			if(fun!=undefined){
				fun.apply(this, Array.prototype.slice.call(arguments, 1));
			}
			else{
				loc_parentResourveView.prv_callScriptFunction.apply(loc_parentResourveView, arguments);
			}
		},
		
		getContext : function(){return loc_context;},
		updateContext : function(wrappers, requestInfo){		loc_context.updateContext(wrappers, requestInfo);		},

		getStartElement : function(){  return loc_startEle;   },
		getEndElement : function(){  return loc_endEle; },
		
		//get all elements of this ui resourve view
		getViews : function(){	return loc_startEle.add(loc_startEle.nextUntil(loc_endEle)).add(loc_endEle).get();	},

		//append this views to some element as child
		appendTo : function(ele){  loc_getViews().appendTo(ele);   },
		//insert this resource view after some element
		insertAfter : function(ele){	loc_getViews().insertAfter(ele);		},

		//remove all elements from outsiders parents and put them back under parentView
		detachViews : function(){	loc_parentView.append(loc_getViews());		},

		//trigue event from this ui resource view
		trigueEvent : function(eventName, data, requestInfo){	
			//for all the child resource view, it will use root resource view to trigue the event 
			this.prv_getRootResourceView().triggerEvent(eventName, data, requestInfo); 
		},
		registerEvent : function(handler, thisContext){	return loc_eventSource.registerEventHandler(handler, thisContext);},

		
		//return dom element
		getElementByUIId : function(uiId){return this.prv_getLocalElementByUIId(uiId)[0];},
		getElementsByAttributeValue : function(attr, value){return loc_getLocalElementByAttributeValue(attr, value).get();},
		getElementByAttributeValue : function(attr, value){return loc_getLocalElementByAttributeValue(attr, value).get(0);},
		
		//return jquery object
		get$EleByUIId : function(uiId){return this.prv_getLocalElementByUIId(uiId);},
		get$EleByAttributeValue : function(attr, value){return loc_getLocalElementByAttributeValue(attr, value);},

		//find tag object according to tag name
		getTagsByName : function(name){
			var tagsOut = [];
			_.each(loc_uiTags, function(uiTag, tagId, list){
				var tagName = uiTag.getTagName();
				if(tagName==name){
					tagsOut.push(uiTag);
				}
			});
			return tagsOut;
		},

		//find tag object that have tag name and particular attribute/value
		getTagsByNameAttribute : function(name, attr, value){
			var tagsOut = [];
			_.each(loc_uiTags, function(uiTag, tagId, list){
				var tagName = uiTag.getTagName();
				if(tagName==name){
					if(uiTag.getAttribute(attr)==value){
						tagsOut.push(uiTag);
					}
				}
			});
			return tagsOut;
		},
		
		//return map containing value/tag pair for particular tag name and its attribute
		getTagsAttrMapByName : function(name, attr){
			var tagsOut = {};
			_.each(loc_uiTags, function(uiTag, tagId, list){
				var tagName = uiTag.getTagName();
				if(tagName==name){
					tagsOut[uiTag.getAttribute(attr)] = uiTag;
				}
			});
			return tagsOut;
		},
		
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
	
	};

	
	//append resource and object life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_UIRESOURCEVIEW);

	node_getLifecycleInterface(loc_out).init(uiResource, id, parent, context, requestInfo);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContext", function(){node_createContext = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uiresource.utility", function(){node_uiResourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uiresource.createEmbededScriptExpressionInContent", function(){node_createEmbededScriptExpressionInContent = this.getData();});
nosliw.registerSetNodeDataEvent("uiresource.createEmbededScriptExpressionInAttribute", function(){node_createEmbededScriptExpressionInAttribute = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uiresource.createUITag", function(){node_createUITag = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.createUIDataOperationRequest", function(){node_createUIDataOperationRequest = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.UIDataOperation", function(){node_UIDataOperation = this.getData();});


//Register Node by Name
packageObj.createChildNode("createUIResourceViewFactory", loc_createUIResourceViewFactory); 

})(packageObj);
