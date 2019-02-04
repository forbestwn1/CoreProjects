<!DOCTYPE html>
<html>
	<body>
	
	<br>
	<nosliw-submit title="Submit" datasource="school" parms="schoolType:criteria.schoolType;schoolRating:criteria.schoolRating"  output="result"/>  
	<br>

	<div>
	Query
	<br>
	<nosliw-include source="Page_MySchool_Query"/> 
	</div>
	
	<div>
	Result
	<br>
	<nosliw-include source="Page_MySchoolList"/> 
	</div>

	<br>

	</body>

	<service>
	{
		"serviceName" : "",
		"parmMapping" : {
			"element" : {
				
			}
		},
		"resultMapping" : {
			
		}
	}
	</service>
	
</html>
