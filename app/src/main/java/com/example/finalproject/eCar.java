package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class eCar extends AppCompatActivity {
    private Spinner dropDownBrand, dropDownModel, dropDownCondition;
    private String [] list1=new String[4];
    private Button BtnOk,BtnCancel;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecar);

        setBtn();
    }

    private void setBtn() {
        dropDownBrand=(Spinner) findViewById(R.id.dropdownBrand); //set up drop down menu for the car brand
        String [] list=new String[] {"Select Brand","Tesla","Vw","BMW"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(eCar.this, android.R.layout.simple_spinner_dropdown_item, list);
        dropDownBrand.setAdapter(adapter);
        dropDownBrand.setAdapter(adapter);

        dropDownModel=(Spinner) findViewById(R.id.dropdownModel); //set up Drop down Menu based on result of drop down menu of the brand
        dropDownBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //https://stackoverflow.com/questions/11322642/i-have-an-error-setonitemclicklistener-cannot-be-used-with-a-spinner-what-is-w
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                        String[] list2 = new String[]{"Select Model", "Model1", "Model2", "Model3","Model4"};
                                                          if(dropDownBrand.getSelectedItem()==null)
                                                            {     dropDownBrand.setAdapter(SetAdapter(list2));}else {
                                                                if(dropDownBrand.getSelectedItem().toString()=="BMW")
                                                                {    list2[4] = "i3";
                                                                    list2[1] = "IX3";
                                                                    list2[2] = "i3s";
                                                                    list2[3] = "i8";}
                                                                if(dropDownBrand.getSelectedItem().toString()=="Tesla"){
                                                                    list2[1] = "Model3";
                                                                    list2[2] = "ModelX";
                                                                    list2[3] = "ModelS";
                                                                    list2[4] = "ModelX";
                                                                }
                                                                if(dropDownBrand.getSelectedItem().toString()=="Vw"){
                                                                    list2[4] = "ID.3";
                                                                    list2[1] = "ID.4";
                                                                    list2[2] = "eGolf";
                                                                    list2[3] = "Passat GTE";
                                                                }

                                                                dropDownModel.setAdapter(SetAdapter(list2));
                                                            }
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> parent) {
                                                        String[] list2 = new String[]{"Select Model", "Model1", "Model2", "Model3","Model4"};
                                                           dropDownModel.setAdapter(SetAdapter(list2));
                                                    }
                                                });




        dropDownCondition=(Spinner) findViewById(R.id.dropdownCondition);
        String [] list3=new String[] {"Select Condition","5%","20%","35%"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list3);
        dropDownCondition.setAdapter(adapter3);

        BtnOk=(Button) findViewById(R.id.BtnOk);
        BtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent=new Intent(getBaseContext(), Summary.class);
                    startActivity(intent);


            }
        });

        BtnCancel=(Button) findViewById(R.id.BtnCancel);
        BtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(), Veritaps.class);
                startActivity(intent);
            }
        });

    }

    private ArrayAdapter SetAdapter(String [] list2) {
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list2);
        return adapter1;
    }
}
