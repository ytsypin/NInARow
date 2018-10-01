var refreshRate = 2000; //ms
var BASIC_INFO_URL = "/NinaRow/game/BasicInfo";
var CURRENT_STATUS_URL = "/NinaRow/game/CurrentStatus";
var PLAYER_TABLE_URL = "/NinaRow/game/PlayerTable";
var GAME_BOARD_URL = "/NinaRow/game/GameBoard";

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

function refreshCurrentStatus(){
    var gameStatus;
    var currentPlayerName;

    $.ajax({
        url: CURRENT_STATUS_URL,
        type: 'GET',
        success: function(json){
            gameStatus = json.isActive? "In Progress" : "Waiting For Players";
            $('#gameStatus').text(gameStatus);

            currentPlayerName = json.currentPlayerName;
            $('#currentTurn').text(currentPlayerName);

            if(json.myTurn){
                alert("It is now your turn to play!");
                $('#myTurn').text("It's now your turn!");
                $('#leaveGame').enable();
            } else {
                $('#leaveGame').disable()
                $('#myTurn').text("");
            }
        }
    })
}

$(function(){
    showStatusBar();
    setInterval(refreshCurrentStatus, refreshRate);
    setInterval(refreshPlayerTable, refreshRate);
    setInterval(refreshGameBoard, refreshRate);
})