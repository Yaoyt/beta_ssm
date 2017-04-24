package test;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by yaoyt on 17/4/19.
 *
 * @author yaoyt
 */
public class StreamTest {

    @Test
    public void test1(){
        // 求单词长度之和
        Stream<String> stream = Stream.of("I", "love", "you", "too");
        Integer lengthSum = stream.reduce(0,// 初始值
                (sum, str) -> sum+str.length(), // 累加器
                (a, b) -> a+b); // 部分和拼接器，并行执行时才会用到
        // int lengthSum = stream.mapToInt(str -> str.length()).sum();
        System.out.println(lengthSum);
    }

    @Test
    public void test2(){
        // 将Stream转换成容器或Map
        Stream<String> stream = Stream.of("I", "love", "you", "too");
        List<String> list = stream.collect(Collectors.toList()); // (1)
        Set<String> set = stream.collect(Collectors.toSet()); // (2)
        Map<String, Integer> map = stream.collect(Collectors.toMap(Function.identity(), String::length)); // (3)
    }
}
