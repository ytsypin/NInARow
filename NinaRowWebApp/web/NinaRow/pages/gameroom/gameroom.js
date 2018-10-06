var refreshRate = 2000; //ms
var BASIC_INFO_URL = "/NinaRow/game/BasicInfo";
var CURRENT_STATUS_URL = "/NinaRow/game/CurrentStatus";
var PLAYER_TABLE_URL = "/NinaRow/game/PlayerTable";
var GAME_BOARD_URL = "/NinaRow/game/GameBoard";
var REGULAR_MOVE_URL = "/NinaRow/game/RegularMove";
var POPOUT_MOVE_URL = "/NinaRow/game/PopoutMove";
var WAITING_ROOM = "/NinaRow/pages/waitingroom/waitingroom.html";

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

            if(json.isActive){
                $('#gameStarted').text("The game has started!");
            }

            $('#gameStatus').text(gameStatus);

            if(json.isActive){
                currentPlayerName = json.currentPlayerName;
                $('#currentTurn').text(currentPlayerName);

                if(json.myTurn){
                    $('#myTurn').text("It's now your turn!");
                    $('#leaveGame').prop('disabled',false);
                } else {
                    $('#leaveGame').prop('disabled',true);
                    $('#myTurn').text("");
                }
            } else {
                if(json.isWinnerFound){
                    if(json.severalWinners){
                        $('#winnerArea').text("Several winners found - " + json.winnerNames);
                    } else {
                        $('#winnerArea').text("Winner found - " + json.winnerNames);
                    }

                    setTimeout(function() {
                        alert("Winner found, redirecting back to game lobby!");
                        window.location.replace(WAITING_ROOM);
                        }, 5000);
                }
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
                .append($("<td>").append(participant.isHuman ? "Yes" : "No"))
                .append($("<td>").append(participant.turns)))
        })
    })
}

function refreshGameBoard(){
    $.ajax({
        url: GAME_BOARD_URL,
        success: function(data){
            var cols = data.cols;

            createTopButtonRow(cols);

            if(data.variant === "Popout"){
                createBottomButtonRow(cols);
            }
            createBoard(data.board, data.rows, data.cols);

            if(data.myTurn){
                $('.moveButton').prop('disabled',false).css('opacity', 1);
            } else {
                $('.moveButton').prop('disabled',true).css('opacity', 0.5);
            }
        }

    })
}

function createTopButtonRow(cols){
    $('#topButtons').empty();

    for(var i = 0; i < cols; i++){
        $('#topButtons').append("<button id='regularMove"+i+"' class='moveButton'>regular</button>")

        var buttonid = 'regularMove' + i;
        var buttonElement = document.getElementById(buttonid);

        buttonElement.onclick = function (col) {
            return function () {
                return regularMove(col);
            };
        }(i);
    }
}

function createBottomButtonRow(cols){
    $('#bottomButtons').empty();

    for(var i = 0; i < cols; i++){
        $('#topButtons').append("<button id='popoutMove"+i+"' class='moveButton'>popout</button>")

        var buttonid = 'popoutMove'+i;
        var buttonElement = document.getElementById(buttonid);

        buttonElement.onclick = function() { return popoutMove(i)};
    }
}

function regularMove(col){
    alert("Making regular move column " + col);

    $.ajax({
        url: REGULAR_MOVE_URL,
        data: {column: col},
        processData: true,
        success: function(result){
            if(result.result === "Column Full"){
                $('#messageArea').text("Column is full! Please select a valid move.");
            } else if(result.result === "Error"){
                $('#messageArea').text("Unspecified error occured");
            } else {
                $('#messageArea').text("");
            }
        }
    })
}

function popoutMove(col){
    $.ajax({
        url: POPOUT_MOVE_URL,
        data: {column: col},
        processData: true,
        success: function(result){
            if(result.result === "Column Full"){
                $('#messageArea').text("Column is full! Please select a different column.");
            } else if(result.result === "Cant Popout"){
                $('#messageArea').text("Can't popout selected column! Please select a valid move.");
            } else {
                $('#messageArea').text("");
            }

        }
    })
}

function createBoard(board, rows, cols){
    var body = $('#board');

    body.empty();

    for(var i = 0; i < rows; i++){
        rowDiv = $(document.createElement('div'));
        rowDiv.addClass('rowDiv');
        rowCells = $(document.createElement('div'));
        rowCells.addClass('rowCells');
        rowCells.appendTo(rowDiv);

        for(var j = 0; j < cols; j++){
            squareDiv = $(document.createElement('div'));
            squareDiv.addClass('cell');
            squareDiv.appendTo(rowCells);

            color = board[i][j];
            if(color !== 0){
                squareDiv.addClass('player'+color);
            } else {
                squareDiv.addClass("empty");
            }

        }

        rowDiv.appendTo(body);
    }
}

$(function(){
    showStatusBar();
    setInterval(refreshCurrentStatus, refreshRate);
    setInterval(refreshPlayerTable, refreshRate);
    setInterval(refreshGameBoard, refreshRate);
})