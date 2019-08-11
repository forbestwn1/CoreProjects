//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){
	
	var loc_out = {

		buildDataAssociationName : function(sourceType, sourceName, targetType, targetName){
			return sourceType+"_"+sourceName+"---"+targetType+"_"+targetName;
		},

		getContextTypes : function(){
			return [ 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PUBLIC, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PROTECTED, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_INTERNAL, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PRIVATE 
			];
		},

		getReversedContextTypes : function(){
			return [ 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PRIVATE, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PUBLIC, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PROTECTED, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_INTERNAL, 
			];
		},
		
		//merge context 
		//if isFlat is true, then treat it is as object
		//if isFlat is false, then treat it as context with group
		mergeContext : function(source, target, isFlat){
			if(target==undefined)   target = {};
			if(isFlat==true){
				_.each(source, function(value, name){
					target[name] = value;
				});
			}
			else{
				_.each(source, function(c, categary){
					var cc = target[categary];
					if(cc==undefined){
						cc = {};
						target[categary] = cc;
					}
					_.each(c, function(ele, name){
						cc[name] = ele;
					});
				});
			}
			return target;
		},
		
		//assigned value to outputIODataSet
		outputToDataSetIORequest : function(outputIODataSet, value, dataSetName, isOutputFlat, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			if(value==undefined){
				//if outputvalue is undefined, then no impact on outputTarget
//				out.addRequest(loc_outputIODataSet.getGetDataValueRequest(dataSetName));
			}
			else{
				out.addRequest(outputIODataSet.getMergeDataValueRequest(dataSetName, value, isOutputFlat));
			}
			return out;
		},

		//apply matchers for different path on value
		processMatchersRequest : function(value, matchersByPath, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			if(value!=undefined && matchersByPath!=undefined){
				var matchersByPathRequest = node_createServiceRequestInfoSet(undefined, {
					success : function(request, resultSet){
						_.each(resultSet.getResults(), function(result, path){
							node_objectOperationUtility.operateObject(value, path, node_CONSTANT.WRAPPER_OPERATION_SET, result);
						});
						return value;
					}
				});
				_.each(matchersByPath, function(matchers, path){
					var valueByPath = node_objectOperationUtility.getObjectAttributeByPath(value, path);
					matchersByPathRequest.addRequest(path, nosliw.runtime.getExpressionService().getMatchDataRequest(valueByPath, matchers));
				});
				out.addRequest(matchersByPathRequest);
			}
			else{
				out.addRequest(node_createServiceRequestInfoSimple(undefined, function(){ return value;  })); 
			}
			return out;
		},
	};
		
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});

//Register Node by Name
packageObj.createChildNode("ioTaskUtility", node_utility); 

})(packageObj);
