package com.fccs.library.callback;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;

/**
 * Created by Ethen on 2016/11/23.
 */

public interface Callback {

    void onSuccess(GlideDrawable drawable, GlideAnimation anim);
}
