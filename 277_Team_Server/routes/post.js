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

exports.getUserPost = function(email, callback){
    var col = mongoDB.getdb().collection(collectionName);
    
    col.find({email: email}, function (err, results){
	if (err)
	{
	    console.log(err);
	}
	else
	{
	    insertionSort(results, "time");
	    console.log(results);
	    callback(null, results);
	}
    });
};

exports.getUserTimeline = function (emails, callback){
    var col = mongoDB.getdb().collection(collectionName);
   
    col.find({email: {$all: emails}}), function (err, results)
    {
	if (err)
	{
	    console.log(err);
	}
	else
	{
	    insertionSort(results, "time");
	    console.log(results);
	    callback(null, results);
	}
    }
};

function insertionSort(files,attrToSortBy){
  for(var k=1; k < files.length; k++){
     for(var i=k; i > 0 && new Date(files[i][attrToSortBy]) < 
       new Date(files[i-1][attrToSortBy]); i--){

        var tmpFile = files[i];
        files[i] = files[i-1];
        files[i-1] = tmpFile;

     }
  }

}
