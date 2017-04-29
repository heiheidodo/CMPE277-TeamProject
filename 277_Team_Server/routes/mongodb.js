/*jslint node: true */
"use strict";

var MongoClient = require('mongodb').MongoClient,
    mongoURL = "mongodb://localhost:27017/social",
    db;

MongoClient.connect(mongoURL, function (err, database) {

    if (err) { throw err; }

    db = database;

});

exports.getdb = function () {
    return db;
};