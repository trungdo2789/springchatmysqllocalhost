// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target ==  document.getElementById('messagebox')) {
        document.getElementById('messagebox').style.display = "none";
    }
}

//type =1: co input nhap vao
function doMessageBox(ms,callback,type){
    var messageboxContainer= document.getElementById('messageboxContainer')
    if(messageboxContainer!=null)
    messageboxContainer.remove();

    let str=
    '<div class="messagebox" id="messagebox">'
    +'<div class="container animate" >'
        +'<div class="message" id="messageboxms"></div>'
        +'<div class="footer">'
        +'<button id="btn-ok-messagebox">ok</button>|'
        +'<button onclick=\'document.getElementById("messagebox").style.display="none"\'>cancel</button>'
        +'</div>'
    +'</div>'
    +'</div>'
    var node= document.createElement("div"); 
    node.id='messageboxContainer';
    node.innerHTML=str;
    document.body.append(node);
    if(!type||type!=1){
        $("#btn-ok-messagebox").click(function(){
            document.getElementById('messagebox').style.display = "none";
            if(!callback)
                return;
            callback();
        });
    }
    
    document.getElementById('messageboxms').innerHTML=ms;
    if(type==1){
        let ip=document.createElement('div');
        ip.innerHTML='<input type="text" id="msb-input"/>';
        document.getElementById('messageboxms').appendChild(ip);
        $("#btn-ok-messagebox").click(function(){
            document.getElementById('messagebox').style.display = "none";
            if(!callback)
                return;
            callback($("#msb-input").val());
        });
    }
    
    document.getElementById('messagebox').style.display = "block";
}



