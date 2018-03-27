<!DOCTYPE html>
<html>
<body>
******************************	Result  ********************************
		<br>
		Sum:<%=#|?(result)?.length()|#.value%>
		<br>
		
		<nosliw-map data="result" element="ele" index="index">
			<div style="height:40px;width:200px;">
				Price: <%=?(ele.price)?.value.price%>   
			</div>
		</nosliw-map>
		
		
		<nosliw-loop data="result" element="ele" index="index">  
			<br>
			Name : <%=?(ele.name)?.value%>   
			<br>
			Price: <%=?(ele.price)?.value.price%>   
			<br>
		</nosliw-loop>
		

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
			result : {
				definition : "test.array;1.0.0",
				default : {
					dataTypeId: "test.array;1.0.0",
					value: [
						{
							dataTypeId: "test.map;1.0.0",
							value: {
								name : {
									dataTypeId: "test.string;1.0.0",
									value: "School1"
								},
								price : {
									dataTypeId: "test.price;1.0.0",
									value: {
										"price" : 500000,
										"currency" : "$"
									}
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
								name : {
									dataTypeId: "test.string;1.0.0",
									value: "School2"
								},
								price : {
									dataTypeId: "test.price;1.0.0",
									value: {
										"price" : 700000,
										"currency" : "$"
									}
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

