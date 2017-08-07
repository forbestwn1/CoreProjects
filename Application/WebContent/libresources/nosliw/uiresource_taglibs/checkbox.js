	var m_createCheckboxUITag = function(id, uiTag, uiResourceView){

		var m_view;
		
		var m_uiTagView = new NosliwUITag(id, uiTag, uiResourceView, 
		{
				tag_initViews : function(){
					m_view = $('<input type="checkbox"/>');
					return m_view;
				},
				
				tag_processAttribute : function(name, value){
					m_view.val(name, value);
				},

				tag_updateView : function(){
					var value = this.getDataBindingValue();
					m_view.attr('checked', value);
				},
				
				tag_getViewValue : function(name){
					return m_view.attr('checked');
				},

				tag_postInit : function(){
					this.tag_setupElementEvent();
				},

				tag_handleDataEvent : function(code, contextPath, eventData){
					this.m_updateView();
					NosliwErrorManager.clearErrorMessage(this.m_createRequestInfo());
				},

				tag_setupElementEvent : function(){
					var that = this;
					m_view.bind('change', function(){
						var value = that.m_getViewValue();
						
						var requestInfo = that.m_startRequest();
						requestInfo.addRequestOperation({
							'operation' : 'set',
							'context' : that.context,
							'contextPath' : that.m_getDataBinding(),
							'data' : value,
						});						
						that.m_submitRequest(requestInfo);
					});
				},
		});
		
		return m_uiTagView;
	};
	