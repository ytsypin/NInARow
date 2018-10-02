var refreshRate = 2000; //ms
var USER_LIST_URL = "/NinaRow/userlist";
var GAME_LIST_URL = "/NinaRow/gameList";
var USERNAME_URL = "/NinaRow/username";
var LOGOUT_URL = "/NinaRow/logout";
var JOIN_GAME_URL = "/NinaRow/joinGame";
var GAME_ROOM_URL = "/NinaRow/pages/gameroom/gameroom.html";


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
    $.getJSON(GAME_LIST_URL, function(data){
        $('#gamesTableBody').empty();
        $(data).each(function(i, game){
            $('#gamesTableBody').append($("<tr>")
                .append($("<td>").append(i+1))
                .append($("<td>").append(game.name))
                .append($("<td>").append(game.variant))
                .append($("<td>").append(game.uploader))
                .append($("<td>").append(game.dimensions))
                .append($("<td>").append(game.goal))
                .append($("<td>").append(game.players))
                .append($("<td>").append(
                    "<button id = 'interact" + i + "' class='" +
                   // If the game isn't active yet players can join the game
                   // Otherwise, players can spectate
                   (!game.isActive ?
                       "joinGame'>Join Game</button>" :
                       "disabled' disabled='disabled'>Running</button>"))));

            var buttonid = 'interact'+i;
            var buttonElement = document.getElementById(buttonid);

            buttonElement.onclick = function(){ return joinGame(i)};
        })
    });
}

function joinGame(gameNum){
    alert("Joining game" + gameNum + "!");

    $.ajax({
        url: JOIN_GAME_URL,
        data: {gameNumber: gameNum},
        processData: true,
        success: function(r) {
            window.location.replace(GAME_ROOM_URL);
        }
    })
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
    });
}

function clearFileInput(){
    $('#gameUploadForm').trigger("reset");
}

$(function() {
    showStatusBar();
    setInterval(ajaxUsersList, refreshRate);
    setInterval(refreshGamesList, refreshRate);

    $('#gameUploadForm').submit(function() {
        var file = this[0].files[0];
        var data = new FormData();
        data.append("fake-key", file);

        $.ajax({
            type: 'POST',
            url: this.action,
            enctype: 'multipart/form-data',
            data: data,
            contentType: false,
            processData: false,
            timeout: 4000,
            success: function (response) {
                if(response.noFile){
                    alert(response.errorMessage)
                } else {
                    if (response.isLoaded) {
                        alert("Load game success!!");
                        refreshGamesList();
                        clearFileInput();
                    } else {
                        clearFileInput();
                        alert(response.errorMessage);
                    }
                }
            }
        });
        return false;
    });
});

