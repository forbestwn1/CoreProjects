<!DOCTYPE html>
<html>
<body>
******************************	Result  ********************************
		<br>
		Sum:<%=#|?(schoolresult)?.length()|#.value%>
		<br>
		
		<nosliw-map data="schoolresult" element="ele" index="index">
			<div style="height:40px;width:200px;">
				<br>
				Name: <%=?(ele.schoolName)?.value%>   
				<br>
				Rating: <%=?(ele.schoolRating)?.value%>   
			</div>
		</nosliw-map>
		
</body>



	<script>
	{
	
	}
	</script>
	
	<constants>
	{
	}
	</constants>
	
		<!-- This part can be used to define context (variable)
				it describle data type criteria for each context element and its default value
		-->
	<context>
	{
		public : {
			schoolresult : {
				definition : "test.array;1.0.0",
				default : {
					dataTypeId: "test.array;1.0.0",
					value: [
						{
							dataTypeId: "test.map;1.0.0",
							value: {
								schoolName : {
									dataTypeId: "test.string;1.0.0",
									value: "School1"
								},
								schoolRating : {
									dataTypeId: "test.float;1.0.0",
									value: 6.0
								},
								geo : {
									dataTypeId: "test.geo;1.0.0",
									value: {
										"latitude" :  43.651299,
										"longitude" : -79.579473
									}
								}
							}
						},
						{
							dataTypeId: "test.map;1.0.0",
							value: {
								schoolName : {
									dataTypeId: "test.string;1.0.0",
									value: "School2"
								},
								schoolRating : {
									dataTypeId: "test.float;1.0.0",
									value: 8.5
								},
								geo : {
									dataTypeId: "test.geo;1.0.0",
									value: {
										"latitude" :  43.649016, 
										"longitude" : -79.485059
									}
								}
							}
						}					
					]
				}
			},
		}
	}
	</context>
	
		<!-- This part can be used to define expressions
		-->
	<expressions>
	{
		
	
	}
	</expressions>
	
</html>

