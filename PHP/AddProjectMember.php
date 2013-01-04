<?php 

require_once './include/db_connect.php';

if($_POST[pid] && $_POST[uid])	{
	
	$response = mysql_query("INSERT INTO project_group 
		VALUES('".$_POST[uid]."','".$_POST[pid]."')"
			);
	if(mysql_affected_rows())
		$output[state] = true;
	else
		$output[state] = false;
	echo json_encode($output);
}	else {
	$output[state] = false;
	echo json_encode($output);
}


?>