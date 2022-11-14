//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_makeObjectWithComponentLifecycle;
	var node_makeObjectWithComponentManagementInterface;
	var node_getComponentManagementInterface;
	var node_createComponentCoreComplex;
	var node_componentUtility;
	var node_getComponentLifecycleInterface;
	var node_createServiceRequestInfoSimple;
	var node_createEventObject;
	var node_basicUtility;
	var node_requestServiceProcessor;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createComplexEntityRuntimeContainer = function(){
	
	var loc_elements = [];
	
	var loc_out = {
		
		addElement : function(element){
			loc_elements.push(element);
		},
		
		getPreInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getPreInitRequest", {}), handlers, request);
			_.each(loc_elements, function(ele, i){
				out.addRequest(ele.getPreInitRequest());
			});
			return out;
		},
		
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getUpdateRuntimeContextRequest", {}), handlers, request);
			_.each(loc_elements, function(ele, i){
				var eleId = ele[node_COMMONATRIBUTECONSTANT.ELEMENTCONTAINER_ELEMENTID];
				var eleRuntimeContext = node_componentUtility.makeChildRuntimeContext(runtimeContext, eleId, ele); 
				out.addRequest(ele.getUpdateRuntimeContextRequest(runtimeContext));
			});
			return out;
		},
		
		getUpdateRuntimeInterfaceRequest : function(runtimeInterface, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getUpdateRuntimeInterfaceRequest", {}), handlers, request);
			_.each(loc_elements, function(ele, i){
				out.addRequest(ele.getUpdateRuntimeInterfaceRequest(runtimeInterface));
			});
			return out;
		},
		
		getPostInitRequest : function(handlers, request){	
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getPostInitRequest", {}), handlers, request);
			_.each(loc_elements, function(ele, i){
				out.addRequest(ele.getPostInitRequest());
			});
			return out;
		},
		
		getLifeCycleRequest : function(transitName, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getLifeCycleRequest", {}), handlers, request);
			_.each(loc_elements, function(ele, i){
				out.addRequest(ele.getLifeCycleRequest(transitName));
			});
			return out;
		},
	};
	
	return loc_out;	
};
	
var node_createEntityContainer = function(){
	
	var loc_elements = [];
	
	var loc_out = {
		
		addElement : function(element){
			loc_elements.push(element);
		},
		
		getPreInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getPreInitRequest", {}), handlers, request);
			_.each(loc_elements, function(ele, i){
				if(ele.getPreInitRequest!=undefined) out.addRequest(ele.getPreInitRequest());
			});
			return out;
		},
		
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getUpdateRuntimeContextRequest", {}), handlers, request);
			_.each(loc_elements, function(ele, i){
				if(ele.getUpdateRuntimeContextRequest!=undefined){
					var eleId = ele[node_COMMONATRIBUTECONSTANT.ELEMENTCONTAINER_ELEMENTID];
					var eleRuntimeContext = node_componentUtility.makeChildRuntimeContext(runtimeContext, eleId, ele); 
					out.addRequest(ele.getUpdateRuntimeContextRequest(runtimeContext));
				}
			});
			return out;
		},
		
		getUpdateRuntimeInterfaceRequest : function(runtimeInterface, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getUpdateRuntimeInterfaceRequest", {}), handlers, request);
			_.each(loc_elements, function(ele, i){
				if(ele.getUpdateRuntimeInterfaceRequest!=undefined){
					out.addRequest(ele.getUpdateRuntimeInterfaceRequest(runtimeInterface));
				}
			});
			return out;
		},
		
		getPostInitRequest : function(handlers, request){	
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getPostInitRequest", {}), handlers, request);
			_.each(loc_elements, function(ele, i){
				if(ele.getPostInitRequest!=undefined){
					out.addRequest(ele.getPostInitRequest());
				}
			});
			return out;
		},
		
		getLifeCycleRequest : function(transitName, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getLifeCycleRequest", {}), handlers, request);
			_.each(loc_elements, function(ele, i){
				if(ele.getLifeCycleRequest!=undefined){
					out.addRequest(ele.getLifeCycleRequest(transitName));
				}
			});
			return out;
		},
	};
	
	return loc_out;	
};
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentLifecycle", function(){node_makeObjectWithComponentLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentManagementInterface", function(){node_makeObjectWithComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentCoreComplex", function(){node_createComponentCoreComplex = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});

//Register Node by Name
//packageObj.createChildNode("createComponentRuntime", node_createComponentRuntime); 

})(packageObj);
