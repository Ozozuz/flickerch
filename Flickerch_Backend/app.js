const express = require('express');
const app = express();
const imageDetailRoute = require('./api/routes/imageDetail');
const flickrApiRoute  = require('./api/routes/flickrApi');
const databaseRoute   = require('./api/routes/database');
const bodyParser = require('body-parser')


//Middleware, any incoming request has to go trought app.use()
// app.use((req,res,next) =>{
//     res.status(200).json({
//         message:"Answer from my backend!"
//     });
// });

app.use(bodyParser.json());
app.use('/imagedetail', imageDetailRoute.rout);
app.use('/search', flickrApiRoute.apirout);
app.use('/database', databaseRoute.rout);

module.exports = app;