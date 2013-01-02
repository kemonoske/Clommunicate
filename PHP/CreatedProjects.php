<?php 

require_once './include/db_connect.php';

if($_POST[user_id])	{
	
	$response  = mysql_query("SELECT * 
		FROM projects
		WHERE owner = '".$_POST[user_id]."'
		LIMIT 0,9999"
			);
		$i = 0;
	while($result = mysql_fetch_assoc($response)){
		//$output[state] = true;
		$output[$i][id] = $result[id];
		$output[$i][name] = $result[name];
		$output[$i][description] = $result[description];
		$output[$i][start_date] = $result[start_date];
		$output[$i][deadline] = $result[deadline];
		$output[$i][end_date] = $result[end_date];
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