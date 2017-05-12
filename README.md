# CMPE277-TeamProject . 
## Android app of social media . 


APIs:

app.post("/user", user.create);
body: 
{ email: "xxx",
  password: "xxxx",
  .....
}
 
return 
{dup: true} email has already existed.
{dup: false}
 
app.put("/user", user.update);
body:
user's information

return:
latest user's information

app.put("/user/verify", user.verify);
body:
{
    email: "xxx",
    password:"xxxx",
    code: "xxx"
}

return
{verified :true} or 
{verified: false}


app.post("/user/signIn", user.signIn);
body: {
    email: "xxx",
    password: "xxx"
    }

return: 
{ verified: true}
{ verified: false}
    
app.post("/user/signOut", user.signOut);
body :{
 email: "xxxx"
 }
return {}

app.get("/user/:email", user.get);
return user information json

app.get("/users", user.getUsers);
return user information jsonArray

app.get("/users/:email/pending", user.getPendingUsers);
return all pending user information jsonArray

app.put("/user/:email/request/:recipientEmail", user.request);
return {}
app.put("/user/:email/accept/:recipientEmail", user.accept);
return {}

app.put("/user/:email/deny/:recipientEmail", user.deny);

return {}
app.put("/user/:email/follow/:recipientEmail", user.follow);

return {}
app.get("/user/:anotherEmail/from/:email", user.getAnotherUser);
another user information json
