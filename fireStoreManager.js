const admin = require('firebase-admin');
const serviceAccount = require('./serviceAccountKey.json');

const callBackHandler = require('./api/routes/imageDetail');

//initialize admin SDK using serciceAcountKey
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
    });
const db = admin.firestore();

module.exports ={
    uploadWrapper: function(url, sizeW, sizeH, sizeB, filename){
            let data = {
                url:url,
                sizeW:sizeW,
                sizeH:sizeH,
                sizeB,sizeB
              };
            let setDoc = db.collection('imageDetails').doc(filename).set(data);
    },

    retreiveWrapper: function(filename, res){
        let ref = db.collection('imageDetails').doc(filename);
        let getDoc = ref.get()
            .then(doc=>{
                if(!doc.exists){
                    callBackHandler.myCallbacks.failedRetreive(res);
                    return;
                }
                else{
                    callBackHandler.myCallbacks.successRetreive(doc.data(), res);
                    return;
                }
            })
            .catch(err => {
                console.log('Error getting document', err);
            });
    }
};

module.exports.db = db;