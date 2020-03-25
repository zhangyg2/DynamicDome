# DynamicDome

sourcebuild模块（创建一个包含私有变量和set，get方法的javaBean实体）：

AbstractCodeBuilder -- 创建field和method实体的抽象类，FieldCodeBuilder和MethodCodeBuilder的继承于它

FieldCodeBuilder --- 创建一个field的具体实现类

MethodCodeBuilder -- 创建一个set或get方法的具体实现类；

ImportsBuilder -- 根据传入的属性typeMap创建一个javaBean所需的imports依赖

ClassWriterEntiy -- 具体创建javaBean的实现类，它接收AbstractCodeBuilder和ImportsBuilder对象并将其拼装为一个类实体

        

dynamic模块(将ClassWriterEntiy创建的javaBean实体动态编译为class文件，底层依赖javaComper封装，可将配置文件反序列化为java对象和类):


DynamicJavaBean --- 传入一个完整的类名和所需的typeMap(配置文件）调用sourcebuild模块各类创建实体，并将实体写成javaFile，交给javaComper
                     进行编译，最后返回具体的java类和对象。
       
MyClassUtils --- 封装的反射单例类，已每个java对象为key将对象的属性，方法存储到本地缓存的map中，提升反射性能；可通过调用此类完成对DynamicJavaBean
                 动态编译的java对象的操作。
