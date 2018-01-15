package com.alibaba.android.arouter.core;

import android.content.Context;
import android.util.LruCache;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.AutowiredService;
import com.alibaba.android.arouter.facade.template.ISyringe;

import java.util.ArrayList;
import java.util.List;

import static com.alibaba.android.arouter.utils.Consts.SUFFIX_AUTOWIRED;

/**
 * Autowired service impl.
 *
 * @author zhilong <a href="mailto:zhilong.lzl@alibaba-inc.com">Contact me.</a>
 * @version 1.0
 * @since 2017/2/28 下午6:08
 */
@Route(path = "/arouter/service/autowired")
public class AutowiredServiceImpl implements AutowiredService {
    private LruCache<String, ISyringe> classCache;
    private List<String> blackList;

    @Override
    public void init(Context context) {
        classCache = new LruCache<>(66);
        blackList = new ArrayList<>();
    }

    @Override
    public void autowire(Object instance) {
        String className = null;

        Class clz = instance.getClass();
        //遍历所有的超类，在while中捕获异常，会带来一些性能开销，但是这样可以确保超类的属性可以被注入
        while (clz != null) {
            try {
                className = clz.getName();
                //放在前面是为了避免异常导致这句代码执行不到
                clz = clz.getSuperclass();
                if (!blackList.contains(className)) {
                    ISyringe autoWiredHelper = classCache.get(className);
                    if (autoWiredHelper == null) {
                        autoWiredHelper = (ISyringe) Class.forName(className + SUFFIX_AUTOWIRED).getConstructor().newInstance();
                    }
                    autoWiredHelper.inject(instance);
                    classCache.put(className, autoWiredHelper);
                }
            } catch (Exception ex) {
                if (className != null)
                    blackList.add(className);    // This instance need not autowired.
            }
        }

    }
}
