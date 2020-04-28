const express = require('express');
const https = require('https');
const router = express.Router();

const flickr_url = 'https://www.flickr.com/services/feeds/photos_public.gne?tags=';
const flick_opt = '&lan=en-en&format=json&nojsoncallback=1';

router.get('/:tags', (req, res, next) => {
    
    const tags = req.params.tags;
    let url = flickr_url;
    url += tags;
    url += flick_opt;

    console.log("searching with tags: " + tags);

    https.get(url,(response) => {
        let body = "";
        response.on("data", (chunk) => {
            body += chunk;
        });
    
        response.on("end", () => {
            try {
                var json = JSON.parse(body);
                editJSON(json, res);
            } catch (error) {
                console.error(error.message);
            };
        });
    
    }).on("error", (error) => {
        console.error(error.message);
    })

});

function editJSON(raw_json, res){
    var len = raw_json.items.length;
    var photos = Array(len);
    for (var i=0; i < len; i++){
        var title = raw_json.items[i].title;
        var tags = raw_json.items[i].tags;

        var raw_author = raw_json.items[i].author;
        var author = raw_author.slice(raw_author.indexOf('(\"')+2, raw_author.indexOf('\")') );

        var raw_url = raw_json.items[i].media.m;
        var url = raw_url.replace('_m','_b');


        photos[i] = {
            title: title,
            author: author,
            url: url,
            tags: tags
        };
    }
    res.status(200).json({
        photos: photos
    });
}

module.exports.apirout = router;
// const express = require('express');
// const bodyParser = require('body-parser');
// const router  = express.Router();
// const https  = require('https');

// const flickrBaseURL  = 'https://www.flickr.com/services/feeds/photos_public.gne?tags=';
// const flickrSettings = '&tagmode=ALL&lang=en-en&format=json&nojsoncallback=1'; 

// const testURL = 'https://www.flickr.com/services/feeds/photos_public.gne?tags=forest&tagmode=ALL&lang=en-en&format=json&nojsoncallback=1'

// router.post('/', (req,res,next)=>{
//     parsed = req.body.tags.split(",");
//     console.log(req.body.tags);
//     var url = flickrBaseURL;

//     for(var i = 0; i<parsed.length; i++){
//         var tag = parsed[i];
//         if(i+1 < parsed.length){
//             url += tag;
//             url += ','; 
//         }
//         else{
//             url+=tag;
//         }
//         i++;
//     }
//     url+=flickrSettings;
//     getData(url, res);
// });

// function getData(passedURL, postRES){
//     console.log(passedURL);
//     let url = passedURL;
//     https.get(url,(res) => {
//         let body = "";
    
//         res.on("data", (chunk) => {
//             body += chunk;
//         });
    
//         res.on("end", () => {
//             try {
//                 let json = JSON.parse(body);
//                 onDataReady(json, postRES);
//                 return;
//             } catch (error) {
//                 console.error(error.message);
//                 return;
//             };
//         });
    
//     }).on("error", (error) => {
//         console.error(error.message);
//     });
// }
// function onDataReady(myJSON, res){
//     numOfEntry = myJSON.items.length;
//     for(var i=0; i< numOfEntry; i++){
//         let toChangeLink = myJSON.items[i].media.m;
//         let changedLink = toChangeLink.replace('_m','_b');

//         let toChangeAuthor = myJSON.items[i].author
//         let changedAuthor =  toChangeAuthor.slice( toChangeAuthor.indexOf('(\"')+2, toChangeAuthor.indexOf('\")') );

//         myJSON.items[i].author = changedAuthor;
//         myJSON.items[i].media.m = changedLink;
//     }

//     res.status(200).json(myJSON);
// }

// module.exports.apirout = router;