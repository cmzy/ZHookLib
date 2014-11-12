ZHookLib
========

A java hook library  for android, it contains a so and a jar file.

And the part of the code copy from [xposed](https://github.com/rovo89/Xposed)

It is compatible with android 2.3-4.4.

The art mode support will come soon!


API:

`com.morgoo.hook.zhook.ZHook` class:

Hook method:
        
	public static MethodHook.Unhook hookMethod(Member hookMethod, MethodHook callback)
        
    public static Set<MethodHook.Unhook> hookAllMethods(Class<?> hookClass,
			String methodName, MethodHook callback)
			
    public static MethodHook.Unhook findAndHookMethod(Class<?> clazz,
			String methodName, Object... parameterTypesAndCallback)
			
	public static MethodHook.Unhook findAndHookMethod(String className,
			ClassLoader classLoader, String methodName,
			Object... parameterTypesAndCallback)
        
Hook constructor:

    public static Set<MethodHook.Unhook> hookAllConstructors(
			Class<?> hookClass, MethodHook callback)           

Check method has been or not hooked:
    
    public static boolean isMethodHooked(Member method)

unhook:

	public static void unhookMethod(Member hookMethod, MethodHook callback)

invoke original method:

	public static Object invokeOriginalMethod(Member method, Object thisObject,
			Object[] args)

sample:

Define a class in `HookTest.java`:
    
    public class HookTest{
        
        private static final String TAG = HookTest.class.getSimpleName();
        
        public HookTest() {
            Log.e(TAG,"Create");
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
    }
  
 
and write a main method for test:


     public static void main() {
        MethodHook callback = new MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.e("HookTest", "beforeHookedMethod " + param.method);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object result = param.getResult();
                if (result instanceof String) {
                    param.setResult("MyResult");
                    Log.e("HookTest",
                            "afterHookedMethod " + param.method + ",getThrowable="
                                    + param.getThrowable() + " we fake result as MyResult");
                } else if ((result instanceof Integer)) {
                    param.setResult(1986);
                    Log.e("HookTest",
                            "afterHookedMethod " + param.method + ",getThrowable="
                                    + param.getThrowable() + " we fake result as 1986");
                } else {
                    Log.e("HookTest",
                            "afterHookedMethod " + param.method + ",getResultOrThrowable="
                                    + param.getResultOrThrowable());
                }

            }
        };
        
        
        Set<Unhook> unhooks1 = ZHook.hookAllConstructors(HookTest.class, callback);
        Set<Unhook> unhooks2 = ZHook.hookAllMethods(HookTest.class, "test", callback);

        HookTest hookTest = new HookTest();
        String msg1 = "i am run on hook: test(1) result=" + hookTest.test(1);
        Log.e("HookTest", msg1);

        String msg2 = "i am run on hook: test(1,fdfdf) result=" + hookTest.test(1, "fdfdf");
        Log.e("HookTest", msg2);

        String msg3 = "i am run on hook: test(1,fdfdf) result=" +
                hookTest.test("fdfdf");
        Log.e("HookTest", msg3);

        hookTest.test("fdfdf", 1);
        String msg4 = "i am run on hook: test(fdfdf,1) result=void";
        Log.e("HookTest", msg4);

        for (Unhook unhook : unhooks1) {
            unhook.unhook();
        }

        for (Unhook unhook : unhooks2) {
            unhook.unhook();
        }

        Log.e("HookTest", "i am run after unhook: test1(1) re=" + hookTest.test(1));
        Log.e("HookTest", "i am run after unhook: test1(1,fdfdf) re=" + hookTest.test(1, "fdfdf"));
        Log.e("HookTest", "i am run after unhook: test1(1,fdfdf) re=" + hookTest.test("fdfdf"));

    }

