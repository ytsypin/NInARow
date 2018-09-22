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
    // TODO - change button appearance/accessibility according to the number of players

    $.getJSON(GAME_LIST_URL, function(data){
        $('#gamesTableBody').empty();
        $(data).each(function(i, game){
           $('#gamesTableBody').append($("<tr>")
               .append($("<td>").append(i+1))
               .append($("<td>").append(game.name))
               .append($("<td>").append(game.uploader))
               .append($("<td>").append(game.dimensions))
               .append($("<td>").append(game.goal))
               .append($("<td>").append(game.players))
               .append($("<td>").append("<button class='joinGame'>Join Game</button>"))
           )
       })
    });
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
    });
}

$(function() {
    showStatusBar();
    setInterval(ajaxUsersList, refreshRate);
    setInterval(refreshGamesList, refreshRate);

    $('#gameUploadForm').submit(function(){
       var file = this[0].files[0];
       var data = new FormData();
       data.append("fake-key", file);

       $.ajax({
           type: 'POST',
           url: this.action,
           enctype:'multipart/form-data',
           data: data,
           contentType: false,
           processData: false,
           timeout: 4000,
           success: function(response){
               if(response.isLoaded){
                   alert("Load game success!!");
                   refreshGamesList();
                   //clearFileInput();
               } else {
                   //clearFileInput();
                   alert(response.errorMessage);
               }
           }
        });
       return false;
    });
});

