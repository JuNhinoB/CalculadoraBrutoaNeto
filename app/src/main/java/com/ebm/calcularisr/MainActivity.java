package com.ebm.calcularisr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private static final double MESES_EN_ANIO = 12;
    private static final double QUINCENAS_EN_ANIO = 2;
    private static final double SEMANAS_EN_ANIO = 4;
    private double sueldoQuincenal;
    private double sueldoSemanal;
    private double sueldo;
    TableLayout breakdownTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        breakdownTable = (TableLayout)findViewById(R.id.main_tbl_breakdown);
        EditText edtSueldoMensual = findViewById(R.id.edtSueldoMensual);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        Button btnCalcular = findViewById(R.id.btnCalcular);
        TextView tvResultadoBruto = findViewById(R.id.tvBruto);
        TextView tvResultadoISR = findViewById(R.id.tvResultadoISR);
        TextView tvResultadoSeguroSocial = findViewById(R.id.tvResultadoSeguroSocial);
        TextView tvResultadoimpuestos = findViewById(R.id.tvResultadototales);
       // TextView tvResultadosubsidio = findViewById(R.id.tvResultadosubsidio);
        TextView tvResultadoSueldoNeto = findViewById(R.id.tvResultadoSueldoNeto);
        TextView tvebm = findViewById(R.id.tvbyEBM);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Actualizar los valores de sueldo según el RadioButton seleccionado

                String edtSueldo = edtSueldoMensual.getText().toString();
                RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());

                if (edtSueldo.isEmpty()){
                    edtSueldoMensual.setError("Ingresa una cantidad");
                    radioGroup.setVisibility(View.GONE);
                    breakdownTable.setVisibility(View.GONE);

                }else {
                    try {
                        double sueldoMensual = Double.parseDouble(edtSueldoMensual.getText().toString());
                        double sueldoSeleccionado=0;

                        if (radioButton.getId() == R.id.radioButtonMensual) {
                            sueldoSeleccionado = sueldoMensual;
                        } else if (radioButton.getId() == R.id.radioButtonQuincenal) {
                            sueldoSeleccionado = sueldoMensual / QUINCENAS_EN_ANIO;
                        } else if (radioButton.getId() == R.id.radioButtonSemanal) {
                            sueldoSeleccionado = sueldoMensual / SEMANAS_EN_ANIO;
                        }


                        double impuestoSeguroSocial = calcularImpuestoSeguroSocial(sueldoSeleccionado);
                        double isrMensual = calcularISR(sueldoSeleccionado);
                        double impuestoTotales = impuestoSeguroSocial + isrMensual;
                       // double subsidioE = calcularSubsidio(sueldoSeleccionado);
                        double sueldoNeto = sueldoSeleccionado - impuestoSeguroSocial - isrMensual; //+ subsidioE;

                        DecimalFormat decimalFormat = new DecimalFormat("$ #,###,###,###.00");

                        String resultadobruto = "Salario: " + decimalFormat.format(sueldoSeleccionado);
                        String resultadoISRTexto = "ISR (Impuestos sobre la renta): " + decimalFormat.format(isrMensual) + " ";
                        String resultadoSeguroSocialTexto = "IMSS (Instituto Mexicano Seguro Social): " + decimalFormat.format(impuestoSeguroSocial) + " ";
                        String resultadoImpuestosTot = "Deducciones totales: " + decimalFormat.format(impuestoTotales) + " ";
                        // String resultadosubsidio = "Subsidio para el empleo: " + decimalFormat.format(subsidioE) + " ";
                        String resultadoSueldoNetoTexto = "Salario neto a pagar: " + decimalFormat.format(sueldoNeto) + " ";
                        String resultadoEBM = "By EBM Studio";

                        tvResultadoBruto.setText(resultadobruto);
                        tvResultadoISR.setText(resultadoISRTexto);
                        tvResultadoSeguroSocial.setText(resultadoSeguroSocialTexto);
                        tvResultadoimpuestos.setText(resultadoImpuestosTot);
                        // tvResultadosubsidio.setText(resultadosubsidio);
                        tvResultadoSueldoNeto.setText(resultadoSueldoNetoTexto);
                        tvebm.setText(resultadoEBM);

                    }catch (NumberFormatException e){
                        edtSueldoMensual.setError("Error Ingrese un valor numerico valido");
                    }
                }






            }

        });


        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sueldo = edtSueldoMensual.getText().toString();


                if (sueldo.isEmpty()){
                    edtSueldoMensual.setError("Ingresa una cantidad");
                    radioGroup.setVisibility(View.GONE);
                    breakdownTable.setVisibility(View.GONE);

                }else {
                    try {
                        double sueldoMensual = Double.parseDouble(edtSueldoMensual.getText().toString());


                        double impuestoSeguroSocial = calcularImpuestoSeguroSocial(sueldoMensual);
                        double isrMensual = calcularISR(sueldoMensual);
                        double impuestoTotales = impuestoSeguroSocial + isrMensual;
                        // double subsidioE = calcularSubsidio(sueldoMensual);
                        double sueldoNeto = sueldoMensual - impuestoSeguroSocial - isrMensual; //+ subsidioE;

                        DecimalFormat decimalFormat = new DecimalFormat("$ #,###,###,###.00");

                        String resultadobruto = "Salario: " + decimalFormat.format(sueldoMensual);
                        String resultadoISRTexto = "ISR (Impuestos sobre la renta): " + decimalFormat.format(isrMensual) + " ";
                        String resultadoSeguroSocialTexto = "IMSS (Instituto Mexicano Seguro Social): " + decimalFormat.format(impuestoSeguroSocial) + " ";
                        String resultadoImpuestosTot = "Deducciones totales: " + decimalFormat.format(impuestoTotales) + " ";
                        // String resultadosubsidio = "Subsidio para el empleo: " + decimalFormat.format(subsidioE) + " ";
                        String resultadoSueldoNetoTexto = "Salario neto a pagar: " + decimalFormat.format(sueldoNeto) + " ";
                        String resultadoEBM = "By EBM Studio";

                        tvResultadoBruto.setText(resultadobruto);
                        tvResultadoISR.setText(resultadoISRTexto);
                        tvResultadoSeguroSocial.setText(resultadoSeguroSocialTexto);
                        tvResultadoimpuestos.setText(resultadoImpuestosTot);
                        // tvResultadosubsidio.setText(resultadosubsidio);
                        tvResultadoSueldoNeto.setText(resultadoSueldoNetoTexto);
                        tvebm.setText(resultadoEBM);

                        if (edtSueldoMensual.getText().length() > 0){
                            radioGroup.setVisibility(View.VISIBLE);
                            breakdownTable.setVisibility(View.VISIBLE);
                        }else{
                            radioGroup.setVisibility(View.GONE);
                            breakdownTable.setVisibility(View.GONE);
                        }


                    }catch (NumberFormatException e){
                        edtSueldoMensual.setError("Error Ingrese un valor numerico valido");
                    }
                }






            }
        });

    }

    public static double calcularISR(double sueldoMensual) {
        double isrMensual = 0;
        double isrMarginal = 0;

        if (sueldoMensual <= 746.04) {
            isrMarginal = (sueldoMensual - 0.01) * 0.0192;
            isrMensual =  isrMarginal + 0.00;

        } else if (sueldoMensual <= 6332.05) {
            isrMarginal = (sueldoMensual - 746.04) * 0.0640;
            isrMensual =  isrMarginal + 14.32;


        } else if (sueldoMensual <= 11128.01) {
            isrMarginal = (sueldoMensual - 6332.05) * 0.1088;
            isrMensual =  isrMarginal + 371.83;

        } else if (sueldoMensual <= 12935.82) {
            isrMarginal = (sueldoMensual - 11128.01) * 0.16;
            isrMensual =  isrMarginal + 893.63;


        } else if (sueldoMensual <= 15487.71) {
            isrMarginal = (sueldoMensual - 12935.82) * 0.1792;
            isrMensual =  isrMarginal + 1182.88;


        } else if (sueldoMensual <= 31236.49) {
            isrMarginal = (sueldoMensual - 15487.71) * 0.2136;
            isrMensual =  isrMarginal + 1640.18;

        } else if (sueldoMensual <= 49233.00) {
            isrMarginal = (sueldoMensual - 31236.49) * 0.2352;
            isrMensual =  isrMarginal + 5004.12;


        } else if (sueldoMensual <= 93993.90) {
            isrMarginal = (sueldoMensual - 49233.00) * 0.30;
            isrMensual =  isrMarginal + 9236.89;


        } else if (sueldoMensual <= 125325.20) {
            isrMarginal = (sueldoMensual - 93993.90) * 0.32;
            isrMensual =  isrMarginal + 22665.17;


        } else if (sueldoMensual <= 375975.61) {
            isrMarginal = (sueldoMensual - 125325.20) * 0.34;
            isrMensual =  isrMarginal + 32691.18;


        } else if(sueldoMensual >= 375975.61) {
            isrMarginal = (sueldoMensual - 375975.61) * 0.35;
            isrMensual = isrMarginal + 117912.32;

        }

        return isrMensual;
    }

    public static double calcularImpuestoSeguroSocial(double sueldoMensual) {
        double impuestoSeguroSocialMensual = 0; //sueldoMensual * 0.025; // 2.5% de impuesto de seguro social

        if(sueldoMensual <= 137401.00 ){
            impuestoSeguroSocialMensual = sueldoMensual * 0.024;
        }else {
            impuestoSeguroSocialMensual = 3611.00;
        }

        return impuestoSeguroSocialMensual;
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, "+52 443-745-1432", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
    /*    public static double calcularSubsidio(double sueldoMensual){
        double subsiodioEmpleo= 0;

        if (sueldoMensual <= 1768.96){
            subsiodioEmpleo = 407.02;
        }
        else if (sueldoMensual <= 2653.38){
            subsiodioEmpleo = 406.83;
        }

        else if (sueldoMensual <= 3472.84){
            subsiodioEmpleo = 406.62;
        }

        else if (sueldoMensual <= 3537.87){
            subsiodioEmpleo = 392.77;
        }

        else if (sueldoMensual <= 4446.15){
            subsiodioEmpleo = 382.46;
        }

        else if (sueldoMensual <= 4717.18){
            subsiodioEmpleo = 354.23;
        }

        else if (sueldoMensual <= 5335.42){
            subsiodioEmpleo = 324.87;
        }

        else if (sueldoMensual <= 6224.67){
            subsiodioEmpleo = 294.63;
        }

       else if (sueldoMensual <= 7113.90)
            subsiodioEmpleo = 253.54;

        else if (sueldoMensual <= 7382){
            subsiodioEmpleo = 217.61;
        }

        else if (sueldoMensual  >= 7383){
            subsiodioEmpleo = 0.00;
        }
        return subsiodioEmpleo;
    }*/




}