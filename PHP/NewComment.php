<?php 

require_once './include/db_connect.php';

if($_POST[owner] &&
	$_POST[text] &&
	$_POST[author])	{
		
	if(mysql_query("INSERT INTO comments(id, text, author, time)
			VALUES (null, '".$_POST[text]."','".$_POST[author]."', null);",$mysql_server))	{
		
		$comment_index = mysql_insert_id();
		if(mysql_query("INSERT INTO task_comments(id_task, id_comment)
			VALUES ('".$_POST[owner]."', '".$comment_index."');",$mysql_server))	{
			$output[state] = true;
			$output[cause] = 'Registrarea a fost efectuata cu succes.';	
			echo json_encode($output);
		}
	}	else {

		$output[state] = false;
		$output[cause] = mysql_error();
		echo json_encode($output);
		
	}
}	else {
	$output[state] = false;
	$output[cause] = 'Nu a fost indicat unul din cimpurile obligatorii.';
	echo json_encode($output);
}


?>