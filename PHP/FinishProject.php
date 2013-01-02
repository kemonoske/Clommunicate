<?php 

require_once './include/db_connect.php';

	if($_POST[id] && mysql_query("UPDATE projects
		set end_date = CURDATE()
		WHERE id = '".$_POST[id]."'"))	{
		$output[state] = true;
	}	else {
		$output[state] = false;
	}

	echo json_encode($output);

?>