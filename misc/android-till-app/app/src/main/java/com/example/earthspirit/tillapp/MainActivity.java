package com.example.earthspirit.tillapp;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.effect.Effect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.MonthDay;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //var
    EditText etTotal, etEFTPOS, etFiftyVal, etTwentyVal;
    Button btGetCash, btRound;
    TextView Cash,Final, Remaining;
    Double RoundedTotal, EFTPOS;

    private String Months[] = new String[]{"Jan","Feb","Mar","April","May","June","July","Aug","Sept","Oct","Nov","Dec"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTotal = findViewById(R.id.edit_total);
        etEFTPOS = findViewById(R.id.edit_eftpos);
        etFiftyVal = findViewById(R.id.edit_50s);
        etTwentyVal = findViewById(R.id.edit_20s);


        btGetCash = findViewById(R.id.btn_getcash);
        btRound = findViewById(R.id.btn_round);

        Cash = findViewById(R.id.tv_cash);
        Final = findViewById(R.id.tv_final);
        Remaining = findViewById(R.id.tv_whatsleft);



        btGetCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etTotal.getText().toString())){
                    //Total is empty
                    Toast.makeText(MainActivity.this, "Please enter the Total Value", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(etEFTPOS.getText().toString())){
                    //eftpos is empty
                    Toast.makeText(MainActivity.this, "Please enter the EFTPOS Value", Toast.LENGTH_SHORT).show();

                }
                else{
                    Double Total = Double.parseDouble(etTotal.getText().toString());
                    EFTPOS = Double.parseDouble(etEFTPOS.getText().toString());
                    if (Total < EFTPOS){
                        Toast.makeText(MainActivity.this, "INPUT ERROR - EFTPOS can not be larger than the Total amount", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String CashVal = String.valueOf(Total - EFTPOS);

                        Cash.setText(CashVal);
                        btRound.setEnabled(true);
                    }
                }

                //hide kb
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        //round cash amt when clicked
        btRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Double EFTPOS = Double.parseDouble(etEFTPOS.getText().toString());
                    Double CashVal = Double.parseDouble(Cash.getText().toString());
                    Long Rounded = 5*(Math.round(CashVal/5));
                    String FinalVal = String.valueOf(EFTPOS + Rounded.doubleValue());


                    RoundedTotal = Rounded.doubleValue();
                    Cash.setText(String.valueOf(Rounded));
                    Final.setText(FinalVal);
                    Remaining.setEnabled(true);

                //}
            }
        });


        Remaining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if 50 val is empty error msg
                String FiftyVal = etFiftyVal.getText().toString();
                String TwentyVal = etTwentyVal.getText().toString();
                if (TextUtils.isEmpty(FiftyVal)){
                    Toast.makeText(MainActivity.this, "PLease Enter 50S Value", Toast.LENGTH_SHORT).show();
                }
                else{
                    Double RemainAfterFifty;
                    //minus rounded with 50s
                    //if 20s is empty only minus 50
                    if (TextUtils.isEmpty(TwentyVal)) {
                        RemainAfterFifty = RoundedTotal - Double.parseDouble(FiftyVal);
                    }
                    else{   //if both values are present
                        RemainAfterFifty = RoundedTotal - Double.parseDouble(FiftyVal)- Double.parseDouble(TwentyVal);
                    }
                    if (RemainAfterFifty < 0){
                        Toast.makeText(MainActivity.this, "INPUT ERROR - Remainder can not be negative", Toast.LENGTH_SHORT).show();
                    }
                    else
                    Remaining.setText(String.valueOf(RemainAfterFifty));

                    //hide kb
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                }
            }
        });

        Final.setOnClickListener(new View.OnClickListener() { //set text msg
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleMonth,simpleDay;
                String MonthInt,DayString, EndMessage;

                Calendar calendar =Calendar.getInstance();
                simpleMonth = new SimpleDateFormat("MM");
                simpleDay = new SimpleDateFormat("dd ");

                DayString = simpleDay.format(calendar.getTime());
                MonthInt = simpleMonth.format(calendar.getTime());

                EndMessage = DayString+""+ Months[Integer.valueOf(MonthInt)-1] +" - "+Final.getText();

                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData .newPlainText("label", EndMessage);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(MainActivity.this, "Copied to clipboard\n"+ EndMessage, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
