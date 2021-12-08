# TwipR 3.0.1
## My First Minecraft Plugin
Test on 1.16.5

This plugin makes minecraft server to listen Twip slotmachine result.  
Before use this, you must create "streamer.json" in "plugins/TwipR/" until I update. sorry

So, file will looks like this.
```json
{
  "streamers":{
  },
  "preset":{
  }
}
```

You can add streamers with minecraft command or edit file, but preset can't edit with command  
With this file, one of the slotmachine's item name will be "5 cooked beef" and "2 diamonds".  
And streamers can be more than one, preset too.
```json
{
  "streamers":{
    "minecraft_user_name":{
      "alertbox_token":"",
      "slotmachine":"",
      "alertbox_key":"",
      "connect":false
    }
  },
  "preset":{
    "default":{
      "5 cooked beef":{
        "commands":[
          "give %minecraft_name% minecraft:cooked_beef 5"
        ]
      },
      "2 diamonds":{
        "commands":[
          "give %minecraft_name% minecraft:diamond 2",
          "title %minecraft_name% title {\"text\":\"2 Diamonds!\"}"
        ]
      }
    }
  }
}
```

In commands you can use these
```
%minecraft_name%    -> minecraft_name
%comment%           -> comment
%sender%            -> sender
%amount%            -> amount
%slot_item_name%    -> itemName
```



In Minecraft, this command will help you
```
/twipr help
```


##Version History

v3.0.1  
add command /twipr test [true|false]  
execute slotmachine test result or not  

v3.0  
First Release