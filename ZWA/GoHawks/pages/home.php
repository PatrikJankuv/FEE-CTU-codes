<?php
//vypisanie strankovania s cislami podstranok
function strankovanie($pocetclankov, $clankynastrane, $pocetstran){
		echo "<br>";
		//podmienka na spravny vypis strankovania, aby neostavala prazdna strana 
		if (($pocetclankov % $clankynastrane) != 0){
			$pocetstran + 1;
			for ($i = 1; $i <= $pocetstran+1; $i++){
			echo "<a class='strana' href='index.php?stranka=home&strana=".$i."'>".$i."</a>";
			}
		}
		else{
			for ($i = 1; $i < $pocetstran+1; $i++){
			echo "<a class='strana' href='index.php?stranka=home&strana=".$i."'>".$i."</a>";}
		}
	}

//ak nie je nastavene $GET['strana'], tak nastavi na 1, kvoli indexu.php
	if (!isset($_GET['strana'])){
	$_GET['strana'] = 1;
	}	

//zobrazenie velkeho clanku na  prvej strane
if ($_GET['strana'] == 1){
	echo "<h1>Slovenskočeská fanpage Seattlu Seahawks</h1>

<a href='https://en.wikipedia.org/wiki/CenturyLink_Field' target='_blank'> 
        <div class='mainarticle'>
            <img src='pics/centurylink.jpg' alt='century link'>
            <h2>Century Link Field domov Seattlu Seahawks</h2>
			<p><strong>Zaujímavosti</strong> | Wikipédia | 1.12.2018</p>
            <h3>Tento štadion patrí k najhlučnejším v celej NFL. Prečítajte si o ňom článok na anglickej wikipédii.</h3>
        </div>
        </a>";
}

	//otveenie suboru zo zoznamom clankov
	$zoznamclankov = file ('https://wa.toad.cz/~jankupat/GoHawks/databases/clanky.TXT');
	//ak neexistuje zoznam, tak vypise ooops
	if(!$zoznamclankov){
		echo "Ooops";
		exit;}
	
	//nastavenie kolko ma byt nadhladov na jednom zozname
	$clankynastrane = 3;
	//zistnie poctu clankov v zozname clankov
	$pocetclankov = sizeOf($zoznamclankov);
	//zistenie na kolko podzoznamov sa ma vytvorit
	$pocetstran = floor($pocetclankov/$clankynastrane);//celocislne delenie
	
	//vypisanie listy
	strankovanie($pocetclankov, $clankynastrane, $pocetstran);
	
	//generovanie clankov	
	for ($i = 1; $i <= $clankynastrane; $i++){	//...,$i <= pocetClankov na stranke....
		$stranka = $i + $clankynastrane*($_GET['strana']-1);
		
		//ak je cislo clanku vacsie ako pocet clanko ukoncenie cyklu
		if ($stranka > $pocetclankov){
			break;
		}
		
		$clanok = $zoznamclankov[$stranka-1];
		$clanokn = explode (';', $clanok);	
		$arturl = ('index.php?stranka='.$clanokn[0]);
		$imgurl = ('https://wa.toad.cz/~jankupat/GoHawks/pics/'.$clanokn[0].'.jpg');
				
		echo "<a href='$arturl'>";
		echo '<div class="article clearfix">';
		echo"<img src='$imgurl' alt='clanok'>";
		echo"<h2>$clanokn[1]</h2>";
		echo"<p><strong>$clanokn[3]</strong> | by $clanokn[4] | $clanokn[5] </p>";
		echo"<h3>$clanokn[2]</h3>";
		echo '</div>';
		echo '</a>';
	}
	
	
	//vypisanie listy na spodok stranky
	strankovanie($pocetclankov, $clankynastrane, $pocetstran);
?>        
        
		
