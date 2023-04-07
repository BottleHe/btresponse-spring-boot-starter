### v1.0.6
* 删除原有的ServerException 定义
* 替换成GlobalException
* 添加一系列的GlobalException 子异常.

### v1.1.0
* 将ServerException 更新为 GlobalException.
* GlobalException 派生自Exception, 使用时需要throws关键字.
* 移除子异常名中的HTTP限定, 定义上去除和http的关联.
* 派生异常有client类型和server类型, 可以理解成它对应4xx错误和5xx错误. 

### v1.1.1
* 添加对GlobalException的派生异常, 自定义message的构造方式.

### v1.1.2
* 添加一个集中的MAP存储空间, 存储默认的异常
* 更改默认异常的存储方式, 不再存储在原对象中, 而是存放在GlobalError实例中, 使用Map映射存储.
* 添加异常的构造函数, 支持定义附加对象和Throwable cause.
* 给GlobalError添加实例方法, 支持通过code去获取一个默认异常


### v1.1.3
* 更新集中存储变更为 类 表, 可以使用code去获取这个类.
* 还原子异常中的Default的定义方式, 还是使用自己new的对象. 