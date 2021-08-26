# JarExplorer
This simple console app decompiles jar file and searches for needed word in decompiled files. It's helpful when you need to find usages of some variables or simply find place where log was called.

This app uses built [Fernflower (Java Decompiler)](https://github.com/JetBrains/intellij-community/tree/master/plugins/java-decompiler/engine) jar file. You can update or downgrade it simply replacing _src/libs/fernflower.jar_ file.

## Building
Using console in root directory of project call:

```gradlew installDist```

Then in _build/install_ folder you can find prepared project. 

## Usage
Add {built project}/bin library to PATH variable. 
Open console in folder where jar file you want to decompile placed and write in console:

```JarExplorer example.jar ClassName```

```JarExplorer example.jar configId```

Result will be like that:

```
    (AdUnit.java:73)
    (BannerAdUnit.java:9)
    (BannerBaseAdUnit.java:11)
    (InterstitialAdUnit.java:16)
    (NativeAdUnit.java:8)
    (NativeAdUnit.java:9)
    (RequestParams.java:39)
    (RequestParams.java:57)
```