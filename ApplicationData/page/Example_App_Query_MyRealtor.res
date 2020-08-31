<!DOCTYPE html>
<html>
<body>
	<br>
************************************   Query  **************************************

	<br>
	SchoolType:<nosliw-textinput id="schoolType" data="criteria.schoolType"/>
	<br>

	<br>
	SchoolType:<nosliw-options id="schoolType" data="criteria.schoolType"/>
	<br>

	<br>
	SchoolRating:<nosliw-float data="criteria.schoolRating"/>  
	<br>

	<br>
	From:<nosliw-price data="criteria.fromPrice"/>
	<br>
	To:<nosliw-price data="criteria.toPrice"/>
	
	<br>
	BuildingType:<nosliw-multioptions id="buildingType" data="criteria.buildingType"/>
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
						value: {
							value : "Public",
							optionsId : "schoolType"
						}
					},
					schoolRating : {
						dataTypeId: "test.float;1.0.0",
						value: 9.0
					},
					buildingType : {
						dataTypeId: "test.array;1.0.0",
						value: [
							{
								dataTypeId: "test.options;1.0.0",
								value: {
									value : "House",
									optionsId : "buildingType"
								}
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

