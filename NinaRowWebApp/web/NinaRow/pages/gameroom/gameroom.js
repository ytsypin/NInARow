var refreshRate = 2000; //ms
var BASIC_INFO_URL = "/NinaRow/game/BasicInfo";
var CURRENT_STATUS_URL = "/NinaRow/game/CurrentStatus";
var PLAYER_TABLE_URL = "/NinaRow/game/PlayerTable";
var GAME_BOARD_URL = "/NinaRow/game/GameBoard";
var REGULAR_MOVE_URL = "/NinaRow/game/RegularMove";
var POPOUT_MOVE_URL = "/NinaRow/game/PopoutMove";

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

function refreshPlayerTable(){
    $.getJSON(PLAYER_TABLE_URL, function(data){
        $('#playerTableBody').empty();
        $(data).each(function(i, participant){
            $('#playerTableBody').append($("<tr>")
                .append($("<td>").append(participant.name))
                .append($("<td>").append(participant.isHuman))
                .append($("<td>").append(participant.turns)))
        })
    })
}

function refreshGameBoard(){
    $.get({
        url: GAME_BOARD_URL,
        success: function(data){
            var cols = data.cols;

            createTopButtonRow(cols);

            if(data.variant = "Popout"){
                createBottomButtonRow(cols);
            }

            createBoard(data.board, data.rows, data.cols);
        }

    })
}

function createTopButtonRow(cols){
    for(var i = 0; i < cols; i++){
        $('#topButtons').append("<button id='regularMove"+i+"' class='moveButton'></button>")

        var buttonid = 'regularMove'+i;
        var buttonElement = document.getElementById(buttonid);

        buttonElement.onclick = function() { return regularMove(i)};
    }
}

function createBottomButtonRow(cols){
    for(var i = 0; i < cols; i++){
        $('#topButtons').append("<button id='popoutMove"+i+"' class='moveButton'></button>")

        var buttonid = 'popoutMove'+i;
        var buttonElement = document.getElementById(buttonid);

        buttonElement.onclick = function() { return popoutMove(i)};
    }
}

function regularMove(col){
    $.ajax({
        url: REGULAR_MOVE_URL,
        data: {col: col},
        processData: true
    })
}

function popoutMove(col){
    $.ajax({
        url: POPOUT_MOVE_URL,
        data: {col: col},
        processData: true
    })
}

function createBoard(board, rows, cols){
    for(var i = 0; i < rows; i++){
        for(var j = 0; j < cols; j++){
            // draw the board
        }
    }
}

$(function(){
    showStatusBar();
    setInterval(refreshCurrentStatus, refreshRate);
    setInterval(refreshPlayerTable, refreshRate);
    setInterval(refreshGameBoard, refreshRate);
})