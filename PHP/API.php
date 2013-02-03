<?php

require_once 'include/REST_API.class.php';
require_once 'include/Response.class.php';

$url = $_SERVER['REQUEST_URI'];
$url = explode('/', $url);


/*
 * Remove /api from url
 */
$url = array_slice($url, 2);

$request_type = $_SERVER['REQUEST_METHOD'];

switch ($request_type)	{
	
	case 'GET':
		
		$params = $_GET;
		
		break;
		
	case 'PUT':


		parse_str(file_get_contents('php://input'),$params);
		
		break;
		
	case 'POST':
		
		$params = $_POST;
		
		break;
		
	case 'DELETE':

		parse_str(file_get_contents('php://input'),$params);
		
		break;
	
}

$api = new REST_API($url, $request_type, $params);

$api->perform();