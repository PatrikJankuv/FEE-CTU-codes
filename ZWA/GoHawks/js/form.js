function ValidateForm(inputText)
{
    var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    if(inputText.value.match(mailformat)){
        
        return true;
    }
    else{
        document.getElementById('result').innerHTML = "Zadaj validny email";
        document.getElementById('mail').classList.add('bad');
        event.preventDefault();
        return false;
    }
}


