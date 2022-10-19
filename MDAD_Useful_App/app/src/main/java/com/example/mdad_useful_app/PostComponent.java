package com.example.mdad_useful_app;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.example.mdad_useful_app.entity.Post;

public class PostComponent {


    private PostComponent(Context context) {
    }

    // Make it so that this method creates the actual post component and returns it
    public static LinearLayout createComponent(Context context){
        // post container
        LinearLayout post = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        layoutParams.setMargins(spToPx(40, context),
                spToPx(40, context),
                spToPx(40, context),
                spToPx(40, context));
        post.setLayoutParams(layoutParams);
        post.setOrientation(LinearLayout.VERTICAL);
        post.setPadding(
                spToPx(40, context),
                spToPx(10, context),
                spToPx(40, context),
                spToPx(10, context));
        post.setBackground(ContextCompat.getDrawable(context, R.drawable.roundedcorners));
        post.setBackgroundTintList(AppCompatResources.getColorStateList(context, com.google.android.material.R.color.material_dynamic_neutral20));

        // ellipses button
        ConstraintLayout ellipsesConstraint = new ConstraintLayout(post.getContext());
        ellipsesConstraint.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        ellipsesConstraint.setId(View.generateViewId());

        Button button = new Button(ellipsesConstraint.getContext());
        button.setLayoutParams(new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        button.setPadding(5, 5, 5, 5);
        button.setText("...");
        button.setTextSize(24);
        button.setId(View.generateViewId());
        button.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        button.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        button.setPadding(0, 0, 0, 0);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        button.setTextColor(Color.WHITE);

        ellipsesConstraint.addView(button);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(ellipsesConstraint);
        constraintSet.connect(button.getId(), ConstraintSet.TOP, ellipsesConstraint.getId(), ConstraintSet.TOP);
        constraintSet.connect(button.getId(), ConstraintSet.RIGHT, ellipsesConstraint.getId(), ConstraintSet.RIGHT);
        constraintSet.connect(button.getId(), ConstraintSet.BOTTOM, ellipsesConstraint.getId(), ConstraintSet.BOTTOM);

        constraintSet.applyTo(ellipsesConstraint);
        post.addView(ellipsesConstraint);
        // ellipses button end

        // image
        ImageView imageView = new ImageView(post.getContext());
        imageView.setLayoutParams(new LayoutParams(MATCH_PARENT, spToPx(200, context)));
        imageView.setBackgroundColor(Color.GRAY);

        post.addView(imageView);

        // linear layout
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            // text view - image caption
            TextView textView = new TextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.6f));
            textView.setText("This is some text to act as a placeholder for caption of the image");
            linearLayout.addView(textView);
            // text view - map icon
            TextView mapTextView = new TextView(context);
            mapTextView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 0.4f));
            mapTextView.setText("Map icon");
            linearLayout.addView(mapTextView);

            post.addView(linearLayout);

        // horizontal scroll view
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
        horizontalScrollView.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            // linear layout
            LinearLayout linearLayout1 = new LinearLayout(context);
            linearLayout1.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
            horizontalScrollView.addView(linearLayout1);
                // text view - misc information items
                TextView textView1 = new TextView(context);
                textView1.setLayoutParams(new LayoutParams(dpToPx(500, context), dpToPx(50, context)));
                textView1.setGravity(Gravity.CENTER);
                textView1.setText("test");
                textView1.setBackgroundColor(Color.GRAY);
                linearLayout1.addView(textView1);
                // text view - misc information items
                TextView textView2 = new TextView(context);
                textView2.setLayoutParams(new LayoutParams(dpToPx(500, context), dpToPx(50, context)));
                textView2.setGravity(Gravity.CENTER);
                textView2.setText("test");
                textView2.setBackgroundColor(Color.GRAY);
                linearLayout1.addView(textView2);

        post.addView(horizontalScrollView);

        return post;
    }

    public static int pxToDp(float px, Context context){
        return (int)px*(int)context.getResources().getDisplayMetrics().density;
    }

    public static int dpToSp(float dp, Context context) {
        return (int) (dpToPx(dp, context) / context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static LinearLayout createComponent(Context context, Post postEntity) {
        String caption = postEntity.caption;

        // post container
        LinearLayout post = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        layoutParams.setMargins(spToPx(40, context),
                spToPx(40, context),
                spToPx(40, context),
                spToPx(40, context));
        post.setLayoutParams(layoutParams);
        post.setOrientation(LinearLayout.VERTICAL);
        post.setPadding(
                spToPx(40, context),
                spToPx(10, context),
                spToPx(40, context),
                spToPx(10, context));
        post.setBackground(ContextCompat.getDrawable(context, R.drawable.roundedcorners));
        post.setBackgroundTintList(AppCompatResources.getColorStateList(context, com.google.android.material.R.color.material_dynamic_neutral20));

        // ellipses button
        ConstraintLayout ellipsesConstraint = new ConstraintLayout(post.getContext());
        ellipsesConstraint.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        ellipsesConstraint.setId(View.generateViewId());

        Button button = new Button(ellipsesConstraint.getContext());
        button.setLayoutParams(new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        button.setPadding(5, 5, 5, 5);
        button.setText("...");
        button.setTextSize(24);
        button.setId(View.generateViewId());
        button.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        button.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        button.setPadding(0, 0, 0, 0);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        button.setTextColor(Color.WHITE);

        ellipsesConstraint.addView(button);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(ellipsesConstraint);
        constraintSet.connect(button.getId(), ConstraintSet.TOP, ellipsesConstraint.getId(), ConstraintSet.TOP);
        constraintSet.connect(button.getId(), ConstraintSet.RIGHT, ellipsesConstraint.getId(), ConstraintSet.RIGHT);
        constraintSet.connect(button.getId(), ConstraintSet.BOTTOM, ellipsesConstraint.getId(), ConstraintSet.BOTTOM);

        constraintSet.applyTo(ellipsesConstraint);
        post.addView(ellipsesConstraint);
        // ellipses button end

        // image
        ImageView imageView = new ImageView(post.getContext());
        imageView.setLayoutParams(new LayoutParams(MATCH_PARENT, spToPx(200, context)));
        imageView.setBackgroundColor(Color.GRAY);

        post.addView(imageView);

        // linear layout
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        // text view - image caption
        TextView textView = new TextView(context);
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.6f));
        textView.setText(caption);
        linearLayout.addView(textView);
        // text view - map icon
        TextView mapTextView = new TextView(context);
        mapTextView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 0.4f));
        mapTextView.setText("Map icon");
        linearLayout.addView(mapTextView);

        post.addView(linearLayout);

        // horizontal scroll view
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
        horizontalScrollView.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        // linear layout
        LinearLayout linearLayout1 = new LinearLayout(context);
        linearLayout1.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        horizontalScrollView.addView(linearLayout1);
        // text view - misc information items
        TextView textView1 = new TextView(context);
        textView1.setLayoutParams(new LayoutParams(dpToPx(500, context), dpToPx(50, context)));
        textView1.setGravity(Gravity.CENTER);
        textView1.setText("test");
        textView1.setBackgroundColor(Color.GRAY);
        linearLayout1.addView(textView1);
        // text view - misc information items
        TextView textView2 = new TextView(context);
        textView2.setLayoutParams(new LayoutParams(dpToPx(500, context), dpToPx(50, context)));
        textView2.setGravity(Gravity.CENTER);
        textView2.setText("test");
        textView2.setBackgroundColor(Color.GRAY);
        linearLayout1.addView(textView2);

        post.addView(horizontalScrollView);

        return post;
    }
}
