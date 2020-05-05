<!--Hvalna funkcia odhlasenie uzivaela-->
<div class='clanok'>
<?php
	if (isset($_SESSION['username'])){
		//vytlaci zakladne udaje o uzivatelovi
		echo 'Meno a priezvisko: '.htmlspecialchars($_SESSION['meno']);
		echo "<br><br>";
		echo 'Požívateľské meno: '.htmlspecialchars($_SESSION['username']);
		echo "<br><br>";
		echo 'Email: '.htmlspecialchars($_SESSION['email']);
		//nastavenie designu, ktory je platny iba pre prihlaseneho. Po odhlaseni sa cookie znici
		include ('design.php');
		//odhlasovanie, zavola funkciu v index.php
		echo "<br>
		<form method='post'>
		<input type='submit' class='registration' name='logout' value='Odhlásenie'>
		</form>";
	}
	//ak sa uzivatel, snazi po odhlaseni vratit naspat na stranku s udajmi o ucte, vypise
	else {
		echo 'Máš blbý SMER. <-';
	}

?>


</div>