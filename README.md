# Instagram Bot
Schedulable accessibility subscriptions bot for instagram.

  There is a simple example of the implementation **Accessility** library. It tries to open Instagram App by it's packageName, then makes some operations like clicking on search/profile icon, pasting needed nickname into the search field and follows certain amount of users. The only thing is that the scrolling and search click is realized by *SU* command (input ... / swipe ...). The device must be rooted for that but you can implement Accessibility realization with Action.SWIPE and check item IDs at the screen at the moment by the command in your terminal:

```
adb shell am broadcast -a checkNodes
```

  It is implemented with the broadcast receiver registered inside the *App* class. I added *SU* commands to show that you can add functionality like that on the rooted devices or emulator like *Genymotion*, which is rooted by default. The main bot's logic you can find in the *ModeService* class. There is also an API realisation of this logic but not fully implemented. 


![Instagram Bot](https://media.giphy.com/media/gKNNAvE6A5Gi2sANwL/giphy.gif)


  There is also exists another strategy of bot, which allows you to unsubscribe all the following once a *24h*, meanwhile subscribtion bot goes once a *1h*, that is you will subscribe a lot of people during a day, but every *24h* all the following will be unfollowed and you can abuse it to grow your real followers.

