# 🧦 UniversalTick

> this gona be hard

A mod to set the tick speed of a server and connected clients.

This _fabric_ mod requires [FabricApi](https://www.curseforge.com/minecraft/mc-mods/fabric-api).
This is ment to be installed on the client and server (thats the universal part of UniversalTick), however it will work just on the server but _not_ just on the client.
On the client it even slows down the audio output.

## 📕 Usage

The following sections are sub commamnds for the `/tick` command.
You only need OP (level 4) to run the set command.

### About

This is a really simple command that just returns `UniversalTick Mod {{VERSION}} by Sigma#8214`.

### Set

This is the main command of this mod, its what lets you change the tickrate of your game.
As said before this command need OP (level 4) perms to be used.
Here is a simple example to set the tick speed of the server and all clients with the mod to 50tps: `/tick set 40` you could also use a percent like this `/tick set 200p`.

To set the tick rate of just the server or just the clients you use the following command: `/tick set 40 (clients | server)`.
Be aware that any de sync in tick speeds between the client and server can make some things weird.

### Get

This command gets the current target and real tickrate.
Its returned in the following format: `"[{{SERVER_TARGET}}, {{CLIENT_TARGET}}] ⌂ SERVER_REAL`.
thats about it.

### Config

Currently, there are only two config settings you can change: `clientMouse` and `clientSound`, bolth of witch are bools.
You can set them with the following command `/tick config {{SETTING}} {{VALUE}}}`.

| Setting     | Default Value | Description                                                       |
| ----------- | ------------- | ----------------------------------------------------------------- |
| clientMouse | true          | Weather the client tick rate will affect mouse sensitivity        |
| clientSound | true          | Weather the client tick rate will affect the sound playback speed |
