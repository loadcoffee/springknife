package com.spring.springknife;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SpringKnifeProcess {
    public static void bind(final Activity activity) {
        // 绑定控件
        Class activityClass = activity.getClass();
        Field[] fields = activityClass.getDeclaredFields();
        Method[] methods = activityClass.getDeclaredMethods();

        for (final Field field : fields) {
            BindView bindViewAnnotation = field.getAnnotation(BindView.class);
            if (null != bindViewAnnotation) {
                field.setAccessible(true);
                try {
                    if(null != bindViewAnnotation)
                        field.set(activity, activity.findViewById(bindViewAnnotation.value()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }

        for (final Method method : methods) {
            //找到添加了OnClick注解的方法
            BindOnClickView clickMethod = method.getAnnotation(BindOnClickView.class);
            if (null != clickMethod ) {
                    final View view = activity.findViewById(clickMethod.value());
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                method.invoke(activity, view);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            }
        }
    }
}
