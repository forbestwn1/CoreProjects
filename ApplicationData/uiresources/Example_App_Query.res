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
					schoolType : "test.string;1.0.0",
					buildingType : "test.array;1.0.0%||element:test.string;1.0.0||%",
					fromPrice : "test.price;1.0.0",
					toPrice : "test.price;1.0.0",
				},
				default: {
					schoolType : {
						dataTypeId: "test.string;1.0.0",
						value: "Public"
					},
					buildingType : {
						dataTypeId: "test.array;1.0.0",
						value: [
							{
								dataTypeId: "test.string;1.0.0",
								value: "House"
							},						
						]
					},
					fromPrice : {
						dataTypeId: "test.price;1.0.0",
						value: 300000
					},
					toPrice : {
						dataTypeId: "test.price;1.0.0",
						value: 700000
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

