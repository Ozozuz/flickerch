const express = require('express');
const bodyParser = require('body-parser')
const admin = require('firebase-admin');
const serviceAccount = require('../../serviceAccountKey.json');
const fireStoreMg    = require('../../fireStoreManager');
const router = express.Router();

// admin.initializeApp({
//     credential: admin.credential.cert(serviceAccount)
//     });
// const db = admin.firestore();
const db = fireStoreMg.db;

router.get('/:id', (req, res, next) => {

	console.log("handling a get request on database...")

    var photos = Array();

    var uid = req.params.id;

    var docs = db.collection(uid).get()
    .then(docs => {
        docs.forEach(doc => {
            var title = doc.data().title;
            var author = doc.data().author;
            var url = doc.data().url;
            var tags = doc.data().tags;

            photos.push({
                title: title,
                author: author,
                url: url,
                tags: tags
            });
        });

        res.status(200).json({
        	photos: photos
        });
    })
    .catch(err => {
        console.log('Error getting document', err);
    });

});


router.post('/:id', (req, res, next) => {

	console.log("Storing image...")

    var uid = req.params.id;

    var title = req.body.title;
    var author = req.body.author;
    var url = req.body.url;
    var tags = req.body.tags;

    filename = url.substr(url.lastIndexOf('/'));

    var data = {
        title: title,
        author: author,
        url: url,
        tags: tags
      };
      
    var setDoc = db.collection(uid).doc(filename).set(data);

    console.log("Stored " + filename);

    res.status(200).json({
        message: 'Database updated!'
    })

});


router.delete('/:id/:filename', (req, res, next) => {

    var uid = req.params.id;
    var filename = req.params.filename
    console.log(uid);
    console.log(filename);

    var deleteDoc = db.collection(uid).doc(filename).delete();

    res.status(200).json({
        message: "Image deleted"
    })

})




module.exports.rout = router;
