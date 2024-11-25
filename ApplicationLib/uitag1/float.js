{
	name : "float",
	type : "data",
	base : "simpleData",
	description : "",
	attributes : [
		{
			name : "data"
		}
	],
	valueStructure: {
		group : {
			public : {
				element : {
				},
			},
			private : {
				flat : {
					internal_data: {
						definition: {
							path : "<%=&(nosliwattribute_data)&%>",
							definition : {
								criteria : "test.float;1.0.0"
							}
						}
					}
				},
			},
		},
		info : {
			inherit : "false"
		}
	},
	event : [
		{
			name : "valueChanged",
			data : {
				element : {
					value : {
						definition : {
							path: "internal_data"
						}
					}
				}
			}
		}
	],
	requires:{
	},
	script : function(base){
		var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
		var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
		var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
		var node_basicUtility = nosliw.getNodeData("common.utility.basicUtility");
		var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
		
		var loc_base = base;
		var loc_view;
		
		var loc_getViewData = function(){
			var value = loc_getRawValue();
			if(value==undefined || value=="")  return;
			return {
				dataTypeId: "test.float;1.0.0",
				value: parseFloat(value),
			};
		};

		var loc_getRawValue = function(){	return loc_view.val();	};
		
		var loc_validateValue = function(){
			if(!node_basicUtility.isNumeric(loc_getRawValue()))  return "The value should be numeric!!!";
		};
		
		var loc_out = 
		{
			initViews : function(requestInfo){
				var flowDataType = loc_base.getDataFlowType();
				if(flowDataType!=node_COMMONCONSTANT.DATAFLOW_IN){
//					loc_view = $('<input type="text" style="display:inline;background:#e6dedc"/>');	
					var enumDataSet = loc_base.getEnumDataSet();
					if(enumDataSet==undefined){
						loc_view = $('<input type="text" style="display:inline;background:#e6dedc"/>');	
					}
					else{
						loc_view = $('<select style="display:inline;background:#e6dedc;border:solid red"/>');	
						for(var i in enumDataSet){
							loc_view.append($('<option>', {
								value: enumDataSet[i].value,
								text: enumDataSet[i].value
							}));
						}
					}
				}
				else{
					loc_view = $('<span/>');	
				}

				//ui event
				if(flowDataType!=node_COMMONCONSTANT.DATAFLOW_IN){
					loc_view.bind('change', function(){
						if(loc_validateValue()==undefined){
							loc_base.onDataChange(loc_getViewData());
						}
					});									
				}
				return loc_view;
			},
			
			updateView : function(data, request){
				var flowDataType = loc_base.getDataFlowType();
				if(flowDataType!=node_COMMONCONSTANT.DATAFLOW_IN){
					if(data==undefined || data.value==undefined)  loc_view.val("");
					else loc_view.val(data.value);
				}
				else{
					if(data==undefined || data.value==undefined)  loc_view.text("");
					else loc_view.text(data.value);
				}
			},
			
			getViewData : function(){
				return loc_getViewData();
			},
			
			getValidationRequest : function(handlers, request){
				return node_createServiceRequestInfoSimple(undefined, function(request){
					return loc_validateValue();
				}, handlers, request);
			},
			
			getDataForDemo : function(){
				return {
					value:"100", 
					dataTypeId:"test.float;1.0.0"
				};
			},
			
			destroy : function(){
				loc_view.remove();
			}
		};	
			
		return loc_out;
	}
}
