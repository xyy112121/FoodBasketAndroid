package com.foodBasket.view.banner;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

/**
 * Created by ""
 */

public class HolderCreator  implements CBViewHolderCreator<ImageHolder> {
    @Override
    public ImageHolder createHolder() {
        return new ImageHolder();
    }
}
