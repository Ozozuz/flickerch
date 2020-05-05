const express = require('express');
const bodyParser = require('body-parser');
const router  = express.Router();

//ImageProcessing
var url, filename, imageSize, stats, fileSizeInMb, downloadOption;

//ImageSizeAndInfo
const sizeOf = require('image-size');
const download = require('download-file');
const fs = require('fs');
const filesize = require("filesize"); 

//Firestore & FireCloud
var fireStoreWrapper = require('./../../fireStoreManager');


router.get('/', (req,res,next)=>{
    res.status(405).json({
        message:"Use a POST request!"
    });
});

router.post('/', (req,res,next)=>{
    //looking for the "image:url" field in a json post request
    url = req.body.image;
    filename = url.substr(url.lastIndexOf('/'));
    imageSize;
    stats, fileSizeInMb;
    downloadOption = {
        directory:'./tmp_images',
        filename:filename
    };

    var result = fireStoreWrapper.retreiveWrapper(filename, res);
});


module.exports.rout = router;
module.exports.myCallbacks = {
    successRetreive: function(data,res){
        console.log("Image Found!");
        sendAnswer(res, data.url, data.sizeW, data.sizeH, data.sizeB );
    },

    failedRetreive: function(res){
        console.log("Not Found! Need to download");
        download(url, downloadOption, function(err){
            if(err){
                console.log('Unable to download')
                throw err;
                
            }
            imageSize = sizeOf('./tmp_images/'+filename);
            stats = fs.statSync('./tmp_images/'+filename);
            fileSizeInMb = filesize(stats.size, {round: 0});
            sendAnswer(res,url, imageSize.width, imageSize.height, fileSizeInMb);
            fireStoreWrapper.uploadWrapper(url,imageSize.width, imageSize.height,fileSizeInMb, filename);
            fs.unlinkSync('./tmp_images/'+filename);
        });
    }
};

function sendAnswer(res, url, width, height, fileSizeInMb ){

    res.status(200).json({
        message:"byebye from backend",
        url:url,
        sizeW:width,
        sizeH:height,
        sizeB:fileSizeInMb
    });
}

