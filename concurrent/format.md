### SimpleDateFormat 线程不安全

#### 原因概述
- 时间格式化不对  
```$xslt
java.text.DateFormat.calendar 属性为共有变量

format方法
java.text.CalendarBuilder.establish 方法调用 先 清除日期cal.clear(); 再cal.setWeekDate(field[MAX_FIELD + WEEK_YEAR], weekOfYear, dayOfWeek);
这样可能会有两个问题
1、时间被后面覆盖，不对
2、时间不存在，报错

parse方法
parse方法也有时间不对的问题
还有一个就是

java.text.DigitList.getStringBuffer 这个方法调用共有属性tempBuffer 会被修改，导致

```
