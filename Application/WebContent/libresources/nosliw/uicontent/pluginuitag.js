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
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createUITagPlugin = function(){
	
	var loc_out = {

		getCreateComplexEntityCoreRequest : function(complexEntityDef, valueContextId, bundleCore, configure, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createUITagCoreEntity"), handlers, request);

			var tagId = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEXUITAG_TAGID);
			var resourceId = new node_ResourceId(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UITAGSCRIPT, tagId);
			
			out.addRequest(nosliw.runtime.getResourceService().getGetResourceDataRequest(resourceId, {
				success : function(requestInfo, resourceData){
					var tagDefScriptFun = resourceData[node_COMMONATRIBUTECONSTANT.EXECUTABLESCRIPT_SCRIPT];
					return loc_createUITagComponentCore(complexEntityDef, tagDefScriptFun, valueContextId, bundleCore, configure);
	 			}
			}));
			
			return out;
		},
	};

	return loc_out;
};

var loc_createUITagComponentCore = function(complexEntityDef, tagDefScriptFun, valueContextId, bundleCore, configure){
	var loc_tagDefScriptFun = tagDefScriptFun;
	var loc_complexEntityDef = complexEntityDef;
	var loc_valueContextId = valueContextId;
	var loc_bundleCore = bundleCore;
	var loc_valueContext = loc_bundleCore.getVariableDomain().getValueContext(loc_valueContextId);
	var loc_envInterface = {};
	var loc_uiTagCore;

	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(handlers, requestInfo){
		var uiTagCore;
		var uiTagBase = loc_complexEntityDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEXUITAG_BASE];
		if(uiTagBase==undefined){
			uiTagCore = loc_tagDefScriptFun.call(loc_out, loc_envObj);
		}
		else if(uiTagBase=="simpleData"){
			uiTagCore = node_createUITagOnBaseSimple(loc_tagDefScriptFun, loc_envObj, loc_complexEntityDef);
		}
		else if(uiTagBase=="arrayData"){
			uiTagCore = node_createUITagOnBaseArray(loc_tagDefScriptFun, loc_envObj, loc_complexEntityDef);
		}
		
		loc_uiTagCore = node_buildUITagCoreObject(uiTagCore); 

		loc_uiTagCore.created();
		
		var uiTagInitRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("UITagInit"), handlers, requestInfo);
		
		//overriden method before view is attatched to dom
		var initObj = loc_uiTagCore.preInit(requestInfo);
		if(initObj!=undefined && node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==node_getObjectType(initObj)){
			uiTagInitRequest.addRequest(initObj);
		}
		
		//overridden method to create init view
		uiTagInitRequest.addRequest(node_createServiceRequestInfoSimple(undefined, function(requestInfo){
			var initRequest = node_createServiceRequestInfoSequence(undefined);
			var initViewsResult = loc_uiTagObj.initViews({
				success : function(request, view){
					loc_viewContainer.setContentView(view);
				}
			}, requestInfo);

			if(initViewsResult!=undefined){
				if( node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==node_getObjectType(initViewsResult)){
					initRequest.addRequest(initViewsResult);
				}
				else{
					loc_viewContainer.setContentView(initViewsResult);
				}
			}

			//overridden method to do sth after view is attatched to dom
			if(loc_uiTagObj.postInit!=undefined){
				var postInitObj = loc_uiTagObj.postInit(requestInfo);
				if(postInitObj!=undefined && node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==node_getObjectType(postInitObj)){
					initRequest.addRequest(postInitObj);
				}
			}

			return initRequest;
		}));

		return uiTagInitRequest;
	};


	var loc_out = {
		
		getComplexEntityInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			
			return out;
		},
		
		updateView : function(view){
			view.append(loc_tagDefObj.initViews());
		},
		
		getUIId : function(){
			return loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.WITHUIID_UIID);
		},
		
		setEnvironmentInterface : function(envInterface){
			loc_envInterface = envInterface;
		},
		
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


//Register Node by Name
packageObj.createChildNode("createUITagPlugin", node_createUITagPlugin); 

})(packageObj);
