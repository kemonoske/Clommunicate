<?php 

require_once './include/db_connect.php';

if($_POST[name] && 
		$_POST[description] &&
		$_POST[type] &&
		$_POST[id] &&
		$_POST[asigned])	{
		
	if(mysql_query("UPDATE tasks
		SET name = '".$_POST[name]."',
			description = '".$_POST[description]."',
			type = '".$_POST[type]."',
			asigned = '".$_POST[asigned]."'
		WHERE id = '".$_POST[id]."';",$mysql_server))	{
		$output[state] = true;
		$output[cause] = 'Registrarea a fost efectuata cu succes.';	
		echo json_encode($output);
			
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