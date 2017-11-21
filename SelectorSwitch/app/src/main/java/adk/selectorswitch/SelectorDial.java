package adk.selectorswitch;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adu on 11/21/2017.
 */

public class SelectorDial {

    public static final int dialRadiusDP = 14;
    private final float[][] defaultDialColors = {{29f, 23.1f, 94.9f}, {59f, 26.3f, 89.4f},
            {129f, 21.7f, 77.6f}};
    private SelectorUtil util;

    private int dialModeCount;
    private int dialRadius;
    private Float modeSweepingAngle;
    private List<Float> modeStartingAngles;
    private List<Paint> dialPaints;
    private List<Integer> dialColors;


    public SelectorDial(SelectorUtil util) {

        this.util = util;
        dialModeCount = 3;
        dialRadius = util.getPixelsFromDips(dialRadiusDP);
        modeSweepingAngle = util.getSweepingAngle(dialModeCount);
        modeStartingAngles = util.generateStartingAngles(dialModeCount, modeSweepingAngle);

        // Use the default colors.
        dialColors = new ArrayList<>(this.dialModeCount);
        dialColors.add(Color.HSVToColor(defaultDialColors[0]));
        dialColors.add(Color.HSVToColor(defaultDialColors[1]));
        dialColors.add(Color.HSVToColor(defaultDialColors[2]));
        dialPaints = util.generateDialPaints(dialModeCount, dialColors);
    }

    public SelectorDial(SelectorUtil util, int dialModeCount) throws IllegalSelectorException {

        if (dialModeCount <= 0) {
            throw new IllegalSelectorException("Not enough modes in the selector dial!");
        } else if (dialModeCount > 8) {
            throw new IllegalSelectorException("Too many modes in the selector dial!");
        }

        this.util = util;
        this.dialModeCount = dialModeCount;
        dialRadius = util.getPixelsFromDips(dialRadiusDP);
        modeSweepingAngle = util.getSweepingAngle(dialModeCount);
        modeStartingAngles = util.generateStartingAngles(dialModeCount, modeSweepingAngle);

        // Generate blending colors and paints.
        dialColors = util.generateBlendingColors(dialModeCount, Color.HSVToColor(defaultDialColors[0]),
                Color.HSVToColor(defaultDialColors[1]));
        dialPaints = util.generateDialPaints(dialModeCount, dialColors);

    }

    public int getDialRadius() {
        return this.dialRadius;
    }

    public List<Integer> getDialColors() {
        return this.dialColors;
    }

    public void setDialColors(List<Integer> dialColors) {
        this.dialColors = dialColors;
        this.dialPaints = util.generateDialPaints(dialModeCount, dialColors);
    }

    public void setDialColors(int startingColor, int endingColor) {
        this.dialColors = util.generateBlendingColors(dialModeCount, startingColor, endingColor);
        this.dialPaints = util.generateDialPaints(dialModeCount, dialColors);
    }

    public void setModeColor(int index, int color) {
        this.dialColors.set(index, color);
        this.dialPaints = util.generateDialPaints(dialModeCount, dialColors);
    }

    public int getDialModeCount() {
        return dialModeCount;
    }

    public void setDialModeCount(int dialModeCount) throws IllegalSelectorException {

        // Check the validity of the new count.
        if (dialModeCount <= 0) {
            throw new IllegalSelectorException("Too few modes to select!");
        } else if (dialModeCount > 10) {
            throw new IllegalSelectorException("Too many modes to accommodate!");
        }

        // Create sub-list / blend colours.
        if (this.dialModeCount < dialModeCount) {
            this.dialColors = util.generateBlendingColors(dialModeCount, this.dialColors.get(0),
                    this.dialColors.get(this.dialColors.size() - 1));
        } else {
            this.dialColors = dialColors.subList(0, dialModeCount);
        }

        this.dialModeCount = dialModeCount;
        this.modeSweepingAngle = util.getSweepingAngle(dialModeCount);
        this.modeStartingAngles = util.generateStartingAngles(dialModeCount, modeSweepingAngle);
    }

    public Float getModeSweepingAngle() {
        return modeSweepingAngle;
    }

    public List<Float> getModeStartingAngles() {
        return modeStartingAngles;
    }

    public Float getModeStartingAngle(int mode) {
        if (mode < 0) mode = 0;
        if (mode >= dialModeCount) mode = dialModeCount - 1;
        return modeStartingAngles.get(mode);
    }

    public List<Paint> getDialPaints() {
        return dialPaints;
    }

    public Paint getDailPaintForMode(int mode){
        if (mode < 0) mode = 0;
        if (mode >= dialModeCount) mode = dialModeCount - 1;
        return dialPaints.get(mode);
    }

}
