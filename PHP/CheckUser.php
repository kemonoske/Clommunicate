<?php 

	require_once './include/db_connect.php';
	
	
	if($_POST[email])	{

		$users = mysql_query('SELECT email FROM users;',$mysql_server);
		while($user = mysql_fetch_assoc($users))
			if(strcmp($_POST[email], $user[email]) == 0)	{
			
				$output[status] = true;
				echo json_encode($output);
				die();
			
			}
		
	}

?>