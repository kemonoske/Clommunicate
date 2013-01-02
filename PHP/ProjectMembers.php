<?php 

require_once './include/db_connect.php';

if($_POST[id])	{
	
	$response  = mysql_query("SELECT * 
		FROM users
		WHERE id IN (
			SELECT id_usr
			FROM project_group
			WHERE id_proj = '".$_POST[id]."')
			OR
			id = (
			SELECT owner
			FROM projects
			WHERE id = '".$_POST[id]."')
		LIMIT 0,9999"
			);
		$i = 0;
	while($result = mysql_fetch_assoc($response)){
		//$output[state] = true;
		$output[$i][id] = $result[id];
		$output[$i][email] = $result[email];
		$output[$i][name] = $result[name];
		$output[$i][location] = $result[location];
		$output[$i][photo] = $result[photo];
		$output[$i][gender] = $result[gender];
		$i++;
	}	
	if(!$i)
		$output[state] = false;
	echo json_encode($output);
}	else {
	$output[state] = false;
	echo json_encode($output);
}


?>