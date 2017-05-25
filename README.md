# CMPE277-TeamProject . 
## Android app of social media . 


APIs:

app.post("/user", user.create);<br>
body: <br>
{ email: "xxx",<br>
  password: "xxxx",<br>
  .....
}  <br><br>
 
return <br>
{dup: true} email has already existed.<br>
{dup: false} <br><br>
 
app.put("/user", user.update);<br>
body:<br>
user's information <br>

return:<br>
latest user's information<br>

app.put("/user/verify", user.verify);<br>
body:<br>
{<br>
    email: "xxx", <br>
    password:"xxxx",<br>
    code: "xxx"<br>
}<br>

return<br>
{verified :true} or <br>
{verified: false}<br>


app.post("/user/signIn", user.signIn);<br>
body: {<br>
    email: "xxx",<br>
    password: "xxx"<br>
    }<br><br>

return: <br>
{ verified: true}<br>
{ verified: false}<br>
    
app.post("/user/signOut", user.signOut);<br>
body :{<br>
 email: "xxxx"<br>
 }<br>
return {}<br>

app.get("/user/:email", user.get);<br>
return user information json<br>

app.get("/users", user.getUsers);<br>
return user information jsonArray<br>

app.get("/users/:email/pending", user.getPendingUsers);<br>
return all pending user information jsonArray<br>

app.put("/user/:email/request/:recipientEmail", user.request);<br>
return {}<br>
app.put("/user/:email/accept/:recipientEmail", user.accept);<br>
return {}<br>

app.put("/user/:email/deny/:recipientEmail", user.deny);<br>

return {}<br>
app.put("/user/:email/follow/:recipientEmail", user.follow);<br>

return {}<br>
app.get("/user/:anotherEmail/from/:email", user.getAnotherUser);<br>
another user information json<br>
<br>
app.post("/inMail", inMail.post); <br>
body: <br>
{
 "fromEmail" : "aaa@aaa.g",
 "fromScreenName": "aaaa",
 "toEmail": "bbb@bbb.g",
 "toScreenName": "bbb",
 "message": "xxxxxxxx"
}

app.get("/inMail/:email", inMail.get); <br>
return inMail jsonArray

app.delete("/inMail/:id", inMail.del);

<br>
app.post("/post", post.create); <br>
app.get("/post/:email/timeline", post.getUserTimeline);
