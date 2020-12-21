package rural.rhiss.Util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by acer on 12/14/2017.
 */

public class TextViewCustom extends AppCompatTextView {
    public TextViewCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewCustom(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Roboto/Roboto-Regular.ttf");
        setTypeface(tf, 1);

    }

}
