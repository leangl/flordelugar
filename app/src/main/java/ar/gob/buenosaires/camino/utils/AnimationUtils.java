package ar.gob.buenosaires.camino.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class AnimationUtils {
    /**
     * Constructor
     */
    protected AnimationUtils() {
        super();
    }

    /**
     * Validate view visibility status and execute the proper animation to expand or collapse
     *
     * @param viewValue
     */
    public static final void verticalExpandTranformation(View viewValue) {
        // validate view visibility status
        if (viewValue.getVisibility() == View.VISIBLE) {
            // collapse view and set visibility as gone
            AnimationUtils.verticalCollapse(viewValue);
        } else {
            // expand view and set visibility as visible
            AnimationUtils.verticalExpand(viewValue);
        }
    }

    /**
     * Expand a view vertically from invisible to visible
     *
     * @param viewValue
     */
    public static void verticalExpand(final View viewValue) {
        // validate if the view is already expanded
        if (viewValue.getVisibility() == View.VISIBLE) {
            return;
        }

        // set layout parameters
        viewValue.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // get and keep initial view height
        final int targtetHeight = viewValue.getMeasuredHeight();

        // initialize view visible and with 0dp height
        viewValue.getLayoutParams().height = 0;
        viewValue.setVisibility(View.VISIBLE);

        // create a new animation
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation transformation) {
                viewValue.getLayoutParams().height = interpolatedTime == 1 ? ViewGroup.LayoutParams.WRAP_CONTENT : (int) (targtetHeight * interpolatedTime);
                viewValue.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // execute the animation 1dp by milliseconds
        animation.setDuration((int) (targtetHeight / viewValue.getContext().getResources().getDisplayMetrics().density));
        viewValue.startAnimation(animation);
    }

    /**
     * Expand a view vertically from invisible to visible
     *
     * @param viewValue
     */
    public static void verticalCollapse(final View viewValue) {
        // validate if the view is already expanded
        if (viewValue.getVisibility() == View.GONE) {
            return;
        }

        // get and keep initial view height
        final int initialHeight = viewValue.getMeasuredHeight();

        // create a new animation
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation transformation) {
                // validate time
                if (interpolatedTime == 1) {
                    viewValue.setVisibility(View.GONE);
                } else {
                    viewValue.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    viewValue.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // execute the animation 1dp by milliseconds
        animation.setDuration((int) (initialHeight / viewValue.getContext().getResources().getDisplayMetrics().density));
        viewValue.startAnimation(animation);
    }

    /**
     * Validate view visibility status and execute the proper animation to expand or collapse
     *
     * @param viewValue
     */
    public static final void horizontalExpandTranformation(View viewValue) {
        // validate view visibility status
        if (viewValue.getVisibility() == View.VISIBLE) {
            // collapse view and set visibility as gone
            AnimationUtils.horizontalCollapse(viewValue);
        } else {
            // expand view and set visibility as visible
            AnimationUtils.horizontalExpand(viewValue);
        }
    }

    /**
     * Expand a view horizontally from invisible to visible
     *
     * @param viewValue
     */
    public static void horizontalExpand(final View viewValue) {
        // validate if the view is already expanded
        if (viewValue.getVisibility() == View.VISIBLE) {
            return;
        }

        // set layout parameters
        viewValue.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // get and keep initial view width
        final int targetWidth = viewValue.getMeasuredWidth();

        // initialize view visible and with 0dp width
        viewValue.getLayoutParams().width = 0;
        viewValue.setVisibility(View.VISIBLE);

        // create a new animation
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation transformation) {
                viewValue.getLayoutParams().width = interpolatedTime == 1 ? ViewGroup.LayoutParams.WRAP_CONTENT : (int) (targetWidth * interpolatedTime);
                viewValue.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // execute the animation 1dp by milliseconds
        animation.setDuration((int) (targetWidth / viewValue.getContext().getResources().getDisplayMetrics().density));
        viewValue.startAnimation(animation);
    }

    /**
     * Expand a view vertically from invisible to visible
     *
     * @param viewValue
     */
    public static void horizontalCollapse(final View viewValue) {
        // validate if the view is already expanded
        if (viewValue.getVisibility() == View.GONE) {
            return;
        }

        // get and keep initial view width
        final int initialWidth = viewValue.getMeasuredWidth();

        // create a new animation
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation transformation) {
                // validate time
                if (interpolatedTime == 1) {
                    viewValue.setVisibility(View.GONE);
                } else {
                    viewValue.getLayoutParams().width = initialWidth - (int) (initialWidth * interpolatedTime);
                    viewValue.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // execute the animation 1dp by milliseconds
        animation.setDuration((int) (initialWidth / viewValue.getContext().getResources().getDisplayMetrics().density));
        viewValue.startAnimation(animation);
    }
}