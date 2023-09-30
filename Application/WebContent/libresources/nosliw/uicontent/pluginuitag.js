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
	
	
	var loc_coreEnvObj = {
		createVariableByName : function(variableName){
			var varsByName = loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEXUITAG_VARIABLEBYNAME); 
			var varId = varsByName[variableName];
			return loc_context.createVariableById(varId);
		},
	};

	var loc_out = {
		
		getComplexEntityInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var uiTagCore;
			var uiTagBase = loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEXUITAG_BASE); 
			if(uiTagBase==undefined){
				uiTagCore = loc_tagDefScriptFun.call(loc_out, loc_coreEnvObj);
			}
			else if(uiTagBase=="simpleData"){
				uiTagCore = node_createUITagOnBaseSimple(loc_tagDefScriptFun, loc_coreEnvObj);
			}
			else if(uiTagBase=="arrayData"){
				uiTagCore = node_createUITagOnBaseArray(loc_tagDefScriptFun, loc_coreEnvObj);
			}
			
			loc_uiTagCore = node_buildUITagCoreObject(uiTagCore); 
	
			loc_uiTagCore.created();
			
			return out;
		},
		
		getPreInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UITagInit"), handlers, request);
			
			//overriden method before view is attatched to dom
			var initObj = loc_uiTagCore.preInit(requestInfo);
			if(initObj!=undefined && node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==node_getObjectType(initObj)){
				out.addRequest(initObj);
			}
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
