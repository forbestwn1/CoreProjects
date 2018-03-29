<!DOCTYPE html>
<html>
	<body>
	
	<br>
	<nosliw-submit title="School" datasource="school" parms="schoolType:criteria.schoolType;schoolRating:criteria.schoolRating"  output="schoolresult"/>  
	<br>
	
	<div>
	Schools
	<br>
	<nosliw-include source="Example_App_Result_School"/> 
	</div>

	<div>
	<br>
	Result
	<br>
	<nosliw-include source="Example_App_Query"/> 
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

