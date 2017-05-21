/*jslint node: true */
'use strict';
var express = require('express'),
    path = require('path'),
    favicon = require('serve-favicon'),
    logger = require('morgan'),
    cookieParser = require('cookie-parser'),
    bodyParser = require('body-parser'),
    session = require('express-session'),
    index = require('./routes/index'),
    users = require('./routes/users'),
    user = require('./routes/user'),
    http = require('http'),
    post = require('./routes/post'),
    app = express();

// view engine setup
app.set('port', process.env.PORT || 3000);
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', index);
app.use('/users', users);



app.use(session({
    secret: 'cmpe277',
    resave: false,
    saveUninitialized: true,
    cookie: { secure: true }
}));

app.post("/user", user.create);
app.put("/user", user.update);
app.put("/user/verify", user.verify);
app.post("/user/signIn", user.signIn);
app.post("/user/signOut", user.signOut);
//app.get("/user/isVerified/:email", user.isVerified);
app.get("/user/:email", user.get);
app.get("/users", user.getUsers);
app.get("/users/:email/pending", user.getPendingUsers);
app.put("/user/:email/request/:recipientEmail", user.request);
app.put("/user/:email/accept/:recipientEmail", user.accept);
app.put("/user/:email/deny/:recipientEmail", user.deny);
app.put("/user/:email/follow/:recipientEmail", user.follow);
app.get("/user/:anotherEmai/from/:email", user.getAnotherUser);
//app.get("/session", user.session);

app.post("/post", post.create);
app.get("/post/:email/timeline", post.getUserTimeline);

// catch 404 and forward to error handler
app.use(function (req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});

// error handler
app.use(function (err, req, res, next) {
  // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
    res.status(err.status || 500);
    res.render('error');
});

module.exports = app;

http.createServer(app).listen(app.get('port'), function () {
    console.log('Express server listening on port ' + app.get('port'));
});
