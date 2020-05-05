<?php
//sluzi na vyber vzhladu
?>


<div class="design-form">
<br>
        <?php
		//ak je nastavene cookie s zvhladom, tak vypise
        if (isset($_COOKIE['design'])) {
            echo "<p>Súčasný vzhľad je: " . htmlspecialchars($_COOKIE['design']) . '</p>';
            echo "<p>Vyberte nový vzhľad:</p>";
        }
        ?>
        <form method="post">
            <label>
                <input type="radio" name="design" tabindex="1" value="day">
                Light
            </label>
            <label>
                <input type="radio" name="design" tabindex="2" value="night">
				Dark
            </label><br>
        <button class="registration">Vybrat vzhľad</button>
    </form>
</div>
<?php

?>
