package com;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

/**
 * @author wangwei
 * @date 2020/10/13.
 */
public class LoadSoHelper {
    private static final String TARGET_LIBS_NAME = "libs";

    private static volatile LoadSoHelper instance;

    private WeakReference<Context> weakReference;

    private LoadSoHelper(Context context) {
        weakReference = new WeakReference<>(context);

    }

    public static LoadSoHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (LoadSoHelper.class) {
                if (instance == null) {
                    instance = new LoadSoHelper(context);
                }
            }
        }
        return instance;
    }

    /**
     * 加载so文件
     */
    public void loadSo(LoadListener loadListener) {
        File dir = weakReference.get().getDir(TARGET_LIBS_NAME, Context.MODE_PRIVATE);
        File[] currentFiles;
        currentFiles = dir.listFiles();
        for (int i = 0; i < currentFiles.length; i++) {
            System.load(currentFiles[i].getAbsolutePath());
        }
        loadListener.finish();

    }

    /**
     * @param fromFile 指定的本地目录
     * @param isCover  true覆盖原文件即删除原有文件后拷贝新的文件进来
     * @return
     */
    public void copySo(String fromFile, boolean isCover, CopyListener copyListener) {
        //要复制的文件目录
        File[] currentFiles;
        File root = new File(fromFile);
        //如同判断SD卡是否存在或者文件是否存在,如果不存在则 return出去
        if (!root.exists()) {
            return;
        }
        //如果存在则获取当前目录下的全部文件并且填充数组
        currentFiles = root.listFiles();

        //目标目录（拷贝的私有目录）
        File targetDir = weakReference.get().getDir(TARGET_LIBS_NAME, Context.MODE_PRIVATE);
        //创建目录
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        } else {
            //删除全部老文件
            if (isCover) {
                for (File file : targetDir.listFiles()) {
                    file.delete();
                }
            }

        }
        //遍历要复制该目录下的全部文件¬
        for (int i = 0; i < currentFiles.length; i++) {

            if (currentFiles[i].getName().contains(".so")) {
                copySdcardFile(currentFiles[i].getPath(), targetDir.toString() + File.separator + currentFiles[i].getName());
            }
        }
        copyListener.finish();
    }


    /**
     * 文件拷贝(要复制的目录下的所有非文件夹的文件拷贝)
     *
     * @param fromFile
     * @param toFile
     * @return
     */
    private static void copySdcardFile(String fromFile, String toFile) {
        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = fosfrom.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            // 从内存到写入到具体文件
            fosto.write(baos.toByteArray());
            // 关闭文件流
            baos.close();
            fosto.close();
            fosfrom.close();
        } catch (Exception ex) {
            return;
        }
    }

    /**
     * copy完成后回调接口
     */
    public interface CopyListener {
        //其实方法返回boolean也成
        void finish();
    }

    /**
     * load完成后回调接口
     */
    public interface LoadListener {
        //其实方法返回boolean也成
        void finish();
    }

}