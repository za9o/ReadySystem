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

function getTeamStatus() 
{
    $.ajax({
        url: "http://localhost:8080/rest/test/json/teams/status",
        method: "GET"
    }).then(function (data) {
        $('.blueTeamStatus').text('Blue team is ' + data.blueTeam);
        $('.redTeamStatus').text('Red team is ' + data.redTeam);
    });
}

function reset()
{
    $.ajax({
        url: "http://localhost:8080/rest/test/json/teams/reset",
        type: "POST",
        contentType: "application/json",
        dataType: "json",
        success: function () {
            alert("Reset performed");
        }
    });
}
