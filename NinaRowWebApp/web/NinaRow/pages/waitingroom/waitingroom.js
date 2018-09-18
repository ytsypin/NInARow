var refreshRate = 2000; //ms
var chatVersion = 0;
var USER_LIST_URL = "/NinaRow/userlist";
var GAME_LIST_URL = "/NinaRow/gameList";
var LOAD_GAME_URL = "/NinaRow/loadGame";
var CHAT_LIST_URL = "/NinaRow/chat";


// TODO - This
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
        $('.userNameSpan').text("Welcome " + window.username + ", you are a " + (json.isComputer ? "computer" : "human"));
    }
}

function loadGameClicked(event){
    var file = event.target.files[0];
    var reader = new FileReader();
    var creatorName = window.username;

    reader.onload=function (){
        var content = reader.result;
        $.ajax({
            url: LOAD_GAME_URL,
            data: {
                file: content,
                creator: creatorName
            },
            type: 'POST',
            success: loadGameCallback
        });
    };
}

function loadGameCallback(json){
    if(json.isLoaded){
        alert("Load game success!!");
        refreshGamesList();
        clearFileInput();
    } else {
        clearFileInput();
        alert(json.errorMessage);
    }
}

function refreshGamesList(){
    $.ajax({
        url: GAME_LIST_URL,
        type: 'GET',
        success: refreshGamesListCallback
    })
}

function refreshGamesListCallback(json){
    var gamesTable = $('.gamesArea tbody');
    gamesTable.empty();
    var gamesList = json.games;

    gamesList.forEach(function (game){
        var tr = $(document.createElement('tr'));
        var tdGameNumber = $(document).createElement('td').text(game.key);
        var tdGameName = $(document.createElement('td')).text(game.name);
        var tdGameUploader = $(document.createElement('td')).text(game.uploaderName);
        var tdGoal = $(document.createElement('td')).text(game.goal);
        var tdBoardSize = $(document.createElement('td')).text(game.rows + "x" + game.cols);
        var tdPlayers = $(document.createElement('td')).text(game.registeredPlayers + "/" + game.requiredPlayers);

        tdGameNumber.appendTo(tr);
        tdGameName.appendTo(tr);
        tdGameUploader.appendTo(tr);
        tdGoal.appendTo(tr);
        tdBoardSize.appendTo(tr);
        tdPlayers.appendTo(tr);

        tr.appendTo(gamesTable);
    });

    // TODO - replace with join game button for each game

    var tr = $('.gamesTableBody tr');
    for(var i = 0; i < tr.length; i++){
        tr[i].onClick = createGameDialog;
    }
}

/* TODO - replace with join game button
function createGameDialog(event){
    var td = event.currentTarget.children[0];
    var number = td.innerText;

    $.ajax({
        url: 'games',
        data:{
            action: 'gameDetails',
            key: number
        },
        type: 'GET',
        success: createGameDialogCallback
    })
}

function createGameDialogCallback(json){
    var div = $('.dialogDiv')[0];
    div.style.display = "block";
    var playersNamesDiv = $('.playersNames');

    var key = json.key;
    var creatorName = json.creatorName;
    var gameName = json.gameTitle;
    var goal = json.goal;
    var boardSize = json.rows + " X " + json.cols;
    var playerNumber = json.registeredPlayers + " / " + json.requiredPlayers;

    // language=JQuery-CSS
    $('.key').text("Game id: " + key + ".");
    $('.creatorName').text("Game Creator: " + creatorName + ".");
    $('.goal').text("Goal: " + goal + ".");
    $('.gameName').text("Game Title: " + gameName);
    $('.boardSize').text("Board size: " + boardSize);
    $('.playerNumber').text("Players : " + playerNumber);
    for (i = 0; i < json.registeredPlayers; i++) {
        var playerDiv = $(document.createElement('div'));
        playerDiv.addClass('playerDiv');
        playerDiv.appendTo(playersNamesDiv);
    }

    var playerDivs = $('.playerDiv');
    for (i = 0; i < json.registeredPlayers; i++) {
        playerDivs[i].innerHTML = (+i + 1) + '. ' + json.players[i].m_Name + '.';
    }
}
*/

//users = a list of usernames, essentially an array of javascript strings:
// ["moshe","nachum","nachche"...]
function refreshUsersList(users) {
    //clear all current users
    $("#userslist").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(index, username) {
        console.log("Adding user #" + index + ": " + username);
        //create a new <option> tag with a value in it and
        //appeand it to the #userslist (div with id=userslist) element
        $('<li>' + username + '</li>').appendTo($("#userslist"));
    });
}

function ajaxUsersList(){
    $.ajax({
        url: USER_LIST_URL,
        success: function(users){
            refreshUsersList(users);
        }
    });
}

/*
 TODO: join a game
function joinGame(index){
    $.ajax({

    });
}
*/

$(function() {
    setInterval(ajaxUsersList, refreshRate);
    setInterval(refreshGamesList, refreshRate);
})

