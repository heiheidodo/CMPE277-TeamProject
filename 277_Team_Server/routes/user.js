/*jslint node: true */
"use strict";

var mongoDB = require("./mongodb"),
    randomstring = require("randomstring"),
    collectionName = "user",
    crypto = require('crypto'),
    post = require('./post'),
    codeJsonArray = [],
    onlineJsonArray = [],
    nodemailer = require('nodemailer'),
    smtpConfig = {
        host: 'smtp.gmail.com',
        port: 465,
        secure: true, // use SSL
        auth: {
            user: 'gdtosilicon2016@gmail.com',
            pass: 'cmpe275jb'
        }
    },
    transporter = nodemailer.createTransport(smtpConfig);

function sendmail(email, subject, text) {

    var mailOptions = {
        from: '"gdtosilicon2016@gmail.com', // sender address
        to: email, // list of receivers
        subject: subject, // Subject line
        text: text // plain text body
        //html: '<b>Warning!!!</b>' // html body
    };
    
    transporter.sendMail(mailOptions, (error, info) => {
      if (error) {
          return console.log(error);
      }
      console.log('Message %s sent: %s', info.messageId, info.response);
    });

};


exports.sendmail = sendmail;


function hasJsonObject(key, value, array) {
    var i = 0;
    
    for (i = 0; i < array.length; i = i + 1) {
        if (value === array[i][key]) { return true; }
    }
    
    return false;
}


function mergeTwoJsonArray(array1, array2, key) {
    var i = 0,
        j = 0,
        newArray = [],
        needPush = true,
        json = {};
    
    console.log("array1");
    console.log(array1);
    console.log("array2");
    console.log(array2);
    
    for (j = 0; j < array2.length; j = j + 1) {
        json[key] = array2[j][key];
        newArray.push(json);
        json = {};
    }
    
    for (i = 0; i < array1.length; i = i + 1) {
        needPush = true;
        
        for (j = 0; j < array2.length; j = j + 1) {
            
            if (array1[i][key] === array2[j][key]) {
                needPush = false;
                break;
            }
        }
        
        if (needPush) { 
            json[key] = array1[i][key];
            newArray.push(json);
            json = {};
        }
    }
    
    console.log("newArray");
    console.log(newArray);
    
    return newArray;
}

function addOnline(email) {
    if (hasJsonObject("email", email, onlineJsonArray)) {
        return;
    } else {
        onlineJsonArray.push({email: email});
        console.log("onlineArray");
        console.log(onlineJsonArray);
    }
    
    return;
}

function deleteOnline(email) {
    
    var i = 0;
    console.log("deleteOnline");
    
    for (i = 0; i < onlineJsonArray.length; i = i + 1) {
        if (onlineJsonArray[i].email === email) {
            delete onlineJsonArray[i];
            console.log(onlineJsonArray);
            return;
        }
    }
}

function saveCode(email, code) {
    
    var i = 0;
    
    for (i = 0; i < codeJsonArray.length; i = i + 1) {
        if (email === codeJsonArray[i].email) {
            codeJsonArray[i].code = code;
            return;
        }
    }
    
    codeJsonArray.push({email: email, code: code});
}

function checkCode(email, code) {
    
    var i = 0;
    
    for (i = 0; i < codeJsonArray.length; i = i + 1) {
        if ((email === codeJsonArray[i].email)  && (code === codeJsonArray[i].code)) {
            return true;
        }
    }
    
    return false;
    
}

function deleteCode(email) {
    
    var i = 0;
    console.log("before remove");
    console.log(codeJsonArray);
    
    for (i = 0; i < codeJsonArray.length; i = i + 1) {
        if (email === codeJsonArray[i].email) {
            delete codeJsonArray[i];
            console.log("after remove");
            console.log(codeJsonArray);
        }
    }
    
    
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
                
                saveCode(req.body.email, code);
                
                sendmail(req.body.email, "signUp pls verify fist!", "pls sign up with this code:" + code);

                console.log(codeJsonArray);
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
        screenName = req.body.screenName,
        insert = {};
    
    if ((null === req.session) || (undefined === req.session)) {
        res.send({expired: true});
        return;
    }
    
    console.log(codeJsonArray);
    
    if (checkCode(req.body.email, req.body.code)) {
        
        insert = {email: email, password: passwordMD5, screenName: screenName, friends: [], pending: [], follow: []};
        col.insertOne(insert, function (err, rows) {
            
            if (err) {
                console.log(err);
            } else {
                deleteCode(email);
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

exports.sendPostNotification = function (email) {
    console.log("sendPostNotification");
    var i = 0,
        col = mongoDB.getdb().collection(collectionName);
    
    col.find({notification: "true"}).toArray(function (err, rows) {
        
        if (err) {
            console.log(err);
        } else {
            console.log(rows);
            
            for (i = 0; i < rows.length; i = i + 1) {
                if (!hasJsonObject("email", rows[i].email, onlineJsonArray)) {
                    if (hasJsonObject("email", email, rows[i].follow)) {
                        sendmail(rows[i].email, "New Post", rows[i].screenName + " has created a new post, please sign in to view details.");
                    }
                }
            }
            
        }
        
    });
};




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
                
                addOnline(req.body.email);
                res.send({verified: true});
                
            } else {
                res.send({verified: false});
            }
        }

    });

};

