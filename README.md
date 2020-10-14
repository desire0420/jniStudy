JNI基本使用，使用JNI实现图片高斯模糊效果

userSoApp  是另一個APP  演示了so的用法
![](https://github.com/wang709693972wei/jnidemo/blob/master/app/src/main/res/mipmap-xhdpi/image.png)


![](https://github.com/wang709693972wei/jnidemo/blob/master/app/src/main/res/mipmap-xhdpi/image1.png)

#动态加载so的文件只能放在两个地方：
1. lib文件夹中，即对应Android Studio中的jniLibs文件夹。
2. 本地data/data/package数据目录下。 

http://blog.sina.com.cn/s/blog_5de73d0b0102xqna.html

https://www.jianshu.com/p/28da77456264