package com.hosiluan.simplecamera;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * Created by User on 11/23/2017.
 */

public class CachingBitmap {

    private LruCache<String, Bitmap> mMemoryCache;

    private static CachingBitmap cache;

    public static CachingBitmap getInstance() {
        if (cache == null) {
            cache = new CachingBitmap();
        }
        return cache;
    }

    public CachingBitmap() {
      initializeCache();
    }

    private void initializeCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        final int cacheSize = maxMemory / 3;

        System.out.println("cache size = " + cacheSize);

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            protected int sizeOf(String key, Bitmap value) {
                // The cache size will be measured in kilobytes rather than number of items.

                int bitmapByteCount = value.getRowBytes() * value.getHeight();

                return bitmapByteCount / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {

        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}