exports.signOut = function (req, res) {
    res.send({msg: "Sign Out Successfully"});
    deleteOnline(req.body.email);
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

function removeEmailFromJsonArray(array, email) {
    var i = 0;
    
    for (i = 0; i < array.length; i = i + 1) {
        if (email === array[i]["email"]) {
            delete array[i];
            return array;
        }
    }
    
    return array
}


exports.getUsers = function (req, res) {
    var col = mongoDB.getdb().collection(collectionName),
        email = req.params.email,
        resArray = [];
    
    col.find({visibility: "public"}).toArray(function (err, publicRows) {
        
        if (err) {
            console.log(err);
        } else {
            console.log(publicRows);
            
            col.findOne({email: email}, function (err, user) {
                console.log(user);
                resArray = mergeTwoJsonArray(publicRows, user.friends, "email");
                col.find({$or: resArray}).toArray(function (err, rows) {
                    console.log(rows);
                    rows = removeEmailFromJsonArray(rows, email);
                    res.send(rows);
                });                           
            });
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
    
    col.findOne({email: recipientEmail}, function (err, userRows) {
        
        if (err) {
            console.log(err);
        } else {
            console.log(userRows);
            if (undefined !== userRows.email) {
                if (hasJsonObject("email", email, userRows.pending)) {
                    res.send({msg: "Add successfully"});
                } else if (hasJsonObject("email", email, userRows.friends)) {
                    res.send({msg: "Friends already"});
                    
                } else {
                    col.updateOne({email: recipientEmail}, {$push: {pending: {email: email}}}, function (err, rows) {
        
                        if (err) {
                            console.log(err);
                        } else {
                            console.log(rows);

                            res.send({msg: "Add successfully"});

                        }
                    });
                }
                
            } else {
                
                res.send({msg: "Email Not Found."});

            }
        }
        
    });
    
    
};

exports.sendInMail = function (email) {
    
    console.log("sendInMail" + email);
    var col = mongoDB.getdb().collection(collectionName);
    col.findOne({$and: [{email: email}, {notification: "true"}]}, function (err, rows) {
        
        if (err) {
            
        } else if (rows) {
            sendmail(email, "New InMail", "You have a new InMail, please sign in to view detail.");
        }
        
    });
    
};

exports.accept = function (req, res) {
    
    var email = req.params.email,
        recipientEmail = req.params.recipientEmail,
        col = mongoDB.getdb().collection(collectionName);
    
    col.updateOne({email: email}, {$push: {friends: {email: recipientEmail}}, $pull: {pending: {email: recipientEmail}}}, function (err, rows) {
        
        if (err) {
            console.log(err);
        } else {
            console.log(rows);
            
            col.updateOne({email: recipientEmail}, {$push: {friends: {email: email}}, $pull: {pending: {email: email}}}, function (err, rows) {
                
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
    
    col.updateOne({email: email}, {$pull: {pending: {email: recipientEmail}}}, function (err, rows) {
        
        if (err) {
            console.log(err);
        } else {
            console.log(rows);
            
            col.updateOne({email: recipientEmail}, {$pull: {pending: {email: email}}}, function (err, rows) {
                
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
                resArray.push({email: email});
                callback(null, resArray);
                /*
                col.find({visibility: "public"}).toArray(function (visibilityErr, visibilityRows) {
        
                    if (visibilityErr) {
                        console.log(visibilityErr);
                    } else {
                        console.log(visibilityRows);
                        resArray = mergeTwoJsonArray(resArray, visibilityRows, "email");
                        
                        callback(null, resArray);
                    }

                });*/
            }
            
            
        }
    });
};




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
                if (hasJsonObject("email", email, rows.friends)) {
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

