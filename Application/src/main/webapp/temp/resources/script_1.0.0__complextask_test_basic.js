
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"script",
"version":"1.0.0"
},
"id":"*complextask_test_basic"
},
"children":[],
"dependency":{},
"info":{}
}, {"script":function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_createValuePortElementInfo = nosliw.getNodeData("valueport.createValuePortElementInfo");

	var loc_valueContext;

	var loc_envInterface = {};

	var loc_init = function(complexEntityDef, valueContextId, bundleCore, configure){
		var varDomain = bundleCore.getVariableDomain();
		loc_valueContext = varDomain.creatValuePortContainer(valueContextId);
	};

	var loc_out = {
		
		setEnvironmentInterface : function(envInterface){		loc_envInterface = envInterface;	},
		
		getPreInitRequest : function(){
		},
		
		updateView : function(view){
			var rootView =  $('<div>Task' + '</div>');
			$(view).append(rootView);
		},
	};
	
	loc_init(complexEntityDef, valueContextId, bundleCore, configure);
	return loc_out;
}

}, {"loadPattern":"file"
});

