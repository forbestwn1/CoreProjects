function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");

	var loc_configure;

	var loc_valueContext;

	var loc_envInterface = {};
	
	var loc_varDomain;

	var loc_variableMan = nosliw.runtime.getVariableManager();

	var loc_init = function(complexEntityDef, valueContextId, bundleCore, configure){
		loc_configure = configure;
		loc_varDomain = bundleCore.getVariableDomain();
	};

	var loc_out = {
		
		setEnvironmentInterface : function(envInterface){		loc_envInterface = envInterface;	},
		
		getPreInitRequest : function(){
		},
		
		updateView : function(view){
			var rootView =  $('<div>' + '</div>');
			var trigueView = $('<button>Check variable value</button>');
			var intputView = $('<textarea rows="1" cols="150" style="resize: none; border:solid 1px;" data-role="none"></textarea>');
			var resultView = $('<textarea rows="5" cols="150" style="resize: none; border:solid 1px;" data-role="none"></textarea>');
			
			rootView.append(trigueView);
			rootView.append(intputView);
			rootView.append(resultView);
			$(view).append(rootView);

			trigueView.click(function() {
				var variableId = intputView.val();
				
				var varInfo = loc_variableMan.getVariableInfo(variableId);
				if(varInfo!=undefined&&varInfo.variable!=undefined){
					var request = varInfo.variable.getGetValueRequest({
						success : function(request, value){
							resultView.val(JSON.stringify(value));
						}
					});
					node_requestServiceProcessor.processRequest(request);
				}
				else{
					resultView.val("not exist");
				}
			});
		},
	};
	
	loc_init(complexEntityDef, valueContextId, bundleCore, configure);
	return loc_out;
}
