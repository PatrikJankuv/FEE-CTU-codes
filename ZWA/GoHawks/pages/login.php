<?php
	//podmienka zabrani, aby sa prihlaseny uzivatel znovu prihlasi cez prihlasovaci formular
	if (isset($_SESSION['username'])){
		include('pages/ucet.php');
	}
	//pokial nie je podmienka smlnena (neprihlaseny uzivatel), tak vypise formular pre prihlasovanie
	else{
		echo "<h1>Prihlásenie</h1>
                <form name='login' method='post'>
                    <label for='username'>Používateľské meno:<br>
                    <input  id='username' class='policko' type='text' name='username'  placeholder='username' pattern='.{3,}' required onkeyup='isUsername(document.login.username)'></label><span id='usernamewarning'></span><br>
                    <label for='pass'>Heslo:<br>
                    <input id='pass' class='policko' type='password' name='pass' onkeyup='isFill(document.login.pass)' placeholder='heslo' pattern='.{3,24}' required></label><span id='fillwarning'></span><br><br>
                    <input type='submit' class='login' value='Prihlásenie' onClick='loginValidation()'>
                </form>"; 

	echo $vypis;
         
    echo "<p>Ste tu prvý krát? Tu sa môžete registrovať:</p>";
           
	echo "<a href='index.php?stranka=registration' class='registration'>Registrácia</a>";
	}
	
  ?>                   
