<!DOCTYPE html>
<html>
<body>
	<br>
************************************   Query  **************************************

	<br>
	SchoolType:<nosliw-textinput data="criteria.schoolType"/>  
	<br>

	<br>
	SchoolType:<nosliw-options id="schoolType" data="criteria.schoolType"/>
	<br>

	<br>
	BuildingType:<nosliw-multioptions id="buildingType" data="criteria.buildingType"/>
	<br>

	<br>
<!--	Price: <nosliw-price from="criteria.fromPrice" to="criteria.toPrice" min="1000" max="2000000"/>  -->
	<br>
	
</body>

	<context>
	{
		public : {
			criteria : {
				definition: {
					schoolType : "test.options;1.0.0",
					schoolRating : "test.float;1.0.0",
					buildingType : "test.array;1.0.0%||element:test.options;1.0.0||%",
					fromPrice : "test.price;1.0.0",
					toPrice : "test.price;1.0.0",
				},
				default: {
					schoolType : {
						dataTypeId: "test.options;1.0.0",
						value: "Public"
					},
					schoolRating : {
						dataTypeId: "test.float;1.0.0",
						value: 5.0
					},
					buildingType : {
						dataTypeId: "test.array;1.0.0",
						value: [
							{
								dataTypeId: "test.options;1.0.0",
								value: "House"
							},						
						]
					},
					fromPrice : {
						dataTypeId: "test.price;1.0.0",
						value: {
							price : 300000,
							currency : "$"
						}
					},
					toPrice : {
						dataTypeId: "test.price;1.0.0",
						value: {
							price : 700000,
							currency : "$"
						}
					},
				}
			},
		}
	}
	</context>

	<script>
	{
	
	}
	</script>
	
	<constants>
	{
	}
	</constants>
	

		<!-- This part can be used to define expressions
		-->
	<expressions>
	{
		
	
	}
	</expressions>
	
</html>

