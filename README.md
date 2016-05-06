# Android Guitar Tuner

![graphic](http://3.bp.blogspot.com/-1yqslV1A61Q/VrIhzdg3szI/AAAAAAAAAPQ/TBwiiGi-K_c/s1600/guitar-tuner-featured-graphic-b.png)

An open source Guitar Tuner library and application code for Android. Design heavily influenced from [Google Chrome's Guitar Tuner](https://github.com/GoogleChrome/guitar-tuner). You can see the application in the Google Play Store:

<a href="https://play.google.com/store/apps/details?id=com.chrynan.guitartuner&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-AC-global-none-all-co-pr-py-PartBadges-Oct1515-1"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge.png" /></a>

If you would like to use the project as a library, clone the project and use the desired classes. You can create an instance of a Tuner object and call it's start method when you're ready to begin listening for pitch, call it's stop method when you are finished, and remember to call it's release method when you no longer need the object. Ex:

```Java
Tuner tuner = new Tuner(new TunerUpdate(){
    @Override
    public void updateNote(Note newNote, PitchDetectionResult result){
        //TODO add your logic, such as View updating, here
    }
});
//To start listening for pitch
tuner.start();
//To stop listening for pitch
tuner.stop();
//When we are done with the tuner object
tuner.release();
```

Feel free to use and alter the project in accordance with the license. Also, contributions are welcome.
