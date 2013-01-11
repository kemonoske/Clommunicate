<?php 

require_once './include/db_connect.php';

if($_POST[id])	{
	
	$response = mysql_query("DELETE 
		FROM comments
		WHERE id = '".$_POST[id]."'"
			);
		$output[state] = true;
		mysql_query("DELETE 
		FROM task_comments
		WHERE id_comment = '".$_POST[id]."'"
			);
	echo json_encode($output);
}	else {
	$output[state] = false;
	echo json_encode($output);
}


?>