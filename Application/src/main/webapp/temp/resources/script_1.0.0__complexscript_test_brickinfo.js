
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"script",
"version":"1.0.0"
},
"id":"*complexscript_test_brickinfo"
},
"children":[],
"dependency":{},
"info":{}
}, {"script":function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	var node_getObjectType = nosliw.getNodeData("common.interfacedef.getObjectType");
	var node_complexEntityUtility = nosliw.getNodeData("complexentity.complexEntityUtility");
	var node_getEntityObjectInterface = nosliw.getNodeData("complexentity.getEntityObjectInterface");

	var loc_configure;

	var loc_valueContext;

	var loc_envInterface = {};
	
	var loc_varDomain;
	var loc_bundleCore;

	var loc_init = function(complexEntityDef, valueContextId, bundleCore, configure){
		loc_configure = configure;
		loc_bundleCore = bundleCore;
		loc_varDomain = bundleCore.getValuePortDomain();
	};

	var loc_out = {
		
		setEnvironmentInterface : function(envInterface){		loc_envInterface = envInterface;	},
		
		getPreInitRequest : function(){
		},
		
		updateView : function(view){
			var rootView =  $('<div>' + '</div>');
			var trigueView = $('<button>Check value port container</button>');
			var intputView = $('<textarea rows="1" cols="150" style="resize: none; border:solid 1px;" data-role="none"></textarea>');
			var resultView = $('<textarea rows="5" cols="150" style="resize: none; border:solid 1px;" data-role="none"></textarea>');
			
			rootView.append(trigueView);
			rootView.append(intputView);
			rootView.append(resultView);
			$(view).append(rootView);

			trigueView.click(function() {
				var brickPath = intputView.val();
				
				var brickCore = node_complexEntityUtility.getDescendantCore(loc_bundleCore.getMainEntityCore(), brickPath);
				var brickType = node_getObjectType(brickCore);
				var valuePortContainer = node_getEntityObjectInterface(brickCore).getInternalValuePortContainer();
				
				var output = {};
				output.brickType = brickType;
				output.valuePortContainerId = valuePortContainer.getId(); 
				
				var request = valuePortContainer.getExportRequest({
					success : function(request, values){
						output.valuePortContainer  = values;
						resultView.val(JSON.stringify(output));
					}
				});
				node_requestServiceProcessor.processRequest(request);
			});
		},
	};
	
	loc_init(complexEntityDef, valueContextId, bundleCore, configure);
	return loc_out;
}

}, {"loadPattern":"file"
});

