window.onLoad = function (){
    refreshLoginStatus();
    refreshUserList();
    setInterval(refreshUserList,2000);
    setInterval(refreshGamesList, 2000);
    setInterval(refreshLoginStatus, 2000);
};

function refreshLoginStatus(){
    $.ajax({
        url:'login',
        data:{
            action:"status"
        },
        type:'GET',
        success: statusCallback
    });
}

function statusCallback(json){
    if(!json.isConnected){
        window.location = "index.html";
    } else if(json.gameNumber != -1){
        window.location = "waitingroom.html";
    } else {
        $('.userNameSpan').text("Welcome " + json.username + ", you are a " + (json.isComputer ? "computer" : "human"));
    }
}

