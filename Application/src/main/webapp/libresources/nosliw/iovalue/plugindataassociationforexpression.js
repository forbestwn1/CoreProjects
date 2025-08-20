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
	var node_getApplicationInterface;
	var node_complexEntityUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createDataAssociationForExpressionAdapterPlugin = function(){
	
	var loc_out = {

		getNewAdapterRequest : function(adapterDefinition, baseEntityCore, handlers, request){
			return node_createServiceRequestInfoSimple({}, function(request){
				return loc_createDataAssociationForExpressionAdapter(adapterDefinition, baseEntityCore);
			}, handlers, request);
		},
	};

	return loc_out;
};


var loc_createDataAssociationForExpressionAdapter = function(dataAssociationExpression, baseEntityCore){
	
	var loc_baseEntityCore;
	
	var loc_dataAssociationForExpression;
	var loc_dataAssociationIn;
	var loc_dataAssociationOut;

	var loc_init = function(dataAssociationExpression, baseEntityCore){
		loc_baseEntityCore = baseEntityCore;
		loc_dataAssociationForExpression = dataAssociationExpression.getAttributeValue(node_COMMONATRIBUTECONSTANT.ADAPTERDATAASSOCIATIONFOREXPRESSION_DATAASSOCIATION);
		loc_dataAssociationIn = node_createDataAssociation(loc_dataAssociationForExpression[node_COMMONATRIBUTECONSTANT.DATAASSOCIATIONFOREXPRESSION_IN], loc_baseEntityCore);
		loc_dataAssociationOut = node_createDataAssociation(loc_dataAssociationForExpression[node_COMMONATRIBUTECONSTANT.DATAASSOCIATIONFOREXPRESSION_OUT], loc_baseEntityCore);
	};
	
	var loc_out = {
		
		getExecuteTaskRequest : function(taskContext, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var taskFactory = node_getApplicationInterface(loc_baseEntityCore, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASKFACTORY);
			var task = taskFactory.createTask(taskContext);

			//task init			
			out.addRequest(task.getTaskInitRequest());

			//input data assocation
			out.addRequest(loc_dataAssociationIn.getExecuteRequest({
				success : function(request){

					return task.getTaskExecuteRequest({
						success: function(request, expressionResult){
							return loc_dataAssociationOut.getExecuteRequest({
								success : function(request){
									return task;
								}
							});
						}
					})
				}
			}));

			return out;
		}
	};
	
	loc_init(dataAssociationExpression, baseEntityCore);
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
nosliw.registerSetNodeDataEvent("task.taskUtility", function(){node_taskUtility = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.ioDataFactory", function(){node_ioDataFactory = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getBasicEntityObjectInterface", function(){node_getBasicEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.IOTaskInfo", function(){node_IOTaskInfo = this.getData();});
nosliw.registerSetNodeDataEvent("component.getApplicationInterface", function(){node_getApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createDataAssociationForExpressionAdapterPlugin", node_createDataAssociationForExpressionAdapterPlugin); 

})(packageObj);
