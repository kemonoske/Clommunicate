<?php 

require_once './include/db_connect.php';

if($_POST[id])	{
	
	$response = mysql_query("UPDATE tasks 
		SET end_date = CURDATE(),
			completed = '".$_POST[completed]."'
		WHERE id = '".$_POST[id]."'"
			);
		$output[state] = true;
	echo json_encode($output);
}	else {
	$output[state] = false;
	echo json_encode($output);
}


?>