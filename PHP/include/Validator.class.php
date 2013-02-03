<?php


class Validator {
	
	static function checkEmail( $email ){
		return filter_var( $email, FILTER_VALIDATE_EMAIL );
	}
	
}