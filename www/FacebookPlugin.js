var exec = require("cordova/exec");

// TODO: Needs Success and failure handling.

var FacebookPlugin = {

    sendInvite: function () {
        exec(function(){console.log("Error")}, function(){onsole.log("Succes")}, "InvitePlugin", "invite", []);
    },

    sendLogin: function () {
        exec(function(){console.log("Error")}, function(){onsole.log("Succes")}, "InvitePlugin", "invite", []);
    }
};

module.exports = FacebookPlugin;