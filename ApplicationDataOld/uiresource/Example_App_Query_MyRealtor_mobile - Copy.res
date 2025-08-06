<!DOCTYPE html>
<html>
<body>
	<br>
************************************   Query  **************************************

	<br>
	SchoolType:<nosliw-string id="schoolType" data="schoolType"/>
	<br>

	<br>
	SchoolType:<nosliw-options id="schoolType" data="schoolType"/>
	<br>

	<br>
	SchoolRating:<nosliw-float data="schoolRating"/>  
	<br>

	<br>
<!--	From:<nosliw-price data="fromPrice"/>  -->
	<br>
<!--	To:<nosliw-price data="toPrice"/>  -->
	
	<br>
<!--	BuildingType:<nosliw-multioptions id="buildingType" data="buildingType"/>-->
	<br>
	
</body>

	<context>
	{
		public : {
			schoolType : {
				definition : "test.options;1.0.0",
				default : {
					dataTypeId: "test.options;1.0.0",
					value: {
						value : "Public",
						optionsId : "schoolType"
					}
				}
			},
			schoolRating : {
				definition : "test.float;1.0.0",
				default : {
					dataTypeId: "test.float;1.0.0",
					value: 9.0
				}
			},
			buildingType : {
				definition : "test.array;1.0.0%||element:test.options;1.0.0||%",
				default : {
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
				}
			},
			fromPrice : {
				definition : "test.price;1.0.0",
				default : {
					dataTypeId: "test.price;1.0.0",
					value: {
						price : 300000,
						currency : "$"
					}
				}
			},
			toPrice : {
				definition : "test.price;1.0.0",
				default : {
					dataTypeId: "test.price;1.0.0",
					value: {
						price : 700000,
						currency : "$"
					}
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
