# Selector Switches  

<img src="https://github.com/adk96r/Selector-Switch-UI-Component/blob/master/Switch%20PNGs/Artboard%205.png" height="80" style="display:inline;">

This UI component, built for _android 5.0+_, is inspired from the <a href="http://goaheadtakethewheel.com/wp-content/uploads/2016/10/20161018_144815.jpg">switches found in the Active Dynamics Panel</a>
of the McLaren road cars. It allows the user to quickly toggle through different modes, using a much smaller screen footprint than the conventional buttons. This article is divided into:

1. [How does it look like ?](#styles)
2. [Looks awesome! How do I use it ?](#usage)
3. [Whoa! I wanna know how it's built ?](#structure)
4. [What else should I know?](#know_more)
5. [How do I contribute my own designs ?!](#build)

<br>
Let's get started!

<br>
<br>

# <a name="styles">The Styles</a>

<br>
The switch can have three styles each one of which offers a different look and usability. Right now the mini and the max are under development but the <b>Normal</b> one works perfectly!
<br>
<br>
<br>
  
|NORMAL| MAX | MINI |
|------|-----|------|
|<img src="https://github.com/adk96r/Selector-Switch-UI-Component/blob/master/Switch%20PNGs/Artboard%201.png">|<img src="https://github.com/adk96r/Selector-Switch-UI-Component/blob/master/Switch%20PNGs/Artboard%203.png">|<img src="https://github.com/adk96r/Selector-Switch-UI-Component/blob/master/Switch%20PNGs/Artboard%202.png">|
|Optimized for 5 modes or below.|Suitable for 6 or more modes.|Suitable for a refined look.|
|The dial is continuous and the modes are static.|The dial is non-continuous and the modes are recycled, similar to a recycler view, when the dial is revolved.|Also a recycled dial, but more layout friendly, as the switch can be stacked up against the border of the screen.|  
  
<br>
<br>

# <a name="usage">Add a selector switch in your app!</a>

<br>
This is how you bring this goodness into your project!

### 1. Download the code

First clone the repo or download the .AAR file and then follow [these steps](https://developer.android.com/studio/projects/android-library.html#AddDependency) to integrate them into your project.
<br>
<br>

### 2. Declare the modes and their colors

Every switch needs a set of modes and each one of these modes has its own color on the dial. These modes and colors are stored in the app
in form of array resources. Thus, we need to create two array resources as shown -

``` xml
  <resources>
      <!-- The names of the modes -->
      <array name="modes">
          <item>Low</item>
          <item>Medium</item>
          <item>High</item>
      </array>
      
      <!-- The colors for the modes -->
      <array>
          <item>@color/lowColor</item>
          <item>@color/midColor</item>
          <item>@color/highColor</item>
      </array>
  </resources>
```
__Note__ : The number of modes and colors should be same. If not, the app will crash after throwing an __IllegalSelectorException__.
<br>
<br>

### 3. Add the switch and _link_ the colors and modes

Now let's add a switch to the layout and *link the modes and colors* ,that we created earlier, to the switch.

``` xml
  <SelectorSwitch
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        colors="@array/colors"
        modes="@array/modes" />
```
<br>

# <a name="structure">Structure of the switch</a>

The structure will be added soon.
<br>
<br>

# <a name="know_more">A few things about the inner workings</a>

This section will be added soon.
<br>
<br>

# <a name="build">Contribute!</a>

This details will be added soon.
<br>
<br>

I hope you like this and find it useful for your projects. Cheers!
<br>
<br>
 ADK<br>
Follow me <a href="https://twitter.com/adk96r">here</a>.<br>
I post awesome stuff <a href="https://www.instagram.com/adk96r/">here</a>.<br>
