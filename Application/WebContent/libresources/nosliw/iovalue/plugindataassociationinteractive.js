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
	var node_dataIOUtility;
	var node_taskUtility;
	var node_ioDataFactory;
	var node_getBasicEntityObjectInterface;
	var node_IOTaskInfo;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createDataAssociationInteractiveAdapterPlugin = function(){
	
	var loc_out = {

		getNewAdapterRequest : function(adapterDefinition, handlers, request){
			return node_createServiceRequestInfoSimple({}, function(request){
				return loc_createDataAssociationInteractiveAdapter(adapterDefinition);
			}, handlers, request);
		},
	};

	return loc_out;
};


var loc_createDataAssociationInteractiveAdapter = function(dataAssociationInteractive){
	
	var loc_dataAssociationInteractive = dataAssociationInteractive.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYDATAASSOCIATIONINTERACTIVE_ATTR_DATAASSOCIATION);
	
	var loc_out = {
		
		getExecuteRequest : function(parentCore, childRuntime, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var inIO = node_createIODataSet();
			
			var outIOs = {};
			var interactiveDef = node_getBasicEntityObjectInterface(childRuntime).getEntityDefinition();
			var interactive = interactiveDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYINTERACTIVE_ATTR_INTERACTIVE);
			_.each(interactive[node_COMMONATRIBUTECONSTANT.DEFINITIONINTERACTIVE_RESULT], function(resultDef, resultName){
				outIOs[resultName] = node_createIODataSet();
			});
			
			var taskInfo = new node_IOTaskInfo(function(handlers, request){
				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				out.addRequest(inIO.getDataValueRequest(undefined, {
					success : function(request, taskInputValue){
						return childRuntime.getExecuteTaskRequest(taskInputValue.default, {
							success : function(request, taskResult){
								var resultName = taskResult.resultName;
								outIOs[resultName].setData(undefined, {default:taskResult.resultValue});
								return resultName;
							}
						});
					}
				}));
				return out;
			}, "interactive", inIO, outIOs);
			
			var parentIODataSet = node_createIODataSet(node_ioDataFactory.createIODataByComplexEntity(parentCore));
			out.addRequest(node_taskUtility.getExecuteWrappedTaskRequest(parentIODataSet, loc_dataAssociationInteractive, taskInfo));
			return out;
		}
	};
	
	return loc_out;
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
nosliw.registerSetNodeDataEvent("iovalue.dataIOUtility", function(){node_dataIOUtility = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.taskUtility", function(){node_taskUtility = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.ioDataFactory", function(){node_ioDataFactory = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getBasicEntityObjectInterface", function(){node_getBasicEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.IOTaskInfo", function(){node_IOTaskInfo = this.getData();});


//Register Node by Name
packageObj.createChildNode("createDataAssociationInteractiveAdapterPlugin", node_createDataAssociationInteractiveAdapterPlugin); 

})(packageObj);
