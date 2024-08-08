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
//*******************************************   Start Node Definition  ************************************** 	

var node_createTagUITest = function(varName, dataType, dataChangeHandler){
	
	var loc_varName = varName;
	var loc_dataType = dataType;
	
	var loc_wrapperView;
	var loc_dataControlView;
	
	var loc_dataChangeHandler = dataChangeHandler;

	var loc_createDataControlView = function(){
		if(loc_dataType=="string"){
			loc_dataControlView = $('<input type="text" style="display:inline;background:#e6dedc"/>');
			loc_dataControlView.bind('change', function(){
				loc_dataChangeHandler(loc_varName, loc_getViewData());
			});
		}
	};
	
	var loc_getViewData = function(){
		if(loc_dataType=="string"){
			var value = loc_dataControlView.val();
			if(value==undefined || value=="")  return;
			return {
				dataTypeId: "test.string;1.0.0",
				value: loc_dataControlView.val()
			};
		}
	};

	var loc_updateView = function(data, request){
		if(loc_dataType=="string"){
			if(data==undefined || data.value==undefined)  loc_dataControlView.val("");
			else loc_dataControlView.val(data.value);
		}
	};

	var loc_out = {

		initViews : function(handlers, request){
			loc_createDataControlView();
			return loc_dataControlView;
		},

		updateView : function(data, request){
			loc_updateView(data, request);
		}

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

//Register Node by Name
packageObj.createChildNode("createTagUITest", node_createTagUITest); 

})(packageObj);
