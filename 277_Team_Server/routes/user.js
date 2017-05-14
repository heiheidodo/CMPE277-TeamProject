/*jslint node: true */
"use strict";

var mongoDB = require("./mongodb"),
    randomstring = require("randomstring"),
    collectionName = "user",
    crypto = require('crypto'),
    post = require('./post');

function sendEmail(code, email) {
    /*  sendEmail  ------to be finished-------------------------*/
}

exports.create = function (req, res) {
    
    var email = req.body.email,
        passwordMD5 = crypto.createHash('md5').update(req.body.password).digest("hex"),
        code,
        screenName = req.body.screenName,
        insert = {},
        col = mongoDB.getdb().collection(collectionName);
    
    
    col.findOne({$or: [{email: email}, {screenName: screenName}]}, function (err, rows) {
        
        if (err) {
            console.log(err);
        } else {
            console.log(rows);
            if (rows) {
                res.send({dup: true});
                       
            } else {
                
                code = randomstring.generate({length: 6, charset: 'numeric'});
                console.log("code");
                console.log(code);

                req.session.code = code;
                req.session.email = req.body.email;
                req.session.password = passwordMD5;
                res.send({dup: false});
            }
        }

    });
};

exports.update = function (req, res) {
    
    var email = req.body.email,
        col = mongoDB.getdb().collection(collectionName);
    
    
    col.findOneAndUpdate({email: email}, {$set: req.body}, {returnOriginal: false}, function (err, rows) {
        if (err) {
            console.log(err);
        } else {
            console.log("rows------1");
            console.log(rows);
            post.getUserPost(rows.email, function (err, result) {
                rows.posts = result;
                console.log("rows------2");
                console.log(rows);
                res.send(rows);
            });
            
        }
    });
    
};

exports.verify = function (req, res) {
    
    var email = req.body.email,
        passwordMD5 = crypto.createHash('md5').update(req.body.password).digest("hex"),
        col = mongoDB.getdb().collection(collectionName),
        insert = {};
    
    if ((null === req.session) || (undefined === req.session)) {
        res.send({expired: true});
        return;
    }
    
    if ((req.body.email === req.session.email) && (req.body.code === req.session.code)) {
        
        insert = {email: email, password: passwordMD5, varified: false, friends: [], pending: [], follow: []};
        col.insertOne(insert, function (err, rows) {
            
            if (err) {
                console.log(err);
            } else {
                res.send({verified: true});
            }


        });
        
    } else {
        res.send({verified: false});
    }
    
};

/*
exports.isVerified = function (req, res) {
    
    var col = mongoDB.getdb().collection(collectionName);
    
    col.findOne({email: req.params.email}, function (err, rows) {
        
        if (err) {
            console.log(err);
        } else {
            console.log(rows);
            if (undefined !== rows.email) {
                
                res.send({verified: rows.verified});
                
            } else {
                res.send({msg: "Not found."});
            }
        }

    });
}; */


exports.signIn = function (req, res) {
    
    var passwordMD5 = crypto.createHash('md5').update(req.body.password).digest("hex"),
        query = {email: req.body.email, password: passwordMD5},
        col = mongoDB.getdb().collection(collectionName);
    
    
    col.findOne(query, function (err, rows) {
        
        if (err) {
            console.log(err);
        } else {
            console.log(rows);
            if (rows) {
                
                post.getUserPost(rows.email, function (err, result) {
                    rows.posts = result;
                    console.log("rows------2");
                    console.log(rows);
                    res.send({checked: true, User: rows});
                });
                
            } else {
                res.send({checked: false});
            }
        }

    });

};

exports.signOut = function (req, res) {
    res.send({msg: "Sign Out Successfully"});
};

exports.get = function (req, res) {
    var col = mongoDB.getdb().collection(collectionName);
    
    col.findOne({email: req.params.email}, function (err, rows) {
        
        if (err) {
            console.log(err);
        } else {
            console.log(rows);
            if (rows) {
                
                post.getUserPost(rows.email, function (err, result) {
                    rows.posts = result;
                    console.log("rows------2");
                    console.log(rows);
                    res.send(rows);
                });
                
            } else {
                res.send({msg: "Email Not Found."});
            }
        }

    });
    
};



exports.getUsers = function (req, res) {
    var col = mongoDB.getdb().collection(collectionName);
    
    col.find({visibility: "public"}).toArray(function (err, rows) {
        
        if (err) {
            console.log(err);
        } else {
            console.log(rows);
            res.send(rows);
        }
        
    });
};


exports.getPendingUsers = function (req, res) {
    var col = mongoDB.getdb().collection(collectionName);
    
    col.findOne({email: req.params.email}, function (err, rows) {
        
        if (err) {
            console.log(err);
        } else {
            console.log(rows);
            if (undefined !== rows.email) {
                res.send(rows.pending);
                
            } else {
                
                res.send({msg: "Email Not Found."});

            }
        }
        
    });
    
};

