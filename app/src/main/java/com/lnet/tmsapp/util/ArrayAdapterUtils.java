package com.lnet.tmsapp.util;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.lnet.tmsapp.R;

import java.util.List;

/**
 * Created by admin on 2015/7/10.
 */
public class ArrayAdapterUtils {
    public static void adapter(final Spinner spinner,List list,Context context, final EditText editText){
        ArrayAdapter<DataItem> myaAdapter = new ArrayAdapter<>(context, R.layout.list_item1, list);
        spinner.setAdapter(myaAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String textValue = ((DataItem) spinner.getSelectedItem()).getTextValue();
                editText.setText(textValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });



    }
}


