package in.msmartpay.agent.rechargeBillPay;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import in.msmartpay.agent.R;
import in.msmartpay.agent.location.GPSTrackerPresenter;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;
import in.msmartpay.agent.utility.RandomNumber;
import in.msmartpay.agent.utility.Util;

public class BillPayActivity extends BaseActivity implements GPSTrackerPresenter.LocationListener {
    private static final String TAG = BillPayActivity.class.getSimpleName();

    private TextView tv_cn_hint, tv_cn, amtt, tv_amunt_hint, tv_oprator_hint, tv_oprator, tv_dueDate, tv_customerName, tv_dueAmount, tv_service;
    private LinearLayout ll_customer, ll_dueAmount, ll_dueDate;
    private String connectionNo, amountString, Add1;
    private Context context;
    private ProgressDialog pd;
    private String mob = "", agentID, txn_key;
    private String AD1 = "", AD2 = "", AD3 = "", AD4 = "", CN = "", OP = "", operator, op, referenceId = "";
    private String billpay_Url = HttpURL.BILL_PAY;
    //private String gTPin_Url = HttpURL.GetTPinVerified;
    private Dialog d;
    private OperatorListModel operatorCodeModel;
    private String requesdID;
    private Button btn_proceed;
    private int value = 0;
    private double dueAmt = 0;
    private String dueAmt_Req;

    private GPSTrackerPresenter gpsTrackerPresenter = null;
    private boolean isTxnClick = false;
    private boolean isLocationGet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_pay_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Confirm Payment");

        context = BillPayActivity.this;
        gpsTrackerPresenter = new GPSTrackerPresenter(this, this, GPSTrackerPresenter.GPS_IS_ON__OR_OFF_CODE);


        tv_amunt_hint = findViewById(R.id.tv_amunt_hint);
        tv_service = findViewById(R.id.tv_service);
        btn_proceed = findViewById(R.id.btn_proceed);
        tv_cn_hint = findViewById(R.id.tv_cn_hint);
        tv_cn = findViewById(R.id.tv_cn);
        amtt = findViewById(R.id.id_sure_ask_amount);
        tv_oprator_hint = findViewById(R.id.tv_oprator_hint);
        tv_oprator = findViewById(R.id.tv_oprator);

