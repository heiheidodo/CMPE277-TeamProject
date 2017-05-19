/*jslint node: true */
"use strict";

var mongoDB = require("./mongodb"),
    collectionName = "post",
    user = require("./user"),
    timestamp = require('time-stamp');

function insertionSort(files, attrToSortBy) {
    var k,
        i,
        tmpFile;
    
    for (k = 1; k < files.length; k = k + 1) {
        for (i = k; i > 0 && (new Date(files[i][attrToSortBy]) < new Date(files[i - 1][attrToSortBy])); i = i - 1) {

            tmpFile = files[i];
            files[i] = files[i - 1];
            files[i - 1] = tmpFile;

        }
    }

}

exports.create = function (req, res) {
    var text = req.body.text,
	    pic = req.body.pic,
	    screenName = req.body.screenName,
	    email = req.body.email,
	    col = mongoDB.getdb().collection(collectionName),
	    time = new Date(),
        query;
    
    console.log(time);

    query = {screenName: screenName, email: email, text: text, pic: pic, time: timestamp('YYYY:MM:DD:HH:mm:ss:ms')};
    col.insertOne(query, function (err, results) {
        if (err) {
            res.send({posted: false});
        } else {
            res.send({posted: true});
	    }
    });
};

exports.getUserPost = function (email, callback) {
    var col = mongoDB.getdb().collection(collectionName);
    
                                               
    col.find({email: email}).sort({time: -1}).toArray(function (err, results) {
        if (err) {
            console.log(err);
        } else {
            console.log(results);
            callback(null, results);
        }
    });
};


exports.getUserTimeline = function (req, res) {
    var email = req.params.email;
    
    user.getVisiblePosterEmails(email, function (err, emails) {
        var col = mongoDB.getdb().collection(collectionName);
   
        col.find({email: email}).sort({time: -1}).toArray(function (err, results) {
            if (err) {
                console.log(err);
            } else {
                console.log(results);
                res.send(results);
            }
        });
    });
    
};
/*
exports.getUserTimeline = function (emails, callback) {
    var col = mongoDB.getdb().collection(collectionName);
   
    col.find({email: {$all: emails}}, function (err, results) {
        if (err) {
            console.log(err);
        } else {
            insertionSort(results, "time");
            console.log(results);
            callback(null, results);
        }
    });
}; */


