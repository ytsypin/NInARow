$(function() {
    $('#usernameForm').submit( function () {
        var parameters = $(this).serialize();

        $.ajax({
            data: parameters,
            url: this.action,
            timeout: 2000,
            success: function (response) {
                if(response.success) {
                    window.location.replace("/NinaRow/waitingroom.html")
                } else {
                    alert("Username taken! Please enter a unique username.")
                }
            }
        })
    })

}