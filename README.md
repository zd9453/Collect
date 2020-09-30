# Collect
How to\\n
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
#TransformImageView
'''java
 * describe: 变换的image  xml可设置属性
 * <!--变化方式  圆角类型  圆形类型-->
 * attr name="TransformType" format="enum"
 * -- enum name="roundCorner" value="0" 
 * -- enum name="circle" value="1" 
 * 
 * <!--圆角类型的时候设置各圆角的半径大小 !如果设置了allCorner那么会覆盖掉其他单独设置的角-->
 * *****<attr name="allCorner" format="dimension" />
 * *****<attr name="leftTopCorner" format="dimension" />
 * *****<attr name="leftBottomCorner" format="dimension" />
 * *****<attr name="rightTopCorner" format="dimension" />
 * *****<attr name="rightBottomCorner" format="dimension" />
	'''
#ShapeBgTextView
'''java
<declare-styleable name="ShapeBgTextView">
        <!--是否显示我们自己画的背景-->
        <attr name="showBgShape" format="boolean" />
        <!--自己画的背景色-->
        <attr name="shapeColor" format="color|reference" />
        <!--线条模式的线条宽度 -->
        <attr name="shapeBgLineWidth" format="dimension" />
        <!--圆角的大小 dp单位-->
        <attr name="shapeCornerSize" format="dimension" />
        <!--画的背景样式-->
        <attr name="shapeStyle" format="enum">
            <!--周围线条样式-->
            <enum name="line" value="0" />
            <!--内部填充样式-->
            <enum name="fill" value="1" />
        </attr>
    </declare-styleable>
   ''' 
    
