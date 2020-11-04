// - [Java Examples for com.android.volley.toolbox.ImageLoader](https://www.javatips.net/api/com.android.volley.toolbox.imageloader)

public static Promise<ImageResult> imagePromise(ImageLoader imageLoader, final String url, int maxWidth, int maxHeight) {
    final DeferredObject<ImageResult> promise = new DeferredObject<ImageResult>();
    imageLoader.get(url, new ImageListener() {

        @Override
        public void onResponse(ImageContainer imageContainer, boolean isImmediate) {
            if (imageContainer.getBitmap() != null) {
                promise.success(new ImageResult(imageContainer, isImmediate));
            }
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            promise.failure(volleyError);
        }
    }, maxWidth, maxHeight);
    return promise;
}
