package application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class BaiduApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // ��ʹ�� SDK �����֮ǰ��ʼ�� context ��Ϣ������ ApplicationContext
        SDKInitializer.initialize(this);
    }

}