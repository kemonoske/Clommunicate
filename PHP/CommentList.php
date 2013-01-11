<?php 

require_once './include/db_connect.php';

if($_POST[id])	{
	
	$response  = mysql_query("SELECT id, text, author, time
		FROM comments 
		WHERE id IN (
			SELECT id_comment
			FROM task_comments
			WHERE id_task = '".$_POST[id]."')
		ORDER BY time
		LIMIT 0,9999"
			);
		$i = 0;
	while($result = mysql_fetch_assoc($response)){
		//$output[state] = true;
		$output[$i][id] = $result[id];
		$output[$i][text] = $result[text];
		$output[$i][author] = $result[author];
		$output[$i][time] = $result[time];
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