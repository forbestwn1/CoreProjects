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
	var node_createTagUITest;
	var node_requestServiceProcessor;
	var node_complexEntityUtility;
	var node_basicUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUICustomerTagTest = function(envObj){
	var loc_envObj = envObj;

	var loc_containerrView;
	var loc_attributesView;


	var loc_initViews = function(handlers, request){
		loc_containerrView = $('<div/>');

		var attributesWrapperView = $('<div/>');
		attributesWrapperView.append($('<br>Attributes: <br>'));
    	loc_attributesView = $('<textarea rows="6" cols="150" style="resize: none; border:solid 1px;" data-role="none"></textarea>');
		attributesWrapperView.append(loc_attributesView);
		
		loc_containerrView.append(attributesWrapperView);

		return loc_containerrView;
	};
	
	var loc_updateAttributeView = function(){
		loc_attributesView.val(node_basicUtility.stringify(loc_envObj.getAttributes()));
	};
	
	var loc_out = {
		
		created : function(){
		},
		
		updateAttributes : function(attributes, request){
//            loc_attributeValues = _.extend(loc_attributeValues, attributes);
            loc_updateAttributeView();
		},
		
		initViews : function(handlers, request){
			var out = loc_initViews(handlers, request);
			loc_updateAttributeView();
			return out;
		},
		postInit : function(request){
		},
		destroy : function(request){
		},
	};
	
	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){	node_createServiceRequestInfoSet = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("uitag.test.createTagUITest", function(){node_createTagUITest = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUICustomerTagTest", node_createUICustomerTagTest); 

})(packageObj);
