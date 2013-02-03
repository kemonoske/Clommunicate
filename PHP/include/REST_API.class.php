<?php

/**
 * This class parses URL and choses action based
 * on REST standart
 * @author Akira
 *
 */
class REST_API {

	private $url;
	private $request_type;
	private $params;

	function __construct($url,  $request_type, $params){

		$this->url = $url;
		$this->request_type = $request_type;
		$this->params = $params;

	}

	function __destruct(){

		unset($this->url);
		unset($this->request_type);
		unset($this->params);

	}

	function perform($owner_entity = null, $owner_id = null)	{

		$current_entity = strtoupper($this->url[0]);

		/*
		 * Entity select
		*/
		if(is_null($owner_entity))	{
			switch ($current_entity){

				case 'USERS':

					$this->parseUsers();

					break;

				case 'PROJECTS':

					$this->parseProjects();

					break;

				case 'TASKS':

					$this->parseTasks();

					break;

				case 'COMMENTS':

					$this->parseComments();

					break;


				default:

					$response = new Response(
					false,
					"Resource not found.",
					null);

					echo $response->respond();

					break;
			}

		}	else if (!strcasecmp($owner_entity, 'USERS'))	{

			switch ($current_entity){

				case 'PROJECTS':

					$this->parseProjects($owner_id);

					break;

				case 'PART_IN_PROJECTS':

					$this->parseProjects(null, $owner_id);

					break;

					/*case 'TASKS':
					 	
					//Tasks created by user N/A

					break;*/

					/*case 'TASKS_ASSIGNED':
					 	
					//Tasks Assigned to User N/A
					$this->parseTasks(null, $owner_id);

					break;*/

					/*case 'COMMENTS':

					//Comments by User N/A
					$this->parseComments($owner_id);

					break;*/

				default:

					$response = new Response(
					false,
					"Resource not found.",
					null);

					echo $response->respond();

					break;
			}


		}	else if (!strcasecmp($owner_entity, 'PROJECTS'))	{

			switch ($current_entity){

				case 'TASKS':

					$this->parseTasks($owner_id);

					break;

				case 'MEMBERS':

					$this->parseMembers($owner_id);

					break;
					/*case 'COMMENTS':

					//Comments in a project N/A
					$this->parseComments();

					break;*/

				default:

					$response = new Response(
					false,
					"Resource not found.",
					null);

					echo $response->respond();

					break;
			}


		}	else if (!strcasecmp($owner_entity, 'TASKS'))	{

			switch ($current_entity){

				case 'COMMENTS':

					$this->parseComments($owner_id);

					break;


				default:

					$response = new Response(
					false,
					"Resource not found.",
					null);

					echo $response->respond();

					break;
			}


		}	else if (!strcasecmp($owner_entity, 'COMMENTS'))	{

			switch ($current_entity){

				/*case 'AUTHOR':

				//N/A

				break;*/

				default:

					$response = new Response(
					false,
					"Resource not found.",
					null);

					echo $response->respond();

					break;
			}


		}	else	{
			$response = new Response(
					false,
					"Resource not found.",
					null);

			echo $response->respond();


		}

	}


