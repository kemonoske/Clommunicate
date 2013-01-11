<?php 

require_once './include/db_connect.php';

if($_POST[id])	{
	
	$response  = mysql_query("SELECT * 
		FROM tasks
		WHERE owner = '".$_POST[id]."'
		LIMIT 0,9999"
			);
		$i = 0;
	while($result = mysql_fetch_assoc($response)){
		//$output[state] = true;
		$output[$i][id] = $result[id];
		$output[$i][name] = $result[name];
		$output[$i][description] = $result[description];
		$output[$i][type] = $result[type];
		$output[$i][start_date] = $result[start_date];
		$output[$i][end_date] = $result[end_date];
		$output[$i][completed] = $result[completed];
		$output[$i][owner] = $result[owner];
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