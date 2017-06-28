//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONTRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_namingConvensionUtility;
	var node_resourceUtility;
	var node_OperationContext;
	var node_OperationParm;
	var node_OperationParms;
//*******************************************   Start Node Definition  ************************************** 	

var node_expressionUtility = 
{
		executeOperation : function(dataTypeId, operation, parmArray, resourcesTree){
			var dataOperationId = node_resourceUtility.createOperationResourceId(dataTypeId, operation);
			var dataOperationResource = node_resourceUtility.getResourceFromTree(resourcesTree, dataOperationId);
			var dataOperationFun = dataOperationResource.resourceData;
			var dataOperationInfo = dataOperationResource[node_COMMONTRIBUTECONSTANT.RESOURCE_INFO][node_COMMONTRIBUTECONSTANT.RESOURCEMANAGERJSOPERATION_INFO_OPERATIONINFO];
			
			//build operation context
			var operationContext = new node_OperationContext(resourcesTree, dataOperationResource.resourceInfo[node_COMMONTRIBUTECONSTANT.RESOURCEINFO_DEPENDENCY]);
			
			//base data is "this" in operation function
			var baseData;
			var operationParmArray = [];
			var parmsDefinitions = dataOperationInfo[node_COMMONTRIBUTECONSTANT.DATAOPERATIONINFO_PAMRS];
			_.each(parmArray, function(parm, index, list){
				var parmName = parm.name;
				if(parmName==undefined){
					//if no parm name, then use base name
					parmName = dataOperationInfo[node_COMMONTRIBUTECONSTANT.DATAOPERATIONINFO_BASEPARM];
				}
				
				var parmDefinition = parmsDefinitions[parmName];
				var isBase = false;
				
				if(parmDefinition[node_COMMONTRIBUTECONSTANT.DATAOPERATIONPARMINFO_ISBASE]=="true"){
					isBase = true;
					baseData = parm.value;
				}
				operationParmArray.push(new node_OperationParm(parm.value, parmName, isBase));
			}, this);
			
			var operationResult = dataOperationFun.call(baseData, new node_OperationParms(operationParmArray), operationContext);
			return operationResult;
		}
};

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("expressionUtility", node_expressionUtility); 

	var module = {
		start : function(packageObj){
			node_COMMONTRIBUTECONSTANT = packageObj.getNodeData("constant.COMMONTRIBUTECONSTANT");
			node_COMMONCONSTANT = packageObj.getNodeData("constant.COMMONCONSTANT");
			node_namingConvensionUtility = packageObj.getNodeData("common.namingconvension.namingConvensionUtility"); 
			node_resourceUtility = packageObj.getNodeData("resource.resourceUtility");
			node_OperationContext = packageObj.getNodeData("expression.entity.OperationContext");
			node_OperationParm = packageObj.getNodeData("expression.entity.OperationParm");
			node_OperationParms = packageObj.getNodeData("expression.entity.OperationParms");
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
