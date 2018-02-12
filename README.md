# Fluent view resizing for Android

##Animation

```java
ViewParams.of(findViewById(R.id.container))
                .animate()
                .widthBy(value)
                .heightBy(value)
                .repeatInfinitely()
                .repeatModeReverse()
                .setDuration(DURATION_MS)
                .start();
```

![](https://github.com/AlexanderKozubets/android-view-resizing-utils/preview/animated_resize_sample.gif)