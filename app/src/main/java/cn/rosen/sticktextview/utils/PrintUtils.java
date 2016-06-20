package cn.rosen.sticktextview.utils;

//import java.lang.reflect.Array;
	/**
	 * 这是一个io工具类，提供了一些常用的处理文件io函数
	 * @author Rosen
	 * @version 1.5
	 */
public class PrintUtils {

	public static boolean Debug = true;
	public PrintUtils() {
	}
	public static void println(Object obj){
		if(Debug){
			System.out.println(obj == null ? null:obj);
		}
	}
	public static void println(Throwable e){
		if(Debug){
			if(e != null){
				e.printStackTrace();
			}
		}
	}
	
	public static void println(){
		System.out.println();
	}
	
	public static void print(Object obj){
		if(Debug){
			System.out.print(obj == null ? null:obj);
		}
	}
}
