var refreshRate = 2000; //ms
var BASIC_INFO_URL = "/NinaRow/game/BasicInfo";

function showStatusBar(){
    var name;
    var humanity;
    var variant;
    var goal;

    $.ajax({
        url: BASIC_INFO_URL,
        type: 'GET',
        success: function(json){
            name = json.username;
            humanity = json.humanity === true? "Human" : "Computer";
            variant = json.variant;
            goal = json.goal;
            $('.usernameSpan').text("Hello " + name + " The " + humanity + ", enjoy!");
            $('.variant').text("Variant: " + variant);
            $('.goal').text("Goal: " + goal);
        }
    })
}


$(function(){
    showStatusBar();
    setInterval(refreshCurrentStatus, refreshRate);
    setInterval(refreshPlayerTable, refreshRate);
    setInterval(refreshGameBoard, refreshRate);
})