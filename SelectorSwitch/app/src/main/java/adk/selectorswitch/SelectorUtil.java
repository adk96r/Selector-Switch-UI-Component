package adk.selectorswitch;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * Cooked by ADK96r on 21 Nov '17.
 */

class SelectorUtil {

    private Context context;

    SelectorUtil(Context context) {
        this.context = context;
    }

    static List<Integer> arrayToList(int[] array) {
        List<Integer> list = new ArrayList<>(array.length);
        for (int anArray : array) {
            list.add(anArray);
        }
        return list;
    }

    /**
     * Calculates the angle between two successive modes in the selector dial.
     *
     * @param maxModes Number of modes in the selector dial.
     * @return sweepingAngle
     */
    static float getSweepingAngle(int maxModes) {
        return (float) 360 / maxModes;
    }

    /**
     * Generates the starting angle for each mode on the dial of the selector.
     *
     * @param dialModeCount Number of modes in the selector dial.
     * @param sweepingAngle Angle between the starting angles of two
     *                      successive modes on the dial.
     * @return startingAngles   List of starting angles for each mode in the selector dial.
     */
    static List<Float> generateStartingAngles(int dialModeCount, Float sweepingAngle) {
        List<Float> startingAngles = new ArrayList<>(dialModeCount);
        for (int i = 0; i < dialModeCount; i++) {
            startingAngles.add((i * sweepingAngle) + 180);
        }
        return startingAngles;
    }

    /**
     * Returns the possible positions (angles) that the knob can exist in when pointing
     * to the modes in the dial.
     *
     * @param dialStartingAngles List of starting angles for each mode in the dial.
     * @return knobPositions     List of angles for the knob.
     */
    static List<Float> generateKnobPositions(List<Float> dialStartingAngles) {

        int size = dialStartingAngles.size();
        List<Float> knobPositions = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            knobPositions.add((dialStartingAngles.get(i) + dialStartingAngles.get((i + 1) % size) - 360) / 2);
        }
        return knobPositions;
    }

    /**
     * Generates a list of paint instances based on the mode colors (ints).
     *
     * @param dialModeCount Number of modes in the selector switch.
     * @param colors        List of Color(integers).
     * @return dialPaints  List of Paint instances.
     */
    static List<Paint> generateDialPaints(int dialModeCount, List<Integer> colors) {
        List<Paint> dialPaints = new ArrayList<>(dialModeCount);
        for (int i = 0; i < dialModeCount; i++) {
            if (i < colors.size())
                dialPaints.add(createPaintFromColor(colors.get(i), Paint.Style.FILL, false, 0, 0));
            else
                dialPaints.add(createPaintFromColor(Color.BLACK, Paint.Style.FILL, false, 0, 0));
        }
        return dialPaints;
    }

    /**
     * Generates a list of colors that blend from a starting color to an ending color.
     *
     * @param dialModeCount Size of the list.
     * @param startingColor Color to start blending from.
     * @param endingColor   Color to finally blend into.
     * @return colorList
     */
    static List<Integer> generateBlendingColors(int dialModeCount, int startingColor, int endingColor) {

        List<Integer> colorList = new ArrayList<>(dialModeCount);
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
            colorList.add(Color.HSVToColor(temp));
        }

        return colorList;
    }

    /**
     * Converts DPs to Pixels based on the screen's pixel density.
     *
     * @param dps DPs.
     * @return pixels   Pixel count equivalent to the given DP value.
     */
    static int getPixelsFromDips(int dps, float density) {
        return (int) (density * dps / 0.5);
    }

    /**
     * Returns a paint instance created as per the required specs.
     *
     * @param color         Color of the paint.
     * @param style         Fill, Stroke, etc.
     * @param shadowEnabled Enables shadow for the objects using this paint.
     * @param shadowColor   Color of the shadow if the shadow is enabled.
     * @param shadowLength  Length of the shadow in pixels.
     * @return paint        Paint instance created.
     */
    static Paint createPaintFromColor(int color, Paint.Style style, boolean shadowEnabled,
                                      int shadowColor, int shadowLength) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(style);
        paint.setAntiAlias(true);
        if (shadowEnabled) {
            paint.setShadowLayer(shadowLength, 0, 0, shadowColor);
        }
        return paint;
    }


}