	function parseUsers(){

		switch(count($this->url)){

			/*
			 * Only entity received:
			* http://example.com/api/entity
			*/
			case 1:

				require_once 'include/Validator.class.php';
				require_once 'DAL/User.class.php';
				require_once 'DAL/User.dao.class.php';

				//Action is performed directly on collection
				//GET: list of all users N/A
				//PUT: replace list of user N/A
				//POST: add new user
				//DELETE: deletes all users N/A

				switch($this->request_type){

					/*case 'GET':

					break;*/

					/*case 'PUT':

					break;*/

					/*
					 * Add new user
					*/
					case 'POST':

						$email = $this->params[email];
						$name = $this->params[name];
						$locale = $this->params[locale];
						$photo = $this->params[photo];
						$gender = $this->params[gender];

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

						/*case 'DELETE':

						break;*/

					default:

						$response = new Response(
						false,
						"Operation not supported.",
						null);

						echo $response->respond();

						break;
							
				}


				break;


				/*
				 * Only entity and id of an item received:
				* http://example.com/api/entity/id
				*/
			case 2:

				//Action is performed on an object

				require_once 'DAL/User.class.php';
				require_once 'DAL/User.dao.class.php';
				require_once 'include/Validator.class.php';

				$id = $this->url[1];
				//GET: return user details
				//PUT: update user details
				//POST: N/A maybe create new project but its bullshit
				//DELETE: delete user probably N/A

				switch($this->request_type){

					case 'GET':

						$ident = $id;

						if(isset($ident) && (Validator::checkEmail($ident) || intval($ident) != 0))	{

							try {

								$user = (intval($ident) != 0)?UserDAO::getUserById($ident):UserDAO::getUser($ident);
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

					case 'PUT':

						$email_found = UserDAO::exists($id);
						$id_found = UserDAO::existsById($id);
						/*if( $email_found || $id_found){

						//Update N/A
							
						}	else	{*/
							
						//Add
						$email = $id;
						$name = $this->params[name];
						$locale = $this->params[locale];
						$photo = $this->params[photo];
						$gender = $this->params[gender];
						echo  $this->params[name];
							
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
							
						//}

						break;
							
						/*case 'POST':

						//Add project will be here
							
						break;*/

						/*case 'DELETE':

						//N/A

						break;*/

					default:

						$response = new Response(
						false,
						"Operation not supported.",
						null);

						echo $response->respond();

						break;

							
				}

				break;

				/*
				 * Entity and posible sub entities:
				* http://example.com/api/entity/id/entity2/id/entity3...
				*/
			default:
				/*
				 * Considering the specific of non hierachical database
				 * we remove first 2 elements and parse further
				 * optionaly manual child <-> parent relation checks can be performed here
				 */

				require_once 'DAL/User.dao.class.php';

				$id = $this->url[1];

				if(isset($id) && intval($id) != 0 && UserDAO::existsById($id)){

					$this->url = array_slice($this->url, 2);
					$this->perform('USERS', $id);

				}	else {

					$response = new Response(
							false,
							"Invalid user.",
							null);

					echo $response->respond();

				}

				break;


		}

	}

	function parseProjects($owner_id = null, $member_id = null){

		switch(count($this->url)){

			/*
			 * Only entity received:
			* http://example.com/api/entity
			*/
			case 1:

				require_once 'include/Validator.class.php';
				require_once 'DAL/Project.class.php';
				require_once 'DAL/Project.dao.class.php';
				//Action is performed directly on collection
				//GET: list of all projects
				//PUT: replace list of projects N/A
				//POST: add new project
				//DELETE: deletes all projects N/A


				switch($this->request_type){

					case 'GET':

						if(is_null($owner_id) && is_null($member_id))	{

							//List All N/A

						}	else if (isset($owner_id)){

							//List by owner

							$uid = $owner_id;

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

							echo $response->respond();

						}	else	{

							//List by member
							$uid = $member_id;

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

							echo $response->respond();

						}

						break;

						/*case 'PUT':

						Replace projects N/A

						break;*/

					case 'POST':

						//Add project

						$name = $this->params[name];
						$description = $this->params[description];
						$owner = (isset($owner_id)?$owner_id: $this->params[owner]);;
						$deadline = $this->params[deadline];
						$members = $this->params[members];
							
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
									
								$status = ProjectDAO::addProject($project,$members);
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

						/*case 'DELETE':

						N/A
							
						break;*/

					default:

						$response = new Response(
						false,
						"Operation not supported.",
						null);

						echo $response->respond();

						break;

				}

				break;


				/*
				 * Only entity and id of an item received:
				* http://example.com/api/entity/id
				*/
			case 2:

				require_once 'include/Validator.class.php';
				require_once 'DAL/Project.class.php';
				require_once 'DAL/Project.dao.class.php';

				//Action is performed on an object

				$id = $this->url[1];
				//GET: return project details
				//PUT: update project details
				//POST: N/A maybe create new task but its bullshit
				//DELETE: delete project


				switch($this->request_type){

					case 'GET':

						$pid = $id;

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

					case 'PUT':

						$pid = $id;
						$name = $this->params[name];
						$description = $this->params[description];
						$deadline = $this->params[deadline];
						$complete = $this->params[complete];

						if(!ProjectDAO::exists($pid))	{

								$response = new Response(
										false,
										"No projects with such id.",
										null);
							
						} else if(isset($complete) && isset($pid) && intval($pid) != 0)	{

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


						}	else if(isset($name) && isset($description) && isset($deadline) &&
								isset($pid) && intval($pid) != 0  && ProjectDAO::exists($pid))	{

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

						/*case 'POST':

						N/A

						break;*/

					case 'DELETE':
							
						$pid = $id;
							
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

					default:

						$response = new Response(
						false,
						"Operation not supported.",
						null);

						echo $response->respond();

						break;


				}

				break;

				/*
				 * Entity and posible sub entities:
				* http://example.com/api/entity/id/entity2/id/entity3...
				*/
			default:
				/*
				 * Considering the specific of non hierachical database
				 * we remove first 2 elements and parse further
				 * optionaly manual child <-> parent relation checks can be performed here
				 */

				require_once 'DAL/Project.dao.class.php';

				$id = $this->url[1];

				if(ProjectDAO::exists($id))	{

					$this->url = array_slice($this->url, 2);
					$this->perform('PROJECTS', $id);

				} else	{

					$response = new Response(
							false,
							"No project with such id.",
							null);

					echo $response->respond();

				}

				break;


		}

	}

	function parseTasks($owner_id = null, $assigned_id = null){

		switch(count($this->url)){

			/*
			 * Only entity received:
			* http://example.com/api/entity
			*/
			case 1:

				require_once 'DAL/Task.class.php';
				require_once 'DAL/Task.dao.class.php';

				//Action is performed directly on collection
				//GET: list of all tasks
				//PUT: replace list of tasks N/A
				//POST: add new task
				//DELETE: deletes all tasks N/A


				switch($this->request_type){

					case 'GET':

						if(isset($owner_id))	{

							$pid = $owner_id;

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
									"Operation not supported.",
									null);

						}

						echo $response->respond();

						break;

						/*case 'PUT':

						N/A

						break;*/

					case 'POST':

						$name = $this->params[name];
						$description = $this->params[description];
						$owner = (isset($owner_id)?$owner_id: $this->params[owner]);;
						$type = $this->params[type];
						$assigned = $this->params[assigned];

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

						/*case 'DELETE':

						N/A

						break;*/

					default:

						$response = new Response(
						false,
						"Operation not supported.",
						null);

						echo $response->respond();

						break;


				}
				break;


				/*
				 * Only entity and id of an item received:
				* http://example.com/api/entity/id
				*/
			case 2:
				//Action is performed on an object

				require_once 'DAL/Task.class.php';
				require_once 'DAL/Task.dao.class.php';

				$id = $this->url[1];

				//TODO: Check if task exists

				//GET: return task details
				//PUT: update task details
				//POST: N/A maybe create new comment but its bullshit
				//DELETE: delete task

				switch($this->request_type){

					case 'GET':

						$tid = $id;

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


						echo $response->respond();


						break;

					case 'PUT':

						$tid = $id;
						$name = $this->params[name];
						$description = $this->params[description];
						$type = $this->params[type];
						$assigned = $this->params[assigned];
						$complete = $this->params[complete];

						if(!TaskDAO::exists($tid))	{

								$response = new Response(
										false,
										"No task with such id.",
										null);
							
						} else if(isset($complete) && isset($tid) && intval($tid) != 0)	{
							
							try {

								$status = TaskDAO::markCompleted($tid, $complete);
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
								
						} else if(isset($name) && isset($description) &&
								isset($tid) && intval($tid) != 0 &&
								isset($type) && intval($type) != 0 &&
								isset($assigned) && intval($assigned) != 0 && TaskDAO::exists($tid))	{

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

						/*case 'POST':

						break;*/

					case 'DELETE':


						$tid = $id;

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

					default:

						$response = new Response(
						false,
						"Operation not supported.",
						null);

						echo $response->respond();

						break;


				}
				break;

				/*
				 * Entity and posible sub entities:
				* http://example.com/api/entity/id/entity2/id/entity3...
				*/
			default:
				/*
				 * Considering the specific of non hierachical database
				 * we remove first 2 elements and parse further
				 * optionaly manual child <-> parent relation checks can be performed here
				 */

				require_once 'DAL/Task.dao.class.php';

				$id = $this->url[1];

				if(TaskDAO::exists($id))	{

					$this->url = array_slice($this->url, 2);
					$this->perform('TASKS', $id);

				} else	{

					$response = new Response(
							false,
							"No task with such id.",
							null);

					echo $response->respond();


				}

				break;


		}

	}

	function parseComments($owner_id = null){

		switch(count($this->url)){

			/*
			 * Only entity received:
			* http://example.com/api/entity
			*/
			case 1:

				require_once 'DAL/Comment.class.php';
				require_once 'DAL/Comment.dao.class.php';

				//Action is performed directly on collection
				//GET: list of all comments
				//PUT: replace list of comments
				//POST: add new comment
				//DELETE: deletes all comments N/A

				switch($this->request_type){

					case 'GET':

						if(isset($owner_id))	{

							$tid = $owner_id;

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
									"Resource not found.",
									null);


						}

						echo $response->respond();

						break;

						/*case 'PUT':

						N/A

						break;*/

					case 'POST':

						$owner = (isset($owner_id)?$owner_id: $this->params[owner]);
						$text = $this->params[text];
						$author = $this->params[author];

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

						/*case 'DELETE':

						$params = $_DELETE;
						break;*/

					default:

						$response = new Response(
						false,
						"Operation not supported.",
						null);

						echo $response->respond();

						break;


				}
				break;


				/*
				 * Only entity and id of an item received:
				* http://example.com/api/entity/id
				*/
			case 2:

				//Action is performed on an object

				require_once 'DAL/Comment.class.php';
				require_once 'DAL/Comment.dao.class.php';

				$id = $this->url[1];

				//GET: return comment details
				//PUT: update comment details
				//POST: N/A never ever ever
				//DELETE: delete comment


				switch($this->request_type){

					/*case 'GET':

					N/A

					break;*/

					/*case 'PUT':

					N/A

					break;*/

					/*case 'POST':

					N/A

					break;*/

					case 'DELETE':

						$cid = $id;

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
									"No comment with such id.",
									null);

						}

						echo $response->respond();

						break;

					default:

						$response = new Response(
						false,
						"Operation not supported.",
						null);

						echo $response->respond();

						break;


				}

				break;

				/*
				 * Entity and posible sub entities:
				* http://example.com/api/entity/id/entity2/id/entity3...
				*/
			default:
				/*
				 * Considering the specific of non hierachical database
				 * we remove first 2 elements and parse further
				 * optionaly manual child <-> parent relation checks can be performed here
				 */

				require_once 'DAL/Comment.dao.class.php';

				$id = $this->url[1];

				if(CommentDAO::exists($id))	{

					$this->url = array_slice($this->url, 2);
					$this->perform('COMMENTS', $id);

				} else	{

					$response = new Response(
							false,
							"No comment with such id.",
							null);

					echo $response->respond();


				}

				break;


		}

	}

	function parseMembers($owner_id = null){

		switch(count($this->url)){

			/*
			 * Only entity received:
			* http://example.com/api/entity
			*/
			case 1:

				require_once 'DAL/User.dao.class.php';
				require_once 'DAL/Project.dao.class.php';
				//Action is performed directly on collection
				//GET: list of all comments
				//PUT: replace list of comments
				//POST: add new comment
				//DELETE: deletes all comments N/A

				switch($this->request_type){

					case 'GET':

						$pid = $owner_id;

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

						echo $response->respond();

						break;

						/*case 'PUT':

						$params = $_PUT;

						break;*/

					case 'POST':


						$pid = $owner_id;
						$uid = $this->params[uid];

						if(isset($uid) && intval($uid) != 0 && UserDAO::existsById($uid))	{

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
									"No such user in the system.",
									null);

						}

						echo $response->respond();

						break;

						/*case 'DELETE':

						$params = $_DELETE;
						break;*/

					default:

						$response = new Response(
						false,
						"Operation not supported.",
						null);

						echo $response->respond();

						break;


				}
				break;


				/*
				 * Only entity and id of an item received:
				* http://example.com/api/entity/id
				*/
			case 2:

				//Action is performed on an object

				require_once 'DAL/Project.dao.class.php';
				require_once 'DAL/User.dao.class.php';

				$id = $this->url[1];

				//GET: return user details
				//PUT: update user details
				//POST: N/A maybe create new project but its bullshit
				//DELETE: delete user probably N/A


				switch($this->request_type){

					/*case 'GET':

					N/A

					break;*/

					/*case 'PUT':

					N/A

					break;*/

					/*case 'POST':



					break;*/

					case 'DELETE':

						$uid = $id;
						$pid = $owner_id;

						if(isset($uid) && intval($uid) != 0 && UserDAO::existsById($uid))	{
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

					default:

						$response = new Response(
						false,
						"Operation not supported.",
						null);

						echo $response->respond();

						break;


				}

				break;

				/*
				 * Entity and posible sub entities:
				* http://example.com/api/entity/id/entity2/id/entity3...
				*/
			default:
				/*
				 * Considering the specific of non hierachical database
				 * we remove first 2 elements and parse further
				 * optionaly manual child <-> parent relation checks can be performed here
				 */

				$this->url = array_slice($this->url, 2);
				$this->perform('COMMENTS', $id);


				require_once 'DAL/User.dao.class.php';

				$id = $this->url[1];

				if(!UserDAO::existsById($id))	{

					$response = new Response(
							false,
							"No member with such id.",
							null);

					echo $response->respond();


				}

				break;


		}

	}


}