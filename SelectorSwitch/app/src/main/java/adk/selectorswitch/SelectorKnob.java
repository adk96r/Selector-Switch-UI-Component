package adk.selectorswitch;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;


/**
 * Created by Adu on 11/21/2017.
 */

public class SelectorKnob {

    private Path knobPath;
    private int knobRadius1;
    private int knobRadius2;
    private int knobLength;
    private int centerX, centerY;

    public SelectorKnob(int centerX, int centerY, SelectorUtil util) {

        // Set knob properties.
        knobRadius1 = util.getPixelsFromDips(4);
        knobRadius2 = util.getPixelsFromDips(1);
        knobLength = util.getPixelsFromDips(3);


        knobPath = new Path();

        // The central knob.
        knobPath.addArc(centerX - knobRadius1, centerY - knobRadius1, centerX + knobRadius1,
                centerY + knobRadius1, 230, 260);

        // The end notch.
        knobPath.addArc(centerX - knobRadius1 - knobLength - knobRadius2, centerY - knobRadius2,
                centerX - knobRadius1 - knobLength + knobRadius2, centerY + knobRadius2, 90, 180);

        // The handle between them.
        knobPath.moveTo(centerX - knobLength + knobRadius2 / 2, centerY - knobRadius1 * 3 / 4);
        knobPath.lineTo(centerX - knobRadius1 - knobLength, centerY - knobRadius2);
        knobPath.lineTo(centerX - knobRadius1 - knobLength, centerY + knobRadius2);
        knobPath.lineTo(centerX - knobLength + knobRadius2 / 2, centerY + knobRadius1 * 3 / 4);
        knobPath.close();

    }

    public Path getKnobPath() {
        return this.knobPath;
    }

    public void rotateKnob(Matrix rotationMatrix) {
        this.knobPath.transform(rotationMatrix);
    }


}
