package rural.rhiss.Object;

import android.content.Context;

/**
 * Created by acer on 12/15/2017.
 */

public class LGDCodeObject {
    private String string;
    private Context mContext;

    public LGDCodeObject(Context ctx) {
        super();
        mContext = ctx;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
