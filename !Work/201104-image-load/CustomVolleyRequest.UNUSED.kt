package ru.lilliputten.camclient.helpers

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
// import android.support.v4.util.LruCache
import com.android.volley.Cache
import com.android.volley.Network
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.ImageLoader
import ru.lilliputten.camclient.ItemDetailFragment
import kotlin.jvm.Synchronized

/**
 * Created by Belal on 10/8/2015.
 */
class CustomVolleyRequest private constructor(private val context: Context) {

    private var requestQueue: RequestQueue?
    val imageLoader: ImageLoader

    fun getRequestQueue(): RequestQueue {
        if (requestQueue == null) {
            val cache: Cache = DiskBasedCache(context!!.cacheDir, 10 * 1024 * 1024)
            val network: Network = BasicNetwork(HurlStack())
            requestQueue = RequestQueue(cache, network)
            requestQueue!!.start()
        }
        return requestQueue!!
    }

    companion object {
        private var customVolleyRequest: CustomVolleyRequest? = null
        private val context: Context? = null

        @Synchronized
        fun getInstance(context: Context): CustomVolleyRequest? {
            if (customVolleyRequest == null) {
                customVolleyRequest = CustomVolleyRequest(context)
            }
            return customVolleyRequest
        }
    }

    init {
        // this.context = context
        requestQueue = getRequestQueue()
        imageLoader = ImageLoader(requestQueue,
                object : ImageLoader.ImageCache {
                    private val cache: LruCache<String, Bitmap> = LruCache<String, Bitmap>(20)
                    override fun getBitmap(url: String): Bitmap? {
                        return cache.get(url)
                    }

                    override fun putBitmap(url: String, bitmap: Bitmap) {
                        cache.put(url, bitmap)
                    }
                })
        // YVolley.getInstance(context).getImageLoader().get(category.child.get(i).icon, new ImageLoader.ImageListener() {
        //     @Override
        //     public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
        //         Drawable drawable = new BitmapDrawable(context.getResources(), response.getBitmap());
        //         drawable.setBounds(0, 0, GeneralUtil.dip2px(context, 45), GeneralUtil.dip2px(context, 45));
        //         button.setCompoundDrawables(null, drawable, null, null);
        //         Log.i("swifter", "get icon ... success == "+url);
        //     }
        //
        //     @Override
        //     public void onErrorResponse(VolleyError error) {
        //         Log.i("swifter", "get drawable icon error...");
        //     }
        // });
    }

    fun getLoader(): ImageLoader {
        return imageLoader
    }

}
