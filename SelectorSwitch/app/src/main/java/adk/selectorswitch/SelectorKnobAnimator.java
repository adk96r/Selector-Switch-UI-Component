package adk.selectorswitch;

import android.os.AsyncTask;

/**
 * SelectorKnobAnimator is built on top of {@link
 * <a href="https://developer.android.com/reference/android/os/AsyncTask.html">AsyncTask</a>}
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
     * The delay, in nanoseconds, between successive increments of
     * the knob's rotation.
     */
    private int stepWait;

    /**
     * Create an Async task to animate the knob's rotation.
     *
     * @param selectorSwitch <tt>SelectorSwitch</tt> : The SelectorSwitch whose knob has
     *                       to be animated.
     * @param selectorKnob   <tt>SelectorKnob</tt> : The SelectorKnob to be animated.
     * @param rotateBy       <tt>float</tt> : The angle to rotate the knob by.
     * @param stepAngle      <tt>float</tt> : Angle increment to rotate the knob until
     *                       the ending angle has been reached.
     * @param stepWaitNanos  <tt>int</tt> : Delay in nanoseconds between each successive
     *                       increment of the knob's rotation.
     * @see SelectorSwitch
     * @see SelectorKnob
     */
    SelectorKnobAnimator(SelectorSwitch selectorSwitch, SelectorKnob selectorKnob,
                         float rotateBy, float stepAngle, int stepWaitNanos) {
        this.selectorSwitch = selectorSwitch;
        this.selectorKnob = selectorKnob;
        this.stepAngle = rotateBy > 0 ? stepAngle : -stepAngle;
        this.rotateBy = Math.abs(rotateBy);
        this.stepWait = stepWaitNanos;
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

        // Number of iterations.
        float iter = Math.abs(rotateBy / stepAngle);

        // Rotate with delay.
        for (float i = 0; i < iter; i++) {
            synchronized (this) {
                try {
                    wait(0, stepWait);
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
