# Java-ExcelPOIUtils
读取写入Excel文件的jar，直接调用方法，写几个必要的参数即可~

1.将Excel中数据保存到数据库中：
	// 第一步：提供Excel表名：user.xls，和程序中的数据结构得到readerXLS对象
	SASAReaderXLS readerXLS = new SASAReaderXLS("user.xls", "com.example.User");
	// 第二步：通过readerXLS对象的方法得到Excel内容的数据源
	List<User> usersList = readerXLS.getExcelContentObject();
	// 第三步：得到相关数据库支持
	SASADatabase database = new SASADatabase();
	// 第四步：调用数据库支持方法，提供数据库中表名：user，和上面得到的数据源：usersList即可将数据插入到数据库中
	database.insertObjects("user", usersList);

2。将数据库中数据导出到Excel中：
	// 第一步：得到database支持对象
	SASADatabase database = new SASADatabase();
	// 第二步：从数据库中得到数据源，只需要提供sql，和数据模型
	List list = database.formatList(sql, null, "com.example.User");
	// 第三步：提供目标文件名，特殊处理上面得到的数据源，数目不定的属性
	SASAWriteXLS.writeXLSByFileName("user.xls", SASAWriteXLS.getDataList(list), "name", "age", "des", ...);

注意事项：
	数据模型必须和数据库中属性名称一致，都是小写的；
	数据模型中属性类型必须是String；
	数据库事先创建好对应表。