exports.request = function (req, res) {
    var email = req.params.email,
        recipientEmail = req.params.recipientEmail,
        col = mongoDB.getdb().collection(collectionName);
    
    col.updateOne({email: email}, {$push: {pending: {email: recipientEmail}}}, function (err, rows) {
        
        if (err) {
            console.log(err);
        } else {
            console.log(rows);

            res.send({msg: "Add successfully"});
                
        }
    });
};

exports.accept = function (req, res) {
    
    var email = req.params.email,
        recipientEmail = req.params.recipientEmail,
        col = mongoDB.getdb().collection(collectionName);
    
    col.updateOne({email: email}, {$push: {friends: {email: recipientEmail}}, $pull: {pending: recipientEmail}}, function (err, rows) {
        
        if (err) {
            console.log(err);
        } else {
            console.log(rows);
            
            col.updateOne({email: recipientEmail}, {$push: {friends: {email: email}}, $pull: {pending: email}}, function (err, rows) {
                
                if (err) {
                    console.log(err);
                } else {
                    console.log(rows);

                    res.send({msg: "Accept Successfully"});

                }
            });
        }

    });
};


exports.deny = function (req, res) {
    
    var email = req.params.email,
        recipientEmail = req.params.recipientEmail,
        col = mongoDB.getdb().collection(collectionName);
    
    col.updateOne({email: email}, {$pull: {pending: recipientEmail}}, function (err, rows) {
        
        if (err) {
            console.log(err);
        } else {
            console.log(rows);
            
            col.updateOne({email: recipientEmail}, {$pull: {pending: email}}, function (err, rows) {
                
                if (err) {
                    console.log(err);
                } else {
                    console.log(rows);

                    res.send({msg: "Deny Successfully"});

                }
            });
        }

    });
};

exports.follow = function (req, res) {
    
    var email = req.params.email,
        recipientEmail = req.params.recipientEmail,
        col = mongoDB.getdb().collection(collectionName);
    
    col.updateOne({email: email}, {$push: {follow: recipientEmail}}, function (err, rows) {
        
        if (err) {
            console.log(err);
        } else {
            console.log(rows);
            res.send({msg: "follow Successfully"});
            
        }

    });
    
};

function mergeTwoJsonArray(array1, array2, key) {
    var i = 0,
        j = 0,
        newArray = [],
        needPush = true;
    
    console.log("array1");
    console.log(array1);
    console.log("array2");
    console.log(array2);
    
    for (j = 0; j < array2.length; j = j + 1) {
        newArray.push(array2[j]);
    }
    
    for (i = 0; i < array1.length; i = i + 1) {
        
        for (j = 0; j < array2.length; j = j + 1) {
            
            if (array1[i][key] === array2[i][key]) {
                needPush = false;
                break;
            }
        }
        
        if (needPush) { newArray.push(array1[i]); }
    }
    
    console.log("newArray");
    console.log(newArray);
    
    return newArray;
}

exports.getVisiblePosterEmails = function (email, callback) {
    
    var resArray = [],
        col = mongoDB.getdb().collection(collectionName);
    
    col.findOne({email: email}, function (err, rows) {
        
        if (err) {
            console.log(err);
        } else {
            console.log(rows);
            if ((undefined === rows) || (null === rows)) {
                
                callback({msg: "Email not found"});
            } else {
                resArray = mergeTwoJsonArray(rows.friends, rows.follow, "email");
                
                col.find({visibility: "public"}).toArray(function (err, rows) {
        
                    if (err) {
                        console.log(err);
                    } else {
                        console.log(rows);
                        resArray = mergeTwoJsonArray(resArray, rows, "email");
                        callback(null, rows);
                    }

                });
            }
            
            
        }
    });
};

function hasJsonObject(key, value, array) {
    var i = 0;
    
    for (i = 0; i < array.length; i = i + 1) {
        if (value === array[i][key]) { return true; }
    }
    
    return false;
}


exports.getAnotherUser = function (req, res) {
    
    var email = req.params.email,
        anotherEmail = req.params.anotherEmail,
        canFollow = true,
        canRequest = true,
        col = mongoDB.getdb().collection(collectionName);
    
    col.findOne({email: anotherEmail}, function (err, rows) {
        
        if (err) {
            console.log(err);
        } else {
            console.log(rows);
            if (rows) {
                
                /* Get the user's all the post order by post time --------------------------------to be finished  */
                if (hasJsonObject("email", email, rows.friend)) {
                    canRequest = false;
                } else if (hasJsonObject("email", email, rows.pending)) {
                    canRequest = false;
                }
                
                if (hasJsonObject("email", email, rows.follow)) { canFollow = false; }
                
                
                post.getUserPost(rows.email, function (err, result) {
                    rows.posts = result;
                    console.log("rows------2");
                    console.log(rows);
                    res.send({user: rows, canRequest: canRequest, canFollow: canFollow});
                });
                
                
                
            } else {
                res.send({msg: "Email Not Found."});
            }
        }

    });
};

