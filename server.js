const http = require('http');
const port = process.env.PORT;
const app  = require('./app');
const server = http.createServer(app);

server.listen(port, ()=>{
    console.log('Server running on port 3000');
});
