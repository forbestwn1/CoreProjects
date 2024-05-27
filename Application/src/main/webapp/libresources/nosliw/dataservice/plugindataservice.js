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

		getCreateEntityCoreRequest : function(entityDef, valueContextId, bundleCore, configure, handlers, request){
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
	
	var loc_input = {};
	var loc_result;

	var loc_getExecuteTaskRequest = function(handlers, request){

		return node_createServiceRequestInfoSimple(undefined, function(request){
			var result = {
			    "resultName": "success",
			    "resultValue": {
			        "outputInService1": {
			            "dataTypeId": "test.string;1.0.0",
			            "valueFormat": "JSON",
			            "value": "default value of parm1",
			            "info": {}
			        },
			        "outputInService2": {
			            "dataTypeId": "test.string;1.0.0",
			            "valueFormat": "JSON",
			            "value": "default value of parm1",
			            "info": {}
			        }
			    }
			};
			loc_result = {};
			loc_result.success = result.resultValue;
			return result;
		}, handlers, request);

/*		
		var out = node_createServiceRequestInfoSequence(handlers, request);
		out.addRequest(nosliw.runtime.getDataService().getExecuteDataServiceRequest(loc_serviceProvider.getAttributeValue([node_COMMONATRIBUTECONSTANT.BLOCKSERVICEPROVIDER_SERVICEID])[node_COMMONATRIBUTECONSTANT.KEYSERVICE_ID], loc_input, {
			success: function(rquest, resultValue){
				loc_result = resultValue;
			}
		}));
		return out;
*/		
		
	};
	
	var loc_facade = node_createTaskInterface({
		getExecuteRequest : function(handlers, request){
			return loc_getExecuteTaskRequest(handlers, request);
		},
		getExecuteRequest1 : function(taskInput, requirement, handlers, request){
			return loc_getExecuteTaskRequest(taskInput, handlers, request);
		}
	});

	var loc_requestValuePort = {
		setValueRequest : function(elmentId, value, handlers, request){        
			
		},

		setValuesRequest : function(setValueInfos, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);      
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				_.each(setValueInfos, function(setValueInfo, i){
					loc_input[setValueInfo.elementId.getRootName()] = setValueInfo.value; 
				});
			}));
			return out;
		},
	};

	var loc_resultValuePort = {
		"interactiveResult_success" : {
			getValueRequest : function(elmentId, handlers, request){ 
				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);      
				out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
					return loc_result.success[elmentId.getRootName()];
				}));
				return out;
			 }
		}
	};

	
	var loc_out = {
		
		getExecuteTaskRequest: function(taskInput, handlers, request){
			return loc_getExecuteTaskRequest(taskInput, handlers, request);
		},
		
		getValuePort : function(valuePortGroup, valuePortName){
			if(valuePortName==node_COMMONCONSTANT.VALUEPORT_NAME_INTERACT_REQUEST){
				return loc_requestValuePort;
			}
			else{
				return loc_resultValuePort[valuePortName];
			}
		}
		
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
