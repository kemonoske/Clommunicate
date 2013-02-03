<?php

/**
 * Main handler checks the HTTP parameters and executes respective action
 * is basicaly a huge ramification
 *
 * @author Akira
 */
error_reporting(E_ALL);
ini_set('display_errors', 1);

require_once 'include/Entities.enum.php';

$entity = $_POST[entity];

switch ($entity)	{

	case Entities::User :

		require_once 'DAL/User.dao.class.php';
		require_once 'include/Response.class.php';

		$request_type = $_POST[type];

		switch ($request_type)	{

			case UserDAO::EXISTS :

				require_once 'include/Validator.class.php';

				$email = $_POST[email];

				if(isset($email) && Validator::checkEmail($email))	{

					try {

						$exists = UserDAO::exists($email);
						$response = new Response(
								$exists,
								($exists)?null:"No user with such email.",
								null);

					} catch (Exception $e) {

						$response = new Response(
								false,
								$e->getMessage(),
								null);

					}

				}	else
					$response = new Response(
							false,
							"Email address is invalid or not provided.",
							null);

				echo $response->respond();

				break;
					
			case UserDAO::ADD :

				require_once 'include/Validator.class.php';
				require_once 'DAL/User.class.php';

				$email = $_POST[email];
				$name = $_POST[name];
				$locale = $_POST[locale];
				$photo = $_POST[photo];
				$gender = $_POST[gender];

				if(isset($email) && Validator::checkEmail($email) &&
						isset($name) && isset($locale) &&
						isset($photo) && isset($gender))	{

					try {
							
						$user = new User(
								null,
								$name,
								$email,
								$locale,
								$photo,
								$gender,
								null,
								null);

						$status = UserDAO::addUser($user);
						$response = new Response(
								$status,
								($status)?null:"Can't register user.",
								null);
							
					} catch (Exception $e) {
							
						$response = new Response(
								false,
								$e->getMessage(),
								null);
							
					}

				}	else {

					$response = new Response(
							false,
							"Some or all of required parameters are invalid or missing.",
							null);

				}

				echo $response->respond();

				break;
					
			case UserDAO::GET :

				require_once 'include/Validator.class.php';

				$email = $_POST[email];

				if(isset($email) && Validator::checkEmail($email))	{

					try {

						$user = UserDAO::getUser($email);
						$user = User::serialize($user);

						$response = new Response(
								true,
								null,
								$user);

					} catch (Exception $e) {

						$response = new Response(
								false,
								$e->getMessage(),
								null);

					}

				}	else
					$response = new Response(
							false,
							"Email address is invalid or not provided.",
							null);

				echo $response->respond();

				break;
					
			case UserDAO::GET_ALL :

				echo "Not implemented";

				break;
					
			case UserDAO::REMOVE :

				echo "Not implemented";

				break;
					
			case UserDAO::UPDATE :

				echo "Not implemented";

				break;
					
			default:

				echo 'About Users Here';
					
				break;

		}

		break;


	case Entities::Project :

		require_once 'DAL/Project.dao.class.php';
		require_once 'include/Response.class.php';

		$request_type = $_POST[type];

		switch ($request_type){

			case ProjectDAO::ADD:

				require_once 'DAL/Project.class.php';

				$name = $_POST[name];
				$description = $_POST[description];
				$owner = $_POST[owner];
				$deadline = $_POST[deadline];
				$members = $_POST[members];

				if(isset($name) && isset($description) &&
						isset($owner) && isset($deadline) && intval($owner) != 0)	{

					try {
							
						$project = new Project(
								null,
								$name,
								$description,
								null,
								$deadline,
								null,
								$owner);

						$status = ProjectDAO::addProject($project);
						$response = new Response(
								$status,
								($status)?null:"Can't create project.",
								null);
							
					} catch (Exception $e) {
							
						$response = new Response(
								false,
								$e->getMessage(),
								null);
							
					}

				}	else {

					$response = new Response(
							false,
							"Some or all of required parameters are invalid or missing.",
							null);

				}

				echo $response->respond();

				break;

			case ProjectDAO::ADD_MEMBER:

				$pid = $_POST[pid];
				$uid = $_POST[uid];

				if(isset($pid) && intval($pid) != 0 &&
						isset($uid) && intval($uid) != 0)	{

					try {

						$status = ProjectDAO::addMember($pid,$uid);
						$response = new Response(
								$status,
								($status)?null:"Can't add member.",
								null);
							
					} catch (Exception $e) {
							
						$response = new Response(
								false,
								$e->getMessage(),
								null);
							
					}

				}	else {

					$response = new Response(
							false,
							"Some or all of required parameters are invalid or missing.",
							null);

				}

				echo $response->respond();
				break;

			case ProjectDAO::LIST_BY_MEMBER:

				$uid = $_POST[uid];

				if(isset($uid) && intval($uid) != 0)	{

					try {

						$project_list = ProjectDAO::getAllByMember($uid);

						$response = new Response(
								true,
								null,
								$project_list);
							
					} catch (Exception $e) {
							
						$response = new Response(
								false,
								$e->getMessage(),
								null);
							
					}

				}	else {

					$response = new Response(
							false,
							"Some or all of required parameters are invalid or missing.",
							null);

				}

				echo $response->respond();
				break;

			case ProjectDAO::LIST_BY_OWNER:

				$uid = $_POST[uid];

				if(isset($uid) && intval($uid) != 0)	{

					try {

						$project_list = ProjectDAO::getAllByOwner($uid);

						$response = new Response(
								true,
								null,
								$project_list);
							
					} catch (Exception $e) {
							
						$response = new Response(
								false,
								$e->getMessage(),
								null);
							
					}

				}	else {

					$response = new Response(
							false,
							"Some or all of required parameters are invalid or missing.",
							null);

				}

				echo $response->respond();

				break;

			case ProjectDAO::LIST_MEMBERS:

				$pid = $_POST[pid];

				if(isset($pid) && intval($pid) != 0)	{

					try {

						$member_list = ProjectDAO::getMembers($pid);

						$response = new Response(
								true,
								null,
								$member_list);
							
					} catch (Exception $e) {
							
						$response = new Response(
								false,
								$e->getMessage(),
								null);
							
					}

				}	else {

					$response = new Response(
							false,
							"Some or all of required parameters are invalid or missing.",
							null);

				}

				echo $response->respond();

				break;

			case ProjectDAO::MARK_COMPLETED:

				$pid = $_POST[pid];

				if(isset($pid) && intval($pid) != 0)	{
					try {

						$status = ProjectDAO::markCompleted($pid);
						$response = new Response(
								$status,
								($status)?null:"Can't update project.",
								null);
							
					} catch (Exception $e) {
							
						$response = new Response(
								false,
								$e->getMessage(),
								null);
							
					}

				}	else {

					$response = new Response(
							false,
							"Some or all of required parameters are invalid or missing.",
							null);

				}

				echo $response->respond();

				break;

			case ProjectDAO::REMOVE:

				$pid = $_POST[pid];

				if(isset($pid) && intval($pid) != 0)	{
					try {

						$status = ProjectDAO::removeProject($pid);
						$response = new Response(
								$status,
								($status)?null:"Can't remove project.",
								null);
							
					} catch (Exception $e) {
							
						$response = new Response(
								false,
								$e->getMessage(),
								null);
							
					}

				}	else {

					$response = new Response(
							false,
							"Some or all of required parameters are invalid or missing.",
							null);

				}

				echo $response->respond();
				break;

			case ProjectDAO::REMOVE_MEMBER:

				$pid = $_POST[pid];
				$uid = $_POST[uid];

				if(isset($pid) && intval($pid) != 0 &&
						isset($uid) && intval($uid) != 0)	{
					try {

						$status = ProjectDAO::removeProjectMember($pid, $uid);
						$response = new Response(
								$status,
								($status)?null:"Can't remove member.",
								null);
							
					} catch (Exception $e) {
							
						$response = new Response(
								false,
								$e->getMessage(),
								null);
							
					}

				}	else {

					$response = new Response(
							false,
							"Some or all of required parameters are invalid or missing.",
							null);

				}

				echo $response->respond();


				break;

			case ProjectDAO::UPDATE:

				require_once 'DAL/Project.class.php';

				$name = $_POST[name];
				$description = $_POST[description];
				$pid = $_POST[pid];
				$deadline = $_POST[deadline];

				if(isset($name) && isset($description) && isset($deadline) &&
						isset($pid) && intval($pid) != 0)	{

					try {
							
						$project = new Project(
								$pid,
								$name,
								$description,
								null,
								$deadline,
								null,
								null);

						$status = ProjectDAO::updateProject($project);
						$response = new Response(
								$status,
								($status)?null:"Can't update project.",
								null);
							
					} catch (Exception $e) {
							
						$response = new Response(
								false,
								$e->getMessage(),
								null);
							
					}

				}	else {

					$response = new Response(
							false,
							"Some or all of required parameters are invalid or missing.",
							null);

				}

				echo $response->respond();

				break;
					
			case ProjectDAO::GET:



				require_once 'include/Validator.class.php';

				$pid = $_POST[pid];

				if(isset($pid) && intval($pid) != 0)	{

					try {

						$project = ProjectDAO::getProjectById($pid);
						$project = Project::serialize($project);

						$response = new Response(
								true,
								null,
								$project);

					} catch (Exception $e) {

						$response = new Response(
								false,
								$e->getMessage(),
								null);

					}

				}	else
					$response = new Response(
							false,
							"Project id is invalid or not provided.",
							null);

				echo $response->respond();

				break;
					

				break;
			default:

				echo 'About projects here.';

				break;
					
		}

		break;


	case Entities::Task :

		require_once 'DAL/Task.dao.class.php';
		require_once 'include/Response.class.php';

		$request_type = $_POST[type];

		switch ($request_type){

			case TaskDAO::ADD:
				require_once 'DAL/Task.class.php';

				$name = $_POST[name];
				$description = $_POST[description];
				$owner = $_POST[owner];
				$type = $_POST[ttype];
				$assigned = $_POST[assigned];

				if(isset($name) && isset($description) &&
						isset($owner) && intval($owner) != 0 &&
						isset($type) && intval($type) != 0 &&
						isset($assigned) && intval($assigned) != 0)	{

					try {
							
						$task = new Task(
								null,
								$name,
								$description,
								null,
								null,
								$type,
								false,
								$owner,
								$assigned);

						$status = TaskDAO::addTask($task);
						$response = new Response(
								$status,
								($status)?null:"Can't create task.",
								null);
							
					} catch (Exception $e) {
							
						$response = new Response(
								false,
								$e->getMessage(),
								null);
							
					}

				}	else {

					$response = new Response(
							false,
							"Some or all of required parameters are invalid or missing.",
							null);

				}

				echo $response->respond();

				break;

			case TaskDAO::UPDATE:

				require_once 'DAL/Task.class.php';

				$name = $_POST[name];
				$description = $_POST[description];
				$tid = $_POST[tid];
				$type = $_POST[ttype];
				$assigned = $_POST[assigned];

				if(isset($name) && isset($description) &&
						isset($tid) && intval($tid) != 0 &&
						isset($type) && intval($type) != 0 &&
						isset($assigned) && intval($assigned) != 0)	{

					try {
							
						$task = new Task(
								$tid,
								$name,
								$description,
								null,
								null,
								$type,
								null,
								null,
								$assigned);

						$status = TaskDAO::updateTask($task);
						$response = new Response(
								$status,
								($status)?null:"Can't update task.",
								null);
							
					} catch (Exception $e) {
							
						$response = new Response(
								false,
								$e->getMessage(),
								null);
							
					}

				}	else {

					$response = new Response(
							false,
							"Some or all of required parameters are invalid or missing.",
							null);

				}

				echo $response->respond();

				break;

			case TaskDAO::GET_BY_ID:

				$tid = $_POST[tid];

				if(isset($tid) && intval($tid) != 0)	{

					try {

						$task = TaskDAO::getTaskById($tid);
						$task = Task::serialize($task);

						$response = new Response(
								true,
								null,
								$task);

					} catch (Exception $e) {

						$response = new Response(
								false,
								$e->getMessage(),
								null);

					}

				}	else
					$response = new Response(
							false,
							"task id is invalid or not provided.",
							null);

				echo $response->respond();


				break;

			case TaskDAO::GET_ALL_BY_PROJECT_ID:

				$pid = $_POST[pid];

				if(isset($pid) && intval($pid) != 0)	{

					try {

						$task_list = TaskDAO::getAllTasksByProjectId($pid);

						$response = new Response(
								true,
								null,
								$task_list);
							
					} catch (Exception $e) {
							
						$response = new Response(
								false,
								$e->getMessage(),
								null);
							
					}

				}	else {

					$response = new Response(
							false,
							"Some or all of required parameters are invalid or missing.",
							null);

				}

				echo $response->respond();

				break;

			case TaskDAO::REMOVE:

				$tid = $_POST[tid];

				if(isset($tid) && intval($tid) != 0)	{

					try {

						$status = TaskDAO::removeTask($tid);
						$response = new Response(
								$status,
								($status)?null:"Can't remove task.",
								null);
							
					} catch (Exception $e) {
							
						$response = new Response(
								false,
								$e->getMessage(),
								null);
							
					}

				}	else {

					$response = new Response(
							false,
							"Some or all of required parameters are invalid or missing.",
							null);

				}

				echo $response->respond();
				break;

			case TaskDAO::MARK_COMPLETED:

				$tid = $_POST[tid];
				$completed = $_POST[completed];

				if(isset($tid) && intval($tid) != 0)	{
					try {

						$status = TaskDAO::markCompleted($tid, $completed);
						$response = new Response(
								$status,
								($status)?null:"Can't update task.",
								null);
							
					} catch (Exception $e) {
							
						$response = new Response(
								false,
								$e->getMessage(),
								null);
							
					}

				}	else {

					$response = new Response(
							false,
							"Some or all of required parameters are invalid or missing.",
							null);

				}

				echo $response->respond();


				break;

			default:

				echo 'task info here';

				break;
					
		}

		break;


	case Entities::Comment :

		require_once 'DAL/Comment.dao.class.php';
		require_once 'include/Response.class.php';

		$request_type = $_POST[type];

		switch ($request_type){

			case CommentDAO::ADD:

				require_once 'DAL/Comment.class.php';

				$owner = $_POST[owner];
				$text = $_POST[text];
				$author = $_POST[author];

				if(isset($owner) && intval($owner) !=0 &&
						isset($author) && intval($author) !=0 &&
						isset($text))	{

					try {
							
						$comment = new Comment(
								null, 
								$owner,
								$text, 
								$author, 
								null);

						$status = CommentDAO::addComment($comment);
						$response = new Response(
								$status,
								($status)?null:"Can't add comment.",
								null);
							
					} catch (Exception $e) {
							
						$response = new Response(
								false,
								$e->getMessage(),
								null);
							
					}

				}	else {

					$response = new Response(
							false,
							"Some or all of required parameters are invalid or missing.",
							null);

				}

				echo $response->respond();

				break;


			case CommentDAO::REMOVE:

				$cid = $_POST[cid];

				if(isset($cid) && intval($cid) != 0)	{
					try {

						$status = CommentDAO::removeComment($cid);
						$response = new Response(
								$status,
								($status)?null:"Can't remove comment.",
								null);
							
					} catch (Exception $e) {
							
						$response = new Response(
								false,
								$e->getMessage(),
								null);
							
					}

				}	else {

					$response = new Response(
							false,
							"Some or all of required parameters are invalid or missing.",
							null);

				}

				echo $response->respond();

				break;


			case CommentDAO::LIST_BY_TASK_ID:

				$tid = $_POST[tid];

				if(isset($tid) && intval($tid) != 0)	{

					try {

						$comment_list = CommentDAO::listCommentsByTaskId($tid);

						$response = new Response(
								true,
								null,
								$comment_list);
							
					} catch (Exception $e) {
							
						$response = new Response(
								false,
								$e->getMessage(),
								null);
							
					}

				}	else {

					$response = new Response(
							false,
							"Some or all of required parameters are invalid or missing.",
							null);

				}

				echo $response->respond();

				break;


			default:

				echo 'About comments here';

				break;


					
		}


		break;


	default :

		require_once 'index.html';

		break;

}

