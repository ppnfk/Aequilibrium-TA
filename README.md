## A Transofmer Game implement by using MVVM architecture with LiveData, Retrofit, Navigation components.

* transformer-data cached in local and will fetch new data from server every time entering the game. It will automatically update UI if there is any update through LiveData.

* I cached the allspark for user and I have implemented/used all those 4 rest apis (create/update/get/delete) in the spec, so the game data will be synced with server.

* UI introduction - Main Screen
    * right/bottom + button is for creating a new transformer
    * left/bottom construction button is for "go for war"
    * touch the ViewCard will enter Edit Screen which you can edit this transformer.

* UI introduction - Edit Screen
    * there are Name (text edit), Team (radio button) and 8 transformer attributes (slide bar) could be edited in this screen.
    * in the button there are "Save" button and "Delete" button. (Delete button will only be visible when editing a existing transformer)

* About  "go to war" button:
    * the battle result will be shown in a popup dialog. 
    * currently I DID NOT DELETE those transformers who are destroyed in the battle, because it is not easy for you to check the result is correct or not if deleting those destroyed transformers. (but I comment out deleteTransformer() code in it)

* I also install detekt for checking code quality, but has no time to fix all those analyze issues. run `./gradlew detekt`
* I also install dokka for documentation, run `./gradlew dokka`
