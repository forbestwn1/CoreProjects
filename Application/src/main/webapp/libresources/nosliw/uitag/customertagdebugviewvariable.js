//get/create package
var packageObj = library.getChildPackage("test");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;
	var node_requestServiceProcessor;
//*******************************************   Start Node Definition  ************************************** 	


	var node_createUICustomerTagViewVariable = function(env){

		var node_dataUtility = nosliw.getNodeData("variable.data.utility");

		var loc_view;
		var loc_viewInput;
		var loc_viewData;
		
		var loc_out = 
		{
			initViews : function(requestInfo){
				loc_view = $('<div/>');
				loc_viewInput = $('<input type="text"/>');	
				loc_viewData = $('<textarea rows="5" cols="150" id="aboutDescription" style="resize: none;" data-role="none"></textarea>');
				loc_view.append(loc_viewInput);
				loc_view.append(loc_viewData);
				
				loc_viewInput.bind('change', function(){
					var variableInfo = nosliw.runtime.getVariableManager().getVariableInfo(loc_viewInput.val());
					
					env.executeDataOperationRequestGet(variableInfo.variable, "", {
						success : function(requestInfo, data){
							var content = {
								value : node_dataUtility.getValueOfData(data),
								usage : variableInfo.usage
							};
							loc_viewData.val(JSON.stringify(content, null, 4));
						}
					});
				});

				return loc_view;
			},
		};
		return loc_out;
	}




	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){	node_createServiceRequestInfoSet = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUICustomerTagViewVariable", node_createUICustomerTagViewVariable); 

})(packageObj);
