//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;
	var node_getObjectType;
	var node_createIODataSet;
	var node_createDynamicIOData;
	var node_getComplexEntityObjectInterface;
	var node_createUIDataOperationRequest;
	var node_UIDataOperation;
//*******************************************   Start Node Definition  ************************************** 	

var node_ioDataFactory = function(){
	
	var loc_out = {

		createIODataByComplexEntity : function(complexEntityCore){
			
			var callBacks = {
				getDataOperationRequest : function(dataOpService, handlers, request){
					var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
	
					var complexInterface = node_getComplexEntityObjectInterface(this);
					var bundle = complexInterface.getBundle();
					var varDomain = bundle.getVariableDomain();
					var valueContext = complexInterface.getValueContext();

					var fullPath = dataOpService.parms.path;
					var index = fullPath.indexOf(node_COMMONCONSTANT.SEPERATOR_PATH);
					var valueStrcutureRuntimeId = fullPath.substring(0, index);
					var eleFullPath = fullPath.substring(index+1);
					var rootName;
					var elePath;
					index = eleFullPath.indexOf(node_COMMONCONSTANT.SEPERATOR_PATH);
					if(index!=-1){
						rootName = eleFullPath.substring(0, index);
						elePath = eleFullPath.substring(index+1);
					}
					else{
						rootName = eleFullPath;
						elePath = undefined;
					}
					
					dataOpService.parms.path = elePath;
					var valueStructure = valueContext.getValueStructure(valueStrcutureRuntimeId);
				
					out.addRequest(node_createUIDataOperationRequest(valueStructure, new node_UIDataOperation(rootName, dataOpService), {
						success : function(request, result){
							if(dataOpService.command==node_CONSTANT.WRAPPER_OPERATION_GET){
								return result.value;
							}
							else return result;
						}
					}));
					
					return out;
				}, 
	
			};
			
			return node_createDynamicIOData(callBacks, undefined, complexEntityCore);
		}

	};
		
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.createDynamicData", function(){node_createDynamicIOData = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getComplexEntityObjectInterface", function(){node_getComplexEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("variable.uidataoperation.createUIDataOperationRequest", function(){node_createUIDataOperationRequest = this.getData();});
nosliw.registerSetNodeDataEvent("variable.uidataoperation.UIDataOperation", function(){node_UIDataOperation = this.getData();});

//Register Node by Name
packageObj.createChildNode("ioDataFactory", node_ioDataFactory); 

})(packageObj);
