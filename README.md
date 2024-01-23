jetpack-MVVM-Best-Practice的Kotlin实现。  
参考项目地址：https://github.com/KunMinX/Jetpack-MVVM-Best-Practice

1.项目采用Kotlin改写（和Jetpack无关的一些基类，仍采用原有项目java写法）。
2.修复了原有项目通知栏无法显示播放ui的问题。
3.gradle编译脚本迁移到kts。

todo list：
1.响应式编程publish event通信模型由原项目MutableResult切换为官方kotlin flow。
2.异步通信框架由rxjava切换为kotlin协程。

关于此项目：
编写此项目的初衷是由于本人有学习jetpack的需求，而原项目作为一个小体量项目，比官方的参考要多一些，有助于自己理解整个mvvm框架的设计思想，在这里再简单总结下：  
1. Lifecycle。解决不同组件间生命周期一致性问题，避免为监听状态注入Activity导致的内存泄露问题。
2. 
