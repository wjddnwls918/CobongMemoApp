package cobong.jeongwoojin.cobongmemo.cobongmemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerView;
import com.skydoves.colorpickerpreference.FlagView;

public class ColorPickerDialog extends AlertDialog {

    private ColorPickerView colorPickerView;

    public ColorPickerDialog(Context context) {
        super(context);
        initColorPickerView();
    }

    protected ColorPickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initColorPickerView();
    }

    protected ColorPickerDialog(Context context, int themeResId) {
        super(context, themeResId);
        initColorPickerView();
    }

    private void initColorPickerView() {
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_dialog_colorpicker, null);
        this.colorPickerView = view.findViewById(R.id.ColorPickerView);
        super.setView(view);
    }

    public void setFlagView(FlagView flagView) {
        this.colorPickerView.setFlagView(flagView);
    }

    public void setOnColorListener(ColorListener colorListener) {
        this.colorPickerView.setColorListener(colorListener);
    }

    public static class Builder extends AlertDialog.Builder {
        private ColorListener colorListener;
        private ColorPickerView colorPickerView;

        private Context context;

        public Builder(Context context) {
            super(context);
            this.context = context;
            initColorPickerView();
        }

        public Builder(Context context, int themeResId) {
            super(context, themeResId);
            this.context = context;
            initColorPickerView();
        }

        private void initColorPickerView()  {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.layout_dialog_colorpicker, null);
            this.colorPickerView = view.findViewById(R.id.ColorPickerView);
            super.setView(view);
        }

        public void setPreferenceName(String preferenceName) {
            this.colorPickerView.setPreferenceName(preferenceName);
        }

        public void setFlagView(FlagView flagView) {
            this.colorPickerView.setFlagView(flagView);
        }

        public void setOnColorListener(ColorListener colorListener) {
            this.colorListener = colorListener;
        }

        public ColorPickerView getColorPickerView() {
            return this.colorPickerView;
        }

        @Override
        public AlertDialog.Builder setPositiveButton(int textId, OnClickListener listener) {
            return super.setPositiveButton(textId, listener);
        }

        public AlertDialog.Builder setPositiveButton(CharSequence text, final ColorListener colorListener) {
            this.colorListener = colorListener;
            OnClickListener onClickListener = new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    colorListener.onColorSelected(colorPickerView.getColorEnvelope());
                    colorPickerView.saveData();
                }
            };

            return super.setPositiveButton(text, onClickListener);
        }
    }

    /**
     * prevent set views
     */
    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view) {
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
    }

    @Override
    public void setView(View view) {
    }

    @Override
    public void setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
    }

}