        tv_dueDate = findViewById(R.id.tv_dueDate);
        tv_customerName = findViewById(R.id.tv_customerName);
        tv_dueAmount = findViewById(R.id.tv_dueAmount);
        ll_customer = findViewById(R.id.ll_customer);
        ll_dueAmount = findViewById(R.id.ll_dueAmount);
        ll_dueDate = findViewById(R.id.ll_dueDate);

        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);

        ll_customer.setVisibility(View.GONE);
        ll_dueDate.setVisibility(View.GONE);
        ll_dueAmount.setVisibility(View.GONE);

        Intent intent = getIntent();
        if (intent != null) {
            connectionNo = intent.getStringExtra("CN");
            amountString = intent.getStringExtra("Amt");
            mob = intent.getStringExtra("Mob") == null ? "" : intent.getStringExtra("Mob");
            Add1 = intent.getStringExtra("Add1") == null ? "" : intent.getStringExtra("Add1");
            tv_cn_hint.setText(intent.getStringExtra("CN_hint"));
            operatorCodeModel = getGson().fromJson(intent.getStringExtra(getString(R.string.pay_operator_model)), OperatorListModel.class);
        }
        tv_cn.setText(connectionNo);
        amtt.setText(amountString);
        tv_oprator.setText(operatorCodeModel.getDisplayName());
        tv_service.setText(operatorCodeModel.getService());

        if (operatorCodeModel.getBillFetch().equalsIgnoreCase("Yes")) {
            btn_proceed.setText("Get Bill Details");
        }
        btn_proceed.setOnClickListener(v -> {
            startPaymentProcess();
        });
    }

    private void startPaymentProcess() {
        /*if (Util.isPowerSaveMode(context)) {
            startPayment();
        } else {*/
            if (isLocationGet) {
                startPayment();
            } else {
                if (!isTxnClick) {
                    isTxnClick = true;
                    gpsTrackerPresenter.checkGpsOnOrNot(GPSTrackerPresenter.GPS_IS_ON__OR_OFF_CODE);
                }
            }
        //}
    }

    private void startPayment() {
        if (operatorCodeModel.getBillFetch().equalsIgnoreCase("Yes") && value == 0) {
            submitPayment("ViewBill");
        } else {
            if (value == 1) {
                if (dueAmt > 0)
                    submitPayment("Pay");
                else
                    Toast.makeText(context, "Due amount is greater than 0", Toast.LENGTH_LONG).show();

            } else {
                submitPayment("Pay");
            }
        }
    }

    private void submitPayment(final String reqType) {
        if (requesdID == null)
            requesdID = RandomNumber.getTranId_20(agentID);

        if (reqType.equalsIgnoreCase("ViewBill"))
            pd = ProgressDialog.show(context, "", "Fetching your bill details.....", true);
        else
            pd = ProgressDialog.show(context, "", "Your payment request is processing.Please wait...", true);
        pd.setCancelable(true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("agent_id", agentID);
            jsonRequest.put("txn_key", txn_key);
            jsonRequest.put("ST", operatorCodeModel.getService());
            jsonRequest.put("service", operatorCodeModel.getService());
            jsonRequest.put("OP", operatorCodeModel.getOpCode());
            jsonRequest.put("OPName", operatorCodeModel.getOperatorName());
            jsonRequest.put("reqType", reqType);
            jsonRequest.put("CN", connectionNo);
            jsonRequest.put("AD1", AD1);
            jsonRequest.put("AD2", AD2);
            jsonRequest.put("AD3", AD3);
            jsonRequest.put("AD4", mob);
            jsonRequest.put("REQUEST_ID", requesdID);
            jsonRequest.put("reference_id", referenceId);
            if (dueAmt > 0) {
                jsonRequest.put("AMT", dueAmt_Req);
            } else {
                jsonRequest.put("AMT", amountString);
            }
            jsonRequest.put("latitude", Util.LoadPrefData(getApplicationContext(), Keys.LATITUDE));
            jsonRequest.put("longitude", Util.LoadPrefData(getApplicationContext(), Keys.LONGITUDE));
            jsonRequest.put("power_mode", Util.LoadPrefBoolean(context, Keys.POWER_MODE));
            jsonRequest.put("ip", Util.getIpAddress(context));
            L.m2("called url", billpay_Url);
            L.m2("request", jsonRequest.toString());

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, billpay_Url,
                    jsonRequest, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject data) {
                    try {
                        pd.dismiss();

                        L.m2("response", data + "");

                        if (data.get("response-code") != null && data.get("response-code").equals("0")) {
                            if (value == 0 && reqType.equalsIgnoreCase("ViewBill")) {
                                value = 1;
                                tv_customerName.setText(data.getString("customername"));
                                tv_dueDate.setText(data.getString("duedate"));
                                tv_dueAmount.setText(data.getString("dueamount"));
                                amtt.setText(data.getString("dueamount"));
                                tv_amunt_hint.setText("Due Amount");
                                dueAmt = data.getDouble("dueamount");
                                dueAmt_Req = data.getString("dueamount");
                                btn_proceed.setText("Proceed to pay");
                                ll_customer.setVisibility(View.VISIBLE);
                                ll_dueDate.setVisibility(View.VISIBLE);
                                referenceId = data.getString("reference_id");
                                // ll_dueAmount.setVisibility(View.VISIBLE);
                            } else {

                                Intent in = new Intent();
                                in.setClass(context, SuccessDetailActivity.class);
                                in.putExtra("responce", data.get("response-message").toString());
                                in.putExtra("mobileno", connectionNo);
                                in.putExtra("requesttype", "Electricity");
                                in.putExtra("operator", operatorCodeModel.getDisplayName());
                                in.putExtra("amount", amtt.getText().toString());
                                startActivity(in);
                                finish();
                            }
                        } else if (data.get("response-code") != null && data.get("response-code").equals("1")) {
                            Toast.makeText(context, (String) data.get("response-message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Unable To Process Your Request. Please try later", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();

                }
            });
            getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(context).addToRequsetque(objectRequest);


        } catch (Exception ex) {
            L.m2("ex", ex.getLocalizedMessage());
            Toast.makeText(context, "Invalid Connection No. Or Amount", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPSTrackerPresenter.GPS_IS_ON__OR_OFF_CODE && resultCode == RESULT_OK) {
            gpsTrackerPresenter.onStart();
        }
    }
    //--------------------------------------------GPS Tracker--------------------------------------------------------------

    @Override
    public void onLocationFound(Location location) {
        isLocationGet = true;
        gpsTrackerPresenter.stopLocationUpdates();
        if (isTxnClick) {
            isTxnClick = false;
            startPaymentProcess();
        }
    }

    @Override
    public void locationError(String msg) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        gpsTrackerPresenter.onStart();
    }

//--------------------------------------------End GPS Tracker--------------------------------------------------------------


    @Override
    protected void onDestroy() {
        super.onDestroy();
        gpsTrackerPresenter.onPause();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
