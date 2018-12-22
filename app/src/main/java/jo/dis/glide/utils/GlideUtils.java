package jo.dis.glide.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Glide工具类（glide 4.x）
 */

public class GlideUtils {

    public static final String TAG = GlideUtils.class.getSimpleName();

    /**
     * 加载图片(默认)
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImage(Context context, String url, @DrawableRes int placeholder, @DrawableRes int error, ImageView imageView) {
        loadImage(context, url, placeholder, error, imageView, null);
    }

    /**
     * 加载图片(带事件)
     * @param context
     * @param url
     * @param placeholder
     * @param error
     * @param imageView
     * @param listener
     */
    public static void loadImage(Context context, String url, @DrawableRes int placeholder, @DrawableRes int error, ImageView imageView, RequestListener<Drawable> listener) {
        if (context instanceof Activity && !isActivityEnabled(context)) {
            return;
        }
        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(placeholder) //占位图
                    .error(error)       //错误图
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context)
                    .load(url)
                    .apply(options)
                    .listener(listener)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 目标加载-用于adapter资源复用情况
     * @param context
     * @param url
     * @param placeholder
     * @param error
     * @param imageView
     */
    public static void loadImageIntoTarget(Context context, String url, @DrawableRes int placeholder, @DrawableRes int error, ImageView imageView) {
        if (context instanceof Activity && !isActivityEnabled(context)) {
            return;
        }
        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(placeholder) //占位图
                    .error(error)       //错误图
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context)
                    .load(url)
                    .apply(options)
                    .into(new DrawableImageViewTarget(imageView));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定图片大小;使用override()方法指定了一个图片的尺寸。
     * Glide现在只会将图片加载成width*height像素的尺寸，而不会管你的ImageView的大小是多少了。
     * 如果你想加载一张图片的原始尺寸的话，可以使用Target.SIZE_ORIGINAL关键字----override(Target.SIZE_ORIGINAL)
     *
     * @param context
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public static void loadImageSize(Context context, String url, @DrawableRes int placeholder, @DrawableRes int error, ImageView imageView, int width, int height) {
        if (context instanceof Activity && !isActivityEnabled(context)) {
            return;
        }
        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(placeholder) //占位图
                    .error(error)       //错误图
                    .override(width, height)
                    // .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context).load(url).apply(options).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 禁用内存缓存功能
     * diskCacheStrategy()方法基本上就是Glide硬盘缓存功能的一切，它可以接收五种参数：
     * <p>
     * DiskCacheStrategy.NONE： 表示不缓存任何内容。
     * DiskCacheStrategy.DATA： 表示只缓存原始图片。
     * DiskCacheStrategy.RESOURCE： 表示只缓存转换过后的图片。
     * DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。
     * DiskCacheStrategy.AUTOMATIC： 表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）。
     */
    public static void loadImageSizekipMemoryCache(Context context, String url, @DrawableRes int placeholder, @DrawableRes int error, ImageView imageView) {
        if (context instanceof Activity && !isActivityEnabled(context)) {
            return;
        }
        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(placeholder) //占位图
                    .error(error)       //错误图S
                    .skipMemoryCache(true)//禁用掉Glide的内存缓存功能
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context).load(url).apply(options).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载圆形图片
     */
    public static void loadCircleImage(Context context, String url, @DrawableRes int placeholder, @DrawableRes int error, ImageView imageView) {
        loadCircleImage(context, url, placeholder, error, imageView, null);
    }

    /**
     * 加载圆形图片
     */
    public static void loadCircleImage(Context context, String url, @DrawableRes int placeholder, @DrawableRes int error, ImageView imageView, RequestListener<Drawable> listener) {
        if (context instanceof Activity && !isActivityEnabled(context)) {
            return;
        }
        try {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .circleCrop()//设置圆形
                    .placeholder(placeholder)
                    .error(error)
                    //.priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context).load(url).apply(options).listener(listener).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 预先加载图片
     * 在使用图片之前，预先把图片加载到缓存，调用了预加载之后，我们以后想再去加载这张图片就会非常快了，
     * 因为Glide会直接从缓存当中去读取图片并显示出来
     */
    public static void preloadImage(Context context, String url) {
        if (context instanceof Activity && !isActivityEnabled(context)) {
            return;
        }
        try {
            Glide.with(context)
                    .load(url)
                    .preload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载圆角图片
     */
    public static void loadRoundImage(Context context, String url, @DrawableRes int placeholder, @DrawableRes int error, int roundDPSize, ImageView imageView) {
        if (context instanceof Activity && !isActivityEnabled(context)) {
            return;
        }
        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(placeholder)
                    .error(error)
                    //.priority(Priority.HIGH)
                    .transform(new RoundedCornersTransformation(dip2px(context, roundDPSize), 0, RoundedCornersTransformation.CornerType.ALL))
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context).load(url).apply(options).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载圆角图片-指定任意部分圆角（图片上、下、左、右四个角度任意定义）
     *
     * @param context
     * @param url
     * @param imageView
     * @param type
     */
    public static void loadCustRoundImage(Context context, String url, @DrawableRes int placeholder, @DrawableRes int error, int roundDPSize, ImageView imageView, RoundedCornersTransformation.CornerType type) {
        if (context instanceof Activity && !isActivityEnabled(context)) {
            return;
        }
        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(placeholder)
                    .error(error)
                    //.priority(Priority.HIGH)
                    .transform(new RoundedCornersTransformation(dip2px(context, roundDPSize), 0, type))
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context).load(url).apply(options).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载模糊图片（自定义透明度）
     *
     * @param context
     * @param url
     * @param imageView
     * @param blur      模糊度，一般1-100够了，越大越模糊
     */
    public static void loadBlurImage(Context context, String url, @DrawableRes int placeholder, @DrawableRes int error, ImageView imageView, int blur) {
        if (context instanceof Activity && !isActivityEnabled(context)) {
            return;
        }
        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(placeholder)
                    .error(error)
                    //.priority(Priority.HIGH)
                    .bitmapTransform(new BlurTransformation(blur))
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context).load(url).apply(options).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载灰度(黑白)图片（自定义透明度）
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadBlackImage(Context context, String url, @DrawableRes int placeholder, @DrawableRes int error, ImageView imageView) {
        if (context instanceof Activity && !isActivityEnabled(context)) {
            return;
        }
        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(placeholder)
                    .error(error)
                    //.priority(Priority.HIGH)
                    .bitmapTransform(new GrayscaleTransformation())
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context).load(url).apply(options).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Glide.with(this).asGif()    //强制指定加载动态图片
     * 如果加载的图片不是gif，则asGif()会报错， 当然，asGif()不写也是可以正常加载的。
     * 加入了一个asBitmap()方法，这个方法的意思就是说这里只允许加载静态图片，不需要Glide去帮我们自动进行图片格式的判断了。
     * 如果你传入的还是一张GIF图的话，Glide会展示这张GIF图的第一帧，而不会去播放它。
     *
     * @param context
     * @param url       例如：https://image.niwoxuexi.com/blog/content/5c0d4b1972-loading.gif
     * @param imageView
     */
    private void loadGif(Context context, String url, @DrawableRes int placeholder, @DrawableRes int error, ImageView imageView) {
        if (context instanceof Activity && !isActivityEnabled(context)) {
            return;
        }
        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(placeholder)
                    .error(error);
            Glide.with(context)
                    .load(url)
                    .apply(options)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int dip2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static void downloadImage(final Context context, final String url, final OnDownloadImageListener listener) {
        if (context instanceof Activity && !isActivityEnabled(context)) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //String url = "http://www.guolin.tech/book.png";
                    FutureTarget<File> target = Glide.with(context)
                            .asFile()
                            .load(url)
                            .submit();
                    final File imageFile = target.get();
                    //Log.d(TAG, "下载好的图片文件路径=" + imageFile.getPath());

                    // 首先保存图片
                    File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
                    File appDir = new File(pictureFolder.getPath());
                    if (!appDir.exists()) {
                        appDir.mkdirs();
                    }
                    String fileName = System.currentTimeMillis() + ".jpg";
                    File destFile = new File(appDir, fileName);

                    copyFile(imageFile.getPath(), destFile.getPath());

                    if (listener != null)
                        listener.onSuccess(destFile.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null)
                        listener.onFail();
                }
            }
        }).start();
    }

    public interface OnDownloadImageListener {
        void onSuccess(String imagePath);
        void onFail();
    }

    /**
     * 判断父窗口是否可用
     * @param context
     * @return
     */
    private static boolean isActivityEnabled(Context context) {
        if (context == null) return false;
        return !((Activity) context).isFinishing() && ((Activity) context).getWindow() != null;
    }

    /**
     * @param oldPath
     * @param newPath
     * @throws Exception
     */
    private static void copyFile(String oldPath, String newPath)
            throws Exception {
        int byteread = 0;
        FileInputStream inPutStream = null;
        FileOutputStream outPutStream = null;

        try {

            // oldPath的文件copy到新的路径下，如果在新路径下有同名文件，则覆盖源文件
            inPutStream = new FileInputStream(oldPath);

            outPutStream = new FileOutputStream(newPath);
            byte[] buffer = new byte[4096];

            while ((byteread = inPutStream.read(buffer)) != -1) {

                // 写入数据
                outPutStream.write(buffer, 0, byteread);
            }
        } finally {
            // inPutStream关闭
            if (inPutStream != null) {
                inPutStream.close();
                inPutStream = null;
            }

            // outPutStream关闭
            if (outPutStream != null) {
                outPutStream.close();
                outPutStream = null;
            }
        }
    }
}
