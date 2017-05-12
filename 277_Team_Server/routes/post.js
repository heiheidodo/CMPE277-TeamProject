"use strict";

var mongoDB = require("./mongodb"),
    collectionName = "post";

exports.create = function (req, res){
    var text = req.body.text,
	pic = req.body.pic,
	screenName = req.body.screenName,
	email = req.body.email,
	col = mongoDB.getdb().collection(collectionName),
	time = new Date();

    query = {screenName: screenName, email: email, text: text, pic: pic, time: time};
    col.insertOne(query, function(err, results){
	if (err)
	{
	    res.send({posted: false});
	}
	else
	{
	    res.send({posted: true});
	}
    });
};

exports.getUserPost = function(req, res){
    var col = mongoDB.getdb().collection(collectionName);
    var email = req.params.email;
    
    col.find({email: email}, function (err, results){
	if (err)
	{
	    console.log(err);
	}
	else
	{
	    /* to do: return by chronical order */
	    console.log(results);
	    res.send(results);
	}
    });
};

exports.getUserTimeline = function (req, res){
    var col = mongoDB.getdb().collection(collectionName);
    /* to do: a list of emails */
};
