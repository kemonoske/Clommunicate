<?php
/**
 * All responses made by WebAPI will match this format
 * @author Akira
 *
 */
class Response {
	
	private $status;
	private $message;
	private $data;
	
	function __construct($status, $message, $data) {
		
		$this->setStatus($status);
		$this->setMessage($message);
		$this->setData($data);
		
	}
	
	function __destruct(){
		
		unset($this->status);
		unset($this->message);
		unset($this->data);
		
	}
	
	function getStatus(){
		
		return $this->status;
		
	}
	
	function getMessage(){
		
		return $this->message;
		
	}
	
	function getData(){
		
		return $this->data;
		
	}
	
	function setStatus($status){
		
		$this->status = $status;
		
	}
	
	function setMessage($message){
		
		$this->message = $message;
		
	}
	
	function setData($data){
		
		$this->data = $data;
		
	}
	
	function respond(){

		$response[status] = $this->getStatus();
		$response[message] = $this->getMessage();
		$response[data] = $this->getData();
		
		return json_encode($response);
		
	}
	
}

