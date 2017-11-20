package adk.selectorswitch;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Adu on 11/20/2017.
 */

public class SelectorSwitch extends View {

    // Base
    int baseCenterX;
    int baseCenterY;
    int baseRadius;
    public SelectorSwitch(Context context) {
        super(context);
    }

    public SelectorSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectorSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SelectorSwitch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
