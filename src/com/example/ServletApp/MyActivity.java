package com.example.ServletApp;
/**
 * Called when the activity is first created.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/** Demonstrates the use of JSON for communicating with a remote HTTP server.
 *  Note that the JSON version that is built into Android is a bit obsolete.
 *  In particular, it lacks a JSONObject constructor that lets you pass a bean.
 *  So, on the Android side we use a Map instead, but the server-side code uses the simpler
 *  and more modern constructor.
 *  <p>
 *  From <a href="http://www.coreservlets.com/android-tutorial/">
 *  the coreservlets.com Android programming tutorial series</a>.
 */
public class MyActivity extends Activity {

    private EditText mBaseUrl, mLoanAmount, mInterestRate, mLoanPeriod;
    private TextView mMontlyPaymentResult, mTotalPaymentsResult;

    /** Initializes the app when it is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mBaseUrl = (EditText)findViewById(R.id.base_url);
        mLoanAmount = (EditText)findViewById(R.id.loan_amount);
        mInterestRate = (EditText)findViewById(R.id.interest_rate);
        mLoanPeriod = (EditText)findViewById(R.id.loan_period);
        mMontlyPaymentResult = (TextView)findViewById(R.id.monthly_payment_result);
        mTotalPaymentsResult = (TextView)findViewById(R.id.total_payments_result);
        System.out.println("dfdsfgf");
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            System.out.println("IN THIS MOTHER FUCKER !!!!!!!!!!!!!!!!!!!");
            String jsonString="";

            try {
                jsonString = HttpUtils.urlContentPost(urls[0], "loanInputs",urls[1]);
            }
            catch (IOException e){}
            System.out.println("Inside doInBackground");
            return jsonString;

        }
        protected void onPostExecute(String result){
            System.out.println("IN THIS MOTHER FUCKER AGAIN!!!!!!!!!!!!!!!!!!!");
            JSONObject jsonResult = null;
            try {
                jsonResult=new JSONObject(result);
                mMontlyPaymentResult.setText(jsonResult.getString("formattedMonthlyPayment"));
                mTotalPaymentsResult.setText(jsonResult.getString("formattedTotalPayments"));
                mLoanAmount.setText(jsonResult.getString("loanAmount"));
                mInterestRate.setText(jsonResult.getString("annualInterestRateInPercent"));
                mLoanPeriod.setText(jsonResult.getString("loanPeriodInMonths"));
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void showLoanPayments(View clickedButton) {
        String baseUrl = mBaseUrl.getText().toString();
        String loanAmount = mLoanAmount.getText().toString();
        String interestRate = mInterestRate.getText().toString();
        String loanPeriod = mLoanPeriod.getText().toString();
        LoanInputs inputs = new LoanInputs(loanAmount, interestRate, loanPeriod);
        JSONObject inputsJson = new JSONObject(inputs.getInputMap());
        new HttpAsyncTask().execute(baseUrl,inputsJson.toString());
        // try {
        ////////////////////modify asyns task


//        } catch (ClientProtocolException e) {
//            mMontlyPaymentResult.setText("Illegal base URL");
//        mTotalPaymentsResult.setText("");
//        } catch (IOException e) {
//            mMontlyPaymentResult.setText("Server error: " + e);
//            mTotalPaymentsResult.setText("");
//        } catch(JSONException jse) {
//            mMontlyPaymentResult.setText("JSON exception");
//            mTotalPaymentsResult.setText("");
//        }

    }
}
