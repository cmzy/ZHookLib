/**
 * 
 */

package com.morgoo.myjavahook;

import com.morgoo.hook.zhook.MethodHook;
import com.morgoo.hook.zhook.MethodHook.Unhook;
import com.morgoo.hook.zhook.ZHook;

import android.os.Handler;
import android.util.Log;

import java.util.Set;

/**
 * @author zhangyong6
 */
public class HookTest {

    private static final String TAG = HookTest.class.getSimpleName();

    /**
     * 
     */
    public HookTest() {
    }

    private int test(int i) {
        Log.e(TAG, "i am in test(int i) re=" + i);
        return i;
    }

    private int test(int i, String t) {
        Log.e(TAG, "i am in test(int i, String t) re=" + i + ",t=" + t);
        return i;
    }

    private String test(String t) {
        Log.e(TAG, "i am in test(String t) t=" + t);
        return t;
    }

    private void test(String t, int i) {
        Log.e(TAG, "i am in test(String t, int i) t=" + t);
    }

    private static void l(Handler handler, String msg) {
        if (msg == null) {
            return;
        }
        if (handler != null) {
            handler.sendMessage(handler.obtainMessage(0, msg));
        }
        Log.e(TAG, msg);
    }

    public static void main(final Handler handler) {
        MethodHook callback = new MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                l(handler, "beforeHookedMethod " + param.method);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object result = param.getResult();
                if (result instanceof String) {
                    param.setResult("zhangyong");
                    l(handler,
                            "afterHookedMethod " + param.method + ",getThrowable="
                                    + param.getThrowable() + " we fake result as zhangyong");
                } else if ((result instanceof Integer)) {
                    param.setResult(1986);
                    l(handler,
                            "afterHookedMethod " + param.method + ",getThrowable="
                                    + param.getThrowable() + " we fake result as 1986");
                } else {
                    l(handler,
                            "afterHookedMethod " + param.method + ",getResultOrThrowable="
                                    + param.getResultOrThrowable());
                }

            }
        };

        Set<Unhook> unhooks1 = ZHook.hookAllConstructors(HookTest.class, callback);
        Set<Unhook> unhooks2 = ZHook.hookAllMethods(HookTest.class, "test", callback);

        HookTest hookTest = new HookTest();
        String msg1 = "i am run on hook: test(1) result=" + hookTest.test(1);
        l(handler, msg1);

        String msg2 = "i am run on hook: test(1,fdfdf) result=" + hookTest.test(1, "fdfdf");
        l(handler, msg2);

        String msg3 = "i am run on hook: test(1,fdfdf) result=" +
                hookTest.test("fdfdf");
        l(handler, msg3);

        hookTest.test("fdfdf", 1);
        String msg4 = "i am run on hook: test(fdfdf,1) result=void";
        l(handler, msg4);

        for (Unhook unhook : unhooks1) {
            unhook.unhook();
        }

        for (Unhook unhook : unhooks2) {
            unhook.unhook();
        }

        l(handler, "i am run after unhook: test1(1) re=" + hookTest.test(1));
        l(handler, "i am run after unhook: test1(1,fdfdf) re=" + hookTest.test(1, "fdfdf"));
        l(handler, "i am run after unhook: test1(1,fdfdf) re=" + hookTest.test("fdfdf"));

    }
}
