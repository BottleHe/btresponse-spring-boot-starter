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
