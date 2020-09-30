# Collect
How to <br/>
To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

gradle
maven
sbt
leiningen
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.zd9453:Collect:1.0.0'
	}
<br>
# TransformImageView <br/>
(```)
 <declare-styleable name="TransformImageView">
         <attr name="TransformType" format="enum">
             <enum name="roundCorner" value="0" />
             <enum name="circle" value="1" />
         </attr>
         <attr name="allCorner" format="dimension" />
         <attr name="leftTopCorner" format="dimension" />
         <attr name="leftBottomCorner" format="dimension" />
         <attr name="rightTopCorner" format="dimension" />
         <attr name="rightBottomCorner" format="dimension" />
     </declare-styleable>
(```)
# ShapeBgTextView <br/>
```
<declare-styleable name="ShapeBgTextView">

        <attr name="showBgShape" format="boolean" />

        <attr name="shapeColor" format="color|reference" />

        <attr name="shapeBgLineWidth" format="dimension" />

        <attr name="shapeCornerSize" format="dimension" />

        <attr name="shapeStyle" format="enum">

            <enum name="line" value="0" />

            <enum name="fill" value="1" />
        </attr>
    </declare-styleable>
```
    
