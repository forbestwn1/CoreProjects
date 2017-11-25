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
//*******************************************   Start Node Definition  ************************************** 	

	/**
	 * 
	 * base customer tag object, child tag just provide extendObj which implements its own method 
	 * it is also constructor object for customer tag object  
	 * 		id: 	id for this tag
	 * 		uiTag:	ui tag resource 
	 * 		uiResourceView: 	parent ui resource view
	 */
var node_createUITag = function(id, uiTagResource, parentUIResourceView, requestInfo){
	
	var loc_uiTagObj;
	
	//id of this tag object
	var loc_id = id;
	//parent resource view
	var loc_parentResourceView = parentUIResourceView;
	//all tag attributes
	var loc_attributes = {};

	//boundary element for this tag
	var loc_startEle = undefined;
	var loc_endEle = undefined;
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(id, uiTagResource, parentUIResourceView, requestInfo){
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
		loc_context = node_uiResourceUtility.buildContext(uiTagResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_CONTEXT], parentContext);
		
		//create uiTagObject
		var uiTagResourceId = node_uiResourceUtility.createTagResourceId(uiTagResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_TAGNAME]);
		var uiTagResourceObj = nosliw.runtime.getResourceService().getResource(uiTagResourceId);
		loc_uiTagObj = uiTagResourceObj[node_COMMONATRIBUTECONSTANT.RESOURCE_RESOURCEDATA][node_COMMONATRIBUTECONSTANT.UITAGDEFINITION_SCRIPT].call(loc_out, loc_context, uiTagResource, loc_attributes);
		
		//overriden method before view is attatched to dom
		loc_uiTagObj.ovr_preInit(requestInfo);
		
		//overridden method to create init view
		var views = loc_uiTagObj.ovr_initViews(requestInfo);
		//attach view to resourve view
		if(views!=undefined)  loc_startEle.after(views);	

//		loc_eventSource = nosliwCreateRequestEventSource();
//		
		//overridden method to do sth after view is attatched to dom
		loc_uiTagObj.ovr_postInit(requestInfo);
	};
	
	
	var loc_out = {
			
			
	};
	
	//append resource and object life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_UITAG);

	node_getLifecycleInterface(loc_out).init(id, uiTagResource, parentUIResourceView, requestInfo);
	
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

//Register Node by Name
packageObj.createChildNode("createUITag", node_createUITag); 

})(packageObj);

