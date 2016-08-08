nosliw.getUITagManager().registerUITag("textinput", 
function(){

		var loc_view;
		
		var loc_revertChange = function(){
			
		};

		var loc_getViewData = function(name){
			var value = loc_view.val();
			var data = nosliwCreateData(value, new NosliwDataTypeInfo("simple", "integer"));
			return data;
		};

		var loc_out = 
		{
				prv_updateView : function(){
					var data = this.prv_getDataBindingData();
					if(data!=undefined){
						var value = data.value;
						loc_view.val(value);
					}
				},

				prv_setupElementEvent : function(){
					var that = this;
					loc_view.bind('change', function(){
						var requestInfo = that.prv_startRequest();
						
						var data = loc_getViewData();
						var setRequest = that.prv_createOperationRequestSet("data", "", data);
						requestInfo.addOperationRequest(setRequest);						
						
						that.prv_submitRequest(requestInfo);
						
						that.prv_triggerEvent("change", data, requestInfo);
					});
				},

				ovr_preInit : function(requestInfo){	},
				
				ovr_initViews : function(requestInfo){
					loc_view = $('<input type="text"/>');
					return loc_view;
				},
				
				ovr_postInit : function(requestInfo){
					this.prv_updateView();
					this.prv_setupElementEvent();
				},

				ovr_processAttribute : function(name, value){},

				ovr_handleDataEvent : function(name, event, path, data, requestInfo){
					this.prv_updateView();
//					NosliwErrorManager.clearErrorMessage(this.m_createRequestInfo());
				},

		};
		
		return loc_out;
}
);	
