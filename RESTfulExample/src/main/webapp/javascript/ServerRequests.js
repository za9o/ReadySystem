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

function getTeamStatus()
{
    $.ajax({
        url: "http://localhost:8080/rest/test/json/teams/status",
        method: "GET",
        success: function (data) {
            blueTeamData = data.BlueTeamStatus;
            redTeamData = data.RedTeamStatus;
            $('.blueTeamStatus').text('Blue team is ' + blueTeamData);
            $('.redTeamStatus').text('Red team is ' + redTeamData);

            if (blueTeamData == 1) {
//                $('.boxBlueTeam').css("background", "green");
                $('#bluebox').prop('checked', true);
            } else if (blueTeamData == 0) {
//                $('.boxBlueTeam').css("background", "blue");
                $('#bluebox').prop('checked', false);
            }

            if (redTeamData == 1) {
//                $('.boxRedTeam').css("background", "green");
                $('#redbox').prop('checked', true);
            } else if (redTeamData == 0) {
//                $('.boxRedTeam').css("background", "red");
                $('#redbox').prop('checked', false);
            }

            if (window.blueTeamData == 1 && window.redTeamData == 1) {
                canOnlyFireOnce();
            }
        },
        error: function (jqXHR, exception) {
            console.log(jqXHR);
        }
    });
}

function getGPSPositionAll()
{
    $.ajax({
        url: "http://localhost:8080/rest/test/json/teams/getgpsposition",
        method: "GET",
        success: function (data) {
//-----------------------------Worsk to extract everything--------------------
            $.each(data, function (key, value) {
                $.each(value, function (key, value) {
                    if (key === 'gpsid')
                        $('#combobox').append($('<option>').text(value).attr('value', value));
                    console.log(key, value);
                });
            });
//            -------------------Dette funker for å hente data---------------
//            for (var i = 0, len = data.length; i < len; i++) {
//                console.log(data[i]);
////                $('.gpsid').text('gpsid is ' + data[1].gpsid);
//                $('.gpsid').text('gpsid is ' + data[i].gpsid);
//                $('.latitude').text('Latitude is ' + data[1].latitude);
//                $('.longitude').text('Longitude is ' + data[1].longitude);
//            }
//            ---------------------------------------------------------------
        },
        error: function (jqXHR, exception) {
            console.log(jqXHR);
        }
    });
}

function setGPSValues() {
    $.ajax({
        url: "http://localhost:8080/rest/test/json/teams/getgpsposition",
        method: "GET",
        success: function (data) {
            var str = "";
            var lat = "";
            var long = "";

            $("select option:selected").each(function () {
                str += $(this).text();
                $.each(data, function () {
                    if (this['gpsid'] === str) {
                        lat = this['latitude'];
                        long = this['longitude'];
                        $('.latitude').text("Latitude: " + lat);
                        $('.longitude').text("Longitude: " + long);
                    }
                });
            });
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
            console.log(exception);
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