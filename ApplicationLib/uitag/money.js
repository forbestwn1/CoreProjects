{
	name : "money",
	type : "data",
	base : "simpleData",
	description : "",
	attributes : [
		{
			name : "data"
		}
	],
	context: {
		group : {
			public : {
				element : {
				},
			},
			private : {
				element : {
					internal_data: {
						definition: {
							path : "<%=&(nosliwattribute_data)&%>",
							definition : {
								criteria : "test.money;1.0.0"
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

		var loc_base = base;
		var loc_view;
		var loc_currencyView;
		var loc_amountView;
		
		var loc_getViewData = function(){
			var currency = loc_currencyView.val();
			var amountStr = loc_amountView.val();
			var amount = 0; 
			if((amountStr==undefined||amountStr=="")||(currency==undefined||currency==""))  return;
			amount = parseFloat(amountStr);
			return {
				dataTypeId: "test.money;1.0.0",
				value: {
					currency : {
						dataTypeId: "test.currency;1.0.0",
						value : currency
					},
					amount : amount
				}
			};
		};

		
		var loc_out = 
		{
			initViews : function(requestInfo){
				loc_view = $('<span/>');
				var flowDataType = loc_base.getDataFlowType();
				if(flowDataType!=node_COMMONCONSTANT.DATAFLOW_IN){
					var enumDataSet = loc_base.getEnumDataSet();
					loc_amountView = $('<input type="text" style="display:inline;background:#e6dedc"/>');	
					if(enumDataSet==undefined){
						loc_currencyView = $('<input type="text" style="display:inline;background:#e6dedc"/>');	
					}
					else{
						loc_currencyView = $('<select style="display:inline;background:#e6dedc;border:solid red"/>');	
						for(var i in enumDataSet){
							loc_currencyView.append($('<option>', {
								value: enumDataSet[i].value,
								text: enumDataSet[i].value
							}));
						}
					}
				}
				else{
					loc_amountView = $('<span/>');
					loc_currencyView = $('<span/>');
				}
				loc_view.append(loc_amountView);
				loc_view.append(loc_currencyView);
					
				//ui event
				if(flowDataType!=node_COMMONCONSTANT.DATAFLOW_IN){
					loc_currencyView.bind('change', function(){
						loc_base.onDataChange(loc_getViewData());
					});
					loc_amountView.bind('change', function(){
						loc_base.onDataChange(loc_getViewData());
					});
				}
				return loc_view;
			},
			
			updateView : function(data, request){
				var currencyStr = ""; 
				if(data!=undefined&&data.value!=undefined&&data.value.currency!=undefined)  currencyStr = data.value.currency.value;
				var amountStr = ""; 
				if(data!=undefined && data.value!=undefined)   amountStr = data.value.amount+"";
				var flowDataType = loc_base.getDataFlowType();
				if(flowDataType!=node_COMMONCONSTANT.DATAFLOW_IN){
					loc_currencyView.val(currencyStr);
					loc_amountView.val(amountStr);
				}
				else{
					loc_currencyView.text(currencyStr);
					loc_amountView.text(amountStr);
				}
			},
			
			getViewData : function(){
				return loc_getViewData();
			},
			
			getDataForDemo : function(){
				return {
					dataTypeId:"test.money;1.0.0",
					value:{
						currency : {
							dataTypeId:"test.currency;1.0.0",
							value:"United States Dollar", 
						},
						amount : 100
					}
				};
			},
			
			destroy : function(){
				loc_view.remove();
			},
		};	
			
		return loc_out;
	}
}
