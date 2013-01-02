<?php 

require_once './include/db_connect.php';

if($_POST[id])	{
	
	$response = mysql_query("DELETE 
		FROM projects
		WHERE id = '".$_POST[id]."'"
			);
	if(mysql_affected_rows())	{
		$output[state] = true;
		mysql_query("DELETE 
		FROM project_group
		WHERE id_proj = '".$_POST[id]."'"
			);
	}else
		$output[state] = false;
	echo json_encode($output);
}	else {
	$output[state] = false;
	echo json_encode($output);
}


?>