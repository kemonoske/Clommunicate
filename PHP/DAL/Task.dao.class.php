<?php


/**
 * Used to describe connection between Task entity
 * and  tasks table in database
 * @author Akira
 *
 */
class TaskDAO {

	const ADD = 1;
	const UPDATE = 2;
	const GET_BY_ID = 3;
	const GET_ALL_BY_PROJECT_ID = 4;
	const REMOVE = 5;
	const MARK_COMPLETED = 6;

	/**
	 * Checks if task with such id is contained in tasks table
	 * @param int $ident
	 */
	static function exists($ident) {
	
		require_once 'DBO.class.php';
	
		$db = DBO::getInstance();
		$ids = $db->prepare('SELECT id FROM tasks');
		$ids->execute();
		if($ids->errorCode() != 0)	{
			$error = $ids->errorInfo();
			throw new Exception($error[2]);
		}
	
		while($id = $ids->fetch())
			if($ident == $id[id])
				return true;
	
		return false;
	}
	

	/**
	 * Add new task object to database
	 * @param Task $task - task object
	 * @throws Exception
	 * @return boolean
	 */
	static function addTask($task){

		require_once 'DBO.class.php';
		require_once 'Task.class.php';

		$db = DBO::getInstance();

		$statement = $db->prepare('
				INSERT INTO tasks(id, name, description, type, start_date, end_date, completed, owner, assigned)
				VALUES (null, :name, :description, :type, CURDATE(), null, \'0\', :owner, :assigned)');
		$statement->execute(array(
				':name' => $task->getName(),
				':description' => $task->getDescription(),
				':type' => $task->getType(),
				':owner' => $task->getOwner(),
				':assigned' => $task->getAssigned()));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}

		return true;


	}

	/**
	 * update task data with new one
	 * @param Task $task - task object with new data
	 * @throws Exception
	 * @return boolean
	 */
	static function updateTask($task){

		require_once 'DBO.class.php';
		require_once 'Task.class.php';

		$db = DBO::getInstance();

		$statement = $db->prepare('UPDATE tasks
				SET name = :name,
					description = :description,
					type = :type,
					assigned = :assigned
				WHERE id = :tid');
		$statement->execute(array(
				':name' => $task->getName(),
				':description' => $task->getDescription(),
				':type' => $task->getType(),
				':tid' => $task->getId(),
				':assigned' => $task->getAssigned()));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}

		if(!$statement->rowCount())
			throw new Exception("No task with such id, or nothing to edit.");
		
		return true;

	}

	/**
	 * Return task object from database based on task id
	 * @param int $tid - task id
	 * @throws Exception
	 * @return Task
	 */
	static function getTaskById($tid){
		
		require_once 'DBO.class.php';
		require_once 'Task.class.php';
		
		$db = DBO::getInstance();
		
		$statement = $db->prepare('SELECT *
				FROM tasks
				WHERE id = :tid
				');
		$statement->execute(array(
				':tid' => $tid));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}
		
		if($row = $statement->fetch())	{

			require_once 'User.class.php';
			require_once 'User.dao.class.php';
			
			$assigned = User::serialize(UserDAO::getUserById($row[assigned]));
			
			$task = new Task(
					$row[id], 
					$row[name], 
					$row[description], 
					$row[start_date], 
					$row[end_date], 
					$row[type], 
					$row[completed], 
					$row[owner], 
					$assigned);
			
		} else
			throw new Exception("no task with such id.");
		
		return $task;

	}

	/**
	 * Get a list of tasks using owner project id
	 * @param int $pid - project id
	 * @throws Exception
	 * @return unknown
	 */
	static function getAllTasksByProjectId($pid){

		require_once 'DBO.class.php';
		require_once 'Task.class.php';

		$db = DBO::getInstance();

		$statement = $db->prepare('SELECT *
				FROM tasks
				WHERE owner = :pid
				LIMIT 0,9999
				');
		$statement->execute(array(
				':pid' => $pid));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}

		$i = 0;

		while($row = $statement->fetch())	{

			$task = new Task(
					$row[id],
					$row[name],
					$row[description],
					$row[start_date],
					$row[end_date],
					$row[type],
					$row[completed],
					$row[owner],
					$row[assigned]);
			
			
			$tasks[$i++] = Task::serialize($task);

		}

		return $tasks;
		

	}

	/**
	 * Delete task  with specified id from database
	 * @param int $tid - task id
	 * @throws Exception
	 * @return boolean
	 */
	static function removeTask($tid)	{

		require_once 'DBO.class.php';
		
		$db = DBO::getInstance();
		
		$statement = $db->prepare('DELETE FROM tasks
				WHERE id = :tid');
		$statement->execute(array(
				':tid' => $tid));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}
		
		return true;

	}

	/**
	 * Marks task as completed or not
	 * @param int $tid - task id
	 * @throws Exception
	 * @return boolean
	 */
	static function markCompleted($tid, $completed){
	
		require_once 'DBO.class.php';
	
		$db = DBO::getInstance();
	
		$statement = $db->prepare('UPDATE tasks
				set completed = :completed,
					end_date = CURDATE()
				WHERE id = :tid');
	
		$statement->execute(array(
				':tid' => $tid,
				':completed' => $completed));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}
	
		if(!$statement->rowCount())
			throw new Exception("Cant update task, the task may have been modified by other user.");
	
		return true;
	
	}

}
