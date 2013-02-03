<?php

class ProjectDAO {

	const ADD_MEMBER = 1;
	const LIST_BY_OWNER = 2;
	const LIST_BY_MEMBER = 3;
	const MARK_COMPLETED = 4;
	const ADD = 5;
	const LIST_MEMBERS = 6;
	const REMOVE = 7;
	const REMOVE_MEMBER = 8;
	const UPDATE = 9;
	const GET = 10;

	/**
	 * Checks if project with such email is contained in projects table
	 * @param int $ident
	 */
	static function exists($ident) {
	
		require_once 'DBO.class.php';
	
		$db = DBO::getInstance();
		$ids = $db->prepare('SELECT id FROM projects');
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
	 * User to add project object to projects table
	 * @param Project $project - project object containing field data
	 * @return boolean - if adding succeed
	 */
	static function addProject($project, $members)	{

		require_once 'DBO.class.php';
		require_once 'Project.class.php';

		$db = DBO::getInstance();

		$db->beginTransaction();
		$statement = $db->prepare('
				INSERT INTO projects(id, name, description, owner, deadline, start_date)
				VALUES (null, :name, :description, :owner, :deadline, CURDATE())');
		$statement->execute(array(
				':name' => $project->getName(),
				':description' => $project->getDescription(),
				':owner' => $project->getOwner(),
				':deadline' => $project->getDeadline()));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}
		$project->setId($db->lastInsertId());
		$statement = $db->prepare('
				INSERT INTO project_group(id_usr, id_proj)
				VALUES (:id_usr, :id_proj)');
		$i = 0;
		while(isset($members[$i]))	{
			$statement->execute(array(
					':id_usr' => $members[$i++],
					':id_proj' => $project->getId()));

			if($statement->errorCode() != 0)	{
				$error = $statement->errorInfo();
				throw new Exception($error[2]);
			}
		}
		$db->commit();

		return true;

	}

	/**
	 * Updates project data with new one
	 * @param Project $project - project object containing new data
	 * @throws Exception
	 * @return boolean
	 */
	static function updateProject($project)	{

		require_once 'DBO.class.php';
		require_once 'Project.class.php';

		$db = DBO::getInstance();

		$statement = $db->prepare('
				UPDATE projects
				SET name = :name,
					description = :description,
					deadline = :deadline
				WHERE id = :pid');
		$statement->execute(array(
				':name' => $project->getName(),
				':description' => $project->getDescription(),
				':pid' => $project->getId(),
				':deadline' => $project->getDeadline()));
		
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}

		if(!$statement->rowCount())
			throw new Exception("No project with such id.");
		
		return true;

	}

	/**
	 * Add a member to a project group
	 * @param int $pid - project id
	 * @param int $uid - user id
	 * @throws Exception - if something goes wrong
	 * @return boolean - if succes true is returned
	 */
	static function addMember($pid, $uid){

		require_once 'DBO.class.php';

		$db = DBO::getInstance();

		$statement = $db->prepare('
				INSERT INTO project_group(id_usr, id_proj)
				VALUES (:id_usr, :id_proj)');
		$statement->execute(array(
				':id_usr' => $uid,
				':id_proj' => $pid));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}

		return true;

	}

	/**
	 * Marks project as completed setting end date to current
	 * @param int $pid - project id
	 * @throws Exception - if something goes wrong
	 * @return boolean
	 */
	static function markCompleted($pid){

		require_once 'DBO.class.php';

		$db = DBO::getInstance();

		$statement = $db->prepare('UPDATE projects
				set end_date = CURDATE()
				WHERE id = :pid');

		$statement->execute(array(
				':pid' => $pid));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}

		if(!$statement->rowCount())
			throw new Exception("No project with such id.");

		return true;

	}

	/**
	 * Removes project
	 * @param int $pid - project id
	 * @throws Exception - if something goes wrong
	 * @return boolean
	 */
	static function removeProject($pid){

		require_once 'DBO.class.php';

		$db = DBO::getInstance();
		
		$statement = $db->prepare('DELETE
				FROM projects
				WHERE id = :pid');

		$statement->execute(array(
				':pid' => $pid));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}

		return true;

	}

	/**
	 * Removes a member from the project and reasign
	 * all his tasks to project owner
	 * @param int $pid - project id
	 * @param int $uid - member id
	 * @throws Exception
	 * @return boolean
	 */
	static function removeProjectMember($pid, $uid){

		require_once 'DBO.class.php';

		$db = DBO::getInstance();

		$db->beginTransaction();
		$statement = $db->prepare('DELETE
				FROM project_group
				WHERE id_usr = :uid AND id_proj = :pid');
		$statement->execute(array(
				':pid' => $pid,
				':uid' => $uid));

		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}

		$statement = $db->prepare('UPDATE tasks
				SET assigned = (SELECT owner
				FROM projects
				WHERE id = :pid)
				WHERE assigned = :uid');
		$statement->execute(array(
				':pid' => $pid,
				':uid' => $uid));

		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}

		$db->commit();

		return true;

	}
	/**
	 * Gets all projects that user is member of
	 * @param int $uid - member id
	 * @throws Exception
	 * @return list of projects
	 */
	static function getAllByMember($uid){

		require_once 'DBO.class.php';
		require_once 'Project.class.php';

		$db = DBO::getInstance();

		$statement = $db->prepare('SELECT *
				FROM projects
				WHERE id IN (
				SELECT id_proj
				FROM project_group
				WHERE id_usr = :uid)
				LIMIT 0 , 99999
				');
		$statement->execute(array(
				':uid' => $uid));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}

		$i = 0;

		while($row = $statement->fetch())	{

			$project = new Project(
					$row[id],
					$row[name],
					$row[description],
					$row[start_date],
					$row[deadline],
					$row[end_date],
					$row[owner]);

			$projects[$i++] = Project::serialize($project);

		}

		return $projects;

	}

	/**
	 * Gets all projects that user is owner of
	 * @param int $uid - owner id
	 * @throws Exception
	 * @return list of projects
	 */
	static function getAllByOwner($uid){

		require_once 'DBO.class.php';
		require_once 'Project.class.php';

		$db = DBO::getInstance();

		$statement = $db->prepare('SELECT *
				FROM projects
				WHERE owner = :uid
				LIMIT 0,9999
				');
		$statement->execute(array(
				':uid' => $uid));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}

		$i = 0;

		while($row = $statement->fetch())	{

			$project = new Project(
					$row[id],
					$row[name],
					$row[description],
					$row[start_date],
					$row[deadline],
					$row[end_date],
					$row[owner]);

			$projects[$i++] = Project::serialize($project);

		}

		return $projects;

	}
	
	/**
	 * Get the project object from database
	 * @param int $pid - project id
	 * @throws Exception
	 * @return Project
	 */
	static function getProjectById($pid){

		require_once 'DBO.class.php';
		require_once 'Project.class.php';

		$db = DBO::getInstance();

		$statement = $db->prepare('SELECT *
				FROM projects
				WHERE id = :pid
				');
		$statement->execute(array(
				':pid' => $pid));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}

		if($row = $statement->fetch())	{
			$project = new Project(
					$row[id],
					$row[name],
					$row[description],
					$row[start_date],
					$row[deadline],
					$row[end_date],
					$row[owner]);
		} else
			throw new Exception("no project with such id.");

		return $project;

	}

	/**
	 * Geting list of members from the project including owner
	 * @param int $pid - project id
	 * @throws Exception
	 * @return list of members
	 */
	static function getMembers($pid){

		require_once 'DBO.class.php';
		require_once 'User.class.php';

		$db = DBO::getInstance();

		$statement = $db->prepare('SELECT id, email, name, photo
				FROM users
				WHERE id IN (
				SELECT id_usr
				FROM project_group
				WHERE id_proj = :pid)
				OR
				id = (
				SELECT owner
				FROM projects
				WHERE id = :pid)
				LIMIT 0,9999');
		$statement->execute(array(
				':pid' => $pid));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}

		$i = 0;

		while($row = $statement->fetch())	{

			$member = new User(
					$row[id],
					$row[name],
					$row[email],
					null,
					$row[photo],
					null,
					null,
					null);

			$members[$i++] = User::serialize($member);

		}

		return $members;

	}

}