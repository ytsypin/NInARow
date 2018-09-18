var refreshRate = 2000; //ms
var chatVersion = 0;
var gameListVersion = 0;
var USER_LIST_URL = "/NinaRow/userlist";
var GAME_LIST_URL = "/NinaRow/gameList";
var CHAT_LIST_URL = "/NinaRow/chat";


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

function gameList(){
    $.ajax({
        url: GAME_LIST_URL,
        success: function(gameList){
            refreshGameList(gameList);
        }
    });
}

function appendToGameListArea(games){
    $.each(games || [], appendGameEntry);

    var scroller = $("gamesArea");
    var height = scroller[0].scrollHeight - $(scroller).height();
    $(scroller).stop().animate({ scrollTop: height}, "slow");
}

function appendGameEntry(index, entry){
    var entryElement = createGameEntry(entry, index);

    $("gamesTable").apppend(entryElement).append("<tr>");
    //$('#joinGame' + index).onclick = joinGame(index);
}

function createGameEntry(entry, index){
    var html = "<td>" + entry.gameName + "</td><td>" + entry.uploader +  "</td>" +
             "<td>" + entry.goal + "</td><td>"+ entry.rows + "x" + entry.cols + "</td>" +
             "<td>"+ entry.activePlayers + "/" + entry.requiredPlayers +"</td>" +
             "<td>" + "<button id='joinGame" + index + "' class='btn-primary btn-xs'>Join</button>"  + "</td>";
    return html;
}

/*
 TODO: join a game
function joinGame(index){
    $.ajax({

    });
}
*/

function ajaxGameListContent(){
    $.ajax({
        url: GAME_LIST_URL,
        data: "gameListVersion="+ gameListVersion,
        dataType: 'json',
        success: function(data){
            /*
            Data is of the form:
            {
                "games": [
                    {
                        "gameName":"game name",
                        "uploader":"username",
                        "goal":5,
                        "rows":4,
                        "cols":5,
                        "playersJoined":2,
                        "playersNeeded":5
                        "active":"true"
                    }, ...
                ]
                "version":1
            }
             */
            console.log("Server game list version: " + data.version + ", Current game list version: " + gameListVersion);

            if(data.version != gameListVersion){
                gameListVersion = data.version;
                appendToGameListArea(data.games);
            }
            triggerAjaxGameListContent();
        },
        error: function(error){
            // TODO: Display or log error
            triggerAjaxGameListContent();
        }
    })
}

function triggerAjaxGameListContent(){
    setTimeout(ajaxGameListContent, refreshRate);
}

$(function() {
    setInterval(ajaxUsersList, refreshRate);
    triggerAjaxGameListContent();
})

