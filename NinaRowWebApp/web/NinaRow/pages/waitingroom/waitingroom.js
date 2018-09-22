var refreshRate = 2000; //ms
var chatVersion = 0;
var USER_LIST_URL = "/NinaRow/userlist";
var GAME_LIST_URL = "/NinaRow/gameList";
var LOAD_GAME_URL = "/NinaRow/loadGame";
var CHAT_LIST_URL = "/NinaRow/chat";
var USERNAME_URL = "/NinaRow/username";
var LOGOUT_URL = "/NinaRow/logout";


function logoutClicked(){
    $.ajax({
        url: LOGOUT_URL,
        type: 'GET',
        success: redirectToIndexPage
    });
}

function redirectToIndexPage(){
    window.location.href= "/NinaRow/index.html";
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

function showStatusBar(){
    var name;
    var humanity;
    $.ajax({
        url: USERNAME_URL,
        type: 'GET',
        success: function(data){
            name = data.username;
            humanity = data.humanity === true ? "Human" : "Computer";
            $('.userNameSpan').text("Hello " +  name + " The " + humanity + "!");
        }
    })

}

$(function() {
    showStatusBar()
    setInterval(ajaxUsersList, refreshRate);
    setInterval(refreshGamesList, refreshRate);

    $('#gameUploadForm').submit(function(e){
       var form = $(this);
       var url = form.attr('action');
       var data = new FormData(form);

       $.ajax({
           type: 'POST',
           url: url,
           enctype:'multipart/form-data',
           data: data,
           contentType: false,
           processData: false,
           timeout: 4000,
           success: function(response){
               var jsonResponse = JSON.parse(response);
               if(jsonResponse["isLoaded"]){
                   alert("Load game success!!");
                   refreshGamesList();
                   //clearFileInput();
               } else {
                   //clearFileInput();
                   alert(jsonResponse["errorMessage"]);
               }
           }
        });
       return false;
    });
})

