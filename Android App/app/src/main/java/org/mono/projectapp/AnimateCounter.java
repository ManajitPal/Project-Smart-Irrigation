package org.mono.projectapp;

/**
 * Created by ManajitPal on 01-04-2017.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.Interpolator;
import android.widget.TextView;

/**
 * AnimateCounter provides ability to animate the changing of numbers using the builtin
 * Android Interpolator animation functionality.
 *
 */
public class AnimateCounter {
    /**
     * TextView to be animated
     */
    private TextView mView;
    /**
     * Duration of animation
     */
    private long mDuration;
    /**
     * Initial value to start animation
     */
    private float mStartValue;
    /**
     * End value to finish animation
     */
    private float mEndValue;
    /**
     * Decimal precision for floating point values
     */
    private int mPrecision;
    /**
     * Interpolator functionality to apply to animation
     */
    private Interpolator mInterpolator;
    private ValueAnimator mValueAnimator;

    /**
     * Provides optional callback functionality on completion of animation
     */
    private AnimateCounterListener mListener;

    /**
     * Call to execute the animation
     */
    public void execute(){
        mValueAnimator = ValueAnimator.ofFloat(mStartValue, mEndValue);
        mValueAnimator.setDuration(mDuration);
        mValueAnimator.setInterpolator(mInterpolator);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float current = Float.valueOf(valueAnimator.getAnimatedValue().toString());
                mView.setText(String.format("%." + mPrecision + "f", current));
            }
        });

        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.onAnimateCounterEnd();
                }
            }
        });

        mValueAnimator.start();
    }

    public static class Builder {
        private long mDuration = 2000;
        private float mStartValue = 0;
        private float mEndValue = 10;
        private int mPrecision = 0;
        private Interpolator mInterpolator = null;
        private TextView mView;

        public Builder(@NonNull TextView view) {
            if (view == null) {
                throw new IllegalArgumentException("View can not be null");
            }
            mView = view;
        }

        /**
         * Set the start and end integers to be animated
         *
         * @param start initial value
         * @param end final value
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setCount(final int start, final int end) {
            if (start == end) {
                throw new IllegalArgumentException("Count start and end must be different");
            }

            mStartValue = start;
            mEndValue = end;
            mPrecision = 0;
            return this;
        }

        /**
         * Set the start and end floating point numbers to be animated
         *
         * @param start initial value
         * @param end final value
         * @param precision number of decimal places to use
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setCount(final float start, final float end, final int precision) {
            if (Math.abs(start - end) < 0.001) {
                throw new IllegalArgumentException("Count start and end must be different");
            }
            if (precision < 0) {
                throw new IllegalArgumentException("Precision can't be negative");
            }
            mStartValue = start;
            mEndValue = end;
            mPrecision = precision;
            return this;
        }

        /**
         * Set the duration of the animation from start to end
         *
         * @param duration total duration of animation in ms
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setDuration(long duration) {
            if (duration <= 0) {
                throw new IllegalArgumentException("Duration must be positive value");
            }
            mDuration = duration;
            return this;
        }

        /**
         * Set the interpolator to be used with the animation
         *
         * @param interpolator Optional interpolator to set
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setInterpolator(@Nullable Interpolator interpolator) {
            mInterpolator = interpolator;
            return this;
        }

        /**
         * Creates a {@link AnimateCounter} with the arguments supplied to this builder. It does not
         * {@link AnimateCounter#execute()} the AnimationCounter.
         * Use {@link #execute()} to start the animation
         */
        public AnimateCounter build() {
            return new AnimateCounter(this);
        }
    }

    private AnimateCounter(Builder builder) {
        mView = builder.mView;
        mDuration = builder.mDuration;
        mStartValue = builder.mStartValue;
        mEndValue = builder.mEndValue;
        mPrecision = builder.mPrecision;
        mInterpolator = builder.mInterpolator;
    }

    /**
     * Stop the current animation
     */
    public void stop() {
        if (mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
    }

    /**
     * Set a listener to get notification of completion of animation
     *
     * @param listener AnimationCounterListener to be used for callbacks
     */
    public void setAnimateCounterListener(AnimateCounterListener listener) {
        mListener = listener;
    }

    /**
     * Callback interface for notification of animation end
     */
    public interface AnimateCounterListener {
        void onAnimateCounterEnd();
    }
}