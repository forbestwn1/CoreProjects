//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
//*******************************************   Start Node Definition  ************************************** 	

var node_utilityDebugValuePort = function(){
	
	var loc_out = {
		
		getExportValuePortContainerRequest : function(valuePortContainer, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var outValue = {};
			_.each(valuePortContainer.getValueStructures(), function(vsByValuePortName, valuePortGroup){
				var valueByValuePortName = {};
				outValue[valuePortGroup] = valueByValuePortName;
				_.each(vsByValuePortName, function(vses, valuePortName){
					var vsById = {};
					valueByValuePortName[valuePortName] = vsById;
					_.each(vses, function(vsWrapper, vsId){
						var values = {};
						vsById[vsWrapper.getName()] = values;
						var vs = vsWrapper.getValueStructure();
						var vsEleNames = vs.getElementsName();
						_.each(vsEleNames, function(eleName){
							var vsEleVar = vs.getElement(eleName);
							out.addRequest(vsEleVar.getGetValueRequest({
								success : function(request, value){
									var varIdPath = [];
									vsEleVar.getVariable().prv_buildVarPath(varIdPath);
									values[eleName] = {
										value : value,
										variableId : vsEleVar.getVariable().prv_id,
										variableIdPath : varIdPath.reverse().join("--") 
									};
								}
							}));
						});
						
/*						
						out.addRequest(vsWrapper.getValueStructure().getAllElementsValuesRequest({
							success : function(request, vsValues){
								vsById[vsId] = vsValues;
							}
						}));
*/
					})
				});
			});
			
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				return outValue;
			}));
			
			return out;
		},		
		
	};
	
	return loc_out;		
		
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});

//Register Node by Name
packageObj.createChildNode("utilityDebugValuePort", node_utilityDebugValuePort); 
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});

})(packageObj);
