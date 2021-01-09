{
	name : "map",
	type : "data",
	base : "arrayData",
	description : "",
	attributes : [
		{
			name : "data",
		},
		{
			name : "element",
			defaultValue : "element", 
		},
		{
			name : "index",
			defaultValue : "index", 
		}	
	],
	context: {
		group : {
			private : {
				element : {
					"internal_data": {
						definition : {
							path : "<%=&(nosliwattribute_data)&%>",
							definition : {
								criteria : "test.array;1.0.0"
							}
						},
					}
				},
			},
			protected : {
				element : {
					"<%=&(nosliwattribute_element)&%>" : {
						definition : {
							path : "<%=&(nosliwattribute_data)&%>.element",
						},
						info:{
							instantiate : "manual"
						}
					},
					"<%=&(nosliwattribute_index)&%>" : {
						definition : {
							criteria : "test.integer;1.0.0",
						},
						info:{
							instantiate : "manual"
						}
					}		
				},
			},
		},
		info : {
			inherit : "true",
		}
	},
	event : [
		
	],
	script : function(base){

		var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
		var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");

		var loc_base = base;
		var loc_contentView;

		var loc_map;
		var loc_infowindow;
		var loc_elements = [];

		var loc_getBuildContentView = function(){

			var extendeContextInfo = [];
			extendeContextInfo.push(loc_base.getEnv().createContextElementInfo(loc_base.getElementVariableName(), undefined));

			var eleContext = loc_base.getEnv().createExtendedContext(extendeContextInfo, requestInfo);

			var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
			out.addRequest(loc_base.getEnv().getCreateUIViewWithIdRequest(id, eleContext, {
				success : function(requestInfo, resourceView){
					loc_contentView = resourceView;
					loc_contentView.appendTo(loc_infowindow.getContent());
					return resourceView;
				}
			}));
			return out;
		};
		

		/**
		*  eleVar : variable for element
		*  indexVar : index variable for index of element
		*  path : element's path from parent
		**/
		var loc_getCreateElementViewRequest = function(id, index, variables, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
			out.addRequest(loc_base.getEnv().getDataOperationRequestGet(variables[loc_base.getElementVariableName()], undefined, {
				success : function(request, data){
					var kkkk = 5555;
				}
			}));
			out.addRequest(loc_base.getEnv().getDataOperationRequestGet(variables[loc_base.getElementVariableName()], "geo", {
				success : function(request, data){
					var marker = new google.maps.Marker({
						position: {lat: data.value.value.latitude, lng: data.value.value.longitude},
						map: loc_map
					});

					var ele = {};
					ele.marker = marker;
					ele.elementVar = variables[loc_base.getElementVariableName()];
					ele.indexVar = variables[loc_base.getIndexVariableName()];
					
					loc_elements.splice(index, 0, ele);

					marker.addListener('click', function() {
						loc_infowindow.open(loc_map, marker);
					});
					
				}
			}));
			return out;
		};

		var loc_initMap = function() {
			var uluru = {lat: 43.751319, lng: -79.407853};
			loc_map = new google.maps.Map(loc_view.get(0), {
			  zoom: 11,
			  center: uluru
			});
			
			loc_infowindow = new google.maps.InfoWindow({
				content: $('<div style="height:100px;width:200px;">').get(0)
			});
	    };
	

		var loc_out = 
		{
			initViews : function(requestInfo){	
				loc_view = $('<div id="map" style="height:600px;width:100%;"></div>');	
				loc_initMap();
				return loc_view;
			},
	
			getCreateElementViewRequest : function(id, index, variables, handlers, requestInfo){
				return loc_getCreateElementViewRequest(id, index, variables, handlers, requestInfo);
			},

			postInit : function(requestInfo){
//				loc_updateView();
			},

			deleteElement : function(index, requestInfo){
				var view = loc_childViews[index];
				view.detachViews();
				view.destroy(requestInfo);
				loc_childViews.splice(index, 1);
			}, 

			destroy : function(request){
			},
			
		};
		return loc_out;
	}
}
