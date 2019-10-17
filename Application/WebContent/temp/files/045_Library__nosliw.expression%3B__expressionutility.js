//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_namingConvensionUtility;
	var node_resourceUtility;
	var node_OperationContext;
	var node_OperationParm;
	var node_OperationParms;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = 
{
		executeOperationResource : function(resourceId, parmArray, resourcesTree){
			var dataOperationResource = node_resourceUtility.getResourceFromTree(resourcesTree, resourceId);
			var dataOperationFun = dataOperationResource[node_COMMONATRIBUTECONSTANT.RESOURCE_RESOURCEDATA];
			var dataOperationInfo = dataOperationResource[node_COMMONATRIBUTECONSTANT.RESOURCE_INFO][node_COMMONATRIBUTECONSTANT.RESOURCEMANAGERJSOPERATION_INFO_OPERATIONINFO];
			
			//build operation context
			var operationContext = new node_OperationContext(resourcesTree, dataOperationResource.resourceInfo[node_COMMONATRIBUTECONSTANT.RESOURCEINFO_DEPENDENCY]);
			
			//base data is "this" in operation function
			var baseData;
			var operationParmArray = [];
			var parmsDefinitions = dataOperationInfo[node_COMMONATRIBUTECONSTANT.DATAOPERATIONINFO_PAMRS];
			_.each(parmArray, function(parm, index, list){
				var parmName = parm[node_COMMONATRIBUTECONSTANT.OPERATIONPARM_NAME];
				if(parmName==undefined){
					//if no parm name, then use base name
					parmName = dataOperationInfo[node_COMMONATRIBUTECONSTANT.DATAOPERATIONINFO_BASEPARM];
					parm[node_COMMONATRIBUTECONSTANT.OPERATIONPARM_NAME] = parmName;
				}
				
				var parmDefinition = parmsDefinitions[parmName];
				var isBase = false;
				if(parmDefinition[node_COMMONATRIBUTECONSTANT.DATAOPERATIONPARMINFO_ISBASE]=="true"){
					isBase = true;
					baseData = parm[node_COMMONATRIBUTECONSTANT.OPERATIONPARM_DATA];
				}
				operationParmArray.push(new node_OperationParm(parm[node_COMMONATRIBUTECONSTANT.OPERATIONPARM_DATA], parmName, isBase));
			}, this);
			
			nosliw.logging.info("************************  operation   ************************");
			nosliw.logging.info(resourceId);
			_.each(parmArray, function(parm, index){
				nosliw.logging.info("Parm " + parm.name+":", parm.data);
			});
			
			var operationResult = dataOperationFun.call(baseData, new node_OperationParms(operationParmArray), operationContext);

			nosliw.logging.info("Out : ", operationResult);
			nosliw.logging.info("***************************************************************");
			
			return operationResult;
		},

		executeConvertResource : function(resourceId, data, dataTypeId, reverse, resourcesTree){
			var dataOperationResource = node_resourceUtility.getResourceFromTree(resourcesTree, resourceId);
			var dataOperationFun = dataOperationResource.resourceData;
			
			//build operation context
			var operationContext = new node_OperationContext(resourcesTree, dataOperationResource.resourceInfo[node_COMMONATRIBUTECONSTANT.RESOURCEINFO_DEPENDENCY]);
			
			//data is "this" in operation function
			var result = dataOperationFun.call(data, data, dataTypeId, reverse, operationContext);
			return result;
		},
		
		getExecuteGetSubDataRequest : function(data, name, handler, requester_parent){
			getSubDatasRequest.addRequest(name, loc_out.getExecuteOperationRequest(data[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID], "getSubData", parmsArray, {}));
		}

};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationContext", function(){node_OperationContext = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationParm", function(){node_OperationParm = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationParms", function(){node_OperationParms = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
