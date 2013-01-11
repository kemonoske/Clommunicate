<?php 

require_once './include/db_connect.php';

if($_POST[pid] && $_POST[uid])	{
	
	$response = mysql_query("DELETE 
		FROM project_group
		WHERE id_proj = '".$_POST[pid]."' AND id_usr = '".$_POST[uid]."'"
			);
	if(mysql_affected_rows())	{
		$output[state] = true;
		mysql_query("UPDATE tasks
		SET asigned = (SELECT owner
			FROM projects
			WHERE id = '".$_POST[pid]."')
		WHERE asigned = '".$_POST[uid]."'"
			);
	}	else
		$output[state] = false;
	echo json_encode($output);
}	else {
	$output[state] = false;
	echo json_encode($output);
}


?>