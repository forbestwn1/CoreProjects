	var m_createErrorUITag = function(id, uiTag, uiResourceView){
		var m_id = id;
		var m_uiTag = uiTag;
		var m_parentResourceView = uiResourceView;

		var m_view = $('<span></span>');

		var m_startEle = m_parentResourceView.getElementById(m_id+"-start");
		m_view.insertAfter(m_startEle);
		var m_endEle = m_parentResourceView.getElementById(m_id+"-end");

		var m_errorMessages = [];
		
		var m_init = function(){
			_.extend(m_uiTagView, Backbone.Events);

			m_uiTagView.listenTo(NosliwErrorManager, "error", function(eventName, reqInfo, serviceData){
				if(eventName=='new'){
					var exist = false;
					for(var i in m_errorMessages){
						var errorInfo = m_errorMessages[i];
						if(errorInfo.reqInfo.id==reqInfo.id){
							exist = true;
							break;
						}
					}
					
					if(exist==false){
						m_errorMessages.push({
								'reqInfo' : reqInfo,
								'serviceData' : serviceData,
							});
					}
				}
				else if(eventName=='clear'){
					for(var i in m_errorMessages){
						var errorInfo = m_errorMessages[i];
						if(errorInfo.reqInfo.id==reqInfo.id){
							m_errorMessages = m_errorMessages.splice(1, i);
							break;
						}
					}
				}
				
				m_updateView();
			}); 
		};
		
		var m_updateView = function(){
			var text = '';
			for(var i in m_errorMessages){
				var errorInfo = m_errorMessages[i];
				text = text + errorInfo.reqInfo.name+' : '+errorInfo.serviceData.message;
			}
			m_view.text(text);
		};
		
		var m_uiTagView = new NosliwUITag(id, uiTag, uiResourceView, 
		{
				setAttribute : function(attribute, value){
				},
				
				setContext : function(context){
				},
				
				updateView : function(){
				},
				
				init : function(){
					
				},
				
		});

		m_init();
		
		return m_uiTagView;
	};
