<?php
// get the q parameter from URL
$q = $_REQUEST["q"];

$hint = "";

// lookup all hints from array if $q is different from "" 
if ($q !== "") {
	
    $len=strlen($q);
	$database = file ('databases/accounts.txt');
		$sizeDt = sizeOf($database);
		
		for ($i = 1; $i <= $sizeDt; $i++){
			$account = $database[$i-1];
			$accountn = explode (';☺♀♥☼♦', $account);
			
			
			if ($q == $accountn[1]){
					$hint = "Meno obsadené, zvoľ prosím iné používateľské<br>";
					break;
			}
			else{
				$hint = "Meno je voľné<br>";
			}
		}
}
?>