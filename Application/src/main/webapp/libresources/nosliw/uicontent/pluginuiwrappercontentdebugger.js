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
	var node_createUITagOnBaseSimple;
	var node_buildUITagCoreObject;
	var node_getObjectType;
	var node_ValueInVarOperation;
	var node_valueInVarOperationServiceUtility;
	var node_createBatchValueInVarOperationRequest;
	var node_requestServiceProcessor;
	var node_createValueInVarOperationRequest;
	var node_createEventObject;
	var node_createUICustomerTagTest;
	var node_createUICustomerTagViewVariable;
	var node_getEntityObjectInterface;
	var node_uiTagUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIContentWrapperDebuggerPlugin = function(){
	
	var loc_out = {

		getCreateEntityCoreRequest : function(complexEntityDef, internalValuePortContainerId, externalValuePortContainerId, bundleCore, configure, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return loc_createUIContentWrapperDebuggerComponentCore(complexEntityDef, internalValuePortContainerId, bundleCore, configure);
			}, handlers, request);
		}

	};

	return loc_out;
};

var loc_createUIContentWrapperDebuggerComponentCore = function(complexEntityDef, valueContextId, bundleCore, configure){
	
	var loc_complexEntityDef = complexEntityDef;
	var loc_valueContextId = valueContextId;
	var loc_bundleCore = bundleCore;
	var loc_envInterface = {};
	
	var loc_childContainer;

	var loc_out = {

		getEntityInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_ENTITY].createAttributeRequest(node_COMMONATRIBUTECONSTANT.BLOCKCOMPLEXUIWRAPPERCONTENTINCUSTOMERTAGDEBUGGER_CHILD, undefined, {
				success : function(request, childNode){
					loc_childContainer = childNode.getChildValue().getCoreEntity();
				}
			}));
			return out;
		},

		
		getPreInitRequest : function(handlers, request){
		},

		updateView : function(view){
			var wrapperView = $("<div/>");
			var childValuePortContainer = node_getEntityObjectInterface(loc_out).getInternalValuePortContainer();
			wrapperView.append($("<br>ValuePortContainerId contentwrapperdbugger: "+childValuePortContainer.getId()+"</br>"));
			wrapperView.append(loc_childContainer.updateView(wrapperView));

			view.append(wrapperView);
			return view;
		},
		
		getPostInitRequest : function(handlers, request){
		},

		setEnvironmentInterface : function(envInterface){	loc_envInterface = envInterface;	},
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
nosliw.registerSetNodeDataEvent("uitag.createUITagOnBaseSimple", function(){node_createUITagOnBaseSimple = this.getData();});
nosliw.registerSetNodeDataEvent("uitag.buildUITagCoreObject", function(){node_buildUITagCoreObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.ValueInVarOperation", function(){node_ValueInVarOperation = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.valueInVarOperationServiceUtility", function(){node_valueInVarOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.createBatchValueInVarOperationRequest", function(){node_createBatchValueInVarOperationRequest  = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.createValueInVarOperationRequest", function(){node_createValueInVarOperationRequest = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("uitag.test.createUICustomerTagTest", function(){node_createUICustomerTagTest = this.getData();	});
nosliw.registerSetNodeDataEvent("uitag.test.createUICustomerTagViewVariable", function(){node_createUICustomerTagViewVariable = this.getData();	});
nosliw.registerSetNodeDataEvent("complexentity.getEntityObjectInterface", function(){node_getEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("uicontent.uiTagUtility", function(){node_uiTagUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIContentWrapperDebuggerPlugin", node_createUIContentWrapperDebuggerPlugin); 

})(packageObj);
