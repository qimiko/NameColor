# NameColor Plugin
Uses Minecraft 1.16's ability to use custom colors to add those colors to usernames in chat.

## Usage
1. Add plugin to `plugins` folder.

### Commands

* `/namecolor get` - Gets the current color of your name
* `/namecolor set <color>` - Takes a color of format `#xxxxxx` and sets it as your chat color
* `/namecolor reset` - Deletes the currently set color value

## Configuration
* `default-color` (`#FFFFFF`) - color to use on players by default
* `format` `<{nc:player}> {nc:msg}` - text to format chat with