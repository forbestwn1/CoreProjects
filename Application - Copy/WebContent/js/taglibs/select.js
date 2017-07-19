/**
 * 
 */

	var m_createSelectUITag = function(id, uiTag, uiResourceView){
		var m_id = id;
		var m_uiTag = uiTag;
		var m_parentResourceView = uiResourceView;
		var m_dataBinding = uiTag.dataBindings;
		
		var m_contextDataBinding;
		var m_view = $('<select />');
		var m_context;
		var m_childResourceViews = {};

		
		var m_attributes = {};
		for(var attribute in uiTag.attributes){
			if(uiTag.attributes.hasOwnProperty(attribute)){
				var attrValue = uiTag.attributes[attribute];
				m_attributes[attribute] = attrValue;
			}
		}

		var m_startEle = m_parentResourceView.getElementById(m_id+"-start");
		m_view.insertAfter(m_startEle);
		var m_endEle = m_parentResourceView.getElementById(m_id+"-end");

		var m_getAttribute = function(attribute){
			return m_attributes[attribute];
		};
		
		var m_uiTagView = {
				setAttribute : function(attribute, value){
					m_attributes[attribute] = value;
					m_view.val(attribute, value);
				},

				setContext : function(context){
//					m_clearContext();
					
					m_context = context;
					
					var contextValue = m_context[m_dataBinding.data.name];
					var optionsWraper = NosliwOptionsManager.getOptions(contextValue.ID, getAbsolutePathOfContextData(m_context, m_dataBinding.data));
					handleDataContainerEachElement(optionsWraper, function(key, eleWraper, eleContext){
						m_addEle(key, eleWraper, eleContext);
					});

				},
				
				updateView : function(){
					m_updateView();
				},
				
				init : function(){
					
				},
				
			};

		var m_addEle = function(key, dataWraper, eleContext){
	    	var eleResourceView = createUIResourceView(m_uiTag, m_parentResourceView.getIdNameSpace()+"."+key, m_parentResourceView);
	    	eleResourceView.key = key;
	    	m_childResourceViews[key] = eleResourceView;
	    	
	 		var context = {};
	 		var contextEleName = m_attributes['context'];
	 		context[contextEleName] = eleContext;
	 		
	 		eleResourceView.setContext(context);

	 		m_view.append(eleResourceView.getViews().children());
		};

		var m_registerContextListener = function(){
			m_contextDataBinding = registerContextPathEvent(m_context, m_dataBinding.data, [EVENT_DATA_CHANGE], function(){
				m_updateView();
				
				NosliwErrorManager.clearErrorMessage(m_reqInfo);
			});
		};
		
		var m_updateView = function(){
			var value = m_getDataBindingValue();
			m_view.val(value);
			
		};
		
		var m_getDataBindingValue = function(){
			return getContextPathWraper(m_context, m_dataBinding.data).data;
		};
		
		var m_setupElementEvent = function(){
			m_view.bind('change', function(){
				m_setDataBindingValue();
			});
		};
		
		var m_setDataBindingValue = function(){
			var value = m_view.val();
			
			var serviceData = requestSetContextPathValue(m_context, m_dataBinding.data, value, m_reqInfo);
		};

		var m_clearContext = function(){
			if(m_contextDataBinding!=undefined)			unregisterEntityContextEvent(m_contextDataBinding);
		};
		
		m_setupElementEvent();
		
		return m_uiTagView;
	};
