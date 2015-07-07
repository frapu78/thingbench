# thingbench
The IoT Thing Workbench

Tired of hooking up an Arduino to test another IoT framework? Or just inside a VM without actual access to hardware? Don't worry anymore, Thingbench helps you get through it. 

ThingBench is an interactive, visual device workbench, where you can have multiple boards with different virtual devices attached. As of now, we ship with one virtual device---a lamp! You can switch it on and off, change the color and size or position multiple lamps on the board. (If you really need something else, this is all open source---feel free to fork and send a pull request!)

Here's how to browse your things:

```http
GET http://localhost:9099/things
```

This gives you the list of current boards (shown as tabs in the workbench) and virtual devices attached to it (demo file used here):

```xml
<things>
  <board name="Demo" id="1692623602">
    <thing name="Lamp 1" id="1253071236" link="http://127.0.0.1:9099/things/1692623602/1253071236"/>
    <thing name="" id="1550999049" link="http://127.0.0.1:9099/things/1692623602/1550999049"/>
    <thing name="" id="1906187103" link="http://127.0.0.1:9099/things/1692623602/1906187103"/>
    <thing name="" id="1901887672" link="http://127.0.0.1:9099/things/1692623602/1901887672"/>
  </board>
</things>
```

To browse the properties of a thing go to one of the shown URLs (here Lamp1):

```http
GET http://127.0.0.1:9099/things/1692623602/1253071236
```

This gives you the current state of the devices:
```xml
<thing>
  <property name="shadow" value="0"/>
  <property name="color_background" value="-3355648"/>
  <property name="x" value="347"/>
  <property name="width" value="100"/>
  <property name="y" value="135"/>
  <property name="text" value="Lamp 1"/>
  <property name="stereotype" value=""/>
  <property name="power" value="OFF"/>
  <property name="#id" value="1253071236"/>
  <property name="#type" value="thingbench.ThingsModel.Lamp"/>
  <property name="height" value="140"/>
</thing>
```
You can easily PUT one or all properties to set new values (like switching the lamp on):
```http
http://127.0.0.1:9099/things/1692623602/1253071236
```
with the body
```xml
<thing>  
  <property name="power" value="ON"/>
</thing>
```

You can easily integrate the corresponding HTTP calls in any IoT framework you like. If you need anything else then a simple lamp, please feel free to browse the code and add your own virtual devices. The Thingbench is based on Processeditor (http://frapu.de/code/processeditor/index.html), which makes it easy to create your own devices with a visual representation.

## Java Libraries
* Processeditor incl. depending libraries, see http://github.com/frapu78/processeditor

## Manual Building (Idea, Netbeans, etc.)
* Create a new Java 1.7 source project with the required libs as dependencies
* Add the "src" folder as source
* Add the "models", "pics" folder as resources (Idea) or source (Netbeans)
* Select "thingbench.Thingbench" as main class for Thingbench

Start interacting with the Things!
