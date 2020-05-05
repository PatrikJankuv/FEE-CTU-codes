<?php
//overenie  unikatnosti pouzivatelskeho mena
function unic($nick){
	//nacitanie "databasy"
		$database = file ('databases/accounts.txt');
		$sizeDt = sizeOf($database);
		
		//"prejde databasu"
		for ($i = 1; $i <= $sizeDt; $i++){
			$account = $database[$i-1];
			$accountn = explode (';☺♀♥☼♦', $account);
		
			//ak sa zhoduje vrati false
			if ($nick === $accountn[1]){
					return false;
			}
		}
		//ked prejde celu db bez false vrati true
		return true;
		}
		
	//pole na zapis do "databazy"
	$account = array();
	$account[0] = '';
	$account[1] = '';
	$account[2] = '';
	//ostatne premenne
	$password = '';
	$repassword = '';
	$vypis = "";
	$checked = "";
	
	//zacne registrovat, ak je nastavene meno	
	if (isset($_POST['meno'])){
		//nastavenie  hodnot z formulara, ktore sa budu zapisovat do pola, ostatne iba ako premenne
		$account[0] = trim($_POST['meno']);
		$account[1] = trim($_POST['nickname']);
		$account[2] = trim($_POST['text1']);
		$password = $_POST['psw'];
		$repassword = $_POST['pswcheck'];
		$checked = 'checked';
		
		//zapis do "databazy"
		//ak je uzivatelske meno jedinecne,
		if (unic($account[1])){
			if(strpos($account[0], ' ') == false){
				$password = '';
				$repassword = '';
				$vypis = 'Meno a priezvisko obsahuje medzeru';
			}
			//kontrola platnosti pouzivatelskeho mena
			elseif(strpos($account[1], ' ')){
				$password = '';
				$repassword = '';
				$vypis = 'Používateľské meno nesmie obsahoť medzeru';
			}
			//kontrola platnosi emailu
			elseif(strpos($account[2], '@') == false){
				$password = '';
				$repassword = '';
				$vypis = 'Email musí obsahovať @';
		} 
			//ak sa heslo a kontrolne heslo nezhoduje, vynuluje hesla a vrati na opravu
			elseif ($password != $repassword){
				$password = '';
				$repassword = '';
				$vypis = 'Hesla sa nezhodujú';
				
			}
			//ak sa hesla zhoduju, zacne zapisovat
			else{
					//zahashovanie hesla a ulozenie do pola na zapis
					$account[3] = password_hash($password, PASSWORD_DEFAULT);
							
					//praca so suborom
					//otvorenie "databazy" z uzivatelskymi uctami na poslednom mieste
					$database = fopen ('databases/accounts.txt', 'a');
					//zapis do suboru
					fwrite ($database, "$account[0];☺♀♥☼♦$account[1];☺♀♥☼♦$account[2];☺♀♥☼♦$account[3];☺♀♥☼♦");
					fwrite ($database, "\n");
					fclose ($database);
					
					//nastavenie premennej, ktora sa vypise na obrazovke
					$vypis ="<strong>Registrácia prebehla úspešne</strong>";
					
					//vynulovanie pola na zapis a ostatnych premennych
					$account[0] = '';
					$account[1] = '';
					$account[2] = '';
					$password = '';
					$repassword = '';
					$checked = '';
				}
			
			}
		}
		
				
	
	
?>
	<!--Registracny formular -->
    <h1>Registrácia</h1>
		<form name="form1" action="#" onload='document.form1.meno.focus()' method="POST"> 
		<fieldset class="field">
			<legend> Údaje o použivateľovi </legend>				
				<label for="name">*Meno a priezvisko:<br>
				<input id="name" type="text" name="meno" class="policko" placeholder="Jmeno a príjmení" onkeyup="validateName(document.form1.meno)" pattern="(?=.*[a-z])(?=.*[A-Z]).{3,32}" title="Minimálna dĺžka 3 znaky" value ="<?php echo htmlspecialchars($account[0]); ?>" required></label><span id="namewarning"></span><br>
				<label for="mail">*Email<br>
				<input id="mail" type='text' name='text1' class="policko" placeholder="vzor@domena.com" onkeyup="validateEmail(document.form1.text1)" value="<?php echo htmlspecialchars($account[2]); ?>" required/></label><span id="mailwarning"></span><br>
		</fieldset>
		<fieldset>
			<legend> Prihlasovacie údaje </legend>
				<label for="username">*Používateľské meno:<br>
				<!--ajax overenie uzivatelskeho mena--><input id="username" type="text" name="nickname" class="policko" onkeyup="showHint(this.value);isUsername(document.form1.nickname)" placeholder="min 4 znaky" pattern=".{4,32}" title="Musí mať minimálne 4 znaky" value = "<?php echo htmlspecialchars($account[1]); ?>" required></label><span id="usernamewarning"></span><br>
				<span id="txtHint"></span><!--Vypis pre ajax-->
				<label for="pass">*Heslo:<br>
				<input id="pass" type="password" name="psw" class="policko" placeholder="min 8 znakov" onkeyup="validatePass(document.form1.psw)"  pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,24}" title="Heslo musí obsahovať minimálne jeden veľký znak, jeded malý znak a jedno číslo. Dĺžka hesla je minimálne 6 znakov a maximálne 24 znakov." required><span id="passwarning"></span></label><br>
				<label for="repass">*Zopakuj heslo:<br>
				<input id="repass" type="password" name="pswcheck"  placeholder="min 8 znakov" class="policko"  onkeyup="isSame(document.form1.psw, document.form1.pswcheck)" pattern=".{8,24}" required><span id="repasswarning"></span></label><br>
				<br>
				
		</fieldset>
        <label for="terms"><input id="terms" type="checkbox" name="terms" <?php echo ($checked); ?> required> *Súhlasím s </label><a target="_blank" class="condition" href="docs/podmienky.pdf"><u>podmienkami</u></a><span id="termswarning"></span><br><br>
         <?php
		 //vypis o stave registracie
			if ($vypis != ""){
				echo $vypis;
				echo "<br>";
			}
		 ?>
		 * Povinné údaje<br>
		<label for="regist"><input id="regist" type="submit" name="submit" value="Registrovať" onClick="registrationValidation()" class="registration" /></label><!--Po odoslani submit zavola js overenie-->
        <label>&nbsp;</label>
          
        </form>
		