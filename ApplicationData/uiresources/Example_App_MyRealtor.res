<!DOCTYPE html>
<html>
	<body>
	
	<br>
	<nosliw-submit title="Submit" datasource="myrealtor" parms="schoolType:criteria.schoolType;schoolRating:criteria.schoolRating;buildingType:criteria.buildingType;fromPrice:criteria.fromPrice;toPrice:criteria.toPrice" output="result"/>  
	<br>

	<div>
	Results:
	<br>
	<nosliw-include source="Example_App_Result_MyRealtor"/> 
	</div>

	<div>
	Query:
	<br>
	<nosliw-include source="Example_App_Query_MyRealtor"/> 
	</div>

	<nosliw-debug/>
	
	<br>
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
	}
	</context>
	
		<!-- This part can be used to define expressions
		-->
	<expressions>
	{
		
	
	}
	</expressions>
	
</html>

