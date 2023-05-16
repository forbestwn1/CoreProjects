//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_objectOperationUtility;
	var node_createIODataSet;
	var node_requestServiceProcessor;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_dataIOUtility;
	var node_getExecuteMappingDataAssociationRequest;
	var node_getExecuteMirrorDataAssociationRequest;
	var node_getExecuteNoneDataAssociationRequest;
	var node_basicUtility;

//*******************************************   Start Node Definition  ************************************** 	
//dataAssociation that has inputIO, dataAssociation and outputIO
//when it is executed, the data from inputIO is mapped to outputIO
//the type of inputIO can be IODataSet or another dataAssociation
//name in parm is for debugging purpose
var node_createDataAssociation = function(inputIO, dataAssociationDef, outputIODataSet, name){
	
	var loc_inputIO = inputIO;
	var loc_outputIODataSet = node_createIODataSet(outputIODataSet);
	var loc_dataAssociationDef = dataAssociationDef;

	var loc_getExecuteDataAssociationRequest = function(inputDataSet, handlers, request){
		nosliw.logging.info("Data association ", loc_out.prv_id, " input data : " + node_basicUtility.stringify(inputDataSet));
		if(loc_dataAssociationDef==undefined)  return node_getExecuteNoneDataAssociationRequest(inputDataSet, loc_dataAssociationDef, loc_outputIODataSet, handlers, request);   //if no data association, then nothing happen
		else{
			var type = loc_dataAssociationDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATION_TYPE];
			if(type==node_COMMONCONSTANT.DATAASSOCIATION_TYPE_MAPPING)	return node_getExecuteMappingDataAssociationRequest(inputDataSet, loc_dataAssociationDef, loc_outputIODataSet, loc_out.prv_name, handlers, request);
			else if(type==node_COMMONCONSTANT.DATAASSOCIATION_TYPE_MIRROR)		return node_getExecuteMirrorDataAssociationRequest(inputDataSet, loc_dataAssociationDef, loc_outputIODataSet, loc_out.prv_name, handlers, request);
			else if(type==node_COMMONCONSTANT.DATAASSOCIATION_TYPE_NONE)	return node_getExecuteNoneDataAssociationRequest(inputDataSet, loc_dataAssociationDef, loc_outputIODataSet, loc_out.prv_name, handlers, request);
			else if(type==node_COMMONCONSTANT.DATAASSOCIATION_TYPE_TRANSPARENT){
				var out = node_createServiceRequestInfoSimple(undefined, function(request){
					_.each(inputDataSet, function(dataName, data){
						loc_outputIODataSet.setData(dataName, data);
					});
				}, handlers, request);
			}
		}
	};

	var loc_out = {
		prv_id : nosliw.generateId(),
		prv_name : name,
		
		getExecuteWithExtraDataRequest : function(extraDataSet, handlers, request){
			nosliw.logging.info("Start execute data association : ", loc_out.prv_name, "   ", loc_out.prv_id);
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteDataAssociation", {}), handlers, request);

			//get input data set first
			out.addRequest(node_dataIOUtility.getGetIODataValueRequest(loc_inputIO, {
				success: function(request, inputDataSet){
					if(extraDataSet!=undefined){
						//merge with extraDataSet
						extraDataSet = node_createIODataSet(extraDataSet);
						_.each(extraDataSet.getDataSet(), function(extraData, setName){
							var inputData = inputDataSet[setName];
							if(inputData==undefined){
								inputData = {};
								inputDataSet[setName] = inputData;
							}
							node_dataIOUtility.mergeContext(extraData, inputData, loc_dataAssociationDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATION_INPUT][setName]);
						})
					}
					
					//execute data associatoin
					return loc_getExecuteDataAssociationRequest(inputDataSet, {
						success :function(request){
							nosliw.logging.info("Data association ", loc_out.prv_id, " result : " + node_basicUtility.stringify(loc_outputIODataSet));
							return loc_outputIODataSet;
						}
					})
				}
			}));
			
			return out;
		},
			
		getExecuteRequest : function(handlers, request){
			return loc_getExecuteDataAssociationRequest(loc_inputIO, handlers, request);
//			return this.getExecuteWithExtraDataRequest(undefined, handlers, request);
		},

		executeRequest : function(handlers, request){
			var requestInfo = this.getExecuteRequest(handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		getExecuteCommandRequest : function(commandName, data, handlers, request){
			if(commandName=="execute"){
				return this.getExecuteWithExtraDataRequest(data, handlers, request);
			}
		}
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION);

	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("iovalue.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.dataIOUtility", function(){node_dataIOUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("iovalue.getExecuteMappingDataAssociationRequest", function(){node_getExecuteMappingDataAssociationRequest = this.getData();	});
nosliw.registerSetNodeDataEvent("iovalue.getExecuteMirrorDataAssociationRequest", function(){node_getExecuteMirrorDataAssociationRequest = this.getData();	});
nosliw.registerSetNodeDataEvent("iovalue.getExecuteNoneDataAssociationRequest", function(){node_getExecuteNoneDataAssociationRequest = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createDataAssociation", node_createDataAssociation); 

})(packageObj);
