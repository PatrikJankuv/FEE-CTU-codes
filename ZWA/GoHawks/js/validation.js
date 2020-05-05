//overuje ci email spulnuje emailovy format
function validateEmail(inputEmail){
    var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;

    if(inputEmail.value.match(mailformat)){
		inputEmail.classList.remove("bad");
		document.getElementById("mailwarning").innerHTML = "";
        return true;
		}
    else{
		inputEmail.classList.add("bad");
		document.getElementById("mailwarning").innerHTML = "<br>Zadal si neplatnu emailovu adresu";
        document.regist.text1.focus();
        return false;
		}
    
    }
	
//overuje ci meno a priezvisko obsahuje medzeru	
function validateName(inputName){
	var name = inputName.value;
	
	if (name.indexOf(" ") >= 1){
		inputName.classList.remove("bad");
		document.getElementById("namewarning").innerHTML = "";
		return true;}
	else{
		inputName.classList.add("bad");
		document.getElementById("namewarning").innerHTML = "<br>Meno a prizvislo obsahuje aspoň jednu medzeru.";
		document.regist.meno.focus();
		return false;
		}
}

//overuje ci je policko prazdne
function isUsername(text){
	
	if (text.value.length < 4)	{
		document.getElementById("usernamewarning").innerHTML = "<br>Username ma aspon 4 znaky";
		text.classList.add("bad");
		e.preventDefault();
		document.regist.nickname.focus();
		return false;
	}
	else{
		document.getElementById("usernamewarning").innerHTML = "";
		text.classList.remove("bad");
		return true; 
	}
}

//overuje ci je vyplnene policko
function isFill(text){
	
	if (text.value == "")	{
		document.getElementById("fillwarning").innerHTML = "<br>Toto pole nesmie byť prázdne.";
		text.classList.add("bad");
		e.preventDefault();
		document.regist.nickname.focus();
		return false;
	}
	else{
		document.getElementById("fillwarning").innerHTML = "";
		text.classList.remove("bad");
		return true; 
	}
}


//overuje ci heslo obsahuje velky, maly znak a cislo a dlzku retazsca
function validatePass(pass){
	var lowerCaseLetters = /[a-z]/g;
	var upperCaseLetters = /[A-Z]/g;
	var numbers = /[0-9]/g;
	
	if((pass.value.match(lowerCaseLetters)) && (pass.value.match(upperCaseLetters)) && (pass.value.match(numbers) && (pass.value.length >= 8))){
		pass.classList.remove("bad");
		document.getElementById("passwarning").innerHTML = "";
		return true;
	}
	else{
		pass.classList.add("bad");
		document.getElementById("passwarning").innerHTML = "<br>Neplatny format \nHeslo musí obsahovať \n-Min. 1 malý znak \n-Min. 1 veľký znak \n-Min. 1 číslo \n-Minimálna dĺžka 8 znakov";
		document.regist.psw.focus();
		return false;
	}
}

//kontroluje ci sa heslo a kontrolne heslo zhoduje
function isSame(first, second){
	if ((first.value) == (second.value)){
		document.getElementById("repasswarning").innerHTML = "";
		second.classList.remove("bad");
		return true;
	}
	else{
		second.classList.add("bad");
		document.getElementById("repasswarning").innerHTML = "<br>Hesla sa nezhoduju.";
		document.regist.pswheck.focus();
		return false;
	}
}

//kontrluje ci su splnene podmienky
function isCheck(checkbox){
	if (checkbox.checked == true){
		document.getElementById("termswarning").innerHTML = "";
		return true;		
	}
	else{
		document.getElementById("termswarning").innerHTML = "<br>Musite suhlasit s podmienkami.";
		document.regist.terms.focus();
		return false;
	}
}   
    
//validacia po kliknuti na tlacitko registrovat	
function registrationValidation(){
	validateName(document.form1.meno);
	validateEmail(document.form1.text1);
	isUsername(document.form1.nickname);
	validatePass(document.form1.psw);
	isSame(document.form1.psw, document.form1.pswcheck);
	isCheck(document.form1.terms);
}

function loginValidation(){
	isUsername(document.login.username);
	isFill(document.login.pass);
}

//kontroluje komentare ci su vyplnene
function commentValidation(){
	isFill (document.comment.commentar);
	}



