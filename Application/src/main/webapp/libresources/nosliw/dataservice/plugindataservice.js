//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createConfigure;
	var node_createErrorData;
	var node_componentUtility;
	var node_requestServiceProcessor;
	var node_createDataAssociation;
	var node_createIODataSet;
	var node_createTaskInterface;
	var node_makeObjectWithApplicationInterface;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createDataServiceEntityPlugin = function(){
	
	var loc_out = {

		getCreateEntityRequest : function(entityDef, configure, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return loc_createDataServiceProvider(entityDef, configure);
			}, handlers, request);
		},
	};

	return loc_out;
};


var loc_createDataServiceProvider = function(serviceProvider, configure){
	
	var loc_serviceProvider = serviceProvider;
	
	var loc_configure = configure;
	
	var loc_getExecuteTaskRequest = function(taskInput, handlers, request){
		return nosliw.runtime.getDataService().getExecuteDataServiceRequest(loc_serviceProvider.getAttributeValue([node_COMMONATRIBUTECONSTANT.BLOCKSERVICEPROVIDER_SERVICEID]), taskInput, handlers, request);
	};
	
	var loc_facade = node_createTaskInterface({
		getExecuteRequest : function(taskInput, requirement, handlers, request){
			return loc_getExecuteTaskRequest(taskInput, handlers, request);
		}
	});
	
	var loc_out = {
		
		getExecuteTaskRequest: function(taskInput, handlers, request){
			return loc_getExecuteTaskRequest(taskInput, handlers, request);
		},
		
		newValuePort : function(valuePortGroup, valuePortName){
		},
		
		releaseValuePort : function(valuePortId){
			
		},
		
		
		
		getValuePort : function(valuePortGroup, valuePortName){
			
		},
		
		getValueRequest : function(elmentId, handlers, request){        },

		setValueRequest : function(elmentId, value, handlers, request){        },

		setValuesRequest : function(setValueInfos, handlers, request){        
			 
			
		},
			
	};

	return node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK, loc_facade);
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("error.entity.createErrorData", function(){node_createErrorData = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("task.createTaskInterface", function(){	node_createTaskInterface = this.getData();	});
nosliw.registerSetNodeDataEvent("component.makeObjectWithApplicationInterface", function(){node_makeObjectWithApplicationInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("createDataServiceEntityPlugin", node_createDataServiceEntityPlugin); 

})(packageObj);
