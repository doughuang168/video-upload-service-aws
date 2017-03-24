var aws = require('aws-sdk');
var elastictranscoder = new aws.ElasticTranscoder();

// return basename without extension
function basename(path) {
   return path.split('/').reverse()[0].split('.')[0];
}

// return output file name with timestamp and extension
function outputKey(name, ext) {
   return name + '-' + Date.now().toString() + '.' + ext;
}

exports.handler = function(event, context) {
    console.log('Received event:', JSON.stringify(event, null, 2));
    // Get the object from the event and show its content type
    var key = event.Records[0].s3.object.key;
    var params = {
      Input: { 
        Key: key
      },
      PipelineId: '1440795695386-x512ni', /* test-web-transcoder */
      OutputKeyPrefix: 'transcoder/output/',
      Outputs: [
        {
          Key: outputKey(basename(key),'mp4'),
          PresetId: '1441222625682-nnthmh', // h264
        },
        {
          Key: outputKey(basename(key),'webm'),
          PresetId: '1441222599518-vt9jbu', // webm
        }
      ]
    };

    elastictranscoder.createJob(params, function(err, data) {
      if (err){
        console.log(err, err.stack); // an error occurred
        context.fail();
        return;
      }
      context.succeed();
    });
};