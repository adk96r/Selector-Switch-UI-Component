package adk.selectorswitch;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adu on 11/21/2017.
 */

public class SelectorUtil {

    private Context context;

    SelectorUtil(Context context) {
        this.context = context;
    }

    /**
     * Converts DIPs to Pixels.
     *
     * @param dips DIPs.
     * @return pixels   Equivalent to the given DIPs.
     */
    public int getPixelsFromDips(int dips) {
        float density = context.getTheme().getResources().getDisplayMetrics().density;
        return (int) (density * dips / 0.5);
    }

    /**
     * Returns a Paint instance created as per the required specs.
     *
     * @param color         Color of the paint.
     * @param style         Fill, Stroke, etc.
     * @param shadowEnabled Enables shadow for the objects using this paint.
     * @param shadowColor   Color of the shadow if the shadow is enabled.
     * @return paint        Paint instance created.
     */
    public Paint createPaintFromColor(int color, Paint.Style style, boolean shadowEnabled, int shadowColor) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(style);
        paint.setAntiAlias(true);
        if (shadowEnabled) {
            paint.setShadowLayer(getPixelsFromDips(2), 0, 0, shadowColor);
        }
        return paint;
    }

    /**
     * Calculates the angle between two successive modes in the selector dial.
     *
     * @param maxModes Number of modes in the selector dial.
     * @return sweepingAngle
     */
    public Float getSweepingAngle(int maxModes) {
        return (float) 360 / maxModes;
    }

    /**
     * Generates the starting angle for each mode position on the dial of the selector.
     *
     * @param dialModeCount Number of modes in the selector dial.
     * @param sweepingAngle Angle between the starting angles of two
     *                      successive modes on the dial.
     * @return startingAngles   List of starting angles for each mode in the selector dial.
     */
    public List<Float> generateStartingAngles(int dialModeCount, Float sweepingAngle) {
        List<Float> startingAngles = new ArrayList<>(dialModeCount);
        for (int i = 0; i < dialModeCount; i++) {
            startingAngles.add(i * sweepingAngle);
        }
        return startingAngles;
    }

    /**
     * Generates a list of paint instances based on the mode colors (ints).
     *
     * @param dialModeCount Number of modes in the selector switch.
     * @param colors        List of Color(integers).
     * @return dialPaints  List of Paint instances.
     */
    public List<Paint> generateDialPaints(int dialModeCount, List<Integer> colors) {
        List<Paint> dialPaints = new ArrayList<>(dialModeCount);
        for (int i = 0; i < dialModeCount; i++) {
            if (i < colors.size())
                dialPaints.add(createPaintFromColor(colors.get(i), Paint.Style.FILL, false, Color.LTGRAY));
            else
                dialPaints.add(createPaintFromColor(Color.BLACK, Paint.Style.FILL, false, Color.LTGRAY));
        }
        return dialPaints;
    }

    public List<Integer> generateBlendingColors(int dialModeCount, int startingColor, int endingColor) {

        List<Integer> blendedColors = new ArrayList<>(dialModeCount);
        float[] startingColorHSV = new float[3];
        float[] endingColorHSV = new float[3];

        Color.colorToHSV(startingColor, startingColorHSV);
        Color.colorToHSV(endingColor, endingColorHSV);

        float hInc = endingColorHSV[0] - startingColorHSV[0];
        float sInc = endingColorHSV[1] - startingColorHSV[1];
        float vInc = endingColorHSV[2] - startingColorHSV[2];
        float[] temp;

        // Generate the blending colors.
        for (int i = 0; i < dialModeCount; i++) {
            temp = new float[]{startingColorHSV[0] + i * hInc,
                    startingColorHSV[1] + i * sInc,
                    startingColorHSV[2] + i * vInc};
            blendedColors.add(Color.HSVToColor(temp));
        }

        return blendedColors;
    }

}
