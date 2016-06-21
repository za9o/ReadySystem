/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//$(document).ready(function () {
//    $.ajax({
//        url: "http://localhost:8080/rest/test/json/teams/status",
//        method: "GET"
//    }).then(function (data) {
//        $('.blueTeamStatus').append(data.blueTeam);
//        $('.redTeamStatus').append(data.redTeam);
//    });
//});

$(document).ready(function () {

    $('.boom').hide();

    $('#demo').pietimer({
        seconds: 10,
        color: 'rgba(0, 0, 0, 0.8)',
        height: 200,
        width: 200,
        is_reversed: false

    },
            function () {
                $('.boom').show('slow');
//                alert("Finish!"); Add further function calls here, like activate sound
            });
});

//function getTeamStatus()
//{
//
//    $.ajax({
//        url: "http://localhost:8080/rest/test/json/teams/status",
//        method: "GET"
//    }).then(function (data) {
//        blueTeamData = data.blueTeamStatus;
//        redTeamData = data.redTeamStatus;
//        $('.blueTeamStatus').text('Blue team is ' + blueTeamData);
//        $('.redTeamStatus').text('Red team is ' + redTeamData);
//
//        if (blueTeamData == "true") {
//            $('.boxBlueTeam').css("background", "green");
//        } else if (blueTeamData == "false") {
//            $('.boxBlueTeam').css("background", "blue");
//        }
//
//        if (redTeamData == "true") {
//            $('.boxRedTeam').css("background", "green");
//        } else if (redTeamData == "false") {
//            $('.boxRedTeam').css("background", "red");
//        }
//
//        if (window.blueTeamData == "true" && window.redTeamData == "true") {
//            canOnlyFireOnce();
//        }
//    });
//}

function getTeamStatus()
{
    $.ajax({
        url: "http://localhost:8080/rest/test/json/teams/status",
        method: "GET",
        success: function (data) {
            blueTeamData = data.blueTeamStatus;
            redTeamData = data.redTeamStatus;
            $('.blueTeamStatus').text('Blue team is ' + blueTeamData);
            $('.redTeamStatus').text('Red team is ' + redTeamData);

            if (blueTeamData == "true") {
                $('.boxBlueTeam').css("background", "green");
            } else if (blueTeamData == "false") {
                $('.boxBlueTeam').css("background", "blue");
            }

            if (redTeamData == "true") {
                $('.boxRedTeam').css("background", "green");
            } else if (redTeamData == "false") {
                $('.boxRedTeam').css("background", "red");
            }

            if (window.blueTeamData == "true" && window.redTeamData == "true") {
                canOnlyFireOnce();
            }
        },
        error: function (jqXHR, exception) {
            console.log(jqXHR);
        }
    });
}

function reset()
{
    console.log("Inne i click");
    event.preventDefault();
    $.ajax({
        url: "http://localhost:8080/rest/test/json/teams/reset",
        type: "POST",
        contentType: "text/plain",
        dataType: "text",
        success: function () {
        },
        error: function (jqXHR, exception) {
            console.log(jqXHR);
        }
    });
}

function gameStatus() {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/rest/test/json/teams/gamestatus",
        contentType: "application/json",
        dataType: "json",
        data: "{\"gameStatus\": \"GAMEON\"}",
        success: function () {
            alert("Success");
        }

    });

}


var canOnlyFireOnce = once(function () {
    $('.boom').hide();
    $('#demo').pietimer('start');
    return false;
});

function once(fn, context) {
    var result;

    return function () {
        if (fn) {
            result = fn.apply(context || this, arguments);
            fn = null;
        }
        return result;
    };
}

//This is for the text based countdown
//
//var canOnlyFireOnce = once(function () {
//    var count = 10,
//            countdown = setInterval(function () {
//                $("p.countdown").html(count + " seconds remaining!");
//                $('.countDownTimer').val(count);
//                if (count == 0) {
//                    count = 11; //since it will be reduced right after this
//                    clearInterval(countdown); //<-- use this if you want to stop
//                }
//                count--;
//            }, 1000);
//});