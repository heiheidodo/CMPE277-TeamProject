/*jslint node: true */
"use strict";

var mongoDB = require("./mongodb"),
    collectionName = "inMail",
    timestamp = require('time-stamp'),
    ObjectID = require('mongodb').ObjectID;

exports.post = function (req, res) {
    
    var body = req.body,
        col = mongoDB.getdb().collection(collectionName);
    
    console.log(req.body);
    
    body.time = timestamp('YYYY/MM/DD  HH:mm:ss:ms');
    
    col.insertOne(body, function (err, rows) {
            
        if (err) {
            console.log(err);
        } else {
            res.send({});
        }


    });
    
};


exports.get = function (req, res) {
    
    var email = req.params.email,
        col = mongoDB.getdb().collection(collectionName);
    
    
    col.find({ $or: [{ fromEmail: email }, { toEmail: email } ] }).sort({time: -1}).toArray(function (err, results) {
        if (err) {
            console.log(err);
        } else {
            console.log(results);
            res.send(results);
        }
    });
};

exports.del = function (req, res) {
    
    var id = req.params.id,
        col = mongoDB.getdb().collection(collectionName);
    
    
    col.deleteOne({_id: new ObjectID(id)}, function (err, rows) {
        
        console.log("delete call back in back end");
        console.log(rows);
        if (err) {
            console.log(err);
        } else {
            
            res.send({status: 200, msg: "delete success"});
        }
    });
};
