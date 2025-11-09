
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"script",
"version":"1.0.0"
},
"id":"*complexscript_debug_variableall"
},
"children":[],
"dependency":{},
"info":{}
}, {"script":function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	var node_UtilityDebug = nosliw.getNodeData("complexentity.utilityDebug");

	var loc_configure;

	var loc_envInterface = {};
	
	var loc_varDomain;
	
	var loc_bundleCore;

	var loc_init = function(complexEntityDef, valueContextId, bundleCore, configure){
		loc_configure = configure;
		loc_varDomain = bundleCore.getValuePortDomain();
		loc_bundleCore = bundleCore;
	};

	var loc_out = {
		
		setEnvironmentInterface : function(envInterface){		loc_envInterface = envInterface;	},
		
		getPreInitRequest : function(){
		},
		
		updateView : function(view){
			var rootView =  $('<div>' + '</div>');
			var trigueView = $('<button>check all variables</button>');
			var resultView = $('<textarea rows="5" cols="150" style="resize: none; border:solid 1px;" data-role="none"></textarea>');
			
			rootView.append(trigueView);
			rootView.append(resultView);
			$(view).append(rootView);

			trigueView.click(function() {
				resultView.val(JSON.stringify(nosliw.runtime.getVariableManager().export()));		
			});
		},
	};
	
	loc_init(complexEntityDef, valueContextId, bundleCore, configure);
	return loc_out;
}

}, {"loadPattern":"file"
});

