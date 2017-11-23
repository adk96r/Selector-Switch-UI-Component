package adk.selectorswitch;

import android.os.AsyncTask;

/**
 * SelectorKnobAnimator is built on top of
 * <a href="https://developer.android.com/reference/android/os/AsyncTask.html"AsyncTask</a>
 * and is used primarily by {@link SelectorSwitch} to animate the rotation of its knob when
 * the mode of the switch is changed.
 *
 * @see SelectorSwitch
 * @see SelectorKnob
 */
class SelectorKnobAnimator extends AsyncTask<Void, Void, Void> {

    /**
     * The selector switch whose knob has to be animated.
     *
     * @see SelectorSwitch
     */
    private SelectorSwitch selectorSwitch;

    /**
     * The selector knob being animated.
     *
     * @see SelectorKnob
     */
    private SelectorKnob selectorKnob;

    /**
     * The angle to rotate the knob by.
     */
    private float rotateBy;

    /**
     * The angle increment in which the rotation proceeds.
     */
    private float stepAngle;

    /**
     * Create an Async task to animate the knob's rotation.
     *
     * @param selectorSwitch <tt>SelectorSwitch</tt> : The SelectorSwitch whose knob has
     *                       to be animated.
     * @param selectorKnob   <tt>SelectorKnob</tt> : The SelectorKnob to be animated.
     * @param rotateBy       <tt>float</tt> : The angle to rotate the knob by.
     * @param stepAngle      <tt>float</tt> : Angle increment to rotate the knob until
     *                       the ending angle has been reached.
     * @see SelectorSwitch
     * @see SelectorKnob
     */
    SelectorKnobAnimator(SelectorSwitch selectorSwitch, SelectorKnob selectorKnob,
                         float rotateBy, float stepAngle) {
        this.selectorSwitch = selectorSwitch;
        this.selectorKnob = selectorKnob;
        this.rotateBy = rotateBy;
        this.stepAngle = stepAngle;
    }

    /**
     * Animates the knob by rotating it in increments and waiting 250ns between each increment,
     * until the knob has been rotated by the desired angle {@link #rotateBy} at which moment
     * the animation is completed. The method call to the knob, for the actual rotation, is in
     * the {@link #onProgressUpdate(Object[])}. This method only controls the time delay between
     * each step increment in the knob's angle.
     *
     * @param params Parameters used. ( None used here. )
     * @return null
     */
    @Override
    protected Void doInBackground(Void... params) {
        for (float angle = 0; angle < rotateBy; angle += stepAngle) {
            synchronized (this) {
                try {
                    wait(0, 250);
                    publishProgress();
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }

    /**
     * Rotates the knob by a specified step angle. This progress is updated repeatedly by
     * {@link #doInBackground(Object[])} until the knob has been rotated by a net desired
     * angle {@link #rotateBy}.
     *
     * @param values Values used. ( None used here. )
     * @see SelectorKnob#rotateBy(float)
     */
    protected void onProgressUpdate(Void... values) {
        selectorKnob.rotateBy(stepAngle);
        selectorSwitch.invalidate();
    }
}
