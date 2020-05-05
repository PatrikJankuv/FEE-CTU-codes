<?php
$actual_link = "https://$_SERVER[HTTP_HOST]$_SERVER[REQUEST_URI]";
$page = substr($actual_link, -3);

if (isset($_POST['comment'])){
		echo htmlspecialchars($komentar);
	}
?>
<div class = "comments">
<h3>Komentáre</h3>
<?php		
	//vypise vsetky komentare
	$comments = file ('https://wa.toad.cz/~jankupat/GoHawks/databases/comments.txt');
	$numberComments = sizeOf($comments);
		echo "<div class='commentsbody'>";
		for ($i = 1; $i <= $numberComments; $i++){
			$comment = $comments[$i-1];
			$ncom = explode (';', $comment);
			
			if ($page === $ncom[0]){
				$prevent = $ncom[2];
				echo "<div class='comment'><strong>".htmlspecialchars($ncom[1])."</strong>: ".htmlspecialchars($ncom[2])."<hr></div>";
				
			}
		}


	//ak je prihlaseny uzivatel vypise formular na pridanie komentara
	if (isset($_SESSION['username']) and ($_SESSION['username'] != '')){
		echo "
		<form name='comment' method='post'>
		<label for='comment'><textarea id='comment' name='commentar' class='comarea' rows='4' cols='90' required></textarea></label><span id='fillwarning'></span><br>
		<label for='commentsubmit'><input id='commentsubmit' type='submit' name='com' value='Pridať komentár' onClick='commentValidation()'></label>
		</form>";}
	//neprihlaseny uzivatel informovany o potrebe registrovania
	else
		echo '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Komentovať môžu iba prihlasený používatelia <a href="index.php?stranka=login" class="registration">Prihlásenie</a>';
?>
</div>
</div>
</div>
