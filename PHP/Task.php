<?php 

require_once './include/db_connect.php';

if($_POST[id])	{
	
	$response  = mysql_query("SELECT t.id, t.name, t.description, t.type, t.start_date, t.end_date, t.completed, t.owner, u.id uid, u.name uname, u.email uemail, u.photo uphoto
		FROM tasks t 
			JOIN users u ON (t.asigned = u .id)
		WHERE t.id = '".$_POST[id]."'
		LIMIT 0,9999"
			);
		echo mysql_error();
	if($result = mysql_fetch_assoc($response)){
		$output[state] = true;
		$output[id] = $result[id];
		$output[name] = $result[name];
		$output[description] = $result[description];
		$output[type] = $result[type];
		$output[start_date] = $result[start_date];
		$output[end_date] = $result[end_date];
		$output[completed] = $result[completed];
		$output[owner] = $result[owner];
		$output[uid] = $result[uid];
		$output[uname] = $result[uname];
		$output[uemail] = $result[uemail];
		$output[uphoto] = $result[uphoto];
	}	else
		$output[state] = false;
	echo json_encode($output);
}	else {
	$output[state] = false;
	echo json_encode($output);
}


?>