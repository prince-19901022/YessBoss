package com.example.princeporosh.yessboss.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.princeporosh.yessboss.R;
import com.example.princeporosh.yessboss.model.TaskCategory;

/**
 * Created by Prince Porosh on 9/7/2018.
 */

public class ViewUtils {

    public static void showCategoryCreatorDialog(Activity activity, final CategoryCreatorListener ccl){

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.category_creator_view, null);
        final EditText categoryET = dialogView.findViewById(R.id.et_category_name);

        dialogBuilder.setView(dialogView);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        dialogView.findViewById(R.id.btn_cancel_in_category_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cat = categoryET.getText().toString();
                ccl.onSpecify(new TaskCategory(cat,false));
                dialog.dismiss();
            }
        });

    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }
}
