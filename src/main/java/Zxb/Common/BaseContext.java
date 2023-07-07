package Zxb.Common;


/**
 * 基于Threadlocal封装工具类，用于保存和获取当前登录用户的id
 * 原理：用户的每一次请求会经过多个过滤器，在本项目中，一个更新操作需要三个过滤器，这一个过程
 * 属于一个线程LoginCheckFilter、EmployeeController、MyMetaObjectHandler
 * 为了让MyMetaObjectHandler里面的方法能够拿到当前登录用户的id信息，我们就可以通过线程的方式
 * 在LoginCheckFilter这里设置值，这样MyMetaObjectHandler就可以通过线程获取到信息
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
