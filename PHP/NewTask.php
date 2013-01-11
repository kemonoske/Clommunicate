<?php 

require_once './include/db_connect.php';

if($_POST[name] && 
		$_POST[description] &&
		$_POST[type] &&
		$_POST[owner] &&
		$_POST[asigned])	{
		
	if(mysql_query("INSERT INTO tasks(id, name, description, type, start_date, end_date, completed, owner, asigned)
			VALUES (null, '".$_POST[name]."','".$_POST[description]."','".$_POST[type]."',CURDATE(), null,'0','".$_POST[owner]."','".$_POST[asigned]."');",$mysql_server))	{
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