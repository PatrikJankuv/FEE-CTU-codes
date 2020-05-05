<?php
	//pomocou skladania sranok ja index vzdy nacitany
	//zacatie session
	session_start();
	
	//sluzi pri presmerovani pri komentovaniu
	$actual_link = "https://$_SERVER[HTTP_HOST]$_SERVER[REQUEST_URI]";
	//ziska cislo clanku, vyuzije na zapis do suboru
	$page = substr($actual_link, -3);
	
	//premenne pre prihlasenie
    $username = '';
	$heslo = '';
	$vypis = '';
	
	
	//na prihlasenie
	if (isset($_POST['username'])){
		
		//$_SESSION['username'] = '';
		$vypis = "<strong>Neplatne meno alebo heslo</strong>";
		//nacitanie do premennych z post
		$username = $_POST['username'];
		$heslo = $_POST['pass'];
		
		//overovanie uzivatela
		//otvorenie suuboru z uzivatelskymi uctami a zistenie velkosti
		$database = file ('databases/accounts.txt');
		$sizeDt = sizeOf($database);
		
		//prechadzanie "databazi"
		for ($i = 1; $i <= $sizeDt; $i++){
			$account = $database[$i-1];
			$accountn = explode (';☺♀♥☼♦', $account);
			
			//ak sa username zhoduje, zacne overovat heslo
			if ($username === $accountn[1]){
				//overenie hesla pomocou, password_verify()
				if (password_verify($heslo, $accountn[3])) {
					//ak overilo heslo, nastavenie session
					$_SESSION['meno']=$accountn[0];
					$_SESSION['username'] = $username;
					$_SESSION['email']=$accountn[2];
					//presmerovanie na hlavnu stranku
					header('Location: https://wa.toad.cz/~jankupat/GoHawks/index.php?stranka=home');
					
				}
				
			}
		}
	}
	
	//nastavenie cookie pre skalovatelnost	
	if (isset($_POST["design"])) {
			//nastavenie cookie cookie zo zivotnostou na 1 hodinu
           setcookie('design', $_POST["design"], time()+60*60);
		   //eload stranky, aby bola vidie5 zmena
		   header("Refresh:0");
       }
	
	//odhlasenie, vymaze nastavenie vzhadu, a znici session s udajmi pouzivatela
	if (isset($_POST["logout"])) {
		//znicenie cookie pomocou nastavenie casu na minulost
		setcookie('design', 'aha', time()-1);
		//unset($_COOKIE['design']);
		session_destroy();
		header('Location: https://wa.toad.cz/~jankupat/GoHawks/index.php?stranka=home');
	}
	
	//zapisovanie komentara do suboru
	//kontrola : ak je nastavene a nie je prazdne
	if (isset($_POST['commentar']) and ($_POST['commentar'] != '')){
		//pole, z ktoreho sa bude zapisovat
		$comment = array();
		$comment[0] = $page;
		$comment[1] = $_SESSION['username'];
		//orezanie nevidetnich znakov na zaciatku a konci
		$comment[2] = Trim($_POST['commentar']);
		//znovu nastavenie 
		$_POST['commentar'] = null;
				
		//zapis do suboru		
		$commentdb = fopen ('databases/comments.txt', 'a');
		fwrite ($commentdb, "$comment[0];$comment[1];$comment[2];");
		fwrite ($commentdb, "\n");
		fclose ($commentdb);
		
		
		$comment[0] = '';
		$comment[1] = '';
		$comment[2] = '';
		//ochrana pred reload
		header('Location: '.$actual_link);
		
		}
?>
<!DOCTYPE html>
<html lang="sk">
    <head>
        <title>GoHawks</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="css/style.css" type="text/css"/>
        <link rel="shortcut icon" href="pics/icon.ico" />  <!--icona hlavičky -->
        <link rel="stylesheet" href="css/navstyle.css" type="text/css"/>
        <link rel="stylesheet" href="css/schedule.css" type="text/css"/>
		<link rel="stylesheet" href="css/teamstyle.css" type="text/css"/>
		<link rel="stylesheet" href="css/home.css" type="text/css"/>
		<link rel="stylesheet" href="css/formstyle.css" type="text/css"/>
		<meta name="viewport" content="width=device-width, initial-scale=1">		
		<script src="js/form.js"></script> 
		<script src="ajax.js"></script> 
        <script src="js/validation.js"></script>
    </head>
	<?php
		//skalovatelnost pomocou tried v CSS na body
		//ak je nastavene cookie vytlaci triedu s nazvom triedy 
		echo "<body ";
        if (isset($_COOKIE['design'])) {
            echo "class='".htmlspecialchars($_COOKIE['design'])."'";}
        else
			//ak nie je nastavene vypise triedu day
			echo "class='day'";
		echo ">";
        
    ?>
    
        <div class="header">
        <img src="pics/logo.png" alt="logo" class="logo">
		<!--Navigacne menu-->
            <div class="header-right">
                <a href="index.php?stranka=home&strana=1">Domov</a>
                <a href="index.php?stranka=team">Tím</a>
                <a href="index.php?stranka=schedule">Rozpis zápasov</a>
                
				<?php
				//ak je prihlaseny uzivatel vypise meno uzivatela, ktore je odkaz na odhlasenie
				if (isset($_SESSION['username']) and ($_SESSION['username'] != '')){
					echo "<a href='index.php?stranka=ucet'>";
					echo htmlspecialchars($_SESSION['meno']); 
					echo "</a>";}
				else{
					//neprihlaseny vidi odkaz na prihlasenie
					echo "<a href='index.php?stranka=login'> Prihlásenie</a>";
				}
				?>
				
				
            </div>
			
        </div>        <br>
		<div class="clearfix content">
		<div class="wall">
		
				<?php	
						
						//skladanie stranky
						//ziskanie stranky z stranka , ktora je v odkaze
						if (isset($_GET['stranka']))
							$stranka = $_GET['stranka'];
						//ak nie je nastavene get alebo je otvereny priamo index.php, nastavenie get['stranka'] na home
						else{
							$stranka = 'home';
							$_GET['stranka'] = 1;
						}
						
						//skladanie stranky podla get['stranka']
						if ($stranka == 'home'){
							//pokiaľ uzivatel zada zlu podstranku do pola v prehliadaci, zabrani aby videl chybove hlasenie generovane php
							error_reporting(0);
							include('pages/' . $stranka. '.php');
						}
						else if (preg_match('/^[a-z0-9]+$/', $stranka))
						{
							$vlozeno = include('pages/' . $stranka. '.php');
							if (!$vlozeno)
								
								echo('Podstránka nenájdená');
						}
						else
							echo('Neplatný parametr.');
					?>
					
         </div>
		 <!--bocna strana z obrazkom, twitterom seahawks, rozpisom zapasov -->
		 <div class="side">
		 <img src="pics/hawk.jpg" alt="hawk">
		 <a class="twitter-timeline" data-width="300" data-height="500" href="https://twitter.com/Seahawks?ref_src=twsrc%5Etfw">Tweets by Seahawks</a> <script async src="https://platform.twitter.com/widgets.js"></script>
         <a href="index.php?stranka=schedule"><img src="pics/schedule.jpg" alt="hawk"></a>
		 
		 </div>
		 </div>
		        
        <footer>
        <p>&copy; 2018 Semestrálna práca na predmet Základy webobých aplikácii, Patrik Jankuv, České vysoké učení technické v Praze</p>
        </footer>
    </body>
</html>
